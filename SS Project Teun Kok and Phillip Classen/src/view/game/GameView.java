package src.view.game;

import src.model.Colour;
import src.model.Inventory;

import java.util.ArrayList;
import java.util.Observer;

public interface GameView extends Observer {

    void printMessage(String message);

    int getXValue();

    int getYValue();

    int getZValue();

    Colour getColour();

    void showBoard();

    void setSecondaryColour(Colour colour);

    void setPrimaryColour(Colour colour);

    boolean getValuesSet();

    void colourRing(String ring, Colour colour);

    void setHintEnabled(boolean bool);

}