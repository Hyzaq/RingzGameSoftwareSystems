package src.model;

import java.util.ArrayList;

/**
 * The type Human player.
 */
public class HumanPlayer implements Player {
    private String name;
    private Ring ring;
    private Board board;
    private ArrayList<Inventory> inv = new ArrayList<Inventory>();

    /**
     * Instantiates a new Human player.
     *
     * @param name  the name
     * @param inv   the inv
     * @param board the board
     */
    public HumanPlayer(String name, ArrayList<Inventory> inv, Board board) {
        this.inv = inv;
        this.name = name;
        this.board = board;
    }

    /**
     * Get name of  the player
     *
     * @return name the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get list of colors of the player
     *
     * @return list the list of colous in the inventory
     */
    public ArrayList<Colour> getColourList() {
        ArrayList<Colour> list = new ArrayList<Colour>();
        if (inv.size() >= 1) {
            list.add(inv.get(0).getInvColour());
        }
        if (inv.size() == 2) {
            list.add(inv.get(1).getInvColour());
        }
        return list;
    }

    /**
     * Returns the board of the players game
     * @return board the board
     */
    public Board getBoard() {
            return board;
    }

    /**
     * return the inventory array
     *
     * @return inv the inventory array
     */
    public ArrayList<Inventory> getInv() {
        return this.inv;
    }

    /**
     * gives Colour from number
     *
     * @param nr
     * @return colour the colour of an int
     */
    public Colour getColourByInt(int nr) {
        if(nr == 0) {
            return Colour.SPECIAL;
        } else if(nr == 1 ) {
            return getInv().get(0).getInvColour();
        } else if(nr == 2 ) {
            return getInv().get(1).getInvColour();
        }
        return null;
    }

    /**
     * getMove for the AI that uses the same interface
     * There is probably a nicer way to do it
     *
     * @param diff
     * @return null
     */
    @Override
    public String getMove(int diff) {
        return null;
    }

    /**
     * Same as getMove
     *
     * @return null
     */
    @Override
    public String getMoveStart() {
        return null;
    }
}
