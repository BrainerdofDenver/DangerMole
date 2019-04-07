import numpy as np
from tensorflow.keras.models import model_from_json
from tensorflow.keras.initializers import glorot_uniform
import json
import cv2
import os

def get_label(file_name):
    data = open_json_file(file_name)

    if 'meta' in data:
        if 'clinical' in data['meta']:
            if 'benign_malignant' in data['meta']['clinical']:
                if data['meta']['clinical']['benign_malignant'] == 'benign':
                    return 0
                else:
                    return 1

def get_image_at_index(features,index):
    
    img = cv2.imread(str(features[index]),1)
    img = cv2.cvtColor(img,cv2.COLOR_BGR2RGB)
    img_resized = cv2.resize(img,(224,224))
    img_final = img_resized / 255.0
    return img_final

def open_json_file(file_name):
    with open(file_name) as f:
        data = json.load(f)
        return data

def generator(features, labels, batch_size):
    batch_features = np.zeros((batch_size,224,224,3))
    batch_labels = np.zeros((batch_size,1))
    
    while True:
        for i in range(batch_size):
            index = int(np.random.choice(len(features),1))
            img = get_image_at_index(features,index)

            label = get_label(str(labels[index]))
            batch_features[i] = img
            batch_labels[i] = label
        yield batch_features, batch_labels

def load_model(model_name='model'):
    json_file = open(model_name + '.json','r')
    loaded_model_json = json_file.read()
    json_file.close()
    model = model_from_json(loaded_model_json,custom_objects={"GlorotUniform": glorot_uniform})
    model.load_weights(model_name + '.h5')
    return model
