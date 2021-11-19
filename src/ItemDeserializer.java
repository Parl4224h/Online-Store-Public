import com.google.gson.*;

import java.lang.reflect.Type;

public class ItemDeserializer implements JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("Name").getAsString();
        int SKU = jsonObject.get("SKU").getAsInt();
        int stock = jsonObject.get("Stock").getAsInt();
        double width = jsonObject.get("Width").getAsDouble();
        double height = jsonObject.get("Height").getAsDouble();
        double length = jsonObject.get("Length").getAsDouble();
        double weight = jsonObject.get("Weight").getAsDouble();
        double price = jsonObject.get("Price").getAsDouble();
        return new Item(name, SKU, stock, width, height, length, weight, price);
    }
}
