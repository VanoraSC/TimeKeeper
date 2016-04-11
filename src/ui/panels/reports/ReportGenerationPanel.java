package ui.panels.reports;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;

import com.github.lgooddatepicker.datepicker.DatePicker;

import database.DatabaseConnector;
import database.databaseRows.SubTaskRow;
import database.databaseRows.TaskRow;
import database.databaseRows.TimeRow;
import database.statements.SelectStatements;
import ui.CustomJFrame;
import ui.factory.layout.GridBagConstraintsFactory;
import ui.panels.AbstractGridBagJPanel;
import ui.panels.tasks.TaskChooserPanel;

public class ReportGenerationPanel extends AbstractGridBagJPanel
	implements SelectStatements {

    private JButton reportsButton;
    private JComboBox<String> taskComboBox;
    private JComboBox<String> subTaskComboBox;
    private DatePicker startTime;
    private DatePicker endTime;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ReportGenerationPanel() {
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
		Timestamp start = Timestamp
			.valueOf(startTime.getDate().atStartOfDay());
		Timestamp end = Timestamp
			.valueOf(endTime.getDate().atTime(23, 59));

		ArrayList<TaskRow> tasksList = TaskChooserPanel.getTasksList();
		outer: for (TaskRow tr : tasksList) {
		    if (((String) taskComboBox.getSelectedItem())
			    .equalsIgnoreCase(tr.getName())) {
			for (SubTaskRow str : tr.getSubTasks()) {
			    if (((String) subTaskComboBox.getSelectedItem())
				    .equalsIgnoreCase(str.getName())) {
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
		    PreparedStatement ps = dbCon
			    .prepareStatement(selectTimesForSubTask);
		    ps.setInt(1, subTask.getId());

		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
			timesList.add(new TimeRow(rs.getInt("id"),
				rs.getInt("sub_task_id"),
				rs.getTimestamp("start_time"),
				rs.getTimestamp("end_time")));
		    }

		} catch (SQLException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		if (DEBUG)
		    for (TimeRow tr : timesList) {
			if (tr.getStartTime().after(start)
				&& tr.getEndTime().before(end)) {
			    System.out.println(tr.toString());
			}
		    }
		buildTimeCardPDF(timesList);
	    }
	});

	startTime = new DatePicker();
	endTime = new DatePicker();

	switch (LocalDate.now().getDayOfWeek()) {
	case MONDAY:
	    startTime.setDate(LocalDate.now().minusDays(1));
	    break;
	case TUESDAY:
	    startTime.setDate(LocalDate.now().minusDays(2));
	    break;
	case WEDNESDAY:
	    startTime.setDate(LocalDate.now().minusDays(3));
	    break;
	case THURSDAY:
	    startTime.setDate(LocalDate.now().minusDays(4));
	    break;
	case FRIDAY:
	    startTime.setDate(LocalDate.now().minusDays(5));
	    break;
	case SATURDAY:
	    startTime.setDate(LocalDate.now().minusDays(6));
	    break;
	case SUNDAY:
	    startTime.setDate(LocalDate.now().minusDays(7));
	    break;

	}

	endTime.setDate(LocalDate.now());

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

    protected void buildTimeCardPDF(ArrayList<TimeRow> timesList) {
	PDDocument doc = new PDDocument();
	String filename = "output.pdf";
	try {
	    PDPage page = new PDPage();
	    doc.addPage(page);

	    PDFont font = PDFontFactory.createDefaultFont();
	    PDPageContentStream contents = new PDPageContentStream(doc, page);
	    contents.beginText();
	    contents.setFont(font, 12);
	    contents.newLineAtOffset(100, 700);
	    contents.showText(timesList.get(0).toString().replace('\n', ' '));
	    contents.endText();
	    contents.close();

	    doc.save(filename);

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		doc.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    protected void updateSubTasks() {

	subTaskComboBox.removeAllItems();
	ArrayList<TaskRow> tasksList = TaskChooserPanel.getTasksList();
	for (TaskRow tr : tasksList) {
	    if (((String) taskComboBox.getSelectedItem())
		    .equalsIgnoreCase(tr.getName())) {
		for (SubTaskRow str : tr.getSubTasks()) {
		    subTaskComboBox.addItem(str.getName());
		}
	    }
	}
    }

    public static void main(String[] args) {
	CustomJFrame frame = new CustomJFrame();
	frame.setLayout(gbl);
	frame.add(new ReportGenerationPanel(),
		GridBagConstraintsFactory.buildConstraints());

    }

}
