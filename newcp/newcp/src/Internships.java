import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Internships extends JFrame {

    private DefaultTableModel model;

    public Internships() {
        setTitle("Post Internship Vacancy");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"Company", "Position", "Location", "Skills", "Stipend", "Duration", "Eligibility"}, 0);
        JTable table = new JTable(model);
        loadTableData();

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Form to enter data
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        JTextField companyField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField skillsField = new JTextField();
        JTextField stipendField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField eligibilityField = new JTextField();

        formPanel.add(new JLabel("Company Name:"));
        formPanel.add(companyField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Required Skills:"));
        formPanel.add(skillsField);
        formPanel.add(new JLabel("Stipend:"));
        formPanel.add(stipendField);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(durationField);
        formPanel.add(new JLabel("Eligibility:"));
        formPanel.add(eligibilityField);

        JButton submitButton = new JButton("Post Internship");
        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.WHITE);

        submitButton.addActionListener(e -> {
            String company = companyField.getText();
            String position = positionField.getText();
            String location = locationField.getText();
            String skills = skillsField.getText();
            String stipend = stipendField.getText();
            String duration = durationField.getText();
            String eligibility = eligibilityField.getText();

            if (company.isEmpty() || position.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Company, Position, and Location are required fields!");
                return;
            }

            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/internship", "root", "root");
                String sql = "INSERT INTO job (company_name, position, location, required_skills, stipend, duration, eligibility) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, company);
                pstmt.setString(2, position);
                pstmt.setString(3, location);
                pstmt.setString(4, skills);
                pstmt.setString(5, stipend);
                pstmt.setString(6, duration);
                pstmt.setString(7, eligibility);

                pstmt.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Internship posted successfully!");
                clearFormFields(companyField, positionField, locationField, skillsField, stipendField, durationField, eligibilityField);
                loadTableData(); // Refresh the table

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(10, 102, 194));
        backButton.setForeground(Color.WHITE);

        backButton.addActionListener(e -> {
            new homepage(); // Assuming you have this class
            dispose();
        });

        bottomPanel.add(submitButton);
        bottomPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void clearFormFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void loadTableData() {
        model.setRowCount(0); // Clear existing
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/internship", "root", "root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM job");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("company_name"),
                        rs.getString("position"),
                        rs.getString("location"),
                        rs.getString("required_skills"),
                        rs.getString("stipend"),
                        rs.getString("duration"),
                        rs.getString("eligibility")
                });
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Internships::new);
    }
}
