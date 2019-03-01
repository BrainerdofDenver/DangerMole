import cv2
import numpy as np
import json
import tensorflow as tf
from tensorflow import keras
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D
from keras import backend as K
from matplotlib import pyplot as plt
from PIL import Image as Img


baseAddrDescriptions = 'E:/\school/tensorflowprc/moleDataSet/Data/Descriptions/'
baseAddrImages = 'E:/\school/tensorflowprc/moleDataSet/Data/Images/'
#tf.enable_eager_execution()

def createFileNames(numNames):
    baseFileName = 'ISIC_'
    fileNames = []
    for i in range(numNames):
        fileNames.append(baseFileName + str(i).zfill(7) + '.jpeg')
    return fileNames

def getLabels(numLabels,fileNames):
    labels = []
    for i in range(numLabels):
        address = baseAddrDescriptions + fileNames[i][:-5]
        data = openJsonFile(address)
        if data['meta']['clinical']['benign_malignant'] == 'benign':
            labels.append(0)
        else:
            labels.append(1)
    return labels

def openJsonFile(fileName):
    with open(fileName) as f:
        data = json.load(f)
        return data

def createDataset(fileNames):
    labels = getLabels(len(fileNames),fileNames)
    print(fileNames)
    print(labels)

    def _parseFunction(fileName, label):
        image_string = tf.read_file(baseAddrImages + fileName)
        image_decoded = tf.image.decode_jpeg(image_string, channels=3)
        image_resized = tf.image.resize_images(image_decoded,[224,224])
        return image_resized, label

    dataset = tf.data.Dataset.from_tensor_slices((tf.constant(fileNames),tf.constant(labels)))
    dataset = dataset.map(_parseFunction)
    return dataset

