package src.view.game;

import src.controller.Client;
import src.model.Colour;
import src.model.Game;
import src.model.Inventory;
import src.model.Ring;
import src.protocol.GameProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GUIViewGame implements Observer, GameView {
    private JPanel Ring14;
    private JPanel Ring25;
    private JPanel Ring24;
    private JPanel Ring23;
    private JPanel Ring22;
    private JPanel Ring21;
    private JPanel Ring31;
    private JPanel Ring32;
    private JPanel Ring33;
    private JPanel Ring34;
    private JPanel Ring35;
    private JPanel Ring45;
    private JPanel Ring44;
    private JPanel Ring43;
    private JPanel Ring42;
    private JPanel Ring41;
    private JPanel Ring51;
    private JPanel Ring52;
    private JPanel Ring53;
    private JPanel Ring54;
    private JPanel Ring55;
    private JPanel Ring11;
    private JPanel Ring111;
    private JPanel Ring112;
    private JPanel Ring113;
    private JPanel Ring114;
    private JPanel Ring110;
    private JPanel Ring12;
    private JPanel Ring124;
    private JPanel Ring123;
    private JPanel Ring122;
    private JPanel Ring121;
    private JPanel Ring120;
    private JPanel Ring13;
    private JPanel Ring130;
    private JPanel Ring131;
    private JPanel Ring132;
    private JPanel Ring133;
    private JPanel Ring134;
    private JPanel Ring140;
    private JPanel Ring141;
    private JPanel Ring142;
    private JPanel Ring143;
    private JPanel Ring144;
    private JPanel Ring15;
    private JPanel Ring150;
    private JPanel Ring151;
    private JPanel Ring152;
    private JPanel Ring153;
    private JPanel Ring154;
    private JPanel Ring210;
    private JPanel Ring211;
    private JPanel Ring212;
    private JPanel Ring213;
    private JPanel Ring214;
    private JPanel Ring220;
    private JPanel Ring221;
    private JPanel Ring222;
    private JPanel Ring223;
    private JPanel Ring224;
    private JPanel Ring230;
    private JPanel Ring231;
    private JPanel Ring232;
    private JPanel Ring233;
    private JPanel Ring234;
    private JPanel Ring240;
    private JPanel Ring241;
    private JPanel Ring242;
    private JPanel Ring243;
    private JPanel Ring244;
    private JPanel Ring250;
    private JPanel Ring251;
    private JPanel Ring252;
    private JPanel Ring253;
    private JPanel Ring254;
    private JPanel Ring310;
    private JPanel Ring311;
    private JPanel Ring312;
    private JPanel Ring313;
    private JPanel Ring314;
    private JPanel Ring320;
    private JPanel Ring321;
    private JPanel Ring322;
    private JPanel Ring323;
    private JPanel Ring324;
    private JPanel Ring330;
    private JPanel Ring331;
    private JPanel Ring332;
    private JPanel Ring333;
    private JPanel Ring334;
    private JPanel Ring340;
    private JPanel Ring341;
    private JPanel Ring342;
    private JPanel Ring343;
    private JPanel Ring344;
    private JPanel Ring350;
    private JPanel Ring351;
    private JPanel Ring352;
    private JPanel Ring353;
    private JPanel Ring354;
    private JPanel Ring410;
    private JPanel Ring411;
    private JPanel Ring412;
    private JPanel Ring413;
    private JPanel Ring414;
    private JPanel Ring420;
    private JPanel Ring421;
    private JPanel Ring422;
    private JPanel Ring423;
    private JPanel Ring424;
    private JPanel Ring430;
    private JPanel Ring431;
    private JPanel Ring432;
    private JPanel Ring433;
    private JPanel Ring434;
    private JPanel Ring440;
    private JPanel Ring441;
    private JPanel Ring442;
    private JPanel Ring443;
    private JPanel Ring444;
    private JPanel Ring450;
    private JPanel Ring451;
    private JPanel Ring452;
    private JPanel Ring453;
    private JPanel Ring454;
    private JPanel Ring510;
    private JPanel Ring511;
    private JPanel Ring512;
    private JPanel Ring513;
    private JPanel Ring514;
    private JPanel Ring520;
    private JPanel Ring521;
    private JPanel Ring522;
    private JPanel Ring523;
    private JPanel Ring524;
    private JPanel Ring530;
    private JPanel Ring531;
    private JPanel Ring532;
    private JPanel Ring533;
    private JPanel Ring534;
    private JPanel Ring540;
    private JPanel Ring541;
    private JPanel Ring542;
    private JPanel Ring543;
    private JPanel Ring544;
    private JPanel Ring550;
    private JPanel Ring551;
    private JPanel Ring552;
    private JPanel Ring553;
    private JPanel Ring554;

    private JPanel controlPanel;
    private JTextArea gameCommunicationPanel;
    private JTabbedPane gameCommunicationTab;
    private JTextArea chatPannel;
    private JLabel gameCommunicationLable;
    private JButton submitButton;
    private JTextField textFieldXValue;
    private JTextField textFieldYValue;
    private JRadioButton colour1RadioButton;
    private JRadioButton colour2RadioButton;
    private JPanel gameField;
    private JTextField textFieldSizeValue;
    private JButton hintButton;
    private JLabel hintlable;
    private JTabbedPane inventoryTabbedpanel;
    private JFrame frame;
    private static GUIViewGame guiViewGame;
    private int x = -1;
    private int y = -1;
    private int z = -1;
    public int colour;
    private ButtonGroup group;
    private Game game;
    private int playerCnt;
    private String str;
    private Colour primaryColour;
    private Colour secondaryColour;
    private boolean set = false;
    private Client client;
    private JTextArea panel1;
    private JTextArea panel2;
    private String message;
    private JTextField chatBox;
    private JButton sendButton;


    public GUIViewGame(Game game) {
        this.game = game;
    }

    public GUIViewGame(Game game, Client client) {
        this.game = game;
        this.client = client;
    }


    public void showBoard() {

        playerCnt = this.game.getPlayCnt();

        this.frame = new JFrame("Game");
        this.frame.setContentPane(gameField);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
        group = new ButtonGroup();
        group.add(colour1RadioButton);
        group.add(colour2RadioButton);

        panel1 = new JTextArea();
        panel2 = new JTextArea();
        gameCommunicationPanel = new JTextArea();
        chatPannel = new JTextArea();

        if (this.game.getPlayCnt() == 4) {
            colour2RadioButton.setEnabled(false);
        }


        run();

    }

    public void setColour(Colour colour) {
        if (colour.equals(primaryColour)) {
            this.colour = 1;
        } else if (colour.equals(secondaryColour)) {
            this.colour = 2;
        }
    }

    private void run() {

        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (group.getSelection().isSelected()) {
                    x = Integer.parseInt(textFieldXValue.getText());
                    y = Integer.parseInt(textFieldYValue.getText());
                    z = Integer.parseInt(textFieldSizeValue.getText());
                    if (colour1RadioButton.isSelected()) {
                        setColour(primaryColour);
                    } else if (colour2RadioButton.isSelected()) {
                        setColour(secondaryColour);
                    }
                    set = true;
                }
            }

        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // printChat(GameProtocol.CLIENT_CHATE + " " +client.getNickname() + ": " + getChat());
                client.sendMessage(GameProtocol.CLIENT_CHATE + " " + client.getNickname() + ": " + getChat());
            }
        });

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hintlable.setText(game.hint());
            }
        });

    }

    private Object getFieldRing(String name) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (name.equals(field.getName())) {
                Object obj = null;
                try {
                    obj = field.get(client.getView());
                    return obj;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //no match
        return null;
    }

    public void colourRing(String name, Colour colour) {

        JPanel panel = (JPanel) getFieldRing(name);

        assert panel != null;

        if (colour == Colour.BLUE) {
            panel.setBackground(Color.BLUE);
        } else if (colour == Colour.RED) {
            panel.setBackground(Color.RED);
        } else if (colour == Colour.YELLOW) {
            panel.setBackground(Color.YELLOW);
        } else if (colour == Colour.GREEN) {
            panel.setBackground(Color.GREEN);
        } else if (colour == Colour.SPECIAL) {
            panel.setBackground(Color.CYAN);
        } else if (colour == Colour.EMPTY) {
            panel.setBackground(Color.WHITE);
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                for (int k = 0; k < 5; k++) {
                    this.colourRing(("Ring" + (i) + (j) + (k)), this.game.getBoard().getBoardRingXYZ(i, j, k).getColour());
                }
            }
        }
        updateInv();
        this.frame.revalidate();
        this.frame.repaint();

    }

    public void setGameCommunicationTab() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                gameCommunicationTab.insertTab("Game communication", null, gameCommunicationPanel, null, 0);
                gameCommunicationTab.insertTab("Chat", null, chatPannel, null, 1);
            }
        });
    }

    @Override
    public void printMessage(String message) {

        gameCommunicationPanel.setText("\n" + message);
    }

    public void printChat(String message) {

        String x = "";
        String[] chat = message.split(" ");
        for (int i = 1; i < chat.length; i++) {
            x = x + chat[i];
            chatPannel.setText(x);
        }
    }

    public String getChat() {
        return chatBox.getText();
    }

    @Override
    public int getXValue() {
        int i = 0;
        i = this.x;
        this.x = -1;
        set = false;
        return i;
    }

    @Override
    public int getYValue() {
        int i = 0;
        i = this.y;
        this.y = -1;
        set = false;
        return i;
    }

    @Override
    public int getZValue() {
        int i = 0;
        i = this.z;
        this.z = -1;
        set = false;
        return i;
    }

    public Colour getColour() {
        if (this.colour == 1) {
            return primaryColour;
        } else if (this.colour == 2) {
            return secondaryColour;
        }
        return null;
    }

    public void setColour1RadioButtonColour(String str) {
        this.colour1RadioButton.setText(str);
        frame.repaint();
    }

    public void setColour2RadioButtonColour(String str) {
        this.colour2RadioButton.setText(str);
        frame.repaint();
    }

    @Override
    public void setPrimaryColour(Colour primaryColour) {
        this.primaryColour = primaryColour;
        setColour1RadioButtonColour(primaryColour.toString());
    }

    @Override
    public boolean getValuesSet() {
        return set;
    }

    @Override
    public void setSecondaryColour(Colour secondaryColour) {
        this.secondaryColour = secondaryColour;
        setColour2RadioButtonColour(secondaryColour.toString());

    }

    @Override
    public void setHintEnabled(boolean bool) {
        hintButton.setEnabled(bool);
    }

    public void setInv() throws InvocationTargetException, InterruptedException {

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                updateInv();
                inventoryTabbedpanel.insertTab(primaryColour.toString(), null, panel1, null, 0);
                inventoryTabbedpanel.insertTab(secondaryColour.toString(), null, panel2, null, 1);
            }
        });
    }

    public void updateInv() {
        this.panel1.setText(printInv(this.game.getPlayerByName(this.client.getNickname()).getInv(), 0));
        if (this.game.getPlayerByName(this.client.getNickname()).getInv().size() > 1) {
            this.panel2.setText(printInv(this.game.getPlayerByName(this.client.getNickname()).getInv(), 1));
        }
    }


    public String printInv(ArrayList<Inventory> invL, int nr) {
        String s = "";

        Inventory inv = invL.get(nr);
        Ring[] rings = inv.getArray();
        s = "";
        for (int i = 0; i < inv.getArray().length; i++) {
            if (!rings[i].getColour().equals(Colour.EMPTY)) {
                try {
                    s = s + "[" + rings[i].toStringRing() + "]," + "\n";
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

}



