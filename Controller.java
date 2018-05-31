
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;


/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
 *
 */
public class Controller {

	//create single instance of Model and View objects
	private Model model; 
	private View view;



	/**
	 * Constructor for Controller class: initializes global Model and View variables declared above.
	 * 
	 */
	public Controller(){
		view = new View(); //initialize View
		model = new Model(view.picSize,view.picSize,view.getWidth(),view.getHeight()); //initialize Model with values from View
	}

	/**
	 * main: Main method of program which governs how it runs.
	 * 
	 * @param args - String array of arguments to call when program is executed
	 */
	public static void main(String args[]) {
		Controller c = new Controller(); //create a new instance of Controller class to run game


		//code for setting up and invoking Runnable object to run the game
		EventQueue.invokeLater(new Runnable() {
			Action gameAction = new AbstractAction() {
				/**
				 * actionPerformed: overrides actionPerformed method from AbstractAction so that the timer can govern the working of the game.
				 * 
				 */
				public void actionPerformed(ActionEvent event) {
					if(c.view.game1) { //if either game is running (not in the start screen, instructions, etc., but actually in the game)
						c.model.setOffScreen(c.view.offScreen); //tells the model object whether or not any items are off-screen still
						c.model.updateLocationAndDirection(c.view.numOnScreen,c.view.gameItems,c.view.drawHeight); //updates locations/logic using values passed from View
						if(c.view.saveState){
							System.out.println("Save Button Pressed");
							FileOutputStream fos;
							c.view.timer.stop();
							try {
								System.out.print("before saving: ");
								for(GameItem g: c.view.gameItems) {
									System.out.print(g.xLoc + " ");
								}
								System.out.println();
								fos = new FileOutputStream("serial/tempdata.ser");
								ObjectOutputStream oos = null;
								oos = new ObjectOutputStream(fos);
								for(GameItem g: c.view.gameItems) {
									oos.writeObject(g);
								}
								System.out.print("saved locations: ");
								for(GameItem g: c.view.gameItems) {
									System.out.print(g.xLoc + " " + g.yLoc + ",");
								}
								System.out.println();
								oos.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
							c.view.saveState = false;
						}
						if(c.view.loadState) {
							System.out.println("Load Button Pressed");
							FileInputStream fis;
							c.view.timer.start();
							try {
								fis = new FileInputStream("serial/tempdata.ser");
								ObjectInputStream ois = null;
								ois = new ObjectInputStream(fis);
								for(int i = 0; i < c.view.gameItems.size(); i++) {
									c.view.gameItems.remove(i);
									c.view.gameItems.add(i, (GameItem)ois.readObject());
									System.out.println(c.view.gameItems.get(i).xLoc +" "+c.view.gameItems.get(i).yLoc );
									c.view.update(c.model.offScreen,c.model.numOffScreen,c.model.updateToScore); 
								}
								System.out.print("loaded locations: ");
								for(GameItem g: c.view.gameItems) {
									System.out.print(g.xLoc + " " + g.yLoc + ", ");

								}
								System.out.println(); 
								ois.close();
							}catch(Exception e) {
								e.printStackTrace();
							}
							c.view.loadState = false;
						}
					}
					if(c.view.tutorial) {
						c.view.curObjClicked = c.model.tutorialUpdate(c.view.currentObject, c.view.drawHeight, c.view.curObjClicked);
						c.view.trashOffScreen = c.model.trashOffScreen;
						c.view.recycleOffScreen = c.model.recycleOffScreen;
						c.view.tutorialFinished = c.model.tutorialFinished;
					}
					else {
						c.model.trashOffScreen = false;
						c.model.recycleOffScreen = false;
						c.model.tutorialFinished = false;
					}
					c.view.update(c.model.offScreen,c.model.numOffScreen,c.model.updateToScore); //updates the view with locations/logic calculated in Model
					c.model.updateToScore = 0; //resets updateToScore variable in Model each tick
				}
			};

			public void run() {
				Timer t = new Timer(c.view.drawDelay,gameAction); //sets up a new timer based on the drawDelay variable

				t.start(); //starts timer to run game
			}
		});
	}

}
