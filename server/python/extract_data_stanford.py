from clean_review import clean_filter_lemma_mini


def get_phrase_id_dicts():
    with open(
            r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\dictionary.txt",
            "r",
    ) as f:
        phrases = f.read()

    # convert into dict of phrase ID | phrase
    index_to_phrase_dict = {}
    phrase_to_index_dict = {}

    phrases_split = phrases.split("\n")
    count = 0
    total = len(phrases_split)
    for phr in phrases_split:
        phrase_components = phr.split("|")
        try:
            index = phrase_components[1]
            phrase = phrase_components[0]

            processed_phrase = clean_filter_lemma_mini(phrase)
            index_to_phrase_dict[int(index)] = processed_phrase
            phrase_to_index_dict[processed_phrase] = int(index)
        except IndexError:
            # this is the newline at the end of the txt
            break
        except ValueError:

    return (index_to_phrase_dict, phrase_to_index_dict)


def get_sentiment_dict():
    with open(
            r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\sentiment_labels.txt",
            "r",
    ) as f:
        labels = f.read()

    sentiment_dict = {}

    sentiment_split = labels.split("\n")
    for lab in sentiment_split:
        sentiment_components = lab.split("|")
        try:
            index = sentiment_components[0]
            sentiment = sentiment_components[1]
            if (sentiment == "sentiment values"):
            else:
                sentiment_dict[int(index)] = float(sentiment)
        except IndexError:
            # this is the newline at the end of the txt
            break
        except ValueError:
            break
    return sentiment_dict


def get_reviews():
    with open(
            r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\stanfordSentimentTreebank\original_rt_snippets.txt",
            "r",
    ) as f:
        reviews = f.read()

    reviews_split = reviews.split("\n")

    reviews_split_cleaned = []

    for rev in reviews_split:
        reviews_split_cleaned.append(clean_filter_lemma_mini(rev))

    return reviews_split_cleaned
