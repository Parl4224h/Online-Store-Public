import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class CustomerDeserializer implements JsonDeserializer<Customer> {
    @Override
    public Customer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gsonOrder = new GsonBuilder().registerTypeAdapter(Order.class, new OrderDeserializer()).create();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int UID = jsonObject.get("UserID").getAsInt();
        String userName = jsonObject.get("UserName").getAsString();
        String password = jsonObject.get("Password").getAsString();
        String fName = jsonObject.get("FirstName").getAsString();
        String lName = jsonObject.get("LastName").getAsString();
        String address = jsonObject.get("Address").getAsString();
        long ccNumber = jsonObject.get("CCNumber").getAsLong();
        int CVC = jsonObject.get("CVC").getAsInt();
        ArrayList<Order> pastOrders = new ArrayList<>();
        JsonArray jsonArray = jsonObject.getAsJsonArray("PastOrders");
        JsonObject jsonObject1;
        for(JsonElement jsonElement1 : jsonArray){
            if(!jsonElement1.isJsonNull()) {
                pastOrders.add(gsonOrder.fromJson(jsonElement1, Order.class));
            }
        }
        LocalDate DOB = LocalDate.parse(jsonObject.get("DOB").getAsString());
        LocalDate ccExpiration = LocalDate.parse(jsonObject.get("CCExpiration").getAsString());
        double distance = jsonObject.get("Distance").getAsDouble();
        return new Customer(fName, lName, address, userName, password, UID, ccNumber, CVC, pastOrders, DOB, ccExpiration, distance);
    }
}
