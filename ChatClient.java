import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "127.0.0.1";  // Local server address
        final int PORT_NUMBER = 12345;  // Port to connect to

        try {
            // Establish a connection to the server
            Socket socket = new Socket(SERVER_ADDRESS, PORT_NUMBER);
            System.out.println("Connected to the chat server.");

            // Set up input and output streams for server communication
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);

            // Thread to listen for incoming messages from the server
            Thread listenerThread = new Thread(() -> {
                String messageFromServer;
                try {
                    while ((messageFromServer = serverInput.readLine()) != null) {
                        System.out.println(messageFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection lost with the server.");
                    e.printStackTrace();
                }
            });
            listenerThread.start();

            // Read messages from console to send to the server
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String messageToSend;
            while ((messageToSend = consoleInput.readLine()) != null) {
                if (messageToSend.equalsIgnoreCase("exit")) {
                    break;
                }
                serverOutput.println(messageToSend);
            }

            // Clean up resources
            serverInput.close();
            serverOutput.close();
            socket.close();
            consoleInput.close();
        } catch (IOException e) {
            System.out.println("Error connecting to server.");
            e.printStackTrace();
        }
    }
}
