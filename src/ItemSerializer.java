import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ItemSerializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(Item item, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("SKU", item.getSKU());
        object.addProperty("Stock", item.getStock());
        object.addProperty("Width", item.getWidth());
        object.addProperty("Height", item.getHeight());
        object.addProperty("Length", item.getLength());
        object.addProperty("Weight", item.getWeight());
        object.addProperty("Price", item.getPrice());
        object.addProperty("Name", item.getName());
        return object;
    }
}
