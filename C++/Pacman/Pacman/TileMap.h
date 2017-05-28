#pragma once

#include <fstream>
#include <SFML/Graphics.hpp>

#include "Tile.h"

using namespace sf;
using namespace std;

// A container for all the Tiles together
class TileMap : public Drawable
{
public:
    static const int ROWS = 10;
    static const int COLS = 10;

    void loadFromFile(string); // reads the tile map from a data file
    Tile getTile(int, int) const; // returns the tile reference that corresponds to a coordinate
    void draw(RenderTarget&, RenderStates) const;

    // Set the points in the tile at the x,y pixel location
    void setTilePoints(int x, int y, int points);

    // Scan the entire set of tiles to see if they've all been cleared
    bool noMorePoints();

private:
    Tile tile[ROWS][COLS];
};

