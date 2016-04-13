package ui.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import classes.listeners.ListenerConstants;
import classes.listeners.tasks.TaskChangeListener;
import classes.listeners.tasks.TaskChangeNotifer;
import classes.listeners.times.TimeChangeListener;
import classes.time.StartStopTime;
import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.statements.InsertStatements;
import ui.UIConstants;
import ui.buttons.CommitButton;
import ui.buttons.PauseButton;
import ui.buttons.StartButton;
import ui.buttons.StopButton;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.menu.MenuBarPanel;
import ui.panels.tasks.TaskChooserPanel;

public class MainPanel extends JPanel implements UIConstants, TaskChangeListener, TaskChangeNotifer, TimeChangeListener,
		ListenerConstants, InsertStatements {

	private long startTime = 0;
	private long endTime = 0;

	public enum State {
		started, paused, stopped;
	}

	private State currentState = State.stopped;

	ArrayList<StartStopTime> timesList = new ArrayList<>();

	StartButton startButton = new StartButton();
	PauseButton pauseButton = new PauseButton();
	StopButton stopButton = new StopButton();
	CommitButton commitButton = new CommitButton();

	private final Object MUTEX = new Object();

	private ArrayList<TaskChangeListener> listeners = new ArrayList<TaskChangeListener>();

	private static final long serialVersionUID = 1L;

	private MenuBarPanel menu = new MenuBarPanel();

	private TaskChooserPanel tcp = new TaskChooserPanel();

	public MainPanel() {
		super();

		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		commitButton.setEnabled(false);

		GridBagConstraints gbc = GridBagConstraintsFactory.buildMenuConstraints();
		this.setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;

		this.add(menu, gbc);

		gbc = GridBagConstraintsFactory.buildConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;

		this.register(tcp);
		this.add(tcp, gbc);

		gbc.gridy++;

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(gbl);

		GridBagConstraints gbc2 = GridBagConstraintsFactory.buildConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 0;

		startButton.register(this);
		buttonPanel.add(startButton, gbc2);

		pauseButton.register(this);
		gbc2.gridx++;
		buttonPanel.add(pauseButton, gbc2);

		stopButton.register(this);
		gbc2.gridx++;
		buttonPanel.add(stopButton, gbc2);

		this.add(buttonPanel, gbc);

		commitButton.register(this);
		gbc.gridy++;
		this.add(commitButton, gbc);

		this.setPreferredSize(
				new Dimension((int) this.getPreferredSize().getWidth() + 300, (int) this.getPreferredSize().getHeight()));

	}

	@Override
	public void taskChangeUpdate() {
		notifyListeners();
		commitButton.setEnabled(false);
	}

	@Override
	public void timeChangeUpdate(int message) {

		synchronized (this) {
			if (message == TIME_START) {
				System.out.println("Timer Started");
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				pauseButton.setEnabled(true);
				commitButton.setEnabled(false);

				tcp.disableComboBoxes();

				startTime = System.currentTimeMillis();
				if (currentState == State.stopped)
					timesList = new ArrayList<StartStopTime>();
				currentState = State.started;

			} else if (message == TIME_STOP) {
				System.out.println("Timer Stopped");
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				pauseButton.setEnabled(false);
				commitButton.setEnabled(true);

				tcp.enableComboBoxes();
				if (currentState == State.started) {
					endTime = System.currentTimeMillis();

					StartStopTime sst = new StartStopTime(new Timestamp(startTime), new Timestamp(endTime));

					timesList.add(sst);
				}

				currentState = State.stopped;

			} else if (message == TIME_PAUSE) {
				System.out.println("Timer Paused");
				stopButton.setEnabled(true);
				startButton.setEnabled(true);
				pauseButton.setEnabled(false);

				endTime = System.currentTimeMillis();
				StartStopTime sst = new StartStopTime(new Timestamp(startTime), new Timestamp(endTime));

				timesList.add(sst);
				currentState = State.paused;
			} else if (message == TIME_COMMIT) {
				storeTimeInDatabase();
			}
		}
	}

	private void storeTimeInDatabase() {

		commitButton.setEnabled(false);
		if (DEBUG) {

			System.out.println("Storing Time in Database");

			for (StartStopTime sst : timesList) {
				System.out.println("Started " + sst.getStartTime() + ", Stopped " + sst.getStopTime());
			}

		}

		Connection dbCon = DatabaseConnector.getConnection();

		int currentSubTaskID = 0;
		outer: for (TaskRow tr : TaskChooserPanel.getTasksList()) {
			if (tr.getName().equalsIgnoreCase(tcp.getCurrentTaskName()))
				for (SubTaskRow str : tr.getSubTasks()) {
					if (str.getName().equalsIgnoreCase(tcp.getCurrentSubTaskName())) {
						currentSubTaskID = str.getId();
						break outer;
					}
				}

		}

		for (StartStopTime sst : timesList) {
			try {
				PreparedStatement ps = dbCon.prepareStatement(insertTime);
				ps.setTimestamp(1, sst.getStartTime());
				ps.setTimestamp(2, sst.getStopTime());
				ps.setInt(3, currentSubTaskID);

				if (ps.executeUpdate() != 1) {
					JOptionPane.showMessageDialog(null, "Insert Failed!!!");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void register(TaskChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (!listeners.contains(tcl))
					listeners.add(tcl);
			}

	}

	@Override
	public void unRegister(TaskChangeListener tcl) {
		if (tcl != null)
			synchronized (MUTEX) {
				if (listeners.contains(tcl))
					listeners.remove(tcl);
			}

	}

	@Override
	public void notifyListeners() {
		synchronized (MUTEX) {
			for (TaskChangeListener tcl : listeners)
				tcl.taskChangeUpdate();
		}
	}

}
