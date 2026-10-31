import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // atribut posisi dan ukuran pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeigth = 512;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    int pipeVelocityX = -2;

    View view;
    Image birdImage;
    Player player;

    // list pipa dan gambarnya
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    private boolean gameOver = false;
    private int score = 0;

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                placePipes();
            }
        });

        gameLoop = new Timer(1000/60, this);
    }

    public void startGame(){
        restartGame();
    }

    public void restartGame(){
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        pipes.clear();
        score = 0;
        gameOver = false;

        if (view != null){
            view.updateScore(score);
        }

        if (pipesCooldown.isRunning()){
            pipesCooldown.stop();
        }
        pipesCooldown.start();

        if (gameLoop.isRunning()){
            gameLoop.stop();
        }
        gameLoop.start();
    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer(){
        return player;
    }

    public ArrayList<Pipe> getPipes(){return pipes;}

    public boolean isGameOver(){
        return gameOver;
    }

    public int getScore(){
        return score;
    }

    public void placePipes(){
        if (gameOver) {
            return;
        }
        int randomPosY = (int) (pipeStartPosY - (pipeHeigth / 4) - Math.random() * (pipeHeigth / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe (pipeStartPosX, randomPosY, pipeWidth, pipeHeigth, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeigth, pipeWidth, pipeHeigth, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public boolean checkCollision(Player p, Pipe pipe){
        return p.getPosX() < pipe.getPosX() + pipe.getWidth() &&
               p.getPosX() + p.getWidth() > pipe.getPosX() &&
               p.getPosY() < pipe.getPosY() + pipe.getHeight() &&
               p.getPosY() + p.getHeight() > pipe.getPosY();
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // Cek jika menabrak batas bawah
        if (player.getPosY() + player.getHeight() >= frameHeight){
            gameOver = true;
        }

        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // Cek collision dengan pipa
            if (checkCollision(player, pipe)){
                gameOver = true;
            }

            // Tambah skor jika melewati pipa (hanya dihitung sekali per pasang pipa, cek upperPipe saja)
            if (!pipe.isPassed() && pipe.getImage() == upperPipeImage && player.getPosX() > pipe.getPosX() + pipe.getWidth()){
                pipe.setPassed(true);
                score++;
                if (view != null){
                    view.updateScore(score);
                }
            }
        }

        if (gameOver){
            gameLoop.stop();
            pipesCooldown.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        if(view != null){
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            if (!gameOver){
                player.setVelocityY(-9);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R){
            if (gameOver){
                restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){}
}
