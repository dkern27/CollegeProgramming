#include "TileMap.h"

void TileMap::loadFromFile(string fileName)
{
    // Open up the input file
    ifstream infile(fileName.c_str());

    // Iterate through all the spots in our 2-D grid
    for (int i=0; i < ROWS; ++i) {
        for (int j=0; j < COLS; ++j) {
            // Read in whether the square is a wall, and directly set the
            // walkable variable in the respective tile
            bool isWall;
            infile >> isWall;
            // Since the data file has a 1 for spots that are walls, we need to
            // invert the variable read in for it to mean whether the spot is walkable
            // i.e. not a wall
            tile[i][j].setWalkable(!isWall);

            // Where does this tile belong? What are the coordinates for the
            // upper-left corner of the tile?
            tile[i][j].setPosition(j*Tile::TILE_WIDTH, i*Tile::TILE_HEIGHT);

            // Based on whether the tile is walkable, draw it as a different color,
            // and put points in every square that's walkable
            if (tile[i][j].isWalkable()) {
                tile[i][j].setColor(Color::Black);
                tile[i][j].setPoints(10);
            } else {
                tile[i][j].setColor(Color::Blue);
                tile[i][j].setPoints(0);
            }
        }
    }
}

Tile TileMap::getTile(int x, int y) const
{
    // Given a pixel location x,y return the tile that exists at that spot
    return tile[y/Tile::TILE_WIDTH][x/Tile::TILE_HEIGHT];
}

void TileMap::draw(RenderTarget& window, RenderStates states) const
{
    for (int row = 0, anInt; row < ROWS; row++)
        for (int col = 0; col < COLS; col++)
            window.draw(tile[row][col], states);
}

// Given a pixel location (x,y), set the tile that it corresponds to's points
void TileMap::setTilePoints(int x, int y, int points)
{
    tile[y/Tile::TILE_WIDTH][x/Tile::TILE_HEIGHT].setPoints(points);
}

// Check all the tiles to see if they have points. i.e. did the player win?
bool TileMap::noMorePoints()
{
    // TODO: Scan through all the tiles, and check if there are any tiles with
    // points (>0). If there are any tiles that do have points, this function
    // should return false; otherwise, it signifies that the player has consumed
    // all the points, and has now won
    for (int i = 0; i < ROWS; i++)
    {
        for (int j = 0; j < COLS; j++)
        {
            if (tile[i][j].getPoints() != 0)
                return false;
        }
    }
    return true;
}
