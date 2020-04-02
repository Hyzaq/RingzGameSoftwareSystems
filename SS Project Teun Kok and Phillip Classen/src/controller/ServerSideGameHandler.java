package src.controller;

import src.model.Ring;
import src.protocol.GameProtocol;

import static src.controller.Server.getSocket;

import java.io.IOException;
import java.util.ArrayList;


/**
 * The type Server side game handler.
 */
public class ServerSideGameHandler extends Thread {

    private static ArrayList<String> nicknames = new ArrayList<String>();
    private Server.Handler sock = null;
    private boolean gameOver = false;
    private static GameSession session;

    /**
     * Instantiates a new Server side game handler.
     *
     * @param sock    the sock
     * @param session the session
     * @throws IOException the io exception
     */
    public ServerSideGameHandler(Server.Handler sock, GameSession session) throws IOException {
        this.sock = sock;
        this.session = session;
    }

    /**
     * Loops through handle messages until gameover == true and socket is alive
     */
    public void run() {
        try {
            do {
                handleMessage(receiveMessage());
            } while (!this.gameOver && sock.isAlive());
        } catch (IOException e) {
            System.out.println("Error");
        }
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
     * Handle message.
     *
     * @param message the message
     * @throws IOException the io exception
     */
    public void handleMessage(String message) throws IOException {
        String[] str = message.split(" ");

        switch (str[0]) {


            /**
             * Setmove
             *
             * Server checks player of Setmove
             * Created Ring to place and deletes ring in the inventory
             * Checks if next player can make a move and sends new moverequest or ends the game if no-one can place a ring anymore
             *
             */
            case GameProtocol.CLIENT_SETMOVE:
                if (this.session.getGame().getCurrentPlayer().getName().equals(this.sock.getNameFromSock(sock)) ) {        // && gameSession.getGame().getBoard().checkMove(your move)
                    Ring ring = new Ring(this.session.getGame().getCurrentPlayer().getColourByInt(Integer.parseInt(str[4])), Integer.parseInt(str[3]));

                    if (this.session.getGame().getBoard().validateMove(Integer.parseInt(str[1])+1, Integer.parseInt(str[2])+1, Integer.parseInt(str[3]),this.session.getGame().getCurrentPlayer().getColourByInt(Integer.parseInt(str[4])))) {
                        this.session.getGame().getBoard().setRingXYZ(Integer.parseInt(str[1])+1, Integer.parseInt(str[2])+1, ring.getSize(), ring.getColour());
                        for (int i = 0; i < this.session.getGame().getCurrentPlayer().getInv().size(); i++) {
                            if (this.session.getGame().getCurrentPlayer().getInv().get(i).getInvColour().equals(ring.getColour())) {
                                this.session.getGame().getCurrentPlayer().getInv().get(i).removeRing(ring);
                            }
                        }
                    } else {
                        sendMessage(GameProtocol.SERVER_DENYMOVE + " " + str[1]);
                    }
                    sendMessageAllInGame(GameProtocol.SERVER_NOTIFYMOVE + " " + this.session.getGame().getCurrentPlayer().getName() + " " + str[1] + " " + (Integer.parseInt(str[2])) + " " + (Integer.parseInt(str[3])) + " " + str[4]);
                    this.session.getGame().changeCurrentPlayer();
                    this.session.getGame().checkIfSkipPlayer();

                    if(!this.session.getGame().getgameStatus()) {
                        sendMessageAllInGame(GameProtocol.SERVER_GAMEOVER + " " + this.session.getGame().getWinner(this.session.getGame().getBoard()));
                        this.session.resetSession();
                        break;
                    } else {
                        this.session.moveRequest();
                    }
                } else {
                    sendMessageAllInGame(GameProtocol.SERVER_GAMEOVER + " " + this.session.getGame().getWinner(this.session.getGame().getBoard()));
                    this.session.resetSession();
                    break;
                   // System.exit(0);
                }
                break;

            case GameProtocol.CLIENT_CHATE:
                String peter = "";
                for (int i = 1; i < str.length; i++) {
                    peter = peter + str[i];
                }
                sendMessageAllInGame(GameProtocol.SERVER_SENDCHAT + " " + peter);
                break;

            default:

                sendMessage(GameProtocol.SERVER_INVALIDCOMMAND);
                break;
        }
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        try {
            sock.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message all in game.
     *
     * @param message the message
     */
    public static void sendMessageAllInGame(String message) {
        for (String name : session.getPlayerNamesinSession()) {
            getSocket(name).sendMessage(message);
        }
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public static GameSession getSession() {
        return session;
    }

    /**
     * Gets sock.
     *
     * @return the sock
     */
    public Server.Handler getSock() {
        return sock;
    }
}

