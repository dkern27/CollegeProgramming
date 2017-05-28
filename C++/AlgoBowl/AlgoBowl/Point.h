#pragma once

#include <string>
#include <vector>

class Point
{
public:
    Point();
    Point(int x, int y, int z, int id);

    int getX();
    int getY();
    int getZ();
    int getId();
    void setX(int);
    void setY(int);
    void setZ(int);
    void setId(int);

    double getDistanceToOrigin();
    int calculateDistance(Point);
    static int calculateAllDistances(std::vector<Point>);
    bool equals(Point);

    //Testing
    std::string getString();

private:
    int x;
    int y;
    int z;
    int id;

};

