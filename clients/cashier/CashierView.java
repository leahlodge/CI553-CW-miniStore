package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * View of the model 
 */
public class CashierView implements Observer
{
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels
    
    private static final String CHECK  = "Check";
    private static final String BUY    = "Buy";
    private static final String PAY_NOW = "Pay Now"; // Updated button text

    private final JLabel      pageTitle  = new JLabel();
    private final JLabel      theAction  = new JLabel();
    private final JTextField  theInput   = new JTextField();
    private final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();
    private final JButton     theBtCheck = new JButton(CHECK);
    private final JButton     theBtBuy   = new JButton(BUY);
    private final JButton     theBtPayNow = new JButton(PAY_NOW); // Updated button initialisation

    private StockReadWriter theStock     = null;
    private OrderProcessing theOrder     = null;
    private CashierController cont       = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen 
     * @param y     y-coordinate of position of window on screen  
     */
    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y)
    {
        try
        {      
            theStock = mf.makeStockReadWriter();        // Database access
            theOrder = mf.makeOrderProcessing();        // Process order
        } 
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();            // Content Pane
        Container rootWindow = (Container) rpc;         // Root Window
        cp.setLayout(null);                             // No layout manager
        rootWindow.setSize(W, H);                       // Size of Window
        rootWindow.setLocation(x, y);

        // Set the background colour of the content pane to navy blue
        cp.setBackground(new Color(0, 31, 63));

        Font f = new Font("Monospaced", Font.PLAIN, 12); // Font for text areas

        // Title
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Thank You for Shopping at MiniStore");
        pageTitle.setForeground(Color.WHITE); // White text for the title
        cp.add(pageTitle);  

        // Apply styling to buttons
        styleButton(theBtCheck, 16, 25 + 60 * 0);
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);

        styleButton(theBtBuy, 16, 25 + 60 * 1);
        theBtBuy.addActionListener(e -> cont.doBuy());
        cp.add(theBtBuy);
        
        // "Pay Now" button
        styleButton(theBtPayNow, 16, 25 + 60 * 3);
        theBtPayNow.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to complete the purchase?",
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION) {
                cont.doBought(); // Assuming this method processes the purchase
                JOptionPane.showMessageDialog(null, "Purchase completed successfully. Thank you for shopping!");
            }
        });
        cp.add(theBtPayNow);

        // Action label
        theAction.setBounds(110, 25, 270, 20);       // Message area
        theAction.setText("");                       // Blank
        theAction.setForeground(Color.WHITE); // White text
        cp.add(theAction); // Add to canvas

        // Input field
        theInput.setBounds(110, 50, 270, 40);        // Input Area
        theInput.setText("");                        // Blank
        cp.add(theInput); // Add to canvas

        // Scroll pane for output
        theSP.setBounds(110, 100, 270, 160);         // Scrolling pane
        theOutput.setText("");                       // Blank
        theOutput.setFont(f);                        // Use font  
        theOutput.setBackground(new Color(173, 216, 230)); // Lighter blue background
        theOutput.setForeground(Color.BLACK); // Black text
        cp.add(theSP); // Add to canvas
        theSP.getViewport().add(theOutput); // In TextArea
        rootWindow.setVisible(true); // Make visible
        theInput.requestFocus(); // Focus on the input field
    }

    /**
     * Style the buttons with consistent appearance
     */
    private void styleButton(JButton button, int x, int y) {
        button.setFont(new Font("Helvetica", Font.BOLD, 14)); // Helvetica font
        button.setBackground(new Color(0, 31, 63)); // Navy blue background
        button.setForeground(Color.WHITE); // White text for the button
        button.setFocusPainted(false); // Remove focus highlight
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border 
        button.setBounds(x, y, 80, 40); // position and size of the button
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param c   The controller
     */
    public void setController(CashierController c)
    {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    @Override
    public void update(Observable modelC, Object arg)
    {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        Basket basket = model.getBasket();
        if (basket == null || basket.isEmpty()) {
            theOutput.setText("Basket is empty. Thank you for shopping!");
        } else {
            theOutput.setText(basket.getDetails());
        }
        theInput.requestFocus(); // Focus is here 
    }
}
