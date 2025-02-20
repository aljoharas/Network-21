package network21;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;

    private List<ClientHandler> Clients;
    private static List<ClientHandler> waitingRoom;

    public ClientHandler(Socket socket, List<ClientHandler> clients, List<ClientHandler> waitingRoom){
        this.socket = socket;
        this.Clients = clients;
        ClientHandler.waitingRoom = waitingRoom;
    }
    

    @Override
    public void run() {
        try {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            out.flush();

            playerName = in.readLine().trim();

            synchronized (Clients){
                Clients.add(this);
            }

            // Add an if statement for invalid player namesp

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }


   

}