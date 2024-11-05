package try2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class cash extends JFrame implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Double> incomeList = new HashMap<>();
    private HashMap<String, Double> expenseList = new HashMap<>();
    private double savingsGoal = 0;
    private double amountSaved = 0;

    // Components
    private JTextField incomeField, expenseField, savingsGoalField, savedAmountField;
    private JButton addIncomeButton, addExpenseButton, setGoalButton, updateSavedButton, closeEntryButton, viewSummaryButton;
    private JComboBox<String> incomeCategoryDropdown, expenseCategoryDropdown;
    private JLabel totalIncomeLabel, totalExpenseLabel, netCashFlowLabel, goalProgressLabel;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    // Linked List to store the order of incomes and expenses
    private LinkedList<Node> nodesList = new LinkedList<>();

    // Constructor
    public cash() {
        setTitle("Cash Flow Minimizer");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top panel for title
        JLabel headerLabel = new JLabel("Cash Flow Minimizer Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel for input and data
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Set padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Ensure components expand horizontally

        // Row 1: Income Category Dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Income Category:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        incomeCategoryDropdown = new JComboBox<>(new String[]{"Salary", "Investment", "Gift", "Bonus"});
        centerPanel.add(incomeCategoryDropdown, gbc);

        // Row 2: Add Income Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Add Income (Amount):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        incomeField = new JTextField(10);
        centerPanel.add(incomeField, gbc);

        gbc.gridx = 2;
        addIncomeButton = new JButton("Add Income");
        addIncomeButton.addActionListener(this);
        centerPanel.add(addIncomeButton, gbc);

        // Row 3: Expense Category Dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Expense Category:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        expenseCategoryDropdown = new JComboBox<>(new String[]{"Rent", "Utilities", "Groceries", "Entertainment"});
        centerPanel.add(expenseCategoryDropdown, gbc);

        // Row 4: Add Expense Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Add Expense (Amount):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        expenseField = new JTextField(10);
        centerPanel.add(expenseField, gbc);

        gbc.gridx = 2;
        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(this);
        centerPanel.add(addExpenseButton, gbc);

        // Row 5: Savings Goal Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Set Savings Goal:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        savingsGoalField = new JTextField(10);
        centerPanel.add(savingsGoalField, gbc);

        gbc.gridx = 2;
        setGoalButton = new JButton("Set Goal");
        setGoalButton.addActionListener(this);
        centerPanel.add(setGoalButton, gbc);

        // Row 6: Update Savings Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(new JLabel("Update Savings Amount:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        savedAmountField = new JTextField(10);
        centerPanel.add(savedAmountField, gbc);

        gbc.gridx = 2;
        updateSavedButton = new JButton("Update Savings");
        updateSavedButton.addActionListener(this);
        centerPanel.add(updateSavedButton, gbc);

        // Row 7: Close Entry Button
        gbc.gridx = 1;
        gbc.gridy = 6;
        closeEntryButton = new JButton("Close Entry");
        closeEntryButton.addActionListener(this);
        centerPanel.add(closeEntryButton, gbc);

        // Row 8: View Summary Button
        gbc.gridx = 2;
        gbc.gridy = 6;
        viewSummaryButton = new JButton("View Summary");
        viewSummaryButton.addActionListener(this);
        centerPanel.add(viewSummaryButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for summary and stats
        JPanel bottomPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        totalIncomeLabel = new JLabel("Total Income: $0.0");
        totalExpenseLabel = new JLabel("Total Expenses: $0.0");
        netCashFlowLabel = new JLabel("Net Cash Flow: $0.0");
        goalProgressLabel = new JLabel("Savings Progress: 0%");

        bottomPanel.add(totalIncomeLabel);
        bottomPanel.add(totalExpenseLabel);
        bottomPanel.add(netCashFlowLabel);
        bottomPanel.add(goalProgressLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Left panel for Graph Visualization
        add(createGraphPanel(), BorderLayout.WEST);

        // Window settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createGraphPanel() {
        dataset = new DefaultCategoryDataset();
        dataset.addValue(0, "Income", "Total Income");
        dataset.addValue(0, "Expenses", "Total Expenses");

        JFreeChart chart = ChartFactory.createBarChart(
                "Income vs Expenses", "Category", "Amount", dataset
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 300));
        return chartPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addIncomeButton) {
            addIncome();
        } else if (e.getSource() == addExpenseButton) {
            addExpense();
        } else if (e.getSource() == setGoalButton) {
            setSavingsGoal();
        } else if (e.getSource() == updateSavedButton) {
            updateSavings();
        } else if (e.getSource() == closeEntryButton) {
            closeEntry();
        } else if (e.getSource() == viewSummaryButton) {
            viewSummary();
        }
    }

    // Method to add income
    private void addIncome() {
        try {
            String category = incomeCategoryDropdown.getSelectedItem().toString();
            double amount = Double.parseDouble(incomeField.getText());

            incomeList.put(category, incomeList.getOrDefault(category, 0.0) + amount);
            nodesList.add(new Node(category, amount, "income"));
            incomeField.setText("");
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid income amount.");
        }
    }

    // Method to add expense
    private void addExpense() {
        try {
            String category = expenseCategoryDropdown.getSelectedItem().toString();
            double amount = Double.parseDouble(expenseField.getText());

            expenseList.put(category, expenseList.getOrDefault(category, 0.0) + amount);
            nodesList.add(new Node(category, amount, "expense"));
            expenseField.setText("");
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid expense amount.");
        }
    }

    // Method to close income and expense entries and save remaining balance
    private void closeEntry() {
        double totalIncome = incomeList.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpense = expenseList.values().stream().mapToDouble(Double::doubleValue).sum();
        double remainingAmount = totalIncome - totalExpense;

        if (remainingAmount > 0) {
            amountSaved += remainingAmount;  // Add remaining income to savings
        }
        incomeList.clear();
        expenseList.clear();
        updateSummary();
    }

    // Method to set the savings goal
    private void setSavingsGoal() {
        try {
            savingsGoal = Double.parseDouble(savingsGoalField.getText());
            savingsGoalField.setText("");
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid savings goal.");
        }
    }

    // Method to update the amount saved
    private void updateSavings() {
        try {
            amountSaved += Double.parseDouble(savedAmountField.getText());
            savedAmountField.setText("");
            updateSummary();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid savings amount.");
        }
    }

 // Method to view summary (income, expenses, and savings progress) in linked list format using Canvas
    private void viewSummary() {
        // Create a frame to display the linked list summary
        JFrame summaryFrame = new JFrame("Income and Expense Summary (Linked List)");
        summaryFrame.setSize(800, 600);
        summaryFrame.setLayout(new BorderLayout());

        // Create a custom canvas for drawing the linked list
        SummaryCanvas summaryCanvas = new SummaryCanvas(nodesList);
        
        // Add the canvas to the frame
        summaryFrame.add(summaryCanvas, BorderLayout.CENTER);

        summaryFrame.setVisible(true);
    }

    // Custom canvas class for drawing linked list with boxes and arrows
    class SummaryCanvas extends JPanel {
        private LinkedList<Node> nodes;

        // Constructor
        public SummaryCanvas(LinkedList<Node> nodes) {
            this.nodes = nodes;
        }

        // Overriding the paintComponent method to draw nodes and arrows
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = 50; // Initial x position for the nodes
            int y = 100; // Initial y position for the nodes
            int boxWidth = 150;
            int boxHeight = 50;
            int arrowLength = 50;

            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);

                // Set color based on node type
                if (node.type.equals("income")) {
                    g2.setColor(Color.GREEN);
                } else if (node.type.equals("expense")) {
                    g2.setColor(Color.RED);
                } else if (node.type.equals("savings")) {
                    g2.setColor(Color.BLUE);
                }

                // Draw the rectangle (box) for the node
                g2.fillRect(x, y, boxWidth, boxHeight);

                // Draw the border for the rectangle
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, boxWidth, boxHeight);

                // Draw the text inside the rectangle (node label and amount)
                g2.setColor(Color.BLACK);
                g2.drawString(node.label + ": $" + node.amount, x + 10, y + 25);

                // Draw the arrow to the next node, if it's not the last node
                if (i < nodes.size() - 1) {
                    g2.setColor(Color.BLACK);
                    // Draw horizontal arrow
                    g2.drawLine(x + boxWidth, y + boxHeight / 2, x + boxWidth + arrowLength, y + boxHeight / 2);
                    // Draw arrowhead
                    g2.drawLine(x + boxWidth + arrowLength - 10, y + boxHeight / 2 - 5, x + boxWidth + arrowLength, y + boxHeight / 2);
                    g2.drawLine(x + boxWidth + arrowLength - 10, y + boxHeight / 2 + 5, x + boxWidth + arrowLength, y + boxHeight / 2);
                }

                // Move to the next position
                x += boxWidth + arrowLength;
            }
        }
    }

    // Node class to store linked list elements
    class Node {
        String label;
        double amount;
        String type;  // Can be "income", "expense", or "savings"

        Node(String label, double amount, String type) {
            this.label = label;
            this.amount = amount;
            this.type = type;
        }
    }



    // Method to update summary labels and chart
    private void updateSummary() {
        double totalIncome = incomeList.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpense = expenseList.values().stream().mapToDouble(Double::doubleValue).sum();
        double netCashFlow = totalIncome - totalExpense;
        double savingsProgress = savingsGoal > 0 ? (amountSaved / savingsGoal) * 100 : 0;

        totalIncomeLabel.setText("Total Income: $" + totalIncome);
        totalExpenseLabel.setText("Total Expenses: $" + totalExpense);
        netCashFlowLabel.setText("Net Cash Flow: $" + netCashFlow);
        goalProgressLabel.setText(String.format("Savings Progress: %.2f%%", savingsProgress));

        // Update dataset for the chart
        dataset.setValue(totalIncome, "Income", "Total Income");
        dataset.setValue(totalExpense, "Expenses", "Total Expenses");
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new cash());
    }

    // Inner class to represent a node (income or expense entry)
   
}
