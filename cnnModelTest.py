'''
Base cnn model.
'''
import tensorflow as tf
from tensorflow.keras.models import Sequential, model_from_json
from tensorflow.keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.metrics import roc_curve,roc_auc_score,classification_report
from sklearn.model_selection import train_test_split
from generator import Generator
import random
import os
import gc

DATADIR = 'E:/school/tensorflowprc/moleDataSet/Data/Images'
# Amount of test data
TEST_PERCENT = 0.2
batch_size = 32

X_file_names = os.listdir(DATADIR)
y_file_names = os.listdir(os.path.join(os.path.dirname(DATADIR),'Descriptions'))

X_train_file_names, X_val_file_names, y_train_file_names, y_val_file_names = train_test_split(X_file_names,y_file_names,test_size=0.2,random_state=2)

num_training_samples = len(X_train_file_names)
num_validation_samples= len(X_val_file_names)

my_training_batch_generator = Generator(X_train_file_names, y_train_file_names, batch_size)
my_validation_batch_generator = Generator(X_val_file_names, y_val_file_names, batch_size)

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

model.fit_generator(generator=my_training_batch_generator,
                    steps_per_epoch=(num_training_samples // batch_size),
                    epochs=5,
                    verbose=1,
                    validation_data=my_validation_batch_generator,
                    validation_steps=(num_validation_samples // batch_size),
                    use_multiprocessing=True,
                    workers=16,
                    )

