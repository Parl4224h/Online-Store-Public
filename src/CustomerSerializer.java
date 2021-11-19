import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomerSerializer implements JsonSerializer<Customer> {
    @Override
    public JsonElement serialize(Customer customer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        Gson gsonOrder = new GsonBuilder().registerTypeAdapter(Order.class, new OrderSerializer()).create();
        object.addProperty("UserID", customer.getUserID());
        object.addProperty("UserName", customer.getUserName());
        object.addProperty("Password", customer.getPassword());
        object.addProperty("FirstName", customer.getFirstName());
        object.addProperty("LastName", customer.getLastName());
        object.addProperty("Address", customer.getAddress());
        object.addProperty("CCNumber", customer.getCcNumber());
        object.addProperty("CVC", customer.getCVC());
        object.addProperty("Distance", customer.getDistance());
        StringBuilder json = new StringBuilder("[");
        for (Order order : customer.getPastOrders()){
            json.append(", ").append(gsonOrder.toJson(order, Order.class));
        }
        json.append("]");
        object.add("PastOrders", JsonParser.parseString(json.toString()));
        object.add("DOB", JsonParser.parseString(String.valueOf(customer.getDOB())));
        object.add("CCExpiration", JsonParser.parseString(String.valueOf(customer.getCcExpiration())));
        return object;
    }
}
