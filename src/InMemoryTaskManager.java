import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>(); //Хранит задачи.
    private final HashMap<Integer, Epic> epics = new HashMap<>();//Хранит Epic.
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();//Хранит подзадачи.
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int countId = 0;

    @Override
    public int getNewId() {//Генерирует уникальный ID.
        countId++;
        return countId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getTasks() { //Получение списка всех задач
        List<Task> taskList = new ArrayList<>(tasks.values());
        return taskList;
    }

    @Override
    public List<Epic> getEpics() { //Получение списка всех Epic
        List<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }

    @Override
    public List<Subtask> getSubtasks() { //Получение списка всех подзадач
        List<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        return subtaskList;
    }

    @Override
    public void clearTasks() { //Удаление всех задач.
        tasks.clear();
    }

    @Override
    public void clearEpics() { //Удаление всех Epic.
        epics.clear();
        clearSubtasks();
    }

    @Override
    public void clearSubtasks() { //Удаление всех подзадач.
        subtasks.clear();
    }

    @Override
    public Optional<Task> getTask(int id) { //Получение Task по идентификатору.

        if (tasks.containsKey(id)) { //Проверяет если такой ID в задачах.
            historyManager.add(tasks.get(id));
        } else {
            System.out.println("Не существует такого ID Task");
        }
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Optional<Epic> getEpic(int id) { //Получение Epic по идентификатору.

        if (epics.containsKey(id)) { //Проверяет если такой ID в Epic.
            historyManager.add(epics.get(id));
        } else {
            System.out.println("Не существует такого ID Epic");
        }
        return Optional.ofNullable(epics.get(id));
    }

    @Override
    public Optional<Subtask> getSubtask(int id) { //Получение Subtask по идентификатору.

        if (subtasks.containsKey(id)) { //Проверяет если такой ID в подзадачах.
            historyManager.add(subtasks.get(id));
        } else {
            System.out.println("Не существует такого ID Subtask");
        }
        return Optional.ofNullable(subtasks.get(id));
    }

    @Override
    public void addTask(Task taskInput) { //Добавляет полученный объект Task в соответсвующий HashMap и проверяет, если такой ID уже.
        boolean isTaskExist = tasks.containsKey(taskInput.getId());// Проверяет на наличие задачи.

        if (isTaskExist) {
            System.out.println("Задача с таким ID уже создана");
        } else {
            tasks.put(taskInput.getId(), taskInput);
        }
    }

    @Override
    public void addEpic(Epic taskInput) { //Добавляет полученный объект Epic в соответсвующий HashMap и проверяет, если такой ID уже.
        boolean isEpicExist = epics.containsKey(taskInput.getId());//Проверяет на наличие Epic.

        if (isEpicExist) {
            System.out.println("Epic с таким ID уже создан");
        } else {
            epics.put(taskInput.getId(), taskInput);
        }
    }

    @Override
    public void addSubtask(Subtask taskInput) { //Добавляет полученный объект Subtask в соответсвующий HashMap и проверяет, если такой ID уже.
        boolean isSubtaskExist = subtasks.containsKey(taskInput.getId());

        if (isSubtaskExist) {
            System.out.println("Подзадача с таким ID уже создана");
        } else {
            boolean isEpicExist = epics.containsKey(taskInput.getId());//если есть Epic, к которому подзадача принадлежит.

            if (isEpicExist) {
                subtasks.put(taskInput.getId(), taskInput);
                epics.get((taskInput).getIdEpic()).
                        addSubtask(taskInput); //Добавляет подзадачу в список определенного Epic.
                StatusTask.checkStatus(taskInput.getIdEpic(), epics); //Проверяет статус Epic после добавления в него подзадачи.
            } else {
                System.out.println("Такого Epic не существует для добавления в него подзадачи");
            }
        }
    }

    @Override
    public void updateTask(Task taskInput) { // Обновление Task. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        boolean isTaskExist = tasks.containsKey(taskInput.getId());

        if (isTaskExist) {
            tasks.put(taskInput.getId(), taskInput);
        } else {
            System.out.println("Такой Задачи не существует для обновления");
        }
    }

    @Override
    public void updateEpic(Epic taskInput) { // Обновление Epic. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        boolean isEpicExist = epics.containsKey(taskInput.getId());

        if (isEpicExist) {
            epics.put(taskInput.getId(), taskInput);

            for (Subtask subtask : taskInput.getSubtasksArray().values()) {//Обновляю список всех существующих подзадач, после обновления Epic.
                subtasks.put(taskInput.getId(), subtask);
            }

        } else {
            System.out.println("Такого Epic не существует для обновления");
        }
    }

    @Override
    public void updateSubtask(Subtask taskInput) { // Обновление Subtask. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        boolean isSubtaskExist = subtasks.containsKey(taskInput.getId());

        if (isSubtaskExist) {
            subtasks.put(taskInput.getId(), taskInput);
            epics.get(taskInput.getIdEpic()).addSubtask(taskInput); //Добавляет подзадачу в список Epic.
            StatusTask.checkStatus(taskInput.getIdEpic(), epics); //Проверяет статус Epic после обновления его подзадачи.
        } else {
            System.out.println("Такой подзадачи не существует для обновления");
        }
    }

    @Override
    public void removeTaskById(int id) { //Удаление Task по идентификатору.
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) { //Удаление Epic по идентификатору и его подзадачи.
        epics.remove(id);
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getIdEpic() == id) {
                subtasks.remove(id);
            }
        }
    }

    @Override
    public void removeSubtaskById(int id) { //Удаление Subtask по идентификатору.
        int epicId = subtasks.get(id).getIdEpic();//сохраняет ID Epic пока не удалил Subtask.
        epics.get(epicId).getSubtasksArray().remove(id);//Удаление Subtask по идентификатору в самом Epic.
        subtasks.remove(id);
        StatusTask.checkStatus(epicId, epics);
    }

    @Override
    public HashMap<Integer, Subtask> getListSubtasks(int id) { //Получение списка всех подзадач определённого Epic.

        if (epics.containsKey(id)) {
            return epics.get(id).getSubtasksArray();
        }

        System.out.println("Такого Epic нету");
        return new HashMap<>();
    }


}