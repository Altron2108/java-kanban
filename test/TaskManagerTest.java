import org.junit.jupiter.api.BeforeEach;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    public abstract void setUp();
}
