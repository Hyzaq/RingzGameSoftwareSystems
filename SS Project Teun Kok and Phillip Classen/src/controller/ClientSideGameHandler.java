package src.controller;

import src.model.Colour;
import src.model.Board;
import src.model.Game;
import src.model.Player;
import src.model.Ring;
import src.protocol.GameProtocol;
import src.view.game.GameView;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;


/**
 * The type Client side game handler.
 */
public class ClientSideGameHandler {

    private Player player;
    private GameView view;
    private Board board;
    private Game game;
    private Client client;


    /**
     * Instantiates a new Client side game handler.
     *
     * @param player the player
     * @param board  the board
     * @param game   the game
     * @param client the client
     * @throws IOException the IOException
     */
    public ClientSideGameHandler(Player player, Board board, Game game, Client client) throws IOException {
        this.player = player;
        this.board = board;
        this.game = game;
        this.view = client.getView();
        this.client = client;
    }

    /**
     * Receive message string.
     *
     * @return the string
     * @throws IOException the io exception
     */
    public String receiveMessage() throws IOException {
        String message = "";

        return message;
    }

    /**
     * Handle messages for the Game that the Client forwards to this class.
     *
     * @param message the message
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public void handleMessage(String message) throws IOException, InterruptedException {

        String[] str = message.split(" ");

        switch (str[0]) {


            /**
             * Moverequest
             *
             * Checks if -AI.
             * First move is Special Base
             * Normal move - validated before send
             * Send out setmove
             *
             */
            case GameProtocol.SERVER_MOVEREQUEST:

                if (this.player.getName().contains("-AI")) {
                    if (this.game.getBoard().isEmpty()) {
                        String str2 = this.player.getMoveStart();
                        String[] str3 = str2.split(" ");
                        sendMessage(GameProtocol.CLIENT_SETMOVE + " " + (Integer.parseInt(str3[0]) - 1) + " " + (Integer.parseInt(str3[1]) - 1) + " " + 0 + " " + 0);

                    } else {

                        String strmove = this.player.getMove(client.getAiDiff());
                        String[] str1 = strmove.split(" ");

                        if (!strmove.equals("possible")) {

                            sendMessage(GameProtocol.CLIENT_SETMOVE + " " + (Integer.parseInt(str1[0]) - 1) + " " + (Integer.parseInt(str1[1]) - 1) + " " + str1[2] + " " + str1[3]);

                        } else {
                            System.out.println("Recieved no move possible");
                        }
                    }
                    handleMessage(receiveMessage());

                } else {

                    view.printMessage("In moverequest in client side game handler");
                    view.setHintEnabled(false);

                    if (str[1].equals(this.player.getName()) && this.game.getBoard().isEmpty()) {
                        view.printMessage("Select a spot for the Special Base");

                        do {
                            Thread.sleep(0);
                        } while (!this.view.getValuesSet());

                        view.printMessage("First Special Base");
                        int x = this.view.getXValue();
                        int y = this.view.getYValue();

                        if (x < 1 || x > 5 || y < 1 || y > 5) {

                            view.printMessage("X or Y not between 1 - 5 ");
                            handleMessage(message);

                        } else {

                            String strmove = this.game.makeMove(x, y, 0, Colour.SPECIAL); //get values from input
                            if (!strmove.equals("inValid")) {
                                view.printMessage(GameProtocol.CLIENT_SETMOVE + " " + x + " " + y + " " + 0 + " " + 0);
                                sendMessage(GameProtocol.CLIENT_SETMOVE + " " + (x - 1) + " " + (y - 1) + " " + 0 + " " + 0);
                            } else {
                                view.printMessage("Invalid placement");
                                handleMessage(message);
                            }
                        }

                    } else if (str[1].equals(player.getName())) {

                        view.setHintEnabled(true);

                        this.view.printMessage("Inventory: ");

                        this.view.printMessage("Place your move:");

                        do {
                            Thread.sleep(0);
                        } while (!this.view.getValuesSet());

                        this.view.printMessage("Normal Move");

                        int x = this.view.getXValue();
                        int y = this.view.getYValue();
                        int z = this.view.getZValue();
                        Colour colour = this.view.getColour();

                        if (x < 1 || x > 5 || y < 1 || y > 5 || z > 4 || z < 0) {

                            view.printMessage("X or Y value not between 1 - 5");
                            handleMessage(message);

                        } else {
                            String strmove = this.game.makeMove(x, y, z, colour);
                            if (!strmove.equals("inValid")) {
                                this.view.printMessage(GameProtocol.CLIENT_SETMOVE + " " + x + " " + y + " " + z + " " + (this.player.getColourList().indexOf(colour) + 1));
                                sendMessage(GameProtocol.CLIENT_SETMOVE + " " + (x - 1) + " " + (y - 1) + " " + z + " " + (this.player.getColourList().indexOf(colour) + 1));
                            } else {
                                view.printMessage("Invalid placement");
                                handleMessage(message);
                            }
                        }


                    } else {
                        view.printMessage("ERROR somethings going wrong in player equals/ currentplayer");
                    }

                }
                this.handleMessage(receiveMessage());
                break;


            /**
             * Notifymove
             *
             * Sets the ring in the local Game when received from the Server
             * Removes Ring from Inventory of the Player
             * Changes the current player
             */
            case GameProtocol.SERVER_NOTIFYMOVE:
                view.setHintEnabled(false);
                view.printMessage("Player " + str[1] +
                        " set Stone on " + str[2] + ", " + str[3] + ", " + str[4] + " of Color: " + str[5]);
                Ring ring = new Ring(this.game.getCurrentPlayer().getColourByInt(Integer.parseInt(str[5])), Integer.parseInt(str[4]));
                this.game.getBoard().setRingXYZ((Integer.parseInt(str[2]) + 1), (Integer.parseInt(str[3]) + 1), ring.getSize(), ring.getColour());

                for (int i = 0; i < this.game.getCurrentPlayer().getInv().size(); i++) {
                    if (this.game.getCurrentPlayer().getInv().get(i).getInvColour().equals(ring.getColour())) {
                        this.game.getCurrentPlayer().getInv().get(i).removeRing(ring);
                    }
                }
                this.game.changeCurrentPlayer();
                this.game.checkIfSkipPlayer();
                this.handleMessage(receiveMessage());
                break;

            /**
             * DenyMove
             *
             * When Server denies a Move
             * Won't happen with our client, as we validate the move before sending already.
             * Kept for the clients of other people
             *
             */

            case GameProtocol.SERVER_SENDCHAT:
                String peter = "";
                for (int i = 2; i < str.length; i++) {
                    peter = peter + str;
                }
                view.printMessage("CHAT " + peter);
                this.handleMessage(receiveMessage());

            case GameProtocol.SERVER_DENYMOVE:

                if (this.player.getName().contains("-AI")) {

                    String strmove = this.player.getMove(client.getAiDiff());
                    String[] str1 = strmove.split(" ");

                    if (!strmove.equals("possible")) {

                        client.sendMessage(GameProtocol.CLIENT_SETMOVE + " " + (Integer.parseInt(str1[0]) - 1) + " " + (Integer.parseInt(str1[1]) - 1) + " " + str1[2] + " " + str1[3]);

                    }
                } else {


                    view.printMessage("Move is not valid please make a different move!");

                    int x = this.view.getXValue();
                    int y = this.view.getYValue();
                    int z = this.view.getZValue();
                    Colour colour = this.view.getColour();

                    String strmove = this.game.makeMove(x, y, z, colour);
                    if (!strmove.equals("inValid")) {
                        this.view.printMessage(GameProtocol.CLIENT_SETMOVE + " " + this.player.getName() + " " + x + " " + y + " " + this.player.getColourList().indexOf(colour));
                        sendMessage(GameProtocol.CLIENT_SETMOVE + " " + this.player.getName() + " " + x + " " + y + " " + this.player.getColourList().indexOf(colour));
                    } else {
                        view.printMessage("Invalid placement");
                        handleMessage(message);
                    }
                }
                break;

            /**
             * GameOver
             *
             * Called when the game is over
             * Announces winner or tie
             *
             */
            case GameProtocol.SERVER_GAMEOVER:

                //this.game.reset();
                if (str.length == 2) {
                    view.printMessage("Player " + str[1] + " won the Game.");
                } else if (str.length == 3) {
                    view.printMessage("Tie between: " + str[1] + "and " + str[2]);
                }
                //resetCSGH();
                break;
        }
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        //PrintWriter out = new PrintWriter(client.getOut(), true);
        client.sendMessage(message);
        //out.println(message);
    }

    public void resetCSGH() {
        /*try {
            this.wait(2000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
    }
}
