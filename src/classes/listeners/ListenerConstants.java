package classes.listeners;

public interface ListenerConstants {

	final static int TIME_COMMIT = 3;
	final static int TIME_PAUSE = 2;
	final static int TIME_START = 0;
	final static int TIME_STOP = 1;

	public enum MainPanelState {
		paused, started, stopped;
	}
}
