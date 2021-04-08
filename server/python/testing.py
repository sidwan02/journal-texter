import numpy as np


# def padding_(sentences, seq_len):
#     features = np.zeros((len(sentences), seq_len), dtype=int)
#     for ii, review in enumerate(sentences):
#         if len(review) != 0:
#             features[ii, -len(review):] = np.array(review)[:seq_len]
#     return features


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

    # print(normalized_arr)
    return np.array(normalized_arr)


x = [[1, 1, 1, 1, 1, 1], [1, 1, 1]]

print(padding_(x, 1))
print(normalize_length(1, x))
