package ui.panels.tasks;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.statements.DeleteStatements;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.tasks.table.ButtonColumn;

public class SubTaskDeletionPanel extends AbstractTaskChangerGridBagJPanel
	implements SelectStatements, DeleteStatements {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    JPanel newPanel = new JPanel();

    private JComboBox<String> tasksComboBox = new JComboBox<String>();
    private JTable subTaskTable;

    public static void main(String[] args) {

	CustomJFrame frame = new CustomJFrame();
	frame.add(new SubTaskDeletionPanel());
    }

    public SubTaskDeletionPanel() {
	super();
	updateComboBox();

	updateTable();
	this.setLayout(gbl);

	newPanel.setLayout(gbl);
	GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;

	tasksComboBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		updateTable();
		subTaskTable.repaint();
	    }
	});
	newPanel.add(tasksComboBox, gbc);
	gbc.gridy++;

	newPanel.add(subTaskTable, gbc);

	this.add(newPanel, GridBagConstraintsFactory.buildConstraints());

    }

    protected void updateTable() {

	String taskName = (String) tasksComboBox.getSelectedItem();
	for (TaskRow tr : TaskChooserPanel.getTasksList()) {
	    if (tr.getName().equalsIgnoreCase(taskName)) {

		Object[][] objectSubTasks = new Object[tr.getSubTasks()
			.size()][4];
		String[] columnNames = new String[] { "ID", "Name", "NumTimes",
			"Delete" };
		Connection dbCon = DatabaseConnector.getConnection();

		for (int i = 0; i < tr.getSubTasks().size(); i++) {

		    int timesCount = 0;
		    try {
			PreparedStatement ps = dbCon
				.prepareStatement(selectTimesForSubTask);
			ps.setInt(1, tr.getSubTasks().get(i).getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
			    timesCount++;

			}
		    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    objectSubTasks[i][0] = tr.getSubTasks().get(i).getId();
		    objectSubTasks[i][1] = tr.getSubTasks().get(i).getName();
		    objectSubTasks[i][2] = new Integer(timesCount);
		    objectSubTasks[i][3] = "Delete";

		}

		if (subTaskTable == null)
		    subTaskTable = new JTable(
			    new DefaultTableModel(objectSubTasks, columnNames));
		else
		    subTaskTable.setModel(
			    new DefaultTableModel(objectSubTasks, columnNames));

		final TableColumnModel columnModel = subTaskTable
			.getColumnModel();

		for (int column = 0; column < subTaskTable
			.getColumnCount(); column++) {
		    int width = 0; // Min width
		    for (int row = 0; row < subTaskTable.getRowCount(); row++) {

			TableCellRenderer renderer = subTaskTable
				.getCellRenderer(row, column);
			Component comp = subTaskTable.prepareRenderer(renderer,
				row, column);
			width = Math.max(comp.getPreferredSize().width + 50,
				width);

		    }
		    columnModel.getColumn(column).setPreferredWidth(width);
		}

	    }
	}
	DeleteSubTaskAction delete = new DeleteSubTaskAction(taskName);
	new ButtonColumn(subTaskTable, delete, 3);
	subTaskTable.repaint();
    }

    protected void updateComboBox() {
	synchronized (this) {
	    tasksComboBox.removeAllItems();
	    for (TaskRow tr : TaskChooserPanel.getTasksList()) {
		tasksComboBox.addItem(tr.getName());
	    }
	}
    }

    private class DeleteSubTaskAction extends AbstractAction {

	private String taskName;

	public DeleteSubTaskAction(String taskName) {
	    super();
	    this.taskName = taskName;

	}

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {

	    Connection dbCon = DatabaseConnector.getConnection();

	    ArrayList<SubTaskRow> subTasks = null;
	    for (TaskRow tr : TaskChooserPanel.getTasksList()) {
		if (tr.getName().equalsIgnoreCase(taskName)) {
		    subTasks = tr.getSubTasks();
		    break;
		}
	    }

	    SubTaskRow subTask = subTasks
		    .get(Integer.valueOf(e.getActionCommand()));

	    Object[] options = { "Yes", "No" };
	    int answer = JOptionPane.showOptionDialog(null,
		    "Are you sure you want to delete " + subTask.getName()
			    + "?",
		    "Confirm Delete", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	    if (!(answer == JOptionPane.YES_OPTION))
		return;

	    if (DEBUG)
		System.out.println("Deleting " + subTask.getName());

	    try {
		PreparedStatement ps = dbCon
			.prepareStatement(deleteTimeBySubTaskID);
		ps.setInt(1, subTask.getId());
		ps.executeUpdate();

		ps = dbCon.prepareStatement(deleteSubTask);
		ps.setInt(1, subTask.getId());
		ps.executeUpdate();
	    } catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }

	    notifyListeners();
	    updateTable();

	}

    }
}
