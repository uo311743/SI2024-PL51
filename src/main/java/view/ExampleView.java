package view;

import javax.swing.JButton;

public class ExampleView extends AbstractView {
	
	private JButton btnExample;
	
	public ExampleView() { super("Example View", true); }
	
	protected void initializeAttrib() {};

	@Override
	protected void initialize() {
		btnExample = new JButton("Example");
	}

	@Override
	protected void configMainPanel() {
        super.getMainPanel().add(btnExample);
	}
	
	public JButton getBtnExample() { return this.btnExample;  }
}