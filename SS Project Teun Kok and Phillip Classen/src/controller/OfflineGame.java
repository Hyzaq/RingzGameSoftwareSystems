package src.controller;

import src.model.*;
import src.view.game.TUIViewGame;

import java.io.IOException;

/**
 * The type Offline game.
 */
public class OfflineGame {
    private Board board;
    private TUIViewGame view;
    private int playCnt;
    private Game game;
    private OfflineGameController gameC;
    private Boolean gameRunning = false;
    private int currentP = 1;
    private String p1N;
    private String p2N;
    private String p3N;
    private String p4N;
    private Player ply1;
    private Player ply2;
    private Player ply3;
    private Player ply4;
    private Player currentPlayer;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        OfflineGameController gameC = new OfflineGameController();
        gameC.run();
    }

    /**
     * Creates an offline game and starts it
     * @param playCnt the player count
     * @param view the view for the game
     * @param gameC the offline game controller
     */
    OfflineGame(int playCnt, TUIViewGame view, OfflineGameController gameC) {
        this.view = view;
        this.playCnt = playCnt;
        this.gameC = gameC;
        this.game = new Game(playCnt);
        createOfflinePlayers();
        start();

    }


    /**
     * Starts the offline game
     */
    void start() {
        gameRunning = true;
        while (gameRunning) {
            play();
            gameRunning = false;
        }
        if (view.readBoolean("Play another game? (Y/N")) {
            start();
        } else {
            System.exit(1);
        }
    }

    /**
     * Creates the local offline players for the game
     */
    private void createOfflinePlayers() {
        if (playCnt == 2) {
            p1N = gameC.setPlayerName(1);
            p2N = gameC.setPlayerName(2);
            game.playerCreate(p1N, p2N, null, null);
        }
        if (playCnt == 3) {
            p1N = gameC.setPlayerName(1);
            p2N = gameC.setPlayerName(2);
            p3N = gameC.setPlayerName(3);
            game.playerCreate(p1N, p2N, p3N, null);
        }
        if (playCnt == 4) {
            p1N = gameC.setPlayerName(1);
            p2N = gameC.setPlayerName(2);
            p3N = gameC.setPlayerName(3);
            p4N = gameC.setPlayerName(4);
            game.playerCreate(p1N, p2N, p3N, p4N);
        }
    }

    /**
     * Play function for the offline game
     * Gets vales from the input and checks the values.
     * Places the moves and change the current player so the next player can make his move
     */
    private void play() {
        boolean test = false;
        if (playCnt >= 2) {
            ply1 = game.getPlayerByName(p1N);
            ply2 = game.getPlayerByName(p2N);
        }
        if (playCnt >= 3) {
            ply3 = game.getPlayerByName(p3N);
        }
        if (playCnt == 4) {
            ply4 = game.getPlayerByName(p4N);
        }

        //While the board does not have a winner, continue playing the game
        while (!this.game.getBoard().hasWinner(ply1, ply2, ply3, ply4)) {
            int x = 0;
            int y = 0;
            int type = 0;
            Colour colour = null;
            this.board = game.getBoard();

            if (game.getBoard().isEmpty()) {
                if (game.getCurrentPlayer().getName().contains("-AI")) {
                    String s = game.getCurrentPlayer().getMoveStart();
                    String[] move = s.split("\\s+");
                    x = Integer.parseInt(move[0]);
                    y = Integer.parseInt(move[1]);
                } else {
                    view.printMessage(game.getCurrentPlayer().getName() + ": please place the special base");
                    x = view.getXValue();
                    y = view.getYValue();
                }
                if (board.validateMove(x, y, 0, Colour.SPECIAL)) {
                    board.setRingXYZ(x, y, 0, Colour.SPECIAL);
                    game.changeCurrentPlayer();
                    view.printBoard(board);
                    view.printInv(game.getCurrentPlayer().getInv());
                } else {
                    view.printMessage("Invalid position, X [2-4] and Y[2-4]");
                }
            } else {
                if (board.checkMovePossible(game.getCurrentPlayer())) {
                    view.setPrimaryColour(game.getCurrentPlayer().getColourList().get(0));
                    view.setSecondaryColour(game.getCurrentPlayer().getColourList().get(1));
                    if (game.getCurrentPlayer().getName().contains("-AI")) {
                        view.printMessage("It is the turn of: " + game.getCurrentPlayer().getName());
                        String s = game.getCurrentPlayer().getMove(1);
                        String[] move = s.split("\\s+");
                        x = Integer.parseInt(move[0]);
                        y = Integer.parseInt(move[1]);
                        type = Integer.parseInt(move[2]);
                        if (Integer.parseInt(move[3]) == 1) {
                            colour = game.getCurrentPlayer().getColourList().get(0);
                        } else {
                            colour = game.getCurrentPlayer().getColourList().get(1);
                        }
                    } else {
                        view.printMessage("Player " + game.getCurrentPlayer().getName() + " may place a ring!");
                        if (!board.checkMovePossible(game.getCurrentPlayer())) {
                            view.printMessage("You can't place a move! Next player;");
                            game.changeCurrentPlayer();
                        } else {
                            view.printMessage("Pssst, " + game.hint() + " is a possible move ;)");
                            view.printInv(game.getCurrentPlayer().getInv());
                            x = view.getXValue();
                            y = view.getYValue();
                            type = view.getZValue();
                            colour = view.getColour();
                        }
                    }
                    Ring ring = new Ring(colour, type);
                    if (board.validateMove(x, y, type, colour)) {
                        board.setRingXYZ(x, y, type, colour);
                        for (int i = 0; i < game.getCurrentPlayer().getInv().size(); i++) {
                            if (game.getCurrentPlayer().getInv().get(i).getInvColour().equals(ring.getColour())) {
                                game.getCurrentPlayer().getInv().get(i).removeRing(ring);
                            }
                        }
                        game.changeCurrentPlayer();
                        view.printBoard(board);
                        view.printInv(game.getCurrentPlayer().getInv());
                    } else {
                        view.printMessage("Invalid move! Please place a valid one.");
                    }
                } else {
                    game.changeCurrentPlayer();
                }
            }
        }


//If the board has a winner stop the game
        gameRunning = false;
        System.out.println("We have a winner!!");
        System.out.println("Good job " + game.getWinner(board));
    }
}
