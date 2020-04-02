package src.view.client;

import src.controller.Client;
import src.protocol.GameProtocol;
import src.view.client.ClientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;


public class GUIViewClient implements ClientView {

    private JPanel Clientmain;
    public JTextField textFieldIP;
    public JTextField textFieldPort;
    public JTextField textFieldNickname;
    public JButton submitInformation;
    private JTextPane servermessagestextPane1;
    private JPanel lablesWest;
    private JPanel textFieldsEast;
    private JPanel ServerMessagesPane;
    private JLabel servermessageslabel;
    private JButton against1PlayerButton;
    private JButton against2PlayersButton;
    private JButton against3PlayersButton;
    private JPanel gamerequestbuttonpanel;
    private JPanel serverStatusPanel;
    private JLabel serverstatuslable;
    private JLabel serverstatusicon;
    private JList lobbyList;
    private JLabel lobbylistlabel;
    private JPanel lobbypanel;
    private JButton StartGameButton;
    private JCheckBox AIPlayerCheckBox;
    private JSpinner spinner1;
    private ImageIcon on = new ImageIcon("src/resource/serverstatuson.png");
    private ImageIcon off = new ImageIcon("src/resource/serverstatusoff.png");
    private String str;
    private Client client;
    private int playerCount = 0;


    public GUIViewClient(Client client) {

        this.client = client;
        JFrame frame = new JFrame("Client");
        frame.setContentPane(Clientmain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        serverstatusicon.setIcon(off);
        spinner1.setModel(new SpinnerNumberModel(1,1,4,1));
        frame.pack();
        frame.setVisible(true);
        run();


    }

    @Override
    public void printMessage(String message) {
        str = str + "\n" + message;
        servermessagestextPane1.setText(str);
    }

    private void run() {

        submitInformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.setIp(textFieldIP.getText());
                client.setPort((Integer.parseInt(textFieldPort.getText())));
                if(AIPlayerCheckBox.isSelected()){
                    client.setNickname( "-AI" + textFieldNickname.getText().replaceAll("\\s+", ""));
                    client.setAiDiff((Integer) spinner1.getValue());
                } else {
                    client.setNickname(textFieldNickname.getText().replaceAll("\\s+", ""));
                }
                System.out.println("Button pressed");

            }
        });

        against1PlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                playerCount = 1;
                //StartGameButton.setEnabled(true);
                System.out.println(GameProtocol.CLIENT_GAMEREQUEST + " " + 2);
                client.sendMessage(GameProtocol.CLIENT_GAMEREQUEST + " " + 2);

            }
        });
        against2PlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                playerCount = 2;
               // StartGameButton.setEnabled(true);
                System.out.println(GameProtocol.CLIENT_GAMEREQUEST + " " + 3);
                client.sendMessage(GameProtocol.CLIENT_GAMEREQUEST + " " + 3);

            }
        });
        against3PlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                playerCount = 3;
                //StartGameButton.setEnabled(true);
                System.out.println(GameProtocol.CLIENT_GAMEREQUEST + " " + 4);
                client.sendMessage(GameProtocol.CLIENT_GAMEREQUEST + " " + 4);


            }
        });

        StartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String str = "";
                if (lobbyList.getSelectedValuesList().size() == playerCount && !lobbyList.getSelectedValuesList().toString().contains(client.getNickname())) {
                    client.setplayerList(lobbyList.getSelectedValuesList());
                    for (Object u : lobbyList.getSelectedValuesList()) {
                        str = str + " " + u.toString();
                    }
                } else if (client.getNickname().equals(lobbyList.getSelectedValue().toString())) {
                    JFrame framebox = new JFrame();
                    JOptionPane.showMessageDialog(framebox, "Please select not yourself as an enemy", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    JFrame framebox = new JFrame();
                    JOptionPane.showMessageDialog(framebox, "Please select  enemy and not yourself", "Error", JOptionPane.WARNING_MESSAGE);
                }


            }
        });
    }


    @Override
    public void update(Observable arg0, Object arg1) {

    }

    public void setServerStatus(boolean b) {
        boolean serverstatus = b;
        serverStatus(serverstatus);
    }

    private void serverStatus(boolean b) {

        if (b) {
            serverstatuslable.setText("Server online");
            serverstatusicon.setIcon(on);
        } else {
            serverstatuslable.setText("Server offline");
            serverstatusicon.setIcon(off);
        }
    }

}
