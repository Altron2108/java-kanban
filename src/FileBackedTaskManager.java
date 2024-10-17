import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filePath;

    public FileBackedTaskManager(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : tasks.values()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(taskToString(subtask));
                writer.newLine();
            }
            writer.newLine();
            // Сохранение истории в формате id,id,id,...
            List<Task> history = historyManager.getHistory();
            String historyLine = history.stream()
                    .map(task -> String.valueOf(task.id))
                    .collect(Collectors.joining(","));
            writer.write(historyLine);
        }
    }

    @Override
    public void load() throws IOException, ManagerSaveException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Пропускаем заголовок
            if (line == null || !line.equals("id,type,name,status,description,duration,startTime,epic")) {
                throw new ManagerSaveException("Некорректный формат файла.");
            }
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] fields = parseLine(line);
                if (fields.length < 7) {
                    throw new ManagerSaveException("Некорректные данные задачи: " + line);
                }
                int id = Integer.parseInt(fields[0]);
                String type = fields[1];
                String name = fields[2];
                Status status;
                try {
                    status = Status.valueOf(fields[3]);
                } catch (IllegalArgumentException e) {
                    throw new ManagerSaveException("Неизвестный статус задачи: " + fields[3]);
                }
                String description = fields[4];
                Duration duration = fields[5].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(fields[5]));
                LocalDateTime startTime = fields[6].isEmpty() ? null : LocalDateTime.parse(fields[6]);

                Task task;
                switch (type) {
                    case "TASK":
                        task = new Task(id, name, description, status, duration, startTime);
                        if (isTaskTimeIntersect(task)) {
                            throw new ManagerSaveException("Задача пересекается с существующими задачами.");
                        }
                        tasks.put(id, task);
                        if (startTime != null) {
                            prioritizedTasks.add(task);
                        }
                        break;

                    case "EPIC":
                        Epic epic = new Epic(id, name, description);
                        epics.put(id, epic);
                        break;

                    case "SUBTASK":
                        if (fields.length < 8) {
                            throw new ManagerSaveException("Подзадача должна иметь поле epicId.");
                        }
                        int epicId = Integer.parseInt(fields[7]);
                        if (!epics.containsKey(epicId)) {
                            throw new ManagerSaveException("Epic с id " + epicId + " не найден для подзадачи.");
                        }
                        Subtask subtask = new Subtask(id, name, description, status, epicId, duration, startTime);
                        if (isTaskTimeIntersect(subtask)) {
                            throw new ManagerSaveException("Задача пересекается с существующими задачами.");
                        }
                        subtasks.put(id, subtask);
                        epics.get(epicId).addSubtask(id);
                        if (startTime != null) {
                            prioritizedTasks.add(subtask);
                        }
                        break;

                    default:
                        throw new ManagerSaveException("Неизвестный тип задачи: " + type);
                }
            }

            // После загрузки всех задач, обновляем эпики
            for (Epic epic : epics.values()) {
                List<Subtask> epicSubtasks = getEpicSubtasks(epic.id);
                epic.updateFields(epicSubtasks);
            }

            // Загрузка истории
            if ((line = reader.readLine()) != null) {
                String[] historyIds = line.split(",");
                for (String idStr : historyIds) {
                    if (idStr.isEmpty()) continue;
                    int id = Integer.parseInt(idStr);
                    Task task = tasks.get(id);
                    if (task == null) {
                        task = epics.get(id);
                    }
                    if (task == null) {
                        task = subtasks.get(id);
                    }
                    if (task != null) {
                        historyManager.add(task);
                    }
                }
            }
        }
    }

    private String[] parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }

    // Метод для проверки пересечения времени задач
    private boolean isTaskTimeIntersect(Task newTask) {
        for (Task existingTask : prioritizedTasks) {
            if (existingTask.startTime != null && newTask.startTime != null) {
                boolean isOverlapping = newTask.startTime.isBefore(existingTask.startTime.plus(existingTask.duration)) &&
                        existingTask.startTime.isBefore(newTask.startTime.plus(newTask.duration));
                if (isOverlapping) {
                    return true;  // Если есть пересечение, возвращаем true
                }
            }
        }
        return false;  // Если пересечений нет, возвращаем false
    }
}
