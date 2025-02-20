package network21;

import java.io.*;
import java.net.*;
import java.util.*;



public class Server {
    static Deck deck= new Deck();
    static List<ClientHandler> Clients=new ArrayList<>();
    static  List<ClientHandler> WaitingRoom=new ArrayList<>();
    private boolean gameStarted=false;
    static final int maxP=4;
    private PrintWriter out;
    private BufferedReader in;


 public static void main(String[] args) throws IOException {
    try(ServerSocket serversocket = new ServerSocket(2121)) {
    System.out.println("Game Server Started!");

    while(true){
        try{
            Socket clientSocket=serversocket.accept();
            ClientHandler clienthandler = new ClientHandler(clientSocket, Clients, WaitingRoom);
            new Thread(clienthandler).start();


          
        } catch (IOException e) {
            System.out.println("Error accepting client connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
} catch (IOException e) {
    System.out.println("Error starting server: " + e.getMessage());
    e.printStackTrace();
}}

       public static synchronized void addToClients(ClientHandler client) {
        if (!Clients.contains(client)) {
            Clients.add(client);
        }//cc
       }
    
       public static synchronized void addToWaitingRoom(ClientHandler client){
            if(!WaitingRoom.contains(client)) {
                WaitingRoom.add(client);
                broadcastWaitingRoomList();
            
            
        }
    }
    public static synchronized void broadcastWaitingRoomList(){
        for (ClientHandler client : WaitingRoom) {
            client.sendMessage("Current waiting room list: " + WaitingRoom.size() + " players.");
        }
        
    }//cc
    public void sendMessage(String message){
        out.println(message);
    }
    public static synchronized void broadcastMessage(String message){
    
        for(ClientHandler client : Clients){
            client.sendMessage(message);
        }
    }


public void run() {
    try {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        out.flush();

        

        // Add an if statement for invalid player namesp

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}}
 



    

