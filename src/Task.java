import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;

    // Конструктор без id, id будет установлен менеджером задач
    public Task(String title, String description, Status status) {
        this.id = -1; // ID будет присвоен менеджером задач
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Конструктор с id, используется в тестах или при загрузке из хранилища
    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }


    public TaskType getType() {
        return TaskType.Task; // Возвращаем тип задачи
    }
    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    // Только менеджер задач может устанавливать id
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    // Позволяет изменять заголовок задачи
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    // Позволяет изменять описание задачи
    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    // Позволяет изменять статус задачи
    public void setStatus(Status status) {
        this.status = status;
    }

    // Переопределение методов equals и hashCode для корректного сравнения объектов

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Переопределение метода toString для удобного вывода информации о задаче

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}