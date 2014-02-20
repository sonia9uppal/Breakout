package Breakout;
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 8;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 8;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
		(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");

	private static final int PAUSE_TIME = 20;
	//score
	private int s = 0;
	//lives
	private int l = 3;
	//rowsdone
	private int rd = 0;
	
	private double vy, vx;

	private RandomGenerator rgen = RandomGenerator.getInstance();

	GOval ball;
	GRect paddle;
	GLabel scorelabel;
	GLabel lives;
	
	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setup();
		vy = +3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		score();
		lives();
		while(true){
			moveBall();
			GObject obj = getElementAt(ball.getX(), ball.getY());
			if(obj == null){
				obj = getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
				if(obj == null){
					obj = getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
					if(obj == null){
						obj = getElementAt(ball.getX() ,ball.getY() + BALL_RADIUS);
						
					}
				}
			}

			if(obj != null){
				if(obj == paddle){
					bounceClip.play();
					vy = -vy;
				}else{
					bounceClip.play();
					remove(obj);
					s++;
					scorelabel.setLabel("Score: " +s);
					vy = -vy;
				}



			}
		}
		

		
	}







	//e.getX() and e.getY() give the mouse location
	//have to call addMouseListeners() 
	public void mouseMoved(MouseEvent e) {
		paddle.setLocation(e.getX() - PADDLE_WIDTH/2 , getHeight() - PADDLE_Y_OFFSET);
		//right
		if(paddle.getX() > getWidth() - PADDLE_WIDTH){
			paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET);
		}
		//left
		if(paddle.getX() < 0){
			paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
		}
	}
	private void endofgame(String message){
		GLabel message1 = new GLabel(message, getWidth()/2, getHeight()/2);
		message1.setFont(new Font("Zapfino", 0, 32));
		message1.setColor(Color.BLACK);
		add(message1);
	
		
		
	}
	
	private void score(){
		scorelabel = new GLabel("Score: " +s, 10, 55);
		add(scorelabel);
		

	}
	
	private void lives(){
		lives = new GLabel("Lives:" + l, 275, 55);
		add(lives);		
	}
	private void setup(){
		drawPaddle();
		addMouseListeners();
		createBricks();
		drawBall();	
	}

	private void createBricks(){
		int yPosition = 80;
		for(int z=0; z<NBRICK_ROWS; z++){
			int xPosition = (getWidth() -  BRICK_WIDTH * NBRICKS_PER_ROW)/15;
			for(int x=0; x<NBRICKS_PER_ROW; x++){
				GRect brick = new GRect(xPosition, yPosition, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				//start of color
				if(rd == 0 || rd == 1) {
					brick.setColor(new Color(17,69,138));
					brick.setFillColor(new Color(17,69,138));
				}
				if(rd == 2 || rd == 3) {
					brick.setColor(Color.orange);
					brick.setFillColor(Color.orange);
					//brick.setColor(new Color(245,137,29));
					//brick.setFillColor(new Color(245,137,29));
				}
				if(rd == 4 || rd == 5) {
					//brick.setColor(Color.yellow);
					//brick.setFillColor(Color.yellow);
					brick.setColor(new Color(250,231,27));
					brick.setFillColor(new Color(250,231,27));
				}
				if(rd == 6 || rd == 7) {
					brick.setColor(new Color(108, 237, 28));
					brick.setFillColor(new Color(108,237,28));
				}
				//end of color
				add(brick);
				xPosition = xPosition + BRICK_WIDTH + BRICK_SEP;
			}
			yPosition = yPosition + BRICK_HEIGHT + BRICK_SEP;
			rd++;
		}

	}
	private void drawPaddle(){
		paddle = new GRect(getWidth()/2, getHeight()-PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(Color.black);
		paddle.setFilled(true);
		paddle.setFillColor(Color.black);
		add(paddle);

	}

	private void drawBall(){
		ball = new GOval (190, 340, BALL_RADIUS, BALL_RADIUS);
		ball.setColor(Color.orange);
		ball.setFilled(true);
		ball.setFillColor(Color.orange);
		add(ball);
	}

		
	
	private void moveBall(){
		ball.move(vx,vy);
		pause(PAUSE_TIME);
		if(ball.getY() <= 0){
			vy=-vy;
		}
		if(ball.getY() >= getHeight() - BALL_RADIUS){
			l--;
			lives.setLabel("Lives: " +l);
			ball.setLocation(90, 340);
			pause(PAUSE_TIME*3);
		}	
		if(ball.getX() <= 0){
			vx = -vx;
		}
		if(ball.getX() >= getWidth() - BALL_RADIUS){
			vx=-vx;
		}


	}




	//private void moveBall(){
	/*while(true){
		pause(PAUSE_TIME);
		if(vy == true){
			ball.move(xspeed, yspeed);

			double circleY = ball.getY();
			if(circleY >= getWidth() - ball.getWidth()){
				vy=false;

			}
		}else{
			ball.move(-xspeed, -yspeed);

			double circleY = ball.getY();
			if(circleY <= 100){
				vy = true;
			}
			speed = speed - speedDecrease;
			}
}
	 */

}
