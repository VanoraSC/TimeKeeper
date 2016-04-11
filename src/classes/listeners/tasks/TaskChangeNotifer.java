package classes.listeners.tasks;

public interface TaskChangeNotifer {

    public abstract void notifyListeners();

    public abstract void register(TaskChangeListener tcl);

    public abstract void unRegister(TaskChangeListener tcl);

}
