import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        final int PORT_NUMBER = 12345;  // Server's listening port

        try {
            // Start the server and listen for client connections
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Chat server is running and waiting for connections...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client has connected: " + clientSocket);

            // Set up input and output streams for client communication
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);

            // Thread to receive messages from the client
            Thread listenerThread = new Thread(() -> {
                String messageFromClient;
                try {
                    while ((messageFromClient = clientInput.readLine()) != null) {
                        System.out.println("Client: " + messageFromClient);
                    }
                } catch (IOException e) {
                    System.out.println("Client connection closed.");
                    e.printStackTrace();
                }
            });
            listenerThread.start();

            // Read messages from the console to send to the client
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String messageToSend;
            while ((messageToSend = consoleInput.readLine()) != null) {
                clientOutput.println("Server: " + messageToSend);
            }

            // Clean up resources
            clientInput.close();
            clientOutput.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error starting the server.");
            e.printStackTrace();
        }
    }
}
