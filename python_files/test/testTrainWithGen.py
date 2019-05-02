import unittest
import modelUtils
import trainWithGen
import os
from numpy.testing import assert_array_equal
from numpy import array

class TestModelUtilsMethods(unittest.TestCase):
    x_file_names = []
    y_file_names = []
    x_file_names_expected = []
    y_file_names_expected = []
    DATADIR = 'D:\TrainingData\Images'
    DATADESC = 'D:\TrainingData\Descriptions'

    @classmethod
    def setUpClass(self):
        for i in range(10):
            self.x_file_names.append("testx" + str(i))
            self.y_file_names.append("testy" + str(i))
            self.x_file_names_expected.append(os.path.join(self.DATADIR,"testx" + str(i)))
            self.y_file_names_expected.append(os.path.join(self.DATADESC,"testy" + str(i)))
        self.y_file_names_expected = array(self.y_file_names_expected)
        self.x_file_names_expected = array(self.x_file_names_expected)

    def test_setup_full_file_names(self):
        y_file_names_actual, x_file_names_actual = trainWithGen.setup_full_file_names(self.x_file_names,self.y_file_names)
        assert_array_equal(x_file_names_actual,self.x_file_names_expected)
        assert_array_equal(y_file_names_actual,self.y_file_names_expected)

if __name__ == '__main__':
    unittest.main()
