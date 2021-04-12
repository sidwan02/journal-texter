#!/usr/bin/python
import sys
from predict import predict_sentiment

print('Number of Arguments:', len(sys.argv), 'arguments.')
print('Argument List:', str(sys.argv))
print('This is Python Code')
print('Executing Python')
print('From Java')

predict_sentiment(sys.argv[1])
