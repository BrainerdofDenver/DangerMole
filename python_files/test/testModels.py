import unittest
import models

class TestModelUtilsMethods(unittest.TestCase):
    def test_get_cnn_model(self):
        model = models.get_cnn_model()
        self.assertIsNotNone(model)

    def test_get_res_model(self):
        model = models.get_res_model()
        self.assertIsNotNone(model)
        
if __name__ == '__main__':
    unittest.main()