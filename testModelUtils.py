import unittest
import modelUtils

class TestModelUtilsMethods(unittest.TestCase):

    test_path_descriptions = 'D:\TrainingData\Descriptions\ISIC_0000000'
    test_path_image = 'D:\TrainingData\Images\ISIC_0000000.jpeg'
 
    def test_getLabel(self):
        self.assertEqual(modelUtils.getLabel(self.test_path_descriptions),0)
    
    def test_get_next_image(self):


if __name__ == '__main__':
    unittest.main()
