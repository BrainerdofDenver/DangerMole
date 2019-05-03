import tensorflow as tf
from keras.initializers import RandomNormal
from keras import regularizers
from keras.models import Sequential, model_from_json,Model
from keras.layers import Dense,Dropout,Flatten,Conv2D,MaxPooling2D,Input
from keras.applications.resnet50 import preprocess_input,ResNet50

def get_cnn_model():
    model = Sequential()
    model.add(Conv2D(32,(3,3),activation='relu', input_shape = (224,224,3),data_format='channels_last',
              kernel_initializer=RandomNormal(mean=0.0,stddev=1e-2,seed=None),kernel_regularizer=regularizers.l2(0.),
              use_bias=False, bias_initializer=RandomNormal(mean=0.0,stddev=1e-2)))
    model.add(MaxPooling2D(pool_size=(2,2),strides=2))

    model.add(Conv2D(64,(3,3),activation='relu',kernel_initializer=RandomNormal(mean=0.0,stddev=1e-2,seed=None),
              kernel_regularizer=regularizers.l2(0.),use_bias=False, bias_initializer=RandomNormal(mean=0.0,stddev=1e-2)))
    model.add(MaxPooling2D(pool_size=(2,2)))

    model.add(Flatten())
    model.add(Dense(64,activation='relu'))
    model.add(Dropout(0.25))

    model.add(Dense(1,activation='sigmoid'))

    model.compile(loss='binary_crossentropy',
            optimizer='adam',
            metrics=['accuracy'],
            sample_weight_mode=None)
    tf.global_variables_initializer()
    return model

def get_res_model():
        image_input = Input(shape=(224,224,3))

        model = ResNet50(include_top=False, weights=None,
                input_tensor=image_input,input_shape=(224,224,3))

        last_layer = model.get_layer('avg_pool').output
        x = Flatten(name='flatten')(last_layer)
        out = Dense(2048,activation='sigmoid',name='fcl-1')(x)
        out = Dense(2048,activation='sigmoid',name='fcl-2')(out)
        out = Dense(1,activation='sigmoid',name='output_layer')(out)
        custom_resnet_model = Model(inputs=image_input,outputs=out)
        custom_resnet_model.summary()

        custom_resnet_model.compile(loss='binary_crossentropy',
                optimizer='adam',
                metrics=['accuracy'])

        return custom_resnet_model
