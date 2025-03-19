// package Network21;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ClientHandler implements Runnable{

    //public static List<ClientHandler> clients = new ArrayList<>();
    //public static List<ClientHandler> waitingRoom = new ArrayList<>();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;
    //private static Timer roomTimer= null;
   // private static boolean gameStarted = false;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    
    @Override 
    public void run(){
        try{

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            playerName = in.readLine();
            Server.clients.add(this);
            System.out.println(playerName + " connected.");

            Server.broadcastClients();

            while (true) {
                String message = in.readLine(); // Read user input for the message
                if (message.equalsIgnoreCase("exit")) { 
                    removeClient();
                    out.println("Client left.");
                }
                if (message.equalsIgnoreCase("join")){
                    String joined = Server.join(this);
                    out.println(joined);
                    //Server.broadcastWaitingRoom();
                }
            }



        } catch (IOException e) {
                    e.printStackTrace();
        }finally{
            
        }

    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    public String getPlayerName(){
        return playerName;
    }

    public void removeClient(){
        Server.clients.remove(this);
        Server.waitingRoom.remove(this);
        Server.broadcastClients();
        Server.broadcastWaitingRoom();
    }

   

}