from node import node
from color import color
from direction import Direction

import csv

#Reads in file and populates connections list with connected nodes
def readFile(fileName):
    nodes = list();
    count = 0;
    with open(fileName, 'r') as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            if(len(row) > 1):
                id1 = int(row[0])
                id2 = int(row[1])
                direction = row[2]
                while(len(nodes) < id1+1 or len(nodes) < id2+1):
                    nodes.append(node(count))
                    count+=1
                direction = Direction.getDirection(direction)
                nodes[id1].addConnection(id2, direction)
                direction = Direction.getOppositeDirection(direction.name)
                nodes[id2].addConnection(id1, direction)
    return nodes

#Finds nodes that are 3 spaces away from the parent node
def makeTravelLinks(nodes):
    for node in nodes:
        for c1 in node.connections:
            for c2 in nodes[c1.id].connections:
                if c2.id == node.id:
                    continue
                if c2.direction != c1.direction:
                    continue
                for c3 in nodes[c2.id].connections:
                    if c3.id == node.id or c3.id == c1.id:
                        continue
                    if c3.direction != c2.direction:
                        continue
                    node.travelToNodes.append(c3.id)

#Performs depth first search
def dfs(nodes, id):
    nodes[id].status = color.Grey
    for n in nodes[id].travelToNodes:
        if nodes[n].status == color.White:
            nodes[n].discoveredBy = id
            dfs(nodes, n)
    nodes[id].status = color.Black

#reconstructs path based on parent node
def reconstructPath(node, nodes):
    id = node.id
    path = list()
    path.append(node.id)
    while id != 0:
        id = node.discoveredBy
        node = nodes[id]
        path.insert(0, id)
    return path

def main():
    nodes = readFile("maze.txt")
    makeTravelLinks(nodes)
    dfs(nodes, 0)
    if(nodes[-1].status == color.Black):
        path = reconstructPath(nodes[-1], nodes)
        print (path)



main()