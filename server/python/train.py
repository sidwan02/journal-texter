# Libraries

import matplotlib.pyplot as plt
import pandas as pd
import torch

# Preliminaries

# from torchtext.data import Field, TabularDataset, BucketIterator

# Models

import torch.nn as nn
from torch.nn.utils.rnn import pack_padded_sequence, pad_packed_sequence

# Training

import torch.optim as optim


class Train(nn.Module):
    def __init__(self, embedding_dim, train_loader, test_loader, dev_loader):
        super(Train, self).__init__()

        model = SentimentLSTM(embedding_dim=vocab_size).to(device)

        optimizer = optim.Adam(model.parameters(), lr=0.001)

        train(model=model, optimizer=optimizer, num_epochs=10)

        self.train_loader = train_loader
        self.test_loader = test_loader
        self.dev_loader = dev_loader


# Save and Load Functions
def save_checkpoint(save_path, model, optimizer, valid_loss):

    if save_path == None:
        return

    state_dict = {'model_state_dict': model.state_dict(),
                  'optimizer_state_dict': optimizer.state_dict(),
                  'valid_loss': valid_loss}

    torch.save(state_dict, save_path)
    print(f'Model saved to ==> {save_path}')


def load_checkpoint(load_path, model, optimizer):

    if load_path == None:
        return

    state_dict = torch.load(load_path, map_location=device)
    print(f'Model loaded from <== {load_path}')

    model.load_state_dict(state_dict['model_state_dict'])
    optimizer.load_state_dict(state_dict['optimizer_state_dict'])

    return state_dict['valid_loss']


def save_metrics(save_path, train_loss_list, valid_loss_list, global_steps_list):

    if save_path == None:
        return

    state_dict = {'train_loss_list': train_loss_list,
                  'valid_loss_list': valid_loss_list,
                  'global_steps_list': global_steps_list}

    torch.save(state_dict, save_path)
    print(f'Model saved to ==> {save_path}')


def load_metrics(load_path):

    if load_path == None:
        return

    state_dict = torch.load(load_path, map_location=device)
    print(f'Model loaded from <== {load_path}')

    return state_dict['train_loss_list'], state_dict['valid_loss_list'], state_dict['global_steps_list']


# Training Function

def train(model,
          optimizer,
          train_loader,
          valid_loader,
          criterion=nn.BCELoss(),
          num_epochs=5,
          eval_every=10,
          file_path='target',
          best_valid_loss=float("Inf")):
    # def train(model,
    #           optimizer,
    #           criterion=nn.BCELoss(),
    #           train_loader=train_iter,
    #           valid_loader=valid_iter,
    #           num_epochs=5,
    #           eval_every=len(train_iter) // 2,
    #           file_path=destination_folder,
    #           best_valid_loss=float("Inf")):

    # initialize running values
    running_loss = 0.0
    valid_running_loss = 0.0
    global_step = 0
    train_loss_list = []
    valid_loss_list = []
    global_steps_list = []

    # training loop
    model.train()
    for epoch in range(num_epochs):
        for (labels, (title, title_len), (text, text_len), (titletext, titletext_len)), _ in train_loader:
            labels = labels.to(device)
            titletext = titletext.to(device)
            titletext_len = titletext_len.to(device)
            output = model(titletext, titletext_len)

            loss = criterion(output, labels)
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            # update running values
            running_loss += loss.item()
            global_step += 1

            # evaluation step
            if global_step % eval_every == 0:
                model.eval()
                with torch.no_grad():
                    # validation loop
                    for (labels, (title, title_len), (text, text_len), (titletext, titletext_len)), _ in valid_loader:
                        labels = labels.to(device)
                        titletext = titletext.to(device)
                        titletext_len = titletext_len.to(device)
                        output = model(titletext, titletext_len)

                        loss = criterion(output, labels)
                        valid_running_loss += loss.item()

                # evaluation
                average_train_loss = running_loss / eval_every
                average_valid_loss = valid_running_loss / len(valid_loader)
                train_loss_list.append(average_train_loss)
                valid_loss_list.append(average_valid_loss)
                global_steps_list.append(global_step)

                # resetting running values
                running_loss = 0.0
                valid_running_loss = 0.0
                model.train()

                # print progress
                print('Epoch [{}/{}], Step [{}/{}], Train Loss: {:.4f}, Valid Loss: {:.4f}'
                      .format(epoch+1, num_epochs, global_step, num_epochs*len(train_loader),
                              average_train_loss, average_valid_loss))

                # checkpoint
                if best_valid_loss > average_valid_loss:
                    best_valid_loss = average_valid_loss
                    save_checkpoint(file_path + '/model.pt',
                                    model, optimizer, best_valid_loss)
                    save_metrics(file_path + '/metrics.pt', train_loss_list,
                                 valid_loss_list, global_steps_list)

    save_metrics(file_path + '/metrics.pt', train_loss_list,
                 valid_loss_list, global_steps_list)
    print('Finished Training!')
