#include "Point.h"
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <map>

using namespace std;

vector<Point> readFile(string, int&);
void quickSortX(vector<Point>&, int, int);
void quickSortY(vector<Point>&, int, int);
void quickSortZ(vector<Point>&, int, int);
void quickSortOrigin(vector<Point>&, int, int);
vector< vector<Point> > makeGroups(vector<Point>, int);
int getMaxDistance(vector< vector<Point> >);

int main() 
{
    cout << "Enter the filename: ";
    string filenum;
    getline(cin, filenum);
    string filename = "i/input_group" + filenum +".txt";
    int numGroups = 0;
    vector<Point> points = readFile(filename, numGroups);
    //Sort by distance to origin
    quickSortOrigin(points,0, points.size()-1);
    vector< vector<Point> > ogroups = makeGroups(points,numGroups);

    //Sort by x, y, and z, and find distance from each group
    //X sort
     quickSortX(points, 0, points.size()-1);
     vector< vector<Point> > xgroups = makeGroups(points, numGroups);

     //Y sort
     quickSortY(points, 0, points.size() - 1);
     vector< vector<Point> > ygroups = makeGroups(points, numGroups);

     //Z sort
     quickSortZ(points, 0, points.size() - 1);
     vector< vector<Point> > zgroups = makeGroups(points, numGroups);

     int oDist = getMaxDistance(ogroups);
     int xDist = getMaxDistance(xgroups);
     int yDist = getMaxDistance(ygroups);
     int zDist = getMaxDistance(zgroups);

     int maxDist = min(min(xDist, yDist), min(oDist,zDist));
    ofstream ofile;
    ofile.open("outputs/" + filenum + ".txt", ios::trunc);
    cout << maxDist << endl;
    ofile << maxDist << endl;
    
    //Print group point Ids
    vector< vector<Point> > groups = xgroups;
    if (maxDist == zDist)
        groups = zgroups;
    else if (maxDist == yDist)
        groups = ygroups;
    else if (maxDist == oDist)
        groups = ogroups;

    //Print out groups
    for (vector<Point> g : groups)
    {       
        for (int i = 0; i < g.size(); i++)
        {
            cout << g[i].getId() << " ";
            ofile << g[i].getId() << " ";
        }
        cout << endl;
        ofile << endl;
    }
    return EXIT_SUCCESS;
}


vector<Point> readFile(string fileName, int& numGroups)
{
    vector<Point> points;
    ifstream file(fileName);
    int numPoints;
    file >> numPoints >> numGroups;
    for (int i = 0; i < numPoints; i++)
    {
        int x, y, z;
        file >> x >> y >> z;
        points.push_back(Point(x, y, z, i+1));
    }
    file.close();
    return points;
}


#pragma region quickSort

void quickSortOrigin(vector<Point>& points, int left, int right) {
    int i = left, j = right;
    int tmp;
    double pivot = points[(left+right)/2].getDistanceToOrigin();
    while (i <= j) {
        while (points[i].getDistanceToOrigin() < pivot)
            i++;
        while (points[j].getDistanceToOrigin() > pivot)
            j--;
        if (i <= j) {
            tmp = points[i].getX();
            points[i].setX(points[j].getX());
            points[j].setX(tmp);
            
            tmp = points[i].getY();
            points[i].setY(points[j].getY());
            points[j].setY(tmp);
            
            tmp = points[i].getZ();
            points[i].setZ(points[j].getZ());
            points[j].setZ(tmp);
            
            tmp = points[i].getId();
            points[i].setId(points[j].getId());
            points[j].setId(tmp);
            i++;
            j--;
        }
    }
    
    if (left < j)
        quickSortOrigin(points, left, j);
    if (i < right)
        quickSortOrigin(points, i, right);
}

void quickSortX(vector<Point>& points, int left, int right) {
    int i = left, j = right;
    int tmp;
    int pivot = points[(left+right)/2].getX();

    while (i <= j) {
        while (points[i].getX() < pivot)
            i++;
        while (points[j].getX() > pivot)
            j--;
        if (i <= j) {
            tmp = points[i].getX();
            points[i].setX(points[j].getX());
            points[j].setX(tmp);

            tmp = points[i].getY();
            points[i].setY(points[j].getY());
            points[j].setY(tmp);

            tmp = points[i].getZ();
            points[i].setZ(points[j].getZ());
            points[j].setZ(tmp);

            tmp = points[i].getId();
            points[i].setId(points[j].getId());
            points[j].setId(tmp);
            i++;
            j--;
        }
    };

    if (left < j)
        quickSortX(points, left, j);
    if (i < right)
        quickSortX(points, i, right);
}

void quickSortY(vector<Point>& points, int left, int right) {
    int i = left, j = right;
    int tmp;
    int pivot = points[(left + right) / 2].getY();

    while (i <= j) {
        while (points[i].getY() < pivot)
            i++;
        while (points[j].getY() > pivot)
            j--;
        if (i <= j) {
            tmp = points[i].getX();
            points[i].setX(points[j].getX());
            points[j].setX(tmp);

            tmp = points[i].getY();
            points[i].setY(points[j].getY());
            points[j].setY(tmp);

            tmp = points[i].getZ();
            points[i].setZ(points[j].getZ());
            points[j].setZ(tmp);

            tmp = points[i].getId();
            points[i].setId(points[j].getId());
            points[j].setId(tmp);
            i++;
            j--;
        }
    };

    if (left < j)
        quickSortY(points, left, j);
    if (i < right)
        quickSortY(points, i, right);
}

void quickSortZ(vector<Point>& points, int left, int right) {
    int i = left, j = right;
    int tmp;
    int pivot = points[(left + right) / 2].getZ();

    while (i <= j) {
        while (points[i].getZ() < pivot)
            i++;
        while (points[j].getZ() > pivot)
            j--;
        if (i <= j) {
            tmp = points[i].getX();
            points[i].setX(points[j].getX());
            points[j].setX(tmp);

            tmp = points[i].getY();
            points[i].setY(points[j].getY());
            points[j].setY(tmp);

            tmp = points[i].getZ();
            points[i].setZ(points[j].getZ());
            points[j].setZ(tmp);

            tmp = points[i].getId();
            points[i].setId(points[j].getId());
            points[j].setId(tmp);
            i++;
            j--;
        }
    };

    if (left < j)
        quickSortZ(points, left, j);
    if (i < right)
        quickSortZ(points, i, right);
}

#pragma endregion

vector< vector<Point> > makeGroups(vector<Point> points, int numGroups)
{
    vector< vector<Point> > allGroups;
    //Here x is point index, y is distance to farthest point
    
    //Get Increment for other groups
    int numPoints = points.size();
    if (points.size() % 2 == 1)
        numPoints--;
    int groupIncr = round(double(points.size()) / double(numGroups-1));

    int adjustGroups = 1;
    
    //Check if origin is a point
    for (int i = points.size()-1; i >= 0; i--)
    {
        if(points[i].getX() == 0 && points[i].getY() == 0 && points[i].getY() == 0)
        {
            vector<Point> group;
            group.push_back(points[i]);
            allGroups.push_back(group);
            points.erase(points.begin() + i);
            adjustGroups++;
            break;
        }
    }
    
    for (int i = 0; i < groupIncr*(numGroups-adjustGroups); i+=groupIncr)
    {
        vector<Point> group;
        group.push_back(points[i]);
        allGroups.push_back(group);
    }
    
    //Add last point as group
    vector<Point>groupLast;
    groupLast.push_back(points[points.size() - 1]);
    allGroups.push_back(groupLast);
    
    //Remove points
    for (int i = points.size()-1; i >= 0; i--)
    {
        for (int j = 0; j < allGroups.size(); j++)
        {
            if (points[i].equals(allGroups[j][0]))
            {
                points.erase(points.begin() + i);
                break;
            }
        }
    }
    //Add points to groups
    for (int i = 0; i < points.size(); i++)
    {
        int minDist = INT_MAX;
        int groupToAddTo = 0;
        for (int j = 0; j < allGroups.size(); j++)
        {
            int groupMaxDist = INT_MIN;
            for(int k = 0; k < allGroups[j].size(); k++)
            {
                int dist = allGroups[j][k].calculateDistance(points[i]);
                if(dist > groupMaxDist)
                    groupMaxDist = dist;
            }
            if (groupMaxDist < minDist)
            {
                minDist = groupMaxDist;
                groupToAddTo = j;
            }
        }
        allGroups[groupToAddTo].push_back(points[i]);
    }
    return allGroups;
}

int getMaxDistance(vector< vector<Point> > groups)
{
    int maxDist = INT_MIN;
    for (vector<Point> g : groups)
    {
        //Get max distance in group
        int dist = Point::calculateAllDistances(g);
        if (dist > maxDist)
            maxDist = dist;
    }
    return maxDist;
}
