package platformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/////////////////////////////////////////
//Function: Contains all necessary methods for updating and rendering enemies
/////////////////////////////////////////
public class Enemy extends NPC{
	private static final long serialVersionUID = -2788112745298356007L;

	static int xVel=5;
	static int yVel=5;
	static int health=2;
	
	public Enemy(int x, int y, int pathType){
		super(x,y,xVel,yVel,health,pathType);
		attackDamage=1;
			int[] xs = {x+1,x+49,x+49,x+1};
			int[] ys = {y+1,y+1,y+49,y+49};
			hitbox=new Hitbox(xs,ys,xs.length);
	}
	
	/////////////////////////////////////////
	//Function: Updates the enemy provided that they are still alive
	//Precondition: The method is called
	//Postcondition: The enemy's position is updated
	/////////////////////////////////////////
	public void tick(){
		if(health>0){
			walkPath();
		}
		else AssetManager.removeEntity(this);	
	}
	
	/////////////////////////////////////////
	//Function: Draws the enemy to the screen
	//Precondition: All given graphics and associated variables exist and are valid
	//Postcondition: The enemy is drawn to the screen
	/////////////////////////////////////////
	public void render(BufferedImage[][] sprites, Graphics2D g, int offsetX, int offsetY){
		if(frame>=sprites[currentAction].length)
			frame=0;
			g.drawImage(sprites[currentAction][frame], x+offsetX,y+offsetY, null);
			long a = System.currentTimeMillis();
			if(a-lastTime>=100){
				lastTime=a;
				frame++;
			}
	}

}
