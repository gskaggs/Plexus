package platformer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/////////////////////////////////////////
//Function: Imports and processes, and returns all necessary game objects and data when requested
/////////////////////////////////////////
public class FileIO {

static String[] stageLocations = {"src/levels/Stage 1.txt","src/levels/Stage 2.txt","src/levels/Stage 3.txt","src/levels/Stage 4.txt","src/levels/Stage 5.txt","src/levels/Stage 6.txt","src/levels/Stage 7.txt"};
static int numLevels=stageLocations.length-1;
static String[] soundLocations = {"src/audio/Vivian-1_23_18.wav","src/audio/Coin_Collect.wav","src/audio/Heart_Collect.wav"};

static String[] imageKey={"o","e"};
static String[] imageTileImports={"src/graphics/BrickTile.png","src/graphics/GroundTile.png",
		   			       ""};
static String[] spriteKey={"#",">","^"};
static String[][] spriteLocations ={{"src/graphics/EVO.png:1","src/graphics/EVO_Zeke.png:1","src/graphics/EVO_Return.png:1","src/graphics/EVO_Dead.png:1","src/graphics/SmallerUpArrow.png:1","src/graphics/HeartRotate.png:6","src/graphics/Coin Counter.png:5"},
									{"src/graphics/brightToFade.png:6"},
									{"src/graphics/CoinRotate.png:16"},
									{"src/graphics/EVA.png:1"},
									{"src/graphics/HeartRotate.png:6"}};
static String[] backgroundImports={"src/graphics/RedForest.png"};
static BufferedImage[] tiles =new BufferedImage[imageKey.length];;
static BufferedImage[][][] spriteGraphics = new BufferedImage[spriteLocations.length][][];

	/////////////////////////////////////////
	//Function: Imports the graphics for a given stage and creates all entities associated with it
	//Precondition: All graphics locations are correct
	//Postcondition: All entities and graphics for a given stage are imported
	//////////////////////////////////////////
	public static void importStage(int fileLocation){
		AssetManager.currentLevel=fileLocation;
		Scanner scan;
		String[] holder;
		AssetManager.clearEntities();
		int levelWidth=0;
		try{
			BufferedImage background = ImageIO.read(new File(backgroundImports[0]));
			AssetManager.setBackgroundImage(background);
			scan = new Scanner(new File(stageLocations[fileLocation]));
			while(scan.hasNextLine()){				//Scans Level and only imports tiles required to create it
				holder=scan.nextLine().split("");	//Scan the file line by line and character by character
				if(holder.length>levelWidth) 		//Sets the level width to the longest line in the text file
					levelWidth=holder.length; 
				for(int i=0;i<holder.length;i++){
					for(int a=0;a<imageKey.length;a++){
						if(holder[i].equals(imageKey[a])&&tiles[a]==null){
							tiles[a]=ImageIO.read(new File(imageTileImports[a]));
						}
					}
				}
			}
			scan.close();
			levelWidth*=50;
			AssetManager.setLevelWidth(levelWidth); // Sets the width of the stage
			
		//	AssetManager.addEntity(new Entity(0,0,levelWidth,0),null);
		//	AssetManager.addEntity(new Entity(0,height,levelWidth,0),null);    //Optional Level barriers and edge of screen
		//	AssetManager.addEntity(new Entity(0,0,0,height),null);
		//	AssetManager.addEntity(new Entity(levelWidth,0,0,height),null);
			
			scan = new Scanner(new File(stageLocations[fileLocation]));
			for(int i=0;scan.hasNextLine();i++){
				holder=scan.nextLine().split("");
				for(int a=0;a<holder.length;a++){ //Scans the file line by line and character by character
					if(!(holder[a].equals("#")||holder[a].equals(">")||holder[a].equals("^")||holder[a].equals("-")||holder[a].equals("+")||holder[a].equals("!")||holder[a].equals("?"))){
						for(int b=0;b<imageKey.length;b++){ 	//If the detected character is associated with a level block it is created otherwise
							if(holder[a].equals(imageKey[b])){
								BufferedImage[][] imageHolder = new BufferedImage[1][1];
								imageHolder[0][0] = tiles[b];
								AssetManager.addEntity(new Entity(a*50,i*50,50,50), imageHolder);
							}
						}
					}
					else{ //If the detected character is associated with a more complex game object than a level block, it detects the type and created it according to its constructor
						if(holder[a].equals(spriteKey[0])){
							AssetManager.addEntity(new Enemy(a*50,i*50,0), importSprite(1));
						}
						else 
						if(holder[a].equals(spriteKey[1])){
						AssetManager.addEntity(new Enemy(a*50,i*50,1), importSprite(1));
						} 
						else
						if(holder[a].equals(spriteKey[2])){
							AssetManager.addEntity(new Enemy(a*50,i*50,2), importSprite(1));
						}
						else
						if(holder[a].equals("-")){
							AssetManager.addEntity(new Collectible(a*50,i*50,50,50,0), importSprite(2));
						}
						else
						if(holder[a].equals("+")){
							AssetManager.addEntity(new Collectible(a*50,i*50,50,50,1), importSprite(4));
						}
						else
						if(holder[a].equals("!")){
							AssetManager.addPlayer(new Player(a*50,i*50,10,0), importSprite(0), a*50, i*50);
						}
						else
						if(holder[a].equals("?")){
							AssetManager.addEntity(new EVA(a*50,i*50,50,100), importSprite(3));
						}
					}
				}
			}
			scan.close();
		}catch(IOException e){
			System.out.println(e.getMessage()+"stageimport");
		}
	}
	
	/////////////////////////////////////////
	//Function: Imports the graphics for a given sprite and return them in an array
	//Precondition: The sprite graphics locations are correct
	//Postcondition: All graphics associated with a sprite are imported and returned in an array
	/////////////////////////////////////////
	public static BufferedImage[][] importSprite(int sprite){
		BufferedImage[][] listOfSprites = new BufferedImage[spriteLocations[sprite].length][]; //the list of sprites is an array of sprite sheets which are arrays of images for each frame of an animation
		for(int i=0;i<spriteLocations[sprite].length;i++){	//Scans through the list of sprite sheets associated with the type of class
			String[] hold = spriteLocations[sprite][i].split(":");	//Divides the sprite entry so it has both the file location and the length of the sprite sheet
			BufferedImage[] holder=null;
			int num=Integer.parseInt(hold[1]);
			try{
				BufferedImage temp = ImageIO.read(new File(hold[0]));
				holder = new BufferedImage[num];
				int size=temp.getWidth()/num;
				for(int a=0;a<holder.length;a++){	// Divides the graphics into frames
					holder[a]=temp.getSubimage(a*size, 0, size, temp.getHeight());
				}
			}catch(IOException e){
				System.out.println(e.getMessage() + "importSprite");
			}
			listOfSprites[i]=holder;
		}
		return listOfSprites;
	}
	
	/////////////////////////////////////////
	//Function: Imports the audio files for a given track number
	//Precondition: The given sound location is valid
	//Postcondition: imports and returns the AudioInputStream associated with the given track number
	/////////////////////////////////////////
	public static AudioInputStream importSound(int url){
	    try {
	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundLocations[url]));
	        return inputStream;
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	    }
	    return null;
	}
	
	/////////////////////////////////////////
	//Function: Imports the graphics associated with the Main Menu
	//Precondition: The graphics locations are correct 
	//Postcondition: The main menu graphics are imported and returned in an array
	/////////////////////////////////////////
	public static BufferedImage[] importMenuGraphics(){
		BufferedImage[] temp = new BufferedImage[4];
		try{
			temp[0]=ImageIO.read(new File("src/graphics/Plexus New Menu.png"));
			temp[1]=ImageIO.read(new File("src/graphics/Pointer.png"));
			temp[2]=ImageIO.read(new File("src/graphics/OptionsMenu.png"));
			temp[3]=ImageIO.read(new File("src/graphics/Plexus Pause Menu.png"));
		}catch(IOException e){
			System.out.println(e.getMessage() + "MenuImport");
		}
		return temp;	
	}
	
	/////////////////////////////////////////
	//Function: Imports the graphics for the game's introduction
	//Precondition: The sprite graphics locations are correct
	//Postcondition: The game introduction graphics are imported and returned
	/////////////////////////////////////////
	public static BufferedImage[] importIntroGraphics(){
	BufferedImage[] temp = new BufferedImage[5];
	try{
		temp[0]=ImageIO.read(new File("src/graphics/Credits.png"));
		temp[1]=ImageIO.read(new File("src/graphics/CogThoughtMedia.png"));
		temp[2]=ImageIO.read(new File("src/graphics/GraetGaemersNew.png"));
		temp[3]=ImageIO.read(new File("src/graphics/Present.png"));
		temp[4]=ImageIO.read(new File("src/graphics/PlexusMainMenu.png"));
	}
	catch(IOException e){System.out.println(e+"IntroImports");}
	return temp;
	}
	
	/////////////////////////////////////////
	//Function: Imports the graphics for the game's introduction cutscene
	//Precondition: The sprite graphics locations are correct
	//Postcondition: the game introduction cutscene graphics are imported and returned
	/////////////////////////////////////////
	public static BufferedImage[] importCutsceneGraphics(){
	BufferedImage[] temp = new BufferedImage[11];
	try{
		temp[0]=ImageIO.read(new File("src/graphics/Cliff1.png"));
		temp[1]=ImageIO.read(new File("src/graphics/Cliff_2.png"));
		temp[2]=ImageIO.read(new File("src/graphics/Cliff3.png"));
		temp[3]=ImageIO.read(new File("src/graphics/Cliff4.png"));
		temp[4]=ImageIO.read(new File("src/graphics/Instructions.png"));
		temp[5]=ImageIO.read(new File("src/graphics/Middle_CutScene.png"));
		temp[6]=ImageIO.read(new File("src/graphics/FinalCutScene.png"));
		temp[7]=ImageIO.read(new File("src/graphics/Final1.png"));
		temp[8]=ImageIO.read(new File("src/graphics/Final2.png"));
		temp[9]=ImageIO.read(new File("src/graphics/Final3.png"));
		temp[10]=ImageIO.read(new File("src/graphics/Final4.png"));
	}
	catch(IOException e){System.out.println(e+"CutsceneImports");}
	return temp;
	}
	
	/////////////////////////////////////////
	//Function: Saves the current state of the game
	//Precondition: The data and objects to be serialized exists
	//Postcondition: The necessary game objects are serialized to a text file
	/////////////////////////////////////////
	public static void saveState(){
	    try{
	        FileOutputStream fos = new FileOutputStream("saveState.txt"); //Saves the current game level, all game entities, and the player's current score to the text file "saveState.txt"
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeInt(AssetManager.currentLevel);	//saves current level
	        oos.writeObject(AssetManager.entities); 	//saves game entities
	        oos.writeObject(Player.scores);				//saves player's score
	        oos.close();
	        fos.close();
	    }
	    catch(IOException e){System.out.println(e);}
	}
	
	/////////////////////////////////////////
	//Function: Loads the last saved state of the game
	//Precondition: The load file exists and is not empty
	//Postcondition: The game is restored to its saved version
	/////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static void loadState(){
	   try{
		   FileInputStream fin = new FileInputStream("saveState.txt"); //Reads saved information from this text file
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   AssetManager.currentLevel=ois.readInt(); //Reads in the saved level number
		   importStage(AssetManager.currentLevel);  //Imports the level
		   AssetManager.entities=(ArrayList<Entity>)ois.readObject(); // Reads in the list of saved entities
		   Player.scores=(int[])ois.readObject();	//Reads in the player's saved score
		   ((Player)AssetManager.entities.get(0)).centerCamera();
		    ois.close();
	   }
	   catch(IOException e){System.out.println(e);}
	   catch(ClassNotFoundException e){System.out.println(e);}
	}
}
