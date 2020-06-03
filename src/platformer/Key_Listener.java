package platformer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/////////////////////////////////////////
//Function: Tracks the keyboard input for the game 
/////////////////////////////////////////
public class Key_Listener extends KeyAdapter{
	
	boolean[] keysPressed = new boolean[8];
	
	/////////////////////////////////////////
	//Function: Logs whether a key has been pressed in a list
	//Precondition: The keyboard input is properly detected and the array for their storage exists
	//Postcondition: Each key pressed is logged in an array as a true value
	/////////////////////////////////////////
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_UP)
			keysPressed[0]=true;
		if(key==KeyEvent.VK_DOWN)
			keysPressed[1]=true;
		if(key==KeyEvent.VK_LEFT)
			keysPressed[2]=true;
		if(key==KeyEvent.VK_RIGHT)
			keysPressed[3]=true;
		if(key==KeyEvent.VK_S)
			keysPressed[4]=true;
		if(key==KeyEvent.VK_SPACE)
			keysPressed[5]=true;
		if(key==KeyEvent.VK_ENTER)
			keysPressed[6]=true;
		if(key==KeyEvent.VK_ESCAPE)
			keysPressed[7]=true;
	}
	
	/////////////////////////////////////////
	//Function: Logs whether a key has been released in a list
	//Precondition: The keyboard input is properly detected and the array for their storage exists
	//Postcondition: Each key released is logged in an array as a false value
	/////////////////////////////////////////
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_UP)
			keysPressed[0]=false;
		if(key==KeyEvent.VK_DOWN)
			keysPressed[1]=false;
		if(key==KeyEvent.VK_LEFT)
			keysPressed[2]=false;
		if(key==KeyEvent.VK_RIGHT)
			keysPressed[3]=false;
		if(key==KeyEvent.VK_S)
			keysPressed[4]=false;
		if(key==KeyEvent.VK_SPACE)
			keysPressed[5]=false;
		if(key==KeyEvent.VK_ENTER)
			keysPressed[6]=false;
		if(key==KeyEvent.VK_ESCAPE){
			keysPressed[7]=false;
			if(Game.gameState==3)
				Game.gameState=6;
		}
	}
	
	/////////////////////////////////////////
	//Function: Returns a list of each game key and its current state (pressed or not pressed)
	//Precondition: The array for the key log exists
	//Postcondition: The array of game keys and their state is returned
	/////////////////////////////////////////
	public boolean[] getKeysPressed(){
		return keysPressed;
	}

}
