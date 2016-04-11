package ui;

import java.awt.GridBagLayout;

import ui.factory.layout.GridBagLayoutFactory;

public interface UIConstants {

    final static String ADD_SUB_TASK = "  Add Sub Task  ";

    final static String ADD_TASK = "  Add Task  ";
    static final double COMBO_BOX_WEIGHT = 1;
    final static int COMBO_BOX_X = 1;
    // final static boolean DEBUG = false;
    final static boolean DEBUG = true;
    final static GridBagLayout gbl = GridBagLayoutFactory.buildLayout();
    static final double LABEL_WEIGHT = 0;
    final static int LABEL_X = 0;

    static final double MENU_WEIGHT = 0;
    final static String NEW_SUB_TASK_NAME = "  New SubTask Name:  ";
    final static String NEW_TASK_NAME = "  New Task Name:  ";

    final static String PRIMARY_TASK = "  Primary Task:  ";
    final static String SUB_TASK = "  Sub Task:  ";
    static final double TEXT_FIELD_WEIGHT = 1;
    final static int TEXT_FIELD_X = 1;

}
