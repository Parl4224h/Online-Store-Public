import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDeserializer implements JsonDeserializer<Order> {

    @Override
    public Order deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gsonItem = new GsonBuilder().registerTypeAdapter(Item.class, new ItemDeserializer()).create();
        Gson gsonPerishableItem = new GsonBuilder().registerTypeAdapter(PerishableItem.class, new PerishableItemDeserializer()).create();
        Gson gsonGarment =  new GsonBuilder().registerTypeAdapter(Garment.class, new GarmentDeserializer()).create();
        LocalDate orderDate = LocalDate.parse(jsonObject.get("OrderDate").getAsString());
        int customerID = jsonObject.get("CustomerID").getAsInt();
        int invoice = jsonObject.get("Invoice").getAsInt();
        int OID = jsonObject.get("OID").getAsInt();
        double total = jsonObject.get("Total").getAsDouble();
        double tax = jsonObject.get("Tax").getAsDouble();
        double shippingCost = jsonObject.get("ShippingCost").getAsDouble();
        boolean perishable = jsonObject.get("Perishable").getAsBoolean();
        boolean hasGarments = jsonObject.get("HasGarments").getAsBoolean();
        ArrayList<Boxes.Box> boxes = new ArrayList<>();
        JsonElement jsonElement2 = jsonObject.get("Boxes");
        String boxesString = String.valueOf(jsonElement2);
        boxesString = boxesString.substring(1, boxesString.length() - 1);
        ArrayList<String> boxesList = new ArrayList<>(List.of(boxesString.split(",")));
        int boxInt;
        Boxes.Box box = Boxes.Box.SMALL;
        for (String boxString : boxesList){
            boxString = boxString.substring(1, boxString.length()-1);
            boxInt = Integer.parseInt(boxString);
            switch (boxInt){
                case 1 -> box = Boxes.Box.MEDIUM;
                case 2 -> box = Boxes.Box.LARGE;
            }
            boxes.add(box);
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray("DefaultItems");
        JsonObject jsonObject1;
        ArrayList<Item> items = new ArrayList<>();
        for(JsonElement jsonElement1 : jsonArray){
            jsonObject1 = jsonElement1.getAsJsonObject();
            items.add(gsonItem.fromJson(jsonObject1, Item.class));
        }
        jsonArray = jsonObject.getAsJsonArray("PerishableItems");
        for(JsonElement jsonElement1 : jsonArray){
            jsonObject1 = jsonElement1.getAsJsonObject();
            items.add(gsonPerishableItem.fromJson(jsonObject1, PerishableItem.class));
        }
        jsonArray = jsonObject.getAsJsonArray("Garments");
        for(JsonElement jsonElement1 : jsonArray){
            jsonObject1 = jsonElement1.getAsJsonObject();
            items.add(gsonGarment.fromJson(jsonObject1, Garment.class));
        }
        int carrierIndex = jsonObject.get("Carrier").getAsInt();
        Carriers.Carrier carrier = Carriers.Carrier.USPS;
        switch (carrierIndex){
            case 1 -> carrier = Carriers.Carrier.UPS;
            case 2 -> carrier = Carriers.Carrier.FEDEX;
        }
        return new Order(orderDate, customerID, invoice, OID, total, tax, shippingCost, boxes, items, perishable, hasGarments, carrier);
    }
}
