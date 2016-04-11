package ui.buttons;

public class CommitButton extends TimeChangerJButton {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
	CommitButton sb = new CommitButton();

	sb.doClick();
    }

    public CommitButton() {
	super(TIME_COMMIT);
	this.setText("Save Time to Task");
	
    }

}
