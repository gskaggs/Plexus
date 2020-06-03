package platformer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/////////////////////////////////////////
//Function: Contains the method to render Collectible according to their state (collected or not)
/////////////////////////////////////////
public class Collectible extends Entity{

	private static final long serialVersionUID = 807206731577809710L;
	public int type=-1;
	public boolean collected = false;

	public Collectible(int x, int y, int width, int height,int type){
		super(x,y,width,height);
		this.type=type;
	}
	
	/////////////////////////////////////////
	//Function: Draws the collectible to the screen if it has not yet been collected
	//Precondition: The given graphics and variables exist are correct
	//Postcondition: The collectible is drawn to the screen if it has not yet been collected
	/////////////////////////////////////////
	public void render(BufferedImage[][] sprites, Graphics2D g, int offsetX, int offsetY){
		if(collected==false){
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
}
