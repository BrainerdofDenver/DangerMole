import tensorflow as tf
from tensorflow.keras.models import Sequential, model_from_json
from tensorflow.keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.metrics import roc_curve,roc_auc_score,classification_report
from sklearn.model_selection import train_test_split
import random
import gc
from tensorflow.contrib import lite
from keras.models import model_from_json
from keras.models import load_model
from keras.initializers import glorot_uniform

#Keras file to tflite file
jsonOpen = open('testCnnModel1.json', 'r')
loadInModel = jsonOpen.read()
jsonOpen.close()

model = model_from_json (loadInModel, custom_objects = {'GlorotUniform': tf.keras.initializers.glorot_uniform})

model.load_weights('testCnnModel1.h5')
model.compile(loss='binary_crossentropy',
        optimizer='adam',
        metrics=['accuracy'],
        sample_weight_mode=None)
 
model.save('fulltestcnnmodel.h5')



converter = lite.TFLiteConverter.from_keras_model_file('fulltestcnnmodel.h5') 
tflite_model = converter.convert()
file = open( "converted_model2.tflite" , "wb" ).write(tflite_model)
