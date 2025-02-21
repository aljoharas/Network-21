
import java.net.Socket;
import java.io.*;

class ServerListener implements Runnable {
    
    private Socket socket;
    private BufferedReader in;

    public ServerListener(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.trim().isEmpty()){
                    continue;
                }
                System.out.println("Server: " + serverMessage); // Print server messages
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

