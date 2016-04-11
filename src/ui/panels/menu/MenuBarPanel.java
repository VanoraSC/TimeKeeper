package ui.panels.menu;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import classes.listeners.tasks.TaskChangeListener;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.AbstractGridBagJPanel;
import ui.panels.reports.ReportGenerationPanel;
import ui.panels.tasks.SubTaskAdditionPanel;
import ui.panels.tasks.SubTaskDeletionPanel;
import ui.panels.tasks.TaskAdditionPanel;
import ui.panels.tasks.TaskDeletionPanel;
import ui.panels.tasks.TimeDeletionPanel;

public class MenuBarPanel extends AbstractGridBagJPanel
	implements TaskChangeListener {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
	CustomJFrame frame = new CustomJFrame();
	frame.add(new MenuBarPanel());
    }

    private JMenuItem deleteSubTaskMenuItem = new JMenuItem("Delete SubTask");
    private JMenuItem deleteSubTaskTimeMenuItem = new JMenuItem("Delete Time");

    private JMenuItem deleteTaskMenuItem = new JMenuItem("Delete Task");
    private JMenu deleteMenu = new JMenu("Delete");
    private JMenu exitMenu = new JMenu("Exit");

    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem newSubTaskMenuItem = new JMenuItem("New SubTask");

    private JMenuItem newTaskMenuItem = new JMenuItem("New Task");
    private JMenu newMenu = new JMenu("New");

    private TaskChangeListener self = this;
    private JMenu reportsMenu = new JMenu("Reports");

    private JMenuItem viewTimesMenuItem = new JMenuItem(
	    "View Time for SubTask");

    public MenuBarPanel() {
	super();

	initComponents();
	addComponents();

	GridBagConstraints gbc = GridBagConstraintsFactory
		.buildMenuConstraints();

	this.add(menuBar, gbc);

    }

    private void addComponents() {
	newMenu.add(newTaskMenuItem);
	newMenu.add(newSubTaskMenuItem);

	menuBar.add(newMenu);

	deleteMenu.add(deleteTaskMenuItem);
	deleteMenu.add(deleteSubTaskMenuItem);
	deleteMenu.add(deleteSubTaskTimeMenuItem);

	menuBar.add(deleteMenu);

	reportsMenu.add(viewTimesMenuItem);
	menuBar.add(reportsMenu);
	exitMenu.add(exitMenuItem);
	menuBar.add(exitMenu);

    }

    private void initComponents() {
	// TODO Auto-generated method stub

	exitMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);

	    }
	});

	newTaskMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		TaskAdditionPanel tap = new TaskAdditionPanel();
		tap.register(self);
		CustomJFrame frame = new CustomJFrame("Add New Tasks", false);
		frame.add(tap);
	    }
	});

	newSubTaskMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		SubTaskAdditionPanel stap = new SubTaskAdditionPanel();
		stap.register(self);
		CustomJFrame frame = new CustomJFrame("Add New Sub Tasks",
			false);
		frame.add(stap);

	    }
	});

	deleteTaskMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		TaskDeletionPanel tdp = new TaskDeletionPanel();
		tdp.register(self);
		CustomJFrame frame = new CustomJFrame("Delete Tasks", false);
		frame.add(tdp);
	    }
	});

	deleteSubTaskMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		SubTaskDeletionPanel stdp = new SubTaskDeletionPanel();
		stdp.register(self);
		CustomJFrame frame = new CustomJFrame("Delete Sub Tasks",
			false);
		frame.add(stdp);

	    }
	});
	deleteSubTaskTimeMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		TimeDeletionPanel tdp = new TimeDeletionPanel();
		tdp.register(self);
		CustomJFrame frame = new CustomJFrame("Delete Time", false);
		frame.add(tdp);

	    }
	});

	viewTimesMenuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		ReportGenerationPanel tdp = new ReportGenerationPanel();

		CustomJFrame frame = new CustomJFrame("Generate Time Card",
			false);
		frame.add(tdp);

	    }
	});

    }

    @Override
    public void taskChangeUpdate() {
	((TaskChangeListener) this.getParent()).taskChangeUpdate();

    }

}
