import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
public class CustomerPortal {
    private static final Scanner sc = new Scanner(System.in);
    private static final Properties connectionProperties = new Properties();
    private static final ArrayList<Item> defaultItems = new ArrayList<>();
    private static final ArrayList<PerishableItem> perishableItems = new ArrayList<>();
    private static final ArrayList<Garment> garments = new ArrayList<>();
    private static final ArrayList<Order> newOrders = new ArrayList<>();
    private static final Gson gsonItems = new GsonBuilder().registerTypeAdapter(Item.class, new ItemDeserializer()).registerTypeAdapter(Item.class, new ItemSerializer()).create();
    private static final Gson gsonPerishable = new GsonBuilder().registerTypeAdapter(PerishableItem.class, new PerishableItemDeserializer()).registerTypeAdapter(PerishableItem.class, new PerishableItemSerializer()).create();
    private static final Gson gsonGarment = new GsonBuilder().registerTypeAdapter(Garment.class, new GarmentDeserializer()).registerTypeAdapter(Garment.class, new GarmentSerializer()).create();
    private static final Gson gsonCustomer = new GsonBuilder().registerTypeAdapter(Customer.class, new CustomerDeserializer()).registerTypeAdapter(Customer.class, new CustomerSerializer()).create();
    private static final Gson gsonOrder = new GsonBuilder().registerTypeAdapter(Order.class, new OrderDeserializer()).registerTypeAdapter(Order.class, new OrderSerializer()).create();
    private static final Gson gson = new Gson();
    public static final String ORIGIN = "";
    private static Customer customer;
    private static boolean perishable;
    private static boolean hasGarment;
    private static final String URL = "";
    private static final String API_KEY = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        connectionProperties.put("user", USERNAME);
        connectionProperties.put("password", PASSWORD);
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
            rs.close();
            connection.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        int response = -1;
        while(response != 3){
            System.out.println("1: Sign In\n2: Sign Up\n3: EXIT");
            response = sc.nextInt();
            sc.nextLine();
            switch (response){
                case 1 -> login();
                case 2 -> createAccount();
                case 3 -> exitRoutine();
            }
        }
    }

    private static void createAccount() {
        int UID = -1;
        boolean validUserName = false;
        String userName = "";
        while (!validUserName){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nEnter a Username:");
            userName = sc.nextLine();
            if (uniqueIDGenerator.userNameExists(userName)){
                System.out.println("User Name is Taken");
            } else{
                validUserName = true;
                UID = uniqueIDGenerator.generateUID();
            }
        }
        boolean verified = false;
        String passwordOne = "";
        while (!verified){
            System.out.println("Enter a password:");
            passwordOne = sc.nextLine();
            System.out.println("Confirm Password");
            String passwordTwo = sc.nextLine();
            if (passwordTwo.equals(passwordOne)){
                verified = true;
            } else {
                System.out.println("Passwords Did Not Match!!!");
            }
        }
        System.out.println("Enter your first name:");
        String fName = sc.nextLine();
        System.out.println("Enter you last name:");
        String lName = sc.nextLine();
        System.out.println("Enter your date of birth (YYYY-MM-DD):");
        String date = sc.nextLine();
        LocalDate DOB = LocalDate.parse(date);
        boolean validAddress = false;
        String address = "";
        double distance = -1;
        System.out.println("Enter your Address:");
        while (!validAddress) {
            address = sc.nextLine();
            address = address.replaceAll(" ","+");
            try {
                java.net.URL mapsAPI = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + ORIGIN + "&destination=" + address + "&units=metric&key="+API_KEY);
                URLConnection con = mapsAPI.openConnection();
                Scanner sc = new Scanner(con.getInputStream());
                StringBuilder bob = new StringBuilder();
                while (sc.hasNext()) {
                    bob.append(sc.next());
                }
                JSONObject object = new JSONObject(bob.toString());
                distance = ((JSONObject) object.query("/routes/0/legs/0/distance")).getInt("value")/1000.0;
                validAddress = true;
            } catch (Exception e) {
                System.out.println("Enter a valid address: ");
            }
        }
        System.out.println("Enter Credit Card Number:");
        long ccNumber = sc.nextLong();
        sc.nextLine();
        System.out.println("Enter Credit Card Expiration Date (YYYY-MM-01):");
        String expiration = sc.nextLine();
        LocalDate ccExpiration = LocalDate.parse(expiration);
        System.out.println("Enter Credit Card CVC: ");
        int CVC = sc.nextInt();
        sc.nextLine();
        ArrayList<Order> pastOrders = new ArrayList<>();
        pastOrders.add(new Order(LocalDate.parse("1111-11-11")));
        try {
            Connection connection = DriverManager.getConnection(URL, connectionProperties); //Home Server
            Statement statement = connection.createStatement();
            String json = gson.toJson(pastOrders);
            Customer customer1 = new Customer(fName, lName, address, userName, passwordOne, UID, ccNumber, CVC, pastOrders, DOB, ccExpiration, distance);
            String commandString = "INSERT INTO store.customer (UserID, FirstName, LastName, Address, CCNumber, CVC, PastOrders, DOB, CCExpiration, Distance, UserName, Password, Object) VALUES(" + UID + ", \"" + fName + "\", \"";
            commandString += lName + "\", \"" + address + "\", " + ccNumber + ", " + CVC + ", '" + json + "', '" + DOB + "', '" + ccExpiration + "', " + distance + ", \"" + userName + "\", \"" + passwordOne + "\", '";
            commandString += gsonCustomer.toJson(customer1) + "')";
            statement.execute(commandString);
        } catch (SQLException e){
            e.printStackTrace();
        }
        login();
    }

    private static void exitRoutine(){
        try {
            Connection connection = DriverManager.getConnection(URL, connectionProperties); //Home Server
            Statement statement = connection.createStatement();
            String commandString;
            StringBuilder json;
            boolean first = true;
            if(customer != null) {
                json = new StringBuilder("[");
                for (Order order : customer.getPastOrders()){
                    if(first){
                        json.append(gsonOrder.toJson(order, Order.class)).append("}");
                        first = false;
                    } else {
                        json.append(", ").append(gsonOrder.toJson(order, Order.class));
                    }
                }
                json.deleteCharAt(json.length() - 1);
                json.append("]");
                commandString = "UPDATE store.customer SET PastOrders = '" + json + "' WHERE UserID = " + customer.getUserID();
                statement.executeUpdate(commandString);
                json = new StringBuilder().append(gsonCustomer.toJson(customer, Customer.class));
                commandString = "UPDATE store.customer SET Object = '" + json + "' WHERE UserID = " + customer.getUserID();
                statement.executeUpdate(commandString);
            }
            System.out.println("BYE");
            for (Order order : newOrders){
                commandString = "INSERT INTO store.order (OID, OrderDate, CustomerID, Invoice, Total, Tax, ShippingCost, Perishable, hasGarments, Boxes, DefaultItems, PerishableItems, Garments, Carrier, Object) VALUES(";
                commandString += order.getOID() + ", '" + order.getOrderDate() + "', '";
                json = new StringBuilder(gson.toJson(order.getCustomerID()));
                commandString += json + "', " + order.getInvoice() + ", " + order.getTotal() + ", " + order.getTax() + ", " + order.getShippingCost() + ", " + order.isPerishable() + ", " + order.hasGarments() + ", '";
                json = new StringBuilder(gson.toJson(order.getBoxes()));
                commandString += json + "', '";
                ArrayList<Item> defaultItems = new ArrayList<>();
                ArrayList<PerishableItem> perishableItems = new ArrayList<>();
                ArrayList<Garment> garments = new ArrayList<>();
                for (Item item : order.getItems()){
                    if (item.type == 0){
                        defaultItems.add(item);
                    } else if (item.type == 1){
                        perishableItems.add((PerishableItem) item);
                    } else{
                        garments.add((Garment) item);
                    }
                }
                json = new StringBuilder().append("[");
                first = true;
                for(Item item : defaultItems){
                    if (first) {
                        json.append(gsonItems.toJson(item));
                        first = false;
                    }else{
                        json.append(", ").append(gsonItems.toJson(item));
                    }
                }
                json.append("]");
                commandString += json + "', '";
                json = new StringBuilder().append("[");
                first = true;
                for(PerishableItem item : perishableItems){
                    if (first) {
                        json.append(gsonPerishable.toJson(item));
                        first = false;
                    }else{
                        json.append(", ").append(gsonPerishable.toJson(item));
                    }
                }
                json.append("]");
                commandString += json + "', '";
                json = new StringBuilder().append("[");
                first = true;
                for(Garment item : garments){
                    if (first) {
                        json.append(gsonItems.toJson(item));
                        first = false;
                    }else{
                        json.append(", ").append(gsonItems.toJson(item));
                    }
                }
                json.append("]");
                commandString += json + "', '";
                json = new StringBuilder(gson.toJson(order.getCarrier()));
                commandString += json + "', '";
                json = new StringBuilder().append(gsonOrder.toJson(order));
                commandString += json + "')";
                statement.execute(commandString);

            }
            connection.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void login() {
        boolean exists = false;
        boolean loginSuccess = false;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nLOGIN:");
        System.out.println("Enter Username: ");
        String response = sc.nextLine();
        String username = response;

        try{
            Connection connection = DriverManager.getConnection(URL, connectionProperties); //Home Server
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT * FROM store.customer");
            while(rs.next()){
                if(rs.getString("UserName").equals(response)){
                    exists = true;
                    break;
                }
            }
            if(exists){
                System.out.println("Enter Password:");
                response = sc.nextLine();
                rs = statement.executeQuery("SELECT * FROM store.customer");
                while(rs.next()){
                    if(rs.getString("Password").equals(response)){
                        loginSuccess = true;
                    }
                }
            }else{
                System.out.println("Incorrect Username");
            }
            if(loginSuccess){
                System.out.println("Login Success");
                shop(statement, username);
            } else {
                System.out.println("Incorrect Password");
            }
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void shop(Statement statement, String userName) throws IOException {
        try {
            String commandString = "SELECT Object FROM store.customer WHERE UserName=";
            commandString += "\"" +userName + "\"";
            ResultSet rs = statement.executeQuery(commandString);
            if(rs.next()) {
                String json = rs.getString("Object");
                customer = gsonCustomer.fromJson(json, Customer.class);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        int response = -1;
        while(response != 3){
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n1: Create New Order\n2: View Previous Orders\n3: Modify Account Info\n4: Logout");
            response = sc.nextInt();
            sc.nextLine();
            switch (response){
                case 1 -> newOrder();
                case 2 -> viewOrders();
                case 3 -> System.out.println("To change account info email: HELP@GENERICSTORENAME.com");
                case 4 -> System.out.println("BYE");
            }
        }
    }

    private static void viewOrders(){
        boolean found = false;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\nOrders:");
        for (Order order: customer.getPastOrders()){
            System.out.println("Order Id Numbers: " + order.getOID());
        }
        System.out.println("Enter the Order Id number you wish to view: ");
        int OID = sc.nextInt();
        sc.nextLine();
        for (Order order: customer.getPastOrders()){
            if(order.getOID() == OID){
                found = true;
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n" + order);
            }
        }
        if (found) {
            System.out.println("\nOrder Found");
        } else{
            System.out.println("Order Does Not Exist");
        }
    }

    private static void newOrder() throws IOException {
        ArrayList<Item> defaultItemsCART = new ArrayList<>();
        ArrayList<PerishableItem> perishableItemsCART = new ArrayList<>();
        ArrayList<Garment> garmentsCART =  new ArrayList<>();
        perishable = false;
        hasGarment = true;
        int response = -1;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\nChoose an option:");
        while (response != 5) {
            System.out.println("1: Add a Perishable Item\n2: Add a Garment\n3: Other Items\n4: Finish Purchase\n5: Exit");
            response = sc.nextInt();
            sc.nextLine();
            switch (response){
                case 1 -> perishableItem(perishableItemsCART);
                case 2 -> garment(garmentsCART);
                case 3 -> defaultItem(defaultItemsCART);
                case 4 -> completePurchase(defaultItemsCART, perishableItemsCART, garmentsCART);
            }
        }

    }

    private static void perishableItem(ArrayList<PerishableItem> perishableItemsCART){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (Item item : perishableItems){
            if (item.getStock() > 0){
                System.out.println(item);
            }
        }
        int numToAdd = 0, SKU;
        boolean found = false;
        PerishableItem currentItem = null;
        System.out.println("Enter the SKU of the item you want to add: ");
        SKU = sc.nextInt();
        sc.nextLine();
        for (PerishableItem item : perishableItems){
            if (item.getSKU() == SKU){
                currentItem = item;
                found = true;
            }
        }
        if(found) {
            perishable = true;
            boolean valid = false;
            while (!valid) {
                System.out.println("Enter the number of items you wish to add: ");
                numToAdd = sc.nextInt();
                sc.nextLine();
                if (numToAdd <= currentItem.getStock()){
                    valid = true;
                }else{
                    System.out.println("Not Enough In Stock\nRemaining: " + currentItem.getStock());
                }
            }
            PerishableItem itemToAdd = SerializationUtils.clone(currentItem);
            itemToAdd.setStock(numToAdd);
            currentItem.setStock(currentItem.getStock()-numToAdd);
            perishableItemsCART.add(itemToAdd);
            System.out.println("Item Successfully Added");
        } else{
            System.out.println("Item Not Found");
        }
    }

    private static void garment(ArrayList<Garment> garmentsCART){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (Item item : garments){
            if (item.getStock() > 0){
                System.out.println(item);
            }
        }
        int numToAdd = 0, SKU;
        boolean found = false;
        Garment currentItem = null;
        System.out.println("Enter the SKU of the item you want to add: ");
        SKU = sc.nextInt();
        sc.nextLine();
        for (Garment item : garments){
            if (item.getSKU() == SKU){
                currentItem = item;
                found = true;
            }
        }
        if(found) {
            hasGarment = true;
            boolean valid = false;
            while (!valid) {
                System.out.println("Enter the number of items you wish to add: ");
                numToAdd = sc.nextInt();
                sc.nextLine();
                if (numToAdd <= currentItem.getStock()){
                    valid = true;
                }else{
                    System.out.println("Not Enough In Stock\nRemaining: " + currentItem.getStock());
                }
            }
            Garment itemToAdd = SerializationUtils.clone(currentItem);
            itemToAdd.setStock(numToAdd);
            currentItem.setStock(currentItem.getStock()-numToAdd);
            garmentsCART.add(itemToAdd);
            System.out.println("Item Successfully Added");
        } else{
            System.out.println("Item Not Found");
        }
    }

    private static void defaultItem(ArrayList<Item> defaultItemsCART){
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (Item item : defaultItems){
            if (item.getStock() > 0){
                System.out.println(item);
            }
        }
        int numToAdd = 0, SKU;
        boolean found = false;
        Item currentItem = null;
        System.out.println("Enter the SKU of the item you want to add: ");
        SKU = sc.nextInt();
        sc.nextLine();
        for (Item item : defaultItems){
            if (item.getSKU() == SKU){
                currentItem = item;
                found = true;
            }
        }
        if(found) {
            boolean valid = false;
            while (!valid) {
                System.out.println("Enter the number of items you wish to add: ");
                numToAdd = sc.nextInt();
                sc.nextLine();
                if (numToAdd <= currentItem.getStock()){
                    valid = true;
                }else{
                    System.out.println("Not Enough In Stock\nRemaining: " + currentItem.getStock());
                }
            }
            Item itemToAdd = SerializationUtils.clone(currentItem);
            itemToAdd.setStock(numToAdd);
            currentItem.setStock(currentItem.getStock()-numToAdd);
            defaultItemsCART.add(itemToAdd);
            System.out.println("Item Successfully Added");
        } else{
            System.out.println("Item Not Found");
        }
    }

    private static void completePurchase(ArrayList<Item> defaultItemsCART, ArrayList<PerishableItem> perishableItemsCART, ArrayList<Garment> garmentsCART) throws IOException {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        ArrayList<Item> items = new ArrayList<>(defaultItemsCART);
        if(perishable) {
            items.addAll(perishableItemsCART);
        }
        if(hasGarment) {
            items.addAll(garmentsCART);
        }
        double tax = 1.0625;
        System.out.println("Here is your cart: ");
        double total = 0;
        double volumeTemp = 0;
        for (Item item : items){
            System.out.println(item + "Quantity: " + item.getStock());
            total += item.getPrice() * item.getStock();
            volumeTemp += item.getWidth() * item.getLength() * item.getHeight();
        }
        int numOfLargeBoxes = (int) (volumeTemp/702);
        int volume = (int) (volumeTemp%702);
        int numOfMediumBoxes = volume/528;
        volume = (int) (volumeTemp%528);
        int numOfSmallBoxes = volume/286;
        ArrayList<Boxes.Box> boxes = new ArrayList<>();
        for (int i = numOfLargeBoxes; i > 0; i--){
            boxes.add(Boxes.Box.LARGE);
        }
        for (int i = numOfMediumBoxes; i > 0; i--){
            boxes.add(Boxes.Box.MEDIUM);
        }
        for (int i = numOfSmallBoxes; i > 0; i--){
            boxes.add(Boxes.Box.SMALL);
        }
        double shippingCost = 0.05 * customer.getDistance() + 5 * boxes.size() + 25;
        LocalDate date = LocalDate.now();
        System.out.println("What Carrier Would You Like To Ship With:\n1: USPS\n2: UPS\n3: FEDEX");
        int numResponse = sc.nextInt();
        sc.nextLine();
        Carriers.Carrier carrier = Carriers.Carrier.USPS;
        switch (numResponse){
            case 2 -> carrier = Carriers.Carrier.UPS;
            case 3 -> carrier = Carriers.Carrier.FEDEX;
        }
        System.out.println("\n\nYour Total is: " + total + "\nAfter Tax: " + (total*tax) + "\nShipping Cost: " + shippingCost + "\nGrand Total: " + (total*tax + shippingCost));
        System.out.println("Would you like to submit your order? (Y/N)");
        String response = sc.nextLine().toLowerCase();
        int OID = uniqueIDGenerator.generateOID();
        if(response.equals("y")){
            Order order = new Order(date, customer.getUserID(), OID, OID, total, tax, shippingCost, boxes, items, perishable, hasGarment, carrier);
            customer.addOrder(order);
            newOrders.add(order);
            PDFGenerator.generateInvoice(order, customer);
            System.out.println("Your order has been submitted!\nYou can start another or exit");
        }
    }
}
