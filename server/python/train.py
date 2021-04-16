import matplotlib.pyplot as plt
import pandas as pd
import torch
from model import SentimentRNN
import numpy as np
from clean_review import clean_filter_lemma_mini
from data_processing import *
import torch.nn as nn
from torch.nn.utils.rnn import pack_padded_sequence, pad_packed_sequence

is_cuda = torch.cuda.is_available()

if is_cuda:
    device = torch.device("cuda")
    print("GPU is available")
else:
    device = torch.device("cpu")
    print("GPU not available, CPU used")


class Train(nn.Module):
    def __init__(self, vocab_size, train_loader, test_loader, batch_size):
        super(Train, self).__init__()

        # hyperparams
        num_layers = 2
        vocab_size = vocab_size + 1  # extra 1 for padding
        embedding_dim = 64
        output_dim = 1
        hidden_dim = 256

        model = SentimentRNN(num_layers, vocab_size, hidden_dim,
                             embedding_dim, drop_prob=0.5)

        # moving to gpu
        model.to(device)

        print(model)

        # more hyperparams
        lr = 0.001
        criterion = nn.BCELoss()
        optimizer = torch.optim.Adam(model.parameters(), lr=lr)

        def acc(pred, label):
            # get the accuracy in terms of whether target and true are both positive (>0.5)
            # or negative (< 0.5)
            pred = torch.round(pred.squeeze())
            return torch.sum(pred == label.squeeze()).item()

        clip = 5
        epochs = 5
        valid_loss_min = np.Inf

        epoch_tr_loss, epoch_vl_loss = [], []
        epoch_tr_acc, epoch_vl_acc = [], []

        for epoch in range(epochs):
            train_losses = []
            train_acc = 0.0
            model.train()
            # initialize hidden state
            h = model.init_hidden(batch_size)
            for inputs, labels in train_loader:

                inputs, labels = inputs.to(device), labels.to(device)

                h = tuple([each.data for each in h])

                model.zero_grad()
                output, h = model(inputs, h)

                loss = criterion(output.squeeze(), labels.float())
                loss.backward()
                train_losses.append(loss.item())

                accuracy = acc(output, labels)
                train_acc += accuracy

                nn.utils.clip_grad_norm_(model.parameters(), clip)
                optimizer.step()

            test_h = model.init_hidden(batch_size)
            test_losses = []
            test_acc = 0.0
            model.eval()
            for inputs, labels in test_loader:
                test_h = tuple([each.data for each in test_h])

                inputs, labels = inputs.to(device), labels.to(device)

                output, test_h = model(inputs, test_h)
                val_loss = criterion(output.squeeze(), labels.float())

                test_losses.append(val_loss.item())

                accuracy = acc(output, labels)
                test_acc += accuracy

            # maintain epoch state
            epoch_train_loss = np.mean(train_losses)
            epoch_val_loss = np.mean(test_losses)
            epoch_train_acc = train_acc/len(train_loader.dataset)
            epoch_test_acc = test_acc/len(test_loader.dataset)
            epoch_tr_loss.append(epoch_train_loss)
            epoch_vl_loss.append(epoch_val_loss)
            epoch_tr_acc.append(epoch_train_acc)
            epoch_vl_acc.append(epoch_test_acc)
            print(f'Epoch {epoch+1}')
            print(
                f'train_loss : {epoch_train_loss} val_loss : {epoch_val_loss}')
            print(
                f'train_accuracy : {epoch_train_acc*100} test_accuracy : {epoch_test_acc*100}')
            if epoch_val_loss <= valid_loss_min:
                print('Validation loss decreased ({:.6f} --> {:.6f}).'.format(
                    valid_loss_min, epoch_val_loss))
                valid_loss_min = epoch_val_loss
            print(25*'==')

        # save model upon training
        print("traning complete!")
        torch.save(model.state_dict(), r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\server\model" + "\sentiment_model.pth")
