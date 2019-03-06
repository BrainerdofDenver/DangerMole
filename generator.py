import cv2
import numpy as np
import os
import json
from keras.utils import Sequence

class My_Generator(Sequence):

    def getLabel(self,file_name):
        data = openJsonFile(file_name[:-5])
        if 'meta' in data:
            if 'clinical' in data['meta']:
                if 'benign_malignant' in data['meta']['clinical']:
                    if data['meta']['clinical']['benign_malignant'] == 'benign':
                        return 0
                    else:
                        return 1

    def openJsonFile(self,file_name):
        with open(file_name) as f:
            data = json.load(f)
            return data
    def print_base(self):
        for base in self.__class__.__bases__:
            print (base.__name__)


    def __init__(self,image_filenames, desc_filenames, batch_size):
        self.image_filenames, self.desc_filenames = image_filenames, desc_filenames
        self.batch_size = batch_size

    def __len__(self):
        return int(np.ceil(len(self.image_filenames) / float(self.batch_size)))

    def __getitem__(self,idx):
        batch_x = self.image_filenames[idx * self.batch_size:(idx + 1) * self.batch_size]
        batch_y_filenames = self.desc_filenames[idx * self.batch_size:(idx + 1) * self.batch_size]
        batch_y = []

        for file_name in batch_y_filenames:
            batch_y.append(getLabel(file_name))

        return np.array([cv2.resize(cv2.imread(file_name), (224,224))/255.0 for file_name in batch_x]), np.array(batch_y)

