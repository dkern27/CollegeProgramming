#pragma once

#include <SFML/graphics.hpp>
#include "Ship.h"
#include "projectile.h"
#include <ctime> //for srand

using namespace sf;
using namespace std;

class Asteroid : public Drawable
{
public:
    //Constructors
    Asteroid();
    Asteroid(int step, int text, int x, int y);

    void draw(RenderTarget&, RenderStates) const;

    //Checks collisions
    bool collisionShip(Ship); 
    bool collisionBullet(projectile); 

    void move();
    void spawnLoc(int, int, int, int);

    //Accessors
    void setSpeed(float);
    void setX(int);
    void setY(int);
    void setAngle(double);
    void setTexture(Texture);
    void setStep(int);

    int getX() const;
    int getY() const;
    int getStep() const;
    int getAngle() const;

private:
    int x, y, angle, step; //x, y - location spawned, angle - direction of travel, step - size of asteroid
    int speed; //speed of object
    Texture texture; //graphic of object


};