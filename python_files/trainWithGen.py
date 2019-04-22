'''
Train resModel using generators
'''
import tensorflow as tf
from keras.models import Sequential, model_from_json,Model
from keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D,Input
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.utils import class_weight
import os
from models import get_res_model
from modelUtils import generator,open_json_file,get_label,save_model

DATADIR = 'D:\TrainingData\Images'
DATADESC = 'D:\TrainingData\Descriptions'
    
def main():
    TEST_PERCENT = 0.2
    batch_size = 32

    X_file_names = os.listdir(DATADIR)
    y_file_names = os.listdir(DATADESC)

    y_file_names_full, X_file_names_full = setup_full_file_names(X_file_names,y_file_names)
    ytest = [get_label(y_file_names_full[i]) for i in range(0,len(y_file_names_full))]
    
    steps = np.ceil(len(X_file_names_full) / batch_size)

    class_weights = class_weight.compute_class_weight('balanced',np.unique(ytest),ytest)

    custom_resnet_model = get_res_model()

    custom_resnet_model.fit_generator(generator(X_file_names_full, y_file_names_full, batch_size),
                        steps_per_epoch=10,
                        epochs=10,
                        class_weight=class_weights)

    save_model(custom_resnet_model,"resModel")

def setup_full_file_names(X_file_names, y_file_names):
    X_file_names_full = []
    y_file_names_full = []

    for file_name in X_file_names:
        X_file_names_full.append(os.path.join(DATADIR, file_name))

    for file_name in y_file_names:
        y_file_names_full.append(os.path.join(DATADESC, file_name))

    X_file_names_full = np.array(X_file_names_full)
    y_file_names_full = np.array(y_file_names_full)
    return y_file_names_full, X_file_names_full

if __name__ == "__main__":
    main()