import pandas as pd
from predict import predict_sentiment

df = pd.read_csv(
    r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\Questions Spreadsheet - Specific Questions.csv")

# print(df["Question"])

sentiments = []
for question in df['Question']:
    sentiments.append(predict_sentiment(question))
    # break

print(sentiments)

df["sentiment"] = sentiments

df.to_csv(r"C:\Users\sidwa\OneDrive\OneDriveNew\Personal\Sid\Brown University\Courses\Computer Science\CSCI 0320\Assignments\term-project-rfameli1-sdiwan2-tfernan4-tzaw\data\QuestionsWithSentiment.csv", index=False)
