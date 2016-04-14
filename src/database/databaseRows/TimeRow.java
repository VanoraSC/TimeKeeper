package database.databaseRows;

import java.sql.Timestamp;

public class TimeRow {
	private int id, subTaskID;
	private Timestamp startTime, endTime;

	public TimeRow(int id, int sub_task_id, Timestamp start_time, Timestamp end_time) {
		this.setId(id);
		this.setSubtaskID(sub_task_id);
		this.setStartTime(start_time);
		this.setEndTime(end_time);
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public int getId() {
		return id;
	}

	public int getSubTaskID() {
		return subTaskID;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	private void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setSubtaskID(int parentID) {
		this.subTaskID = parentID;
	}

	private void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: " + this.id + "\n");
		sb.append("SubTask ID: " + this.subTaskID + "\n");
		sb.append("Start Time:" + this.startTime.toString() + "\n");
		sb.append("End Time: " + this.endTime.toString() + "\n");
		return sb.toString();
	}
}
