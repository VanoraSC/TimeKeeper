package ui.buttons;

public class PauseButton extends TimeChangerJButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		PauseButton sb = new PauseButton();

		sb.doClick();
	}

	public PauseButton() {
		super(TIME_PAUSE);
		this.setText("Pause");
	}
}
