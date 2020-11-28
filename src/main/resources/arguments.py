import multiprocessing as mp
from multiprocessing import Pool
from contextlib import closing
import os
import sys
import json

def argsss():

    print("This is node {}".format(sys.argv[2]))
    print(sys.argv[1])
    print(sys.argv[3])
    print(sys.argv[4])
    print(sys.argv[5])
    print(sys.argv[6])

    data = {
      "jobId": sys.argv[1],
      "state": sys.argv[2],
      "compactness": sys.argv[3],
      "popDifference": sys.argv[4],
      "iterations": sys.argv[5]
    }

    dirName = "districtings/{}".format(sys.argv[1])
    if not os.path.exists(dirName):
        os.makedirs(dirName)
    outputFile = "districtings/{}/{}.json".format(sys.argv[1], sys.argv[2])
    with open(outputFile, 'w+') as file:
        json.dump(data, file, ensure_ascii=False, indent=4)

    return

if __name__ == '__main__':
    argsss()

