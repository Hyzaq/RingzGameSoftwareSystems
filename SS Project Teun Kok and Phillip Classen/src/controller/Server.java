// CLientHandler in my old project is the server handeling messages
//ClientConnection is the client side of the game messages

package src.controller;

import src.protocol.GameProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


/**
 * The type Server.
 */
public class Server {

    private static int port = 1337;
    private static HashMap<String, Handler> names = new HashMap<String, Handler>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private static ArrayList<String> inGame = new ArrayList<>();

    /**
     * The entry point of application.
     * Request port from input
     * Create Handler for each client connected
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Server started...");
        System.out.println("Type in the Port for the Server: ");
        Scanner in = new Scanner(System.in);
        port = Integer.parseInt(in.nextLine());
        ServerSocket listener = new ServerSocket(port);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }

    /**
     * Gets names.
     *
     * @return the list names of all connected clients
     */
    public static HashMap<String, Handler> getNames() {
        synchronized (names) {
            return names;
        }
    }

    /**
     * Gets socket.
     *
     * @param nickname the nickname of the client with the socket
     * @return the socket
     */
    public static Handler getSocket(String nickname) {
        return names.get(nickname);
    }


    /**
     * Start game game session.
     *
     * @param namemess the combined String of all names that start a game together
     * @return the game session
     * @throws IOException the io exception
     */
    public static GameSession startGame(String namemess) throws IOException {
        String[] mess = namemess.split(" ");
        GameSession x = null;
        if (mess.length == 4) {
            x = new GameSession(mess[0], mess[1], mess[2], mess[3]);
        } else if (mess.length == 3) {
            x = new GameSession(mess[0], mess[1], mess[2]);
        } else if (mess.length == 2) {
            x = new GameSession(mess[0], mess[1]);
        }

        new Thread(x).start();
        return x;

    }

    /**
     * The type Handler.
     * ClientHandler
     */
    public static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;


        /**
         * Instantiates a new Handler.
         *
         * @param socket the socket
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Creates in and output streams
         * <p>
         * place clients into names list
         * removes on disconnect
         */
        @Override
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (!socket.isClosed()) {
                    handleMessage(receiveMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if (name != null) {
                    names.remove(name);

                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Gets name from sock.
         *
         * @param handler the handler
         * @return the name from sock
         */
        public String getNameFromSock(Server.Handler handler) {
            for (Map.Entry<String, Handler> entry : names.entrySet()) {

                if (entry.getValue().equals(handler)) {
                    return entry.getKey();
                }
            }
            return null;
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
                out.close();
                e.printStackTrace();
            }
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

                case GameProtocol.CLIENT_JOINREQUEST:       // joinrequest Pittt 0 0 0 0
                    if (str[1] != null) {
                        this.name = str[1];
                    }
                    synchronized (names) {
                        if (!names.containsKey(name)) {
                            names.put(name, this);
                            sendMessage(GameProtocol.SERVER_ACCEPTJOIN + " " + this.name + " " + "0 0 0 0");
                            System.out.println(GameProtocol.SERVER_ACCEPTJOIN + " " + this.name + " " + "0 0 0 0");
                            writers.add(out);
                            //sendMessageAll(GameProtocol.LOBBY_UPDATE + " " + lobbyToString());
                            System.out.println(names.toString());
                        } else {
                            sendMessage(GameProtocol.SERVER_DENYJOIN + " " + this.name);
                            System.out.println(GameProtocol.SERVER_DENYJOIN + " " + this.name);
                        }
                    }
                    break;

                /**
                 * Gamerequest
                 *
                 * Selects enemies for the player
                 * sends startgame to the clients
                 * Starts gamesession/game on serverside
                 *
                 */
                case GameProtocol.CLIENT_GAMEREQUEST:
                    String str1 = "";
                    String str2 = "";
                    ArrayList<String> test = new ArrayList<String>();
                    int i = Integer.parseInt(str[1]) - 1;


                    for (Map.Entry<String, Handler> entry : names.entrySet()) {

                        if (!entry.getKey().equals(this.name)) {
                            test.add(entry.getKey());
                        }

                        if (entry.getKey().equals(this.name)) {
                            str2 = entry.getKey();
                        }

                    }
                    if ((names.size()) >= Integer.parseInt(str[1])) {
                        for (int k = 0; k < i; k++) {
                            str1 = str1 + " " + test.get(k);
                        }
                        str1 = str2 + str1;
                        str1 = str1.trim();

                        for (int x = 0; x < i; x++) {
                            String t = test.get(x);
//                            if (!inGame.contains(t)) {
                            getSocket(t).sendMessage(GameProtocol.SERVER_STARTGAME + " " + str1);
//                                inGame.add(test.get(x));
//                            }
                        }
                        sendMessage(GameProtocol.SERVER_STARTGAME + " " + str1);

                        startGame(str1);
                        handleMessage(receiveMessage());
                    } else {
                        handleMessage(receiveMessage());
                    }


                /**
                 * Setmove
                 *
                 * Forwards message to ServerSideGameHandler class
                 */
                case GameProtocol.CLIENT_SETMOVE:

                    (GameSession.getServerSidegameHandlerBySock(this)).handleMessage(message);
                    handleMessage(receiveMessage());
                    break;

                case GameProtocol.CLIENT_CHATE:
                    (GameSession.getServerSidegameHandlerBySock(this)).handleMessage(message);
                    handleMessage(receiveMessage());
                    break;
            }
            try {
                handleMessage(receiveMessage());

            } catch (StackOverflowError | IOException e) {
                System.out.println(this.getNameFromSock(this) + " disconnected from the server");
                //this.out.close();
                //this.socket.close();

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

            try {
                message = in.readLine();
                System.out.println(message);

            } catch (IOException e) {

                //e.printStackTrace();
                in.close();
                out.close();
                socket.close();

            }
            return message;
        }

        /**
         * Send message all.
         *
         * @param message the message
         */
        public static void sendMessageAll(String message) {

            for (PrintWriter writer : writers) {
                writer.println(message);
            }
        }

    }


}