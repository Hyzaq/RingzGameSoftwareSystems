package src.view.client;

import java.util.Observer;

public interface ClientView extends Observer {

    void printMessage(String message);
}
