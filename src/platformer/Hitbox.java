package platformer;
import java.awt.Polygon;
import java.awt.geom.Line2D;

/////////////////////////////////////////
//Function: Contains all method necessary for using hitboxes (collision detection and hitbox movement)
/////////////////////////////////////////
public class Hitbox extends Polygon{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2863309057920997754L;
	
	int collisionType=-1;
	
	public Hitbox(int[] xs, int[] ys, int num){
		super(xs,ys,num);
	}

	/////////////////////////////////////////
	//Function: Checks to see if a collision has occured with the given hitbox given its displacement
	//Precondition: The given hitbox and displacement are valid and correctly passed to the method
	//Postcondition: Return true if a collision has occured and false otherwise
	/////////////////////////////////////////
	public boolean collides(Hitbox hitboxB, int xvel, int yvel){
		Line2D.Double[] sides = new Line2D.Double[npoints];
		Line2D.Double[] sidesB = new Line2D.Double[hitboxB.npoints];
		int xPos;
		int yPos;
		for(int i=0;i<sides.length;i++){ //Turns the first hitbox's points into an array of lines, accounting for the displacement
			if(i==sides.length-1){
				xPos=-1;
				yPos=-1;
			}
			else{
				xPos=i;
				yPos=i;
			}
			sides[i] = new Line2D.Double(xpoints[i]+xvel, ypoints[i]+yvel,xpoints[xPos+1]+xvel,ypoints[yPos+1]+yvel);
		}
		for(int i=0;i<sidesB.length;i++){ //Turns the second hitbox's points into an array of lines
			if(i==sidesB.length-1){
				xPos=-1;
				yPos=-1;
			}
			else{
				xPos=i;
				yPos=i;
			}
			sidesB[i] = new Line2D.Double(hitboxB.xpoints[i], hitboxB.ypoints[i],hitboxB.xpoints[xPos+1],hitboxB.ypoints[yPos+1]);
		}
		for(int i=0;i<sides.length;i++){ //Checks to see if any line from the first hitbox collides with any line from the second hitbox
			for(int a=0;a<sidesB.length;a++){
				if(sides[i].intersectsLine(sidesB[a])){
					return true;
				}
			}
		}
			return false;
	}
	
	/////////////////////////////////////////
	//Function: Moves the Hitbox by the given displacement
	//Precondition: The variables for the displacement are correctly passed to the method
	//Postcondition: The Hitbox is moved by the given amount
	/////////////////////////////////////////
	public void translate(int deltax, int deltay){
		for(int i=0;i<npoints;i++){
			xpoints[i]+=deltax;
			ypoints[i]+=deltay;
		}
	}

}
