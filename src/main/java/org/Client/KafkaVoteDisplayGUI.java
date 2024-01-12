package org.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaVoteDisplayGUI extends JFrame {

    private JTextArea voteDisplayArea;
    private JTextArea voteAnalysisArea;
    private JButton refreshButton;
    private KafkaVoteConsumer voteConsumer;
    private List<String> allVotes;  // Collection to store all votes
    private static final String TOPIC = "foobar";  // Replace with your Kafka topic

    public KafkaVoteDisplayGUI() {
        createUI();
        voteConsumer = new KafkaVoteConsumer(TOPIC); // Replace with your Kafka topic
        allVotes = new ArrayList<>(); // Initialize the vote list
    }

    private void createUI() {
        setTitle("Vote Display and Analysis");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        // Vote Display Area
        voteDisplayArea = new JTextArea();
        voteDisplayArea.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(voteDisplayArea);
        mainPanel.add(scrollPane1);

        // Vote Analysis Area
        voteAnalysisArea = new JTextArea();
        voteAnalysisArea.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(voteAnalysisArea);
        mainPanel.add(scrollPane2);

        add(mainPanel, BorderLayout.CENTER);

        refreshButton = new JButton("Refresh Votes");
        add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshVotes());

        setLocationRelativeTo(null);
    }

    private void refreshVotes() {
        // Fetch new votes from Kafka and add to allVotes list
        voteConsumer.pollVotes().forEach(record -> allVotes.add(record.value()));

        // Update the vote display area with all votes
        StringBuilder votesRaw = new StringBuilder();
        for (String vote : allVotes) {
            votesRaw.append("Vote: ").append(vote).append("\n");
        }
        voteDisplayArea.setText(votesRaw.toString());

        // Initialize or reset voteCounts map
        Map<String, Integer> voteCounts = new HashMap<>();
        voteCounts.put("Reda", 0);
        voteCounts.put("Hamza", 0);
        voteCounts.put("Brahim", 0);

        // Analyze votes from allVotes list
        for (String vote : allVotes) {
            voteCounts.put(vote, voteCounts.getOrDefault(vote, 0) + 1);
        }

        // Update the vote analysis area
        StringBuilder votesAnalysis = new StringBuilder();
        for (Map.Entry<String, Integer> entry : voteCounts.entrySet()) {
            votesAnalysis.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        voteAnalysisArea.setText(votesAnalysis.toString());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KafkaVoteDisplayGUI().setVisible(true));
    }
}
