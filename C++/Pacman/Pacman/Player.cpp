#include "Player.h"

// When constructing a player, give them a place to update their score (the Hud)
// Also assign some defaults for the position, speed, and direction
Player::Player(Hud* hud, int x, int y, int radius, int direction, int speed)
{
    // The player holds on to the HUD so that when
    // they collect points, they can update it.
    this->hud = hud;
    this->x = x;
    this->y = y;
    this->radius = radius;
    this->direction = direction;
    this->speed = speed;
}

// Draw the player onto the screen. For extra fanciness, we use sprites to draw
// the player
void Player::draw(RenderTarget& window, RenderStates states) const
{
    // Load up the texture to use for the sprite based off the direction
    Texture texture;
    switch (direction)
    {
    case Direction::NORTH:
        texture.loadFromFile("data/player0.png");
        break;
    case Direction::EAST:
        texture.loadFromFile("data/player1.png");
        break;
    case Direction::SOUTH:
        texture.loadFromFile("data/player2.png");
        break;
    case Direction::WEST:
        texture.loadFromFile("data/player3.png");
        break;
    }

    // Create a sprite that we can texture and use for drawing
    Sprite sprite;
    sprite.setTexture(texture);
    sprite.setPosition((float) x, (float) y);

    // Scale to fit inside a 2*radius by 2*radius square
    Vector2u tsz = texture.getSize();
    sprite.setScale((float) 2*radius/tsz.x, (float) 2*radius/tsz.y);

    // Draw the sprite
    window.draw(sprite, states);
}

// before move to a direction we should check whether we are not hitting a wall (i.e., non-isWalkable() tile)
void Player::move(TileMap &tileMap)
{
    // Based on the direction the player is headed, we add/subtract the speed
    // with the player's coordinates, moving them by a distance of speed in the
    // respective direction. North results in subtracting from y, East in adding
    // to x, and etc.
    //
    // Each time, though, we need to make sure the player can actually move
    // there; otherwise, they'd cheat, and just clip through walls! Each direction
    // checks that the 2 points on the outer edges of the player on that side
    // are in isWalkable() areas, like so:
    //
    // Normal Player:   Checking East (marked by '-')
    // sssss            ssss-
    // s   s            s   s
    // sssss            ssss-
    //
    // Remember that x,y is the top-left coordinate, and each side-length is 2*radius long
    switch (direction)
    {
    case Direction::NORTH:
        // Check top-left coord. with speed applied up (y-), and also the top-right
        // with speed applied up
        if (tileMap.getTile(x, y - speed).isWalkable()
            && tileMap.getTile(x + 2*radius , y - speed).isWalkable())
            y -= speed;
        break;
    case Direction::EAST:
        // Check top-right coord. with speed applied right (x+), and also the
        // bottom-right coord. with speed applied right.
        if (tileMap.getTile(x + 2*radius + speed, y).isWalkable()
            && tileMap.getTile(x + 2*radius + speed, y + 2*radius).isWalkable())
            x += speed;
        break;
    case Direction::SOUTH:
        // Check bottom-left coord. with speed applied down (y+), and also the
        // bottom-right coord. with speed applied down.
        if (tileMap.getTile(x, y + 2*radius + speed).isWalkable()
            && tileMap.getTile(x + 2*radius, y + 2*radius + speed).isWalkable())
            y += speed;
        break;
    case Direction::WEST:
        // Check top-left coord. with speed applied left (x-), and also the
        // bottom-left coord. with speed applied right.
        if (tileMap.getTile(x - speed, y).isWalkable()
            && tileMap.getTile(x- speed, y + 2*radius).isWalkable())
            x -= speed;
        break;
    }

    // Evaluate the new position to see if we can/should consume a point-giving dot
    // We add radius to x,y so that we check the tile that the player is in the middle of
    if (tileMap.getTile(x+radius, y+radius).getPoints() > 0) {
        // Add those points to the HUD's displayed score
        hud->addPoints(tileMap.getTile(x+radius, y+radius).getPoints());
        // Remove the points from the tile
        tileMap.setTilePoints(x+radius, y+radius, 0);
    }
}

// Have the player change directions
void Player::setDirection(int direction)
{
    this->direction = direction;
}

void Player::setX(int x)
{
    this-> x = x;
}

void Player::setY(int y)
{
    this->y = y;
}

void Player::setRadius(int radius)
{
    this->radius = radius;
}

void Player::setSpeed(int speed)
{
    this->speed = speed;
}

int Player::getX() const
{
    return x;
}

int Player::getY() const
{
    return y;
}

int Player::getRadius() const
{
    return radius;
}

int Player::getDirection() const
{
    return direction;
}

int Player::getSpeed() const
{
    return speed;
}