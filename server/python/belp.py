# print(rev)
token_so_far = []
index = 0
words = "hello".split(" ")
print(words)
word = words[index]
# print("rev: ", rev)
# print("words: ", words)
# print("len: ", len(words))
while index < len(words):  # until done for all words
    print("word")

    # phrase with phrase id has been found
    token_so_far.append(word)
    # print(word)
    # print(index)
    # print(try_tokenize(word))
    index += 1
    try:
        word = words[index]
    except IndexError:
        # reached the end of the rev
        print(token_so_far)
        break
