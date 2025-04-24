package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Debugger {

    private JFrame frame;
    private JButton executeButton;
    private JTextArea textArea;

    public Debugger() {
        this.createView();
        frame.setVisible(true);
    }

    private void createView() {
        // Create the frame
        frame = new JFrame("Debugger");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Create the TextArea
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create the Execute button
        executeButton = new JButton("Execute");
        frame.add(executeButton, BorderLayout.SOUTH);

        // Action Listener for Execute button
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtil.exceptionWrapper(() -> {
                    execute();
                });
            }
        });
    }

    private void execute() {
        String sql = textArea.getText().trim();
        if (sql.isEmpty()) {
            return;
        }
        
        Database db = new Database();
        
        // Get the result of the query
        LinkedList<LinkedList<String>> result = db.executeRaw(sql);

        // Prepare a StringBuilder to construct the HTML string for display
        StringBuilder resultBuilder = new StringBuilder();

        // Start the HTML table
        resultBuilder.append("<html><table border='1'>");

        // Fetch the column names and add them as the first row of the table
        resultBuilder.append("<tr>");
        for (String columnName : result.get(0)) {
            resultBuilder.append("<th>").append(columnName).append("</th>");
        }
        resultBuilder.append("</tr>");

        // Iterate over the result list and create a row for each result
        for (int i = 1; i < result.size(); i++) {  // Starting from 1 to skip column names
            resultBuilder.append("<tr>");
            for (String cell : result.get(i)) {
                resultBuilder.append("<td>").append(cell).append("</td>");
            }
            resultBuilder.append("</tr>");
        }

        // Close the HTML table
        resultBuilder.append("</table></html>");

        // Display the result in the JOptionPane using HTML
        JOptionPane.showMessageDialog(null, resultBuilder.toString(), "Query Result", JOptionPane.INFORMATION_MESSAGE);
    }

    // Entry point for the application
    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Debugger());
    }
}

