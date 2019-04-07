import unittest
import modelUtils
from numpy import array
from tensorflow.keras import models
from numpy.testing import assert_allclose
from cv2 import imread,resize,flip,IMREAD_COLOR,cvtColor,COLOR_BGR2RGB

class TestModelUtilsMethods(unittest.TestCase):

    test_path_descriptions = 'D:\TrainingData\Descriptions\ISIC_0000000'
    test_path_descriptions2 = 'D:\TrainingData\Descriptions\ISIC_0000004'
    test_path_image = 'D:\TrainingData\Images\ISIC_0000000.jpeg'
 
    def test_getLabel(self):
        self.assertEqual(modelUtils.get_label(self.test_path_descriptions),0)
        self.assertEqual(modelUtils.get_label(self.test_path_descriptions2),1)
    
    def test_get_next_image(self):
        img = imread(self.test_path_image,IMREAD_COLOR)
        img = cvtColor(img,COLOR_BGR2RGB)
        img_resized = resize(img,(224,224))
        img_final = img_resized / 255.0

        img_actual = modelUtils.get_image_at_index([self.test_path_image],0)

        assert_allclose(img_actual,img_final)

    def test_open_json_file(self):
        self.assertIsNotNone(modelUtils.open_json_file(self.test_path_descriptions))

    def test_generator(self):
        expected = ([modelUtils.get_image_at_index([self.test_path_image],0)],0)
        for batch in modelUtils.generator(array([self.test_path_image]),array([self.test_path_descriptions]),1):
            actual_image, actual_label = batch
            expected_image, expected_label = expected
            assert_allclose(actual_image,expected_image)
            self.assertEqual(actual_label,expected_label)
            break

    def test_load_model(self):
        self.assertIsNotNone(modelUtils.load_model())
        with self.assertRaises(FileNotFoundError) as cm:
            modelUtils.load_model('notamodel')
        err = cm.exception
        self.assertEqual(str(err),"[Errno 2] No such file or directory: 'notamodel.json'")

if __name__ == '__main__':
    unittest.main()
