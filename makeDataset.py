'''
This file takes the downloaded dataset and creates a numpy array with the image and its label
and saves it. It takes about 20 minutes on my machine, but has a progress bar so you can keep
track of progress.
'''
import numpy as np
import matplotlib.pyplot as plt
import os 
import cv2
import json
from tqdm import tqdm
from random import shuffle
from modelUtils import get_label, open_json_file

#change directory to the data to work on your machine
DATADIR = "D:/TrainingData/Images"
DESCDIR = "D:/TrainingData/Descriptions"

CATEGORIES = ['Benign','Malignant']
#new image size after resizing
IMG_SIZE = 224
IMG_AMOUNT = 1000

training_data = []

def createTrainingData(image_amount):
    filenames = np.array(os.listdir(DATADIR))
    for image in tqdm(range(image_amount)):
        img_array = cv2.imread(os.path.join(DATADIR,filenames[image]))
        new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
        new_array = cv2.cvtColor(new_array,cv2.COLOR_BGR2RGB)
        label = get_label(os.path.join(DESCDIR,filenames[image].split(".")[0]))
        if label == None:
            continue
        
        # new_array = new_array/255
        training_data.append([new_array,label])
        if len(training_data) >= image_amount:
            break

def main():
    createTrainingData(IMG_AMOUNT)

    print(len(training_data))
    shuffle(training_data)
    X = []
    y = []
    for features, label in training_data:
        X.append(features)
        y.append(label)

    plt.imshow(X[0])
    plt.title(y[0])
    plt.show()
    X = np.array(X).reshape(-1,IMG_SIZE,IMG_SIZE,3)
    np.save('X_data_test',X)
    np.save('y_data_test',y)

if __name__ == "__main__":
    main()