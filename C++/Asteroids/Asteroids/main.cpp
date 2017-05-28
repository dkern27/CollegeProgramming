/*
Drew Lange - dlange - Section C
Dylan Kern - dkern - Section D

ASTEROIDS
Plays a single level of asteroids
*/

#include <SFML/Graphics.hpp>
#include <iostream>
#include <vector>

#include "Asteroid.h"
#include "Ship.h"
#include "projectile.h"


using namespace std;
using namespace sf;

const int WINDOW_WIDTH(750), WINDOW_HEIGHT(750); //Window size

const double ACCELERATION = 0.05; //ship acceleration
const double ROTATION_SPEED = 5; //ship rotation speed
const int MAX_BULLETS = 4; //Max bullets at one time
const int START_ASTEROIDS = 4; //Number of starting asteroids

//Function prototypes
void shoot(Ship ship, vector<projectile>&);
bool checkCollisionShip(Ship, vector<Asteroid>& asteroid);
void createAsteroid(vector<Asteroid>& asteroid, int step, int location);
void disappear(vector<Asteroid>&, vector <projectile>&);
void move(vector<Asteroid>&, vector <projectile>&);



int main()
{
    srand(time(NULL)); //seed for random

    //Vectors to store projectiles and asteroids
    vector<projectile> projectiles;
    vector<Asteroid> asteroid;

    Clock clock;

    //Creates window
    RenderWindow window(VideoMode(WINDOW_WIDTH, WINDOW_HEIGHT), "Asteroids", Style::Titlebar | Style::Close);

    //Loads font for text
    Font font;
    font.loadFromFile("data/LiberationMono-Regular.ttf");

    //Initializes ship object
    Ship ship(360, 352, 0, 0.f);

    //Initializes asteroids
    for (int i = 0; i < START_ASTEROIDS; i++)
        createAsteroid(asteroid, 1, i);

    Event event;
    window.setFramerateLimit(60);

    //For text
    Text t;
    t.setFont(font);
    t.setPosition(225, 325);
    t.setColor(Color::Green);
    t.setScale(2.0, 2.0);

    while (window.isOpen())
    {
        if (checkCollisionShip(ship, asteroid))
        {


            // Text to display game over - collision with asteroid
            t.setString("GAME OVER");
            window.clear();
            window.draw(t);

        }

        else if (asteroid.size() == 0)
        {
            //Text to display winning message - destroy all asteroids
            t.setString("You Win!");
            window.clear();
            window.draw(t);

        }

        else
        {
            //Ship movement and control
            if (Keyboard::isKeyPressed(Keyboard::Up)) //Move forward
            {
                if (ship.getSpeed()<5) //Accelerate ship
                    ship.setSpeed(ship.getSpeed() + ACCELERATION);
                ship.move();
            }
            else //decelerate
            {
                if (ship.getSpeed() > 0)
                {
                    ship.setSpeed(ship.getSpeed() - ACCELERATION);
                    ship.move();
                }
            }

            if (Keyboard::isKeyPressed(Keyboard::Right)) //Rotate clockwise
            {
                ship.setAngle(ship.getAngle() + ROTATION_SPEED);
            }
            if (Keyboard::isKeyPressed(Keyboard::Left)) //Rotate counter-clockwise
            {
                ship.setAngle(ship.getAngle() - ROTATION_SPEED);
            }
            if (Keyboard::isKeyPressed(Keyboard::Space)) //Fire projectile
            {
                //Checks max bullets and time limit between shots
                if (projectiles.size() < MAX_BULLETS && clock.getElapsedTime().asMilliseconds() >= 200)
                {
                    shoot(ship, projectiles);
                    clock.restart();
                }
            }

            disappear(asteroid, projectiles); //Deletes asteroids and/or projectiles

            move(asteroid, projectiles); //Changes x,y of asteroids and projectiles

            window.clear();

            //Draw onto window
            window.draw(ship);
            for (int i = 0; i < asteroid.size(); i++)
            {
                window.draw(asteroid[i]);
            }
            for (int i = 0; i < projectiles.size(); i++)
            {
                window.draw(projectiles[i]);
            }
        }
        window.display();

        while (window.pollEvent(event))
        {
            if (event.type == Event::Closed)
                window.close();
        }
    }
    return 0;
}

bool checkCollisionShip(Ship ship, vector<Asteroid>& asteroid)
{
    //Loops through each asteroid checking if it hits the ship
    for (int i = 0; i < asteroid.size(); i++)
    {
        if (asteroid[i].collisionShip(ship))
            return true;
    }
    return false;
}

void shoot(Ship ship, vector<projectile>& projectiles)
{
    //Creates projectile object and puts onto end of vector
    projectile p(ship.getX(), ship.getY(), ship.getAngle());
    projectiles.push_back(p);
}

void createAsteroid(vector<Asteroid>& asteroid, int step, int location)
{
    //Creates asteroid object and puts onto end of vector
    if (step == 1) //Large asteroid
    {
        Asteroid a;
        asteroid.push_back(a);
    }
    if (step == 2) //Small asteroid
    {
        //Get location of big asteroid
        int tempX = asteroid[location].getX();
        int tempY = asteroid[location].getY();

        int randNum = rand();

        Asteroid a(step, randNum % 3, tempX, tempY);
        asteroid.push_back(a);
    }

}

void disappear(vector<Asteroid>& asteroid, vector<projectile>& projectiles)
{
    //Checks collision of each bullet with each asteroid
    for (int i = 0; i < asteroid.size(); i++)
    {
        for (int j = 0; j < projectiles.size(); j++)
        {
            if (asteroid[i].collisionBullet(projectiles[j]))
            {
                createAsteroid(asteroid, asteroid[i].getStep() + 1, i);
                asteroid[asteroid.size() - 1].setAngle(asteroid[i].getAngle() + 15);
                createAsteroid(asteroid, asteroid[i].getStep() + 1, i);
                asteroid[asteroid.size() - 1].setAngle(asteroid[i].getAngle() - 15);

                asteroid.erase(asteroid.begin() + i); //Deletes asteroid from vector
                projectiles.erase(projectiles.begin() + j); //Deletes projectile from vector
                break;
            }

            else if (projectiles[j].lifetime()) //Checks if lifetime of bullet is over
            {
                projectiles.erase(projectiles.begin());
            }
        }
    }
}



void move(vector<Asteroid>& asteroid, vector <projectile>& projectiles)
{
    //Loops to move asteroids
    for (int i = 0; i < projectiles.size(); i++)
    {
        projectiles[i].move();
    }

    //Loops to move bullets
    for (int i = 0; i < asteroid.size(); i++)
    {
        asteroid[i].move();
    }
}