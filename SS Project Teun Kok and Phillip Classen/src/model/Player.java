package src.model;

import java.util.ArrayList;

public interface Player {

    //Returns the name of this player.
    String getName();

    //Returns the Colour of this player.
    ArrayList<Colour> getColourList();

    //Returns the inventory of the player.
    ArrayList<Inventory> getInv();

    Board getBoard();

    Colour getColourByInt(int nr);

    String getMove(int diff);

    String getMoveStart();

}
