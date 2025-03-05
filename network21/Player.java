import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Player extends javax.swing.JFrame {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;

    public Player() {
        initComponents();

        UsernamePage.setVisible(true);
        ConnectedRoom.setVisible(false);
        WaitingPlayers.setVisible(false); // Initially hide the WaitingPlayers panel

        connectButton.addActionListener(evt -> connectToServer());
        joinButton.addActionListener(evt -> joinWaitingRoom()); // Action for joinButton in ConnectedRoom

        // Add a window listener to handle the close operation
        this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    sendExitMessage();
                    System.exit(0); // Close the application
                }
            });
    }

    private void sendExitMessage() {
        if (out != null) {
            out.println("exit"); // Send exit message to the server
        }
    }

    public void connectToServer() {
        username = usernameField.getText().trim();

        if (username.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return;
        }

        try {
            socket = new Socket("localhost", 2121);
            out = new PrintWriter(socket.getOutputStream(), true); // Auto flush

            out.println(username);
            new Thread(new ServerListener(socket, this)).start();

            // Update UI
            UsernamePage.setVisible(false);
            ConnectedRoom.setVisible(true);

            connectedList.setCaretPosition(connectedList.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinWaitingRoom() {

        out.println("join"); // Send "join" message to the server

        System.out.println("Switching to WaitingPlayers panel...");
        ConnectedRoom.setVisible(false);
        WaitingPlayers.setVisible(true);
        System.out.println("WaitingPlayers panel visible: " + WaitingPlayers.isVisible());

        waitingPlayersList.setCaretPosition(waitingPlayersList.getDocument().getLength());
    }

    public void handleMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Received message: " + message);

            // Handle message for "Waiting Players:"
            if (message.startsWith("Waiting Players:")) {
                WaitingPlayers.setVisible(true); // Make sure the panel is visible now
                waitingPlayersList.setText(""); // Clear the previous list
                
                // Extract the part after "Waiting Players:\n"
                String playerList = message.substring("Waiting Players:\n".length());
                playerList = playerList.replace(" ", "\n");

                waitingPlayersList.setText(playerList); // Update the text area with the player names
                waitingPlayersList.setCaretPosition(waitingPlayersList.getDocument().getLength());
            } 
            // Handle other types of messages (e.g., connected players, etc.)
            else if (message.startsWith("Connected Players:")) {
                connectedList.setText("");
                String displayMessage = message.substring("Connected Players:\n".length());
                connectedList.append(displayMessage);
            } else {
                connectedList.append(message + "\n");
            }

            connectedList.setCaretPosition(connectedList.getDocument().getLength());
            ConnectedRoom.revalidate();
        });
    }
  

    @SuppressWarnings("unchecked")
    private void initComponents() {

        ConnectedRoom = new javax.swing.JPanel();
        connectedPlayers = new javax.swing.JScrollPane();
        connectedList = new javax.swing.JTextArea();
        UsernamePage = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();

        // New panel for WaitingPlayers
        WaitingPlayers = new javax.swing.JPanel();
        waitingPlayersLabel = new javax.swing.JLabel();
        waitingPlayersList = new javax.swing.JTextArea();
        JScrollPane waitingPlayersScroll = new javax.swing.JScrollPane(waitingPlayersList);

        // New "Join" button in ConnectedRoom panel
        joinButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        connectedList.setEditable(false);
        connectedList.setColumns(20);
        connectedList.setRows(5);
        connectedPlayers.setViewportView(connectedList);

        javax.swing.GroupLayout ConnectedRoomLayout = new javax.swing.GroupLayout(ConnectedRoom);
        ConnectedRoom.setLayout(ConnectedRoomLayout);
        ConnectedRoomLayout.setHorizontalGroup(
                ConnectedRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ConnectedRoomLayout.createSequentialGroup()
                                .addGap(243, 243, 243)
                                .addComponent(connectedPlayers, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(244, Short.MAX_VALUE))
                        .addGroup(ConnectedRoomLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(joinButton) // Center the joinButton by putting it at the center of the
                                                          // layout
                                .addGap(0, 0, Short.MAX_VALUE)));
        ConnectedRoomLayout.setVerticalGroup(
                ConnectedRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ConnectedRoomLayout.createSequentialGroup()
                                .addGap(294, 294, 294)
                                .addComponent(connectedPlayers, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(joinButton) // Place the joinButton directly under the connectedPlayers
                                                          // text area
                                .addContainerGap(295, Short.MAX_VALUE)));

        joinButton.setText("Join Game");

        UsernamePage.setToolTipText("");

        jLabel1.setText("Enter your name:");

        connectButton.setText("Connect");

        javax.swing.GroupLayout UsernamePageLayout = new javax.swing.GroupLayout(UsernamePage);
        UsernamePage.setLayout(UsernamePageLayout);
        UsernamePageLayout.setHorizontalGroup(
                UsernamePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(UsernamePageLayout.createSequentialGroup()
                                .addGap(222, 222, 222)
                                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 260,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(227, Short.MAX_VALUE))
                        .addGroup(UsernamePageLayout.createSequentialGroup()
                                .addGap(305, 305, 305)
                                .addComponent(connectButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(UsernamePageLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        UsernamePageLayout.setVerticalGroup(
                UsernamePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(UsernamePageLayout.createSequentialGroup()
                                .addGap(180, 180, 180)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(connectButton)
                                .addContainerGap(374, Short.MAX_VALUE)));

        waitingPlayersList.setEditable(false);
        waitingPlayersList.setColumns(20);
        waitingPlayersList.setRows(5);
        waitingPlayersScroll.setViewportView(waitingPlayersList);

        javax.swing.GroupLayout WaitingPlayersLayout = new javax.swing.GroupLayout(WaitingPlayers);
        WaitingPlayers.setLayout(WaitingPlayersLayout);
        WaitingPlayersLayout.setHorizontalGroup(
                WaitingPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(WaitingPlayersLayout.createSequentialGroup()
                                .addGap(243, 243, 243)
                                .addComponent(waitingPlayersScroll, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(244, Short.MAX_VALUE)));
        WaitingPlayersLayout.setVerticalGroup(
                WaitingPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(WaitingPlayersLayout.createSequentialGroup()
                                .addGap(294, 294, 294)
                                .addComponent(waitingPlayersScroll, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(295, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(UsernamePage, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ConnectedRoom, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(WaitingPlayers, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(UsernamePage, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ConnectedRoom, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(WaitingPlayers, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        pack();
    }// </editor-fold>

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Player().setVisible(true);
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel ConnectedRoom;
    private javax.swing.JPanel UsernamePage;
    private javax.swing.JPanel WaitingPlayers;
    private javax.swing.JButton connectButton;
    private javax.swing.JTextArea connectedList;
    private javax.swing.JScrollPane connectedPlayers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField usernameField;
    private javax.swing.JButton joinButton;
    private javax.swing.JLabel waitingPlayersLabel;
    private javax.swing.JTextArea waitingPlayersList;
    // End of variables declaration
}
