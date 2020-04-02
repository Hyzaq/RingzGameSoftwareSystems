package src.view.game;

import src.model.Board;
import src.model.Colour;
import src.model.Inventory;
import src.model.Ring;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Scanner;

public class TUIViewGame implements GameView {

    private Scanner in = new Scanner(System.in);
    private Colour primaryColour;
    private Colour secondaryColour;

    @Override
    public void update(Observable o, Object arg) {

    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private static final String LINE = "_______________________________________________________________________________" +
            "__________________________________________________________________________________________________________" +
            "_______________________";



    public void printBoard(Board b) {
        String string = "";
        String s = "";
        for (int y = 1; y < 6; y++) {
            for (int x = 1; x < 6; x++) {
                Ring[] rings = b.getBoardArrayXY(x, y);
                s = "";
                for (int z = 0; z < 5; z++) {
                    s = s + "[" + rings[z].toStringRing() + "], ";
                }
                string = string + s + " | ";
            }
            string = string + "\n" + LINE + "\n";
        }
        printMessage(string);
    }

    public void printInv(ArrayList<Inventory> invL) {
        for (Inventory inv : invL) {
            Ring[] rings = inv.getArray();
            String s = "";
            for (int i = 0; i < inv.getArray().length; i++) {
                if (rings[i].getColour().equals(Colour.EMPTY)) {
                    continue;
                } else {
                    try {
                        s = s + "[" + rings[i].toStringRing() + "], ";
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(s);
            System.out.println("------");
        }
    }

    public boolean readBoolean(String prompt) {
        printMessage(prompt);
        String answer = in.nextLine();
        if (answer.equals("y")) {
            return true;
        } else if (answer.equals("n")) {
            return false;
        } else {
            return false;
        }
    }

    @Override
    public int getXValue() {
        int x = 0;
        printMessage("Type in the X value: ");
        try {
            x = Integer.parseInt(in.nextLine());
        }catch (NumberFormatException e) {
            printMessage("You did not enter a valid integer, please enter an integer between 1 and 5.");
            getXValue();
        }
        if (x < 1 || x > 5) {
            printMessage("You did not enter a valid integer, please enter an integer between 1 and 5.");
            getXValue();
        }
        return x;
    }

    @Override
    public int getYValue() {
        printMessage("Type in the Y value: ");
        int y = 0;
        try {
            y = Integer.parseInt(in.nextLine());
        }catch (NumberFormatException e) {
            printMessage("You did not enter a valid integer, please enter an integer between 1 and 5.");
            getYValue();
        }
        if (y < 1 || y > 5) {
            printMessage("You did not enter a valid integer, please enter an integer between 1 and 5.");
            getYValue();
        }
        return y;
    }

    @Override
    public int getZValue() {

        printMessage("Type in the Size of the Ring [1-5]: ");
        int z = 0;
        try {
            z = Integer.parseInt(in.nextLine());
        }catch (NumberFormatException e) {
            printMessage("You did not enter a valid integer, please enter an integer between 0 and 4.");
            getXValue();
        }
        if (z < 0 || z > 4) {
            printMessage("You did not enter a valid integer, please enter an integer between 0 and 4.");
            getZValue();
        }
            return z;
    }

    public Colour getColour() {

        printMessage("Type in the Colour integer (1: Primary, 2: Secondary): ");
        int colour = 0;
        try {
            colour = Integer.parseInt(in.nextLine());

        }catch (InputMismatchException | NumberFormatException e) {
            printMessage("You did not enter a valid colour, please enter 1 or 2");
            getColour();
        }

        if (colour < 1 || colour > 2) {
            printMessage("You did not enter a valid colour, please enter 1 or 2");
            getColour();
        }
        if (colour == 1) {
            return primaryColour;
        }
        if (colour == 2) {
            return secondaryColour;
        }
        return null;
    }

    @Override
    public void showBoard() {

    }

    @Override
    public void setSecondaryColour(Colour colour) {
        if (colour.equals(null)) {
            secondaryColour = Colour.EMPTY;
        }
        secondaryColour = colour;
    }

    @Override
    public void setPrimaryColour(Colour colour) {
        if (colour.equals(null)) {
            primaryColour = Colour.EMPTY;
        }
        primaryColour = colour;
    }

    @Override
    public boolean getValuesSet() {
        return false;
    }

    @Override
    public void colourRing(String ring, Colour colour) {

    }

    @Override
    public void setHintEnabled(boolean bool) {

    }
}
