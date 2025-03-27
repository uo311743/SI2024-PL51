package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import util.SwingUtil;

public class SubmenuView {

	private String viewName;
	private JFrame frame;
	private List<ButtonDataClass> buttonList;

	public SubmenuView(String viewName)
	{
		this.viewName = viewName;
		this.buttonList = new ArrayList<SubmenuView.ButtonDataClass>();
	}

	public void addButton(String text, Runnable action, boolean enabled) {
		this.buttonList.add(new ButtonDataClass(text, action, enabled));
	}

	public void addButton(String text, Runnable action) {
		this.buttonList.add(new ButtonDataClass(text, action, true));
	}

	public void setVisible() {
		this.initialize();
		this.frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle(viewName);
		frame.setBounds(0, 0, 300, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center the window

		// Main panel with BorderLayout
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins
		frame.getContentPane().add(mainPanel);

		// Creates the title
		JLabel titleLabel = new JLabel(viewName, SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Button panel with vertical BoxLayout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		for (ButtonDataClass button : this.buttonList)
			this.createButton(buttonPanel, button);

		mainPanel.add(buttonPanel, BorderLayout.CENTER);

		// Create the panel with GridBagLayout
		JPanel lowButtonPanel = new JPanel(new GridBagLayout());
		lowButtonPanel.setPreferredSize(new Dimension(frame.getWidth(), 40));
		lowButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JButton goBack = new JButton("Go Back");
		goBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtil.exceptionWrapper(() -> {
					frame.dispose();
				});
			}
		});

		lowButtonPanel.add(goBack);
		mainPanel.add(lowButtonPanel, BorderLayout.SOUTH);

		// Adjust frame size to fit content
		frame.pack();

		// Set minimum size to prevent shrinking below initial dimensions
		frame.setMinimumSize(frame.getSize());
	}

	private class ButtonDataClass
	{
		protected String text;
		protected Runnable action;
		protected boolean enabled;

		ButtonDataClass(String text, Runnable action, boolean enabled)
		{
			this.text = text;
			this.action = action;
			this.enabled = enabled;
		}

		protected String getText() { return text; }
		protected Runnable getAction() { return action; }
		protected boolean isEnabled() { return enabled; }
	}

	/**
	 * Creates a button in the main frame
	 * 
	 * @param text   to be displied inside the button
	 * @param action to be performed by the button on-click
	 */
	private void createButton(JPanel panel, ButtonDataClass buttonData)
	{
		JButton button = new JButton(buttonData.getText());
		button.addActionListener(e -> buttonData.getAction().run());
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setEnabled(buttonData.isEnabled());
		panel.add(button);
		panel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
	}
}
