
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author RurarzŁukasz
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener{
    
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310; //pozycja startowa
    private int ballPositionX = 120; //pozycja piłki
    private int ballPositionY = 350; //pozycja piłki
    private int ballXdir = -1; //kierynek piłki
    private int ballYdir = -2;
    private MapGenerator map;
    
    public GamePlay() {
        map = new MapGenerator(3,7); //utworzenie obiektu odpowiedzialnego za kafelki
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    
    public void paint(Graphics g){
       
        
        
        // tło
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);
        
        map.draw((Graphics2D) g);
        //ramki
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(692,0,3,592);
        
        //paletka
        g.setColor(Color.green);
        g.fillRect(playerX, 550,100,8);
        
        //piłka
         g.setColor(Color.red);
         g.fillOval(ballPositionX ,ballPositionY,20, 20);
         
         //wynik
         g.setColor(Color.red);
         g.setFont( new Font("serif", Font.BOLD, 25));
         g.drawString(""+score, 590,30 );
         
         if(totalBricks <= 0 ){
             play = false;
             ballXdir = 0;
             ballYdir = 0;
             g.setColor(Color.blue);
             g.setFont( new Font("serif", Font.BOLD, 30));
             g.drawString("Gratulacje, wygrałeś Twój wynik: " + score, 190,300);
             
             g.setFont( new Font("serif", Font.BOLD, 20));
             g.drawString("Naciśnij enter aby zagrać ponownie ", 230,320);
         }
         
         if(ballPositionY > 570){
             play = false;
             ballXdir = 0;
             ballYdir = 0;
             g.setColor(Color.blue);
             g.setFont( new Font("serif", Font.BOLD, 30));
             g.drawString("Koniec Gry, Wynik: " + score, 190,300);
             
             g.setFont( new Font("serif", Font.BOLD, 20));
             g.drawString("Naciśnij enter aby zagrać ponownie ", 230,320);
             
         }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_RIGHT){
           if(playerX >= 600 ){
               playerX = 600;
           }
           else {
               moveRight();
           }
       }
       if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10 ){
               playerX = 10;
           }
           else {
               moveLeft();
           }
       }
       if(e.getKeyCode() == KeyEvent.VK_ENTER){
           if(!play){
               play = true;
               ballPositionX = 120;
               ballPositionY = 350;
               ballXdir = -1;
               ballYdir = -2;
               playerX = 310;
               score = 0;
               totalBricks = 21;
               map = new MapGenerator(3 , 7);
               repaint();
           }
       }
    }
    
    public void moveRight(){
        play = true;
        playerX+=20;
    }
     public void moveLeft(){
        play = true;
        playerX-=20;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       timer.start();
       
       //część odpowiedzialna za ruch piłki
       if(play) {
           
        if( new Rectangle(ballPositionX, ballPositionY, 20,20).intersects( new Rectangle(playerX, 550,100,8))){
            ballYdir = -ballYdir;
        }
        
        A: for( int i = 0; i < map.map.length; i++){
            for ( int j = 0; j < map.map[0].length; j++){
                if(map.map[i][j]>0){
                    int brickX = j * map.brickWidth+80;
                    int brickY = i * map.brickHeight+50;
                    int brickWidth = map.brickWidth;
                    int brickHeight = map.brickHeight;
                    
                    Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                    Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY,20, 20);
                    Rectangle brickRect = rect;
                    
                    if(ballRect.intersects(brickRect)){
                        map.setBrickValue(0, i, j);
                        totalBricks--;
                        score +=5;
                        
                        if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width){
                            ballXdir =- ballXdir;
                            
                        }
                        else{
                            ballYdir = -ballYdir;
                        }
                        break A;
                    }
                   
                }
            }
        }
        ballPositionX += ballXdir;
        ballPositionY += ballYdir;
        if (ballPositionX < 0 ){
            ballXdir = -ballXdir;
        }
        if (ballPositionY < 0 ){
            ballYdir = -ballYdir;
        }
        if (ballPositionX > 670 ){
            ballXdir = -ballXdir;
        }
    }
       
       repaint();
       
    }
    
    
}
