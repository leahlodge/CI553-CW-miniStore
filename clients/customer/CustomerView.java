package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
  }

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      pageTitle  = new JLabel();
  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( Name.CHECK );
  private final JButton     theBtClear = new JButton( Name.CLEAR );

  private Picture thePicture = new Picture(80,80);
  private StockReader theStock   = null;
  private CustomerController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    // Set the background color of the content pane to navy blue
    cp.setBackground(new Color(0, 31, 63));  // Navy blue background

    pageTitle.setBounds( 110, 0 , 270, 20 );       
    pageTitle.setText( "Let's go shopping" ); 
    pageTitle.setForeground(Color.WHITE); // White text for the title
    cp.add( pageTitle );

    // Customize the "Check" button
    theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);
    theBtCheck.setFont(new Font("Helvetica", Font.BOLD, 14)); // Helvetica font
    theBtCheck.setBackground(new Color(0, 31, 63)); // Navy blue background for button
    theBtCheck.setForeground(Color.WHITE); // White text for the button
    theBtCheck.setFocusPainted(false); // Remove focus highlight
    theBtCheck.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border
    theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
    cp.add(theBtCheck); // Add to canvas

    // Customize the "Clear" button
    theBtClear.setBounds(16, 25 + 60 * 1, 80, 40);
    theBtClear.setFont(new Font("Helvetica", Font.BOLD, 14)); // Helvetica font
    theBtClear.setBackground(new Color(255, 0, 0)); // Red background for button
    theBtClear.setForeground(Color.WHITE); // White text for the button
    theBtClear.setFocusPainted(false); // Remove focus highlight
    theBtClear.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // White border
    theBtClear.addActionListener(e -> cont.doClear());
    cp.add(theBtClear); // Add to canvas

    theAction.setBounds( 110, 25 , 270, 20 );       // Message area
    theAction.setText( " " );                       // blank
    theAction.setForeground(Color.WHITE);  // Set the action label text to white
    cp.add( theAction );                            //  Add to canvas

    // Set a lighter shade of blue for the input field
    theInput.setBounds( 110, 50, 270, 40 );         // Product no area
    theInput.setText("");                           // Blank
    theInput.setBackground(new Color(173, 216, 230)); // Lighter blue background for input field
    theInput.setForeground(Color.BLACK);  // Black text for the input field
    cp.add( theInput );                             //  Add to canvas
    
    theSP.setBounds( 110, 100, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    theOutput.setBackground(new Color(173, 216, 230)); // Lighter blue background for text area
    theOutput.setForeground(Color.BLACK);  // Black text for output area
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea

    thePicture.setBounds( 16, 25+60*2, 80, 80 );   // Picture area
    cp.add( thePicture );                           //  Add to canvas
    thePicture.clear();
    
    rootWindow.setVisible( true );                  // Make visible
    theInput.requestFocus();                        // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    ImageIcon image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.clear();                  // Clear picture
    } else {
      thePicture.set( image );             // Display picture
    }
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
  }
}
