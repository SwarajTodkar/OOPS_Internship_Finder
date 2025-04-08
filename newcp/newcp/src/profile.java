import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class profile extends JFrame {

    public profile() {
        setTitle("Candidate Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(0x0077B5));  // LinkedIn blue

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Candidate Profile Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0x0077B5));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(600, 50));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(emailField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0; gbc.gridy = 2; centerPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(phoneField, gbc);

        JLabel skillsLabel = new JLabel("Skills:");
        gbc.gridx = 0; gbc.gridy = 3; centerPanel.add(skillsLabel, gbc);

        JTextField skillsField = new JTextField(20);
        gbc.gridx = 1; centerPanel.add(skillsField, gbc);

        // Resume upload button
        JButton uploadResumeBtn = new JButton("Upload Resume");
        uploadResumeBtn.setBackground(new Color(0x00A0DC));
        uploadResumeBtn.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 4; centerPanel.add(uploadResumeBtn, gbc);
        uploadResumeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose a Resume File");
                int userSelection = fileChooser.showOpenDialog(profile.this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToUpload = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(profile.this, "Uploaded: " + fileToUpload.getAbsolutePath());
                }
            }
        });

        JButton registerBtn = new JButton("Save Profile");
        registerBtn.setBackground(new Color(0x00A0DC));
        registerBtn.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 5; centerPanel.add(registerBtn, gbc);
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String skills = skillsField.getText();

                // Database connection and insertion
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/internship", "root", "root")) {
                    String sql = "INSERT INTO candidate_info (name, email, phone, skills) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, email);
                    statement.setString(3, phone);
                    statement.setString(4, skills);
                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(profile.this, "Profile Saved Successfully!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(profile.this, "Error saving profile: " + ex.getMessage());
                }
            }
        });

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(0x00A0DC));
        backBtn.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 6; centerPanel.add(backBtn, gbc);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new homepage();
            }
        });

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new profile());
    }
}
