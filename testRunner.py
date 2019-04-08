import os
os.system("coverage run testModelUtils.py")
os.system("coverage run -a testMakeDataset.py")
os.system("coverage run -a testTrainWithoutGen.py")
os.system("coverage run -a testTrainWithGen.py")
os.system("coverage report")
os.system("coverage xml -i")
os.system("move coverage.xml cov.xml")