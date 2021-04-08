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
from clean_review import clean_filter_lemma_mini


def try_tokenize(word, phrase_to_index_dict):
    # print("trying")
    try:
        return phrase_to_index_dict[word]
    except:
        # return -1
        return 0


def tokenize_sentence(rev, phrase_to_index_dict):
    # print(rev)
    token_so_far = []
    index = 0
    words = rev.split(" ")
    # word = words[index]
    # print("rev: ", rev)
    # print("words: ", words)
    # print("len: ", len(words))
    # while index < len(words):  # until done for all words
    #     try:
    #         while (
    #             try_tokenize(word, phrase_to_index_dict) == -1
    #         ):  # until find a valid prase which has a phrase id

    #             index += 1
    #             word = word + " " + words[index]
    #     except IndexError:
    #         # print(word)
    #         something = 0
    #     # phrase with phrase id has been found
    #     token_so_far.append(try_tokenize(word, phrase_to_index_dict))
    #     # print(word)
    #     # print(index)
    #     # print(try_tokenize(word))
    #     index += 1

    #     try:
    #         word = words[index]
    #     except IndexError:
    #         # reached the end of the rev
    #         # print(word)
    #         # break
    #         hi = 0

    # while index < len(words):  # until done for all words
    #     try:
    #         word = words[index]
    #     except IndexError:
    #         print("seriously can't find the word???")
    #         print(word)
    #         something = 0
    #         break
    #     token_so_far.append(try_tokenize(word, phrase_to_index_dict))
    #     # print(word)
    #     # print(index)
    #     # print(try_tokenize(word))
    #     index += 1
    # return token_so_far

    while index < len(words):  # until done for all words
        word = words[index]

        token = try_tokenize(word, phrase_to_index_dict)
        if token != 0:
            token_so_far.append(token)
        # print(word)
        # print(index)
        # print(try_tokenize(word))
        index += 1
    return token_so_far


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
