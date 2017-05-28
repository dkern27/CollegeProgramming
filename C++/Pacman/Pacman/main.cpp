#include <iostream>
#include <cstdlib>
#include <fstream>
#include <SFML/Graphics.hpp>

#include "AIPlayer.h"
#include "Direction.h"
#include "Hud.h"
#include "Player.h"
#include "Tile.h"
#include "TileMap.h"

using namespace std;
using namespace sf;

// declare your constants below here
static const string DATA_FILE = "data/tilemap.txt";

/********************************************************************************
* main() function                                                              *
********************************************************************************/
int main()
{
    // creates the window object    
    RenderWindow window(sf::VideoMode(TileMap::COLS * Tile::TILE_WIDTH, TileMap::ROWS * Tile::TILE_HEIGHT), "HW11: Pacman", Style::Titlebar | Style::Close);

    // The TileMap contains all the Tiles that are to be drawn
    TileMap tileMap;
    tileMap.loadFromFile(DATA_FILE);

    // This Texture contains every single texture to be used. Sprites are supposed
    // to grab rectangles from this texture, and use/rotate it as needed
    /*
    Texture spritemap;
    spritemap.loadFromFile("data/ms-pacman-spritesheet.png");
    */

    // A font that will be distributed to any that need it
    Font font;
    font.loadFromFile("data/LiberationMono-Regular.ttf");

    // The HUD draws the player's score in the top-left corner of the screen
    Hud hud(font);


    // Initialize a player, who will be able to update the HUD
    //
    // TODO: Move all the initializations into a Player constructor and make
    // the data members private.
    // Keep the Hud argument exactly as it is
    Player player(&hud,250,255,20,Direction::EAST,1);


    // Initialize the AI that will chase the player
    AIPlayer ai;

    // constantly draws the window until it is closed
    while (window.isOpen())
    {
        // Do a re-draw. First, clear the old objects, then redraw them freshly
        // Here, we draw all the main components: tiles, player, ai, and the HUD
        window.clear();
        window.draw(tileMap);
        window.draw(player);
        window.draw(ai);
        window.draw(hud);

        // Check for an end-condition: Did the player get caught? Did the player
        // consume all the points? When an end condition, the ending message is
        // shown (Text on top of a Rectangle), and the player and AI updates are
        // no longer required; hence, player/ai move are in the else condition.
        if (ai.playerCollide(player))
        {
            // Gray rectangle to put text on top of
            RectangleShape rect(Vector2f(300,100));
            rect.setFillColor(Color(100, 100, 100));
            rect.setPosition(100,200);
            window.draw(rect);

            // Text to display the caught-message
            Text t;
            t.setFont(font);
            t.setString("GAME OVER");
            t.setPosition(100, 200);
            t.setColor(Color::Green);
            t.setScale(2.0, 2.0);
            window.draw(t);
        }
        else if (tileMap.noMorePoints())
        {
            // Gray rectangle to put text on top of
            RectangleShape rect(Vector2f(300,100));
            rect.setFillColor(Color(100, 100, 100));
            rect.setPosition(100,200);
            window.draw(rect);

            // Text to display the winning message
            Text t;
            t.setFont(font);
            t.setString("YOU WIN!");
            t.setPosition(100, 200);
            t.setColor(Color::Green);
            t.setScale(2.0, 2.0);
            window.draw(t);
        }
        else
        {
            // moves the player and AI
            player.move(tileMap);
            ai.move(tileMap, player);
        }

        // Finally, update the screen with the freshly drawn objects
        window.display();

        // Checks for keyboard presses, and decides the player's direction
        Event event;
        while (window.pollEvent(event))
        {
            if (event.type == Event::Closed)
                window.close();
            else if (event.type == Event::KeyPressed)
            {
                switch (event.key.code)
                {
                case Keyboard::Up:
                    player.setDirection(Direction::NORTH);
                    break;
                case Keyboard::Down:
                    player.setDirection(Direction::SOUTH);
                    break;
                case Keyboard::Left:
                    player.setDirection(Direction::WEST);
                    break;
                case Keyboard::Right:
                    player.setDirection(Direction::EAST);
                    break;
                default:
                    break;
                }
            }
        }
    }

    return EXIT_SUCCESS;
}
