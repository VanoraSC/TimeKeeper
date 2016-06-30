package ui.buttons;

public class StartButton extends TimeChangerJButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		StartButton sb = new StartButton();

		sb.doClick();
	}

	public StartButton() {
		super(TIME_START);
		this.setText("Start");
	}

}
