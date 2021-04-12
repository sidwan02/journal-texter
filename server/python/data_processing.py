import numpy as np


def tokenize_all(all_sentences, phrase_to_index_dict):
    tokenized = []
    for sentence in all_sentences:
        tokenized.append(tokenize_sentence(sentence, phrase_to_index_dict))

    return np.array(tokenized)


def try_tokenize(word, phrase_to_index_dict):
    try:
        return phrase_to_index_dict[word]
    except:
        # return -1
        return 0


def tokenize_sentence(rev, phrase_to_index_dict):
    token_so_far = []
    index = 0
    words = rev.split(" ")
    while index < len(words):  # until done for all words
        word = words[index]

        token = try_tokenize(word, phrase_to_index_dict)
        if token != 0:
            token_so_far.append(token)
        index += 1
    return token_so_far


def normalize_length(target_length, twod_arr):
    normalized_arr = []

    for arr in twod_arr:
        if len(arr) > target_length:
            truncated_arr = arr[0:target_length]
            normalized_arr.append(truncated_arr)
        elif len(arr) < target_length:
            padded_arr = [0] * (target_length - len(arr)) + arr
            normalized_arr.append(padded_arr)
        else:
            normalized_arr.append(arr)
    return normalized_arr
