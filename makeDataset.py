'''
This file takes the downloaded dataset and creates a numpy array with the image and its label
and saves it. It takes a long time to do this so be ready to wait. Also doesn't actually work
for all the data yet as some of it is labeled differently than others so it currently crashes
when trying to run and hits that differntly labeled data.
'''

import numpy as np
import matplotlib.pyplot as plt
import os 
import cv2
import json
from tqdm import tqdm
from random import shuffle

#change directory to the data to work on your machine
DATADIR = "D:/TrainingData/Images"
DESCDIR = "D:/TrainingData/Descriptions"

CATEGORIES = ['Benign','Malignant']
#new image size after resizing
IMG_SIZE = 224
IMG_AMOUNT = 22000

training_data = []
#check if image is none and skip it if so

def createTrainingData():
    for image in tqdm(os.listdir(DATADIR)):
        img_array = cv2.imread(os.path.join(DATADIR,image))
        new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
        new_array = cv2.cvtColor(new_array,cv2.COLOR_BGR2RGB)
        label = getLabel(image)
        if label == None:
            continue
        
        # new_array = new_array/255
        training_data.append([new_array,label])
        if len(training_data) >= IMG_AMOUNT:
            break



# This needs to change to work with all data descriptions
def getLabel(fileName):
    data = openJsonFile(os.path.join(DESCDIR,fileName.split(".")[0]))

    if 'meta' in data:
        if 'clinical' in data['meta']:
            if 'benign_malignant' in data['meta']['clinical']:
                if data['meta']['clinical']['benign_malignant'] == 'benign':
                    return 0
                else:
                    return 1
    else:
        raise ValueError("No benign bool given in data given at file: " + fileName)

def openJsonFile(fileName):
    with open(fileName) as f:
        data = json.load(f)
        #print(data)
        return data

createTrainingData()

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
np.save('X_data1',X)
np.save('y_data1',y)