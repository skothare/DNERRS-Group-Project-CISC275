import java.awt.image.BufferedImage;

/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
 *
 */
public class Animal extends GameItem {

	/**
	 * Constructor for Animal class: calls superclass constructor
	 * 
	 * @param icon - the BufferedImage associated with the Animal object
	 * @param x - the x-location associated with the Animal object
	 * @param y - the y-location associated with the Animal object
	 */
	public Animal(BufferedImage icon, int x, int y) {
		super(icon,x,y);
	}

	@Override
	/**
	 * scoreUpdate: overrides superclass's scoreUpdate method based on type. When Animals are clicked with either left or right click, score should decrease by 2.
	 * 
	 * @param side - a String representing which side of the mouse was used to click (i.e. was it a left-click or right-click)
	 */
	public void scoreUpdate(String side) {
		super.scoreUpdate(side);
		updateToScore -= 2;
		clickedWrong = true;
		clickedRight = false;
	}

}
