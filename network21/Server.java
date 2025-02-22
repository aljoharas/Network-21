

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

    static List<ClientHandler> clients = new ArrayList<>();
    static List<ClientHandler> waitingRoom = new ArrayList<>();

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
        StringBuilder playerList = new StringBuilder("Waiting Players: \n");

        for (ClientHandler client: waitingRoom){
            playerList.append(client.getPlayerName()).append("\n");
        }

        for (ClientHandler client : waitingRoom){
            client.sendMessage(playerList.toString());
        }
    }


}



    

