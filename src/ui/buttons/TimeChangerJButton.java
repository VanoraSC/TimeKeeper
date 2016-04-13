package ui.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JButton;

import classes.listeners.ListenerConstants;
import classes.listeners.times.TimeChangeListener;
import classes.listeners.times.TimeChangeNotifer;
import ui.UIConstants;

public abstract class TimeChangerJButton extends JButton implements TimeChangeNotifer, ListenerConstants, UIConstants {

	private static final long serialVersionUID = 1L;

	protected int buttonType;

	private ArrayList<TimeChangeListener> listeners = new ArrayList<TimeChangeListener>();

	private final Object MUTEX = new Object();

	public TimeChangerJButton(int buttonType) {
		super();
		this.buttonType = buttonType;

		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (DEBUG) {
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					Date now = new Date(System.currentTimeMillis());

					System.out.println(now.toString());
					System.out.println(ts.toString());
				}
				notifyListeners(buttonType);
			}
		});
	}

	@Override
	public void notifyListeners(int buttonType) {
		synchronized (MUTEX) {
			for (TimeChangeListener tcl : listeners)
				tcl.timeChangeUpdate(buttonType);
		}
	}

	@Override
	public void register(TimeChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (!listeners.contains(tcl))
					listeners.add(tcl);
			}

	}

	@Override
	public void unRegister(TimeChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (listeners.contains(tcl))
					listeners.remove(tcl);
			}

	}

}
