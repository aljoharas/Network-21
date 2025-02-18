import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;

    private List<ClientHandler> players;
    private static List<ClientHandler> waitingRoom;

    public ClientHandler(Socket socket, List<ClientHandler> players, List<ClientHandler> waitingRoom){
        this.socket = socket;
        this.players = players;
        ClientHandler.waitingRoom = waitingRoom;
    }
    

    @Override
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
    }

}