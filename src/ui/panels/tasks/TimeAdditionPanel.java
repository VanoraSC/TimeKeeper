package ui.panels.tasks;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.lgooddatepicker.datepicker.DatePicker;

import classes.time.DatePickerFactory;
import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.statements.InsertStatements;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;

public class TimeAdditionPanel extends AbstractTaskChangerGridBagJPanel implements InsertStatements, SelectStatements {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JComboBox<String> taskComboBox = new JComboBox<String>();
	private JComboBox<String> subTaskComboBox = new JComboBox<String>();

	private JComboBox<Integer> startHourComboBox = new JComboBox<Integer>();
	private JComboBox<Integer> startMinuteComboBox = new JComboBox<Integer>();
	private JComboBox<Integer> startSecondComboBox = new JComboBox<Integer>();

	private JComboBox<Integer> endHourComboBox = new JComboBox<Integer>();
	private JComboBox<Integer> endMinuteComboBox = new JComboBox<Integer>();
	private JComboBox<Integer> endSecondComboBox = new JComboBox<Integer>();

	private JLabel startDateLabel = new JLabel("Start Date");
	private JLabel endDateLabel = new JLabel("End Date");

	private JLabel startHourLabel = new JLabel("Start Hour");
	private JLabel startMinuteLabel = new JLabel("Minute");
	private JLabel startSecondLabel = new JLabel("Second");

	private JLabel endHourLabel = new JLabel("End Hour");
	private JLabel endMinuteLabel = new JLabel("Minute");
	private JLabel endSecondLabel = new JLabel("Second");

	private DatePicker startDate;
	private DatePicker endDate;

	private JButton addTimeButton = new JButton(ADD_TIME);

	private ArrayList<TaskRow> taskRows = new ArrayList<TaskRow>();

	public static void main(String[] args) {

		CustomJFrame frame = new CustomJFrame();
		frame.add(new TimeAdditionPanel());

	}

	public TimeAdditionPanel() {
		super();

		initComponents();

		JPanel newTimePanel = new JPanel();
		newTimePanel.setLayout(gbl);

		GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();
		gbl.setConstraints(this, gbc);
		gbl.setConstraints(newTimePanel, gbc);

		gbc.gridy = 0;
		gbc.gridx = LABEL_X;
		gbc.weightx = LABEL_WEIGHT;

		JPanel datePickerPanel = new JPanel();

		GridBagConstraints gbc3 = GridBagConstraintsFactory.buildConstraints();
		datePickerPanel.add(startDateLabel, gbc3);

		gbc3.gridx++;

		datePickerPanel.add(startDate, gbc3);

		gbc3.gridx++;

		datePickerPanel.add(endDateLabel, gbc3);

		gbc3.gridx++;

		datePickerPanel.add(endDate, gbc3);

		newTimePanel.add(datePickerPanel, gbc);

		gbc.gridy++;

		gbc.gridx = LABEL_X;
		gbc.weightx = LABEL_WEIGHT;

		JPanel timeChoserPanel = new JPanel();
		GridBagConstraints gbc4 = GridBagConstraintsFactory.buildConstraints();

		timeChoserPanel.add(startHourLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(startHourComboBox, gbc4);

		gbc4.gridy++;

		gbc4.gridx = LABEL_X;
		gbc4.weightx = LABEL_WEIGHT;

		timeChoserPanel.add(startMinuteLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(startMinuteComboBox, gbc4);

		gbc4.gridy++;

		gbc4.gridx = LABEL_X;
		gbc4.weightx = LABEL_WEIGHT;

		timeChoserPanel.add(startSecondLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(startSecondComboBox, gbc4);

		gbc4.gridy++;

		timeChoserPanel.add(endHourLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(endHourComboBox, gbc4);

		gbc4.gridy++;

		gbc4.gridx = LABEL_X;
		gbc4.weightx = LABEL_WEIGHT;

		timeChoserPanel.add(endMinuteLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(endMinuteComboBox, gbc4);

		gbc4.gridy++;

		gbc4.gridx = LABEL_X;
		gbc4.weightx = LABEL_WEIGHT;

		timeChoserPanel.add(endSecondLabel, gbc4);

		gbc4.gridx++;

		gbc4.weightx = COMBO_BOX_WEIGHT;
		timeChoserPanel.add(endSecondComboBox, gbc4);

		newTimePanel.add(timeChoserPanel, gbc);
		gbc.gridy++;

		// newTimePanel.setPreferredSize(new Dimension((int)
		// taskComboBox.getPreferredSize().getWidth() + 300,
		// (int) taskComboBox.getPreferredSize().getHeight()));

		GridBagConstraints gbc2 = GridBagConstraintsFactory.buildConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 0;

		gbc2.weightx = COMBO_BOX_WEIGHT;

		this.add(taskComboBox, gbc2);
		gbc2.gridy++;
		this.add(subTaskComboBox, gbc2);
		gbc2.gridy++;
		this.add(newTimePanel, gbc2);

		gbc2.gridy++;

		this.add(addTimeButton, gbc2);
	}

	private void initComponents() {

		Connection dbCon = DatabaseConnector.getConnection();

		startDate = DatePickerFactory.buildTodayDatePicker();
		endDate = DatePickerFactory.buildTodayDatePicker();

		for (int i = 0; i < 24; i++) {
			startHourComboBox.addItem(new Integer(i));
			endHourComboBox.addItem(new Integer(i));
		}

		for (int i = 0; i < 60; i++) {
			startMinuteComboBox.addItem(new Integer(i));
			endMinuteComboBox.addItem(new Integer(i));
		}
		for (int i = 0; i < 60; i++) {
			startSecondComboBox.addItem(new Integer(i));
			endSecondComboBox.addItem(new Integer(i));
		}

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

		taskComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubTasks();

			}
		});

		updateSubTasks();

		addTimeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int currentSubTaskID = 0;
				outer: for (TaskRow tr : TaskChooserPanel.getTasksList()) {
					if (tr.getName().equalsIgnoreCase((String) taskComboBox.getSelectedItem()))
						for (SubTaskRow str : tr.getSubTasks()) {
							if (str.getName().equalsIgnoreCase((String) subTaskComboBox.getSelectedItem())) {
								currentSubTaskID = str.getId();
								break outer;
							}
						}
				}

				Timestamp startTime = Timestamp
						.valueOf(startDate.getDate().atTime((Integer) startHourComboBox.getSelectedItem(),
								(Integer) startMinuteComboBox.getSelectedItem(), (Integer) startSecondComboBox.getSelectedItem()));

				Timestamp endTime = Timestamp.valueOf(endDate.getDate().atTime((Integer) endHourComboBox.getSelectedItem(),
						(Integer) endMinuteComboBox.getSelectedItem(), (Integer) endSecondComboBox.getSelectedItem()));
				try {
					PreparedStatement ps = dbCon.prepareStatement(insertTime);
					ps.setTimestamp(1, startTime);
					ps.setTimestamp(2, endTime);
					ps.setInt(3, currentSubTaskID);

					if (ps.executeUpdate() != 1) {
						JOptionPane.showMessageDialog(null, "Insert Failed!!!");
					} else
						JOptionPane.showMessageDialog(null, "Insert Successful!!!");

				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
		});
	}

	protected void updateSubTasks() {

		subTaskComboBox.removeAllItems();
		ArrayList<TaskRow> tasksList = TaskChooserPanel.getTasksList();
		for (TaskRow tr : tasksList) {
			if (((String) taskComboBox.getSelectedItem()).equalsIgnoreCase(tr.getName())) {
				for (SubTaskRow str : tr.getSubTasks()) {
					subTaskComboBox.addItem(str.getName());
				}
			}
		}
	}

}
