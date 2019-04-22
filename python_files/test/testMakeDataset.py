import unittest
import modelUtils
import makeDataset
import numpy as np
from numpy.testing import assert_allclose

class TestModelUtilsMethods(unittest.TestCase):

    test_path_descriptions = 'D:\TrainingData\Descriptions\ISIC_0000000'
    test_path_descriptions2 = 'D:\TrainingData\Descriptions\ISIC_0000004'
    test_path_image = 'D:\TrainingData\Images\ISIC_0000000.jpeg'

    def test_create_training_data(self):
        expected_x_value = modelUtils.get_image_at_index([self.test_path_image],0)
        expected_y_value = 0
        makeDataset.create_training_data(1,'x_data_test','y_data_test')
        actual_x_value = np.load('x_data_test.npy')[0] / 255.0
        actual_y_value = np.load('y_data_test.npy')[0]
        assert_allclose(expected_x_value,actual_x_value)
        self.assertEqual(expected_y_value,actual_y_value)

if __name__ == '__main__':
    unittest.main()