import java.util.HashMap;

 enum StatusTask {
    NEW,
    IN_PROGRESS,
    DONE;

    public static void checkStatus(int epicId, HashMap<Integer, Epic> epics) {
        Epic epic = epics.get(epicId);
        HashMap<Integer, Subtask> subtasks = epic.getSubtasksArray();

        int countNew = 0;
        int countDone = 0;

        for (Subtask sub : subtasks.values()) {

            if (sub.getStatus() == StatusTask.NEW) {
                countNew++;
            } else if (sub.getStatus() == StatusTask.DONE) {
                countDone++;
            }
        }

        if (countNew == subtasks.size() || subtasks.isEmpty()) {
            epic.setStatus(StatusTask.NEW);
        } else if (countDone == subtasks.size()) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }
}
