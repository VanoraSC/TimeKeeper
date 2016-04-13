package database.statements;

public interface InsertStatements {

	final static String insertSubTask = "INSERT INTO time_card.sub_tasks(parent, name) VALUES (?,?)";
	final static String insertTask = "INSERT INTO time_card.tasks(name) VALUES (?)";
	final static String insertTime = "INSERT INTO time_card.times(start_time,end_time,sub_task_id) VALUES (?,?,?)";

}
