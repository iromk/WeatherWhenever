package pro.xite.dev.weatherwhenever.manage;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Roman Syrchin on 3/31/18.
 */
public class InterfaceAdapter<T> implements JsonSerializer, JsonDeserializer {

    private static final String TAG_CLASS_NAME = "TAG_CLASS_NAME";
    private static final String TAG_OBJECT_CONTENT = "TAG_OBJECT_CONTENT";

    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonObject.get(TAG_CLASS_NAME);
        String className = jsonPrimitive.getAsString();
        Class klass = getClassByString(className);
        return jsonDeserializationContext.deserialize(jsonObject.get(TAG_OBJECT_CONTENT), klass);
    }

    public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TAG_CLASS_NAME, jsonElement.getClass().getName());
        jsonObject.add(TAG_OBJECT_CONTENT, jsonSerializationContext.serialize(jsonElement));
        return jsonObject;
    }

    private Class getClassByString(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e("InterfaceAdapter", e.getLocalizedMessage());
            throw new JsonParseException(e.getMessage());
        }
    }
}
