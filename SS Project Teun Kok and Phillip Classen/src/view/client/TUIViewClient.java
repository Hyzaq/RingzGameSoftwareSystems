package src.view.client;

import src.controller.Client;
import src.view.client.ClientView;

import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Scanner;

public class TUIViewClient implements ClientView {

    private static Scanner input = new Scanner(System.in);
    private Client client;


    public void TUIViewGame(Client str){
        this.client = str;
    }
    public void printMessage(String message) {
        System.out.println(message);
    }

    //@Override
    public void getIp() {
        try {
            System.out.println("Type in your IP address: ");
            client.setIp(input.next());
        } catch (InputMismatchException e) {
            System.out.println("Ip invalid, try again!");
            getIp();
        }
    }

    //@Override
    public void getNickname() {
        System.out.println("Type in your nickname: ");
        client.setNickname(input.next().replaceAll("\\s+", ""));
    }

    //@Override
    public void getPort() {
        System.out.println("Type in your nickname: ");
        client.setPort(input.nextInt());
    }

    @Override
    public void update(Observable o, Object arg) {

    }


}
