import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Vacancy extends JFrame {

    public Vacancy() {
        super("Job Vacancies");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        // Updated columns
        model.addColumn("Company Name");
        model.addColumn("Position");
        model.addColumn("Total Vacancies");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/internship", "root", "root");
            Statement stmt = conn.createStatement();

            // Match the updated column names
            ResultSet rs = stmt.executeQuery("SELECT company_name, position, total_vacancies FROM vacancy");

            while (rs.next()) {
                String companyName = rs.getString("company_name");
                String position = rs.getString("position");
                int vacancies = rs.getInt("total_vacancies");

                model.addRow(new Object[]{companyName, position, vacancies});
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Back Button Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setBackground(new Color(10, 102, 194));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> {
            new homepage(); // Redirect to your homepage
            dispose();
        });

        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Vacancy::new);
    }
}
