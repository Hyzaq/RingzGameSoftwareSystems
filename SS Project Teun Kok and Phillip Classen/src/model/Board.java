package src.model;

import java.util.HashMap;
import java.util.*;

public class Board extends Observable {

    /**
     * Creates empty board
     */

    public Board() {
        int id;
        //Create a map for this board
        HashMap boardMap = new HashMap<Integer, Ring[]>();

        //Creates a stringbuilder for the id
        StringBuilder sb = new StringBuilder();

        //For all possible x and y values, put an ID with an array in the Hashmap.
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                //Create a nicer ID (for y = 1 it's 11 --> 12 --> 13 etc.)
                sb.setLength(0);
                sb.append(x);
                sb.append(y);
                id = Integer.parseInt(sb.toString());
                Ring[] rings = new Ring[5];
                for (int i = 0; i < 5; i++) {
                    Ring emptyRing = new Ring(Colour.EMPTY, i);
                    rings[i] = emptyRing;
                }
                boardMap.put(id, rings);
            }
        }
        this.boardMap = boardMap;
    }

    //The map keeping track of the field of our board
    HashMap<Integer, Ring[]> boardMap;
    StringBuilder sb = new StringBuilder();

    /**
     * Returns the boardMap for this Board
     *
     * @return The boardMap.
     */

    /*@
        ensures \result != null;
        pure;
     */

    public synchronized HashMap<Integer, Ring[]> getBoardMap() {
        return boardMap;
    }

    /**
     * getID for a faster search in our board map
     *
     * @param x
     * @param y
     * @return returns the ID for the BoardArray
     */

    /*@
    requires x >= 0;
    requires y >= 0;
    ensures \result >= 0;
    pure;
    */
    //Returns the ID of x/y
    public int getID(int x, int y) {
        int id = 0;
        if (x < 0) {
            x = x + 1;
        } else if (y < 0) {
            y = y + 1;
        }
        sb.setLength(0);
        sb.append(x);
        sb.append(y);
        id = Integer.parseInt(sb.toString());
        return id;
    }


    /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    ensures \result != null;
    pure;
    */
    /**
     *
     * @param x
     * @param y
     * @return Array of the Board at specific position
     */
    public Ring[] getBoardArrayXY(int x, int y) {
        int u = x - 1;
        int k = y - 1;
        int id = getID(u, k);
        return getBoardMap().get(id);
    }


    /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    requires z >= 0 && z < 6;
    ensures \result != null;
    pure;
    */

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return ring[z] returns the ring in the array with the size z
     */
    public Ring getBoardRingXYZ(int x, int y, int z) {
        int u = x -1;
        int k = y -1;
        Ring[] rings = this.boardMap.get(getID(u, k));
        return rings[z];
    }

    /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    ensures \result != null;
    pure;
    */

    /**
     *
     * @param id
     * @param z
     * @return ring[z] returns ring from the array with the ID of the array
     */
    public Ring getBoardRingID(int id, int z) {
        Ring[] rings = this.boardMap.get(id);
        return rings[z];
    }

    /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    requires z >= 0 && z < 6;
    requires (colour == EMPTY || colour == RED || colour == SPECIAL || colour == BLUE || colour == GREEN || colour == YELLOW);
    ensures \result != null;
    pure;
    */

    /**
     * Sets the ring on the the board and notifies the observer that is has changed
     *
     * @param x
     * @param y
     * @param z
     * @param colour
     */
    public void setRingXYZ(int x, int y, int z, Colour colour) {
        int p = x -1;
        int u = y -1;


        Ring[] rings = this.boardMap.get(getID(p, u));

        if (z == 0) {
            for (int i = 0; i < 5; i++) {
                Ring ringx = new Ring(colour, i);
                rings[i] = ringx;
            }
        } else {
            Ring ring = new Ring(colour, z);
            rings[z] = ring;
        }
        this.boardMap.replace(getID(p, u), rings);
        this.setChanged();
        this.notifyObservers("move made");
    }

    /*@
    requires p1 != null;
    requires p2 != null;
    requires p3 != null;
    requires p4 != null;
    ensures \result == false || \result == true;
    pure;
    */

    /**
     * Checks if the board has a winner
     * Checks if player still can place moves to determine end
     *
     *
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @return true or false
     */
    public boolean hasWinner(Player p1, Player p2, Player p3, Player p4) {

        //When invs are all empty.
        if (p1.getInv().size() < 2 ) {
            if (emptyInv(p1.getInv()) && emptyInv(p2.getInv()) && emptyInv(p3.getInv()) && emptyInv(p4.getInv())) {
                return true;
            }
            if (!checkMovePossible(p1) && !checkMovePossible(p2) && !checkMovePossible(p3) && !checkMovePossible(p4)) {
                return true;
            }
        } else if (p1.getInv().get(1).getInvColour().equals(Colour.YELLOW)) {
            if (emptyInv(p1.getInv()) && emptyInv(p2.getInv()) && emptyInv(p3.getInv())) {
                return true;
            }
            if (!checkMovePossible(p1) && !checkMovePossible(p2) && !checkMovePossible(p3)) {
                return true;
            }
        } else {
            if (emptyInv(p1.getInv()) && emptyInv(p2.getInv())) {
                return true;
            }
             if (!checkMovePossible(p1) && !checkMovePossible(p2)) {
                return true;
            }
        }
        return false;
    }

    /*@
    requires p != null;
    ensures \result == false || \result == true;
    pure;
    */

    /**
     * Checks if the player can still place a move somewhere on the board
     *
     * @param p
     * @return if a move possible
     */
    public boolean checkMovePossible(Player p) {
        if (this.isEmpty()) {
            return true;
        }
        if(this.isFull()){
            return false;
        }
        for (int i = 0; i < p.getInv().size(); i++) {
            for (int k = 0; k < p.getInv().get(i).getArray().length; k++) {
                Ring ring = new Ring (p.getInv().get(i).getArray()[k].getColour(),p.getInv().get(i).getArray()[k].getSize());
                if (ring != null) {
                    if (!ring.getColour().equals(Colour.EMPTY)) {
                        for (int x = 1; x < 6; x++) {
                            for (int y = 1; y < 6; y++) {
                                if (validateMove(x, y, ring.getSize(), ring.getColour())) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    requires z >= 0 && z < 6;
    requires (colour == EMPTY || colour == RED || colour == SPECIAL || colour == BLUE || colour == GREEN || colour == YELLOW);
    ensures \result == false || \result == true;
    pure;
    */

    /**
     * Checks the move, if it is valid.
     *
     * @param x
     * @param y
     * @param z
     * @param colour
     * @return if move is valid
     */
    public boolean validateMove(int x, int y, int z, Colour colour) {
        if (colour.equals(Colour.SPECIAL) && x < 5 && x > 1 && y < 5 && y > 1) {
            return true;
        }

        if (this.getBoardRingXYZ(x, y, z).getColour() == Colour.EMPTY) {
            return checkValid(x, y, z, colour);
        }
        return false;
    }

        /*@
    requires x >= 0 && x < 6;
    requires y >= 0 && y < 6;
    requires z >= 0 && z < 6;
    requires (colour == EMPTY || colour == RED || colour == SPECIAL || colour == BLUE || colour == GREEN || colour == YELLOW);
    ensures \result == false || \result == true;
    pure;
    */

    /**
     * Checks if the move can be placed on the board and if the right rings are next to it.
     *
     *
     * @param x
     * @param y
     * @param size
     * @param colour
     * @return if move is valid
     */
    public boolean checkValid(int x, int y, int size, Colour colour) {
        Boolean valid = false;

        //for the array itself
        if (size == 0) {
            for (int p = 0; p < 5; p++) {
                if (!this.getBoardArrayXY(x, y)[p].getColour().equals(Colour.EMPTY)) {
                    return false;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (this.getBoardArrayXY(x, y)[i].getColour().equals(colour)) {
                valid = true;
            }
        }


        //for x + 1
        for (int i = 0; i < 5; i++) {
            if ((x + 1) > 5) {
                break;
            }
            if ((size == 0) && !this.getBoardRingXYZ((x + 1), y, 0).getColour().equals(Colour.EMPTY)) {
                return false;
            }
            if ((this.getBoardArrayXY((x + 1), y)[i].getColour()).equals(colour) || this.getBoardArrayXY((x + 1), y)[i].getColour().equals(Colour.SPECIAL)) {
                valid = true;
            }
        }

        //for x - 1
        for (int i = 0; i < 5; i++) {
            if ((x - 1) < 1) {
                break;
            }
            if ((size == 0) && !this.getBoardRingXYZ((x - 1), y, 0).getColour().equals(Colour.EMPTY)) {
                return false;
            }
            if ((this.getBoardArrayXY((x - 1), y)[i].getColour()).equals(colour) || this.getBoardArrayXY((x - 1), y)[i].getColour().equals(Colour.SPECIAL)) {
                valid = true;
            }
        }

        //for y + 1
        for (int i = 0; i < 5; i++) {
            if ((y + 1) > 5) {
                break;
            }
            if ((size == 0) && !this.getBoardRingXYZ(x, (y + 1), 0).getColour().equals(Colour.EMPTY)) {
                return false;
            }
            if ((this.getBoardArrayXY(x, (y + 1))[i].getColour()).equals(colour) || this.getBoardArrayXY(x, (y + 1))[i].getColour().equals(Colour.SPECIAL)) {
                valid = true;
            }
        }

        //for y - 1
        for (int i = 1; i < 5; i++) {
            if ((y - 1) < 1) {
                break;
            }
            if ((size == 0) && !this.getBoardRingXYZ(x, (y - 1), 0).getColour().equals(Colour.EMPTY)) {
                return false;
            }
            if ((this.getBoardArrayXY(x, (y - 1))[i].getColour()).equals(colour) || this.getBoardArrayXY(x, (y - 1))[i].getColour().equals(Colour.SPECIAL)) {
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Checks if the inventory is empty
     *
     * @param invL
     * @return if inventory empty
     */
    public boolean emptyInv(ArrayList<Inventory> invL) {
        if (invL.size() > 1) {
            for (int x = 0; x < 2; x++) {
                for (int i = 0; i < invL.get(x).getArray().length; i++) {
                    if (!invL.get(x).getArray()[i].getColour().equals(Colour.EMPTY)) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < invL.get(0).getArray().length; i++) {

                if (!invL.get(0).getArray()[i].getColour().equals(Colour.EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }


    /*@
    requires (colour == EMPTY || colour == RED || colour == SPECIAL || colour == BLUE || colour == GREEN || colour == YELLOW);
    ensures \result >= 0;
    pure;
    */

    /**
     * Counts the points of a colour
     *
     * @param colour
     * @return the points of a colour
     */
    public int hasPoints(Colour colour) {
        int RC = 0;
        int GC = 0;
        int BC = 0;
        int YC = 0;
        for (int x = 1; x < 6; x++) {
            for (int y = 1; y < 6; y++) {
                int RP = 0;
                int GP = 0;
                int BP = 0;
                int YP = 0;
                if (!getBoardArrayXY(x, y)[0].colour.equals(Colour.EMPTY)) {
                    continue;
                }
                for (int i = 0; i < 5; i++) {
                    switch (getBoardArrayXY(x, y)[i].getColour()) {
                        case RED:
                            RP += 1;
                            break;
                        case BLUE:
                            BP += 1;
                            break;
                        case GREEN:
                            GP += 1;
                            break;
                        case YELLOW:
                            YP += 1;
                            break;
                        default:
                            continue;
                    }
                }

                if (RP == 0 && GP == 0 && BP == 0 && YP == 0) {
                    continue;
                } else if (RP > GP && RP > BP && RP > YP) {
                    RC += 1;
                } else if (GP > RP && GP > BP && GP > YP) {
                    GC += 1;
                } else if (BP > RP && BP > GP && BP > YP) {
                    BC += 1;
                } else if (YP > RP && YP > BP && YP > GP) {
                    YC += 1;
                }
            }
        }

        if (colour.equals(Colour.RED)) {
            return RC;
        } else if (colour.equals(Colour.YELLOW)) {
            return YC;
        } else if (colour.equals(Colour.BLUE)) {
            return BC;
        } else if (colour.equals(Colour.GREEN)) {
            return GC;
        } else return 0;
    }

    /**
     * Resets the board
      */
    public void reset() {
        for (int x = 1; x < 6; x++) {
            for (int y = 1; y < 6; y++) {
                for (int p = 0; p < 5; p++) {
                    Ring emptyRing = new Ring(Colour.EMPTY, p);
                    getBoardArrayXY(x, y)[p] = emptyRing;
                }
            }
        }
    }

    public boolean isEmpty() {
        for (int i = 1; i < 6; i++) {
            for (int k = 1; k < 6; k++) {
                for (int j = 0; j < 5; j++) {
                    Ring ring = new Ring(Colour.EMPTY, j);
                    if (!this.getBoardRingXYZ(i, k, j).toStringRing().equals(ring.toStringRing())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isFull() {
        for (int i = 1; i < 6; i++) {
            for (int k = 1; k < 6; k++) {
                for (int j = 0; j < 5; j++) {
                    if (this.getBoardRingXYZ(i, k, j).getColour().equals(Colour.EMPTY)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}