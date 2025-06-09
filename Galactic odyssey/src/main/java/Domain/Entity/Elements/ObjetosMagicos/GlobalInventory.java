package Domain.Entity.Elements.ObjetosMagicos;


import java.util.ArrayList;
import java.util.List;

public class GlobalInventory {
    private static final List<Item> sharedItems = new ArrayList<>();

    public static void addItem(Item item) {
        sharedItems.add(item);
    }

    public static void removeItem(Item item) {
        sharedItems.remove(item);
    }

    public static List<Item> getItems() {
        return sharedItems;
    }

    public static void clear() {
        sharedItems.clear();
    }
}

