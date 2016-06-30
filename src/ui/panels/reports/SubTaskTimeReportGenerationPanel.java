package ui.panels.reports;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.github.lgooddatepicker.datepicker.DatePicker;

import classes.time.DatePickerFactory;
import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.databaseRows.TimeRow;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.AbstractGridBagJPanel;
import ui.panels.tasks.TaskChooserPanel;

public class SubTaskTimeReportGenerationPanel extends AbstractGridBagJPanel implements SelectStatements {

	private JButton reportsButton;
	private JComboBox<String> taskComboBox;
	private JComboBox<String> subTaskComboBox;
	private DatePicker startTime;
	private DatePicker endTime;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubTaskTimeReportGenerationPanel() {
		initComponents();

		this.setLayout(gbl);

		JPanel newPanel = new JPanel();
		newPanel.setLayout(gbl);

		GridBagConstraints gbc = GridBagConstraintsFactory.buildConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;

		newPanel.add(new JLabel("Start Time"), gbc);
		gbc.gridx++;
		newPanel.add(new JLabel("End Time"), gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		newPanel.add(startTime, gbc);
		gbc.gridx++;
		newPanel.add(endTime, gbc);

		this.add(newPanel, gbc);
		gbc.gridy++;
		this.add(taskComboBox, gbc);
		gbc.gridy++;
		this.add(subTaskComboBox, gbc);
		gbc.gridy++;

		this.add(reportsButton, gbc);
	}

	private void initComponents() {
		reportsButton = new JButton("Get Work Report");
		reportsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SubTaskRow subTask = null;
				Timestamp start = Timestamp.valueOf(startTime.getDate().atStartOfDay());
				Timestamp end = Timestamp.valueOf(endTime.getDate().atTime(23, 59));

				ArrayList<TaskRow> tasksList = TaskChooserPanel.getTasksList();
				outer: for (TaskRow tr : tasksList) {
					if (((String) taskComboBox.getSelectedItem()).equalsIgnoreCase(tr.getName())) {
						for (SubTaskRow str : tr.getSubTasks()) {
							if (((String) subTaskComboBox.getSelectedItem()).equalsIgnoreCase(str.getName())) {
								subTask = str;
								break outer;
							}
						}
					}
				}
				ArrayList<TimeRow> timesList = new ArrayList<TimeRow>();
				Connection dbCon = DatabaseConnector.getConnection();
				if (subTask == null)
					return;
				try {
					PreparedStatement ps = dbCon.prepareStatement(selectTimesForSubTask);
					ps.setInt(1, subTask.getId());

					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						timesList.add(new TimeRow(rs.getInt("id"), rs.getInt("sub_task_id"), rs.getTimestamp("start_time"),
								rs.getTimestamp("end_time")));
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ArrayList<TimeRow> filteredTimesList = new ArrayList<TimeRow>();
				for (TimeRow tr : timesList) {
					if (tr.getStartTime().after(start) && tr.getStartTime().before(end)) {
						filteredTimesList.add(tr);
						if (DEBUG)
							System.out.println(tr.toString());
					}
				}
				buildTimeCardCSV(filteredTimesList);
			}
		});

		startTime = DatePickerFactory.buildStartDatePicker();
		endTime = DatePickerFactory.buildEndDatePicker();

		taskComboBox = new JComboBox<String>();
		taskComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubTasks();

			}
		});
		subTaskComboBox = new JComboBox<String>();

		ArrayList<TaskRow> tasksList = TaskChooserPanel.getTasksList();
		for (TaskRow tr : tasksList) {
			taskComboBox.addItem(tr.getName());
		}

	}

	private void buildTimeCardCSV(ArrayList<TimeRow> timesList) {
		String filename = "output.txt";

		long count = 0;

		StringBuilder sb = new StringBuilder();

		sb.append("Report for " + startTime.getDate().toString() + " through " + endTime.getDate().toString() + "\nTask: "
				+ (String) taskComboBox.getSelectedItem() + "\nSubTask: " + (String) subTaskComboBox.getSelectedItem()
				+ "\n\n");
		for (TimeRow tr : timesList) {
			sb.append("\tStarted: " + tr.getStartTime().toString() + "\n");
			sb.append("\tEnded  : " + tr.getEndTime().toString() + "\n");

			long diff = tr.getEndTime().getTime() - tr.getStartTime().getTime();
			count += diff;
			sb.append("\tDuration : " + getDurationBreakdown(diff));

		}

		sb.append("\tTotal Duration : " + getDurationBreakdown(count));

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(),
				(String) taskComboBox.getSelectedItem() + " - " + (String) subTaskComboBox.getSelectedItem()));

		// create the middle panel components

		JTextArea jta = new JTextArea(16, 58);
		jta.setText(sb.toString());
		jta.setEditable(false); // set textArea non-editable
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Add Textarea in to middle panel
		panel.add(jsp);

		new CustomJFrame("Time Card", false).add(panel);

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

	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		// long days = TimeUnit.MILLISECONDS.toDays(millis);
		// millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		// long milliseconds = TimeUnit.MILLISECONDS.toMillis(millis);

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
				+ "\n\n");

		return (sb.toString());
	}

	public static void main(String[] args) {
		CustomJFrame frame = new CustomJFrame();
		frame.setLayout(gbl);
		frame.add(new SubTaskTimeReportGenerationPanel(), GridBagConstraintsFactory.buildConstraints());

	}

}
