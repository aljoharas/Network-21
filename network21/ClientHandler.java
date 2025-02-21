package network21;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    //public static List<ClientHandler> clients = new ArrayList<>();
    //public static List<ClientHandler> waitingRoom = new ArrayList<>();

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    
    @Override 
    public void run(){
        try{

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

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
                if (message.equalsIgnoreCase("connect")){
                    Server.waitingRoom.add(this);
                    System.out.println(playerName + " entered waiting room.");
                    Server.broadcastWaitingRoom();
                }
            }



        } catch (IOException e) {
                    e.printStackTrace();
        }finally{
            
        }

    }

    public void sendMessage(String message){
        out.println(message + "\n");
        out.flush();
    }

    public String getPlayerName(){
        return playerName;
    }

    public void removeClient(){
        Server.clients.remove(this);
        Server.broadcastClients();
    }
}