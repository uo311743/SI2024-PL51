package view;

import javax.swing.JButton;

public class ExampleView extends AbstractView {
	
	private JButton btnExample;
	
	public ExampleView() { super("Example View"); }
	
	protected void initializeAttrib() {};

	@Override
	protected void initialize() {
		this.btnExample = new JButton("Example");
		super.createButtonLowLeft("A");
		//super.createButtonLowMiddle("B");
		super.createButtonLowRight("C");
	}

	@Override
	protected void configMainPanel() {
        super.getMainPanel().add(btnExample);
	}
	
	public JButton getBtnExample() { return this.btnExample;  }
}