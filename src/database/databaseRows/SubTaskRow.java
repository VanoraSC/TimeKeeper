package database.databaseRows;

public class SubTaskRow implements Comparable<SubTaskRow> {
    private int id, parentID;
    private String name;

    public SubTaskRow(int id, int parentID, String name) {
	this.setId(id);
	this.setName(name);
	this.setParentID(parentID);
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public int getParentID() {
	return parentID;
    }

    private void setId(int id) {
	this.id = id;
    }

    private void setName(String name) {
	this.name = name;
    }

    private void setParentID(int parentID) {
	this.parentID = parentID;
    }

    @Override
    public int compareTo(SubTaskRow o) {
	if (this.id > o.id)
	    return 1;
	else if (this.id < o.id)
	    return -1;
	else
	    return 0;
    }
}
