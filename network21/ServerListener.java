import java.net.Socket;
import java.io.*;

class ServerListener implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private Player player;

    public ServerListener(Socket socket, Player player) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.player = player;

    }

    @Override
    public void run() {
        try {
            String serverMessage;
            System.out.println("Listening");
            serverMessage = in.readLine();
            System.out.println(serverMessage);
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.trim().isEmpty()){
                    continue;
                }
                System.out.println("Received from server: " + serverMessage);
                player.handleMessage(serverMessage);
                //System.out.println("Server: " + serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

