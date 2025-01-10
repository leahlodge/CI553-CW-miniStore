package clients;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

public class LoginPage implements ActionListener {
    private boolean loginSuccessful;
    //Create Buttons and features on login page 
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField AccIdField = new JTextField();
    JPasswordField AccPasswordField = new JPasswordField();
    JLabel AccIdLabel = new JLabel("Username:");
    JLabel AccPasswordLabel = new JLabel("Password:");
    JLabel loginLabel = new JLabel("Login to your account");
    JLabel messageLabel = new JLabel();
    HashMap<String, String> logininfo;

    public LoginPage(HashMap<String, String> loginInfoOriginal) {
        this.logininfo = loginInfoOriginal;

        // Set up loginLabel
        loginLabel.setBounds(125, 10, 200, 25);
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Set up other labels 
        AccIdLabel.setBounds(50, 100, 75, 25);
        AccIdLabel.setForeground(Color.WHITE);

        AccPasswordLabel.setBounds(50, 150, 75, 25);
        AccPasswordLabel.setForeground(Color.WHITE);

        messageLabel.setBounds(125, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));
        messageLabel.setForeground(Color.WHITE); 

        AccIdField.setBounds(125, 100, 200, 25);
        AccPasswordField.setBounds(125, 150, 200, 25);

        loginButton.setBounds(125, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setForeground(Color.red);

        resetButton.setBounds(225, 200, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        resetButton.setForeground(Color.BLUE);

       
        frame.add(loginLabel);
        frame.add(AccIdLabel);
        frame.add(AccPasswordLabel);
        frame.add(messageLabel);
        frame.add(AccIdField);
        frame.add(AccPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(0, 31, 63));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            AccIdField.setText("");
            AccPasswordField.setText("");
            messageLabel.setText(""); // Clear the message label
        }

        if (e.getSource() == loginButton) {
            String userID = AccIdField.getText();
            String password = String.valueOf(AccPasswordField.getPassword());

            // login success produced by checking the entered user credentials
            if (logininfo.containsKey(userID)) {
                if (logininfo.get(userID).equals(password)) {
                    // Update messageLabel with success message
                    SwingUtilities.invokeLater(() -> {
                        messageLabel.setForeground(Color.green);
                        messageLabel.setText("Login successful");
                        frame.repaint();  // Force a repaint to update the UI
                    });

                    // Close the login page and open the welcome page
                    frame.dispose(); // Close the login page
                    WelcomePage welcomePage = new WelcomePage(userID); // Open the welcome page
                } else {
                    SwingUtilities.invokeLater(() -> {
                        messageLabel.setForeground(Color.red);
                        messageLabel.setText("Wrong password");
                        frame.repaint();  // Reflect changes 
                    });
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Username not found");
                    frame.repaint(); // reflect changes 
                });
            }
        }
    }

    // Method to check if login was successful 
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
