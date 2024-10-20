import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Сериализация задач
    public void save()  {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,duration,startTime,epicId\n"); // Добавим новые поля
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        System.err.println("Ошибка при сохранении задач в файл" + ": " + e.getMessage());
    }

    // Де сериализация задач
    public void load() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Пропускаем заголовок
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    createSubtask((Subtask) task);
                } else {
                    createTask(task);
                }
            }
        }
    }

    // Преобразование задачи в строку для сохранения в файл
    private String toString(Task task) {
        StringBuilder taskString = new StringBuilder(String.join(",",
                String.valueOf(task.getId()),
                task.getType().name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                String.valueOf(task.getDuration().toMinutes()), // duration в минутах
                task.getStartTime().toString()));

        if (task instanceof Subtask subtask) {
            taskString.append(",").append(subtask.getEpicId()); // Добавляем ID эпика для подзадачи
        }

        return taskString.toString();
    }

    // Преобразование строки в объект Task
    private Task fromString(String line) {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = LocalDateTime.parse(fields[6]);

        Task task = switch (type) {
            case Task ->
                    new RegularTask(title, description, status, duration, startTime); // Пример использования подкласса
            case Epic -> new Epic(title, description);
            case Subtask -> {
                int epicId = Integer.parseInt(fields[7]); // Дополнительный аргумент - ID эпика
                yield new Subtask(title, description, status, duration, startTime, epicId);
            }
        };
        task.setId(id);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}