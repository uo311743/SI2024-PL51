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
    private JTextField totalIncomeEstimatedField, totalIncomePaidField, totalExpensesEstimatedField, totalExpensesPaidField;
    private JTextField balanceEstimatedField, balanceActualField;
    private JTextField subTotalSponsorshipIncomeEstimatedField, subTotalSponsorshipIncomeActualField;
    private JTextField subTotalSponsorshipExpensesEstimatedField, subTotalSponsorshipExpensesActualField;
    private JTextField subTotalSponsorshipBalanceEstimatedField, subTotalSponsorshipBalanceActualField;
    private JTextField subTotalIncomeEstimatedField, subTotalIncomeActualField;
    private JTextField subTotalExpensesEstimatedField, subTotalExpensesActualField;

    public ConsultStatusActivityView() {
        super("Consult Status Activity");
    }

    @Override
    protected void initialize() {
        this.createButtonLowLeft("Go back");

        totalIncomeEstimatedField = new JTextField(10);
        totalIncomeEstimatedField.setEditable(false);
        totalIncomePaidField = new JTextField(10);
        totalIncomePaidField.setEditable(false);
        totalExpensesEstimatedField = new JTextField(10);
        totalExpensesEstimatedField.setEditable(false);
        totalExpensesPaidField = new JTextField(10);
        totalExpensesPaidField.setEditable(false);
        balanceEstimatedField = new JTextField(10);
        balanceEstimatedField.setEditable(false);
        balanceActualField = new JTextField(10);
        balanceActualField.setEditable(false);
        subTotalSponsorshipIncomeEstimatedField = new JTextField(10);
        subTotalSponsorshipIncomeEstimatedField.setEditable(false);
        subTotalSponsorshipIncomeActualField = new JTextField(10);
        subTotalSponsorshipIncomeActualField.setEditable(false);
        subTotalSponsorshipExpensesEstimatedField = new JTextField(10);
        subTotalSponsorshipExpensesEstimatedField.setEditable(false);
        subTotalSponsorshipExpensesActualField = new JTextField(10);
        subTotalSponsorshipExpensesActualField.setEditable(false);
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
        estimatedPanel.add(totalIncomeEstimatedField);
        estimatedPanel.add(totalExpensesEstimated);
        estimatedPanel.add(totalExpensesEstimatedField);
        estimatedPanel.add(balanceEstimated);
        estimatedPanel.add(balanceEstimatedField);

        // Actual totals panel
        JPanel actualPanel = new JPanel();
        actualPanel.setLayout(new BoxLayout(actualPanel, BoxLayout.Y_AXIS));
        actualPanel.setBorder(BorderFactory.createTitledBorder("Actual"));
        JLabel totalIncomePaid = new JLabel("Income:");
        JLabel totalExpensesPaid = new JLabel("Expenses:");
        JLabel balanceActual = new JLabel("Balance:");
        actualPanel.add(totalIncomePaid);
        actualPanel.add(totalIncomePaidField);
        actualPanel.add(totalExpensesPaid);
        actualPanel.add(totalExpensesPaidField);
        actualPanel.add(balanceActual);
        actualPanel.add(balanceActualField);

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
        panel.add(subTotalSponsorshipIncomeEstimatedField);
        panel.add(subTotalSponsorshipIncomeActual);
        panel.add(subTotalSponsorshipIncomeActualField);
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

    public JTextField getTotalIncomeEstimatedField() {
        return totalIncomeEstimatedField;
    }

    public JTextField getTotalIncomePaidField() {
        return totalIncomePaidField;
    }

    public JTextField getTotalExpensesEstimatedField() {
        return totalExpensesEstimatedField;
    }

    public JTextField getTotalExpensesPaidField() {
        return totalExpensesPaidField;
    }

    public JTextField getBalanceEstimatedField() {
        return balanceEstimatedField;
    }

    public JTextField getBalanceActualField() {
        return balanceActualField;
    }

    public JTextField getSubTotalSponsorshipIncomeEstimatedField() {
        return subTotalSponsorshipIncomeEstimatedField;
    }

    public JTextField getSubTotalSponsorshipIncomeActualField() {
        return subTotalSponsorshipIncomeActualField;
    }

    public JTextField getSubTotalSponsorshipExpensesEstimatedField() {
        return subTotalSponsorshipExpensesEstimatedField;
    }

    public JTextField getSubTotalSponsorshipExpensesActualField() {
        return subTotalSponsorshipExpensesActualField;
    }

    public JTextField getSubTotalSponsorshipBalanceEstimatedField() {
        return subTotalSponsorshipBalanceEstimatedField;
    }

    public JTextField getSubTotalSponsorshipBalanceActualField() {
        return subTotalSponsorshipBalanceActualField;
    }

    public JTextField getSubTotalIncomeEstimatedField() {
        return subTotalIncomeEstimatedField;
    }

    public JTextField getSubTotalIncomeActualField() {
        return subTotalIncomeActualField;
    }

    public JTextField getSubTotalExpensesEstimatedField() {
        return subTotalExpensesEstimatedField;
    }

    public JTextField getSubTotalExpensesActualField() {
        return subTotalExpensesActualField;
    }

    public JTable getActivityTable() {
        return activityTable;
    }

    public JTable getSponsorshipTable() {
        return sponsorshipTable;
    }

    public JTable getIncomeTable() {
        return incomeTable;
    }

    public JTable getExpensesTable() {
        return expensesTable;
    }
}
