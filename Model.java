import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Arnav Bindra, Woody Hu, Tong Zhao
 *
 */

public class Model {


	//variables for height and width of drawn frame
	private int frameHeight; 
	private int frameWidth;

	//variables for height and width of pictures
	private int imgWidth;
	int imgHeight;

	private int xLoc = 0; //x-location for individual object
	private int[] yLoc = new int[5]; //array of y-locations for the objects drawn on screen

	//movement increments in the x and y directions
	public int xIncr = 8;
	public int yIncr = 8;

	public boolean offScreen; //boolean represents whether or not any item is offScreen
	public int numOffScreen = 100; //represents array index of the item which is off-screen (set to 100 at first because the array is not 100 long)
	public int updateToScore; //represents the amount to change the score by based on objects' locations or being clicked

	public Random r;

	public boolean trashOffScreen;
	public boolean recycleOffScreen;
	public boolean tutorialFinished;

	/**
	 * Constructor for Model class: Sets global variables in Model class
	 * 
	 * @param imgWidth width of a single image
	 * @param imgHeight height of a single image
	 * @param width overall frame width
	 * @param height overall frame height
	 */
	public Model(int imgWidth, int imgHeight, int width, int height) {

		//sets all the values of global variables in the Model class
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.frameHeight = height;
		this.frameWidth = width;
		r = new Random();
		yIncr = r.nextInt(7) + 1;

	}


	/**
	 * updateLocationAndDirection: given an array of on-screen items from View, this method handles the logic to update their positions
	 * 
	 * @param n how many items are on-screen currently
	 * @param itemsToUpdate collection of on-screen items
	 * @param drawHeight y-location of where objects disappear from screen
	 */
	public void updateLocationAndDirection(int n, ArrayList<GameItem> itemsToUpdate, int drawHeight) {

		//GameItem g; //set up arbitrary GameItem variable

		for(GameItem g: itemsToUpdate) { //loop over all objects on screen
			//g = itemsToUpdate.get(i); //set g equal to each object
			if(g.yLoc >= drawHeight) { //if the object's y-location is off the screen
				g.setYLoc(0); //set its location back to zero
				if((g instanceof Trash || g instanceof Recyclable) && g.isThere) //if it is still visible and is either Trash or Recyclable
					updateToScore -= 2; //we should take 2 points off the score, so total score update is decremented by 2
				if(g instanceof Animal)
					updateToScore += 1;
				offScreen = true; //set the offScreen flag to true, since its height is too high
				numOffScreen = itemsToUpdate.indexOf(g); //take the index of the object and set it to denote which item is off-screen
			}else { //if it's not off-screen
				g.setYLoc(g.yLoc + g.yIncr); //increase its y location
			}
		}		
	}


	/**
	 * tutorialUpdate: handles logic for updating game while in tutorial mode
	 * 
	 * @param g - the current GameItem drawn on screen which needs to be updated
	 * @param drawHeight - y-location of where objects disappear from screen
	 * @param clicked - boolean saying whether or not the item in question has been clicked (info is retrieved from MouseListener in View)
	 * @return false, to reset the boolean value in View
	 */

	public boolean tutorialUpdate(GameItem g, int drawHeight, boolean clicked) {
		g.yLoc += 5;
		if(g.yLoc >= drawHeight && !clicked) {
			g.yLoc = 0;
			if(g instanceof Animal)
				tutorialFinished = true;
		}else if(clicked) {
			if(g instanceof Trash) {
				trashOffScreen = true;
			}else if(g instanceof Recyclable) {
				recycleOffScreen = true;
			}
		}
		return false;
	}

	/**
	 * setOffScreen: setter method which sets the offScreen variable using the passed argument. Also resets numOffScreen variable if isOff is false
	 * 
	 * @param isOff boolean representing whether any items are off-screen
	 */
	public void setOffScreen(boolean isOff) {
		offScreen = isOff; //sets the offScreen value to the passed boolean value
		if(!offScreen) { //if nothing is off-screen
			numOffScreen = 100; //then the number of the off-screen item should be set back to 100
		}
	}


	/**
	 * setX: setter method which sets xLoc to the passed argument
	 * 
	 * @param location desired x-location to be set
	 */
	public void setX(int location) {
		xLoc = location; //set x-location to passed argument
	}

	/**
	 * getX: getter method which returns the xLoc of the Model object
	 * 
	 * @return x-location of model object
	 */
	public int getX() { return xLoc; }

	/**
	 * getX: getter method which returns the y-locations of the Model object
	 * 
	 * @return y-location of model object
	 */
	public int[] getY() { return yLoc; }

	/**
	 * setY: a setter method which sets the y-locations of the Model object
	 * 
	 * @param locations - array of y-locations to be set
	 */
	public void setY(int[] locations) { yLoc = locations; }
}