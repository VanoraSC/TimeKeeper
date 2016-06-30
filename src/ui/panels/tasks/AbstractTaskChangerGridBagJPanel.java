package ui.panels.tasks;

import java.util.ArrayList;

import classes.listeners.tasks.TaskChangeListener;
import classes.listeners.tasks.TaskChangeNotifer;
import ui.UIConstants;
import ui.panels.AbstractGridBagJPanel;

public abstract class AbstractTaskChangerGridBagJPanel extends AbstractGridBagJPanel
		implements UIConstants, TaskChangeNotifer {

	private ArrayList<TaskChangeListener> listeners = new ArrayList<TaskChangeListener>();

	private final Object MUTEX = new Object();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notifyListeners() {
		synchronized (MUTEX) {
			for (TaskChangeListener tcl : listeners)
				tcl.taskChangeUpdate();
		}
	}

	@Override
	public void register(TaskChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (!listeners.contains(tcl))
					listeners.add(tcl);
			}

	}

	@Override
	public void unRegister(TaskChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (listeners.contains(tcl))
					listeners.remove(tcl);
			}

	}

}
