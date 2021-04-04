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

    # print(index_to_phrase_dict[10])
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
        reviews_split_cleaned += filter(clean(rev))
        # reviews_split_cleaned.append(rev)

    # print(reviews_split_cleaned[1])
    print("done splitting reviews !!!!!!!!!!!!!!!!!")

    def try_tokenize(word):
        # print("trying")
        try:
            return phrase_to_index_dict[word]
        except:
            return -1

    reviews_tokenized = []

    for rev in reviews_split_cleaned:
        # print(rev)
        token_so_far = []
        index = 0
        words = rev.split(" ")
        word = words[index]
        # print("rev: ", rev)
        print("words: ", words)
        print("len: ", len(words))
        while index < len(words):  # until done for all words
            try:
                while (
                    try_tokenize(word) == -1
                ):  # until find a valid prase which has a phrase id

                    index += 1
                    word = word + " " + words[index]
            except IndexError:
                print(word)
            # phrase with phrase id has been found
            token_so_far.append(try_tokenize(word))
            print(word)
            print(index)
            # print(try_tokenize(word))
            index += 1
            try:
                word = words[index]
            except IndexError:
                # reached the end of the rev
                print(word)
                break
        break


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
    # sentence = re.sub(r"’", "'", sentence)
    # # words = sentence.split()
    # sentence = re.sub(r":", " ", sentence)
    sentence = re.sub(r"n't", "n not", sentence)

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
    punct = r"[{}]".format(string.punctuation)
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
    sentence = re.sub(r"(^|\s)[a][a]($|\s)", " ", sentence)  # fixing for privacy
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
#         "The camera twirls ! :: Oh, look at that clever angle you can't just hohohohoh! Wow, a jump cut !"
#     )
# )

run()


def run2(something):
    print(something[0])
