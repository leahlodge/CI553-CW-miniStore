package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */
public class BackDoorView implements Observer {
    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtRStock = new JButton(RESTOCK);
    private final JButton theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factory to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();          // Database access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null);                     // No layout manager
        rootWindow.setSize(W, H);               // Size of Window
        rootWindow.setLocation(x, y);

        // Set navy blue background for the window
        cp.setBackground(new Color(0, 31, 63));

        Font f = new Font("Monospaced", Font.PLAIN, 12);  // Font for text areas

        // Title
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Staff check and manage stock");
        pageTitle.setForeground(Color.WHITE); // White text for the title
        cp.add(pageTitle);

        // "Query" button
        theBtQuery.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
        styleButton(theBtQuery, new Color(0, 31, 63), Color.WHITE);
        cp.add(theBtQuery);

        // "Restock" button
        theBtRStock.setBounds(16, 25 + 60 * 1, 80, 40);
        theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
        styleButton(theBtRStock, new Color(0, 31, 63), Color.WHITE);
        cp.add(theBtRStock);

        // "Clear" button
        theBtClear.setBounds(16, 25 + 60 * 2, 80, 40);
        theBtClear.addActionListener(e -> cont.doClear());
        styleButton(theBtClear, Color.RED, Color.WHITE);
        cp.add(theBtClear);

        // Action label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.WHITE); // White text
        cp.add(theAction);

        // Input field
        theInput.setBounds(110, 50, 120, 40);
        cp.add(theInput);

        // Input number field
        theInputNo.setBounds(260, 50, 120, 40);
        theInputNo.setText("0");
        cp.add(theInputNo);

        // Scroll pane for output
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        theOutput.setBackground(new Color(173, 216, 230)); // Lighter blue background
        theOutput.setForeground(Color.BLACK); // Black text
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

      
        rootWindow.revalidate();
        rootWindow.repaint();

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public BackDoorView() {
        // Default constructor
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model,
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }

    /**
     * Helper method to style buttons with specific background and text colors.
     *
     * @param button      The button to style
     * @param bgColor     The background color
     * @param textColor   The text color
     */
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setFont(new Font("Helvetica", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createLineBorder(textColor)); 

    }
}
