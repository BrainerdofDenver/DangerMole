import tensorflow as tf
import keras
import numpy as np
from numpy import random
from keras.models import model_from_json
import os
import cv2
import json
import modelUtils
import matplotlib.pyplot as plt
from keras.initializers import glorot_uniform
from modelUtils import get_label, open_json_file, get_image_at_index,load_model

DATADIR = 'D:/TrainingData/Images'
DATADESC = 'D:/TrainingData/Descriptions'

X_file_names = np.array(os.listdir(DATADIR))
y_file_names = np.array(os.listdir(DATADESC))
class_names = ['benign','malignant']

def plot_image(i, predictions_array, true_label, img):
    predictions_array, true_label, img = predictions_array[i], true_label[i], img[i]
    plt.grid(False)
    plt.xticks([])
    plt.yticks([])

    plt.imshow(img)
    predicted_label = 1
    if predictions_array > 0.50:
        predicted_label = 1
    else:
        predicted_label = 0

    if predicted_label == true_label:
        color = 'blue'
    else:
        color = 'red'

    plt.xlabel("{} {:2.0f}% ({})".format(class_names[predicted_label],
                                  100*np.max(predictions_array),
                                  class_names[true_label]),
                                  color=color)

def plot_value_array(i, predictions_array, true_label):
    predictions_array, true_label = predictions_array[i], true_label[i]
    plt.grid(False)
    plt.xticks([])
    plt.yticks([])
    plt.xlabel('predicted label',fontsize=10)
    plt.ylabel('label confidence',fontsize=10)
    thisplot = plt.bar(1, predictions_array, color="#777777")
    plt.ylim([0, 1]) 
    
    if predictions_array > 0.50:
        predicted_label = 1
    else:
        predicted_label = 0

    if predicted_label == true_label:
        thisplot[0].set_color('blue')
    else:
        thisplot[0].set_color('red')
    
num_rows = 5
num_cols = 3
num_images = num_rows*num_cols
test_images = []
test_labels = []

for i in range(num_images):
    index = int(random.choice(len(X_file_names),1))
    #test_images.append(cv2.resize(cv2.imread(os.path.join(DATADIR,X_file_names[index])),(224,224)))
    file_name = os.path.join(DATADIR,X_file_names[index])
    print(file_name)
    image = get_image_at_index(np.array([file_name]),0)
    test_images.append(image)
    test_labels.append(get_label(os.path.join(DATADESC,y_file_names[index])))

model = load_model('restest_model')

predictions = model.predict(np.array(test_images))
'''
predictions = []
for test_image in test_images:
    prediction = model.predict(np.array([test_image]))
    print(prediction)
    predictions.append(prediction)
'''
plt.figure(figsize=(2*2*num_cols, 2*num_rows))
for i in range(num_images):
    plt.subplot(num_rows, 2*num_cols, 2*i+1)
    plot_image(i, predictions, test_labels, test_images)
    plt.subplot(num_rows, 2*num_cols, 2*i+2)
    plot_value_array(i, predictions, test_labels)
plt.show()
