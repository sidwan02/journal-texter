import torch
import torch.nn as nn
import numpy as np


class TrainModel():
    def __init__(self, model, batch_size, train_loader, dev_loader, target_length):
        super().__init__()
        self.model = model
        self.batch_size = batch_size
        self.train_loader = train_loader
        self.dev_loader = dev_loader
        self.target_length = target_length

        train_on_gpu = torch.cuda.is_available()

        # loss and optimization functions
        lr = 0.001

        criterion = nn.BCELoss()
        optimizer = torch.optim.Adam(model.parameters(), lr=lr)
        # optimizer = torch.optim.SGD(model.parameters(), lr=lr)

        # training params

        epochs = 4  # 3-4 is approx where I noticed the validation loss stop decreasing

        counter = 0
        print_every = 1
        clip = 5  # gradient clipping

        # move model to GPU, if available
        # if(train_on_gpu):
        #     model.cuda()

        model.train()
        # train for some number of epochs
        for e in range(epochs):
            # initialize hidden state
            h = model.init_hidden(batch_size)

            # batch loop
            for inputs, labels in train_loader:
                counter += 1

                print(counter)

                # if(train_on_gpu):
                #     inputs, labels = inputs.cuda(), labels.cuda()

                # Creating new variables for the hidden state, otherwise
                # we'd backprop through the entire training history
                h = tuple([each.data for each in h])

                # zero accumulated gradients
                # model.zero_grad()
                optimizer.zero_grad()

                # get the output from the model
                inputs = inputs.type(torch.LongTensor)
                output, h = model(inputs, h)

                # calculate the loss and perform backprop
                # print(output.squeeze().shape)
                # print(labels.shape)
                loss = criterion(output.squeeze(), labels.float())

                # loss = criterion(torch.sigmoid(
                #     output.squeeze()), labels.float())

                loss.backward()
                # `clip_grad_norm` helps prevent the exploding gradient problem in RNNs / LSTMs.
                nn.utils.clip_grad_norm_(model.parameters(), clip)
                optimizer.step()

                # loss stats
                if counter % print_every == 0:
                    # Get validation loss
                    val_h = model.init_hidden(batch_size)
                    val_losses = []
                    model.eval()
                    for inputs, labels in dev_loader:

                        # Creating new variables for the hidden state, otherwise
                        # we'd backprop through the entire training history
                        val_h = tuple([each.data for each in val_h])

                        # if(train_on_gpu):
                        #     inputs, labels = inputs.cuda(), labels.cuda()

                        inputs = inputs.type(torch.LongTensor)

                        if((inputs.shape[0], inputs.shape[1]) != (batch_size, target_length)):
                            print('Validation - Input Shape Issue: ', inputs.shape)
                            continue

                        output, val_h = model(inputs, val_h)
                        val_loss = criterion(output.squeeze(), labels.float())

                        val_losses.append(val_loss.item())

                    model.train()
                    print("Epoch: {}/{}...".format(e+1, epochs),
                          "Step: {}...".format(counter),
                          "Loss: {:.6f}...".format(loss.item()),
                          "Val Loss: {:.6f}".format(np.mean(val_losses))
                          )
