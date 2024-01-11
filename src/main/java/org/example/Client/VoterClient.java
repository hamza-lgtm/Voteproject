package org.example.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class VoterClient {

    private static final String SERVER_ADDRESS = "localhost"; // Replace with the actual server address
    private static final int SERVER_PORT = 9002;  // Replace with the actual server port

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            Scanner scanner = new Scanner(System.in);

            // Input voter credentials
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Send voter credentials to the server
            out.writeObject(username);
            out.writeObject(password);

            // Receive authentication result from the server
            String authResult = (String) in.readObject();
            System.out.println(authResult);

            if (authResult.equals("Authentication successful")) {
                // Input and send the vote
                System.out.print("Enter your vote: ");
                String vote = scanner.nextLine();
                out.writeObject(vote);

                // Receive vote processing result from the server
                String voteResult = (String) in.readObject();
                System.out.println(voteResult);
            } else {
                System.out.println("Failed to authenticate. Voting not possible.");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error communicating with the server.");
        }
    }
}
