'''
Trains a model using numpy arrays for the data instead of generators
'''
import tensorflow as tf
from keras.optimizers import SGD
import numpy as np
import matplotlib.pyplot as plt
from random import shuffle
from sklearn.metrics import roc_curve,roc_auc_score,classification_report
from sklearn.model_selection import train_test_split
from models import get_res_model, get_cnn_model
from modelUtils import save_model
import random
import gc
from memory_profiler import profile


@profile 
def main():
    # Amount of test data
    TEST_PERCENT = 0.2

    benign, malignant = create_base_data('testx.npy','testy.npy')
    X, y = create_subset_of_data(malignant, benign,100)
    X= X.astype('float32')/255.0
    print(X.shape)
    print(y.shape)
    X_train, X_test, y_train, y_test = train_test_split(X,y,test_size=TEST_PERCENT,random_state=2)

    custom_resnet_model = get_res_model()
                
    custom_resnet_model.fit(X_train,y_train,batch_size = 32,epochs=50,shuffle=True,validation_split=0.1)            

    scores = custom_resnet_model.evaluate(X_test,y_test)
    print("%s: %.2f%%" % (custom_resnet_model.metrics_names[1], scores[1] * 100))

    # Saves model along with weights
    save_model(custom_resnet_model,'testresModel')
    
    predictions = custom_resnet_model.predict(X_test)
    plot_roc(predictions, y_test)

def plot_roc(predictions, y_test):
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
    

def create_subset_of_data(malignant, benign, amount_of_benign_examples=100):
    X= []
    y= []

    if len(benign) is not 0:
        for i in range(amount_of_benign_examples):
            malignant.append(benign[i])
            if i > len(benign):
                break
        print(len(malignant))

    for feature,label in malignant:
        X.append(feature)
        y.append(label)

    print(len(X))
    X= np.asarray(X)
    y= np.asarray(y)
    return X, y

def create_base_data(x_data_name,y_data_name):
    X_load = np.load(x_data_name)
    y_load = np.load(y_data_name)
    print(X_load.shape)
    print(y_load.shape)
    benign = []
    malignant = []

    for i in range(len(X_load)):
        if y_load[i] == 0:
            benign.append([X_load[i],y_load[i]])
        else:
            malignant.append([X_load[i],y_load[i]])
    return benign, malignant

if __name__ == "__main__":
    main()
