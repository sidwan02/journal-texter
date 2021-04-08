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
from model import SentimentRNN


# https://stackoverflow.com/questions/42703500/best-way-to-save-a-trained-model-in-pytorch
num_layers = 2
vocab_size = 1001
vocab_size = vocab_size + 1  # extra 1 for padding
embedding_dim = 64
hidden_dim = 256

model = SentimentRNN(num_layers, vocab_size, hidden_dim,
                     embedding_dim, drop_prob=0.5)

model.load_state_dict(torch.load(
    r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\server\model" + "\sentiment_model.pth"))

is_cuda = torch.cuda.is_available()


# If we have a GPU available, we'll set our device to GPU. We'll use this device variable later in our code.
if is_cuda:
    device = torch.device("cuda")
    print("GPU is available")
else:
    device = torch.device("cpu")
    print("GPU not available, CPU used")

model.to(device)


def predict_text(text):
    # word_seq = np.array([vocab[preprocess_string(word)] for word in text.split()
    #                      if preprocess_string(word) in vocab.keys()])

    rev = clean_filter_lemma_mini(text)

    review_to_sentiment_dict = get_review_sentiment_dict()

    vocab = generate_vocabulary(review_to_sentiment_dict)

    word_seq = tokenize_sentence(rev, vocab)

    reviews_tokenized = []
    # batch size of 1 doesn't work for some reason because of that squeeze error thingy remember [] vs [1]
    reviews_tokenized.append(word_seq)
    # reviews_tokenized.append(word_seq)

    # word_seq = np.expand_dims(word_seq, axis=0)
    pad = torch.from_numpy(
        np.array(normalize_length(500, reviews_tokenized)))
    inputs = pad.to(device)
    print(inputs)
    batch_size = 1
    h = model.init_hidden(batch_size)
    h = tuple([each.data for each in h])
    output, h = model(inputs, h)
    print(output)

    return(output.item())


pro = predict_text(
    "horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible horrible ")
status = "positive" if pro > 0.5 else "negative"
print(f'Predicted sentiment is {status} with value {pro}')
