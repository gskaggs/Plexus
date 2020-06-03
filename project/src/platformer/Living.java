package platformer;

/////////////////////////////////////////
//Function: Contains all necessary methods for living, moving objects in the game
/////////////////////////////////////////
public abstract class Living extends Entity{

	private static final long serialVersionUID = -6787963593731621107L;
	int health,currentAction=0,attackDamage;
	public Living(int x, int y,int xVel, int yVel,int health){
		super(x,y,5,5,xVel,yVel);
		this.health=health;
	}
	
	/////////////////////////////////////////
	//Function: Damages the object by the given amount
	//Precondition: The value for the damage is correctly passed to the method
	//Postcondition: The objects health is subtracted by the given value
	/////////////////////////////////////////
	public void takeDamage(int damage){
		health-=damage;
	}
	
	/////////////////////////////////////////
	//Function: Adds the given amount of health to the object
	//Precondition: The value for health is correctly passed to the method
	//Postcondition: the objects health is increased by the given amount
	/////////////////////////////////////////
	public void addHealth(int hp){
		health+=hp;
	}
	
	/////////////////////////////////////////
	//Function: Checks to see if the object has collided with any other objects on screen and reacts to it
	//Precondition: The list of on screen entities is correct and the variables for velocity are correctly passed to the method
	//Postcondition: The object checks for collisions given its velocity and reacts to a collision if necessary
	/////////////////////////////////////////
	public boolean hasCollided(int xvel, int yvel){
		Entity[] entities = AssetManager.getOnScreenEntities();
			for(int a=0;a<entities.length;a++){
				if(entities[a].canCollide()){
					if(entities[a]!=this){
						if(hitbox.collides(entities[a].getHitbox(),xvel, yvel)){		
							if(this instanceof Enemy && a==0){
								((Player)entities[0]).takeDamage(this.attackDamage);
								if(((Enemy)this).pathType==0){
									return true;
								}
							}
							else if(this == entities[0] && entities[a] instanceof EVA){
								if(AssetManager.currentLevel<FileIO.numLevels){
									AssetManager.currentLevel++;
									CutscenesManager.setTimeCurrent();
									Game.gameState=4;
									FileIO.importStage(AssetManager.currentLevel);
								}
								else{
									CutscenesManager.setTimeCurrent();
									Game.gameState=5;
								}
							}
							else if(this == entities[0] && entities[a] instanceof Collectible){
								if(((Collectible)entities[a]).collected==false){
									if(((Collectible)entities[a]).type==0){
										AssetManager.playCollectibleSound(FileIO.importSound(1));
										Player.scores[AssetManager.currentLevel]++;                                  
										((Collectible)entities[a]).collected=true;
									}
									else if(((Collectible)entities[a]).type==1){
										AssetManager.playCollectibleSound(FileIO.importSound(2));
										((Player)entities[0]).health++;
										((Collectible)entities[a]).collected=true;
									}
								}
							}
							else if (this == entities[0] && entities[a] instanceof Enemy){
								((Player)entities[0]).takeDamage(((Enemy)entities[a]).attackDamage);
								if(((Enemy)entities[a]).pathType==0)
									return true;
							}
							else if(!(this instanceof Enemy && (a==0||entities[a] instanceof Collectible))){
								return true;
							}					
						}
					}	 
				}
			}
		return false;
	}
}
