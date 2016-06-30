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
import database.databaseRows.TimeRow;
import database.statements.DeleteStatements;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.tasks.table.ButtonColumn;

public class TimeDeletionPanel extends AbstractTaskChangerGridBagJPanel implements SelectStatements, DeleteStatements {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel newPanel = new JPanel();

	private JComboBox<String> tasksComboBox = new JComboBox<String>();
	private JComboBox<String> subTasksComboBox = new JComboBox<String>();
	private JTable subTaskTimesTable;

	public static void main(String[] args) {

		CustomJFrame frame = new CustomJFrame();
		frame.add(new TimeDeletionPanel());
	}

	public TimeDeletionPanel() {
		super();
		updateTaskComboBox();

		updateTable();
		this.setLayout(gbl);

		newPanel.setLayout(gbl);
		GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		tasksComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubTaskComboBox();
				updateTable();
				subTaskTimesTable.repaint();
			}
		});
		subTasksComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
				subTaskTimesTable.repaint();
			}

		});
		gbc.weighty = 0;
		newPanel.add(tasksComboBox, gbc);

		gbc.gridy++;
		newPanel.add(subTasksComboBox, gbc);
		gbc.gridy++;

		gbc.weighty = 1;
		newPanel.add(subTaskTimesTable, gbc);

		this.add(newPanel, GridBagConstraintsFactory.buildConstraints());

	}

	protected void updateTable() {

		String[] columnNames = new String[] { "ID", "StartTime", "EndTime", "Delete" };
		String taskName = (String) tasksComboBox.getSelectedItem();
		String subTaskName = (String) subTasksComboBox.getSelectedItem();
		for (TaskRow tr : TaskChooserPanel.getTasksList()) {
			if (tr.getName().equalsIgnoreCase(taskName)) {

				Connection dbCon = DatabaseConnector.getConnection();

				if (subTaskTimesTable != null) {
					Object[][] blank = new Object[0][0];

					subTaskTimesTable.setModel(new DefaultTableModel(blank, columnNames));
					subTaskTimesTable.repaint();
				}

				for (int i = 0; i < tr.getSubTasks().size(); i++) {
					if (tr.getSubTasks().get(i).getName().equalsIgnoreCase(subTaskName)) {

						ArrayList<TimeRow> timesList = new ArrayList<TimeRow>();
						try {
							PreparedStatement ps = dbCon.prepareStatement(selectTimesForSubTask);
							ps.setInt(1, tr.getSubTasks().get(i).getId());
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								timesList.add(new TimeRow(rs.getInt("id"), rs.getInt("sub_task_id"), rs.getTimestamp("start_time"),
										rs.getTimestamp("end_time")));

							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Object[][] objectSubTaskTimes = new Object[timesList.size()][4];
						columnNames = new String[] { "ID", "StartTime", "EndTime", "Delete" };

						for (int j = 0; j < timesList.size(); j++) {
							objectSubTaskTimes[j][0] = timesList.get(j).getId();
							objectSubTaskTimes[j][1] = timesList.get(j).getStartTime();
							objectSubTaskTimes[j][2] = timesList.get(j).getEndTime();
							objectSubTaskTimes[j][3] = "Delete";
						}

						if (subTaskTimesTable == null)
							subTaskTimesTable = new JTable(new DefaultTableModel(objectSubTaskTimes, columnNames));
						else
							subTaskTimesTable.setModel(new DefaultTableModel(objectSubTaskTimes, columnNames));

						final TableColumnModel columnModel = subTaskTimesTable.getColumnModel();

						for (int column = 0; column < subTaskTimesTable.getColumnCount(); column++) {
							int width = 0; // Min width
							for (int row = 0; row < subTaskTimesTable.getRowCount(); row++) {

								TableCellRenderer renderer = subTaskTimesTable.getCellRenderer(row, column);
								Component comp = subTaskTimesTable.prepareRenderer(renderer, row, column);
								width = Math.max(comp.getPreferredSize().width + 50, width);

							}
							columnModel.getColumn(column).setPreferredWidth(width);
						}

					}
				}
				DeleteSubTaskTimeAction delete = new DeleteSubTaskTimeAction(taskName, subTaskName);
				new ButtonColumn(subTaskTimesTable, delete, 3);
				subTaskTimesTable.repaint();
			}

		}

	}

	protected void updateTaskComboBox() {
		synchronized (this) {
			tasksComboBox.removeAllItems();
			for (TaskRow tr : TaskChooserPanel.getTasksList()) {
				tasksComboBox.addItem(tr.getName());
			}
			updateSubTaskComboBox();
		}
	}

	private void updateSubTaskComboBox() {
		subTasksComboBox.removeAllItems();
		String taskName = (String) tasksComboBox.getSelectedItem();
		for (TaskRow tr : TaskChooserPanel.getTasksList())
			if (tr.getName().equalsIgnoreCase(taskName))
				for (SubTaskRow str : tr.getSubTasks())
					subTasksComboBox.addItem(str.getName());

	}

	private class DeleteSubTaskTimeAction extends AbstractAction {

		private String taskName;
		private String subTaskName;

		public DeleteSubTaskTimeAction(String taskName, String subTaskName) {
			super();
			this.taskName = taskName;
			this.subTaskName = subTaskName;

		}

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

			Connection dbCon = DatabaseConnector.getConnection();

			ArrayList<TimeRow> timesList = null;
			for (TaskRow tr : TaskChooserPanel.getTasksList()) {
				if (tr.getName().equalsIgnoreCase(taskName)) {
					for (SubTaskRow str : tr.getSubTasks())
						if (str.getName().equalsIgnoreCase(subTaskName)) {

							timesList = new ArrayList<TimeRow>();
							try {
								PreparedStatement ps = dbCon.prepareStatement(selectTimesForSubTask);
								ps.setInt(1, str.getId());
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
									timesList.add(new TimeRow(rs.getInt("id"), rs.getInt("sub_task_id"), rs.getTimestamp("start_time"),
											rs.getTimestamp("end_time")));

								}
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
					break;
				}
			}

			TimeRow timeRow = timesList.get(Integer.valueOf(e.getActionCommand()));

			Object[] options = { "Yes", "No" };
			int answer = JOptionPane.showOptionDialog(null,
					"Are you sure you want to delete Time ID " + timeRow.getId() + "?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (!(answer == JOptionPane.YES_OPTION))
				return;

			if (DEBUG)
				System.out.println("Deleting Time ID " + timeRow.getId());

			try {
				PreparedStatement ps = dbCon.prepareStatement(deleteTimeByID);
				ps.setInt(1, timeRow.getId());
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
