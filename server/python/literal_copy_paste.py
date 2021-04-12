from train import Train
import numpy as np  # linear algebra
import pandas as pd  # data processing, CSV file I/O (e.g. pd.read_csv)
import torch
import torch.nn as nn
import torch.nn.functional as F
from nltk.corpus import stopwords
from collections import Counter
import string
import re
# import seaborn as sns
from tqdm import tqdm
import matplotlib.pyplot as plt
from torch.utils.data import TensorDataset, DataLoader
from sklearn.model_selection import train_test_split
from clean_review import clean_filter_lemma_mini
from extract_data_IMDB import *
from data_processing import *

is_cuda = torch.cuda.is_available()

# If we have a GPU available, we'll set our device to GPU. We'll use this device variable later in our code.
if is_cuda:
    device = torch.device("cuda")
    print("GPU is available")
else:
    device = torch.device("cpu")
    print("GPU not available, CPU used")
# base_csv = r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\IMDB DS\IMDB Dataset.csv"
# df = pd.read_csv(base_csv)
# df.head()

# X, y = df['review'].values, df['sentiment'].values

reviews, sentiment, review_to_sentiment_dict = get_review_sentiment_dict()

x_train, x_test, y_train, y_test = train_test_split(
    reviews, sentiment, stratify=sentiment)
print(f'shape of train data is {x_train.shape}')
print(f'shape of test data is {x_test.shape}')

# review_to_sentiment_dict = get_review_sentiment_dict()

vocab = generate_vocabulary(review_to_sentiment_dict)

x_train = tokenize_all(x_train, vocab)
x_test = tokenize_all(x_test, vocab)

y_train = np.array([1 if label == 'positive' else 0 for label in y_train])
y_test = np.array([1 if label == 'positive' else 0 for label in y_test])


# x_train, y_train, x_test, y_test, vocab = tokenize(
#     x_train, y_train, x_test, y_test)
print(f'Length of vocabulary is {len(vocab)}')

# rev_len = [len(i) for i in x_train]
# pd.Series(rev_len).hist()
# plt.show()
# pd.Series(rev_len).describe()


def padding_(sentences, seq_len):
    features = np.zeros((len(sentences), seq_len), dtype=int)
    for ii, review in enumerate(sentences):
        if len(review) != 0:
            features[ii, -len(review):] = np.array(review)[:seq_len]
    return features


# we have very less number of reviews with length > 500.
# So we will consideronly those below it.
# x_train_pad = padding_(x_train, 500)
# x_test_pad = padding_(x_test, 500)
x_train_pad = np.array(normalize_length(500, x_train))
x_test_pad = np.array(normalize_length(500, x_test))

# create Tensor datasets
train_data = TensorDataset(torch.from_numpy(
    x_train_pad), torch.from_numpy(y_train))
valid_data = TensorDataset(torch.from_numpy(
    x_test_pad), torch.from_numpy(y_test))

# dataloaders
batch_size = 50

# make sure to SHUFFLE your data
train_loader = DataLoader(train_data, shuffle=True, batch_size=batch_size)
valid_loader = DataLoader(valid_data, shuffle=True, batch_size=batch_size)
# obtain one batch of training data
dataiter = iter(train_loader)
sample_x, sample_y = dataiter.next()

print('Sample input size: ', sample_x.size())  # batch_size, seq_length
print('Sample input: \n', sample_x)
print('Sample input: \n', sample_y)

no_layers = 2
vocab_size = len(vocab) + 1  # extra 1 for padding
embedding_dim = 64
output_dim = 1
hidden_dim = 256


# model = SentimentRNN(no_layers, vocab_size, hidden_dim,
#                      embedding_dim, drop_prob=0.5)

Train(vocab_size=vocab_size,
      train_loader=train_loader, test_loader=valid_loader, batch_size=batch_size)
