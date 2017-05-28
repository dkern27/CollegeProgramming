#include "AIPlayer.h"

AIPlayer::AIPlayer()
{
    // To avoid unnecessary collisions, we want the AI to ALMOST take up
    // an entire square. If they're too big, they can't move effectively
    // The width/height leaves 2 pixels on each side
    width =  Tile::TILE_HEIGHT-4;
    height = Tile::TILE_WIDTH-4;

    // Start the AI inside the top-left isWalkable() square, and inset by a pixel
    // we inset by 1x1 pixel so they aren't accidentally inside the wall at spawn
    x = width+5;
    y = height+5;

    // The AI is a little slower than the player (1 vs 2)
    speed = 1;

    // Start the AI moving off in some direction; doesn't actually matter which,
    // since each move action will decide on the best direction to move
    direction = Direction::NORTH;
}

// Draw the AIPlayer inside the window
void AIPlayer::draw(RenderTarget& window, RenderStates states) const
{
    // TODO: Load up the texture for the stalker, and draw the AIPlayer.
    // Hints:
    // The Texture class has a function called loadFromFile
    // The Sprite class has a function setTexture
    // You can set the position of a sprite
    // The RenderTarget::draw function takes a Drawable and a RenderStates as arguments
    // Sprite inherits from Drawable
    Texture texture;
    switch (direction)
    {
    case Direction::NORTH:
        texture.loadFromFile("data/stalker0.png");
        break;
    case Direction::EAST:
        texture.loadFromFile("data/stalker1.png");
        break;
    case Direction::SOUTH:
        texture.loadFromFile("data/stalker2.png");
        break;
    case Direction::WEST:
        texture.loadFromFile("data/stalker3.png");
        break;
    }
    Sprite sprite;
    sprite.setTexture(texture);
    sprite.setPosition((float)x, (float)y);

    Vector2u tsz = texture.getSize();
    sprite.setScale((float)width / tsz.x, (float)height / tsz.y);

    window.draw(sprite, states);
}

bool AIPlayer::canWalkNorth(const TileMap &tileMap)
{
    return tileMap.getTile(x, y - speed).isWalkable()
            && tileMap.getTile(x + width, y - speed).isWalkable();
}

bool AIPlayer::canWalkEast(const TileMap &tileMap)
{
    return tileMap.getTile(x + width+ speed, y).isWalkable()
            && tileMap.getTile(x + width + speed, y + height).isWalkable();
}

bool AIPlayer::canWalkSouth(const TileMap &tileMap)
{
    return tileMap.getTile(x, y + height + speed).isWalkable()
            && tileMap.getTile(x + width, y + height + speed).isWalkable();
}

bool AIPlayer::canWalkWest(const TileMap &tileMap)
{
    return tileMap.getTile(x - speed, y).isWalkable()
            && tileMap.getTile(x - speed, y + height).isWalkable();

}

// before move to a direction we should check whether we are not hitting a wall (i.e., non-isWalkable() tile)
void AIPlayer::move(const TileMap &tileMap, const Player player)
{
    // If the player is to some direction of the AI, then face that direction.
    if (   player.getX() - x < 0
        && player.getX() - x + speed < 0
        && canWalkWest(tileMap))
    {
        direction = Direction::WEST;
    }
    else if (player.getY() - y < 0
        && player.getY() - y + speed < 0
        && canWalkNorth(tileMap))
    {
        direction = Direction::NORTH;
    }
    else if (player.getX() - x > 0
        && player.getX() - x - speed > 0
        && canWalkSouth(tileMap))
    {
        direction = Direction::EAST;
    }
    else if (player.getY() - y > 0
        && player.getY() - y - speed > 0
        && canWalkEast(tileMap))
    {
        direction = Direction::SOUTH;
    }

    // Apply a movement on the AI depending on the direction they're headed
    switch (direction)
    {
    case Direction::NORTH:
        if (canWalkNorth(tileMap))
            y -= speed;
        break;
    case Direction::EAST:
        if (canWalkEast(tileMap))
            x += speed;
        break;
    case Direction::SOUTH:
        if (canWalkSouth(tileMap))
            y += speed;
        break;
    case Direction::WEST:
        if (canWalkWest(tileMap))
            x -= speed;
        break;
    }
}

bool AIPlayer::playerCollide(const Player player) const
{
    // TODO: Check if the player and the AIPlayer have collided
    int pXcenter, pYcenter, aiX, aiY;
    pXcenter=player.getX()+player.getRadius();
    pYcenter = player.getY()+player.getRadius();

    aiX = x + width / 2;
    aiY = y + height / 2;

    if (sqrt(pow(pXcenter - aiX, 2)) < player.getRadius() + width / 2 && sqrt(pow(pYcenter - aiY, 2)) < player.getRadius() + height / 2)
        return true;

    return false;
}
