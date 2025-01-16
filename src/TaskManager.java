import java.util.HashMap;
import java.util.Optional;

class TaskManager {
    public HashMap<Integer, Task> tasks = new HashMap<>(); //Хранит задачи
    public HashMap<Integer, Epic> epics = new HashMap<>();//Хранит Epic
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();//Хранит подзадачи

    private int countId = 0;

    public int getNewId() {//Генерирует уникальный ID
        countId++;
        return countId;
    }

    public HashMap<Integer, Task> getTasks() { //Получение списка всех задач
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() { //Получение списка всех Epic
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() { //Получение списка всех подзадач
        return subtasks;
    }

    public void clearTasks() { //Удаление всех задач.
        tasks.clear();
    }

    public void clearEpics() { //Удаление всех Epic.
        epics.clear();
        clearSubtasks();
    }

    public void clearSubtasks() { //Удаление всех подзадач.
        subtasks.clear();
    }

    public Optional<Task> getTaskById(int id) { //Получение Task по идентификатору
        Optional<Task> getTask = Optional.empty();

        if (tasks.containsKey(id)) { //Проверяет ID, принадлежит задачам
            getTask = Optional.of(tasks.get(id));
        } else {
            System.out.println("Не существует такого ID Task");
        }
        return getTask;
    }

    public Optional<Epic> getEpicById(int id) { //Получение Epic по идентификатору
        Optional<Epic> getTask = Optional.empty();

        if (epics.containsKey(id)) { //Проверяет ID, принадлежит Epic
            getTask = Optional.of(epics.get(id));
        } else {
            System.out.println("Не существует такого ID Epic");
        }
        return getTask;
    }

    public Optional<Subtask> getSubtaskById(int id) { //Получение Subtask по идентификатору
        Optional<Subtask> getTask = Optional.empty();

        if (subtasks.containsKey(id)) { //Проверяет ID, принадлежит ли подзадачам
            getTask = Optional.of(subtasks.get(id));
        } else {
            System.out.println("Не существует такого ID Subtask");
        }
        return getTask;
    }

    public void addTask(Task taskInput) { //Добавляет полученный объект Task в соответсвующий HashMap и проверяет, если такой ID уже
        if (tasks.containsKey(taskInput.getId())) {// Проверяет на совместимость с задачей
            System.out.println("Задача с таким ID уже создана");
        } else {
            tasks.put(taskInput.getId(), taskInput);
        }
    }

    public void addEpic(Epic taskInput) { //Добавляет полученный объект Epic в соответсвующий HashMap и проверяет, если такой ID уже
        if (epics.containsKey(taskInput.getId())) {//Проверяет на совместимость с Epic
            System.out.println("Epic с таким ID уже создан");
        } else {
            epics.put(taskInput.getId(), taskInput);
        }
    }

    public void addSubtask(Subtask taskInput) { //Добавляет полученный объект Subtask в соответсвующий HashMap и проверяет, если такой ID уже
        if (subtasks.containsKey(taskInput.getId())) {
            System.out.println("Подзадача с таким ID уже создана");
        } else {
            if (epics.containsKey(((taskInput).getIdEpic()))) { //если есть Epic, к которому подзадача принадлежит
                subtasks.put(taskInput.getId(), taskInput);
                epics.get((taskInput).getIdEpic()).
                        addSubtask(taskInput); //Добавляет подзадачу в список определенного Epic
                StatusTask.checkStatus(taskInput.getIdEpic(), epics); //Проверяет статус Epic после добавления в него подзадачи
            } else {
                System.out.println("Такого Epic не существует для добавления в него подзадачи");
            }
        }
    }

    public void updateTask(Task taskInput) { // Обновление Task. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        if (tasks.containsKey(taskInput.getId())) {
            tasks.put(taskInput.getId(), taskInput);
        } else {
            System.out.println("Такой Задачи не существует для обновления");
        }
    }

    public void updateEpic(Epic taskInput) { // Обновление Epic. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        if (epics.containsKey(taskInput.getId())) {
            epics.put(taskInput.getId(), taskInput);

            for (Subtask subtask : taskInput.getSubtasksArray().values()) {//Обновляю список всех существующих подзадач, после обновления Epic
                subtasks.put(taskInput.getId(), subtask);
            }

        } else {
            System.out.println("Такого Epic не существует для обновления");
        }
    }

    public void updateSubtask(Subtask taskInput) { // Обновление Subtask. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        if (subtasks.containsKey(taskInput.getId())) {
            subtasks.put(taskInput.getId(), taskInput);
            epics.get(taskInput.getIdEpic()).addSubtask(taskInput); //Добавляет подзадачу в список Epic
            StatusTask.checkStatus(taskInput.getIdEpic(), epics); //Проверяет статус Epic после обновления его подзадачи
        } else {
            System.out.println("Такой подзадачи не существует для обновления");
        }
    }

    public void removeTaskById(int id) { //Удаление Task по идентификатору.
        tasks.remove(id);
    }

    public void removeEpicById(int id) { //Удаление Epic по идентификатору и его подзадачи.
        epics.remove(id);
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getIdEpic() == id) {
                subtasks.remove(id);
            }
        }
    }

    public void removeSubtaskById(int id) { //Удаление Subtask по идентификатору.
        int epicId = subtasks.get(id).getIdEpic();//сохраняет ID Epic пока не удалил Subtask
        epics.get(epicId).getSubtasksArray().remove(id);//Удаление Subtask по идентификатору в самом Epic
        subtasks.remove(id);
        StatusTask.checkStatus(epicId, epics);
    }

    public HashMap<Integer, Subtask> getListSubtasks(int id) { //Получение списка всех подзадач определённого Epic.

        if (epics.containsKey(id)) {
            return epics.get(id).getSubtasksArray();
        }

        System.out.println("Такого Epic нету");
        return new HashMap<>();
    }
}