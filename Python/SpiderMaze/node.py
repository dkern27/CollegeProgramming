from color import color
from connection import Connection

class node:
    def __init__(self, id):
        self.id = id;
        self.connections = list()
        self.travelToNodes = list()
        self.status = color.White
        self.discoveredBy = -1;

    #only add if not already in list
    def addConnection(self, id, direction):
        for connection in self.connections:
            if connection.id == id and connection.direction == direction:
                return
        self.connections.append(Connection(id,direction))

    #Only add if not already in list
    def addTravelToNode(self, id):
        if id not in self.travelToNodes:
            self.travelToNodes.append(id)

    #For testing
    def toString(self):
        message = "id: {0}\n".format(self.id)
        message += "Connections: "
        for connection in self.connections:
            message += "{0} ".format(connection.id)
        message += '\n'
        message += "Can travel to: "
        for i in self.travelToNodes:
            message += "{0} ".format(i)
        message +=  '\n'
        message += "Discovered: {0}".format(self.status)
        return message