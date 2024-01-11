package org.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


public class VoteServer {
    private ServerSocket serverSocket;
    private static final String TOPIC = "foobar";

    public VoteServer(int port) {
        try {
            // Initialize the ServerSocket
            serverSocket = new ServerSocket(port);
            System.out.println("VoteServer is running on port " + port);
        } catch (IOException e) {
            System.err.println("Could not start the server on port " + port);
            e.printStackTrace();
        }
    }

    public void start() {
        if (serverSocket == null) {
            System.err.println("ServerSocket is not initialized. Exiting.");
            System.exit(1);
        }

        while (true) {
            try {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle client connection in a separate thread
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException e) {
                System.err.println("An error occurred while accepting a client connection.");
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            // Receive voter credentials from the client
            String username = (String) in.readObject();
            String password = (String) in.readObject();
    
            // Use RMI service for authentication
          
            AuthenticationService authenticationService = new AuthenticationServiceImpl();
            LocateRegistry.createRegistry(9003); // RMI registry port
            Naming.rebind("rmi://localhost:9003/authentication", authenticationService);
            
            boolean isAuthenticated = authenticationService.authenticate(username, password);
    
            // Send authentication result to the client
            System.out.println("Authentication result for user " + username + ": " + isAuthenticated);
            if (isAuthenticated) {
                out.writeObject("Authentication successful");
    
                // Receive and process the vote
                String vote = (String) in.readObject();
                System.out.println("Received vote from user " + username + ": " + vote);
    
                // Here, add logic to record the ve


                KafkaProducerService kafkaProducerService = new KafkaProducerService();
                kafkaProducerService.sendVoteToKafka(vote,TOPIC);




                // For example, update a database or a file
                System.out.println("Vote recorded successfully");
                out.writeObject("Vote received and recorded");
            } else {
                out.writeObject("Authentication failed");
                System.out.println("Authentication failed for user " + username);
            }
    
        } catch (Exception e) {
            System.err.println("An error occurred while handling a client.");
            e.printStackTrace();

        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket.");
                e.printStackTrace();
            }
        }
    }
    

    public static void main(String[] args) {
        int port = 9002;
        VoteServer voteServer = new VoteServer(port);
        voteServer.start();
    }
}
