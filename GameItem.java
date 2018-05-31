import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.util.*;

import javax.imageio.ImageIO;

import java.io.Serializable;

/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
 *
 */
public class GameItem implements Serializable {



	/**
	 * Determines if a de-serialized file is compatible with this class
	 * @serial
	 */
	private static final long serialVersionUID = -4056124972097052374L;


	/**
	 * Image to display associated with this object
	 * @serial
	 */
	transient BufferedImage image;

	/**
	 * x-location of object
	 * @serial
	 */
	public int xLoc;

	/**
	 * y-location of object
	 * @serial
	 */
	public int yLoc;

	/**
	 * width of object image
	 * @serial
	 */
	public int width;

	/**
	 * height of object image
	 * @serial
	 */
	public int height;

	/**
	 * value by which to update score
	 * @serial
	 */
	public int updateToScore;

	public boolean isThere = true;
	public static Random r = new Random();

	/**
	 * value by which to increment y integer
	 * @serial
	 */
	public int yIncr;

	public boolean clickedRight = false;
	public boolean clickedWrong = false;

	/**
	 * Constructor for GameItem class: sets global variable of GameItem object
	 * 
	 * @param icon - the BufferedImage associated with the GameItem object
	 * @param x - the x-location associated with the GameItem object
	 * @param y - the y-location associated with the GameItem object
	 */
	public GameItem(BufferedImage icon, int x, int y) {
		image = icon;
		xLoc = x;
		yLoc = y;
		height = 100;
		width = 100;
		yIncr = r.nextInt(3) + 5;

	}
	/**
	 * This method creates a "fields" parameter which stores all the objects read from the input file.
	 * Each of the objects or fields are read and assigned to the respective fields in the GameItem class.
	 * @param ois ObjectInputStream used as parameter
	 * @throws java.io.IOException if file cannot be read
	 * @throws ClassNotFoundException if class cannot be found
	 */
	protected void readObject(java.io.ObjectInputStream ois)
			throws java.io.IOException, ClassNotFoundException
	{
		java.io.ObjectInputStream.GetField fields = ois.readFields();

		ois.defaultReadObject();

		xLoc = (int)fields.get("xLoc", 0);
		yLoc = (int)fields.get("yLoc", 0);
		width = (int)fields.get("width", 0);
		height = (int)fields.get("height", 0);
		updateToScore = (int)fields.get("updateToScore", 0);
		yIncr = (int)fields.get("yIncr", 0);
		image = ImageIO.read(ois);

	}
	/**
	 * This method also creates a parameter "fields" which is set equal to the collection of fields of the current
	 * instance of the GameItem objects.
	 * @param oos objectOutputStream used as parameter
	 * @throws java.io.IOException if file cannot be written to
	 */
	protected void writeObject(java.io.ObjectOutputStream oos)
			throws java.io.IOException
	{
		java.io.ObjectOutputStream.PutField fields = oos.putFields();

		oos.defaultWriteObject();

		fields.put("xLoc", xLoc);
		fields.put("yLoc", yLoc);
		fields.put("width", width);
		fields.put("height", height);
		fields.put("updateToScore", updateToScore);
		fields.put("yIncr", yIncr);
		ImageIO.write(image, "png", oos);
		oos.writeFields();
	}

	/**
	 * setYLoc: setter method which sets the y-location of the GameItem object
	 * 
	 * @param y - desired y-location of GameItem object
	 */
	public void setYLoc(int y) {
		yLoc = y;
	}

	/**
	 * getBounds: generates a rectangle around the given GameItem object which can be used for detection of MouseEvents.
	 * 
	 * @return a Rectangle whose bounds describe the location of the GameItem object
	 */
	public Rectangle getBounds() {
		return new Rectangle(xLoc,yLoc,width,height);
	}

	/**
	 * scoreUpdate: a method which sets the global updateToScore variable based on what happens to the object. This will later be added to the existing score.
	 * 
	 * @param side - a String representing which side of the mouse was used to click (i.e. was it a left-click or right-click)
	 */
	public void scoreUpdate(String side) {
		image = null;
		isThere = false;
	}

}
