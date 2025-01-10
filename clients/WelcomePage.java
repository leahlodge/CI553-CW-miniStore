package clients;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class WelcomePage {
    JFrame frame = new JFrame();
    JLabel welcomeLabel = new JLabel("Welcome to Brighton Ministore");
    JLabel helloLabel;
    JButton nextButton = new JButton("Next");
    JPanel headerPanel = new JPanel();

    WelcomePage(String userID) {
        // Initialize helloLabel after userID is available
        helloLabel = new JLabel("Hello " + userID);
        helloLabel.setBounds(10, 90, 400, 35); //  size the hello label
        helloLabel.setFont(new Font("Helvetica", Font.BOLD, 25)); // font: Helvetica
        helloLabel.setForeground(new Color(0, 31, 63)); // colour
        
        // Header panel with a white background
        headerPanel.setBounds(0, 0, 420, 80); // size of header panel
        headerPanel.setBackground(Color.WHITE); // White background for header panel
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centre align content

        // Set up the welcome label within the header panel
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 30)); // Elegant font with bold styling
        welcomeLabel.setForeground(new Color(0, 31, 63)); // colour
        welcomeLabel.setText("Welcome to Brighton Ministore"); //  welcome text

        //lines above and below the welcome label
        Border lineBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY), // Top line
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)  // Bottom line
        );
        welcomeLabel.setBorder(lineBorder);

        // welcome label added to the header panel
        headerPanel.add(welcomeLabel);

        // Set up the next button with a flat design
        nextButton.setBounds(150, 150, 100, 40);
        nextButton.setFont(new Font("Helvetica", Font.BOLD, 20)); // Helvetica font
        nextButton.setBackground(new Color(0, 0, 128)); // Navy blue background for the button
        nextButton.setForeground(Color.WHITE); //  colour for button
        nextButton.setFocusPainted(false); //  focus highlight
        nextButton.setBorder(null); // 
        nextButton.setOpaque(true); //
        
        // action listener for the button
        nextButton.addActionListener(e -> openMVCs());

        // gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Gradient from pastel blue to dark navy blue
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(162, 194, 225), // Pastel Blue
                        getWidth(), getHeight(), new Color(0, 31, 63) // Dark Navy Blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight()); 
        };

        };

        // Set the gradient panel background to navy blue
        gradientPanel.setBackground(new Color(0, 31, 63)); // Navy blue background for the entire panel
        gradientPanel.setLayout(null);
        gradientPanel.setPreferredSize(new Dimension(420, 420));
        
        // Add components to the gradient panel
        gradientPanel.add(headerPanel); // Add the header panel with the welcome label
        gradientPanel.add(helloLabel); // Add the hello label
        gradientPanel.add(nextButton); // Add the next button

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420); // Frame size
        frame.add(gradientPanel);
        frame.setVisible(true); // Make frame visible
    }

    /**
     * This method will be called when the "Next" button is clicked.
     * It opens the 5 MVCs (Customer, Cashier, Packing, Backdoor).
     */
    private void openMVCs() {
        // Call the begin method from Main class to open all MVCs
        new Main().begin();
        frame.dispose(); // Close the welcome page after opening the MVCs
    }

    public static void main(String[] args) {
        new WelcomePage("JohnDoe"); // Test Method 
    }
}
