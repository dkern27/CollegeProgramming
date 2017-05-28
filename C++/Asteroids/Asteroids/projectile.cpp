#include "projectile.h"


projectile::projectile(int x, int y, double angle)
{
    setX(x);
    setY(y);
    setAngle(angle);
}

void projectile::draw(RenderTarget& window, RenderStates states) const
{
    CircleShape circle(2.f);
    circle.setPosition(x,y);
    window.draw(circle, states);
}

void projectile::move()
{
    //Infinite borders
    if (x < -5)
        setX(755 + SPEED*sin(angle*PI / 180));
    if (x > 755)
        setX(-5 + SPEED*sin(angle*PI / 180));
    if (y < -5)
        setY(755 - SPEED*cos(angle*PI / 180));
    if (y > 755)
        setY(-5 - SPEED*cos(angle*PI / 180));

    setX(x+SPEED*sin(angle*PI / 180));
    setY(y-SPEED*cos(angle*PI / 180));
}

void projectile::setX(int x)
{
    this->x = x;
}

void projectile::setY(int y)
{
    this->y = y;
}

void projectile::setAngle(double angle)
{
    this->angle = angle;
}

int projectile::getX() const
{
    return x;
}

int projectile::getY() const
{
    return y;
}


bool projectile::lifetime()
{
    if (clock.getElapsedTime().asMilliseconds() >= 500) //Checks length of time bullet exists
        return true;
    return false;
}