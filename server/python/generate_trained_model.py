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


# https://towardsdatascience.com/sentiment-analysis-for-text-with-deep-learning-2f0a0c6472b5
# https://towardsdatascience.com/sentiment-analysis-using-lstm-step-by-step-50d074f09948


def run():

    index_to_phrase_dict, phrase_to_index_dict = get_phrase_id_dicts()

    count = 0
    for i in index_to_phrase_dict.values():
        # if "gorgeously elaborate continuation of" in i:
        # if ("the sundance film festival has become so buzz obsessed that fans and producers descend upon utah each january to ferret out the next great thing"
        #     in i
        #     ):
        #     print(i)
        # break
        count = count + 1
    # print(index_to_phrase_dict)

    sentiment_dict = get_sentiment_dict()
    # sentiment_map = {}

    # for key in index_to_phrase_dict.keys():
    #     sentiment_map[index_to_phrase_dict[key]] = sentiment_dict[key]

    # print(sentiment_map["the"])

    reviews_split_cleaned = get_reviews()

    reviews_tokenized = []

    count_reach = 0
    total_count = 0
    # for rev in reviews_split_cleaned:
    print("hoho", len(phrase_to_index_dict))
    for rev in index_to_phrase_dict.values():
        total_count += 1

        token = tokenize_sentence(rev, phrase_to_index_dict)

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

    features = normalize_length(50, reviews_tokenized)
    print("features length: ", len(features))
    # print(features)

    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\datasetSplit.txt",
        "r",
    ) as f:
        data_split = f.read()

    index_to_spilt_state_dict = {}
    splitset_tuples = data_split.split("\n")

    for tup in splitset_tuples:
        # print(tup)
        split = tup.split(",")
        try:
            index_to_spilt_state_dict[int(split[0])] = int(split[1])
        except IndexError:
            # reached the end of the splitset
            # print(word)
            break
        except ValueError:
            hahahaha = 10
    print(index_to_spilt_state_dict[100])

    # split_frac = 0.8
    # len_feat = len(features)

    # train_x = features[0 : int(split_frac * len_feat)]
    # train_y = encoded_labels[0 : int(split_frac * len_feat)]
    # remaining_x = features[int(split_frac * len_feat) :]
    # remaining_y = encoded_labels[int(split_frac * len_feat) :]
    # valid_x = remaining_x[0 : int(len(remaining_x) * 0.5)]
    # valid_y = remaining_y[0 : int(len(remaining_y) * 0.5)]
    # test_x = remaining_x[int(len(remaining_x) * 0.5) :]
    # test_y = remaining_y[int(len(remaining_y) * 0.5) :]

    train_x, train_y, test_x, test_y = split_data(
        features, index_to_phrase_dict, sentiment_dict, 0.99)

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
    # vocab_size = len(index_to_phrase_dict.keys())+1  # +1 for the 0 padding
    vocab_size = len(index_to_phrase_dict.keys())
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
          train_loader=test_loader, test_loader=test_loader, batch_size=batch_size)


print("ALL DONE WOOWOWO")


def split_data(features, index_to_phrase_dict, sentiment_dict, threshold):
    train_x = []
    train_y = []

    dev_x = []
    dev_y = []

    test_x = []
    test_y = []

    i = 1
    valid_count = 0

    print("features length: ", len(features))
    print("index_to_phrase_dict length: ", len(index_to_phrase_dict.keys()))

    # while i < len(features):
    for phrase_index in index_to_phrase_dict.keys():
        try:
            sentiment = sentiment_dict[phrase_index]
            # print("sentiment: ", sentiment)
            if phrase_index < threshold * len(index_to_phrase_dict):
                train_x.append(features[i])
                train_y.append(0 if sentiment < 0.5 else 1)
            else:
                test_x.append(features[i])
                test_y.append(0 if sentiment < 0.5 else 1)
            # print(i)

            i = i + 1
            valid_count = valid_count + 1
        except KeyError:
            print(phrase_index)
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
