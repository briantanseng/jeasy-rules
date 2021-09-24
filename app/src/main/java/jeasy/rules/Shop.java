package jeasy.rules;

public class Shop {

    public void declinePurchaseMessage(String customerName) {
        System.out.println(String.format("Shop: Sorry. No can do, %s.", customerName));
    }

    public void acceptPurchaseMessage(String customerName) {
        System.out.println(String.format("Shop: Here you go, %s. Enjoy!", customerName));
    }

}
