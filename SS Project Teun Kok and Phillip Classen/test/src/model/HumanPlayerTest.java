package test.src.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import src.model.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

/**
 * HumanPlayer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jan 28, 2018</pre>
 */
public class HumanPlayerTest {

    private Player player;
    private ArrayList<Inventory> invL = new ArrayList<Inventory>();
    private Inventory inv, inv2;
    private Board board = new Board();


    @Before
    public void before() throws Exception {

        inv = new Inventory(Colour.BLUE);
        inv2 = new Inventory(Colour.RED);
        invL.add(inv);
        invL.add(inv2);
        player = new HumanPlayer("Peter", invL, board);

    }

    /**
     * Method: getName()
     */
    @Test
    public void testGetName() throws Exception {

        assertEquals(true, player.getName().equals("Peter"));
        assertEquals(false, player.getName().equals("Notplayer"));
    }

    /**
     * Method: getColourList()
     */
    @Test
    public void testGetColourList() throws Exception {

        ArrayList<Colour> x = new ArrayList<Colour>();
        x.add(Colour.BLUE);
        x.add(Colour.RED);
        ArrayList<Colour> y = new ArrayList<Colour>();
        y.add(Colour.YELLOW);

        assertEquals(true,player.getColourList().equals(x));
        assertEquals(false, player.getColourList().equals(y));


    }

    /**
     * Method: getBoard()
     */
    @Test
    public void testGetBoard() throws Exception {

        Board notboard = new Board();

        assertEquals(true, player.getBoard().equals(board));
        assertEquals(false, player.getBoard().equals(notboard));


    }

    /**
     * Method: getInv()
     */
    @Test
    public void testGetInv() throws Exception {

        ArrayList<Inventory> notinv = new ArrayList<Inventory>();

        assertEquals(true, player.getInv().equals(invL));
        assertEquals(false, player.getInv().equals(notinv));

    }

    /**
     * Method: getColourByInt(int nr)
     */
    @Test
    public void testGetColourByInt() throws Exception {

        assertEquals(true, player.getColourByInt(1).equals(Colour.BLUE));
        assertEquals(false, player.getColourByInt(0).equals(Colour.BLUE));
        assertEquals(true, player.getColourByInt(2).equals(Colour.RED));

    }


} 
