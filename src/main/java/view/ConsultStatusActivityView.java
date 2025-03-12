package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ConsultStatusActivityView extends AbstractView {
    private JTable activityTable, sponsorshipTable, incomeTable, expensesTable;
    
    private JTextField totalEstimatedIncomeField, totalEstimatedExpensesField, totalEstimatedBalanceField;
    private JTextField totalActualIncomeField, totalActualExpensesField, totalActualBalanceField;
    
    private JTextField subTotalSponsorshipEstimatedField, subTotalSponsorshipActualField;
    private JTextField subTotalSponsorshipBalanceEstimatedField, subTotalSponsorshipBalanceActualField;
    private JTextField subTotalIncomeEstimatedField, subTotalIncomeActualField;
    private JTextField subTotalExpensesEstimatedField, subTotalExpensesActualField;

    public ConsultStatusActivityView() {
        super("Consult Status Activity");
    }

    @Override
    protected void initialize() {
        this.createButtonLowLeft("Go back");

        totalEstimatedIncomeField = new JTextField(10);
        totalEstimatedIncomeField.setEditable(false);
        totalActualIncomeField = new JTextField(10);
        totalActualIncomeField.setEditable(false);
        totalEstimatedExpensesField = new JTextField(10);
        totalEstimatedExpensesField.setEditable(false);
        totalActualExpensesField = new JTextField(10);
        totalActualExpensesField.setEditable(false);
        totalEstimatedBalanceField = new JTextField(10);
        totalEstimatedBalanceField.setEditable(false);
        totalActualBalanceField = new JTextField(10);
        totalActualBalanceField.setEditable(false);
        subTotalSponsorshipEstimatedField = new JTextField(10);
        subTotalSponsorshipEstimatedField.setEditable(false);
        subTotalSponsorshipActualField = new JTextField(10);
        subTotalSponsorshipActualField.setEditable(false);
        subTotalSponsorshipBalanceEstimatedField = new JTextField(10);
        subTotalSponsorshipBalanceEstimatedField.setEditable(false);
        subTotalSponsorshipBalanceActualField = new JTextField(10);
        subTotalSponsorshipBalanceActualField.setEditable(false);
        subTotalIncomeEstimatedField = new JTextField(10);
        subTotalIncomeEstimatedField.setEditable(false);
        subTotalIncomeActualField = new JTextField(10);
        subTotalIncomeActualField.setEditable(false);
        subTotalExpensesEstimatedField = new JTextField(10);
        subTotalExpensesEstimatedField.setEditable(false);
        subTotalExpensesActualField = new JTextField(10);
        subTotalExpensesActualField.setEditable(false);

        activityTable = new JTable();
        sponsorshipTable = new JTable();
        incomeTable = new JTable();
        expensesTable = new JTable();
    }

    @Override
    protected void configMainPanel() {
        JPanel topPanel = createTopPanel();
        JPanel centralPanel = createCentralPanel();
        JPanel incomePanel = createIncomePanel();
        JPanel expensesPanel = createExpensesPanel();

        // Main layout: Top, Central, Income, and Expenses panels stacked vertically
        JPanel mainPanel = super.getMainPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(topPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(centralPanel, gbc);

        gbc.gridy = 2;
        mainPanel.add(incomePanel, gbc);

        gbc.gridy = 3;
        mainPanel.add(expensesPanel, gbc);
    }

    private JPanel createTopPanel() {
        // Activity selection panel
        activityTable.setName("activityTable");
        activityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activityTable.setDefaultEditor(Object.class, null);
        JScrollPane activityScroll = new JScrollPane(activityTable);
        activityScroll.setBorder(BorderFactory.createTitledBorder("Select an Activity"));

        // Estimated totals panel
        JPanel estimatedPanel = new JPanel();
        estimatedPanel.setLayout(new BoxLayout(estimatedPanel, BoxLayout.Y_AXIS));
        estimatedPanel.setBorder(BorderFactory.createTitledBorder("Estimated"));
        JLabel totalIncomeEstimated = new JLabel("Income:");
        JLabel totalExpensesEstimated = new JLabel("Expenses:");
        JLabel balanceEstimated = new JLabel("Balance:");
        estimatedPanel.add(totalIncomeEstimated);
        estimatedPanel.add(totalEstimatedIncomeField);
        estimatedPanel.add(totalExpensesEstimated);
        estimatedPanel.add(totalEstimatedExpensesField);
        estimatedPanel.add(balanceEstimated);
        estimatedPanel.add(totalEstimatedBalanceField);

        // Actual totals panel
        JPanel actualPanel = new JPanel();
        actualPanel.setLayout(new BoxLayout(actualPanel, BoxLayout.Y_AXIS));
        actualPanel.setBorder(BorderFactory.createTitledBorder("Actual"));
        JLabel totalIncomePaid = new JLabel("Income:");
        JLabel totalExpensesPaid = new JLabel("Expenses:");
        JLabel balanceActual = new JLabel("Balance:");
        actualPanel.add(totalIncomePaid);
        actualPanel.add(totalActualIncomeField);
        actualPanel.add(totalExpensesPaid);
        actualPanel.add(totalActualExpensesField);
        actualPanel.add(balanceActual);
        actualPanel.add(totalActualBalanceField);

        // Totals panel with two columns
        JPanel totalPanel = new JPanel(new GridLayout(1, 2));
        totalPanel.add(estimatedPanel);
        totalPanel.add(actualPanel);
        totalPanel.setBorder(BorderFactory.createTitledBorder("Totals"));
        totalPanel.setPreferredSize(new Dimension(400, 150));

        // Top panel: Activity selection and totals
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        topPanel.add(activityScroll, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        topPanel.add(totalPanel, gbc);

        return topPanel;
    }

    private JPanel createCentralPanel() {
        // Sponsorship panel
        sponsorshipTable.setDefaultEditor(Object.class, null);
        JScrollPane sponsorshipScroll = new JScrollPane(sponsorshipTable);
        sponsorshipScroll.setBorder(BorderFactory.createTitledBorder("Sponsorships"));

        // Sub-total panel for sponsorships
        JPanel sponsorshipSubTotalPanel = createSponsorshipSubTotalPanel();

        // Central panel: Sponsorships and their sub-totals
        JPanel centralPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centralPanel.add(sponsorshipScroll, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        centralPanel.add(sponsorshipSubTotalPanel, gbc);

        return centralPanel;
    }

    private JPanel createIncomePanel() {
        // Income panel
        incomeTable.setDefaultEditor(Object.class, null);
        JScrollPane incomeScroll = new JScrollPane(incomeTable);
        incomeScroll.setBorder(BorderFactory.createTitledBorder("Income"));

        // Sub-total panel for income
        JPanel incomeSubTotalPanel = createIncomeSubTotalPanel();

        // Income panel: Income and its sub-totals
        JPanel incomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        incomePanel.add(incomeScroll, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        incomePanel.add(incomeSubTotalPanel, gbc);

        return incomePanel;
    }

    private JPanel createExpensesPanel() {
        // Expenses panel
        expensesTable.setDefaultEditor(Object.class, null);
        JScrollPane expensesScroll = new JScrollPane(expensesTable);
        expensesScroll.setBorder(BorderFactory.createTitledBorder("Expenses"));

        // Sub-total panel for expenses
        JPanel expensesSubTotalPanel = createExpensesSubTotalPanel();

        // Expenses panel: Expenses and their sub-totals
        JPanel expensesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        expensesPanel.add(expensesScroll, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        expensesPanel.add(expensesSubTotalPanel, gbc);

        return expensesPanel;
    }

    private JPanel createSponsorshipSubTotalPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Sponsorship Sub-Total"));
        JLabel subTotalSponsorshipIncomeEstimated = new JLabel("Estimated:");
        JLabel subTotalSponsorshipIncomeActual = new JLabel("Actual:");
        panel.add(subTotalSponsorshipIncomeEstimated);
        panel.add(subTotalSponsorshipEstimatedField);
        panel.add(subTotalSponsorshipIncomeActual);
        panel.add(subTotalSponsorshipActualField);
        return panel;
    }

    private JPanel createIncomeSubTotalPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Income Sub-Total"));
        JLabel subTotalIncomeEstimated = new JLabel("Estimated:");
        JLabel subTotalIncomeActual = new JLabel("Actual:");
        panel.add(subTotalIncomeEstimated);
        panel.add(subTotalIncomeEstimatedField);
        panel.add(subTotalIncomeActual);
        panel.add(subTotalIncomeActualField);
        return panel;
    }

    private JPanel createExpensesSubTotalPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Expenses Sub-Total"));
        JLabel subTotalExpensesEstimated = new JLabel("Estimated:");
        JLabel subTotalExpensesActual = new JLabel("Actual:");
        panel.add(subTotalExpensesEstimated);
        panel.add(subTotalExpensesEstimatedField);
        panel.add(subTotalExpensesActual);
        panel.add(subTotalExpensesActualField);
        return panel;
    }

	public JTable getActivityTable() {
		return activityTable;
	}

	public void setActivityTable(JTable activityTable) {
		this.activityTable = activityTable;
	}

	public JTable getSponsorshipTable() {
		return sponsorshipTable;
	}

	public void setSponsorshipTable(JTable sponsorshipTable) {
		this.sponsorshipTable = sponsorshipTable;
	}

	public JTable getIncomeTable() {
		return incomeTable;
	}

	public void setIncomeTable(JTable incomeTable) {
		this.incomeTable = incomeTable;
	}

	public JTable getExpensesTable() {
		return expensesTable;
	}

	public void setExpensesTable(JTable expensesTable) {
		this.expensesTable = expensesTable;
	}

	public JTextField getTotalEstimatedIncomeField() {
		return totalEstimatedIncomeField;
	}

	public void setTotalEstimatedIncomeField(JTextField totalEstimatedIncomeField) {
		this.totalEstimatedIncomeField = totalEstimatedIncomeField;
	}

	public JTextField getTotalEstimatedExpensesField() {
		return totalEstimatedExpensesField;
	}

	public void setTotalEstimatedExpensesField(JTextField totalEstimatedExpensesField) {
		this.totalEstimatedExpensesField = totalEstimatedExpensesField;
	}

	public JTextField getTotalEstimatedBalanceField() {
		return totalEstimatedBalanceField;
	}

	public void setTotalEstimatedBalanceField(JTextField totalEstimatedBalanceField) {
		this.totalEstimatedBalanceField = totalEstimatedBalanceField;
	}

	public JTextField getTotalActualIncomeField() {
		return totalActualIncomeField;
	}

	public void setTotalActualIncomeField(JTextField totalActualIncomeField) {
		this.totalActualIncomeField = totalActualIncomeField;
	}

	public JTextField getTotalActualExpensesField() {
		return totalActualExpensesField;
	}

	public void setTotalActualExpensesField(JTextField totalActualExpensesField) {
		this.totalActualExpensesField = totalActualExpensesField;
	}

	public JTextField getTotalActualBalanceField() {
		return totalActualBalanceField;
	}

	public void setTotalActualBalanceField(JTextField totalActualBalanceField) {
		this.totalActualBalanceField = totalActualBalanceField;
	}

	public JTextField getSubTotalSponsorshipEstimatedField() {
		return subTotalSponsorshipEstimatedField;
	}

	public void setSubTotalSponsorshipEstimatedField(JTextField subTotalSponsorshipEstimatedField) {
		this.subTotalSponsorshipEstimatedField = subTotalSponsorshipEstimatedField;
	}

	public JTextField getSubTotalSponsorshipActualField() {
		return subTotalSponsorshipActualField;
	}

	public void setSubTotalSponsorshipActualField(JTextField subTotalSponsorshipActualField) {
		this.subTotalSponsorshipActualField = subTotalSponsorshipActualField;
	}

	public JTextField getSubTotalSponsorshipBalanceEstimatedField() {
		return subTotalSponsorshipBalanceEstimatedField;
	}

	public void setSubTotalSponsorshipBalanceEstimatedField(JTextField subTotalSponsorshipBalanceEstimatedField) {
		this.subTotalSponsorshipBalanceEstimatedField = subTotalSponsorshipBalanceEstimatedField;
	}

	public JTextField getSubTotalSponsorshipBalanceActualField() {
		return subTotalSponsorshipBalanceActualField;
	}

	public void setSubTotalSponsorshipBalanceActualField(JTextField subTotalSponsorshipBalanceActualField) {
		this.subTotalSponsorshipBalanceActualField = subTotalSponsorshipBalanceActualField;
	}

	public JTextField getSubTotalIncomeEstimatedField() {
		return subTotalIncomeEstimatedField;
	}

	public void setSubTotalIncomeEstimatedField(JTextField subTotalIncomeEstimatedField) {
		this.subTotalIncomeEstimatedField = subTotalIncomeEstimatedField;
	}

	public JTextField getSubTotalIncomeActualField() {
		return subTotalIncomeActualField;
	}

	public void setSubTotalIncomeActualField(JTextField subTotalIncomeActualField) {
		this.subTotalIncomeActualField = subTotalIncomeActualField;
	}

	public JTextField getSubTotalExpensesEstimatedField() {
		return subTotalExpensesEstimatedField;
	}

	public void setSubTotalExpensesEstimatedField(JTextField subTotalExpensesEstimatedField) {
		this.subTotalExpensesEstimatedField = subTotalExpensesEstimatedField;
	}

	public JTextField getSubTotalExpensesActualField() {
		return subTotalExpensesActualField;
	}

	public void setSubTotalExpensesActualField(JTextField subTotalExpensesActualField) {
		this.subTotalExpensesActualField = subTotalExpensesActualField;
	}

    
}
