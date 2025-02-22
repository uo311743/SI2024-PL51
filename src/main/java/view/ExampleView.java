package view;

import javax.swing.JButton;

public class ExampleView extends AbstractView {
	
	private JButton btnExample;
	
	public ExampleView() { super("Example View", true); }
	
<<<<<<< HEAD
	protected void initializeAttrib() {
=======
	@Override
	protected void initialize() {
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git
		btnExample = new JButton("Example");
	}

	@Override
	protected void configMainPanel() {
        super.getMainPanel().add(btnExample);
	}
	
	public JButton getBtnExample() { return this.btnExample;  }
}