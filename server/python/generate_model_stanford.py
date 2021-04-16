from torch.utils.data import DataLoader, TensorDataset
import torch
from train import Train
from data_processing import *


# https://towardsdatascience.com/sentiment-analysis-for-text-with-deep-learning-2f0a0c6472b5
# https://towardsdatascience.com/sentiment-analysis-using-lstm-step-by-step-50d074f09948


def run():

    index_to_phrase_dict, phrase_to_index_dict = get_phrase_id_dicts()

    count = 0
    for i in index_to_phrase_dict.values():
        count = count + 1

    sentiment_dict = get_sentiment_dict()

    reviews_split_cleaned = get_reviews()

    reviews_tokenized = []

    count_reach = 0
    total_count = 0

    for rev in index_to_phrase_dict.values():
        total_count += 1

        token = tokenize_sentence(rev, phrase_to_index_dict)

        if token == [-1]:

        try:
            reviews_tokenized.append(token)

        except:
            reviews_tokenized.append(token)

    features = normalize_length(200, reviews_tokenized)

    with open(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\datasetSplit.txt",
        "r",
    ) as f:
        data_split = f.read()

    index_to_spilt_state_dict = {}
    splitset_tuples = data_split.split("\n")

    for tup in splitset_tuples:
        split = tup.split(",")
        try:
            index_to_spilt_state_dict[int(split[0])] = int(split[1])
        except IndexError:
            break
        except ValueError:
            hahahaha = 10

    train_x, train_y, test_x, test_y = split_data(
        features, index_to_phrase_dict, sentiment_dict, 0.999)

    # create Tensor datasets
    train_data = TensorDataset(torch.from_numpy(
        np.array(train_x)), torch.from_numpy(np.array(train_y)))

    test_data = TensorDataset(torch.from_numpy(
        np.array(test_x)), torch.from_numpy(np.array(test_y)))

    batch_size = 50

    # https://discuss.pytorch.org/t/runtimeerror-expected-hidden-0-size-2-20-256-got-2-50-256/38288/11
    train_loader = DataLoader(train_data, shuffle=True,
                              batch_size=batch_size, drop_last=True)

    test_loader = DataLoader(test_data, shuffle=True,
                             batch_size=batch_size, drop_last=True)

    # obtain one batch of training data
    dataiter = iter(train_loader)
    sample_x, sample_y = dataiter.next()

    # Instantiate hyperparams
    vocab_size = len(index_to_phrase_dict.keys())
    output_size = 1
    embedding_dim = 256
    hidden_dim = 2
    n_layers = 2

    Train(vocab_size=vocab_size,
          train_loader=test_loader, test_loader=test_loader, batch_size=batch_size)


def split_data(features, index_to_phrase_dict, sentiment_dict, threshold):
    # split into train and test arrs
    train_x = []
    train_y = []

    dev_x = []
    dev_y = []

    test_x = []
    test_y = []

    i = 1
    valid_count = 0

    print("features length: ", len(features))
    print("index_to_phrase_dict length: ", len(index_to_phrase_dict.keys()))

    for phrase_index in index_to_phrase_dict.keys():
        try:
            sentiment = sentiment_dict[phrase_index]
            if phrase_index < threshold * len(index_to_phrase_dict):
                train_x.append(features[i])
                train_y.append(0 if sentiment < 0.5 else 1)
            else:
                test_x.append(features[i])
                test_y.append(0 if sentiment < 0.5 else 1)

            i = i + 1
            valid_count = valid_count + 1
        except KeyError:
            break
        except IndexError:
            break

    return (train_x, train_y, test_x, test_y)


run()
