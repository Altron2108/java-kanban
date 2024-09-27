public class Subtask extends Task {
    private int epicId;

    // Конструктор без id, id будет установлен менеджером задач
    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    // Конструктор с id, используется в тестах или при загрузке из хранилища
    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    // Геттер и сеттер для epicId

    @Override
    public TaskType getType() {
        return TaskType.Subtask;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Переопределение метода toString для удобного вывода информации о подзадаче

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }


}