#pragma once

#include <SFML/Graphics.hpp>
#include <iostream>

using namespace sf;
using namespace std;

// A Tile is a single square inside the map. Each Tile could be a wall, or could
// be a walkway, as described by walkable. Each Tile has some optional points that could
// be held. If there are points, a circle is drawn that can be collected.
class Tile : public Drawable
{
private:
    int x, y; // upper left coordinate
    Color color;    // The color that will be used to draw the Tile
    int width, height;  // The size of the square
    bool walkable; // whether the tile is 'walkable' or not
    int points; // How many points the Tile has. If 0, no circle will be drawn

public:
    static const int TILE_WIDTH = 50;
    static const int TILE_HEIGHT = 50;

    void setPosition(int x, int y);
    void setColor(Color color);
    void setSize(int w, int h);
    void setPoints(int p);
    void setWalkable(bool w);
    bool isWalkable() const;
    int  getPoints() const;
    void draw(RenderTarget&, RenderStates) const;
};

