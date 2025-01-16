import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyTask = new ArrayList<>();//Хранит последние 10 просмотренных пользователем задач.

    @Override
    public void add(Task task) {//Проверяет, чтобы в historyTask было не больше 10 Задач
        if (historyTask.size() == 10) {
            historyTask.remove(0);
        }
        historyTask.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
