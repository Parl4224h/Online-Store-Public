import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class ItemGenerator {
    private static final Properties connectionProperties = new Properties();
    private static final Gson gsonItems = new GsonBuilder().registerTypeAdapter(Item.class, new ItemDeserializer()).registerTypeAdapter(Item.class, new ItemSerializer()).create();
    private static final Gson gsonPerishable = new GsonBuilder().registerTypeAdapter(PerishableItem.class, new PerishableItemDeserializer()).registerTypeAdapter(PerishableItem.class, new PerishableItemSerializer()).create();
    private static final Gson gsonGarment = new GsonBuilder().registerTypeAdapter(Garment.class, new GarmentDeserializer()).registerTypeAdapter(Garment.class, new GarmentSerializer()).create();

    public static void main(String[] args) {
        connectionProperties.put("user","parker_hoffmann");
        connectionProperties.put("password", "COMPSCIA(2021)perdomo");

        ArrayList<Item> items = new ArrayList<>();
        Item item_1 = new Item("CPU", 1, 500,2.0,2.0,2.0,12.0,599.99);
        Item item_2 = new Item("GPU", 2, 500,2.0,2.0,2.0,12.0,1599.99);
        Item item_3 = new Item("PSU", 3, 500,2.0,2.0,2.0,12.0,99.99);
        items.add(item_1);
        items.add(item_2);
        items.add(item_3);
        PerishableItem pItem_1 = new PerishableItem("Ice_Cream", 4, 500,2.0,2.0,2.0,12.0,599.99, LocalDate.parse("2021-12-02"), true);
        PerishableItem pItem_2 = new PerishableItem("Banana_Pops", 5, 500,2.0,2.0,2.0,12.0,599.99, LocalDate.parse("2021-12-02"), true);
        PerishableItem pItem_3 = new PerishableItem("Strawberries", 6, 500,2.0,2.0,2.0,12.0,599.99, LocalDate.parse("2021-12-02"), true);
        items.add(pItem_1);
        items.add(pItem_2);
        items.add(pItem_3);
        Garment gItem_1 = new Garment("Small_Shirt", 7, 500,2.0,2.0,2.0,12.0,599.99,"S");
        Garment gItem_2 = new Garment("Medium_Shirt", 8, 500,2.0,2.0,2.0,12.0,599.99,"M");
        Garment gItem_3 = new Garment("Large_Shirt", 9, 500,2.0,2.0,2.0,12.0,599.99,"L");
        items.add(gItem_1);
        items.add(gItem_2);
        items.add(gItem_3);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://parl4224h.ddns.net/store?useSSL=true", connectionProperties); //Home Server
            Statement statement = connection.createStatement();
            String commandString;
            for (Item item : items){
                if(item.getType() == 0){
                    commandString = "INSERT INTO store.item (SKU, Name, Stock, Width, Height, Length, Weight, Price, Object) VALUES(";
                    commandString += item.getSKU() + ", \"" + item.getName() + "\", " + item.getStock() + ", " + item.getWidth() + ", " + item.getHeight() + ", " + item.getLength() + ", " + item.getWeight() + ", " + item.getPrice() + ", '";
                    commandString += gsonItems.toJson(item) + "')";
                    System.out.println(commandString);
                    statement.execute(commandString);
                } else if(item.getType() == 1){
                    PerishableItem item2 = (PerishableItem) item;
                    commandString = "INSERT INTO store.perishable (SKU, Name, Stock, Width, Height, Length, Weight, Price, ExpirationDate, IsDonatable, Object) VALUES(";
                    commandString += item2.getSKU() + ", \"" + item2.getName() + "\", " + item2.getStock() + ", " + item2.getWidth() + ", " + item2.getHeight() + ", " + item2.getLength() + ", " + item2.getWeight() + ", " + item2.getPrice() + ", '";
                    commandString += item2.getExpirationDate() + "', " + item2.isDonatable() + ", '";
                    commandString += gsonPerishable.toJson(item2) + "')";
                    System.out.println(commandString);
                    statement.execute(commandString);
                }else{
                    Garment item3 = (Garment) item;
                    commandString = "INSERT INTO store.garment (SKU, Name, Stock, Width, Height, Length, Weight, Price, Size, Object) VALUES(";
                    commandString += item3.getSKU() + ", \"" + item3.getName() + "\", " + item3.getStock() + ", " + item3.getWidth() + ", " + item3.getHeight() + ", " + item3.getLength() + ", " + item3.getWeight() + ", " + item3.getPrice() + ", \"";
                    commandString += item3.getSize() + "\", '";
                    commandString += gsonGarment.toJson(item3) + "')";
                    System.out.println(commandString);
                    statement.execute(commandString);
                }
            }
            connection.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
