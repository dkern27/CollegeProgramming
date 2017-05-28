#pragma once
#include <SFML/Graphics.hpp>
#include <math.h>

#define PI 3.14159265

using namespace sf;
using namespace std;

class Ship : public Drawable
{
public:
    //Constructors
    Ship();
    Ship(int, int, int, float);

    void draw(RenderTarget&, RenderStates) const;
    void move();

    //Accessors
    void setSpeed(float);
    void setX(int);
    void setY(int);
    void setAngle(double);
    
    int getAngle() const;
    float getSpeed() const;
    int getX() const;
    int getY() const;

private:
    int x, y;
    double angle;
    float speed;

};

