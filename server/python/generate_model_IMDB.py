# -*- coding: utf-8 -*-
# class Hello:
# #     __gui = None
# #
#     def __init__(self):
#         # self.__gui = gui
#

from torch.utils.data import DataLoader, TensorDataset
import en_core_web_sm
import numpy as np
import spacy
import itertools
import emoji
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
import string
import re
import os
import torch
from train import Train
from clean_review import clean_filter_lemma_mini
from data_processing import *
from extract_data_IMDB import *


# https://towardsdatascience.com/sentiment-analysis-for-text-with-deep-learning-2f0a0c6472b5
# https://towardsdatascience.com/sentiment-analysis-using-lstm-step-by-step-50d074f09948


def run():

    review_to_sentiment_dict = get_review_sentiment_dict()

    vocabulary = generate_vocabulary(review_to_sentiment_dict)

    count = 0
    for a in vocabulary.keys():
        if count == 0:
            print(a)
            break

    # print(vocabulary.keys()[0])

    reviews_tokenized = []

    count_reach = 0
    total_count = 0
    # for rev in reviews_split_cleaned:
    print("hoho", len(vocabulary))
    for rev in review_to_sentiment_dict.keys():
        total_count += 1

        token = tokenize_sentence(rev, vocabulary)

        if token == [-1]:
            print("oh no")
            print(words)
            # break
        try:
            # token_so_far.remove(-1)

            reviews_tokenized.append(token)
            # count_reach += 1
        except:
            # count_reach += 1
            reviews_tokenized.append(token)

    # print(token_so_far)
    # reviews_tokenized.append(token_so_far)
    # break

    # print("count reach: ", count_reach)
    # print("total: ", total_count)
    print(len(reviews_tokenized))

    features = normalize_length(200, reviews_tokenized)
    print("features length: ", len(features))
    # print(features)

    train_x, train_y, test_x, test_y = split_data(
        features, review_to_sentiment_dict, 0.99)

    # print(train_x[:10])
    # print(train_y[:10])
    # print(test_x[:10])
    # print(test_y[:10])

    # create Tensor datasets
    train_data = TensorDataset(torch.from_numpy(
        np.array(train_x)), torch.from_numpy(np.array(train_y)))
    # dev_data = TensorDataset(torch.from_numpy(
    #     np.array(dev_x)), torch.from_numpy(np.array(dev_y)))
    test_data = TensorDataset(torch.from_numpy(
        np.array(test_x)), torch.from_numpy(np.array(test_y)))
    # dataloaders
    batch_size = 50
    # make sure to SHUFFLE your data
    # https://discuss.pytorch.org/t/runtimeerror-expected-hidden-0-size-2-20-256-got-2-50-256/38288/11
    train_loader = DataLoader(train_data, shuffle=True,
                              batch_size=batch_size, drop_last=True)
    # dev_loader = DataLoader(dev_data, shuffle=True,
    #                         batch_size=batch_size, drop_last=True)
    test_loader = DataLoader(test_data, shuffle=True,
                             batch_size=batch_size, drop_last=True)

    # obtain one batch of training data
    dataiter = iter(train_loader)
    sample_x, sample_y = dataiter.next()
    print('Sample input size: ', sample_x.size())  # batch_size, target_length
    print('Sample input: \n', sample_x)
    print()
    print('Sample label size: ', sample_y.size())  # batch_size
    print('Sample label: \n', sample_y)

    # Instantiate the model w/ hyperparams
    # vocab_size = len(vocabulary.keys())+1  # +1 for the 0 padding
    vocab_size = len(vocabulary)
    output_size = 1
    # embedding_dim = 400
    embedding_dim = 256
    # hidden_dim = 256
    hidden_dim = 2
    n_layers = 2
    # net = SentimentLSTM(vocab_size, output_size,
    #                     embedding_dim, hidden_dim, n_layers)
    # print(net)

    Train(vocab_size=vocab_size,
          train_loader=train_loader, test_loader=test_loader, batch_size=batch_size)


print("ALL DONE WOOWOWO")


def split_data(features, review_to_sentiment_dict, threshold):
    train_x = []
    train_y = []

    dev_x = []
    dev_y = []

    test_x = []
    test_y = []

    i = 1
    valid_count = 0

    print("features length: ", len(features))
    # print("vocabulary length: ", len(vocabulary.keys()))

    # while i < len(features):
    for sentiment in review_to_sentiment_dict.values():
        try:
            # print("sentiment: ", sentiment)
            if i < threshold * len(features):
                train_x.append(features[i])
                train_y.append(0 if sentiment == "negative" else 1)
            else:
                test_x.append(features[i])
                test_y.append(0 if sentiment == "negative" else 1)
            # print(i)

            i = i + 1
            valid_count = valid_count + 1
        except KeyError:
            print(review_index)
            hilo = 1
            # i = i + 1
            break
        except IndexError:
            print("this is a problem why is it happening tho")
            print("index: ", i)
            break

    # print(dev_y)
    # print(dev_y)
    print("portion considered: ", valid_count / len(features))

    print("train", len(train_x))
    print("train", len(train_y))

    print("dev", len(dev_x))
    print("dev", len(dev_y))

    print("test", len(test_x))
    print("test", len(test_y))

    return (train_x, train_y, test_x, test_y)


run()


def run2(something):
    print(something[0])
