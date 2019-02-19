import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense,Dropout,Activation,Flatten,Conv2D,MaxPooling2D
import numpy as np

TEST_PERCENT = 0.8

X = np.load('X_data.npy')
X = X/255.0

howMany = int(round(TEST_PERCENT * len(X)))

X_test = X[howMany:len(X)]
X = X[0:howMany]

y = np.load('y_data.npy')
y_test = y[howMany:len(y)]
y = y[0:howMany]

model = Sequential()
model.add(Conv2D(64,(3,3),activation='relu', input_shape = X.shape[1:]))
model.add(MaxPooling2D(pool_size=(2,2)))

model.add(Conv2D(32,(3,3),activation='relu'))
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
#with tf.device("/gpu:0"):
model.fit(X,y,batch_size = 32,epochs=2,validation_split=0.1)
print(model.evaluate(X_test,y_test,batch_size=32))
'''with tf.Session() as sess:
    sess.run()
    '''
i = 0
for img in X_test:
    print(model.predict(img))
    print(y_test[i])
    i += 1
