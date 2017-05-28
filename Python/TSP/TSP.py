import random
import itertools
import csv
import os.path
from timeit import default_timer as timer

from Point import Point
from Tour import Tour

#<editor-fold desc = "Nearest Neighbor">
def doNearestNeighbor(points):
    start = timer()
    tour = nearestNeighbor(points)
    end = timer()
    print 'Nearest Neighbor'
    print 'Path Taken:', tour.toString()
    print 'Distance Traveled: ', tour.pathLength
    print 'Time Taken: ', end - start

def nearestNeighbor(points):
    p0 = p = points[0]
    points[0].visited = True
    i = 0
    tour = Tour(list(), 0)
    tour.path.append(p0)
    while i < len(points)-1:
        i += 1
        nextPoint = p.findNearestPoint(points)
        tour.pathLength += p.distance(nextPoint)
        p = nextPoint
        tour.path.append(p)
    tour.pathLength += p.distance(p0)
    return tour
#</editor-fold>

#<editor-fold desc = "Exhaustive Search">
def doExhaustiveSearch(points):
    start = timer()
    tour = exhaustiveSearch(points)
    end = timer()
    print 'Exhaustive Search'
    print 'Path Taken:', tour.toString()
    print 'Distance Traveled: ', tour.pathLength
    print 'Time Taken: ', end - start

def exhaustiveSearch(points):
    p0 = points[0]
    points.remove(p0)
    permutations = itertools.permutations(points)
    bestTour = Tour()
    for perm in permutations:
        dist = calculatePermutationCost(p0, perm)
        if(dist < bestTour.pathLength):
            bestTour.path = perm
            bestTour.pathLength = dist
    bestTour.path = list(bestTour.path)
    bestTour.path.insert(0, p0)
    return bestTour

def calculatePermutationCost(p0, perm):
    totalDist = p0.distance(perm[0])
    i = 0
    while i != len(perm)-1:
        totalDist += perm[i].distance(perm[i+1])
        i+=1
    totalDist += perm[-1].distance(p0)
    return totalDist

#</editor-fold>

#<editor-fold desc="Make/Get points">
def generatePoints(n):
    points = list()
    random.seed()
    while len(points) < n:
        point = Point(random.randint(0, 100), random.randint(0, 100))
        if not any(point.equals(item) for item in points):
            points.append(point)
    return points

def readFile(fileName):
    points = list()
    with open(fileName, 'r') as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            if(len(row) > 1):
                x = int(row[0])
                y = int(row[1])
                points.append(Point(x, y))
    return points

def writeFile(points):
    with open("points.txt", 'w') as file:
        file.write(str(len(points)) + '\n')
        for point in points:
            file.write(str(point.x) + ' ' + str(point.y) + '\n')

#</editor-fold>

def main():
    userInput = raw_input('Enter a filename or the number of points to generate: ')
    if userInput.isdigit():
        points = generatePoints(int(userInput))
        writeFile(points)
    elif(os.path.isfile(userInput)):
        points = readFile(userInput)
    else:
        print 'Invalid input: File does not exist or input is not a number'
        exit()
    print ''
    doNearestNeighbor(points)
    print ''
    doExhaustiveSearch(points)

main()
