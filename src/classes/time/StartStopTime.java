package classes.time;

import java.sql.Timestamp;

public class StartStopTime {
	private Timestamp startTime;
	private Timestamp stopTime;

	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getStopTime() {
		return stopTime;
	}

	public StartStopTime(Timestamp startTime, Timestamp stopTime) {
		this.startTime = startTime;
		this.stopTime = stopTime;

	}
}
