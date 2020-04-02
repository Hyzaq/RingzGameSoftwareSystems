package src.controller;

import src.model.Colour;
import src.model.Game;
import src.protocol.GameProtocol;
import src.view.client.GUIViewClient;
import src.view.game.GUIViewGame;
import src.view.game.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;

/**
 * The type Client.
 */
public class Client {

    private BufferedReader in;
    private static PrintWriter out;
    private String nickname = null;
    private int port = 0;
    private String ip = null;
    private Socket socket;
    private SocketAddress address;
    private static Client client;
    private static GUIViewClient guiviewclient;
    private List playerList;
    private Game game;
    private GUIViewGame view;
    private ClientSideGameHandler clientSideGameHandler;
    private int aiDiff;

    /**
     * Constructor for the Client
     */
    private Client() {

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        client = new Client();
        guiviewclient = new GUIViewClient(client);
        client.run();
    }


    /**
     * The entry point of application.
     *
     *
     *
     */
    private void run() throws Exception {

        do {
            Thread.sleep(0);
        } while (ip == null || port == 0 || nickname == null);

        connectToServer();
        joinServer(nickname);

    }

    /**
     * Handling messages that the Client receives from the Server regarding the Connection
     * Messages regarding the Game will be send to ClientSideGameHandler class
     *
     * @param message
     * @throws Exception
     */
    public void handleMessage(String message) throws Exception {
        String[] str = message.split(" ");

        switch (str[0]) {

            case GameProtocol.SERVER_ACCEPTJOIN:
                System.out.println("Client accepted");
                guiviewclient.printMessage("Client accepted");
                guiviewclient.setServerStatus(true);
                guiviewclient.textFieldIP.setEnabled(false);
                guiviewclient.textFieldPort.setEnabled(false);
                guiviewclient.textFieldNickname.setEnabled(false);
                handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_DENYJOIN:
                guiviewclient.printMessage("Client denied, username already taken, try again.");
                String name = nickname;
                do {
                    Thread.sleep(0);
                } while (ip == null || port == 0 || nickname.equals(name));

                connectToServer();
                joinServer(nickname);
                break;

            case GameProtocol.SERVER_STARTGAME:
                this.game = new Game(str.length - 1);
                this.game.setGameRunning(true);

                this.view = new GUIViewGame(this.game, this);
                this.game.getBoard().addObserver(view);
                this.view.showBoard();

                if (str.length == 3) {
                    this.game.playerCreate(str[1], str[2], null, null);

                } else if (str.length == 4) {
                    this.game.playerCreate(str[1], str[2], str[3], null);

                } else {
                    this.game.playerCreate(str[1], str[2], str[3], str[4]);

                }
                clientSideGameHandler = new ClientSideGameHandler(game.getPlayerByName(nickname), this.game.getBoard(), this.game, this);
                view.setPrimaryColour(game.getPlayerByName(this.nickname).getColourList().get(0));

                if (game.getPlayerByName(this.nickname).getColourList().size()> 1) {
                    view.setSecondaryColour(game.getPlayerByName(this.nickname).getColourList().get(1));
                } else {
                    view.setSecondaryColour(Colour.EMPTY);
                }
                view.setInv();
                view.setGameCommunicationTab();
                this.handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_MOVEREQUEST:

                clientSideGameHandler.handleMessage(message);
                this.handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_NOTIFYMOVE:

                clientSideGameHandler.handleMessage(message);
                this.handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_SENDCHAT:

                view.printChat(message);
                this.handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_DENYMOVE:

                clientSideGameHandler.handleMessage(message);
                this.handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_GAMEOVER:


                if (str.length == 2) {
                    view.printMessage("Player " + str[1] + " won the Game.");
                } else if (str.length == 3) {
                    view.printMessage("Tie between: " + str[1] + "and " + str[2]);
                }
                handleMessage(receiveMessage());
                break;

            case GameProtocol.SERVER_INVALIDCOMMAND:
                handleMessage(receiveMessage());

            default:
                System.out.println("EEEEERRRRROOOOOOOORRRRRR");
                break;
        }


    }

    /**
     * Method to that listens to the input stream
     *
     * @return message
     * @throws IOException
     */

    public String receiveMessage() throws IOException {
        String message = "";

        try {
            message = in.readLine();
            System.out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
            in.close();
            socket.close();
        }
        return message;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        try {

            this.out.println(message);

        } catch (Exception e) {
            //out.close();
            //e.printStackTrace();
        }
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(ClientSideGameHandler handler) {
        this.clientSideGameHandler = handler;
    }


    /**
     * Sets nickname.
     *
     * @param str the str
     */
    public void setNickname(String str) {
        nickname = str;
    }

    /**
     * Sets port.
     *
     * @param str the str
     */
    public void setPort(int str) {
        port = str;
    }

    /**
     * Sets ip.
     *
     * @param str the str
     */
    public void setIp(String str) {
        ip = str;
    }

    /**
     * Used to make a join request to the Server
     * @param nickname
     */

    private void joinServer(String nickname) {

        sendMessage(GameProtocol.CLIENT_JOINREQUEST + " " + nickname + " 0 0 0 0");
        guiviewclient.printMessage(GameProtocol.CLIENT_JOINREQUEST + " " + nickname + " 0 0 0 0");
        System.out.println(GameProtocol.CLIENT_JOINREQUEST + " " + nickname + " 0 0 0 0");
        try {
            handleMessage(receiveMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to server.
     *
     * @throws Exception the IOException
     */
    public void connectToServer() {

        this.socket = new Socket();
        this.address = new InetSocketAddress(ip, port);
        try {
            socket.connect(address, 2000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            connectToServer();
        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public static Client getClient() {
        return client;
    }

    /**
     * Gets nickname of Client.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets list of players.
     *
     * @param list the PlayerList
     */
    public void setplayerList(List list) {
        playerList = list;
    }

    /**
     * Gets the view.
     *
     * @return the view
     */
    public GameView getView() {
        return view;
    }

    /**
     * Gets PrintWriter out.
     *
     * @return the out
     */
    public static PrintWriter getOut() {
        return out;
    }

    public void setAiDiff(int aiDiff){
        this.aiDiff = aiDiff;
    }

    public int getAiDiff(){
        return aiDiff;
    }

}
