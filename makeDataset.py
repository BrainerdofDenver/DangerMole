import numpy as np
import matplotlib.pyplot as plt
import os 
import cv2
import json

DATADIR = "E:/\school/tensorflowprc/moleDataSet/Data/Images/"
DESCDIR = "E:/\school/tensorflowprc/moleDataSet/Data/Descriptions/"
CATEGORIES = ['Benign','Malignant']
IMG_SIZE = 224
IMG_AMOUNT = 24000
training_data = []

def createTrainingData():
    for image in os.listdir(DATADIR):
        img_array = cv2.imread(os.path.join(DATADIR,image))
        new_array = cv2.resize(img_array, (IMG_SIZE, IMG_SIZE))
        training_data.append([new_array,getLabel(image)])
        if len(training_data) >= IMG_AMOUNT:
            break

def getLabel(fileName):
        #address = baseAddrDescriptions + fileNames[i][:-5]
        #print(address)
    data = openJsonFile(os.path.join(DESCDIR,fileName[:-5]))
        #print(data['meta']['clinical']['benign_malignant'])
    if data['meta']['clinical']['benign_malignant'] == 'benign':
        return 0
    else:
        return 1

def openJsonFile(fileName):
    with open(fileName) as f:
        data = json.load(f)
        #print(data)
        return data

createTrainingData()
'''plt.imshow(training_data[0][0])
plt.show()
'''
print(len(training_data))
X = []
y = []
for features, label in training_data:
    X.append(features)
    y.append(label)

plt.imshow(X[0])
plt.show()
X = np.array(X).reshape(-1,IMG_SIZE,IMG_SIZE,3)
np.save('X_data',X)
np.save('y_data',y)
