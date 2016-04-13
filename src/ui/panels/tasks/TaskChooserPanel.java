package ui.panels.tasks;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import classes.listeners.tasks.TaskChangeListener;
import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.UIConstants;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.AbstractGridBagJPanel;

public class TaskChooserPanel extends AbstractGridBagJPanel
		implements SelectStatements, UIConstants, TaskChangeListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		CustomJFrame frame = new CustomJFrame();
		TaskChooserPanel tf = new TaskChooserPanel();

		frame.add(tf);

	}

	private JPanel panelForComboBoxes = new JPanel();

	private JComboBox<String> subTasksComboBox = new JComboBox<String>();

	private JComboBox<String> tasksComboBox = new JComboBox<String>();

	private static ArrayList<TaskRow> tasksList = new ArrayList<TaskRow>();

	public static ArrayList<TaskRow> getTasksList() {

		refreshDatabase();
		return tasksList;
	}

	public TaskChooserPanel() {

		super();

		refreshDatabase();

		updateComboBoxes();
		addListenersToComboBoxes();

		GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();

		gbl.setConstraints(this, gbc);
		gbl.setConstraints(panelForComboBoxes, gbc);

		panelForComboBoxes.setLayout(gbl);

		gbc.gridy = 0;
		gbc.gridx = LABEL_X;
		gbc.weightx = LABEL_WEIGHT;
		panelForComboBoxes.add(new JLabel(PRIMARY_TASK), gbc);
		gbc.gridx = COMBO_BOX_X;
		gbc.weightx = COMBO_BOX_WEIGHT;
		panelForComboBoxes.add(tasksComboBox, gbc);

		gbc.gridy++;
		gbc.gridx = LABEL_X;
		gbc.weightx = LABEL_WEIGHT;
		panelForComboBoxes.add(new JLabel(SUB_TASK), gbc);
		gbc.gridx = COMBO_BOX_X;
		gbc.weightx = COMBO_BOX_WEIGHT;
		panelForComboBoxes.add(subTasksComboBox, gbc);
		gbc.gridx = 0;
		gbc.gridy++;

		JPanel containerPanel = new JPanel();
		GridBagConstraints gbc2 = GridBagConstraintsFactory.buildConstraints();

		gbc2.gridx = gbc2.gridy = 0;

		containerPanel.setLayout(gbl);
		containerPanel.add(panelForComboBoxes, gbc2);

		this.add(containerPanel, gbc);
		this.setMinimumSize(this.getPreferredSize());
		this.setSize(300, 200);

	}

	private void addListenersToComboBoxes() {

		tasksComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				updateComboBoxes();

			}
		});

	}

	public void disableComboBoxes() {
		tasksComboBox.setEnabled(false);
		subTasksComboBox.setEnabled(false);
	}

	public void enableComboBoxes() {
		tasksComboBox.setEnabled(true);
		subTasksComboBox.setEnabled(true);
	}

	public String getCurrentSubTaskName() {
		return (String) subTasksComboBox.getSelectedItem();
	}

	public String getCurrentTaskName() {
		return (String) tasksComboBox.getSelectedItem();
	}

	private static void refreshDatabase() {
		tasksList = new ArrayList<TaskRow>();
		if (DEBUG)
			System.out.println("refreshing data from database");
		Connection dbCon = DatabaseConnector.getConnection();
		PreparedStatement ps;
		try {
			ps = dbCon.prepareStatement(SelectStatements.selectTasks);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				TaskRow tr = new TaskRow(rs.getInt("id"), rs.getString("name"));

				PreparedStatement ps2 = dbCon.prepareStatement(selectSubTasksForTask);
				ps2.setInt(1, tr.getId());
				ResultSet rs2 = ps2.executeQuery();

				while (rs2.next()) {
					tr.AddSubTask(new SubTaskRow(rs2.getInt("sub_task_id"), rs2.getInt("tasks_id"), rs2.getString("name")));
				}
				tasksList.add(tr);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void taskChangeUpdate() {
		refreshDatabase();
		updateComboBoxes();

	}

	protected void updateComboBoxes() {
		synchronized (this) {
			if (tasksComboBox.getItemCount() != tasksList.size())
				tasksComboBox.removeAllItems();
			subTasksComboBox.removeAllItems();

			for (TaskRow tr : tasksList) {

				boolean found = false;
				for (int i = 0; i < tasksComboBox.getItemCount(); i++) {

					if (tasksComboBox.getItemAt(i).equalsIgnoreCase(tr.getName())) {
						found = true;
						break;
					}
				}
				if (!found)
					tasksComboBox.addItem(tr.getName());

				if (tr.getName().equalsIgnoreCase((String) tasksComboBox.getSelectedItem())) {

					if (DEBUG)
						System.out.println("Task: " + tr.getName() + " has " + tr.getSubTasks().size() + " sub tasks");
					for (SubTaskRow str : tr.getSubTasks()) {
						if (DEBUG)
							System.out.println("Adding " + str.getName());
						subTasksComboBox.addItem(str.getName());

					}

				}
			}
		}

	}

}
