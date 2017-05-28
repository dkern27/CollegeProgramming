#include "Ship.h"



Ship::Ship(int x,int y,int angle, float speed)
{
    this->x = x;
    this->y = y;
    this->angle = angle;
    this->speed = speed;
}

void Ship::draw(RenderTarget& window, RenderStates states) const
{
    Sprite sprite;
    Texture t;
   
    t.loadFromFile("data/spaceship.png");
    sprite.setTexture(t);
    
    sprite.setOrigin(15, 23); //Sets axis of rotation
    sprite.setPosition((float)x, (float)y);
    
    sprite.setRotation(angle); //Rotates sprite

    window.draw(sprite, states);


}

void Ship::move()
{
    //Infinite borders
    if (x < -42)
        setX(755 + speed*sin(angle*PI / 180));
    if (x > 755 )
        setX(-5 + speed*sin(angle*PI / 180));
    if (y < -42)
        setY(755 - speed*cos(angle*PI / 180));
    if (y > 755)
        setY(-5 - speed*cos(angle*PI / 180));

    setX(x + speed*sin(angle*PI/180));
    setY(y - speed*cos(angle*PI/180));
}
void Ship::setX(int x)
{
    this->x = x;
}

void Ship::setY(int y)
{
    this->y = y;
}
int Ship::getY() const
{
    return y;
}
int Ship::getX() const
{
    return x;
}

void Ship::setSpeed(float speed)
{
    this->speed = speed;
}

void Ship::setAngle(double angle)
{
    this->angle = angle;
}
float Ship::getSpeed() const
{
    return speed;
}

int Ship::getAngle() const
{
    return angle;
}

