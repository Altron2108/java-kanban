import com.google.gson.*;

import java.lang.reflect.Type;

public class TaskTypeAdapter implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String taskType = jsonObject.get("type").getAsString();

        if (taskType.equals("RegularTask")) {
            return context.deserialize(jsonObject, RegularTask.class);
            // Добавьте другие подклассы, если необходимо
        }
        throw new JsonParseException("Unknown type: " + taskType);
    }
}
