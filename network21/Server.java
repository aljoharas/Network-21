//package Network21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server{

    static List<ClientHandler> clients = new ArrayList<>();
    static List<ClientHandler> waitingRoom = new ArrayList<>();
    private BufferedReader in;
    private PrintWriter out;
    private static Timer roomTimer= null;
    private static boolean gameStarted = false;


    public static void main (String [] args) throws IOException{

        ServerSocket serverSocket = new ServerSocket(2121);
        System.out.println("Server started...");

        try{
            while(true){

                Socket socket = serverSocket.accept();

                System.out.println("A new client has connected.");

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        }catch(IOException e){

        }

    }

    public static void broadcastClients(){
        StringBuilder playerList = new StringBuilder("Connected Players: \n");

        for (ClientHandler client: clients){
            playerList.append(client.getPlayerName()).append("\n");
        }

        for (ClientHandler client : clients){
            client.sendMessage(playerList.toString());
        }
    }
    
    public static void broadcastWaitingRoom(){
        StringBuilder playerList = new StringBuilder("Waiting Players: "); //\n

        for (ClientHandler client: waitingRoom){
            playerList.append(client.getPlayerName()).append(" "); //\n
        }

        System.out.println("Broadcasting waiting room message: " + playerList.toString());
        System.out.println("Number of players in waiting room: " + waitingRoom.size());

        for (ClientHandler client : waitingRoom){
            client.sendMessage(playerList.toString());
        }
    }

    public static String join(ClientHandler client) {
        if (Server.waitingRoom.size() >= 4) {
            return "Cannot join game at this time. Try again in a few minutes";
        }
        Server.waitingRoom.add(client);
        System.out.println(client.getPlayerName() + " joined the waiting room.");
        Server.broadcastWaitingRoom();
        if(Server.waitingRoom.size() >= 2 && !gameStarted){
            if(Server.waitingRoom.size() == 4) {
            
                startGame();
            }
            else {
                if (roomTimer == null) {
                roomTimer = new Timer();
                roomTimer.scheduleAtFixedRate(new TimerTask() {
                    private int countdown = 30; // Countdown from 30 seconds

                    @Override
                    public void run() {
                        // Send the countdown to all clients in the waiting room
                        if (countdown > 0) {
                            broadcastTimer(countdown);
                            countdown--;
                        } else {
                            broadcastTimer(countdown);
                            startGame();
                            roomTimer.cancel();
                        }
                    }
                }, 0, 1000);
            }
        }
    }
        return client.getPlayerName() + " joined.";
    }

    public static synchronized void startGame(){
        if(gameStarted)  { 
        System.out.println("already started"); }
        else {
        gameStarted=true;
        System.out.println("started"); }
    }

    public static void broadcastTimer(int countdown) {
        String t;
        if(countdown<=9){
            t = "0"+countdown;
        } else {
            t = countdown + "";
        }
        String timeLeft = "00:" + t;
        for (ClientHandler client : waitingRoom){
            client.sendMessage(timeLeft);
        }
    }

}