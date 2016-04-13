package ui.buttons;

import java.util.ArrayList;

import classes.listeners.times.TimeChangeListener;

public class StopButton extends TimeChangerJButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		StopButton sb = new StopButton();

		sb.doClick();
	}

	private ArrayList<TimeChangeListener> listeners = new ArrayList<TimeChangeListener>();

	private final Object MUTEX = new Object();

	public StopButton() {
		super(TIME_STOP);
		this.setText("Stop");

	}
}
