package org.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VoterLoginGUI extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9002;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton voteButton;
    private JTextField voteField;
    private JLabel statusLabel;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public VoterLoginGUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Voter Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        voteField = new JTextField();
        voteField.setVisible(false);
        voteButton = new JButton("Submit Vote");
        voteButton.setVisible(false);
        statusLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(statusLabel);
        add(voteField);
        add(voteButton);

        loginButton.addActionListener(this::performLogin);
        voteButton.addActionListener(this::submitVote);

        setLocationRelativeTo(null);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(username);
            out.writeObject(password);

            String authResult = (String) in.readObject();
            if ("Authentication successful".equals(authResult)) {
                statusLabel.setText("Login Successful. Please vote.");
                voteField.setVisible(true);
                voteButton.setVisible(true);
            } else {
                statusLabel.setText("Login Failed. Try again.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            statusLabel.setText("Error communicating with the server.");
        }
    }

    private void submitVote(ActionEvent e) {
        String vote = voteField.getText();
        try {
            out.writeObject(vote);
            String voteResult = (String) in.readObject();
            JOptionPane.showMessageDialog(this, "Vote result: " + voteResult);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting vote.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VoterLoginGUI().setVisible(true));
    }
}
