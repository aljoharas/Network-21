package network21;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Socket socket = new Socket("localhost", 2121)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("> Enter your username: ");
            String username = in.readLine();
            out.println(username);

            new Thread(new ServerListener(socket)).start();

            while (true) {
                String message = in.readLine(); // Read user input for the message
                if (message.equalsIgnoreCase("exit")) { 
                    out.println(message);
                    System.out.println(in.readLine());// You can add a condition to close the connection
                    break;
                }
                out.println(message); // Send the message to the server
            }

        }
        System.exit(0);
    }

}

