'''
Base cnn model.
'''
import tensorflow as tf
from keras.models import Sequential, model_from_json,Model
from keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D,Input
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
import cv2
from sklearn.metrics import roc_curve,roc_auc_score,classification_report
from sklearn.model_selection import train_test_split
from sklearn.utils import class_weight
import random
import os
#from resnet50 import ResNet50
from keras.applications.resnet50 import preprocess_input,ResNet50
from modelUtils import generator,openJsonFile,getLabel
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

print(X_file_names[0])
print(X_file_names_full[0])
X_file_names_full = np.array(X_file_names_full)
y_file_names_full = np.array(y_file_names_full)
ytest = [getLabel(y_file_names_full[i]) for i in range(0,len(y_file_names_full))]
print(ytest[0])
print(len(X_file_names_full))
steps = np.ceil(len(X_file_names_full) / batch_size)
print(steps)

'''
X_load = np.load('X_data.npy')
y_load = np.load('y_data.npy')
benign = []
malignant = []

for i in range(len(X_load)):
    if y_load[i] == 0:
        benign.append([X_load[i],y_load[i]])
    else:
        malignant.append([X_load[i],y_load[i]])
print(type(benign))
print(len(malignant))

# Don't know if this actually does anything but im leaving it here for now
gc.collect()
del X_load
del y_load

X= []
y= []

for i in range(1000):
    malignant.append(benign[i])
print(type(malignant))

for feature,label in malignant:
    X.append(feature)
    y.append(label)

X= np.asarray(X)
y= np.asarray(y)
X= X/255.0
X = preprocess_input(X)

print(X.shape)
X_train, X_test, y_train, y_test = train_test_split(X,y,test_size=0.2,random_state=2)
'''
#class_weight = {0: 25.,1: 1.}
class_weights = class_weight.compute_class_weight('balanced',np.unique(ytest),ytest)
print(class_weights)
image_input = Input(shape=(224,224,3))

model = ResNet50(include_top=True, weights='imagenet',
             input_tensor=image_input)

#model.summary()
last_layer = model.get_layer('avg_pool').output
x = Flatten(name='flatten')(last_layer)
out = Dense(1,activation='relu',name='output_layer')(x)
custom_resnet_model = Model(inputs=image_input,outputs=out)
custom_resnet_model.summary()

for layer in custom_resnet_model.layers[:-1]:
    layer.trainable = False

custom_resnet_model.layers[-1].trainable

custom_resnet_model.compile(loss='binary_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

#custom_resnet_model.fit(X_train,y_train,batch_size = 32,epochs=15,shuffle=True,validation_split=0.1)
custom_resnet_model.fit_generator(generator(X_file_names_full, y_file_names_full, batch_size),
                    steps_per_epoch=steps,
                    epochs=1,
                    class_weight=class_weights)


#scores = custom_resnet_model.evaluate(X_test,y_test)
#print("%s: %.2f%%" % (custom_resnet_model.metrics_names[1], scores[1] * 100))

# Saves model along with weights
model_json = custom_resnet_model.to_json()
with open("resModel.json", "w") as json_file:
    json_file.write(model_json)

custom_resnet_model.save_weights("resModel.h5")
print("saved model")

# Just doing my own little test to make sure im not crazy
predictions = custom_resnet_model.predict(X_test)
i = 0
for prediction in predictions:
    print(prediction)
    print(y_test[i])
    i += 1

above_threshold_indices = predictions > 0.5
below_threshold_indices = predictions < 0.5
predictions[above_threshold_indices] = 1
predictions[below_threshold_indices] = 0

# most of this block was taken from https://towardsdatascience.com/building-a-logistic-regression-in-python-step-by-step-becd4d56c9c8
logit_roc_auc = roc_auc_score(y_test, predictions)
fpr, tpr, thresholds = roc_curve(y_test, predictions)
plt.figure()
plt.plot(fpr, tpr, label='Binary classification(area = %0.2f)' % logit_roc_auc)
plt.plot([0, 1], [0, 1],'r--')
plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('Receiver operating characteristic')
plt.legend(loc="lower right")
plt.savefig('Log_ROC')
plt.show()

print(classification_report(y_test,predictions))
