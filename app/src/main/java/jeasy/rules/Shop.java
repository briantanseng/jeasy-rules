package jeasy.rules;

public class Shop {

    public void declinePurchaseMessage(String name) {
        System.out.println(String.format("Sorry. No can do, %s.", name));
    }

}
