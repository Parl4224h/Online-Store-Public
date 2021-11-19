import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderSerializer implements JsonSerializer<Order> {
    @Override
    public JsonElement serialize(Order order, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        Gson gson = new Gson();
        Gson gsonItem = new GsonBuilder().registerTypeAdapter(Item.class, new ItemSerializer()).create();
        Gson gsonPerishableItem = new GsonBuilder().registerTypeAdapter(PerishableItem.class, new PerishableItemSerializer()).create();
        Gson gsonGarment =  new GsonBuilder().registerTypeAdapter(Garment.class, new GarmentSerializer()).create();
        object.addProperty("Invoice", order.getInvoice());
        object.addProperty("CustomerID", order.getCustomerID());
        object.add("OrderDate", JsonParser.parseString(String.valueOf(order.getOrderDate())));
        object.addProperty("OID", order.getOID());
        object.addProperty("Total", order.getTotal());
        object.addProperty("Tax", order.getTax());
        object.addProperty("ShippingCost", order.getShippingCost());
        object.addProperty("Perishable", order.isPerishable());
        object.addProperty("HasGarments", order.hasGarments());
        object.add("Boxes", JsonParser.parseString(gson.toJson(order.getBoxes(), new TypeToken<List<Boxes.Box>>(){}.getType())));
        ArrayList<Item> defaultItems = new ArrayList<>();
        ArrayList<PerishableItem> perishableItems = new ArrayList<>();
        ArrayList<Garment> garments = new ArrayList<>();
        for (Item item : order.getItems()){
            if (item.getType() == 0){
                defaultItems.add(item);
            } else if (item.getType() == 1){
                perishableItems.add((PerishableItem) item);
            } else{
                garments.add((Garment) item);
            }
        }
        StringBuilder json = new StringBuilder().append("[");
        boolean first = true;
        for (Item item : defaultItems){
            if(first){
                json.append(gsonItem.toJson(item, Item.class));
                first = false;
            } else {
                json.append(", ").append(gsonItem.toJson(item, Item.class));
            }
        }
        json.append("]");
        object.add("DefaultItems", JsonParser.parseString(json.toString()));
        json = new StringBuilder().append("[");
        first = true;
        for (PerishableItem item : perishableItems){
            if(first){
                json.append(gsonPerishableItem.toJson(item, PerishableItem.class));
                first = false;
            } else {
                json.append(", ").append(gsonPerishableItem.toJson(item, PerishableItem.class));
            }
        }

        json.append("]");
        object.add("PerishableItems", JsonParser.parseString(json.toString()));
        json = new StringBuilder().append("[");
        first = true;
        for (Garment item : garments){
            if(first){
                json.append(gsonGarment.toJson(item, Garment.class));
                first = false;
            } else {
                json.append(", ").append(gsonGarment.toJson(item, Garment.class));
            }

        }
        json.append("]");
        object.add("Garments", JsonParser.parseString(json.toString()));
        object.add("Carrier", JsonParser.parseString(gson.toJson(order.getCarrier(), new TypeToken<Carriers.Carrier>(){}.getType())));
        return object;
    }
}
