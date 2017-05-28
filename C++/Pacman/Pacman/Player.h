#pragma once

#include <SFML/Graphics.hpp>

#include "Direction.h"
#include "Hud.h"
#include "TileMap.h"

using namespace sf;
using namespace std;

// The player class holds a representation of the player, and maintains all their
// attributes: where they are, how big they are, the direction they're facing, etc.
class Player : public Drawable
{
public:
    Hud* hud;   // A reference to the HUD, so that the player can collect points

    //const int thing[10] = {1,2,3,4,5};


    // Make a player that has access to the Hud (so they can update it when
    // collecting points. Ignore the Hud* part for now. It's a pointer to a
    // Hud object, and operates much like a reference.
    Player(Hud* hud, int x, int y, int radius, int direction, int speed);

    // Draw the player onto the screen
    void draw(RenderTarget& rt, RenderStates rs) const;

    // Instruct the player to update their location, given their speed and direction
    void move(TileMap &tileMap);

    // Getters and Setters
    void setDirection(int direction);
    void setX(int x);
    void setY(int y);
    void setRadius(int radius);
    void setSpeed(int speed);
    int getX() const;
    int getY() const;
    int getRadius() const;
    int getDirection() const;
    int getSpeed() const;

private:
    int x, y;   // Coordinates of the top-left corner that the player takes up
    int radius; // Pacman is circular, so we represent the size as a radius

    int direction;  // Which direction is the player facing/moving?
    int speed;  // Number of pixels moved each frame
};

