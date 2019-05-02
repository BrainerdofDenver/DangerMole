import numpy as np
from numpy import random
import matplotlib.pyplot as plt
from sklearn.utils import class_weight
import os
from models import get_res_model
import modelUtils

DATADIR = 'D:/TrainingData/Images'
DATADESC = 'D:/TrainingData/Descriptions'

X_file_names = np.array(os.listdir(DATADIR))
y_file_names = np.array(os.listdir(DATADESC))

num_rows = 5
num_cols = 3
num_images = num_rows*num_cols
images = []
images_aug = []

for i in range(num_images):
    index = int(random.choice(len(X_file_names),1))
    #test_images.append(cv2.resize(cv2.imread(os.path.join(DATADIR,X_file_names[index])),(224,224)))
    file_name = os.path.join(DATADIR,X_file_names[index])
    print(file_name)
    image = modelUtils.get_image_at_index(np.array([file_name]),0)
    image_aug = modelUtils.get_image_at_index_with_transform(np.array([file_name]),0)
    images.append(image)
    images_aug.append(image_aug)

plt.figure(figsize=(2*2*num_cols, 2*num_rows))
for i in range(num_images):
    plt.subplot(num_rows, 2*num_cols, 2*i+1)
    plt.imshow(images[i])
    plt.xticks([])
    plt.yticks([])
    plt.subplot(num_rows, 2*num_cols, 2*i+2)
    plt.imshow(images_aug[i])
    plt.xticks([])
    plt.yticks([])
plt.show()