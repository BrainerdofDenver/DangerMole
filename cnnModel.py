'''
Base cnn model. Currently doesn't work acc and lose don't change at all
'''
import tensorflow as tf
from tensorflow.keras.models import Sequential, model_from_json
from tensorflow.keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.metrics import roc_curve,roc_auc_score,classification_report

# Amount of test data
TEST_PERCENT = 0.2

X = np.load('X_data.npy')
y = np.load('y_data.npy')

# takes a sections of the dataset to train on, otherwise too big to fit into memory
X = X[3000:5000]
y = y[3000:5000]

X = X/255.0

# Partition for test and train data
howMany = int(round(1 - TEST_PERCENT * len(X)))

X_test = X[howMany:len(X)]
X = X[0:howMany]

# Was confused about y so I printed them out. Saw that there are much more benign than malignant
'''for label in y:
    print(label)
'''
y_test = y[howMany:len(y)]
y = y[0:howMany]
print(y_test)

model = Sequential()
model.add(Conv2D(64,(3,3),activation='sigmoid', input_shape = (224,224,3)))
model.add(MaxPooling2D(pool_size=(2,2)))

model.add(Conv2D(32,(3,3),activation='sigmoid'))
model.add(MaxPooling2D(pool_size=(2,2)))

model.add(Flatten())
model.add(Dense(1,activation='sigmoid'))
'''
model.add(Dense(1))
model.add(Activation('sigmoid'))
'''
model.compile(loss='binary_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

model.fit(X,y,batch_size = 64,epochs=2,shuffle=True,validation_split=0.1)
scores = model.evaluate(X_test,y_test)
print("%s: %.2f%%" % (model.metrics_names[1], scores[1] * 100))

# Saves model along with weights
model_json = model.to_json()
with open("model.json", "w") as json_file:
    json_file.write(model_json)

model.save_weights("model.h5")
print("saved model")

# Just doing my own little test to make sure im not crazy
predictions = model.predict(X_test)
i = 0
fpr = 0
tpr = 0
for prediction in predictions:
    print(prediction)
    print(y_test[i])
    i += 1

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
