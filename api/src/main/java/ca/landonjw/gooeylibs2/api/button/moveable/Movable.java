package ca.landonjw.gooeylibs2.api.button.moveable;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.moveable.MovableButtonAction;

public interface Movable
extends Button {
    void onPickup(MovableButtonAction action);

    void onDrop(MovableButtonAction action);
}

