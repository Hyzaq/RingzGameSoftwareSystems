package test.src.model;

import org.junit.Before;
import org.junit.Test;
import src.model.*;
import src.view.game.TUIViewGame;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class GameTest {
    private TUIViewGame v;
    private Board b;
    private Ring r;
    private Inventory inv;
    private HumanPlayer player;
    private HumanPlayer player1;
    private HumanPlayer player2;
    private HumanPlayer player3;
    private HumanPlayer player4;
    private ArrayList invL;
    private ArrayList invLE, invT;
    private Inventory inv2, inv3;
    private Inventory invE;
    private Ring eRing;
    private Game game;

    @Before
    public void setup() {
        b = new Board();
        v = new TUIViewGame();
        r = new Ring(Colour.BLUE, 2);
        eRing = new Ring(Colour.EMPTY, 0);
        inv = new Inventory(Colour.BLUE);
        inv2 = new Inventory(Colour.RED);
        inv3 = new Inventory(Colour.GREEN);
        invL = new ArrayList<Inventory>();
        invLE = new ArrayList<Inventory>();
        invT = new ArrayList<Inventory>();
        invE = new Inventory(Colour.EMPTY);
        invL.add(inv);
        invL.add(inv2);
        invLE.add(invE);
        invT.add(inv2);
        invT.add(inv3);
        player1 = new HumanPlayer("Peter", invLE, b);
        player2 = new HumanPlayer("Teun", invLE, b);
        player3 = new HumanPlayer("name3", invLE, b);
        player4 = new HumanPlayer("name4", invLE, b);
        player = new HumanPlayer("name", invL, b);
        game = new Game(2);

    }

    @Test
    public void playerCreateTest() {
        game.playerCreate("Peter", "Teun", "Pittt", "Arthur");
        assertEquals(false, game.getPlayerByName("Peter").equals(player1));

    }

    @Test
    public void getCurrentPlayerTest() {
        Player test = new HumanPlayer("Peter", invT, b);
        game.playerCreate("Peter", "Teun", "Pittt", "Arthur");
        assertEquals(true, game.getCurrentPlayer().getName().equals(test.getName()));
        assertEquals(false, game.getCurrentPlayer().getName().equals("Teun"));
    }

    @Test
    public void checkInvTest() {
        game.playerCreate("Peter", "Teun", "Pittt", "Arthur");
        Ring ring = new Ring(Colour.GREEN, 2);
        Ring ring1 = new Ring(Colour.BLUE, 4);

        assertEquals(true, game.checkInv(ring));
        assertEquals(false, game.checkInv(ring1));
    }

    @Test
    public void getWinnerTest() {
        game.playerCreate("Peter", "Teun", "Pittt", "Arthur");
        game.getBoard().setRingXYZ(2, 3, 0, Colour.BLUE);
        assertEquals(true, game.getWinner(game.getBoard()).equals("Teun"));
        game.getBoard().setRingXYZ(3, 3, 0, Colour.RED);
        assertEquals(true, game.getWinner(game.getBoard()).equals("Peter " + "Teun"));

    }

    @Test
    public void getgameStatusTest() {

        assertEquals(true, game.getgameStatus());
        game.setGameRunning(false);
        assertEquals(false, game.getgameStatus());

    }

    @Test
    public void getPlayCnt() {

        assertEquals(false, game.getPlayCnt() == 3);
        assertEquals(true, game.getPlayCnt() == 2);

    }

    @Test
    public void stringToColour() {

        assertEquals(false, game.stringToColour("RED").equals(Colour.YELLOW));
        assertEquals(true, game.stringToColour("RED").equals(Colour.RED));

    }

    @Test
    public void setPlayerTest(){
        game.playerCreate("Peter", "Teun", "Pittt", "Arthur");
        assertEquals(false,game.getPlayerByName("Teun").equals(player1) );
        game.setPlayer(player1,player2,player3,player4);
        assertEquals(true, game.getPlayerByName("name4").equals(player4) );



    }


    @Test
    public void hasWinnerTestEmptyInv() {
        assertEquals(true, b.hasWinner(player1, player2, player3, player4));
        player4 = new HumanPlayer("name4.1", invL, b);
        assertEquals(false, b.hasWinner(player1, player2, player3, player4));
    }

    @Test
    public void emptyInvTest() {
        assertEquals(true, b.emptyInv(invLE));
    }

    @Test
    public void printRingTest() {
        System.out.println("A print of a ring:\n");
        assertEquals("B 2", r.toStringRing());
        System.out.println(r.toStringRing());
    }

    @Test
    public void printInvTest() {
        System.out.println("\nA Print of the inventory:\n");
        v.printInv(invL);
    }

    @Test
    public void printBoardTest() {
        System.out.println("\nA print of the Board: \n");
        v.printBoard(b);
    }

    @Test
    public void makeMove() {
        b.setRingXYZ(3, 2, 3, Colour.RED);
        assertEquals(true, b.checkValid(3, 2, 3, Colour.RED));
        assertEquals(false, b.checkValid(3, 2, 3, Colour.GREEN));
        assertEquals(32, b.getID(3, 2));
        assertEquals(Colour.RED, b.getBoardArrayXY(3, 2)[3].getColour());
        for (int i = 0; i < b.getBoardArrayXY(3, 2).length; i++) {
            System.out.println(b.getBoardArrayXY(3, 2)[i].getColour());
        }
    }

    @Test
    public void resetTest() {
        System.out.println("Only one position Red");
        b.setRingXYZ(1, 1, 1, Colour.RED);
        v.printBoard(b);
        b.reset();
        System.out.println("All spots empty");
        v.printBoard(b);
    }

    @Test
    public void hasPointsTest() {
        b.setRingXYZ(1, 1, 1, Colour.YELLOW);
        b.setRingXYZ(1, 2, 4, Colour.BLUE);
        b.setRingXYZ(1, 3, 3, Colour.GREEN);
        b.setRingXYZ(1, 4, 2, Colour.RED);
        assertEquals(1, b.hasPoints(Colour.GREEN));
    }

    @Test
    public void validMove() {
        b.setRingXYZ(2, 2, 2, Colour.BLUE);
        assertEquals(true, b.checkValid(2, 3, 2, Colour.BLUE));
        assertEquals(true, b.checkValid(2, 2, 3, Colour.BLUE));
        assertEquals(false, b.checkValid(1, 1, 1, Colour.BLUE));
        assertEquals(true, b.checkValid(2, 3, 2, Colour.BLUE));
    }

    @Test
    public void playerTest() {
        assertEquals("Peter", player1.getName());
        assertEquals(invLE, player1.getInv());
        assertEquals(b, player1.getBoard());
        Ring ring = new Ring(Colour.RED, 2);
        player1.getInv().get(0).getArray()[0] = ring;
        player2.getInv().get(0).removeRing(ring);
        assertEquals(Colour.EMPTY, player2.getInv().get(0).getArray()[0].getColour());
    }

}
