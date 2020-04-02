package src.model;

import java.util.ArrayList;
import java.util.Random;


/**
 * The type Game.
 */
public class Game {

    private Board board;
    private boolean gameRunning = false;
    private boolean newGame = false;
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private Player ply1;
    private Player ply2;
    private Player ply3;
    private Player ply4;
    private static int currentP = 1;
    private int playCnt = 0;
    private Game game;
    private int x;
    private int y;
    private int z;
    private Ring playRing;


    /**
     * Instantiates a new Game.
     *
     * @param playCnt the playcount
     */
    public Game(int playCnt) {
        this.board = new Board();
        this.playCnt = playCnt;
        gameRunning = true;
    }

    /**
     * Creates the players and assign the inventories to them
     * -AI in the name will create an AI player
     *
     * @param name1 the first player
     * @param name2 the second player
     * @param name3 the third player
     * @param name4 the fourth player
     */

    /*@
    ensures ply1 != null;
    ensures ply2 != null;
    ensures if (playCnt == 3) {
    ply3 != null;}
    ensures if (playCnt == 4) {
    ply4 != null && ply3 != null;}
    */
    public void playerCreate(String name1, String name2, String name3, String name4) {

        //Creating the inventories.
        Inventory invRed = new Inventory(Colour.RED);
        Inventory invBlue = new Inventory(Colour.BLUE);
        Inventory invGreen = new Inventory(Colour.GREEN);
        Inventory invYellow = new Inventory(Colour.YELLOW);

        ArrayList<Inventory> invListRed = new ArrayList<>();
        invListRed.add(invRed);
        ArrayList<Inventory> invListBlue = new ArrayList<>();
        invListBlue.add(invBlue);
        ArrayList<Inventory> invListGreen = new ArrayList<>();
        invListGreen.add(invGreen);
        ArrayList<Inventory> invListYellow = new ArrayList<>();
        invListYellow.add(invYellow);

        //Creating the inventories lists.
        if (playCnt == 2) {
            invListRed.add(invGreen);
            invListBlue.add(invYellow);
            if (name1.contains("-AI")) {
                ply1 = new AiPlayer(name1, invListRed, this.board, this);
            } else {
                ply1 = new HumanPlayer(name1, invListRed, this.board);
            }

            if (name2.contains("-AI")) {
                ply2 = new AiPlayer(name2, invListBlue, this.board, this);
            } else {
                ply2 = new HumanPlayer(name2, invListBlue, this.board);
            }
        }

        if (playCnt == 3) {
            invListRed.add(invRed.changeToSpecialInv());
            invListBlue.add(invBlue.changeToSpecialInv());
            invListGreen.add(invGreen.changeToSpecialInv());
            if (name1.contains("-AI")) {
                ply1 = new AiPlayer(name1, invListRed, this.board, this);
            } else {
                ply1 = new HumanPlayer(name1, invListRed, this.board);
            }

            if (name2.contains("-AI")) {
                ply2 = new AiPlayer(name2, invListBlue, this.board, this);
            } else {
                ply2 = new HumanPlayer(name2, invListBlue, this.board);
            }

            if (name3.contains("-AI")) {
                ply3 = new AiPlayer(name3, invListGreen, this.board, this);
            } else {
                ply3 = new HumanPlayer(name3, invListGreen, this.board);
            }
        }

        if (playCnt == 4) {
            if (name1.contains("-AI")) {
                ply1 = new AiPlayer(name1, invListRed, this.board, this);
            } else {
                ply1 = new HumanPlayer(name1, invListRed, this.board);
            }

            if (name2.contains("-AI")) {
                ply2 = new AiPlayer(name2, invListBlue, this.board, this);
            } else {
                ply2 = new HumanPlayer(name2, invListBlue, this.board);
            }

            if (name3.contains("-AI")) {
                ply3 = new AiPlayer(name3, invListGreen, this.board, this);
            } else {
                ply3 = new HumanPlayer(name3, invListGreen, this.board);
            }

            if (name4.contains("-AI")) {
                ply4 = new AiPlayer(name4, invListYellow, this.board, this);
            } else {
                ply4 = new HumanPlayer(name4, invListYellow, this.board);
            }
        }
    }

    /**
     * Gets current player through modulo .
     *
     * @return the current player
     */

    /*@
    ensures \result != null;
    ensures \result == playCnt;
    pure;
    */
    public Player getCurrentPlayer() {

        switch (currentP % (playCnt + 1)) {

            case 0:
                currentP += 1;
                getCurrentPlayer();
            case 1:
                currentP = 1;
                return ply1;
            case 2:
                currentP = 2;
                return ply2;
            case 3:
                currentP = 3;
                return ply3;
            case 4:
                currentP = 4;
                return ply4;

        }
        return null;
    }

    /*@
    ensures \result = true || \result = false;
    pure;
    */

    public boolean checkInv(Ring ring) {

        for (int i = 0; i < this.getCurrentPlayer().getInv().size(); i++) {
            for (int x = 0; x < this.getCurrentPlayer().getInv().get(i).getArray().length; x++) {
                Ring testRing = this.getCurrentPlayer().getInv().get(i).getArray()[x];
                if (testRing.getSize() == ring.getSize() && testRing.getColour().equals(ring.getColour())) {
                    return true;
                }
            }
        }
        return false;
    }

    /*@
    ensure currentPlayer != \old(currentPlayer) || gameRunning != \old(gameRunning);
    */

    public void checkIfSkipPlayer() {

        switch (this.getPlayCnt()) {
            case 2:
                if (!this.getBoard().hasWinner(ply1, ply2, null, null)) {
                    if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                        this.changeCurrentPlayer();
                        break;
                    }
                } else {
                    this.setGameRunning(false);
                    break;
                }
                break;
            case 3:
                if (!this.getBoard().hasWinner(ply1, ply2, ply3, null)) {

                    if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                        this.changeCurrentPlayer();
                        if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                            this.changeCurrentPlayer();
                            break;
                        }
                        break;
                    }
                } else {
                    this.setGameRunning(false);
                    break;
                }
                break;
            case 4:

                if (!this.getBoard().hasWinner(ply1, ply2, ply3, ply3)) {

                    if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                        this.changeCurrentPlayer();
                        if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                            this.changeCurrentPlayer();
                            if (!this.getBoard().checkMovePossible(this.getCurrentPlayer())) {
                                this.changeCurrentPlayer();
                                break;
                            }
                            break;
                        }
                        break;
                    }
                } else {
                    this.setGameRunning(false);
                    break;
                }
                break;
        }
    }

    /**
     * Make move string.
     *
     * @param x      the x value
     * @param y      the y value
     * @param z      the size of the ring
     * @param colour the colour of the ring
     * @return the string combinning everything
     */
    public String makeMove(int x, int y, int z, Colour colour) {
        if (this.board.isEmpty() && board.validateMove(x, y, z, colour)) {

            return x + " " + y + " " + 0 + " " + 0;

        } else if (board.validateMove(x, y, z, colour) && checkInv(new Ring(colour, z))) {

            return (x + " " + y + " " + z + " " + colour.toString());
        }
        return "inValid";
    }

    /**
     * Gets winner. Counts points for every colour
     *
     * @param board the board
     * @return the winner
     */

    /*@
    requires board != null;
    ensures \result != null
    pure;
    */
    public String getWinner(Board board) {
        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        int p4 = 0;
        int R = board.hasPoints(Colour.RED);
        int B = board.hasPoints(Colour.BLUE);
        int Y = board.hasPoints(Colour.YELLOW);
        int G = board.hasPoints(Colour.GREEN);
        if (playCnt == 2) {
            if (R + G > Y + B) {
                return ply1.getName();
            }
            if (R + G == Y + B) {

                for (int i = 0; i < 2; i++) {
                    for (int p = 0; p < ply1.getInv().get(i).getArray().length; p++) {
                        if (!ply1.getInv().get(i).getArray()[p].getColour().equals(Colour.EMPTY)) {
                            p1 += 1;
                        }
                    }
                    for (int p = 0; p < ply2.getInv().get(i).getArray().length; p++) {
                        if (!ply2.getInv().get(i).getArray()[p].getColour().equals(Colour.EMPTY)) {
                            p2 += 1;
                        }
                    }
                }
                if (p1 == p2) {
                    return ply1.getName() + " " + ply2.getName();
                } else if (p1 < p2) {
                    return ply1.getName();
                } return ply2.getName();
            }
        } else {
            return ply2.getName();
        }

        if (playCnt == 3) {
            Y = 0;
        }

        if (playCnt > 3) {
            for (int k = 0; k < ply2.getInv().get(0).getArray().length; k ++) {
                if (!ply2.getInv().get(0).getArray()[k].colour.equals(Colour.EMPTY)) {
                    p2 += 1;
                }
            }
            for (int t = 0; t < ply2.getInv().get(0).getArray().length; t ++) {
                if (!ply1.getInv().get(0).getArray()[t].colour.equals(Colour.EMPTY)) {
                    p1 += 1;
                }
            }
            for (int p = 0; p < ply2.getInv().get(0).getArray().length; p ++) {
                if (!ply1.getInv().get(0).getArray()[p].colour.equals(Colour.EMPTY)) {
                    p3 += 1;
                }
            }
            for (int l = 0; l < ply2.getInv().get(0).getArray().length; l ++) {
                if (!ply1.getInv().get(0).getArray()[l].colour.equals(Colour.EMPTY)) {
                    p4 += 1;
                }
            }
            if (R > B && R > Y && R > G) {
                return ply1.getName();
            } else if (B > R && B > Y && B > G) {
                return ply2.getName();
            } else if (G > R && G > Y && G > B) {
                return ply3.getName();
            } else if (Y > R && Y > B && Y > G) {
                return ply4.getName();
            } else if (B == R) {
                if (p2 == p1) {
                    return ply2.getName() + "" + ply1.getName();
                } else if (p2 < p1) {
                    return ply2.getName();
                } return  ply1.getName();
            } else if (R == Y) {
                if (p1 == p4) {
                    return ply1.getName() + "" + ply4.getName();
                } else if (p1 < p4) {
                    return ply1.getName();
                } return  ply4.getName();
            } else if (R == G) {
                if (p1 == p3) {
                    return ply1.getName() + "" + ply3.getName();
                } else if (p1 < p3) {
                    return ply1.getName();
                } return  ply3.getName();
            } else if (G == Y) {
                if (p3 == p4) {
                    return ply3.getName() + "" + ply4.getName();
                } else if (p3 < p4) {
                    return ply3.getName();
                } return  ply4.getName();
            } else if (B == Y) {
                if (p2 == p4) {
                    return ply2.getName() + "" + ply4.getName();
                } else if (p2 < p4) {
                    return ply2.getName();
                } return  ply4.getName();
            } else if (B == G) {
                if (p2 == p3) {
                    return ply2.getName() + "" + ply3.getName();
                } else if (p2 < p3) {
                    return ply2.getName();
                } return  ply3.getName();
            }
        }
        return "Dafuq did you do?";
    }


    /**
     * Reset.
     */
//Resets the board and the currentplayer.

    /*@
    ensures board.empty = true;
    ensures currentP = 1;
    */
    public void reset() {
        currentP = 1;
        board.reset();
    }

    /**
     * Gets status.
     *
     * @return the status
     */

    /*@
    requires gameStatus != null;
    ensures \result = gameRunning;
    pure;
    */
    public boolean getgameStatus() {
        return this.gameRunning;
    }

    /**
     * Gets board.
     *
     * @return the board
     */

    /*@
    requires board != null;
    ensures \result = board;
    pure;
    */
    public Board getBoard() {
        return board;
    }

    /**
     * Hint function will start at  0 0 and goes through the whole board to find a possible move.
     *
     * @return the the string of the move
     */

    /*@
    requires board != null;
    requires playCnt > 1 && playCnt < 5;
    requires game.getCurrentPlayer() != null;
    ensures \result != null;
    pure;
    */
    public String hint() {
        Random random = new Random();
        int rand = random.nextInt(2);
        if (playCnt == 4 || rand == 0) {
            Player p = this.getCurrentPlayer();
            for (int i = 0; i < p.getInv().size(); i++) {
                for (int k = 0; k < p.getInv().get(i).getArray().length; k++) {
                    Ring ring = p.getInv().get(i).getArray()[k];
                    if (!ring.getColour().equals(Colour.EMPTY)) {
                        for (int x = 1; x < 6; x++) {
                            for (int y = 5; y > 0; y--) {
                                if (this.board.validateMove(x, y, ring.getSize(), p.getInv().get(i).getInvColour())) {
                                    return "X: " + x + " -Y: " + y + " -Size: " + ring.getSize() + " -Colour: " + (i + 1);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Player p = this.getCurrentPlayer();
            for (int i = 2; i > 0; i--) {
                for (int k = 0; k < p.getInv().get(i - 1).getArray().length; k++) {
                    Ring ring = p.getInv().get(i - 1).getArray()[k];
                    if (!ring.getColour().equals(Colour.EMPTY)) {
                        for (int x = 5; x > 0; x--) {
                            for (int y = 1; y < 6; y++) {
                                if (this.board.validateMove(x, y, ring.getSize(), p.getInv().get(i - 1).getInvColour())) {
                                    return "X: " + x + " -Y: " + y + " -Size: " + ring.getSize() + " -Colour: " + (i);
                                }
                            }
                        }
                    }
                }
            }
        }
        return "No possible move";
    }

    /**
     * Gets play cnt.
     *
     * @return the play cnt
     */

    /*@
    requires playCnt != null;
    ensures \result == playCnt;
    pure;
    */
    public int getPlayCnt() {
        return playCnt;
    }

    /**
     * Sets game running.
     *
     * @param bool the bool
     */

    /*@
    requires bool = true || bool = false;
    ensures gameRunning = bool;
    */
    public void setGameRunning(Boolean bool) {
        this.gameRunning = bool;
    }

    /**
     * Change current player.
     */

    /*@
    requires currentP > 1;
    ensures currentP = \old(currentP) + 1;
    */
    public static void changeCurrentPlayer() {
        currentP += 1;
    }

    /**
     * String to colour colour.
     *
     * @param colour the colour
     * @return the colour
     */

    /*@
    requires colour != null;
    ensures (\result.equals(EMPTY) || \result.equals(RED) || \result.equals(SPECIAL) || \result.equals(BLUE) || \result.equals(GREEN) || \result.equals(YELLOW));
    */
    public Colour stringToColour(String colour) {
        switch (colour) {
            case "EMPTY":
                return Colour.EMPTY;

            case "RED":
                return Colour.RED;

            case "BLUE":
                return Colour.BLUE;

            case "GREEN":
                return Colour.GREEN;

            case "YELLOW":
                return Colour.YELLOW;

            case "SPECIAL":
                return Colour.SPECIAL;
        }
        return null;
    }

    /**
     * Sets player.
     *
     * @param ply1 the first player
     * @param ply2 the second player
     * @param ply3 the third player
     * @param ply4 the fourth player
     */

    /*@
    requires ply1 != null;
    requires ply2 != null;
    requires ply3 != null;
    requires ply4 != null;
    ensures this.ply1 = ply1;
    ensures this.ply2 = ply2;
    ensures this.ply3 = ply3;
    ensures this.ply4 = ply4;
    */
    public void setPlayer(Player ply1, Player ply2, Player ply3, Player ply4) {
        this.ply1 = ply1;
        this.ply2 = ply2;
        this.ply3 = ply3;
        this.ply4 = ply4;
    }

    /**
     * Gets player by name.
     *
     * @param string the string
     * @return the player by name
     */

    /*@
    requires string != null;
    ensures \result != null;
    pure;
    */
    public Player getPlayerByName(String string) {
        ArrayList<Player> x = new ArrayList<Player>();
        x.add(ply1);
        x.add(ply2);
        x.add(ply3);
        x.add(ply4);

        for (Player y : x) {
            if (y.getName().equals(string)) {
                return y;
            }
        }
        return null;
    }


}

