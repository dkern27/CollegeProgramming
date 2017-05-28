
#include "Hud.h"

Hud::Hud(Font font)
{
    // TODO: Save the font for later use, and initialize variables
    this->font = font;
    points = 0;
}

void Hud::addPoints(int p)
{
    points += p;
}

void Hud::draw(RenderTarget& rt, RenderStates rs) const
{
    // TODO: Draw the HUD
    Text t;
    t.setFont(font);
    t.setString("Score:"+to_string(points));
    t.setPosition(0, 0);
    rt.draw(t, rs);
}


int Hud::getPoints() const
{
    return points;
}

