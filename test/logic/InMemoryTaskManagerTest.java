package logic;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}
