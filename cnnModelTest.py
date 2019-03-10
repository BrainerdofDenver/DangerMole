'''
Base cnn model.
'''
import tensorflow as tf
from tensorflow.keras.models import Sequential, model_from_json
from tensorflow.keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D
import numpy as np
import matplotlib.pyplot as plt
import json
from random import shuffle
from sklearn.metrics import roc_curve,roc_auc_score,classification_report
from sklearn.model_selection import train_test_split
from generator import My_Generator
import cv2
from numpy import random
import os
import gc
import pandas as pd

DATADIR = 'D:/TrainingData/Images'
DATADESC = 'D:/TrainingData/Descriptions'
# Amount of test data
TEST_PERCENT = 0.2
batch_size = 128 

X_file_names = np.array(os.listdir(DATADIR))
y_file_names = np.array(os.listdir(DATADESC))

steps = np.ceil(len(X_file_names) / batch_size)
print(steps)

def getLabel(file_name):
    #data = openJsonFile(file_name[:-5])
    data = openJsonFile(file_name)

    if 'meta' in data:
        if 'clinical' in data['meta']:
            if 'benign_malignant' in data['meta']['clinical']:
                if data['meta']['clinical']['benign_malignant'] == 'benign':
                    return 0
                else:
                    return 1

def openJsonFile(file_name):
    with open(file_name) as f:
        data = json.load(f)
        return data

def generator(features, labels, batch_size):
    batch_features = np.zeros((batch_size,224,224,3))
    #batch_features = []

    batch_labels = np.zeros((batch_size,1))
    
    while True:
        for i in range(batch_size):
            index = random.choice(len(features),1)
            img = cv2.imread(os.path.join(DATADIR,str(features[index])[2:14]+'.jpeg'),1)
            while img is None:
                index = random.choice(len(features),1)
                img = cv2.imread(os.path.join(DATADIR,str(features[index])[2:14]+'.jpeg'),1)

            img_resized = cv2.resize(img,(224,224))
            img_final = img_resized / 255.0

            label = getLabel(os.path.join(DATADESC,str(labels[index])[2:14]))
            batch_features[i] = img_final
            batch_labels[i] = label
        yield batch_features, batch_labels
'''
index = random.choice(len(X_file_names),1)
print(str(y_file_names[index])[2:14])
print(getLabel(os.path.join(DATADESC,str(y_file_names[index])[2:14])))
img = cv2.resize(cv2.imread(os.path.join(DATADIR,str(X_file_names[index])[2:14]+'.jpeg')), (224,224))
cv2.imshow('image',img)
cv2.waitKey(0)
X_train_file_names, X_val_file_names, y_train_file_names, y_val_file_names = train_test_split(X_file_names,y_file_names,test_size=0.2,random_state=2)

num_training_samples = len(X_train_file_names)
num_validation_samples= len(X_val_file_names)
print(num_training_samples)
print(num_validation_samples)
my_training_batch_generator = My_Generator(X_train_file_names, y_train_file_names, batch_size)
my_validation_batch_generator = My_Generator(X_val_file_names, y_val_file_names, batch_size)
my_training_batch_generator.print_base()
'''
model = Sequential()
model.add(Conv2D(32,(3,3),activation='relu', input_shape = (224,224,3),data_format='channels_last'))
model.add(MaxPooling2D(pool_size=(2,2),strides=2))

model.add(Conv2D(64,(3,3),activation='relu'))
model.add(MaxPooling2D(pool_size=(2,2)))

model.add(Flatten())
model.add(Dense(64,activation='relu'))
model.add(Dropout(0.25))

model.add(Dense(1,activation='sigmoid'))

model.compile(loss='binary_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

model.fit_generator(generator(X_file_names, y_file_names,batch_size),
                    steps_per_epoch=steps,
                    epochs=3)

model_json = model.to_json()
with open("model2.json", "w") as json_file:
    json_file.write(model_json)

model.save_weights("model2.h5")

