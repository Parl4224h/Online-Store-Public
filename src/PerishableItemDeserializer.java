import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class PerishableItemDeserializer implements JsonDeserializer<PerishableItem> {

    @Override
    public PerishableItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("Name").getAsString();
        int SKU = jsonObject.get("SKU").getAsInt();
        int stock = jsonObject.get("Stock").getAsInt();
        double width = jsonObject.get("Width").getAsDouble();
        double height = jsonObject.get("Height").getAsDouble();
        double length = jsonObject.get("Length").getAsDouble();
        double weight = jsonObject.get("Weight").getAsDouble();
        double price = jsonObject.get("Price").getAsDouble();
        LocalDate expirationDate = LocalDate.parse(jsonObject.get("ExpirationDate").getAsString());
        boolean isDonatable = jsonObject.get("IsDonatable").getAsBoolean();
        return new PerishableItem(name, SKU, stock, width, height, length, weight, price, expirationDate, isDonatable);
    }
}
