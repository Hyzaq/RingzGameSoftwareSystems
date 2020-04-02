package src.controller;

import src.view.game.TUIViewGame;
import java.io.IOException;
import java.util.Scanner;

/**
 * The type Offline game controller.
 */
public class OfflineGameController {

    private OfflineGame game;
    private Scanner in;
    private TUIViewGame view;
    private int playCnt = 0;

    /**
     * Run. Creates view, and a game.
     * Starts game.
     *
     * @throws IOException the io exception
     */
    public void run() throws IOException {
        view = new TUIViewGame();
        in = new Scanner(System.in);
        playCnt = getPlayers();
        game = new OfflineGame(playCnt, view, this);
        game.start();
    }

    /**
     * Gets player count.
     *
     * @return the players
     */
    public int getPlayers() {
        int x = 0;
        view.printMessage("Please enter the number of players [2-4]: ");
        try {
            x = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            view.printMessage("You did not enter a valid integer, please enter 2, 3 or 4.");
            getPlayers();
        }
        if (x < 2 || x > 4) {
            view.printMessage("You did not enter a valid integer, please enter 2, 3 or 4.");
            this.getPlayers();
        }
        return x;
    }

    /**
     * Sets player names.
     *
     * @param p the p
     * @return the player name
     */
    public String setPlayerName(int p) {
        String name = "";
        do {
            view.printMessage("Type the name for Player " + p + ":");
            view.printMessage("For AI player add -AI (example: -AIteun");
            name = in.nextLine();
        } while (name.equals(""));
        return name;
    }
}


//    public determineMove() {
//        int x = 0;
//        int y = 0;
//        int type = 0;
//        int[] command = new int[3];
//        System.out.println("Please give the position you'd like to place a ring:");
//        System.out.println("[" + currentPlayer.getName() + "] x: ");
//        try {
//            x = Integer.parseInt(in.nextLine());
//        } catch (NumberFormatException e) {
//            System.out.println("You did not enter a valid integer, please enter an integer between 1 and 5.");
//            determineMove(b);
//        }
//        command[0] = x - 1;
//
//        System.out.println("[" + currentPlayer.getName() + "] y: ");
//        try {
//            y = Integer.parseInt(in.nextLine());
//        } catch (NumberFormatException e) {
//            System.out.println("You did not enter a valid integer, please enter an integer between 1 and 5.");
//            determineMove(b);
//        }
//        command[1] = y - 1;
//
//        System.out.println("Which ring would you like to place? (example R 0)");
//        view.printInv(currentPlayer.getInv());
//
//        System.out.println("[" + currentPlayer.getName() + "] type: ");
//        String s = in.nextLine();
//        String[] splitS = s.split("\\s+");
//        String colour = splitS[0];
//        String s2 = splitS[1];
//        try {
//            type = Integer.parseInt(s2);
//        } catch (NumberFormatException e) {
//            System.out.println("You did not enter a valid ring, please enter an integer between 1 and 5.");
//            determineMove(b);
//        }
//        command[2] = type;
//    }



