import math
import sys

class Point:

    x = 0
    y = 0
    visited = False

    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y
        self.visited = False

    def distance(self, point):
        return math.sqrt((self.x - point.x)**2 + (self.y - point.y)**2)

    def findNearestPoint(self, points):
        closestPoint = Point()
        minDist = sys.maxint
        for point in points:
            if point.visited:
                continue
            dist = self.distance(point)
            if dist < minDist:
                closestPoint = point
                minDist = dist
        closestPoint.visited = True
        return closestPoint

    def equals(self, p):
        if self.x == p.x and self.y == p.y:
            return True
        return False

    def toString(self):
        return '({0},{1})'.format(self.x, self.y)
