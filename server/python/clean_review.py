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

spice = en_core_web_sm.load()


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
