package clients.cashier;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import debug.DEBUG;
/**
 * Implements the Model of the cashier client.
 */
public class CashierModel extends Observable {
    private enum State { process, checked }

    private State theState = State.process;   // Current state
    private Product theProduct = null;        // Current product
    private Basket theBasket = null;          // Bought items
    private String pn = "";                   // Product being processed

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;

    /**
     * Construct the model of the Cashier.
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process;     // Current state
    }
    public Basket getBasket()
    {
      return theBasket;
    }
  

    /**
     * Check if the product is in stock.
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process;
        pn = productNum.trim();
        int amount = 1;

        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                if (pr.getQuantity() >= amount) {
                    theAction = String.format("%s : %7.2f (%2d) ",
                            pr.getDescription(), pr.getPrice(), pr.getQuantity());
                    theProduct = pr;
                    theProduct.setQuantity(amount);
                    theState = State.checked;
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }

        setChanged(); notifyObservers(theAction);
    }

    /**
     * Buy the product.
     */
    public void doBuy() {
        String theAction = "";
        int amount = 1;

        try {
            if (theState != State.checked) {
                theAction = "please check its availability";
            } else {
                boolean stockBought = theStock.buyStock(theProduct.getProductNum(), theProduct.getQuantity());
                if (stockBought) {
                    makeBasketIfReq();
                    theBasket.add(theProduct);
                    theAction = "Purchased " + theProduct.getDescription();
                } else {
                    theAction = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }

        theState = State.process;
        setChanged(); notifyObservers(theAction);
    }

    /**
     * Customer pays for the contents of the basket.
     */
    public void doBought() {
        String theAction = "";

        try {
            if (theBasket != null && !theBasket.isEmpty()) {
                theOrder.newOrder(theBasket);
                theBasket = null;
            }
            theAction = "Start New Order";
            theState = State.process;
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBought", e.getMessage());
            theAction = e.getMessage();
        }

        notifyObservers(theAction);
    }

    /**
     * Ask for update of view called at the start of day or after system reset.
     */
    public void askForUpdate() {
        notifyObservers("Welcome");
    }

    /**
     * Make a Basket when required.
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber();
                theBasket = makeBasket();
                theBasket.setOrderNum(uon);
            } catch (OrderException e) {
                DEBUG.error("Comms failure\nCashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    /**
     * Return an instance of a new Basket.
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new BetterBasket();
    }

  
}

