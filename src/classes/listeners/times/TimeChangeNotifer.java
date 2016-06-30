package classes.listeners.times;

public interface TimeChangeNotifer {

	public abstract void notifyListeners(int message);

	public abstract void register(TimeChangeListener tcl);

	public abstract void unRegister(TimeChangeListener tcl);

}
