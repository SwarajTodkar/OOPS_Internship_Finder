import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class SearchInternships extends JFrame {

    private JTextField searchField;
    private JButton searchButton, applyButton, backButton;
    private JTable companyTable;
    private DefaultTableModel tableModel;
    private Connection conn;

    public SearchInternships() {
        setTitle("Search Internships by Skill");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 248, 248));

        JLabel titleLabel = new JLabel("Search Internships by Skill", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(10, 102, 194));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(800, 60));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(248, 248, 248));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setMaximumSize(new Dimension(400, 30));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(10, 102, 194));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(100, 30));

        searchButton.addActionListener(e -> searchCompaniesBySkill(searchField.getText()));

        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(searchPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Updated column names
        String[] columnNames = {
                "Company Name", "Position", "Required Skills",
                "Location", "Stipend", "Duration", "Eligibility"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        companyTable = new JTable(tableModel);
        companyTable.setFont(new Font("Arial", Font.PLAIN, 14));
        companyTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(companyTable);
        scrollPane.setPreferredSize(new Dimension(750, 250));

        centerPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        applyButton = new JButton("Apply");
        applyButton.setFont(new Font("Arial", Font.BOLD, 16));
        applyButton.setBackground(new Color(10, 102, 194));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.setPreferredSize(new Dimension(100, 30));

        applyButton.addActionListener(e -> applyToCompany());

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(10, 102, 194));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(100, 30));

        backButton.addActionListener(e -> {
            dispose();
            new homepage(); // Replace with your homepage class
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(backButton);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);

        initDatabaseConnection();
    }

    private void initDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/internship";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCompaniesBySkill(String skill) {
        tableModel.setRowCount(0);  // Clear existing rows
        ArrayList<String[]> companies = getCompaniesForSkill(skill);

        if (companies.isEmpty()) {
            tableModel.addRow(new String[]{"No internships found for: " + skill, "", "", "", "", "", ""});
        } else {
            for (String[] company : companies) {
                tableModel.addRow(company);
            }
        }
    }

    private ArrayList<String[]> getCompaniesForSkill(String skill) {
        ArrayList<String[]> companies = new ArrayList<>();
        String query = "SELECT company_name, position, required_skills, location, stipend, duration, eligibility FROM job WHERE required_skills LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + skill + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] companyInfo = {
                        rs.getString("company_name"),
                        rs.getString("position"),
                        rs.getString("required_skills"),
                        rs.getString("location"),
                        String.format("₹%.2f", rs.getDouble("stipend")),
                        rs.getString("duration"),
                        rs.getString("eligibility")
                };
                companies.add(companyInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    private void applyToCompany() {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow != -1) {
            String companyName = (String) tableModel.getValueAt(selectedRow, 0);
            String position = (String) tableModel.getValueAt(selectedRow, 1);
            JOptionPane.showMessageDialog(this,
                    "Applied to " + position + " at " + companyName,
                    "Application Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a company to apply",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchInternships::new);
    }
}
