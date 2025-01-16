import java.util.HashMap;

class Epic extends Task {
    private HashMap<Integer, Subtask> subtaskArray = new HashMap<>();

    public Epic(String name, String description, int id, StatusTask status) {
        super(name, description, id, status);
    }

    public void addSubtask(Subtask subtask) {
        subtaskArray.put(subtask.getId(), subtask);
    }

    public HashMap<Integer, Subtask> getSubtasksArray() {
        return subtaskArray;
    }

}
