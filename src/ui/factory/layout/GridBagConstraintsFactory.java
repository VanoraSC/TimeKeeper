package ui.factory.layout;

import java.awt.GridBagConstraints;

import ui.UIConstants;

public class GridBagConstraintsFactory implements UIConstants {

    public static GridBagConstraints buildConstraints() {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.weightx = 1;
	gbc.weighty = 1;

	gbc.fill = GridBagConstraints.BOTH;
	gbc.anchor = GridBagConstraints.CENTER;
	return gbc;
    }

    public static GridBagConstraints buildMenuConstraints() {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.weightx = MENU_WEIGHT;
	gbc.weighty = 0.001;

	gbc.fill = GridBagConstraints.NONE;
	gbc.anchor = GridBagConstraints.LINE_START;
	return gbc;
    }

    private GridBagConstraintsFactory() {

    }

}
