package src.controller;

import src.model.Game;
import src.protocol.GameProtocol;
import src.model.Board;
import src.view.game.GUIViewGame;
import src.view.game.GameView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static src.controller.ServerSideGameHandler.sendMessageAllInGame;
import static src.controller.Server.getSocket;

/**
 * The type Game session.
 */
public class GameSession implements Runnable {

    private Game game;
    private GameSession session = null;
    PrintWriter out = null;
    private GameView view;
    private Board board;
    private static ServerSideGameHandler serverSideGamehandler1;
    private static ServerSideGameHandler serverSideGamehandler2;
    private static ServerSideGameHandler serverSideGamehandler3;
    private static ServerSideGameHandler serverSideGamehandler4;
    private static String p1;
    private static String p2;
    private static String p3;
    private static String p4;
    private ArrayList<String> x;


    /**
     * Instantiates a new Game session.
     * Overloaded Constructor for 2 - 4 player
     *
     * @param p1 the first player
     * @param p2 the second player
     * @param p3 the third player
     * @param p4 the fourth player
     * @throws IOException the io exception
     */
//Constructor
    public GameSession(String p1, String p2, String p3, String p4) throws IOException {

        serverSideGamehandler1 = new ServerSideGameHandler(getSocket(p1), this);
        serverSideGamehandler2 = new ServerSideGameHandler(getSocket(p2), this);
        serverSideGamehandler3 = new ServerSideGameHandler(getSocket(p3), this);
        serverSideGamehandler4 = new ServerSideGameHandler(getSocket(p4), this);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.session = this;
        System.out.println("Creating Game");
        this.board = new Board();
        this.game = new Game(4);
        this.game.playerCreate(p1, p2, p3, p4);
        this.view = new GUIViewGame(this.game);
        this.getGame().setGameRunning(true);
        setPlayerNamesinSession(p1, p2, p3, p4);
        this.session.moveRequest();
    }

    /**
     * Instantiates a new Game session.
     * Overloaded Constructor for 2 - 4 player
     *
     * @param p1 the first player
     * @param p2 the second player
     * @param p3 the third player
     * @throws IOException the io exception
     */
    public GameSession(String p1, String p2, String p3) throws IOException {

        serverSideGamehandler1 = new ServerSideGameHandler(getSocket(p1), this);
        serverSideGamehandler2 = new ServerSideGameHandler(getSocket(p2), this);
        serverSideGamehandler3 = new ServerSideGameHandler(getSocket(p3), this);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.session = this;
        System.out.println("Creating Game");
        this.board = new Board();
        this.game = new Game(3);
        this.game.playerCreate(p1, p2, p3, null);
        this.view = new GUIViewGame(this.game);
        this.getGame().setGameRunning(true);
        setPlayerNamesinSession(p1, p2, p3);
        this.session.moveRequest();
    }

    /**
     * Instantiates a new Game session.
     * Overloaded Constructor for 2 - 4 player
     *
     * @param p1 the first player
     * @param p2 the second player
     * @throws IOException the io exception
     */
    public GameSession(String p1, String p2) throws IOException {

        serverSideGamehandler1 = new ServerSideGameHandler(getSocket(p1), this);
        serverSideGamehandler2 = new ServerSideGameHandler(getSocket(p2), this);
        this.p1 = p1;
        this.p2 = p2;
        this.session = this;
        System.out.println("Creating Game");
        this.board = new Board();
        this.game = new Game(2);
        this.game.playerCreate(p1, p2, null, null);
        this.view = new GUIViewGame(this.game);
        this.getGame().setGameRunning(true);
        setPlayerNamesinSession(p1, p2);
        this.session.moveRequest();
    }

    /**
     * Sets the GameRunning status to true
     *
     * game is running
     */
    @Override
    public void run() {
        this.getGame().setGameRunning(true);
    }

    public void resetSession(){
        if(this.getGame().getPlayCnt() >= 2){
            serverSideGamehandler1.stop();
            serverSideGamehandler2.stop();
        }
        if(this.getGame().getPlayCnt() >= 3){
            serverSideGamehandler3.stop();

        }
        if(this.getGame().getPlayCnt() == 4){
            serverSideGamehandler4.stop();
        }

    }

    /**
     * Move request
     *
     * Determine the socket of the current player and send a moverequest to him
     *  GameOver if status is false
     */
    public void moveRequest() {
        if (this.game.getgameStatus()) {

            getSocket(this.game.getCurrentPlayer().getName()).sendMessage(GameProtocol.SERVER_MOVEREQUEST + " " + this.game.getCurrentPlayer().getName());

        } else {
            if (!game.getgameStatus()) {
                sendMessageAllInGame(GameProtocol.SERVER_GAMEOVER + " " + this.game.getCurrentPlayer().getName()); //not completely finished

                //closeConnection();
            }
        }
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public GameView getView() {
        return view;
    }

    /**
     * Gets server sidegame handler by sock.
     *
     * @param sock the Socket
     * @return the ServerSideGameHandler by sock
     */
    public static ServerSideGameHandler getServerSidegameHandlerBySock(Server.Handler sock) {
        if (getSocket(p1).equals(sock)) {
            return serverSideGamehandler1;

        } else if (getSocket(p2).equals(sock)) {
            return serverSideGamehandler2;

        } else if (getSocket(p3).equals(sock)) {
            return serverSideGamehandler3;

        } else if (getSocket(p4).equals(sock)) {
            return serverSideGamehandler4;

        }
        return null;
    }

    /**
     * Sets player names in session.
     *
     * @param args the String list of all players
     */
    public void setPlayerNamesinSession(String... args) {
        x = new ArrayList<String>();
        for (String str : args) {
            x.add(str);
        }
    }

    /**
     * Gets player names in session.
     *
     * @return the player names in session
     */
    public ArrayList<String> getPlayerNamesinSession() {
        return this.x;
    }

}