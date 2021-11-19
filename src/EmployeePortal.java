import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class EmployeePortal {
    private static final Scanner sc = new Scanner(System.in);
    private static final Properties connectionProperties = new Properties();
    private static final ArrayList<Item> defaultItems = new ArrayList<>();
    private static final ArrayList<PerishableItem> perishableItems = new ArrayList<>();
    private static final ArrayList<Garment> garments = new ArrayList<>();
    private static final ArrayList<Order> orders = new ArrayList<>();
    private static final Gson gsonItems = new GsonBuilder().registerTypeAdapter(Item.class, new ItemDeserializer()).registerTypeAdapter(Item.class, new ItemSerializer()).create();
    private static final Gson gsonPerishable = new GsonBuilder().registerTypeAdapter(PerishableItem.class, new PerishableItemDeserializer()).registerTypeAdapter(PerishableItem.class, new PerishableItemSerializer()).create();
    private static final Gson gsonGarment = new GsonBuilder().registerTypeAdapter(Garment.class, new GarmentDeserializer()).registerTypeAdapter(Garment.class, new GarmentSerializer()).create();
    private static final Gson gsonOrder = new GsonBuilder().registerTypeAdapter(Order.class, new OrderDeserializer()).registerTypeAdapter(Order.class, new OrderSerializer()).create();
    private static final String URL = "jdbc:mysql://parl4224h.ddns.net/store?useSSL=true";

    public static void main(String[] args) {
        connectionProperties.put("user", "parker_hoffmann");
        connectionProperties.put("password", "COMPSCIA(2021)perdomo");
        try {
            Connection connection = DriverManager.getConnection(URL, connectionProperties);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Object FROM store.item");
            while (rs.next()){
                defaultItems.add(gsonItems.fromJson(JsonParser.parseString(rs.getString("Object")), Item.class));
            }
            rs = statement.executeQuery("SELECT Object FROM store.perishable");
            while(rs.next()){
                perishableItems.add(gsonPerishable.fromJson(JsonParser.parseString(rs.getString("Object")), PerishableItem.class));
            }
            rs = statement.executeQuery("SELECT Object FROM store.garment");
            while(rs.next()){
                garments.add(gsonGarment.fromJson(JsonParser.parseString(rs.getString("Object")), Garment.class));
            }
            rs = statement.executeQuery("SELECT Object FROM store.`order`");
            while(rs.next()){
                orders.add(gsonOrder.fromJson(JsonParser.parseString(rs.getString("Object")), Order.class));
            }
            rs.close();
            connection.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        int response = -1;
        while(response != 2){
            System.out.println("1: Sign In\n2: EXIT");
            response = sc.nextInt();
            sc.nextLine();
            switch (response){
                case 1 -> login();
                case 2 -> System.out.println("BYE");
            }
        }
    }
    private static void login() {
        boolean exists = false;
        boolean loginSuccess = false;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nLOGIN:");
        System.out.println("Enter Username: ");
        String response = sc.nextLine();
        String username = response;

        try {
            Connection connection = DriverManager.getConnection(URL, connectionProperties); //Home Server
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT UserName FROM store.employee");
            while (rs.next()) {
                if (rs.getString("UserName").equals(response)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("Enter Password:");
                response = sc.nextLine();
                rs = statement.executeQuery("SELECT Password FROM store.employee WHERE UserName = \"" + username + "\"");
                while (rs.next()) {
                    if (rs.getString("Password").equals(response)) {
                        loginSuccess = true;
                    }
                }
            } else {
                System.out.println("Incorrect Username");
            }
            if (loginSuccess) {
                System.out.println("Login Success");
                modify();
            } else {
                System.out.println("Incorrect Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void modify(){
        int response = -1;
        while(response != 3){
            System.out.println("1: Manage Inventory\n2: View Orders\n3: Logout");
            response = sc.nextInt();
            sc.nextLine();
            switch (response){
                case 1 -> inventoryManage();
                case 2 -> viewOrders();
                case 3 -> System.out.println("BYE");
            }
        }
    }
    private static void viewOrders(){
        for (Order order: orders){
            System.out.println(order);
        }
    }
    private static void inventoryManage(){
        System.out.println("1: Add Inventory Item\n2: Adjust Stock\n3: Exit");
        int response = sc.nextInt();
        sc.nextLine();
        switch(response){
            case 1 -> addItem();
            case 2 -> changeStock();
            case 3 -> System.out.println("COMPLETED");
        }
    }
    private static void addItem() {
        System.out.println("1: Add a Perishable Item\n2: Add a Garment\n3: Other Items\n4: Exit");
        int response = sc.nextInt();
        sc.nextLine();
        switch (response) {
            case 1 -> perishableItem();
            case 2 -> garment();
            case 3 -> defaultItem();
            case 4 -> System.out.println("Actions Completed");
        }
    }
    private static void perishableItem(){
        System.out.println("Item Name:");
        String name = sc.nextLine();
        System.out.println("Stock: ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.println("Width: ");
        double width = sc.nextDouble();
        sc.nextLine();
        System.out.println("Length: ");
        double length = sc.nextDouble();
        sc.nextLine();
        System.out.println("Height: ");
        double height = sc.nextDouble();
        sc.nextLine();
        System.out.println("Weight: ");
        double weight = sc.nextDouble();
        sc.nextLine();
        System.out.println("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.println("Is Donatable (Y/N): ");
        String response = sc.nextLine().toLowerCase();
        boolean donatable;
        donatable = response.equals("y");
        System.out.println("Expiration Date (yyyy-MM-dd): ");
        response = sc.nextLine();
        LocalDate expirationDate = LocalDate.parse(response);
        PerishableItem item = new PerishableItem(name, uniqueIDGenerator.generateUID(), stock, width, height, length, weight, price, expirationDate, donatable);
        perishableItems.add(item);
        try{
            Connection connection = DriverManager.getConnection(URL, connectionProperties);
            Statement statement = connection.createStatement();
            String commandString = "INSERT INTO store.perishable (SKU, Name, Stock, Width, Height, Length, Weight, Price, ExpirationDate, IsDonatable, Object) VALUES(";
            commandString += item.getSKU() + ", " + item.getName() + ", " + item.getStock() + ", " + item.getWidth() + ", " + item.getHeight() + ", " + item.getLength() + ", " + item.getWeight() + ", " + item.getPrice();
            commandString += ", " + item.getExpirationDateString() + ", " + item.isDonatable() + ", ";
            String json = gsonPerishable.toJson(item, PerishableItem.class);
            commandString += json + ")";
            statement.execute(commandString);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void garment(){
        System.out.println("Item Name:");
        String name = sc.nextLine();
        System.out.println("Stock: ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.println("Width: ");
        double width = sc.nextDouble();
        sc.nextLine();
        System.out.println("Length: ");
        double length = sc.nextDouble();
        sc.nextLine();
        System.out.println("Height: ");
        double height = sc.nextDouble();
        sc.nextLine();
        System.out.println("Weight: ");
        double weight = sc.nextDouble();
        sc.nextLine();
        System.out.println("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.println("Size (XXS,XS,S,M,L,XL,XXL,3Xl): ");
        String size = sc.nextLine();
        Garment item = new Garment(name, uniqueIDGenerator.generateUID(), stock, width, height, length, weight, price, size);
        garments.add(item);
        try{
            Connection connection = DriverManager.getConnection(URL, connectionProperties);
            Statement statement = connection.createStatement();
            String commandString = "INSERT INTO store.perishable (SKU, Name, Stock, Width, Height, Length, Weight, Price, ExpirationDate, IsDonatable, Object) VALUES(";
            commandString += item.getSKU() + ", " + item.getName() + ", " + item.getStock() + ", " + item.getWidth() + ", " + item.getHeight() + ", " + item.getLength() + ", " + item.getWeight() + ", " + item.getPrice();
            commandString += ", " + item.getSize() + ", ";
            String json = gsonGarment.toJson(item, Garment.class);
            commandString += json + ")";
            statement.execute(commandString);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void defaultItem(){
        System.out.println("Item Name:");
        String name = sc.nextLine();
        System.out.println("Stock: ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.println("Width: ");
        double width = sc.nextDouble();
        sc.nextLine();
        System.out.println("Length: ");
        double length = sc.nextDouble();
        sc.nextLine();
        System.out.println("Height: ");
        double height = sc.nextDouble();
        sc.nextLine();
        System.out.println("Weight: ");
        double weight = sc.nextDouble();
        sc.nextLine();
        System.out.println("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        Item item = new Item(name, uniqueIDGenerator.generateUID(), stock, width, height, length, weight, price);
        defaultItems.add(item);
        try{
            Connection connection = DriverManager.getConnection(URL, connectionProperties);
            Statement statement = connection.createStatement();
            String commandString = "INSERT INTO store.perishable (SKU, Name, Stock, Width, Height, Length, Weight, Price, ExpirationDate, IsDonatable, Object) VALUES(";
            commandString += item.getSKU() + ", " + item.getName() + ", " + item.getStock() + ", " + item.getWidth() + ", " + item.getHeight() + ", " + item.getLength() + ", " + item.getWeight() + ", " + item.getPrice();
            String json = gsonItems.toJson(item, Item.class);
            commandString += json + ")";
            statement.execute(commandString);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void changeStock(){
        ArrayList<Item> items = new ArrayList<>(defaultItems);
        items.addAll(perishableItems);
        items.addAll(garments);
        for (Item item : items){
            System.out.println(item + ", Stock: " + item.getStock());
        }
        System.out.println("Enter the SKU of the item you want to modify");
        int sku = sc.nextInt();
        sc.nextLine();
        boolean found = false;
        for (Item item : items){
            if (item.getSKU() == sku){
                found = true;
                System.out.println("Enter The New Stock:");
                int stock = sc.nextInt();
                sc.nextLine();
                item.setStock(stock);
                try {
                    Connection connection = DriverManager.getConnection(URL, connectionProperties);
                    Statement statement = connection.createStatement();
                    String commandString;
                    if(item.getType() == 0){
                        commandString = "UPDATE store.item SET Stock = " + stock + " WHERE SKU = " + item.getSKU();
                        statement.execute(commandString);
                    } else if(item.getType() == 1){
                        commandString = "UPDATE store.perishable SET Stock = " + stock + " WHERE SKU = " + item.getSKU();
                        statement.execute(commandString);
                    } else if(item.getType() == 2){
                        commandString = "UPDATE store.garment SET Stock = " + stock + " WHERE SKU = " + item.getSKU();
                        statement.execute(commandString);
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        if (!found){
            System.out.println("Item with SKU: " + sku + " Not Found");
        }
    }
}
