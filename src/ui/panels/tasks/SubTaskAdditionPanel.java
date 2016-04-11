package ui.panels.tasks;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.DatabaseConnector;
import database.databaseRows.TaskRow;
import database.statements.InsertStatements;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;

public class SubTaskAdditionPanel extends AbstractTaskChangerGridBagJPanel
	implements InsertStatements, SelectStatements {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

	CustomJFrame frame = new CustomJFrame();
	frame.add(new SubTaskAdditionPanel());

    }

    private JButton addSubTaskButton = new JButton(ADD_SUB_TASK);

    private JLabel newSubTaskNameLabel = new JLabel(NEW_SUB_TASK_NAME);
    private JTextField newSubTaskNameTextField = new JTextField();
    private JComboBox<String> taskComboBox = new JComboBox<String>();

    private ArrayList<TaskRow> taskRows = new ArrayList<TaskRow>();

    public SubTaskAdditionPanel() {
	super();

	initComponents();

	JPanel newTaskPanel = new JPanel();
	newTaskPanel.setLayout(gbl);

	GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();
	gbl.setConstraints(this, gbc);
	gbl.setConstraints(newTaskPanel, gbc);

	gbc.gridy = 0;
	gbc.gridx = LABEL_X;
	gbc.weightx = LABEL_WEIGHT;

	newTaskPanel.add(newSubTaskNameLabel, gbc);

	gbc.gridx = TEXT_FIELD_X;
	gbc.weightx = TEXT_FIELD_WEIGHT;

	newTaskPanel.add(newSubTaskNameTextField, gbc);
	newTaskPanel.setPreferredSize(new Dimension(
		(int) taskComboBox.getPreferredSize().getWidth() + 300,
		(int) taskComboBox.getPreferredSize().getHeight()));

	GridBagConstraints gbc2 = GridBagConstraintsFactory.buildConstraints();
	gbc2.gridx = 0;
	gbc2.gridy = 0;
	this.add(taskComboBox, gbc2);
	gbc2.gridy++;
	this.add(newTaskPanel, gbc2);
	gbc2.gridy++;
	this.add(addSubTaskButton, gbc2);

    }

    private void initComponents() {

	Connection dbCon = DatabaseConnector.getConnection();

	PreparedStatement ps;
	try {
	    ps = dbCon.prepareStatement(selectTasks);

	    ResultSet rs;

	    rs = ps.executeQuery();

	    while (rs.next()) {
		taskComboBox.addItem(rs.getString("name"));
		TaskRow tr = new TaskRow(rs.getInt("id"), rs.getString("name"));
		taskRows.add(tr);

	    }
	} catch (SQLException e1) {

	    e1.printStackTrace();
	}

	newSubTaskNameTextField.setPreferredSize(new Dimension(300, 0));

	addSubTaskButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

		if (newSubTaskNameTextField.getText().equalsIgnoreCase("")) {
		    JOptionPane.showMessageDialog(null,
			    "Insert Failed, Cannot Insert Empty Name!!!");
		    return;
		}

		Connection dbCon = DatabaseConnector.getConnection();

		PreparedStatement ps;
		int taskID = 0;
		try {
		    ps = dbCon.prepareStatement(selectSubTasksForTask);

		    for (TaskRow tr : taskRows) {
			if (tr.getName().equalsIgnoreCase(
				(String) taskComboBox.getSelectedItem())) {
			    taskID = tr.getId();
			    break;
			}
		    }

		    ps.setInt(1, taskID);

		    ResultSet rs;

		    rs = ps.executeQuery();

		    while (rs.next()) {
			if (rs.getString("name").equalsIgnoreCase(
				newSubTaskNameTextField.getText())) {
			    JOptionPane
				    .showMessageDialog(null,
					    "SubTask Already Exists for Task "
						    + (String) taskComboBox
							    .getSelectedItem()
						    + "!!!");

			    return;
			}
		    }
		} catch (SQLException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		try {
		    ps = dbCon.prepareStatement(insertSubTask);

		    ps.setInt(1, taskID);
		    ps.setString(2, newSubTaskNameTextField.getText());
		    if (ps.executeUpdate() != 1) {
			JOptionPane.showMessageDialog(null, "Insert Failed!!!");
		    } else
			JOptionPane
				.showMessageDialog(null,
					"\"" + newSubTaskNameTextField.getText()
						+ "\""
						+ " Successfully Added to Task "
						+ (String) taskComboBox
							.getSelectedItem()
						+ ".");

		} catch (SQLException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

		notifyListeners();

	    }
	});

    }

}
