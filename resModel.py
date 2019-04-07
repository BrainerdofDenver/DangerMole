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
from keras.applications.resnet50 import preprocess_input,ResNet50
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

image_input = Input(shape=(224,224,3))

model = ResNet50(include_top=True, weights=None,
             input_tensor=image_input,classes=2)

last_layer = model.get_layer('avg_pool').output
x = Flatten(name='flatten')(last_layer)
out = Dense(1,activation='sigmoid',name='output_layer')(x)
custom_resnet_model = Model(inputs=image_input,outputs=out)
custom_resnet_model.summary()

for layer in custom_resnet_model.layers[:-1]:
    layer.trainable = False

custom_resnet_model.layers[-1].trainable

custom_resnet_model.compile(loss='binary_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

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