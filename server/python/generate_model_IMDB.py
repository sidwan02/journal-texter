from train import Train
import torch
from torch.utils.data import TensorDataset, DataLoader
from sklearn.model_selection import train_test_split
from extract_data_IMDB import *
from data_processing import *

is_cuda = torch.cuda.is_available()

# check availability of gpu
if is_cuda:
    device = torch.device("cuda")
    print("GPU is available")
else:
    device = torch.device("cpu")
    print("GPU not available, CPU used")

reviews, sentiment, review_to_sentiment_dict = get_review_sentiment_dict()

x_train, x_test, y_train, y_test = train_test_split(
    reviews, sentiment, stratify=sentiment)

vocab = generate_vocabulary(review_to_sentiment_dict)

x_train = tokenize_all(x_train, vocab)
x_test = tokenize_all(x_test, vocab)

y_train = np.array([1 if label == 'positive' else 0 for label in y_train])
y_test = np.array([1 if label == 'positive' else 0 for label in y_test])


def padding_(sentences, seq_len):
    features = np.zeros((len(sentences), seq_len), dtype=int)
    for ii, review in enumerate(sentences):
        if len(review) != 0:
            features[ii, -len(review):] = np.array(review)[:seq_len]
    return features


# arbitrary choice of max 500 length
x_train_pad = np.array(normalize_length(500, x_train))
x_test_pad = np.array(normalize_length(500, x_test))

# create Tensor datasets
train_data = TensorDataset(torch.from_numpy(
    x_train_pad), torch.from_numpy(y_train))
valid_data = TensorDataset(torch.from_numpy(
    x_test_pad), torch.from_numpy(y_test))

# dataloaders
batch_size = 50

train_loader = DataLoader(train_data, shuffle=True, batch_size=batch_size)
valid_loader = DataLoader(valid_data, shuffle=True, batch_size=batch_size)

dataiter = iter(train_loader)
sample_x, sample_y = dataiter.next()

no_layers = 2
vocab_size = len(vocab) + 1  # extra 1 for padding
embedding_dim = 64
output_dim = 1
hidden_dim = 256


Train(vocab_size=vocab_size,
      train_loader=train_loader, test_loader=valid_loader, batch_size=batch_size)
