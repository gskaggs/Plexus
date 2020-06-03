package platformer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/////////////////////////////////////////
//Function: Contains all methods needed for updating non game play aspects of the game (menus and cutscenes)
/////////////////////////////////////////
public class CutscenesManager{
	private static BufferedImage drawboard = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private static BufferedImage[] introBackgrounds = FileIO.importIntroGraphics();
	private static BufferedImage[] menuBackgrounds = FileIO.importMenuGraphics();
	private static BufferedImage[] cutsceneBackgrounds = FileIO.importCutsceneGraphics();
	private static boolean[] keysPressed = AssetManager.getKeysPressed();
	private static int duration0=4500,duration1=duration0+4000, duration2=duration1+2500, duration3=duration2+4000, duration4=duration3+2500, duration5=duration4+4000, duration6=duration5+2500, duration7=duration6+4000, duration8=duration7+2500;
	private static int fadeTime1=2000, fadeTime2=2000, fadeTime3=2000, fadeTime4=2000, fadeTime5=2000, fadeTime6=2000, fadeTime7=2000, fadeTime8=2000, fadeTime9=4000;
	private static float fadeIncrement1=1f/fadeTime1, fadeIncrement2=1f/fadeTime2, fadeIncrement3=1f/fadeTime3, fadeIncrement4=1f/fadeTime4, fadeIncrement5=1f/fadeTime5, fadeIncrement6=1f/fadeTime6, fadeIncrement7=1f/fadeTime7, fadeIncrement8=1f/fadeTime8, fadeIncrement9=1f/fadeTime9;
	static int menuNum=0,selection=0,selectionOV=2,selectionOS=0;
	static int[][] mCPs={{95,275},{90,355},{90,425},{90,500}};
	static int[][] oCPs={{50,200},{330,360},{40,520},{135,220,325,430},{330},{420,510,615,720},{490}};
	static int[][] pCPs={{440,66,478,93},{80,319,458,498}};
	static int frame=0;
	static boolean complete=false;
	static float alpha=0f,alpha2=.1f;
	static boolean started=true;
	static int word=0;
	static long startTime=System.currentTimeMillis();
	static long currentTime=System.currentTimeMillis();
	private static Graphics2D g = (Graphics2D)drawboard.getGraphics();
	private static boolean haveReleased=true;
	private static int lastKey=0;
	private static boolean wasPause=false;
	
	
	/////////////////////////////////////////
	//Function: Updates and controls the use of the Main Menu 
	//Precondition: The game state allows for this method to be called to update and all graphics were imported correctly
	//Postcondition: the main menu is updated based on user input
	/////////////////////////////////////////
	public static void mainMenu(){
		g.drawImage(menuBackgrounds[0],0,0,null);
		switch (selection){	//Switches effects of keys based on the current arrow selection
		case 0:
			if(keysPressed[1] && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			if(keysPressed[6]&&haveReleased==true){
				haveReleased=false;
				lastKey=6;
				FileIO.importStage(AssetManager.currentLevel);
				((Player)AssetManager.entities.get(0)).resetScores();
				Game.gameState=2;
				setTimeCurrent();
			}
				
			g.drawImage(menuBackgrounds[1], mCPs[0][0], mCPs[0][1],null);
		break;
		case 1:
			if(keysPressed[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else if(keysPressed[1]==true && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			if(keysPressed[6]&&haveReleased==true){
				FileIO.loadState();
				Game.gameState=3;
				haveReleased=false;
				lastKey=6;
			}
			g.drawImage(menuBackgrounds[1], mCPs[1][0], mCPs[1][1],null);
		break;
		case 2:
			if(keysPressed[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else if(keysPressed[1]==true && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			if(keysPressed[6]&&haveReleased==true){
				haveReleased=false;
				lastKey=6;
				Game.gameState=7;
			}
			g.drawImage(menuBackgrounds[1], mCPs[2][0], mCPs[2][1],null);
		break;
		case 3:
			if(keysPressed[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			g.drawImage(menuBackgrounds[1], mCPs[3][0], mCPs[3][1],null);
			if(keysPressed[6]==true && haveReleased==true){
				Window.closeFrame();
				haveReleased=false;
				lastKey=6;
			}
				
		break;
		}
		if(!keysPressed[lastKey])
			haveReleased=true;

		AssetManager.setDrawboard(drawboard);
		
	}
	
	
	/////////////////////////////////////////
	//Function: Updates and controls the use of the options menu
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: the options menu is updated based on user input
	/////////////////////////////////////////
	public static void optionsMenu(){
		//System.out.println(selection);
		g.drawImage(menuBackgrounds[2],0,0,null);
		g.setColor(new Color(176,102,189));
		g.fillRect(oCPs[3][selectionOV], oCPs[4][0], 20, 20);
		g.fillRect(oCPs[5][selectionOS], oCPs[6][0], 20, 20);
		
		switch (selection){	//Switches effects of keys based on the current arrow selection
		case 0:
			if(keysPressed[1] && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			else if(keysPressed[2]==true && haveReleased==true && selectionOV>=1){
				selectionOV--;
				AssetManager.volume-=10;
				AssetManager.updateVolume();
				haveReleased=false;
				lastKey=2;
			}
			else if(keysPressed[3]==true && haveReleased==true && selectionOV<=2){
				selectionOV++;
				AssetManager.volume+=10;
				AssetManager.updateVolume();
				haveReleased=false;
				lastKey=3;
			}
			g.drawImage(menuBackgrounds[1], oCPs[0][0], oCPs[0][1],null);
		break;
		case 1:
			if(keysPressed[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else if(keysPressed[1]==true && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			else if(keysPressed[2]==true && haveReleased==true && selectionOS>=1){
				selectionOS--;
				Player.skin--;
				haveReleased=false;
				lastKey=2;
			}
			else if(keysPressed[3]==true && haveReleased==true && selectionOS<=2){
				
				selectionOS++;
				Player.skin++;
				
				haveReleased=false;
				lastKey=3;
			}
			g.drawImage(menuBackgrounds[1], oCPs[1][0], oCPs[1][1],null);
		break;
		case 2:
			if(keysPressed[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			g.drawImage(menuBackgrounds[1], oCPs[2][0], oCPs[2][1],null);
			if(keysPressed[6]==true && haveReleased==true){
				if(wasPause)
				Game.gameState=6;
				else
				Game.gameState=1;
				haveReleased=false;
				lastKey=6;
			}
				
		break;
		}
		if(!keysPressed[lastKey])
			haveReleased=true;

		AssetManager.setDrawboard(drawboard);
		
	}
	
	/////////////////////////////////////////
	//Function: Updates and controls the use of the pause menu
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: the pause menu is updated based on user input
	/////////////////////////////////////////
	public static void pauseMenu(){
		//System.out.println(selection);
		g.drawImage(menuBackgrounds[3],0,0,null);
		switch (selection){ //Switches effects of keys based on the current arrow selection
		case 0:
			if(keysPressed[1] && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			if(keysPressed[6]&&haveReleased==true){
				haveReleased=false;
				lastKey=6;
				Game.gameState=3;
				setTimeCurrent();
			}
			g.drawImage(menuBackgrounds[1], mCPs[0][0], mCPs[0][1],null);
		break;
		case 1:
			if(AssetManager.getKeysPressed()[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else if(AssetManager.getKeysPressed()[1]==true && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			else
			if(keysPressed[6]==true && haveReleased==true){
				lastKey=6;
				haveReleased=false;
				wasPause=true;
				Game.gameState=7;
			}
			g.drawImage(menuBackgrounds[1], mCPs[1][0], mCPs[1][1],null);
		break;
		case 2:
			if(AssetManager.getKeysPressed()[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else if(AssetManager.getKeysPressed()[1]==true && haveReleased==true){
				selection++;
				haveReleased=false;
				lastKey=1;
			}
			else
			if(keysPressed[6]==true && haveReleased==true){
				lastKey=6;
				haveReleased=false;
				FileIO.saveState();
				System.out.println("Progress Saved");
			}
			g.drawImage(menuBackgrounds[1], mCPs[2][0], mCPs[2][1],null);
		break;
		case 3:
			if(AssetManager.getKeysPressed()[0]==true && haveReleased==true){
				selection--;
				haveReleased=false;
				lastKey=0;
			}
			else
			if(keysPressed[6]==true && haveReleased==true){
				lastKey=6;
				haveReleased=false;
				wasPause=false;
				Game.gameState=1;
			}
			g.drawImage(menuBackgrounds[1], mCPs[3][0], mCPs[3][1],null);
		break;
		}
		if(!keysPressed[lastKey])
			haveReleased=true;

		AssetManager.setDrawboard(drawboard);
		
	}
	
	
	/////////////////////////////////////////
	//Function: Displays the game's introduction
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: The game's introduction is updated
	/////////////////////////////////////////
	public static void gameIntro(){
		long elapsedTime=currentTime-startTime;	//Different cutscene segments are played based on how much time has passed
		
		//System.out.println(elapsedTime);
		//System.out.println(alpha);
		
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
		
		if(AssetManager.keysPressed[4]==true){ //Allows for the cutscene to be skipped
			Game.gameState=1;
			wasPause=false;
			haveReleased=false;
			lastKey=4;
			g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
			setTimeCurrent();
			
		}
		
		if(elapsedTime<duration0){
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else
		if(elapsedTime<duration1){
			if(elapsedTime-duration0<fadeTime1)
				alpha=fadeIncrement1*(elapsedTime-duration0);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[0], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration2){
			if(elapsedTime-duration1<fadeTime2)
				alpha=fadeIncrement2*(fadeTime2-(elapsedTime-duration1));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[0], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration3){
			if(elapsedTime-duration2<fadeTime3)
				alpha=fadeIncrement3*(elapsedTime-duration2);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[1], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration4){
			if(elapsedTime-duration3<fadeTime4)
				alpha=fadeIncrement4*(fadeTime4-(elapsedTime-duration3));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[1], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration5){
			if(elapsedTime-duration4<fadeTime5)
				alpha=fadeIncrement5*(elapsedTime-duration4);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[2], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration6){
			if(elapsedTime-duration5<fadeTime6)
				alpha=fadeIncrement6*(fadeTime6-(elapsedTime-duration5));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[2], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration7){
			if(elapsedTime-duration6<fadeTime7)
				alpha=fadeIncrement7*(elapsedTime-duration6);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[3], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration8){
			if(elapsedTime-duration7<fadeTime8)
				alpha=fadeIncrement8*(fadeTime8-(elapsedTime-duration7));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[3], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		else {
			if(AssetManager.keysPressed[6]==true){
				Game.gameState=1;
				wasPause=false;
				haveReleased=false;
				lastKey=6;
				g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
				setTimeCurrent();
				
			}
			else
			if(elapsedTime-duration8<fadeTime9)
				alpha=fadeIncrement9*(elapsedTime-duration8);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(introBackgrounds[4], 0, 0, null);
			g2.dispose();
			currentTime=System.currentTimeMillis();
		}
		AssetManager.setDrawboard(drawboard);
	}
	
	
	/////////////////////////////////////////
	//Function: Displays the game's introduction cutscene
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: The game's introduction cutscene is updated
	/////////////////////////////////////////
	public static void introCutscene(){
	long elapsedTime=currentTime-startTime;	//Different cutscene segments are played based on how much time has passed
		//System.out.println(elapsedTime);
		//System.out.println(alpha);
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
		
		
		if(AssetManager.keysPressed[4]==true){ //Allows for the cutscene to be skipped
			Game.gameState=3;
			wasPause=false;
			haveReleased=false;
			lastKey=4;
			g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
			setTimeCurrent();
			
		}
		
		if(elapsedTime<duration1){
			if(elapsedTime<fadeTime1)
				alpha=fadeIncrement1*elapsedTime;
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[0], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration2){
			if(elapsedTime-duration1<fadeTime2)
				alpha=fadeIncrement2*(fadeTime2-(elapsedTime-duration1));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[0], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration3){
			if(elapsedTime-duration2<fadeTime3)
				alpha=fadeIncrement3*(elapsedTime-duration2);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[1], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration4){
			if(elapsedTime-duration3<fadeTime4)
				alpha=fadeIncrement4*(fadeTime4-(elapsedTime-duration3));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[1], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration5){
			if(elapsedTime-duration4<fadeTime5)
				alpha=fadeIncrement5*(elapsedTime-duration4);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[2], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration6){
			if(elapsedTime-duration5<fadeTime6)
				alpha=fadeIncrement6*(fadeTime6-(elapsedTime-duration5));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[2], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration7){
			if(elapsedTime-duration6<fadeTime7)
				alpha=fadeIncrement7*(elapsedTime-duration6);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[3], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration8){
			if(elapsedTime-duration7<fadeTime8)
				alpha=fadeIncrement8*(fadeTime8-(elapsedTime-duration7));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[3], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else {
			if(AssetManager.keysPressed[6]==true){
				Game.gameState=3;
				haveReleased=false;
				lastKey=6;
			}
			if(elapsedTime-duration8<fadeTime9)
				alpha=fadeIncrement9*(elapsedTime-duration8);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[4], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}	
	}

	
	/////////////////////////////////////////
	//Function: Displays the game's transition cutscene
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: The game's transition cutscene is updated
	/////////////////////////////////////////
	public static void transitionCutscene(){
	long elapsedTime=currentTime-startTime;	//Different cutscene segments are played based on how much time has passed
		//System.out.println(elapsedTime);
		//System.out.println(currentTime + " " + startTime);
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
		if(elapsedTime<duration1){
			if(elapsedTime<fadeTime1)
				alpha=fadeIncrement1*elapsedTime;
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[5], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration2){
			if(elapsedTime-duration1<fadeTime2)
				alpha=fadeIncrement2*(fadeTime2-(elapsedTime-duration1));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[5], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else{
			Game.gameState=3;
		}
		
	}
	
	
	/////////////////////////////////////////
	//Function: Displays the game's final cutscene
	//Precondition: the game state allows for this method to be called and all graphics were imported correctly
	//Postcondition: The game's final cutscene is updated
	/////////////////////////////////////////
	public static void finalCutscene(){
	long elapsedTime=currentTime-startTime;	//Different cutscene segments are played based on how much time has passed
	//System.out.println(elapsedTime + " "+ alpha);
	
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
		
		if(elapsedTime<duration0){
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else
		if(elapsedTime<duration1){
			if(elapsedTime-duration0<fadeTime1)
				alpha=fadeIncrement1*(elapsedTime-duration0);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[7], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration2){
			if(elapsedTime-duration1<fadeTime2)
				alpha=fadeIncrement2*(fadeTime2-(elapsedTime-duration1));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[7], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration3){
			if(elapsedTime-duration2<fadeTime3)
				alpha=fadeIncrement3*(elapsedTime-duration2);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[8], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration4){
			if(elapsedTime-duration3<fadeTime4)
				alpha=fadeIncrement4*(fadeTime4-(elapsedTime-duration3));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[8], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration5){
			if(elapsedTime-duration4<fadeTime5)
				alpha=fadeIncrement5*(elapsedTime-duration4);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[9], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else if(elapsedTime<duration6){
			if(elapsedTime-duration5<fadeTime6)
				alpha=fadeIncrement6*(fadeTime6-(elapsedTime-duration5));
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[9], 0, 0, null);
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}
		else {
			if(AssetManager.keysPressed[6]==true){
				haveReleased=false;
				lastKey=6;
				Game.gameState=0;
				setTimeCurrent();
			}
			if(elapsedTime-duration6<fadeTime7)
				alpha=fadeIncrement7*(elapsedTime-duration6);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			g2.drawImage(cutsceneBackgrounds[10], 0, 0, null);
			for(int i=0; i<Player.scores.length;i++){
				g2.drawImage(AssetManager.entitySprites.get(0)[6][Player.scores[i]],225+i*50,425,null);
			}
			g2.dispose();
			AssetManager.setDrawboard(drawboard);
			currentTime=System.currentTimeMillis();
		}	
		
	}
	
	
	/////////////////////////////////////////
	//Function: Resets the timing variables as if a cutscene has just begun
	//Precondition: The necessary variables exist
	//Postcondition: The game's timing variables (startTime, currentTime) are updated to the current time
	/////////////////////////////////////////
	public static void setTimeCurrent(){
		startTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
	}
}