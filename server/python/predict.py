import numpy as np
import preprocess_string

# https://stackoverflow.com/questions/42703500/best-way-to-save-a-trained-model-in-pytorch
num_layers = 2
vocab_size = 239232
vocab_size = vocab_size + 1  # extra 1 for padding
embedding_dim = 64
hidden_dim = 256

model = SentimentRNN(num_layers, vocab_size, hidden_dim,
                     embedding_dim, drop_prob=0.5)

model.load_state_dict(torch.load(PATH))


def predict_text(text):
    word_seq = np.array([vocab[preprocess_string(word)] for word in text.split()
                         if preprocess_string(word) in vocab.keys()])
    word_seq = np.expand_dims(word_seq, axis=0)
    pad = torch.from_numpy(padding_(word_seq, 500))
    inputs = pad.to(device)
    batch_size = 1
    h = model.init_hidden(batch_size)
    h = tuple([each.data for each in h])
    output, h = model(inputs, h)
    return(output.item())


pro = predict_text("Wow it is so warm today!")
status = "positive" if pro > 0.5 else "negative"
pro = (1 - pro) if status == "negative" else pro
print(f'Predicted sentiment is {status} with a probability of {pro}')
