import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import java.util.Random;


    public class GamePanel extends JPanel implements ActionListener {
        // VARIABLE LIST
        static final int SCREEN_WIDTH = 768;
        static final int SCREEN_HEIGHT = 600;
        static final int UNIT_SIZE = SCREEN_HEIGHT/24;
        static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
        static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyparts = 1;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        //SETS BACKGROUNDS
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        //ADDS THE APPLE APPEAR COMMAND AND STATES THAT IT IS INDEED RUNNING
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        //COMPONENTS FOR g
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        //DRAWS THE APPLE
        if(running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        //DRAWS THE BODY PARTS
            for (int i = 0; i < bodyparts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(5, 180, 200));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //DRAWS THE SCOREBOARD
            g.setColor(Color.yellow);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score"+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score"+applesEaten))/2,g.getFont().getSize());

        }
        else{
            //COMMAND FOR GAME OVER SCREEN
            gameOver(g);
        }
    }
    public void newApple(){
        //COMMAND FOR POSITION OF APPLE
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        //MAKES BODY PART MOVE
        for (int i = bodyparts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];

        }
        switch (direction) {
            //DECIDES CHARACTERS FOR DIRECTIONS OF SNAKE
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }
    public void checkApple(){
        //CHECKS IF SNAKE HEAD IS AT THE SAME POSITION AS APPLE
        if((x[0]==appleX)&&(y[0]==appleY)){
            bodyparts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //CHECKS IF SNAKE HEAD IS IN SAME POSITION AS ANY OF ITS BODY PARTS
        for(int i = bodyparts; i >0; i--){
            if((x[0]== x[i])&&(y[0]==y[i])){
                running = false;
            }
        }
        //CHECKS IF SNAKES HEAD HITS ANY WALL
        if(x[0]<0) {
            running = false;
        }
        if(x[0]>SCREEN_WIDTH) {
            running = false;
        }
        if(y[0]<0) {
            running = false;
        }
        if(y[0]>SCREEN_HEIGHT) {
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //CREATES GAME OVER LABEL
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
        //CREATES SCORE
        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score"+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score"+applesEaten))/2,g.getFont().getSize());
    }



    public void actionPerformed(ActionEvent e){
        if(running){
            //CALLS THE MOVE COMMAND AS WELL AS THE CHECK IF APPLE IS EATEN OR IF SNAKE COLLIDES
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            //ALLOWS YOU TO CONTROL SNAKE
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(direction != 'R'){
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(direction != 'L'){
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if(direction != 'D'){
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 'U'){
                    direction = 'D';
                }
                break;
            }
        }
    }
}