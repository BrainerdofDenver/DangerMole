import tensorflow as tf
from keras.models import Sequential, model_from_json,Model
from keras.layers import Dense,Dropout,Flatten,Conv2D,MaxPooling2D,Input
from keras.applications.resnet50 import preprocess_input,ResNet50

def get_cnn_model():
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

    return model

def get_res_model():
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

        return custom_resnet_model