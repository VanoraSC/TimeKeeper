package ui;

import java.awt.Component;

import javax.swing.JFrame;

public class CustomJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomJFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	public CustomJFrame(boolean autoClose) {
		super();
		if (autoClose)
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	public CustomJFrame(String s, boolean autoClose) {
		super(s);
		if (autoClose)
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	@Override
	public Component add(Component c) {
		super.add(c);
		this.pack();
		this.setMinimumSize(this.getPreferredSize());
		this.setVisible(true);

		this.setLocationRelativeTo(null);
		return c;
	}

	@Override
	public void add(Component comp, Object constraints) {
		super.add(comp, constraints);
		this.pack();
		this.setMinimumSize(this.getPreferredSize());
		this.setVisible(true);

		this.setLocationRelativeTo(null);

	}

}
