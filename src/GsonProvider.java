import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class GsonProvider {
    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter()) // Регистрируем адаптер для Duration
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                // Добавляем адаптер для LocalDateTime
                .create(); // Создаём и возвращаем экземпляр Gson
    }
}
