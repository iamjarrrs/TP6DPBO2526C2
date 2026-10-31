import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;
    private Logic logic;
    private JLabel scoreLabel;
    private Image backgroundImage;

    public View(Logic logic){
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(20, 20, 200, 30);
        add(scoreLabel);

        setFocusable(true);
        addKeyListener(logic);
    }

    public void updateScore(int score){
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        }

        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
        }

        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null){
            for (int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        if (logic.isGameOver()){
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g.getFontMetrics();
            String gameOverText = "GAME OVER";
            int goX = (width - fm.stringWidth(gameOverText)) / 2;
            g.drawString(gameOverText, goX, height / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            fm = g.getFontMetrics();
            String restartText = "Press 'R' to Restart";
            int rX = (width - fm.stringWidth(restartText)) / 2;
            g.drawString(restartText, rX, height / 2 + 25);
        }
    }
}
