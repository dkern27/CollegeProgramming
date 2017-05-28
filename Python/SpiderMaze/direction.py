from enum import Enum

class Direction(Enum):
    N = 0
    NE = 1
    E = 2
    SE = 3
    S = 4
    SW = 5
    W = 6
    NW = 7

    @staticmethod
    def getDirection(direction):
        return {
            'N' : Direction.N,
            'NE' : Direction.NE,
            'E' : Direction.E,
            'SE' : Direction.SE,
            'S' :Direction.S,
            'SW' : Direction.SW,
            'W' : Direction.W,
            'NW' : Direction.NW
        }[direction]

    @staticmethod
    def getOppositeDirection(direction):
        return {
            'S' : Direction.N,
            'SW' : Direction.NE ,
            'W' : Direction.E ,
            'NW' : Direction.SE ,
            'N' : Direction.S ,
            'NE' : Direction.SW ,
            'E' : Direction.W ,
            'SE' : Direction.NW
        }[direction]