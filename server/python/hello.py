# -*- coding: utf-8 -*-
# class Hello:
# #     __gui = None
# #
#     def __init__(self):
#         # self.__gui = gui
#

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

# my_path = os.path.join(os.path.dirname(__file__), "..")
# print(my_path)


spice = en_core_web_sm.load()

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
            index_to_phrase_dict[index] = processed_phrase
            phrase_to_index_dict[processed_phrase] = index
        except IndexError:
            # this is the newline at the end of the txt
            break

    count = 0
    for i in index_to_phrase_dict.values():
        # if "gorgeously elaborate continuation of" in i:
        if ("if you love reading and or poetry then by all "
            in i
            ):
            print(i)
        # break
        count = count + 1
    # print(index_to_phrase_dict)
    print("done splitting phrases !!!!!!!!!!!!!!!!!")

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
            sentiment_dict[index] = sentiment
        except IndexError:
            # this is the newline at the end of the txt
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
            return -1

    reviews_tokenized = []

    count_reach = 0
    for rev in reviews_split_cleaned:

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
                break

        if token_so_far == [-1]:
            print("oh no")
            print(words)
            # break
        try:
            token_so_far.remove(-1)

            reviews_tokenized.append(token_so_far)
            count_reach += 1
        except:
            count_reach += 1
            reviews_tokenized.append(token_so_far)

        # print(token_so_far)
        # reviews_tokenized.append(token_so_far)
        # break

    print(count_reach)
    print(len(reviews_tokenized))

    features = normalize_length(10, reviews_tokenized)
    print(len(features))
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

    train_x = []
    train_y = []

    dev_x = []
    dev_y = []

    test_x = []
    test_y = []

    i = 1
    while i <= len(features):
        # print(type(index_to_spilt_state_dict[i]))
        if index_to_spilt_state_dict[i] == 0:
            train_x.append(features[i])
            test_x.append(phrase_to_index_dict[reviews_split_cleaned[i]])
        elif index_to_spilt_state_dict[i] == 1:
            # print(features[i])
            # print(phrase_to_index_dict[reviews_split_cleaned[i]])
            test_x.append(features[i])
            test_y.append(phrase_to_index_dict[reviews_split_cleaned[i]])
        elif index_to_spilt_state_dict[i] == 2:
            dev_x.append(features[i])
            dev_y.append(phrase_to_index_dict[reviews_split_cleaned[i]])
        # else:
        # print("yo wut")
        # print(i)
        i = i + 1

    print(dev_x)

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


def clean(sentence):
    # remove mentions and URLs
    # p.set_options(p.OPT.URL, p.OPT.MENTION, p.OPT.HASHTAG, p.OPT.NUMBER)
    # sentence = p.clean(sentence)
    # replace consecutive non-ASCII characters with a space
    sentence = re.sub(r"[^\x00-\x7F]+", " ", sentence)
    # take care of contractions and stray quote marks
    sentence = re.sub(r"’", "'", sentence)
    # words = sentence.split()
    sentence = re.sub(r":", " ", sentence)
    sentence = re.sub(r"n't", " not", sentence)
    # fix spellings
    sentence = "".join("".join(s)[:2] for _, s in itertools.groupby(sentence))
    # emojis conversion
    sentence = emoji.demojize(sentence)
    sentence = " ".join(sentence.split())
    return sentence


def clean_filter_lemma_mini(sentence):
    sentence = re.sub(r"’", "'", sentence)
    # # words = sentence.split()
    # sentence = re.sub(r":", " ", sentence)
    sentence = re.sub(r"can't", "cannot", sentence)
    sentence = re.sub(r"can not", "cannot", sentence)
    sentence = re.sub(r"ca n't", "cannot", sentence)
    sentence = re.sub(r"n't", " not", sentence)
    sentence = re.sub(r"'s", " is", sentence)
    sentence = re.sub(r"'ve", " have", sentence)
    sentence = re.sub(r"'ll", " will", sentence)
    sentence = re.sub(r"'d", " would", sentence)

    # weird characters
    sentence = re.sub(r"â", "a", sentence)
    # sentence = re.sub(r"––", " ", sentence)
    # stop_words = set(stopwords.words("english"))
    # stray_tokens = [
    #     "amp",
    #     "`",
    #     "``",
    #     "'",
    #     "''",
    #     '"',
    #     "n't",
    #     "I",
    #     "i",
    #     ",00",
    # ]  # stray words
    # punct = r"[{}]".format(string.punctuation)
    punct = r"[^\w\s]".format(string.punctuation)
    sentence = re.sub(punct, " ", sentence)
    sentence = re.sub(r"[0-9]", " ", sentence)
    # # correct spelling mistakes
    # sentence = re.sub(
    #     r'privacy|privcy|privac|privasy|privasee|privarcy|priavcy|privsy', ' privacy ', sentence)
    # sentence = re.sub(r'(^|\s)[a-z]($|\s)', ' ', sentence)
    # sentence = re.sub(r"(^|\s)[a-z][a-z]($|\s)", " ", sentence)
    # word_tokens = word_tokenize(sentence)
    # filter using NLTK library append it to a string
    # filtered_sentence = [w for w in word_tokens if not w in stop_words]
    # filtered_sentence = []
    # looping through conditions
    # for w in word_tokens:
    #     # check tokens against stopwords and punctuations
    #     if (
    #         w not in stop_words
    #         and w not in string.punctuation
    #         and w not in stray_tokens
    #     ):
    #         w = w.lower()
    #         filtered_sentence.append(w)
    sentence = sentence.lower()
    # sentence = " ".join(filtered_sentence)
    # re-removing single characters
    # sentence = re.sub(r"(^|\s)[a-z]($|\s)", " ", sentence)
    # sentence = re.sub(r"(^|\s)[a][a]($|\s)", " ", sentence)  # fixing for privacy
    # reduce consecutive spaces into single space between words
    sentence = " ".join(sentence.split())

    # hotfixes
    sentence = re.sub(r"heck a few", "heck few", sentence)
    # if "heck a few" in sentence:
    #     print("what")

    return sentence


def filter(sentence):
    stop_words = set(stopwords.words("english"))
    stray_tokens = [
        "amp",
        "`",
        "``",
        "'",
        "''",
        '"',
        "n't",
        "I",
        "i",
        ",00",
    ]  # stray words
    punct = r"[{}]".format(string.punctuation)
    sentence = re.sub(punct, " ", sentence)
    sentence = re.sub(r"[0-9]", " ", sentence)
    # # correct spelling mistakes
    # sentence = re.sub(
    #     r'privacy|privcy|privac|privasy|privasee|privarcy|priavcy|privsy', ' privacy ', sentence)
    # sentence = re.sub(r'(^|\s)[a-z]($|\s)', ' ', sentence)
    sentence = re.sub(r"(^|\s)[a-z][a-z]($|\s)", " ", sentence)
    word_tokens = word_tokenize(sentence)
    # filter using NLTK library append it to a string
    filtered_sentence = [w for w in word_tokens if not w in stop_words]
    filtered_sentence = []
    # looping through conditions
    for w in word_tokens:
        # check tokens against stopwords and punctuations
        if (
            w not in stop_words
            and w not in string.punctuation
            and w not in stray_tokens
        ):
            w = w.lower()
            filtered_sentence.append(w)
    sentence = " ".join(filtered_sentence)
    # re-removing single characters
    sentence = re.sub(r"(^|\s)[a-z]($|\s)", " ", sentence)
    sentence = re.sub(r"(^|\s)[a][a]($|\s)", " ",
                      sentence)  # fixing for privacy
    return sentence


def lemma(sentence):
    doc = spice(sentence)
    wrong_class = ["pending", "madras", "data", "media"]
    wrong_words = " ".join(wrong_class)
    wrong_doc = spice(wrong_words)
    lemma_sentence = []
    for token in doc:
        if token not in wrong_doc:
            token = token.lemma_
            if token != "-PRON-":
                lemma_sentence.append(token)
    return " ".join(lemma_sentence)


# print(
#     clean_filter_lemma_mini(
#         "We’ve seen the hippie-turned-yuppie plot before, but there’s an enthusiastic charm in Fire that makes the formula fresh again."
#     )
# )

run()


def run2(something):
    print(something[0])
