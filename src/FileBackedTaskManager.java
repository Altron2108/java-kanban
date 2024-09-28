import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

// Наследуем FileBackedTaskManager от InMemoryTaskManager для переиспользования существующей логики
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    // Конструктор с указанием файла для автосохранения
    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод для сохранения состояния менеджера в файл
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            // Сохранение всех задач
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }

            // Сохранение всех эпиков
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }

            // Сохранение всех подзадач
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask));
                writer.newLine();
            }

            // Сохранение истории просмотра
            writer.newLine();
            writer.write(historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл.", e);
        }
    }

    // Метод для загрузки менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            boolean isHistory = false;

            for (String line : lines) {
                if (line.isEmpty()) {
                    isHistory = true;
                    continue;
                }
                if (!isHistory) {
                    if (!line.startsWith("id,")) { // Пропуск заголовка
                        Task task = fromString(line);
                        switch (task.getType()) {
                            case Task:
                                manager.createTask(task);
                                break;
                            case Epic:
                                manager.createEpic((Epic) task);
                                break;
                            case Subtask:
                                manager.createSubtask((Subtask) task);
                                break;
                        }
                    }
                } else {
                    List<Integer> historyIds = historyFromString(line);
                    for (int id : historyIds) {
                        if (manager.getTaskById(id) != null) {
                            manager.getHistoryManager().add(manager.getTaskById(id));
                        } else if (manager.getEpicById(id) != null) {
                            manager.getHistoryManager().add(manager.getEpicById(id));
                        } else if (manager.getSubtaskById(id) != null) {
                            manager.getHistoryManager().add(manager.getSubtaskById(id));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла.", e);
        }
        return manager;
    }

    // Преобразование задачи в строку CSV
    private String toString(Task task) {
        String epicId = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                epicId);
    }

    // Восстановление задачи из строки CSV
    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        return switch (type) {
            case Task -> new Task(id, name, description, status);
            case Epic -> new Epic(name, description);
            case Subtask -> {
                int epicId = Integer.parseInt(fields[5]);
                yield new Subtask(id, name, description, status, epicId);
            }
        };
    }

    // Преобразование истории в строку CSV
    private String historyToString() {
        return getHistoryManager().getHistory().stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    // Восстановление истории из строки CSV
    private static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    // Переопределяем методы изменения состояния менеджера, чтобы добавлять вызов save()
    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
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
