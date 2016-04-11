package database.statements;

public interface SelectStatements {

    static final String selectSubTasksForTask = "select sub_tasks.id as sub_task_id,tasks.id as tasks_id, sub_tasks.name from time_card.sub_tasks as sub_tasks join time_card.tasks as tasks on tasks.id = sub_tasks.parent where tasks.id = ?";
    static final String selectTasks = "select id, name from time_card.tasks";
    static final String selectTimesForSubTask = "SELECT start_time, id, end_time, sub_task_id FROM time_card.times where sub_task_id = ?";
}
