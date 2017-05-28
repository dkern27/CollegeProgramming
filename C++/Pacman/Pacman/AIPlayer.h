#pragma once

#include <SFML/Graphics.hpp>
#include <cmath>

#include "TileMap.h"
#include "Player.h"

using namespace sf;

class AIPlayer : public Drawable
{
private:

    int x, y;   // Top-left corner of the AIPlayer. Since the AIPlayers are
                // not circular, we choose to draw them as rectangles, and
                // describe their position using the top-left corner of that
                // rectangle
    int width,height;   // much space does this AI take up?

    int direction;  // Which direction is the player facing/moving?
    int speed;  // Number of pixels moved each frame


    // Helper functions to simplify the move logic
    bool canWalkNorth(const TileMap &tileMap);
    bool canWalkEast(const TileMap &tileMap);
    bool canWalkSouth(const TileMap &tileMap);
    bool canWalkWest(const TileMap &tileMap);

public:
    AIPlayer();
    bool playerCollide(const Player player) const;
    void draw(RenderTarget& window, RenderStates states) const;
    void move(const TileMap &tileMap, const Player player);
};
