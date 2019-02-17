import cv2
import numpy as np
import json
import tensorflow as tf

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
        #print(address)
        data = openJsonFile(address)
        #print(data['meta']['clinical']['benign_malignant'])
        if data['meta']['clinical']['benign_malignant'] == 'benign':
            labels.append(0)
        else:
            labels.append(1)
    return labels

def openJsonFile(fileName):
    with open(fileName) as f:
        data = json.load(f)
        #print(data)
        return data
'''
def load_image(addr):
    img = cv2.imread(addr)
    if img is None:
        return None
    img = cv2.resize(img, (224,224), interpolation=cv2.INTER_CUBIC)
    return img
'''
def createDataset(fileNames):
    labels = getLabels(len(fileNames),fileNames)
    print(fileNames)
    print(labels)
    def _parseFunction(fileName, label):
        image_string = tf.read_file(baseAddrImages + fileName)
        image_decoded = tf.image.decode_jpeg(image_string, channels=3)
        return image_decoded, label
    dataset = tf.data.Dataset.from_tensor_slices((tf.constant(fileNames),tf.constant(labels)))
    dataset = dataset.map(_parseFunction)
    return dataset

def main():
    fileNames = createFileNames(10)
    dataset = createDataset(fileNames)
    #train = dataset.take(5)
    iterator = dataset.make_one_shot_iterator()
    images, labels = iterator.get_next()

    with tf.Session() as sess:
        print(sess.run(labels))

if __name__ == '__main__':
    main()
