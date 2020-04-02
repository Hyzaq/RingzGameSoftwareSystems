package src.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * The type Ai player.
 */
public class AiPlayer implements Player {

    private String name;
    private Ring ring;
    private Board board;
    private ArrayList<Inventory> inv;
    private Game game;
    private int order = 1;

    /**
     * Instantiates a new Ai player.
     *
     * @param name  the name
     * @param inv   the inv
     * @param board the board
     * @param game  the game
     */

    AiPlayer(String name, ArrayList<Inventory> inv, Board board, Game game) {
        this.inv = inv;
        this.name = name;
        this.board = board;
        this.game = game;
    }

    /**
     * Returns the name of the AI
     *
     * @return name the name of the AI
     */

    /*@
    ensures \result != null;
    pure;
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of all colours of the AI
     *
     * @return list the colour list
     */

    /*@
    ensures \result != null;
    pure;
     */
    @Override
    public ArrayList<Colour> getColourList() {
        ArrayList<Colour> list = new ArrayList<>();
        if (inv.size() >= 1) {
            list.add(inv.get(0).getInvColour());
        }
        if (inv.size() == 2) {
            list.add(inv.get(1).getInvColour());
        }
        return list;
    }

    /**
     * Returns the inventory array
     *
     * @return inv the inventory
     */

    /*@
    ensures \result != null;
    pure;
     */
    public ArrayList<Inventory> getInv() {
        return inv;
    }

    /*@
    ensures \result != null;
    pure;
     */

    public Board getBoard() {
        synchronized (board) {
            return board;
        }
    }

    /**
     * gives Colour from number
     *
     * @param nr the number
     * @return colour the colour of an int
     */

    /*@
    require nr >= 0 && nr < 3;
    ensures \result != null;
    pure;
     */
    @Override
    public Colour getColourByInt(int nr) {
        if (nr == 0) {
            return Colour.SPECIAL;
        } else if (nr == 1) {
            return getInv().get(0).getInvColour();
        } else if (nr == 2) {
            return getInv().get(1).getInvColour();
        }
        return null;
    }

    /**
     * Here the AI decides what move it should make
     * The Brain
     *
     * @param difficulty the difficulty integer
     * @return move the move the AI determined
     */

    /*@
    require difficulty >= 0;
    ensures \result != null;
    pure;
     */
    @Override
    public String getMove(int difficulty) {
        String move = "";
        for (int x = 1; x < 6; x++) {
            for (int y = 1; y < 6; y++) {
                int re = 0;
                int bl = 0;
                int gr = 0;
                int ye = 0;
                int myC1 = 0;
                int myC2 = 0;

                for (int z = 0; z < 5; z++) {

                    if (getColourList().get(0).equals(this.board.getBoardRingXYZ(x, y, z).getColour())) {
                        myC1 += 1;
                        continue;
                    } else if (getColourList().size() > 1) {
                        if (getColourList().get(1).equals(this.board.getBoardRingXYZ(x, y, z).getColour())) {
                            myC2 += 1;
                            continue;
                        }
                    }
                    switch (this.board.getBoardRingXYZ(x, y, z).getColour()) {
                        case RED:
                            re += 1;
                            break;

                        case BLUE:
                            bl += 1;
                            break;

                        case GREEN:
                            gr += 1;
                            break;

                        case YELLOW:
                            ye += 1;
                            break;

                        default:
                            break;
                    }
                }
//
//                if (difficulty > 3) {
//                    //!!Optional (for agressive AI) search for majority (tie on field, then place ring)!!
//                    move = "";
//                }

                //Focus on fillable arrays
                if (difficulty > 1) {

                    //See if the array is fillable
                    if ((re + bl + gr + ye + myC1 + myC2) == 3) {
                        for (int r = 0; r < 5; r++) {

                            //Check if I can place a ring there
                            Ring ring = new Ring(getColourList().get(0), r);
                            if (board.validateMove(x, y, r, getColourList().get(0)) && game.checkInv(ring)) {

                                //When I have 0 rings placed, if no enemy has 2 rings I block it
                                if (myC1 == 0) {
                                    if ((bl < 2) && (re < 2) && (gr < 2) && (ye < 2) && (myC2 == 0)) {
                                        move = x + " " + y + " " + r + " " + 1;
                                        return move;
                                    }
                                }

                                //When I have a ring placed
                                if (myC1 == 1) {

                                    //If someone has 2 placed rings I block (unless its me)
                                    if ((((bl > 1) || (re > 1) || (gr > 1) || (ye > 1)) && !(myC2 == 2))) {
                                        move = x + " " + y + " " + r + " " + 1;
                                        return move;

                                        //Else I win the spot if my difficulty is high enough (DIFFICULTY 4)
                                    } else if (difficulty > 3) {
                                        move = x + " " + y + " " + r + " " + 1;
                                        return move;
                                    }
                                }

                                //When I have 2 rings placed I win it (unless its me
                                if (myC1 == 2 && !(myC2 == 1)) {
                                    move = x + " " + y + " " + r + " " + 1;
                                    return move;
                                }

                                //If I have three rings I do nothing
                            }

                            //When playing with 2 players
                            if (getColourList().size() > 1) {
                                ring = new Ring(getColourList().get(1), r);
                                if (board.validateMove(x, y, r, getColourList().get(0)) && game.checkInv(ring)) {

                                    //When I have 0 rings placed, if no enemy has 2 rings I block it
                                    if (myC2 == 0) {
                                        if ((bl < 2) && (re < 2) && (gr < 2) && (ye < 2) && (myC1 == 2)) {
                                            move = x + " " + y + " " + r + " " + 2;
                                            return move;
                                        }
                                    }

                                    //When I have a ring placed
                                    if (myC2 == 1) {

                                        //If someone has 2 placed rings I block (unless its me)
                                        if ((((bl > 1) || (re > 1) || (gr > 1) || (ye > 1)) && !(myC1 == 2))) {
                                            move = x + " " + y + " " + r + " " + 2;
                                            return move;

                                            //Else I win the spot if my difficulty is high enough
                                        } else if (difficulty > 4) {
                                            move = x + " " + y + " " + r + " " + 2;
                                            return move;
                                        }
                                    }

                                    //When I have 2 rings placed I win it unless the second colour is me
                                    if (myC2 == 2 && !(myC1 == 1)) {
                                        move = x + " " + y + " " + r + " " + 2;
                                        return move;
                                    }

                                    //If I have three rings I do nothing
                                }
                            }
                        }
                    }

                    //In case a position has 2 placed rings
                    if ((re + bl + gr + ye + myC1 + myC2) == 2) {
                        for (int k = 0; k < 5; k++) {
                            Ring ring = new Ring(getColourList().get(0), k);
                            if (board.validateMove(x, y, k, getColourList().get(0)) && game.checkInv(ring)) {
                                //if I have no rings placed
                                if (myC1 == 0) {
                                    //When no one has more than 1 ring place a ring (unless im already on the position)
                                    if (!((bl > 1) || (re > 1) || (gr > 1) || (ye > 1) && myC2 == 2)) {
                                        move = x + " " + y + " " + k + " " + 1;
                                        return move;
                                    }
                                }
                                if (myC1 == 2) {
                                    move = x + " " + y + " " + k + " " + 1;
                                    return move;
                                }
                            }
                        }
                        if (getColourList().size() > 1) {
                            for (int t = 0; t < 5; t++) {
                                Ring ring = new Ring(getColourList().get(1), t);
                                if (board.validateMove(x, y, t, getColourList().get(1)) && game.checkInv(ring)) {
                                    //if I have no rings placed
                                    if (myC1 == 0) {
                                        //When no one has more than 1 ring place a ring (unless im already on the position)
                                        if (!(((bl > 1) || (re > 1) || (gr > 1) || (ye > 1)) && myC1 == 2)) {
                                            move = x + " " + y + " " + t + " " + 2;
                                            return move;
                                        }
                                    }
                                    if (myC1 == 1) {
                                        move = x + " " + y + " " + t + " " + 2;
                                        return move;
                                    }
                                    if (myC1 == 2) {
                                        move = x + " " + y + " " + t + " " + 2;
                                        return move;
                                    }
                                }
                            }
                        }
                    }
                    //In case a position has 1 placed ring
                    if ((re + bl + gr + ye + myC1 + myC2) == 1) {
                        for (int p = 0; p < 5; p++) {
                            Ring ring = new Ring(getColourList().get(0), p);
                            if (board.validateMove(x, y, p, getColourList().get(0)) && game.checkInv(ring)) {
                                move = x + " " + y + " " + p + " " + 1;
                                return move;
                            }
                        }
                        if (getColourList().size() > 1) {
                            for (int h = 0; h < 5; h++) {
                                Ring ring = new Ring(getColourList().get(1), h);
                                if (board.validateMove(x, y, h, getColourList().get(1)) && game.checkInv(ring)) {
                                    move = x + " " + y + " " + h + " " + 2;
                                    return move;
                                }
                            }
                        }
                    }
                    //Can I place a base = field win
                    if (difficulty > 2) {
                        if ((re + bl + gr + ye + myC1 + myC2) == 0) {
                            Ring ring = new Ring(getColourList().get(0), 0);
                            if (board.validateMove(x, y, 0, getColourList().get(0)) && game.checkInv(ring)) {
                                move = x + " " + y + " " + 0 + " " + 1;
                                return move;
                            }
                            if (getColourList().size() > 1) {
                                ring = new Ring(getColourList().get(1), 0);
                                if (board.validateMove(x, y, 0, getColourList().get(1)) && game.checkInv(ring)) {
                                    move = x + " " + y + " " + 0 + " " + 2;
                                    return move;
                                }
                            }
                        }
                    }
                }
            }
        }

        //Random
        String s = this.game.hint();
        String[] hint = s.split("\\s+");
        int l = Integer.parseInt(hint[1]);
        int y = Integer.parseInt(hint[3]);
        int size = Integer.parseInt(hint[5]);
        int colour = Integer.parseInt(hint[7]);

        move = hint[1] + " " + hint[3] + " " + hint[5] + " " + hint[7];
        return move;
    }

    /**
     * gets the move for the special base at the start of the game
     *
     * @return move the special base placement move
     */

    /*@
    ensures x > 1 && x < 5;
    ensures y > 1 && y < 5;
    ensures \result != null;
    pure;
     */
    @Override
    public String getMoveStart() {
        Random random = new Random();
        int x = random.nextInt(3) + 2;
        int y = random.nextInt(3) + 2;
        return (x + " " + y);
    }
}
