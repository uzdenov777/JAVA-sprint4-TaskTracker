import java.util.List;

interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

}
