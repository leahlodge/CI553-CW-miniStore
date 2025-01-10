package catalogue;

import java.io.Serializable;
import java.util.Collections;

/**
 * Write a description of class BetterBasket here.
 * 
 * @author  Your Name
 * @version 1.0
 */
public class BetterBasket extends Basket implements Serializable
{
  private static final long serialVersionUID = 1L;
  @Override
  public boolean add(Product pr) {
      for (Product existingProduct : this) {
          if (existingProduct.getProductNum().equals(pr.getProductNum())) {
              // Merge quantities
              existingProduct.setQuantity(existingProduct.getQuantity() + pr.getQuantity());
              return true; //
          }
      }
      return super.add(pr); // Add as a new product if not already in the basket

  }
}
  