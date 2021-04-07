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


# https://towardsdatascience.com/sentiment-analysis-for-text-with-deep-learning-2f0a0c6472b5
# https://towardsdatascience.com/sentiment-analysis-using-lstm-step-by-step-50d074f09948


def run():
    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\dictionary.txt",
        "r",
    ) as f:
        phrases = f.read()
    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\sentiment_labels.txt",
        "r",
    ) as f:
        labels = f.read()

    print("running")

    # convert into dict of phrase ID | phrase
    index_to_phrase_dict = {}
    phrase_to_index_dict = {}

    phrases_split = phrases.split("\n")
    # print(phrases[:50])
    # print(phrases_split[4])
    count = 0
    total = len(phrases_split)
    for phr in phrases_split:
        # print(count / total)
        # print("phriew: ", phriew)
        phrase_components = phr.split("|")
        # print(phrase_components)
        try:
            index = phrase_components[1]
            phrase = phrase_components[0]

            processed_phrase = clean_filter_lemma_mini(phrase)
            # processed_phrase = lemma(phrase)
            # processed_phrase = phrase

            # print("phrase: ", phrase)
            # print("processed_phrase: ", processed_phrase)
            index_to_phrase_dict[int(index)] = processed_phrase
            # print(index)
            phrase_to_index_dict[processed_phrase] = int(index)
        except IndexError:
            # this is the newline at the end of the txt
            break
        except ValueError:
            print(" WHU WHWUH WUN UN W")
            print(phrase_components[1])

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
    print("done splitting phrases !!!!!!!!!!!!!!!!!: ", len(index_to_phrase_dict))

    sentiment_dict = {}

    sentiment_split = labels.split("\n")
    # print(phrases[:50])
    # print(phrases_split[4])
    for lab in sentiment_split:
        # print("phriew: ", phriew)
        sentiment_components = lab.split("|")
        # print(phrase_components)
        try:
            index = sentiment_components[0]
            sentiment = sentiment_components[1]
            if (sentiment == "sentiment values"):
                print('skip header')
            else:
                sentiment_dict[int(index)] = float(sentiment)
        except IndexError:
            # this is the newline at the end of the txt
            break
        except ValueError:
            print('should not be here')
            # print(sentiment_components[1])
            break
    # print(sentiment_dict[10])

    # sentiment_map = {}

    # for key in index_to_phrase_dict.keys():
    #     sentiment_map[index_to_phrase_dict[key]] = sentiment_dict[key]

    # print(sentiment_map["the"])

    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\original_rt_snippets.txt",
        "r",
    ) as f:
        reviews = f.read()

    reviews_split = reviews.split("\n")

    print("reviews_split: ", reviews_split[1])
    reviews_split_cleaned = []

    for rev in reviews_split:
        reviews_split_cleaned.append(clean_filter_lemma_mini(rev))
        # reviews_split_cleaned.append(rev)

    print(len(reviews_split_cleaned))

    # print(reviews_split_cleaned)
    print("done splitting reviews !!!!!!!!!!!!!!!!!")

    def try_tokenize(word):
        # print("trying")
        try:
            return phrase_to_index_dict[word]
        except:
            # return -1
            return 0

    reviews_tokenized = []

    count_reach = 0
    total_count = 0
    # for rev in reviews_split_cleaned:
    print("hoho", len(phrase_to_index_dict))
    for rev in index_to_phrase_dict.values():
        total_count += 1

        # print(rev)
        token_so_far = []
        index = 0
        words = rev.split(" ")
        word = words[index]
        # print("rev: ", rev)
        # print("words: ", words)
        # print("len: ", len(words))
        while index < len(words):  # until done for all words
            try:
                while (
                    try_tokenize(word) == -1
                ):  # until find a valid prase which has a phrase id

                    index += 1
                    word = word + " " + words[index]
            except IndexError:
                # print(word)
                something = 0
            # phrase with phrase id has been found
            token_so_far.append(try_tokenize(word))
            # print(word)
            # print(index)
            # print(try_tokenize(word))
            index += 1

            try:
                word = words[index]
            except IndexError:
                # reached the end of the rev
                # print(word)
                # break
                hi = 0

        if token_so_far == [-1]:
            print("oh no")
            print(words)
            # break
        try:
            # token_so_far.remove(-1)

            reviews_tokenized.append(token_so_far)
            count_reach += 1
        except:
            count_reach += 1
            reviews_tokenized.append(token_so_far)

        # print(token_so_far)
        # reviews_tokenized.append(token_so_far)
        # break

    print("count reach: ", count_reach)
    print("total: ", total_count)
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


def normalize_length(target_length, twod_arr):
    normalized_arr = []

    for arr in twod_arr:
        if len(arr) > target_length:
            truncated_arr = arr[0:target_length]
            normalized_arr.append(truncated_arr)
        elif len(arr) < target_length:
            padded_arr = arr + [0] * (target_length - len(arr))
            normalized_arr.append(padded_arr)
        else:
            normalized_arr.append(arr)

    # print(normalized_arr)
    return normalized_arr


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
