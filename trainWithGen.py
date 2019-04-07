'''
Train resModel using generators
'''
import tensorflow as tf
from keras.models import Sequential, model_from_json,Model
from keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D,Input
from keras.preprocessing.image import ImageDataGenerator
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.utils import class_weight
import random
import os
#from keras.applications.resnet50 import preprocess_input,ResNet50
from models import get_res_model
from modelUtils import generator,open_json_file,get_label
import gc

DATADIR = 'D:\TrainingData\Images'
DATADESC = 'D:\TrainingData\Descriptions'

TEST_PERCENT = 0.2
batch_size = 32

X_file_names = list(os.listdir(DATADIR))
y_file_names = os.listdir(DATADESC)

X_file_names_full = []
y_file_names_full = []

for file_name in X_file_names:
    X_file_names_full.append(os.path.join(DATADIR, file_name))

for file_name in y_file_names:
    y_file_names_full.append(os.path.join(DATADESC, file_name))

X_file_names_full = np.array(X_file_names_full)
y_file_names_full = np.array(y_file_names_full)
ytest = [get_label(y_file_names_full[i]) for i in range(0,len(y_file_names_full))]

steps = np.ceil(len(X_file_names_full) / batch_size)

class_weights = class_weight.compute_class_weight('balanced',np.unique(ytest),ytest)

custom_resnet_model = get_res_model()

custom_resnet_model.fit_generator(generator(X_file_names_full, y_file_names_full, batch_size),
                    steps_per_epoch=steps,
                    epochs=5,
                    class_weight=class_weights)

# Saves model along with weights
model_json = custom_resnet_model.to_json()
with open("resModel.json", "w") as json_file:
    json_file.write(model_json)

custom_resnet_model.save_weights("resModel.h5")
print("saved model")