import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class homepage extends JFrame {

    public homepage() {
        setTitle("Internship Management");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Internship Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(new Color(10, 102, 194));
        titleLabel.setPreferredSize(new Dimension(400, 60));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to Intern...!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(10, 102, 194));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        centerPanel.add(welcomeLabel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);

        JButton loginBtn = createStyledButton("Register", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openregisterpage();
            }
        });
        JButton jobsBtn = createStyledButton("Internships", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openIntershipspage();
            }
        });
        JButton searchCandidatesBtn = createStyledButton("Search Internships", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opensearchintershippage();
            }
        });
        JButton postVacancyBtn = createStyledButton("Vacancies", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openvacanciespage();
            }
        });

        gbc.gridy = 0;
        buttonPanel.add(loginBtn, gbc);
        gbc.gridy = 1;
        buttonPanel.add(jobsBtn, gbc);
        gbc.gridy = 2;
        buttonPanel.add(searchCandidatesBtn, gbc);
        gbc.gridy = 3;
        buttonPanel.add(postVacancyBtn, gbc);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(10, 102, 194));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.addActionListener(actionListener);
        return button;
    }

    private void openregisterpage() {
        new profile();
        dispose();
    }

    private void openIntershipspage() {
        new Internships();
        dispose();
    }

    private void opensearchintershippage() {
        new SearchInternships();
        dispose();
    }

    private void openvacanciespage() {
        new Vacancy();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(homepage::new);
    }
}
