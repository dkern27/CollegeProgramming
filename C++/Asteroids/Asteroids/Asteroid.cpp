#include "Asteroid.h"

Asteroid::Asteroid() 
{
    this->step = 1;

    //Chooses border to spawn asteroid
    if ((rand() % 2) == 1)
    {
        if (rand() % 2 == 1)
            spawnLoc(0, 750, 0, 225);
        else
            spawnLoc(0, 750, 525, 750);
    }
    else
    {
        if (rand() % 2 == 1)
            spawnLoc(0, 225, 0, 750);
        else
            spawnLoc(525, 750, 0, 750);
    }

    this->angle = rand() % 360;
    this->speed = 3;

    //Randomizes texture to use
    Texture t1;
    if (rand() % 3 == 0)
        t1.loadFromFile("data/asteroid1.png");
    else if (rand() % 3 == 1)
        t1.loadFromFile("data/asteroid2.png");
    else
        t1.loadFromFile("data/asteroid3.png");
    this->texture = t1;
}

Asteroid::Asteroid(int step, int text, int x, int y)
{
    this->step = step;

    this->x = x;
    this->y = y;

    this->speed = 4;

    Texture t1;
    if (text == 0)
        t1.loadFromFile("data/asteroid1.png");
    else if (text == 1)
        t1.loadFromFile("data/asteroid2.png");
    else
        t1.loadFromFile("data/asteroid3.png");
    this->texture = t1;

}

void Asteroid::draw(RenderTarget& window, RenderStates states) const
{
    Sprite sprite;
    sprite.setTexture(texture);

    sprite.setPosition((float)x, (float)y);

    //Scales asteroid to make smaller;
    if (step == 2)
        sprite.setScale(0.5, 0.5);


    window.draw(sprite, states);


}
void Asteroid::move()
{
    //Infinite borders
    if (x < -86)
        setX(755 + speed*sin(angle*PI / 180));
    if (x > 836)
        setX(-25 + speed*sin(angle*PI / 180));
    if (y < -90)
        setY(755 - speed*cos(angle*PI / 180));
    if (y > 836)
        setY(-25 - speed*cos(angle*PI / 180));

    setX(x + speed*sin(angle*PI / 180));
    setY(y - speed*cos(angle*PI / 180));
}

void Asteroid::spawnLoc(int xLow, int xHigh, int yLow, int yHigh) //Sets x and y to spawn asteroid
{
    setX(rand() % (xHigh - xLow)+xLow);
    setY(rand() % (yHigh - yLow) + yLow);
}
void Asteroid::setX(int x)
{
    this->x = x;
}
int Asteroid::getX() const
{
    return x;
}

void Asteroid::setY(int y)
{
    this->y = y;
}

int Asteroid::getY() const
{
    return y;
}

int Asteroid::getAngle() const
{
    return angle;
}

void Asteroid::setStep(int step)
{
    this->step = step;
}
int Asteroid::getStep() const
{
    return step;
}

void Asteroid::setSpeed(float speed)
{
    this->speed = speed;
}

void Asteroid::setAngle(double angle)
{
    this->angle = angle;
}
bool Asteroid::collisionShip(Ship ship)
{
    //Check if ship and asteroid have collided
    int playerX, playerY, aiX, aiY;

    //Centers of ship and asteroid
    playerX = ship.getX() + 15 * cos(ship.getAngle()*PI/180) + 23 * sin(ship.getAngle()*PI/180);
    playerY = ship.getY() - 23 * cos(ship.getAngle()*PI/180) - 15 * sin(ship.getAngle()*PI/180);
    aiX = x + 45/step;
    aiY = y + 43/step;

    //Check for collision
    if (sqrt(pow(playerX - aiX, 2)) <  15 * cos(ship.getAngle()*PI/180) + 23 * sin(ship.getAngle()*PI/180) + 45/step &&
        sqrt(pow(playerY - aiY, 2)) <  -23 * cos(ship.getAngle()*PI/180) + -15 * sin(ship.getAngle()*PI/180) + 43/step)
        return true;
   return false;
}

bool Asteroid::collisionBullet(projectile proj)
{
    //Checks for collision of bullet and asteroid
    int playerX, playerY, aiX, aiY;

    //Centers of bullet and asteroid
    playerX = proj.getX() + 1;
    playerY = proj.getY() + 1;
    aiX = x + 45;
    aiY = y + 43;

    //Checks for collision
    if (sqrt(pow(playerX - aiX, 2)) <  45 &&
        sqrt(pow(playerY - aiY, 2)) <  43)
        return true;
    return false;
}