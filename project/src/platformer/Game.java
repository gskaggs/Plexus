package platformer;

/**
 * @author Jacob McCalla, Graet Gaemers 
 * Purpose: Creates the game, manages the game state, and runs the main game loop
 */
/////////////////////////////////////////
//Function: Creates the game, manages the game state, and runs the main game loop
/////////////////////////////////////////
public class Game{
	
	public static final int WIDTH=800, HEIGHT=600;
	private boolean running = false;
	static AssetManager manager;
	public static int gameState = 0;
	private static long lastTime;
	
	public Game(){
		manager = new AssetManager();
		manager.setDoubleBuffered(true);
		new Window(WIDTH,HEIGHT,"PLEXUS",manager);
		AssetManager.playBackgroundMusic(FileIO.importSound(0));
		running=true;
		run();
	}

	/////////////////////////////////////////
	//Function: Contains the game loop which controls the timing and calling of game and window updates
	//Precondition: The Game class constructor has been called
	//Postcondition: The game loop will continue indefinitely until stopped
	/////////////////////////////////////////
	public void run() {
		lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000/amountofTicks; //Speed at which the game updates
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames=0;
		while(running){
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime=now;
			while(delta>=1){ //Accounts for lag by doing multiple updates if the updates were previously delayed
				tick();	  //Updates the game
				delta--;
				}
			render(); //Updates the game
			frames++;
			if(System.currentTimeMillis()-timer>1000){ //Calculates and prints the frame rate
				timer+=1000;
				System.out.println("Frames: "+frames);
				frames=0;
			}
		}
	}
	
	/////////////////////////////////////////
	//Function: Calls the correct tick method depending on the current game state
	//Precondition: gameState has a value greater than 0 and less than 7
	//Postcondition: Calls the necessary tick method associated with a given game state
	/////////////////////////////////////////
	private void tick(){
		switch(gameState){ //Calls the correct tick method depending on the current state of the game
		case 0:
			CutscenesManager.gameIntro();
			break;
		case 1:
			CutscenesManager.mainMenu();
			break;
		case 2:
			CutscenesManager.introCutscene();
			break;
		case 3:
			AssetManager.tick();
			break;
		case 4:
			CutscenesManager.transitionCutscene();
			break;
		case 5:
			CutscenesManager.finalCutscene();
			break;
		case 6:
			CutscenesManager.pauseMenu();
			break;
		case 7:
			CutscenesManager.optionsMenu();
			break;
		default:
			System.out.println("Invalid game State");
		break;
		}
	}
	
	/////////////////////////////////////////
	//Function: Calls the render method of the AssetManager
	//Precondition: the manager object has been created
	//Postcondition: the repaint method for the manager object is called
	/////////////////////////////////////////
	private void render(){
		manager.repaint();
	}
	
	/////////////////////////////////////////
	//Function: Creates the game
	//Precondition: None
	//Postcondition: The game is created
	/////////////////////////////////////////
	public static void main(String[] args) {
		new Game(); //Creates the game
	}
}