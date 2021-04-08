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
base_csv = r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\IMDB DS\IMDB Dataset.csv"
df = pd.read_csv(base_csv)
df.head()

X, y = df['review'].values, df['sentiment'].values
x_train, x_test, y_train, y_test = train_test_split(X, y, stratify=y)
print(f'shape of train data is {x_train.shape}')
print(f'shape of test data is {x_test.shape}')


def preprocess_string(s):
    # # Remove all non-word characters (everything except numbers and letters)
    # s = re.sub(r"[^\w\s]", '', s)
    # # Replace all runs of whitespaces with no space
    # s = re.sub(r"\s+", '', s)
    # # replace digits with no space
    # s = re.sub(r"\d", '', s)

    # return s
    return clean_filter_lemma_mini(s)


def tokenize(x_train, y_train, x_val, y_val):
    word_list = []

    stop_words = set(stopwords.words('english'))
    for sent in x_train:
        for word in sent.lower().split():
            word = preprocess_string(word)
            if word not in stop_words and word != '':
                word_list.append(word)

    print("done preprocessing")
    corpus = Counter(word_list)
    # sorting on the basis of most common words
    corpus_ = sorted(corpus, key=corpus.get, reverse=True)[:1000]
    # creating a dict
    onehot_dict = {w: i+1 for i, w in enumerate(corpus_)}

    # tokenize
    final_list_train, final_list_test = [], []
    for sent in x_train:
        final_list_train.append([onehot_dict[preprocess_string(word)] for word in sent.lower().split()
                                 if preprocess_string(word) in onehot_dict.keys()])
    for sent in x_val:
        final_list_test.append([onehot_dict[preprocess_string(word)] for word in sent.lower().split()
                                if preprocess_string(word) in onehot_dict.keys()])

    encoded_train = [1 if label == 'positive' else 0 for label in y_train]
    encoded_test = [1 if label == 'positive' else 0 for label in y_val]
    return np.array(final_list_train), np.array(encoded_train), np.array(final_list_test), np.array(encoded_test), onehot_dict


review_to_sentiment_dict = get_review_sentiment_dict()

vocab = generate_vocabulary(review_to_sentiment_dict)


def tokenize_all(all_sentences, phrase_to_index_dict):
    tokenized = []
    for sentence in all_sentences:
        tokenized.append(tokenize_sentence(sentence, phrase_to_index_dict))

    return np.array(tokenized)


x_train = tokenize_all(x_train, vocab)
x_test = tokenize_all(x_test, vocab)

y_train = [1 if label == 'positive' else 0 for label in y_train]
y_test = [1 if label == 'positive' else 0 for label in y_test]


# x_train, y_train, x_test, y_test, vocab = tokenize(
#     x_train, y_train, x_test, y_test)
print(f'Length of vocabulary is {len(vocab)}')

rev_len = [len(i) for i in x_train]
pd.Series(rev_len).hist()
plt.show()
pd.Series(rev_len).describe()


def padding_(sentences, seq_len):
    features = np.zeros((len(sentences), seq_len), dtype=int)
    for ii, review in enumerate(sentences):
        if len(review) != 0:
            features[ii, -len(review):] = np.array(review)[:seq_len]
    return features


# we have very less number of reviews with length > 500.
# So we will consideronly those below it.
x_train_pad = padding_(x_train, 500)
x_test_pad = padding_(x_test, 500)

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
      train_loader=valid_loader, test_loader=valid_loader, batch_size=batch_size)
