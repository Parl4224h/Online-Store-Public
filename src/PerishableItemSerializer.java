import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class PerishableItemSerializer implements JsonSerializer<PerishableItem> {
    @Override
    public JsonElement serialize(PerishableItem perishableItem, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("SKU", perishableItem.getSKU());
        object.addProperty("Stock", perishableItem.getStock());
        object.addProperty("Width", perishableItem.getWidth());
        object.addProperty("Height", perishableItem.getHeight());
        object.addProperty("Length", perishableItem.getLength());
        object.addProperty("Weight", perishableItem.getWeight());
        object.addProperty("Price", perishableItem.getPrice());
        object.addProperty("Name", perishableItem.getName());
        object.addProperty("IsDonatable", perishableItem.isDonatable());
        object.addProperty("ExpirationDate", perishableItem.getExpirationDateString());
        return object;
    }
}
