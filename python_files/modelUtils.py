import numpy as np
from tensorflow.keras.models import model_from_json
from tensorflow.keras.initializers import glorot_uniform
import json
import cv2
import os
import imgaug as ia
from imgaug import augmenters as iaa


def get_label(file_name):
    data = open_json_file(file_name)

    if data['meta']['clinical']['benign_malignant'] == 'benign':
        return 0
    else:
        return 1

def get_image_at_index(features,index):
    img = cv2.imread(str(features[index]),1)
    img = cv2.cvtColor(img,cv2.COLOR_BGR2RGB)
    img_resized = cv2.resize(img,(224,224))
    img_final = img_resized.astype('float32') / 255.0
    return img_final

def get_image_at_index_with_transform(features,index):   
    img = cv2.imread(str(features[index]),1)
    img = cv2.cvtColor(img,cv2.COLOR_BGR2RGB)
    img_resized = cv2.resize(img,(224,224))
    img_transformed = random_augment_images(img_resized)
    img_final = img_transformed.astype('float32') / 255.0
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
            img = get_image_at_index_with_transform(features,index)
            label = get_label(str(labels[index]))
            batch_features[i] = img
            batch_labels[i] = [label]

        yield batch_features, batch_labels

def load_model(model_name='model'):
    json_file = open(model_name + '.json','r')
    loaded_model_json = json_file.read()
    json_file.close()
    model = model_from_json(loaded_model_json,custom_objects={"GlorotUniform": glorot_uniform})
    model.load_weights(model_name + '.h5')
    return model

def save_model(model_to_save,model_name='model'):
    model_json = model_to_save.to_json()
    with open(model_name + ".json", "w") as json_file:
        json_file.write(model_json)

    model_to_save.save_weights(model_name + ".h5")
    print("saved model")

# From https://github.com/aleju/imgaug I've adjusted some params though
def random_augment_images(image):
    sometimes = lambda aug: iaa.Sometimes(0.5, aug)

    # Define our sequence of augmentation steps that will be applied to every image
    # All augmenters with per_channel=0.5 will sample one value _per image_
    # in 50% of all cases. In all other cases they will sample new values
    # _per channel_.
    seq = iaa.Sequential(
        [
            # apply the following augmenters to most images
            iaa.Fliplr(0.5), # horizontally flip 50% of all images
            iaa.Flipud(0.2), # vertically flip 20% of all images
            # crop images by -5% to 10% of their height/width
            sometimes(iaa.CropAndPad(
                percent=(-0.05, 0.1),
                pad_mode=ia.ALL,
                pad_cval=(0, 255)
            )),
            sometimes(iaa.Affine(
                scale={"x": (0.8, 1.2), "y": (0.8, 1.2)}, # scale images to 80-120% of their size, individually per axis
                translate_percent={"x": (-0.2, 0.2), "y": (-0.2, 0.2)}, # translate by -20 to +20 percent (per axis)
                rotate=(-45, 45), # rotate by -45 to +45 degrees
                shear=(-16, 16), # shear by -16 to +16 degrees
                order=[0, 1], # use nearest neighbour or bilinear interpolation (fast)
                cval=(0, 255), # if mode is constant, use a cval between 0 and 255
                mode=ia.ALL # use any of scikit-image's warping modes (see 2nd image from the top for examples)
            )),
            # execute 0 to 5 of the following (less important) augmenters per image
            # don't execute all of them, as that would often be way too strong
            iaa.SomeOf((0, 5),
                [
                    sometimes(iaa.Superpixels(p_replace=(0, 0.2), n_segments=(150, 200))), # convert images into their superpixel representation
                    iaa.OneOf([
                        iaa.GaussianBlur((0, 1.0)), # blur images with a sigma between 0 and 3.0
                        iaa.AverageBlur(k=(2, 3)), # blur image using local means with kernel sizes between 2 and 3
                        iaa.MedianBlur(k=(3, 5)), # blur image using local medians with kernel sizes between 3 and 5
                    ]),
                    iaa.Sharpen(alpha=(0, 0.5), lightness=(0.75, 1.5)), # sharpen images
                    iaa.Emboss(alpha=(0, 1.0), strength=(0, 0.25)), # emboss images
                    # search either for all edges or for directed edges,
                    # blend the result with the original image using a blobby mask
                    iaa.SimplexNoiseAlpha(iaa.OneOf([
                        iaa.EdgeDetect(alpha=(0.25, 0.5)),
                        iaa.DirectedEdgeDetect(alpha=(0.0, 0.25), direction=(0.0, 1.0)),
                    ])),
                    iaa.AdditiveGaussianNoise(loc=0, scale=(0.0, 0.03*255), per_channel=0.5), # add gaussian noise to images
                    iaa.OneOf([
                        iaa.Dropout((0.01, 0.05), per_channel=0.5), # randomly remove up to 5% of the pixels
                    ]),
                    iaa.Add((-10, 10), per_channel=0.5), # change brightness of images (by -10 to 10 of original value)
                    iaa.AddToHueAndSaturation((-20, 20)), # change hue and saturation
                    # either change the brightness of the whole image (sometimes
                    # per channel) or change the brightness of subareas
                    iaa.OneOf([
                        iaa.Multiply((0.5, 1.5), per_channel=0.5),
                        iaa.FrequencyNoiseAlpha(
                            exponent=(-4, 0),
                            first=iaa.Multiply((0.5, 1.5), per_channel=True),
                            second=iaa.ContrastNormalization((0.5, 2.0))
                        )
                    ]),
                    iaa.GammaContrast((0.8,1.2)),
                    iaa.Grayscale(alpha=(0.0, 0.2)),
                    sometimes(iaa.ElasticTransformation(alpha=(0.01, 10.0), sigma=5.0)), # move pixels locally around (with random strengths)
                    sometimes(iaa.PiecewiseAffine(scale=(0.01, 0.05))), # sometimes move parts of the image around
                    sometimes(iaa.PerspectiveTransform(scale=(0.01, 0.1)))
                ],
                random_order=True
            )
        ],
        random_order=True
    )

    return seq.augment_image(image)
