package platformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/////////////////////////////////////////
//Function: Provides the basic functions and variables for every game object
/////////////////////////////////////////
public class Entity  implements Serializable{
	private static final long serialVersionUID = 8380973666213937000L;
	int x,y,xVel,yVel,width,height;
	long lastTime=System.currentTimeMillis();
	int frame=0;
	Hitbox hitbox;
	
	public Entity(int x, int y,int width, int height){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		if(hitbox==null){
			int[] xs = {x,x+width,x+width,x};
			int[] ys = {y,y,y+height,y+height};
			hitbox=new Hitbox(xs,ys,xs.length);
		}
	}
	
	public Entity(int x, int y, int width, int height, int xVel, int yVel){
		this(x,y,width,height);
		this.xVel=xVel;
		this.yVel=yVel;
	}
	
	/////////////////////////////////////////
	//Function: Sets the x location of the entity to the given value
	//Precondition: The variable for the x value is correctly passed to the method
	//Postcondition: The entity's x position is set to a new value
	/////////////////////////////////////////
	public void setX(int x){
		this.moveHitbox(x-this.x,0);
		this.x=x;
	}
	
	/////////////////////////////////////////
	//Function: Sets the y location of the entity to the given value
	//Precondition: The variable for the y value is correctly passed to the method
	//Postcondition: The entity's y position is set to a new value
	/////////////////////////////////////////
	public void setY(int y){
		this.moveHitbox(0,y-this.y);
		this.y=y;
	}
	
	/////////////////////////////////////////
	//Function: Returns the x location of the entity
	//Precondition: The variable for the x location of the entity exists
	//Postcondition: The current x position of the entity is returned
	/////////////////////////////////////////
	public int getX(){
		return x;
	}
	
	/////////////////////////////////////////
	//Function: Returns the y location of the entity
	//Precondition: The variable for the y location of the entity exists
	//Postcondition: The current y position of the entity is returned
	/////////////////////////////////////////
	public int getY(){
		return y;
	}
	
	/////////////////////////////////////////
	//Function: Returns a true/false depending on whether the entity has a Hitbox that can be collided with
	//Precondition: The method is called
	//Postcondition: True is returned if the entity's Hitbox exists, false is returned otherwise 
	/////////////////////////////////////////
	public boolean canCollide(){
		if(hitbox!=null)
		return true;
		else return false;
	}
	
	/////////////////////////////////////////
	//Function: Moves the Hitbox of the entity by a given displacement
	//Precondition: The entity's Hitbox exists
	//Postcondition: The entity's hitbox is moved by the given amount
	/////////////////////////////////////////
	public void moveHitbox(int deltax,int deltay){
		hitbox.translate(deltax,deltay);
	}	
	
	/////////////////////////////////////////
	//Function: Returns the entity's Hitbox
	//Precondition: The entity's Hitbox exists
	//Postcondition: The enemies Hitbox is returned
	/////////////////////////////////////////
	public Hitbox getHitbox(){
		return hitbox;
	}	
	
	//Function: Updates the entity
	public void tick(){}
	
	/////////////////////////////////////////
	//Function: Draws the entity to the screen
	//Precondition: The entity's graphics and associated variables are correct
	//Postcondition: The entity is correctly drawn to the screen
	/////////////////////////////////////////
	public void render(BufferedImage[][] sprites, Graphics2D g, int offsetX, int offsetY){
		if(frame>=sprites[0].length)
			frame=0;
		g.drawImage(sprites[0][frame],x+offsetX,y+offsetY,null);
		long a = System.currentTimeMillis();
		if(a-lastTime>=100){
			lastTime=a;
			frame++;
		}
	}

}
