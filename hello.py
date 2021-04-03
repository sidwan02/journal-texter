# class Hello:
# #     __gui = None
# #
#     def __init__(self):
#         # self.__gui = gui

import time
import json
import csv
import preprocessor as p
import re
import string
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
import emoji
import itertools
import spacy
import numpy as np
import en_core_web_sm
spice = en_core_web_sm.load()


def run():
    with open('data\stanfordSentimentTreebank\dictionary.txt', 'r') as f:
        reviews = f.read()
    with open('data\stanfordSentimentTreebank\sentiment_labels.txt', 'r') as f:
        labels = f.read()


def clean(sentence):
    # remove mentions and URLs
    # p.set_options(p.OPT.URL, p.OPT.MENTION, p.OPT.HASHTAG, p.OPT.NUMBER)
    # sentence = p.clean(sentence)
    # replace consecutive non-ASCII characters with a space
    sentence = re.sub(r'[^\x00-\x7F]+', ' ', sentence)
    # take care of contractions and stray quote marks
    sentence = re.sub(r'’', "'", sentence)
    # words = sentence.split()
    sentence = re.sub(r":", " ", sentence)
    sentence = re.sub(r"n't", " not", sentence)
    # fix spellings
    sentence = ''.join(''.join(s)[:2] for _, s in itertools.groupby(sentence))
    # emojis conversion
    sentence = emoji.demojize(sentence)
    sentence = ' '.join(sentence.split())
    return sentence


def filter(sentence):
    stop_words = set(stopwords.words('english'))
    stray_tokens = ['amp', '`', "``", "'", "''",
                    '"', "n't", "I", "i", ",00"]  # stray words
    punct = r'[{}]'.format(string.punctuation)
    sentence = re.sub(punct, ' ', sentence)
    sentence = re.sub(r'[0-9]', ' ', sentence)
    # # correct spelling mistakes
    # sentence = re.sub(
    #     r'privacy|privcy|privac|privasy|privasee|privarcy|priavcy|privsy', ' privacy ', sentence)
    # sentence = re.sub(r'(^|\s)[a-z]($|\s)', ' ', sentence)
    sentence = re.sub(r'(^|\s)[a-z][a-z]($|\s)', ' ', sentence)
    word_tokens = word_tokenize(sentence)
    # filter using NLTK library append it to a string
    filtered_sentence = [w for w in word_tokens if not w in stop_words]
    filtered_sentence = []
    # looping through conditions
    for w in word_tokens:
        # check tokens against stopwords and punctuations
        if w not in stop_words and w not in string.punctuation and w not in stray_tokens:
            w = w.lower()
            filtered_sentence.append(w)
    sentence = ' '.join(filtered_sentence)
    # re-removing single characters
    sentence = re.sub(r'(^|\s)[a-z]($|\s)', ' ', sentence)
    sentence = re.sub(r'(^|\s)[a][a]($|\s)', ' ',
                      sentence)  # fixing for privacy
    return sentence


def lemma(sentence):
    doc = spice(sentence)
    wrong_class = ['pending', 'madras', 'data', 'media']
    wrong_words = ' '.join(wrong_class)
    wrong_doc = spice(wrong_words)
    lemma_sentence = []
    for token in doc:
        if token not in wrong_doc:
            token = token.lemma_
            if token != "-PRON-":
                lemma_sentence.append(token)
    return ' '.join(lemma_sentence)


print(lemma(filter(clean("The camera twirls ! Oh, look at that clever angle ! Wow, a jump cut !"))))

run()
