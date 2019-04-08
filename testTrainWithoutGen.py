import unittest
import modelUtils
import makeDataset
import trainWithoutGen
import os
import numpy as np

class TestModelUtilsMethods(unittest.TestCase):
    XDATA = 'X_data_for_test.npy'
    YDATA = 'y_data_for_test.npy'

    @classmethod
    def setUpClass(self):
        makeDataset.create_training_data(10,self.XDATA,self.YDATA)

    @classmethod
    def tearDownClass(self):
        os.remove(self.XDATA)
        os.remove(self.YDATA)
        
    def test_create_base_data(self):
        actual_benign, actual_malignant = trainWithoutGen.create_base_data(self.XDATA,self.YDATA)
        self.assertEqual(len(actual_benign),8)
        self.assertEqual(len(actual_malignant),2)

    def test_create_subset_data(self):
        benign,malignant = trainWithoutGen.create_base_data(self.XDATA,self.YDATA)
        actual_X, actual_y = trainWithoutGen.create_subset_of_data(malignant,benign,1)
        self.assertEqual(len(actual_X),3)

    def test_plot_roc(self):
        predictions = np.random.rand(10)
        y_test = np.random.randint(2,size=10)
        trainWithoutGen.plot_roc(predictions,y_test)


if __name__ == '__main__':
    unittest.main()