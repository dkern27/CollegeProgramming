#include "Point.h"
#include <vector>
#include <cmath>

using namespace std;

#pragma region Constructors

Point::Point()
{
    this->x = 0;
    this->y = 0;
    this->z = 0;
    this->id = INT_MAX;
}

Point::Point(int x, int y, int z, int id)
{
    this->x = x;
    this->y = y;
    this->z = z;
    this->id = id;
}

#pragma endregion

double Point::getDistanceToOrigin()
{
    double dist = sqrt(pow(x,2) + pow(y,2) + pow(z,2));
    if (x < 0)
        dist = dist * -1;
    //dist = x + y + z;
    return dist;
}

int Point::calculateDistance(Point p)
{
    return fabs(x - p.getX()) + fabs(y - p.getY()) + fabs(z - p.getZ());
}

int Point::calculateAllDistances(vector<Point> points)
{
    int maxDist = INT_MIN;
    for (int i = 0; i < points.size(); i++)
    {
        for (int j = i; j < points.size(); j++)
        {
            int dist = points[i].calculateDistance(points[j]);
            if (dist > maxDist)
                maxDist = dist;
        }
    }
    return maxDist;
}

bool Point::equals(Point p)
{
    if (this->x == p.getX() && this->y == p.getY() && this->z == p.getZ() && this->id == p.getId())
        return true;
    return false;
}

#pragma region Testing functions

string Point::getString()
{
    return to_string(getX()) + " " + to_string(getY()) + " " + to_string(getZ()) + " ";
}

#pragma endregion

#pragma region getters/setter
int Point::getX()
{
    return this->x;
}

int Point::getY()
{
    return this->y;
}

int Point::getZ()
{
    return this->z;
}

int Point::getId()
{
    return this->id;
}

void Point::setX(int x)
{
    this->x = x;
}

void Point::setY(int y)
{
    this->y = y;
}

void Point::setZ(int z)
{
    this->z = z;
}

void Point::setId(int id)
{
    this->id = id;
}

#pragma endregion


