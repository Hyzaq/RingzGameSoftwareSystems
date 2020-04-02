package test.src.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import src.model.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Board Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jan 27, 2018</pre>
 */
public class BoardTest {

    private Game game;
    private Board board;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player;
    private ArrayList invL,invLE;
    private Inventory inv, inv2, invE;

    @Before
    public void before() throws Exception {

        board = new Board();
        inv = new Inventory(Colour.BLUE);
        inv2 = new Inventory(Colour.RED);
        invL = new ArrayList<Inventory>();
        invLE = new ArrayList<Inventory>();
        invE = new Inventory(Colour.EMPTY);
        invL.add(inv);
        invL.add(inv2);
        invLE.add(invE);
        player1 = new HumanPlayer("name1", invLE, board);
        player2 = new HumanPlayer("name2", invLE, board);
        player3 = new HumanPlayer("name3", invLE, board);
        player4 = new HumanPlayer("name4", invLE, board);
        player = new HumanPlayer("name", invL, board);

    }

    /**
     * Method: getID(int x, int y)
     */
    @Test
    public void testGetID() throws Exception {

        assertEquals(12, board.getID(1, 2));
        assertEquals(22, board.getID(2, 2));
        assertEquals(02, board.getID(-1, 2));

    }

    /**
     * Method: getBoardArrayXY(int x, int y)
     */
    @Test
    public void testGetBoardArrayXY() throws Exception {

        assertEquals(board.getBoardMap().get(11),board.getBoardArrayXY(2,2));
        assertEquals(board.getBoardMap().get(22),board.getBoardArrayXY(3,3));
        assertEquals(board.getBoardMap().get(33),board.getBoardArrayXY(4,4));
        assertEquals(false, board.getBoardMap().get(22).equals(board.getBoardArrayXY(4,4)));

    }

    /**
     * Method: getBoardRingXYZ(int x, int y, int z)
     */
    @Test
    public void testGetBoardRingXYZ() throws Exception {
        board.setRingXYZ(2,2, 2, Colour.BLUE);
        Ring ring = new Ring(Colour.BLUE, 2);
        Ring ring1 = new Ring(Colour.RED, 2);
        assertEquals(true, board.getBoardRingXYZ(2,2,2).toStringRing().equals(ring.toStringRing()));
        assertEquals(false, board.getBoardRingXYZ(2,2,2).toStringRing().equals(ring1.toStringRing()));


    }

    /**
     * Method: getBoardRingID(int id, int z)
     */
    @Test
    public void testGetBoardRingID() throws Exception {

        board.setRingXYZ(2,2, 2, Colour.BLUE);
        Ring ring = new Ring(Colour.BLUE, 2);
        Ring ring1 = new Ring(Colour.RED, 2);
        assertEquals(true, board.getBoardRingID(11,2).toStringRing().equals(ring.toStringRing()));
        assertEquals(false, board.getBoardRingID(11,2).toStringRing().equals(ring1.toStringRing()));

    }

    /**
     * Method: setRingXYZ(int x, int y, int z, Colour colour)
     */
    @Test
    public void testSetRingXYZ() throws Exception {

        Ring ring = new Ring(Colour.BLUE, 2);
        Ring ring1 = new Ring(Colour.RED, 2);
        board.setRingXYZ(2,2, 2, Colour.BLUE);
        assertEquals(true, board.getBoardRingXYZ(2,2,2).toStringRing().equals(ring.toStringRing()));
        assertEquals(false , board.getBoardRingXYZ(2,2,2).toStringRing().equals(ring1.toStringRing()));

    }

    /**
     * Method: hasWinner(Player p1, Player p2, Player p3, Player p4)
     */
    @Test
    public void testHasWinner() throws Exception {

        assertEquals(true, board.hasWinner(player1,player2,player3,player4));

    }

    /**
     * Method: checkMovePossible(Player p)
     */
    @Test
    public void testCheckMovePossible() throws Exception {

        assertEquals(true, board.checkMovePossible(player1));
        board.setRingXYZ(4,4,0,Colour.SPECIAL);
        assertEquals(true, board.checkMovePossible(player));
        assertEquals(false, board.checkMovePossible(player1));

    }

    /**
     * Method: validateMove(int x, int y, int z, Colour colour)
     */
    @Test
    public void testValidateMove() throws Exception {

        assertEquals(true, board.validateMove(3,3,0,Colour.SPECIAL));
        board.setRingXYZ(3,3,0,Colour.SPECIAL);
        assertEquals(false, board.validateMove(2,4,2,Colour.BLUE));
        assertEquals(true, board.validateMove(2,3,2,Colour.BLUE));


    }

    /**
     * Method: checkValid(int x, int y, int size, Colour colour)
     */
    @Test
    public void testCheckValid() throws Exception {

        board.setRingXYZ(3,3,0,Colour.SPECIAL);
        assertEquals(false, board.checkValid(2,4,2,Colour.BLUE));
        assertEquals(true, board.checkValid(2,3,2,Colour.BLUE));
    }

    /**
     * Method: emptyInv(ArrayList<Inventory> invL)
     */
    @Test
    public void testEmptyInv() throws Exception {

        assertEquals(true, board.emptyInv(player1.getInv()));

    }


} 
