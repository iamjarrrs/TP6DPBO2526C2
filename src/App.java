import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FlappyBird");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(360, 640);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            Logic logika = new Logic();
            View tampilan = new View(logika);
            logika.setView(tampilan);

            // Main Menu Panel
            JPanel menuPanel = new JPanel() {
                Image bg = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (bg != null) {
                        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                    }
                }
            };
            menuPanel.setPreferredSize(new Dimension(360, 640));
            menuPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel titleLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 40, 10);
            menuPanel.add(titleLabel, gbc);

            gbc.insets = new Insets(10, 10, 10, 10);
            JButton startButton = new JButton("Start Game");
            startButton.setFont(new Font("Arial", Font.BOLD, 18));
            startButton.setFocusPainted(false);
            gbc.gridy = 1;
            menuPanel.add(startButton, gbc);

            JButton exitButton = new JButton("Exit");
            exitButton.setFont(new Font("Arial", Font.BOLD, 18));
            exitButton.setFocusPainted(false);
            gbc.gridy = 2;
            menuPanel.add(exitButton, gbc);

            mainPanel.add(menuPanel, "MENU");
            mainPanel.add(tampilan, "GAME");

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "GAME");
                    tampilan.requestFocusInWindow();
                    logika.startGame();
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            frame.add(mainPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}