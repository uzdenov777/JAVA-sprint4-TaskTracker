public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("task1", "task1task1", manager.getNewId(), StatusTask.NEW);
        Task task2 = new Task("task2", "task2task2", manager.getNewId(),  StatusTask.NEW);
        Epic epic1 = new Epic("epic1", "epic1epic1", manager.getNewId(),  StatusTask.NEW);
        Epic epic2 = new Epic("epic2", "epic2epic2", manager.getNewId(),  StatusTask.NEW);
        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1", manager.getNewId(),  StatusTask.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2", manager.getNewId(),  StatusTask.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3", manager.getNewId(),  StatusTask.NEW, epic2.getId());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        System.out.println(manager.getEpic(epic1.getId()));
        System.out.println(manager.getListSubtasks(epic1.getId()));
        System.out.println(" ");
        Subtask subtask4 = new Subtask("subtask4", "subtask4subtask4", subtask2.getId(),  StatusTask.NEW, epic1.getId());
        Subtask subtask5 = new Subtask("subtask4", "subtask4subtask4", subtask1.getId(),  StatusTask.NEW, epic1.getId());
        manager.updateSubtask(subtask4);
        manager.updateSubtask(subtask5);
        System.out.println(manager.getEpic(epic1.getId()));
        System.out.println(manager.getListSubtasks(epic1.getId()));
        System.out.println(" ");
        System.out.println(manager.getHistory());
    }
}