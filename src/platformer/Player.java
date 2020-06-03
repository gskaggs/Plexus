package platformer;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.awt.Graphics2D;

/////////////////////////////////////////
//Function: Controls all movement, rendering, updating, and tracking for a given player object
/////////////////////////////////////////
public class Player extends Living implements Serializable{
	private static final long serialVersionUID = 9090453803470891580L;
	long damagedTime, flashingTime=System.currentTimeMillis();
	static int[] scores = new int[FileIO.numLevels+1];
	boolean damaged=false;
	int xSpawn, ySpawn;
	static int pHealth=4, skin=0;
	
	public Player(int x, int y,int xVel, int yVel){
		super(x,y,xVel,yVel,pHealth);
		int[] xs = {x+1,x+49,x+49,x+1};
		int[] ys = {y+7,y+7,y+99,y+99};
		hitbox=new Hitbox(xs,ys,xs.length);
		xSpawn=x;
		ySpawn=y;
		attackDamage=1;
	}
	
	/////////////////////////////////////////
	//Function: Resets the players current health to its original value
	//Precondition: the variables for the player's health exist
	//Postcondition: The players health is reset
	/////////////////////////////////////////
	public void resetHealth(){
		health=pHealth;
	}
	
	/////////////////////////////////////////
	//Function: Deducts a given amount of damage from the Player's health
	//Precondition: The damage amount is correctly passed to the method
	//Postcondition: The player is damaged by the given amount and registers being recently damaged
	/////////////////////////////////////////
	public void takeDamage(int x){
		if(!damaged){
			health-=x;
			damaged=true;
			damagedTime=System.currentTimeMillis();
		}
	}
	
	/////////////////////////////////////////
	//Function: Sets the location of the player's respawn point
	//Precondition: The location of the new respawn point is correctly passed to the method
	//Postcondition: The player's repsawn point is set to a new location
	/////////////////////////////////////////
	public void setRespawn(int x, int y){
		xSpawn=x;
		ySpawn=y;
	}
	
	/////////////////////////////////////////
	//Function: Resets the player's scores for the current game
	//Precondition: The list for the player's scores exists
	//Postcondition: The player's scores for the current game are reset
	/////////////////////////////////////////
	public void resetScores(){
		scores = new int[FileIO.numLevels+1];
	}
	
	/////////////////////////////////////////
	//Function: Center's the game's camera on the player
	//Precondition: The game's camera and current background exist
	//Postcondition: The game's camera is centered on the player
	/////////////////////////////////////////
	public void centerCamera(){
		int cameraX,cameraY;
		if(x>=Game.WIDTH/2 && x<=AssetManager.getLevelWidth()-Game.WIDTH/2)
			cameraX=x-Game.WIDTH/2;
		else if(x<=Game.WIDTH/2)
			cameraX=0;
		else cameraX=AssetManager.getLevelWidth()-Game.WIDTH;
		cameraY=0;
		AssetManager.setCamera(cameraX, cameraY);
	}
	
	/////////////////////////////////////////
	//Function: Updates the player
	//Precondition: All necessary variables associate with the player and the list of keys pressed are valid
	//Postcondition: The player's information is updated
	/////////////////////////////////////////
	public void tick(){
		boolean[] keys=AssetManager.getKeysPressed();
		if(health>0){
			if(hasCollided(0,yVel)==false){
				moveHitbox(0,yVel);
				y+=yVel;
			}
			else yVel=0;
			
			yVel+=1;
			
			if(yVel>=0 && keys[0]==true&&hasCollided(0,yVel)){
				yVel=-18;
			}	
			
			if(keys[2]==true&&(x-xVel>=Game.WIDTH/2&&x<=AssetManager.getLevelWidth()-Game.WIDTH/2)&&hasCollided(-xVel,0)!=true){
				AssetManager.setCameraX(AssetManager.getCameraX()-xVel);
				x-=xVel;
				moveHitbox(-xVel,0);
			}	
			else if(keys[2]==true&&hasCollided(-xVel,0)!=true){
				if(x-xVel<Game.WIDTH)
					AssetManager.setCameraX(0);
				x-=xVel;
				moveHitbox(-xVel,0);
			}
			if(keys[3]==true&&(x+xVel>=Game.WIDTH/2&&x<=AssetManager.getLevelWidth()-Game.WIDTH/2)&&hasCollided(xVel,0)!=true){
				AssetManager.setCameraX(AssetManager.getCameraX()+xVel);
				x+=xVel;
				moveHitbox(xVel,0);
			}	
			else if(keys[3]==true&&hasCollided(xVel,0)!=true){
				if(x+xVel>AssetManager.getLevelWidth()-Game.WIDTH)
					AssetManager.setCameraX(AssetManager.getLevelWidth()-Game.WIDTH);
				x+=xVel;
				moveHitbox(xVel,0);
			}	

			long temp = System.currentTimeMillis();
				if(temp-damagedTime>=3000)
						damaged=false;
						
			if(y>Game.HEIGHT){
				//health--;             //Jump after falling
				//yVel=-25;             
				//damaged=true;
				//damagedTime=System.currentTimeMillis();
					
				health=0;               // die upon falling off screen
					
				//setY(-100);           // Teleport falling
			}
		}
		else{ 
			setX(xSpawn);
			setY(ySpawn);
			centerCamera();
			addHealth(4);
			damaged=false;
			scores[AssetManager.currentLevel]=0;
			AssetManager.resetCollectibles();
		}
	}
	
	/////////////////////////////////////////
	//Function: Draws the player and its health and collectible counter to the screen
	//Precondition: The sprites and associated variable passed to the method exist and are correct
	//Postcondition: The player and its health and collectible counter are drawn to the screen depending on their current state
	/////////////////////////////////////////
	public void render(BufferedImage[][] sprites, Graphics2D g, int offsetX, int offsetY){
		long a = System.currentTimeMillis();
		if(frame>=sprites[5].length)
			frame=0;
		for(int i=1;i<health;i++){
			g.drawImage(sprites[5][frame], 800-(i*50), 15, null);
		}
		if(scores[AssetManager.currentLevel]<=4)
			g.drawImage(sprites[6][scores[AssetManager.currentLevel]],10,10,null);
		if(y+100>0){
			if(damaged){
				if(a-flashingTime>=200){
					g.drawImage(sprites[skin][0], x+offsetX,y+offsetY, null);
					if(a-flashingTime>=400)
						flashingTime=a;
				}
			}
			else g.drawImage(sprites[skin][0], x+offsetX,y+offsetY, null);
		}
		else
			g.drawImage(sprites[4][0], x+offsetX, 0, null);
		if(a-lastTime>=100){
			lastTime=a;
			frame++;
		}		
	}	
}
