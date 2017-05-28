import sys

class Tour:
    path = list()
    pathLength = sys.maxint

    def __init__(self, path = list(), pathLength = sys.maxint):
        self.path = path
        self.pathLength = pathLength

    def toString(self):
        returnString = ""
        for point in self.path:
            returnString += point.toString()
            returnString += ' '
        return returnString