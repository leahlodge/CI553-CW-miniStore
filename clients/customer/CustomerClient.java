package clients.customer;

import middle.MiddleFactory;
import middle.RemoteMiddleFactory;
import middle.Names;

import javax.swing.*;

/**
 * The standalone Customer Client. Allows the customer to interact with the system and perform actions like placing orders.
 * @author Mike Smith University of Brighton
 * @version 2.0
 * @author Shine University of Brighton
 * @version year 2024
 */
public class CustomerClient {
   
    public static void main(String args[]) {
        String stockURL = args.length < 1     // URL of stock RW
                         ? Names.STOCK_RW      // Default location
                         : args[0];            // Supplied location
        String orderURL = args.length < 2     // URL of order
                         ? Names.ORDER         // Default location
                         : args[1];            // Supplied location
        
        // Initialize RemoteMiddleFactory with stock and order URLs
        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);
        
        // Initialize the GUI for customer
        displayGUI(mrf);
    }
    
    // Method to create and display the GUI
    public static void displayGUI(MiddleFactory mf) {        
        JFrame window = new JFrame();
        window.setTitle("Search Products");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize the model, view, and controller for customer
        CustomerModel model = new CustomerModel(mf);
        CustomerView view = new CustomerView(window, mf, 0, 0);
        CustomerController controller = new CustomerController(model, view);
        
        // Set the controller for the view
        view.setController(controller);
        
        // Add observer to the model
        model.addObserver(view);
        
        // Make the window visible
        window.setSize(800, 600);
        window.setVisible(true);
    }
}
