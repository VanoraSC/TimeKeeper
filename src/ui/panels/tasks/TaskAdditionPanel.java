package ui.panels.tasks;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.DatabaseConnector;
import database.statements.InsertStatements;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;

public class TaskAdditionPanel extends AbstractTaskChangerGridBagJPanel implements InsertStatements, SelectStatements {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		CustomJFrame frame = new CustomJFrame();
		frame.add(new TaskAdditionPanel());

	}

	private JButton addTaskButton = new JButton(ADD_TASK);

	private JLabel newTaskNameLabel = new JLabel(NEW_TASK_NAME);

	private JTextField newTaskNameTextField = new JTextField();

	public TaskAdditionPanel() {
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

		newTaskPanel.add(newTaskNameLabel, gbc);

		gbc.gridx = TEXT_FIELD_X;
		gbc.weightx = TEXT_FIELD_WEIGHT;

		newTaskPanel.add(newTaskNameTextField, gbc);
		newTaskPanel.setMinimumSize(new Dimension(100, 0));

		this.add(newTaskPanel, GridBagConstraintsFactory.buildConstraints());
		this.add(addTaskButton, GridBagConstraintsFactory.buildConstraints());

	}

	private void initComponents() {
		newTaskNameTextField.setPreferredSize(new Dimension(300, 0));

		addTaskButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (newTaskNameTextField.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(null, "Insert Failed, Cannot Insert Empty Name!!!");
					return;
				}
				Connection dbCon = DatabaseConnector.getConnection();

				PreparedStatement ps;
				try {
					ps = dbCon.prepareStatement(selectTasks);

					ResultSet rs;

					rs = ps.executeQuery();

					while (rs.next()) {
						if (rs.getString("name").equalsIgnoreCase(newTaskNameTextField.getText())) {
							JOptionPane.showMessageDialog(null, "Task Already Exists!!!");

							return;
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					ps = dbCon.prepareStatement(insertTask);

					ps.setString(1, newTaskNameTextField.getText());
					if (ps.executeUpdate() != 1) {
						JOptionPane.showMessageDialog(null, "Insert Failed!!!");
					} else
						JOptionPane.showMessageDialog(null, "\"" + newTaskNameTextField.getText() + "\"" + " Successfully Added.");

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				notifyListeners();

			}
		});

	}

}
