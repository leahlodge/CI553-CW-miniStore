package catalogue;

public class BetterBasketTest {
    public static void main(String[] args) {
        // Create an instance of BetterBasket
        BetterBasket basket = new BetterBasket();

        // Add some products
        basket.add(new Product("001", "Product A", 10.0, 1));
        basket.add(new Product("002", "Product B", 15.0, 1));
        basket.add(new Product("001", "Product A", 10.0, 2)); // This should merge

        // Print basket details to verify
        System.out.println(basket.getDetails());
    }
}