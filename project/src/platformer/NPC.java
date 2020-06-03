package platformer;

/////////////////////////////////////////
//Function: Defines the necessary methods for NPCs to function independently
/////////////////////////////////////////
public class NPC extends Living{
	private static final long serialVersionUID = -7096317645085834251L;
	public int pathType=-1;
	
	public NPC(int x, int y, int xVel, int yVel, int health, int pathType){
		super(x,y,xVel,yVel,health);
		this.pathType=pathType;
	}
	
	/////////////////////////////////////////
	//Function: Controls the movement of the NPC depending on its set walk path
	//Precondition: The NPC's walkpath is defined
	//Postcondition: The NPC moves depending on its walkpath, detecting collisions and adjusting if necessary
	/////////////////////////////////////////
	public void walkPath(){
		switch(pathType){
		case 0:break; 			//Non-moving enemy
		case 1: 				//Horizontally moving enemy
			if(hasCollided(xVel,0)==true)
				xVel*=-1;
			x+=xVel;
			moveHitbox(xVel,0);
			break;
		case 2: 				//Vertically moving enemy
			if((hasCollided(0,yVel)==true||y+yVel<0||y+50+yVel>Game.HEIGHT))
				yVel*=-1;
			y+=yVel;
			moveHitbox(0,yVel);
			break;
		}
	}
}