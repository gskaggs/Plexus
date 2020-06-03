package platformer;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/////////////////////////////////////////
//Function: Creates the game window for the game
/////////////////////////////////////////
public class Window{
	public static JFrame frame;
	
	public Window(int WIDTH, int HEIGHT, String name, AssetManager manager){
		frame = new JFrame(name);
		frame.setPreferredSize(new Dimension(WIDTH+6,HEIGHT+35));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.add(manager);
		frame.setVisible(true);	
	}
	
	/////////////////////////////////////////
	//Function: Closes the game's window
	//Precondition: The method is called
	//Postcondition: The game window is closed
	/////////////////////////////////////////
	public static void closeFrame(){
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
