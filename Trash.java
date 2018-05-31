import java.awt.image.BufferedImage;

/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
 *
 */
public class Trash extends GameItem {

	/**
	 * Constructor for Trash class: calls superclass constructor
	 * 
	 * @param icon - the BufferedImage associated with the Recyclable object
	 * @param x - the x-location associated with the Recyclable object
	 * @param y - the y-location associated with the Recyclable object
	 */
	public Trash(BufferedImage icon, int x, int y) {
		super(icon, x, y);
	}

	@Override
	/**
	 * scoreUpdate: overrides superclass's scoreUpdate method based on type. When Trash are clicked with left click, score increases. With right click, score decreases.
	 * 
	 * @param side - a String representing which side of the mouse was used to click (i.e. was it a left-click or right-click)
	 */
	public void scoreUpdate(String side) {
		super.scoreUpdate(side);
		if(side.equals("left")) {
			updateToScore += 2;
			clickedRight = true;
			clickedWrong = false;
		}
		else if(side.equals("right")) {
			updateToScore += -2;
			clickedWrong = true;
			clickedRight = false;
		}
	}

}