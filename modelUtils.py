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
import os
import gc
import pandas as pd

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

def get_next_image(features, index):
    
    img = cv2.imread(str(features[index]),1)

    while img is None:
        print('im still null')
        index = int(np.random.choice(len(features),1))
        img = cv2.imread(str(features[index]),1)


    img_resized = cv2.resize(img,(224,224))
    img_final = img_resized / 255.0
    return img_final

def openJsonFile(file_name):
    with open(file_name) as f:
        data = json.load(f)
        return data

def generator(features, labels, batch_size):
    batch_features = np.zeros((batch_size,224,224,3))
    batch_labels = np.zeros((batch_size,1))
    
    while True:
        for i in range(batch_size):
            index = int(np.random.choice(len(features),1))
            img = get_next_image(features,index)

            label = getLabel(str(labels[index]))
            batch_features[i] = img
            batch_labels[i] = label
        yield batch_features, batch_labels

def load_model(model_name='model'):
    json_file = open(model_name + '.json','r')
    loaded_model_json = json_file.read()
    json_file.close()
    model = model_from_json(loaded_model_json,custom_objects={"GlorotUniform": tf.keras.initializers.glorot_uniform})
    model.load_weights(model_name + '.h5')
    return model
