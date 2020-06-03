package platformer;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;

/////////////////////////////////////////
//Function: Manages all game assets and graphics, calling updates and rendering the screen. Manages the game.
/////////////////////////////////////////
public class AssetManager extends JComponent {

	private static final long serialVersionUID = -3468031457381995376L;
	static ArrayList<Entity> entities = new ArrayList<Entity>();
	static ArrayList<BufferedImage[][]> entitySprites = new ArrayList<BufferedImage[][]>();
	static BufferedImage background,drawboard= new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
	static boolean[] gameProgress;
	static int cameraX,cameraY,levelWidth,currentLevel=0;
	static Clip clipM, clipC;
	static float volume=-5f;
	static BufferedImage fade = new BufferedImage(Game.WIDTH,Game.HEIGHT,BufferedImage.TYPE_INT_RGB);
	static Key_Listener keyListen = new Key_Listener();
	static boolean[] keysPressed=keyListen.keysPressed;
	
	public AssetManager(){
		this.setFocusable(true);
		this.addKeyListener(keyListen);
	}
	
	/////////////////////////////////////////
	//Function: Calls the tick method of all current game entities within range of the player
	//Precondition: entities exist for the method to update
	//Postcondition: All entities within range of the player are updated based on their tick() methods
	/////////////////////////////////////////
	public static void tick(){
		entities.get(0).tick();
		for(int i=1;i<entities.size();i++){
			if(isWithinRange(entities.get(i)))
				entities.get(i).tick();	
		}
	}
	
	/////////////////////////////////////////
	//Function: Draws to the screen the correct entity and background graphics
	//Precondition: The graphics to be rendered exist and are associated with the correct entities
	//Postcondition: The screen is updated based on game data
	/////////////////////////////////////////
	public void paintComponent(Graphics g){
		if(cameraX<0)
			cameraX=0;
		//super.paintComponent(g);
		Toolkit.getDefaultToolkit().sync();
		Graphics2D g2 = (Graphics2D)drawboard.getGraphics(); //Creates a image to act as a buffer
		if(Game.gameState==3){
			if(background!=null){
				int offset=(cameraX%background.getWidth())+Game.WIDTH-background.getWidth();
				if(offset>0){
					g2.drawImage(background.getSubimage(cameraX%background.getWidth(), cameraY, background.getWidth()-(cameraX%background.getWidth()), Game.HEIGHT),0,0,null);	//Draws the looping background if the camera is
					g2.drawImage(background.getSubimage(0, cameraY, offset+50, Game.HEIGHT),background.getWidth()-(cameraX%background.getWidth()),0,null);						//centered on an area where the background loops
				}
				else
					g2.drawImage(background.getSubimage(cameraX%background.getWidth(), cameraY, Game.WIDTH, Game.HEIGHT),0,0,null);	//Draws the background a place where the background loops is not visible
				for(int i=1;i<entities.size();i++){ // Renders all entities currently on the screen
					if(isOnScreen(entities.get(i))&&entitySprites.get(i)!=null) //Ensures that the entity hasa graphics associated with it before drawing
						entities.get(i).render(entitySprites.get(i),(Graphics2D)g2,-cameraX,0);	
				}
				if(entities.get(0) instanceof Player)
				entities.get(0).render(entitySprites.get(0),(Graphics2D)g2,-cameraX,0);	 //Renders the player last so it is never obscured
			}
			else {
				g2.setColor(Color.BLACK);		//Cuts to a black screen if there is no background
				g2.drawRect(0, 0, Game.WIDTH, Game.HEIGHT);
			}
		}
		if(drawboard!=null){
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
			g.drawImage(drawboard, 0,0,null); //Draws the graphics buffer to the screen
		}
		else{
			g.setColor(Color.BLACK); //Cuts to a black screen if nothing is drawn
			g.drawRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}
		g.dispose();
		g2.dispose();
		
	}
	
	/////////////////////////////////////////
	//Function: Adds an entity and its graphics to the current list of game entities
	//Precondition: The entity and their sprites are correctly passed to the method
	//Postcondition: An entity and is associated graphics are added to the game
	/////////////////////////////////////////
	public static void addEntity( Entity entity, BufferedImage[][] sprites){
		entities.add(entity);
		entitySprites.add(sprites);
	}
	
	/////////////////////////////////////////
	//Function: Adds a Player to the list of entities
	//Precondition: the new player and their sprites are correctly passed to the method
	//Postcondition: A new Player is added to the game, replacing the existing one if one already exists
	/////////////////////////////////////////
	public static void addPlayer(Entity entity, BufferedImage[][] sprites, int x, int y){
		if(entities.get(0) instanceof Player){
			entities.set(0,entity);
			entitySprites.set(0,sprites);
		}
		else{
			entities.add(0,entity);
			entitySprites.add(0,sprites);
		}
		((Player)entity).centerCamera();
		((Player)entity).xSpawn=x;
		((Player)entity).ySpawn=y;

	}
	
	/////////////////////////////////////////
	//Function: Removes an entity and its graphics from the list of entities
	//Precondition: The entity to be removed exists within the list of entities
	//Postcondition: The given entity and its associate graphics are removed from the game
	/////////////////////////////////////////
	public static void removeEntity(Entity i){
		int index=entities.indexOf(i);
		entitySprites.remove(index);
		entities.remove(index);
	}
	
	/////////////////////////////////////////
	//Function: Returns the list of game entities
	//Precondition: The list of entities exists
	//Postcondition: The list of entities is returned
	/////////////////////////////////////////
	public static ArrayList<Entity> getEntities(){
		return entities;
	}
	
	/////////////////////////////////////////
	//Function: Sets the current game background image to a given Image
	//Precondition: The background image is correctly passed
	//Postcondition: The games background image is set to the given image
	/////////////////////////////////////////
	public static void setBackgroundImage(BufferedImage backgroundImage){
		background=backgroundImage;
	}
	
	/////////////////////////////////////////
	//Function: Returns the list of current keyboard keys pressed
	//Precondition: the array of keys currently pressed exists
	//Postcondition: the list of current keyboard keys pressed is returned
	/////////////////////////////////////////
	public static boolean[] getKeysPressed(){
		return keysPressed;
	}
	
	/////////////////////////////////////////
	//Function: sets the location of the game's Camera
	//Precondition: The variables for the camera's position are correctly passed to the method
	//Postcondition: The game's camera is set to a new location
	/////////////////////////////////////////
	public static void setCamera(int camerax, int cameray){
		cameraX=camerax;
		cameraY=cameray;
	}
	
	/////////////////////////////////////////
	//Function: Sets the x location of the camera
	//Precondition: The x location of the camera is correctly passed to the method
	//Postcondition: The game's camera is set to a new x position
	/////////////////////////////////////////
	public static void setCameraX(int camerax){
		cameraX=camerax;
	}
	
	/////////////////////////////////////////
	//Function: Sets the y location of the camera
	//Precondition: The y location of the camera is correctly passed to the method
	//Postcondition: The game's camera is set to a new y position
	/////////////////////////////////////////
	public static void setCameraY(int cameray){
		cameraY=cameray;
	}
	
	/////////////////////////////////////////
	//Function: Returns the x location of the camera's position
	//Precondition: the variable for the camera's x position exists
	//Postcondition: The game camera's current x position is returned
	/////////////////////////////////////////
	public static int getCameraX(){
		return cameraX;
	}
	
	/////////////////////////////////////////
	//Function: Returns the y location of the camera's position
	//Precondition: the variable for the camera's y position exists
	//Postcondition: The game camera's current y position is returned
	/////////////////////////////////////////
	public static int getCameraY(){
		return cameraY;
	}
	
	/////////////////////////////////////////
	//Function: Returns the width of the current game background
	//Precondition: The background image exists
	//Postcondition: The width of the current background image returned
	/////////////////////////////////////////
	public static int getBackgroundWidth(){
		return background.getWidth();
	}
	
	/////////////////////////////////////////
	//Function: Returns the height of the current game background
	//Precondition: The background image exists
	//Postcondition: The height of the current background image returned
	/////////////////////////////////////////
	public static int getBackgroundHeight(){
		return background.getHeight();
	}	
	/////////////////////////////////////////
	//Function: Returns a list of the current entities that are on screen in the game
	//Precondition: The list of game entities exists
	//Postcondition: A list of all game entities currently visible in the game are returned
	/////////////////////////////////////////
	public static Entity[] getOnScreenEntities(){
		ArrayList<Entity> onScreenEntities = new ArrayList<Entity>();
		for(int i=0; i<entities.size();i++){
			if(isOnScreen(entities.get(i)))
				onScreenEntities.add(entities.get(i));
		}
		Entity[] temp= new Entity[onScreenEntities.size()];
		return entities.toArray(temp);
	}
	
	/////////////////////////////////////////
	//Function: Returns true if the given entity is on screen in the game, otherwise returns false
	//Precondition: the entity is correctly passed to the method
	//Postcondition: Return a true/false value depending on if an entity is visible in the game
	/////////////////////////////////////////
	public static boolean isOnScreen(Entity entity){
		if(entity.getY()+100>=cameraY && entity.getY()<=cameraY+Game.HEIGHT){ //Checks to see if the entity is visible
			if(entity.getX()+100>=cameraX && entity.getX()<=cameraX+Game.WIDTH)
				return true;
			else return false;
		}
		else return false;
	}
	
	/////////////////////////////////////////
	//Function: Returns true if the given entity may soon need to be rendered, otherwise returns false
	//Precondition: The entity is correctly passed to the method
	//Postcondition:k Returns a true/false depending on if an entity nearby to the game's camera
	/////////////////////////////////////////
	public static boolean isWithinRange(Entity entity){
		if(entity.getY()+1000>=cameraY && entity.getY()-100<=cameraY+Game.HEIGHT){
			if(entity instanceof Enemy){
				if(entity.getX()+600>=cameraX && entity.getX()-600<=cameraX+Game.WIDTH) //Updates enemies even if they are far off screen to ensure that moving enemies return
					return true;
				else return false;
			}
			else 
			if(entity.getX()+100>=cameraX && entity.getX()<=cameraX+Game.WIDTH)
				return true;
			else return false;
		}
		else return false;
	}
	
	/////////////////////////////////////////
	//Function: Plays a given audio clip as the game's background music in a continuous loop
	//Precondition: The given audio clip is correctly passed to the method and the game's volume is correctly updated
	//Postcondition: A given audio clip will continuously loop as the game's background music until stopped 
	/////////////////////////////////////////
	public static void playBackgroundMusic(AudioInputStream inputStream){
	       try {
	           clipM = AudioSystem.getClip();
	           clipM.open(inputStream); 
	           clipM.loop(Clip.LOOP_CONTINUOUSLY);
	           updateVolume();
	       } catch (Exception e) {
	           System.err.println(e.getMessage() + " - Play Background Music");
	       }

	   }
	
	/////////////////////////////////////////
	//Function: Plays a given audio clip once as a sound for a collectible being picked up by the player
	//Precondition: The given audio clip is correctly passed to the method and the game's volume is correctly updated
	//Postcondition: A given audio clip is played once
	/////////////////////////////////////////
	public static void playCollectibleSound(AudioInputStream inputStream){
	       try {
	           clipC = AudioSystem.getClip();
	           clipC.open(inputStream); 
	           clipC.loop(0);
	           updateVolume();
	       } catch (Exception e) {
	           System.err.println(e.getMessage() + " - Play Collectible sound");
	       }
	   }
	
	/////////////////////////////////////////
	//Function: Updates the game's audio to the current volume
	//Precondition: the background background and collectible music players for the game exist
	//Postcondition: Updates the current sounds playing in the game 
	/////////////////////////////////////////
	public static void updateVolume(){
	       try {
	    	   if(clipM!=null){	//Updates the background music if it exists
	    		   FloatControl control = (FloatControl)clipM.getControl(FloatControl.Type.MASTER_GAIN); 
	    		   control.setValue(volume);  
	    	   }
	    	   if(clipC!=null){ //Updates the collectible music if it exists
	    		   FloatControl controlB = (FloatControl)clipC.getControl(FloatControl.Type.MASTER_GAIN);
	    		   controlB.setValue(volume);  
	    	   }
	       } catch (Exception e) {
	           System.err.println(e.getMessage()+" - Update Volume " + e.getLocalizedMessage());
	           e.printStackTrace();
	       }
	   }
	
	/////////////////////////////////////////
	//Function: Clears all non player entities and their associated graphics from the list of current game entities
	//Precondition: The list of game entities exists
	//Postcondition: All entities that aren't the player are removed from the game
	/////////////////////////////////////////
	public static void clearEntities(){
		if(entities.size()>0){
			if(entities.get(0) instanceof Player){
				Entity playerTemp = entities.get(0);//Saves the player object
				entities.clear();
				entities.add(playerTemp);		
				BufferedImage[][] spriteTemp = entitySprites.get(0);
				entitySprites.clear();
				entitySprites.add(spriteTemp); //Return the player object to the list
			}
			else{
				entities.clear();
				entitySprites.clear();
			}
		}

	}
	
	/////////////////////////////////////////
	//Function: Sets the game's drawboard, used for displaying the game, to a new image
	//Precondition: The drawboard exists and the imaged to be put on it is correctly passed to the method
	//Postcondition: The drawboard is set to a new image
	/////////////////////////////////////////
	public static void setDrawboard(BufferedImage i){
		BufferedImage temp = new BufferedImage(i.getWidth(),i.getHeight(),i.getType());
		Graphics g = temp.getGraphics();
		g.drawImage(i, 0, 0, null);
		drawboard=temp;
		g.dispose();
	}
	
	/////////////////////////////////////////
	//Function: Sets the width of the game's current level
	//Precondition: the new value for the width is correctly passed to the method
	//Postcondition: The width of the current level is set to a new value
	/////////////////////////////////////////
	public static void setLevelWidth(int width){
		levelWidth=width;
	}
	
	/////////////////////////////////////////
	//Function: Returns the width of the game's current level
	//Precondition: The variable for the current level width exists
	//Postcondition: The width of the current level is returned
	/////////////////////////////////////////
	public static int getLevelWidth(){
		return levelWidth;
	}
	
	/////////////////////////////////////////
	//Function: Resets all collectibles in the current level
	//Precondition: The list of current game entities exists
	//Postcondition: All collectibles picked up by the player are made visible and collectible again
	/////////////////////////////////////////
	public static void resetCollectibles(){
		for(int i=0; i<entities.size();i++){
			if(entities.get(i) instanceof Collectible && ((Collectible)entities.get(i)).collected==true){ //Sets every collectible to being not collected
				((Collectible)entities.get(i)).collected=false;
			}
		}
	}
	
}
