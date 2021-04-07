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


def get_phrase_id_dicts():
    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\dictionary.txt",
        "r",
    ) as f:
        phrases = f.read()

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

    print("done splitting phrases !!!!!!!!!!!!!!!!!: ",
          len(index_to_phrase_dict))
    return (index_to_phrase_dict, phrase_to_index_dict)


def get_sentiment_dict():
    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\sentiment_labels.txt",
        "r",
    ) as f:
        labels = f.read()

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
    return sentiment_dict


def get_reviews():
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

    return reviews_split_cleaned


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
    word = words[index]
    # print("rev: ", rev)
    # print("words: ", words)
    # print("len: ", len(words))
    while index < len(words):  # until done for all words
        try:
            while (
                try_tokenize(word, phrase_to_index_dict) == -1
            ):  # until find a valid prase which has a phrase id

                index += 1
                word = word + " " + words[index]
        except IndexError:
            # print(word)
            something = 0
        # phrase with phrase id has been found
        token_so_far.append(try_tokenize(word, phrase_to_index_dict))
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
