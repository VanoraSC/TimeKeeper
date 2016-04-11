package database.databaseRows;

import java.util.ArrayList;

public class TaskRow implements Comparable<TaskRow> {

    private int id;
    private String name;
    private ArrayList<SubTaskRow> subTaskList = new ArrayList<SubTaskRow>();

    public TaskRow(int id, String name) {
	this.setId(id);
	this.setName(name);
    }

    public void AddSubTask(SubTaskRow str) {
	subTaskList.add(str);
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public ArrayList<SubTaskRow> getSubTasks() {
	return subTaskList;
    }

    private void setId(int id) {
	this.id = id;
    }

    private void setName(String name) {
	this.name = name;
    }

    @Override
    public int compareTo(TaskRow o) {
	if (this.id > o.id)
	    return 1;
	else if (this.id < o.id)
	    return -1;
	else
	    return 0;
    }
}
