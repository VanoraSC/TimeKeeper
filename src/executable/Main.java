package executable;

import database.DatabaseConnector;
import ui.CustomJFrame;
import ui.panels.MainPanel;

public class Main {

	public static void main(String args[]) {

		CustomJFrame frame = new CustomJFrame("TimeKeeper" + (DatabaseConnector.isProduction() ? "" : "-DEVELOPMENT"),
				true);
		frame.add(new MainPanel());
		frame.setMinimumSize(frame.getPreferredSize());

	}
}
