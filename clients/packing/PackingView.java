package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.
 */
public class PackingView implements Observer
{
    private static final String PACKED = "Packed";
    private static final String PRINT_RECEIPT = "Receipt";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtPack = new JButton(PACKED);
    private final JButton printReceiptButton = new JButton(PRINT_RECEIPT);  // New Print Receipt button
    private final JLabel dateTimeLabel = new JLabel();  // New Date and Time label

    private OrderProcessing theOrder = null;
    private PackingController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen
     * @param y     y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rpc, MiddleFactory mf, int x, int y)
    {
        try
        {
            theOrder = mf.makeOrderProcessing(); // Process order
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null);                      // No layout manager
        rootWindow.setSize(W, H);               // Size of Window
        rootWindow.setLocation(x, y);

        cp.setBackground(new Color(0, 31, 63)); // Navy blue background for the container

        Font f = new Font("Monospaced", Font.PLAIN, 12); // Font for text areas

        // Title
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Packing Bought Order");
        pageTitle.setForeground(Color.WHITE); // White text for the title
        cp.add(pageTitle);

        // "Packed" button
        theBtPack.setBounds(16, 25 + 60 * 0, 80, 40); // Packed Button
        theBtPack.addActionListener(e -> cont.doPacked());
        theBtPack.setFont(new Font("Helvetica", Font.BOLD, 14)); // Helvetica font
        theBtPack.setBackground(new Color(0, 31, 63)); // Navy blue background for button
        theBtPack.setForeground(Color.WHITE); // White text for the button
        theBtPack.setFocusPainted(false); // Remove focus highlight
        theBtPack.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border for 
        cp.add(theBtPack); // Add to canvas

        // " Receipt" button
        printReceiptButton.setBounds(16, 25 + 60 * 1, 80, 40); // Print Receipt button position
        printReceiptButton.addActionListener(e -> showPrintConfirmationDialog()); // Action listener
        printReceiptButton.setFont(new Font("Helvetica", Font.BOLD, 14)); // Helvetica font
        printReceiptButton.setBackground(new Color(0, 31, 63)); // Navy blue background
        printReceiptButton.setForeground(Color.WHITE); // White text
        printReceiptButton.setFocusPainted(false); // Remove focus highlight
        printReceiptButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border
        cp.add(printReceiptButton); // Add to canvas

        // Action label
        theAction.setBounds(110, 25, 270, 20); // Message area
        theAction.setText("");  // Blank
        theAction.setForeground(Color.WHITE); // White text for action message
        cp.add(theAction);  // Add to canvas

        // Date and Time label
        dateTimeLabel.setBounds(16, 25 + 60 * 2, 300, 20); // Position; // Position for Date and Time
        dateTimeLabel.setForeground(Color.WHITE); // White text
        cp.add(dateTimeLabel); // Add to canvas
        startDateTimeUpdater(); // Start updating the Time

        // Scroll pane for output
        theSP.setBounds(110, 55, 270, 205);  // Scrolling pane
        theOutput.setText("Your order is on the way...");  // Default text
        theOutput.setFont(f);   // Use font
        theOutput.setBackground(new Color(173, 216, 230)); // Lighter blue background for text area
        theOutput.setForeground(Color.BLACK); // Black text for the output area
        cp.add(theSP);          // Add to canvas
        theSP.getViewport().add(theOutput); // Add text area to the scroll pane

        rootWindow.setVisible(true);  // Make window visible
    }

    public void setController(PackingController c)
    {
        cont = c;
    }

    // Print confirmation dialog when the Print Receipt button is clicked
    private void showPrintConfirmationDialog() {
        JOptionPane.showMessageDialog(
            null,
            "Your receipt will now be printed.",
            "Printing Receipt",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Start a timer to update the date and time
    private void startDateTimeUpdater() {
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();
    }

    // Update the date and time label
    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        dateTimeLabel.setText(now.format(formatter));
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args
     */
    @Override
    public void update(Observable modelC, Object arg)
    {
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket != null)
        {
            theOutput.setText(basket.getDetails());  // Update with basket details
        }
        else
        {
            theOutput.setText("No items to display.");
        }
    }
}
