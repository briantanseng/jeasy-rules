package jeasy.rules;

public class Shop {

    public void declinePurchaseMessage(String name) {
        System.out.println(String.format("Shop: Sorry. No can do, %s.", name));
    }

}
