package ui.panels;

import javax.swing.JPanel;

import ui.UIConstants;

public abstract class AbstractGridBagJPanel extends JPanel implements UIConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractGridBagJPanel() {
		super();
		this.setLayout(gbl);
	}

}
