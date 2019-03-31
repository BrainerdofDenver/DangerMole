import unittest
import modelUtils
from numpy.testing import assert_allclose
from cv2 import imread,resize,flip,IMREAD_COLOR,cvtColor,COLOR_BGR2RGB

class TestModelUtilsMethods(unittest.TestCase):

    test_path_descriptions = 'D:\TrainingData\Descriptions\ISIC_0000000'
    test_path_descriptions2 = 'D:\TrainingData\Descriptions\ISIC_0000004'
    test_path_image = 'D:\TrainingData\Images\ISIC_0000000.jpeg'
 
    def test_getLabel(self):
        self.assertEqual(modelUtils.getLabel(self.test_path_descriptions),0)
        self.assertEqual(modelUtils.getLabel(self.test_path_descriptions2),1)
    
    def test_get_next_image(self):
        img = imread(self.test_path_image,IMREAD_COLOR)
        img = cvtColor(img,COLOR_BGR2RGB)
        img_resized = resize(img,(224,224))
        img_final = img_resized / 255.0

        img_actual, index_actual = modelUtils.get_next_image_index([self.test_path_image])

        assert_allclose(img_actual,img_final)
        self.assertEqual(index_actual, 0)

if __name__ == '__main__':
    unittest.main()
