package database.statements;

public interface DeleteStatements {

	final static String deleteSubTask = "DELETE from time_card.sub_tasks where id = ?";
	final static String deleteTask = "DELETE from time_card.tasks where id = ?";
	final static String deleteTimeBySubTaskID = "DELETE from time_card.times where sub_task_id = ?";
	final static String deleteTimeByID = "DELETE from time_card.times where id = ?";
}
