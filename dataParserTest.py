import unittest
import dataParser

class TestDataParserMethods(unittest.TestCase):
    def test_createFileNames(self):
        actual = dataParser.createFileNames(1)
        expected = ['ISIC_0000000.jpeg']
        self.assertEqual(actual,expected)
    
    def test_getLabels(self):
        actual = dataParser.getLabels(1,dataParser.createFileNames(1))
        expected = [0]
        self.assertEqual(actual,expected)

if __name__ == '__main__':
    unittest.main()

