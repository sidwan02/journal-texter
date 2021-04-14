from clean_review import clean_filter_lemma_mini
import pandas as pd
from collections import Counter


def get_review_sentiment_dict():
    df = pd.read_csv(
        r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\IMDB DS\IMDB Dataset.csv")

    reviews = df['review'].values
    sentiments = df['sentiment'].values

    reviews, sentiments = df['review'].values, df['sentiment'].values

    review_to_sentiment_dict = {}

    for phr, sent in zip(reviews, sentiments):
        review_to_sentiment_dict[phr] = sent
    return reviews, sentiments, review_to_sentiment_dict


def generate_vocabulary(review_to_sentiment_dict):
    all_words = []
    count = 0
    for review in review_to_sentiment_dict.keys():
        review = clean_filter_lemma_mini(review)
        words = review.split(" ")
        for word in words:
            all_words.append(word)

    corpus = Counter(all_words)
    # most common words
    corpus_ = sorted(corpus, key=corpus.get, reverse=True)[:1000]
    # creating a dict
    vocabulary = {w: i+1 for i, w in enumerate(corpus_)}

    # print(vocabulary)

    return vocabulary
