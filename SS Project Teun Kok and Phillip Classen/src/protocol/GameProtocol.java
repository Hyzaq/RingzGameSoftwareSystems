package src.protocol;

public interface GameProtocol {

    /**
     * PAY ATTENTION:
     *
     * All arguments must be separated by a space. This also implies that the individual arguments are not allowed to contain spaces. The only exception to
     * this rule will be the chat function.
     */

    // --------------- Pre game ---------------

    /**
     * Value for the standart port
     */
    public static final int PORTNUMBER = 1337;


    /**
     * Used for joining a server (or a.k.a. "lobby")
     *
     * Arguments:
     * - name = Name of the client
     * - chat = Whether the client supports chat or not
     * - challenge = Whether the client supports challenge or not
     * - leaderboard = Whether the client supports leaderboard or not
     * - security = Whether the client supports security functionalities or not
     *
     * Requirements for arguments:
     * - chat = 0 || 1
     * - challenge = 0 || 1
     * - leaderboard = 0 || 1
     * - security = 0 || 1
     *
     * Example:
     *
     * My client, Pittt, wants to join and only support the chat feature
     * Code: "joinrequest Pittt 1 0 0 0"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_JOINREQUEST = "joinrequest";

    /**
     * Used for accepting the client request to join the server  and notify the client of the features that the server supports
     *
     * Arguments:
     * - name = Name of the client
     * - chat = Whether the client supports chat or not
     * - challenge = Whether the client supports challenge or not
     * - leaderboard = Whether the client supports leaderboard or not
     * - security = Whether the client supports security functionalities or not
     *
     * Requirements for arguments:
     * - chat = 0 || 1
     * - challenge = 0 || 1
     * - leaderboard = 0 || 1
     * - security = 0 || 1
     *
     * Example:
     *  Following up on the before mentioned joinrequest, the server only support the challenge feature and the leaderboard
     *
     *  code: "acceptjoin Pittt 0 1 1 0
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_ACCEPTJOIN = "acceptjoin";
    //Change to acceptjoin with the 4 booleans

    /**
     * Used to deny the client if the name is already in use
     *
     * Argument:
     * name : Name of the client
     *
     * Requirements for arguments:
     *  - name is not allowed to contain spaces
     *
     * Example:
     *  If there is already a client named 'pittt'
     *
     *  code: "denyjoin pittt"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_DENYJOIN  = "denyjoin";

    /**
     * Used to let the server know that the client wants to start a game with x amount of player
     *
     * code: "gamerequest 2"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_GAMEREQUEST = "gamerequest";

    /**
     * Used to let the clients know that a game has been started
     *
     * Arguments:
     * - name1 = Name of the first player
     * - name2 = Name of the second player
     * - name3
     *
     * Example:
     *  A game is being started for the clients 'pittt' and 'teun'
     *
     *  code: "startgame pittt teun"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_STARTGAME = "startgame";

    // --------------- In game ---------------

    /**
     * Used by the server to let the client know that he has to do a move
     *
     * Arguments:
     * - name = name of the player whose turn it is
     *
     * Example:
     *  It is my turn to do a move
     *
     *  code: "moverequest pittt"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_MOVEREQUEST = "moverequest";


    /**
     * Used by the client to do a move
     *
     * Arguments:
     * - x = x Coordinate on the board
     * - y = y Coordimate onm the board
     * - size = size of the ring on the board
     *
     *  Example:
     *  I want to place a move on the position x = 1 && y = 1 && ring_size = 2 && color = RED
     *
     *  code: "setmove 1 1 2 1"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_SETMOVE = "setmove";


    /**
     * Used to deny a move if the move is not valid.
     *
     * code: "denymove pittt "
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_DENYMOVE = "denymove";


    /**
     * Used to let the clients know that a move has been made
     *
     * Arguments:
     * - name = Name of the client that performed the move
     * - x = x Coordinate on the board
     * - y = y Coordinate on the board
     * - size = size of the ring on the board
     *
     * Example:
     *  pittt placed a move on x = 1 && y = 0 && size = 1 && colour =1
     *
     *  code: "notifymove pittt 1 0 1 1"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_NOTIFYMOVE = "notifymove";


    /**
     * Used to announce that the game is over
     *
     * Arguments:
     * - name = Name of the client that won the game.
     *
     * Example:
     * teun wins the game
     *
     * code: "gameover teun"
     *
     *  Draw between teun and pittt
     *
     * code: "gameover teun pittt"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_GAMEOVER = "gameover";

    // --------------- Error ---------------

    /**
     * Used when one client disconnects from the server
     *
     * Arguments:
     * - name = name of the player who disconnected
     *
     * Example: teun rage quites because he is losing
     *
     * code: "connectionlost teun"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_CONNECTIONLOST = "connectionlost";

    /**
     * Used everytime a client tries to perform an invalid command
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_INVALIDCOMMAND = "invalidcommand";

    // --------------- Extra features ---------------

    /**
     * Used to send chat messages to the server
     *
     * Argument:
     * - message = The message that you want to send to the server
     *
     * Example:
     *
     *  code: chat pittt "this is a test message"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_CHATE = "chat";


    /**
     * Used to broadcast the message to all the clients that support the chat feature
     *
     * Argument:
     * - message = The message that you want to send to the server
     *
     * Example:
     *
     *  code: sendChat pittt "this is a test message"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_SENDCHAT = "sendChat";


    /**
     * Used to request a list of all the clients that support the challenge feature
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_REQUESTCHALLENGELIST = "requestchallengelist";

    /**
     * Used to send the clients that support the challenge feature
     *
     * Direction: Server -> Client
     *
     * Example:
     *  Pittt request the challenge list. Teun, steen and arthur support the feature
     *
     *  code: challengelist Teun steen arthur
     */
    public static final String SERVER_CHALLENGELIST = "challengelist";

    /**
     * Used to send a challenge to the other client
     *
     * Arguments:
     * - name = Name of the client that you want to challenge
     *
     * Example:
     *  I (Pittt) want to challenge teun to a game
     *
     *  code: "requestchallenge teun"
     *
     *  Direction: Client -> Server
     */
    public static final String CLIENT_REQUESTCHALLENGE = "requestchallenge";

    /**
     * Used to notify the client that someone else is challenging him
     *
     * Arguments:
     * - name = Name of the client that is challenging you
     *
     * Example:
     *  teun challenges me to a game
     *
     *  code: "notifychallenge teun"
     *
     * Direction: Server -> Client
     *
     */
    public static final String SERVER_NOTIFYCHALLENGE = "notifychallenge";

    /**
     * Used by the client that got challenged to answer the challenge
     *
     * Arguments:
     * - answer = The answer to the challenge
     * - name = Name of the client that challenged
     *
     * Requirements for arguments:
     * - answer = 0 || 1
     *
     * Example:
     * I am responding to the challenge that teun send me, and I accept it
     *
     * code: "answerchallenge victor 1"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_CHALLENGERESPONSE = "challengeresponse";

    /**
     * Used to notify the other client whether the challenge has been accepted or not
     *
     * Arguments:
     * - answer = The answer to the challenge
     * - name = Name of the client that answered the challenge
     *
     * Requirements for arguments:
     * - answer = 0 || 1
     *
     * Example:
     * My previous response that is being broadcasted to the challenger (teun)
     *
     * code: "resultchallenge Pittt 1"
     *
     * Direction: Server -> Client
     */
    public static final String SERVER_RESULTCHALLENGE = "resultchallenge";

    /**
     * Used by the client to request the leaderboard from the server
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_REQUESTLEADERBOARD = "requestleaderboard";

    /**
     * Used to send the data of the game to the leaderboard. Can be ignored if the server saves the data to the leaderboard.
     *
     * Arguments:
     * - name  = Name of the player
     * - score = The score of the game
     * - time = date and time when the game was played
     *
     * Requirements for arguments:
     * - score = 0 || 1 || 3
     *      0 = loss
     *      1 = draw
     *      3 = win
     *      (also  the client that disconnects, loses the game)
     * - time = UNIX Timestamp
     *
     * Example:
     * I won my last game
     *
     * code: "setleaderboard Pttt 3 1484670803"
     *
     * Direction: Client -> Server
     */
    public static final String CLIENT_SETLEADERBOARD = "setleaderboard";

    /**
     * Used to broadcast the leaderboard to the clients
     *
     * Arguments:
     * - name  = Name of the player
     * - score = The score of the game
     * - time = date and time when the game was played
     *
     * Note:
     * This is done as often as needed. Untill all the entries in the leaderboard are returned
     *
     * Example:
     * There are 2 entries in the leaderbaord. 1 draw and 1 game that I lost
     *
     * code: "broadcastleaderboard Pittt 1 1484670803 Pittt 0 1484671115"
     *
     * Direction: Server -> Client
     *
     */
    public static final String SERVER_BROADCASTLEADERBOARD = "broadcastleaderboard";

    public static final String SERVER_PUBLICKEY = "publickey";

    public static final String CLIENT_ENCRYPTPASS = "encryptpass";

}