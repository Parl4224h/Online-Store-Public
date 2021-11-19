import com.google.gson.*;

import java.lang.reflect.Type;

public class GarmentSerializer implements JsonSerializer<Garment> {
    @Override
    public JsonElement serialize(Garment garment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("SKU", garment.getSKU());
        object.addProperty("Stock", garment.getStock());
        object.addProperty("Width", garment.getWidth());
        object.addProperty("Height", garment.getHeight());
        object.addProperty("Length", garment.getLength());
        object.addProperty("Weight", garment.getWeight());
        object.addProperty("Price", garment.getPrice());
        object.addProperty("Name", garment.getName());
        object.addProperty("Size", garment.getSize());
        return object;
    }
}
