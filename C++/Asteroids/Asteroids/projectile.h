#pragma once

#include <SFML/Graphics.hpp>
#include "Ship.h"

using namespace std;
using namespace sf;

class projectile : public Drawable
{
public:
    //Constructor
    projectile(int,int,double);

    void draw(RenderTarget&, RenderStates) const;

    void move();
    bool lifetime(); //Length of time bullets exist

    //Accessors
    void setX(int);
    void setY(int);
    void setAngle(double);
    
    int getX() const;
    int getY() const;

    static const int SPEED = 12; //Speed of bullets

private:
    int x, y;
    double angle;
    Clock clock; //For lifetime of bullets

};

