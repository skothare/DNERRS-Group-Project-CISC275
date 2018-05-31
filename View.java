import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
 *
 */
public class View extends JFrame implements ActionListener {

	Timer timer; //timer object which updates score and spawning of items

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //used to achieve full-screen

	int counter = 0; //variable for game timer

	protected JButton Start, Loc1,Finish,Quit,Menu,Tutorial, Save,Load; //JButtons for game navigation

	final int frameWidth = (int)screenSize.getWidth(); //width of frame is equal to width of screen
	final int frameHeight = (int)screenSize.getHeight(); //height of frame is equal to height of screen


	JFrame frame = new JFrame(); //new JFrame object to draw on screen	
	final int frameStartSize = 700; //initial size of frame

	int rightOffset= 0;

	BufferedImage[] startimg = new BufferedImage[2]; //array for starting images
	BufferedImage background; //image representing background of game
	BufferedImage woodBackground; //image representing background of playerPanel
	BufferedImage coffee;
	BufferedImage crab;
	BufferedImage bottle;
	BufferedImage funfact;
	Trash tutTrash;
	Recyclable tutRecycle;
	Animal tutAnimal;
	BufferedImage[] recycleImages = new BufferedImage[3]; //array for images of recycling objects
	BufferedImage[] animalImages = new BufferedImage[3]; //array for images of animal objects
	BufferedImage[] trashImages = new BufferedImage[5]; //array for images of trash objects

	JPanel buttonPanel = new JPanel(); //JPanel to place buttons on screen
	JPanel playerPanel = new PlayerPanel(); //JPanel to place player icon on screen
	DrawPanel drawPanel = new DrawPanel(); //new DrawPanel object which will represent the majority of the game

	final public int picSize = 165; //size of images
	final public int drawDelay = 50; //delay of 50ms between drawing

	Action drawAction; //AbstractAction used to repaint panel

	public boolean started = false; //boolean for whether the start button has been clicked (the game is past the mode of the initial screen)
	public boolean backAtMenu = false;
	public boolean tutorial = false; //boolean for whether the tutorial is active
	public boolean saveState = false;// boolean to check whether the Save button has been pressed. 
	public boolean loadState = false;

	//x-location and y-locations of different objects (determined in Model)
	/*public int xloc; 
	public int[] yloc = new int[5];*/

	final JLabel label; //JLabel to display timer on screen
	public JLabel player = new JLabel(); //JLabel to display player on screen
	public JLabel instructions = new JLabel();

	//booleans representing whether the game is in the mode where game1 is running (not title screens, instructions, or end screens)
	public boolean game1 = false;

	final JLabel scoreLabel; //JLabel to display score on screen
	final JLabel title = new JLabel(); //JLabel to display title on screen

	public int score = 0; //score variable
	public int finalScore = 0;

	private MouseListener mouseListener; //mouseListener variable to listen for clicks on on-screen objects

	public GameItem currentObject = tutTrash;

	Random r = new Random(); //random object used in item spawning

	public int remaining = 120; //remaining time in game (in seconds)

	int numOnScreen = 0; //number of objects currently on-screen

	public boolean finished = false; //boolean for whether the game has finished by timer expiration

	public boolean offScreen = false; //boolean for whether an item is off-screen
	public boolean trashOffScreen = false;
	public boolean recycleOffScreen = false;
	public boolean tutorialFinished = false;

	public boolean curObjClicked = false;
	public ArrayList<GameItem> gameItems = new ArrayList<GameItem>(); //collection of which items to draw on-screen

	public int drawHeight; //y-location where objects disappear from screen


	/**
	 * setUpButtonPanel: sets up JPanel where buttons will be located (initializes buttons, adds them to panel, adds ActionListeners, etc.
	 */
	public void setUpButtonPanel() {
		if(!finished) { //if the game has not yet finished

			//set Layout of the buttonPanel, initialize and add buttons for Quit and Start, draw Title on screen
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			ImageIcon image = new ImageIcon("pic/quit1.png");
			Quit = new JButton(image);
			Quit.setVerticalTextPosition(AbstractButton.CENTER);
			Quit.setHorizontalTextPosition(AbstractButton.LEADING);
			Quit.setActionCommand("quit");

			Quit.setIcon(image);
			Quit.setContentAreaFilled(false);
			Quit.addActionListener(this);

			ImageIcon image1 = new ImageIcon("pic/start.png");
			Start = new JButton(image1);
			Start.setVerticalTextPosition(AbstractButton.CENTER);
			Start.setHorizontalTextPosition(AbstractButton.LEADING);
			Start.setActionCommand("start");

			Start.setIcon(image1);
			Start.setContentAreaFilled(false);
			Start.addActionListener(this);
			Start.setToolTipText("Click to start");

			ImageIcon image2 = new ImageIcon("pic/tutorial.jpg");
			Tutorial = new JButton(image2);
			Tutorial.setVerticalTextPosition(AbstractButton.CENTER);
			Tutorial.setHorizontalTextPosition(AbstractButton.LEADING);

			Tutorial.setActionCommand("tutorial");
			Tutorial.setIcon(image2);
			Tutorial.setContentAreaFilled(false);
			Tutorial.addActionListener(this);
			Tutorial.setToolTipText("Click to launch tutorial");
			ImageIcon iconTutorial = new ImageIcon("pic/tutorial.jpg");
			Tutorial.setIcon(iconTutorial);

			buttonPanel.add(Start);
			buttonPanel.add(Tutorial);
			buttonPanel.add(Quit);
			getContentPane().setBackground(Color.GRAY);
			buttonPanel.setBackground(Color.gray);
			drawPanel.setBackground(Color.gray);
			buttonPanel.setForeground(Color.gray);
			title.setText("Estuary Adventure!");
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.BLACK);
			title.setFont(new Font(title.getFont().getName(),Font.BOLD,60));
			drawPanel.add(title,BorderLayout.NORTH);
			repaint();
		}else { //if game has finished

			//remove buttons for choosing games, add button to finish the game and return to menu)
			buttonPanel.remove(Loc1);
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			ImageIcon image5 = new ImageIcon("pic/finish.png");
			Finish = new JButton(image5);
			Finish.setIcon(image5);
			Finish.setContentAreaFilled(false);
			Finish.setVerticalTextPosition(AbstractButton.CENTER);
			Finish.setHorizontalTextPosition(AbstractButton.LEADING);
			Finish.setActionCommand("finish");
			Finish.addActionListener(this);
			Finish.setToolTipText("Click to end game");
			buttonPanel.add(Finish);
			getContentPane().setBackground(Color.BLUE);
			buttonPanel.setBackground(Color.GRAY);
			drawPanel.setBackground(Color.BLUE);
			buttonPanel.setForeground(Color.BLUE);
		}
	}


	/**
	 * Constructor for View class
	 * <p>
	 * Sets up JPanels for button and player, sets layout of the frame, initializes drawAction to repaint frame.
	 * <p>
	 * adds buttonPanel and/or playerPanel to screen, depending on the state of the game. Sets drawHeight variable, adds drawPanel to frame.
	 * <p>
	 * Sets up and adds labels for score and timer, and initializes timer to decrease remaining time once per second, as well as increasing the number
	 * of on-screen objects once per second.
	 * <p>
	 * Loads images into appropriate arrays, calls method to determine which objects should be drawn on-screen, and sets up frame properties.
	 */
	public View() {

		setLayout(new BorderLayout()); //set the frame up with a BorderLayout
		setUpButtonPanel(); //sets up JPanel for buttons
		setUpPlayerPanel(); //sets up JPanel for player
		setUpMenu();

		//initializes AbstractAction to repaint screen
		drawAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				drawPanel.repaint();
			}
		};	  



		if(!game1) { //if neither game is actively running
			add(buttonPanel,BorderLayout.SOUTH); //add the button panel to the screen
			repaint();
		}


		try {
			drawHeight = frameHeight - ImageIO.read(new File("pic/player.gif")).getHeight() - 10; //set drawHeight by subtracting height of player image from screen height
		}catch(IOException e) {
			e.printStackTrace(); //print stack trace of exception, if it's thrown
		}

		try {
			background = ImageIO.read(new File("pic/background.png"));
			woodBackground = ImageIO.read(new File("pic/wood.jpg"));
		}catch(IOException e) {
			e.printStackTrace();
		}try {
			funfact = ImageIO.read(new File("pic/funfact.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		Save = new JButton("Save Game");
		Save.setVerticalTextPosition(AbstractButton.CENTER);
		Save.setHorizontalTextPosition(AbstractButton.LEADING);
		Save.setHorizontalAlignment(JLabel.RIGHT);
		Save.setVerticalAlignment(JLabel.BOTTOM);
		Save.setActionCommand("save");
		Save.addActionListener(this);
		Save.setToolTipText("Click to save the game");
		Load = new JButton("Load Last Save");
		Load.setVerticalTextPosition(AbstractButton.CENTER);
		Load.setHorizontalTextPosition(AbstractButton.LEADING);
		Load.setHorizontalAlignment(JLabel.RIGHT);
		Load.setVerticalAlignment(JLabel.BOTTOM);
		Load.setActionCommand("load");
		Load.addActionListener(this);
		Load.setToolTipText("Click to load most recent save");

		getContentPane().add(drawPanel); //add drawPanel object to contentPane
		add(drawPanel,BorderLayout.NORTH); //add drawPanel to north of frame
		label = new JLabel("120 seconds"); //initialize timer label to read 120 seconds
		label.setFont(new Font(label.getFont().getName(),label.getFont().getStyle(),30));
		label.setHorizontalAlignment(JLabel.RIGHT);
		scoreLabel = new JLabel("0"); //initialize score label to 0
		scoreLabel.setFont(new Font(scoreLabel.getFont().getName(),scoreLabel.getFont().getStyle(),30));

		//set foreground colors of both labels to white, to be able to see them
		scoreLabel.setForeground(Color.WHITE);
		label.setForeground(Color.WHITE);

		//initialize timer to tick of 1000ms (1s), and override actionPerformed to update counter and increase numberOnScreen every second
		timer = new Timer(1000, new ActionListener() {

			@Override
			/**
			 * actionPerformed: overrides actionPerformed method to deal with timer ticks
			 * <p>
			 * Increases timer variable, and sets variable for remaining time to be displayed. Keeps track of when game ends by time, and adjusts graphics accordingly
			 * 
			 * @param e - ActionEvent triggered by timer
			 */
			public void actionPerformed(ActionEvent e) {
				counter++;
				if(numOnScreen < 5) { //if there are less than 5 items on screen
					numOnScreen++; //add another one
				}
				remaining = 120 - counter; //counter is increased, so remaining time decreases
				label.setText(String.valueOf(remaining)+"     seconds"); //set text of timer label to the time remaining

				if (counter == 120) { //if 120 seconds have passed

					finished = true; //set the game to finished mode
					setUpButtonPanel(); //set up the button panel for this new mode
					View.this.remove(playerPanel); //remove player from the screen
					View.this.add(buttonPanel,BorderLayout.SOUTH); //add newly-set-up button panel
					for(GameItem ga: gameItems) {
						ga.setYLoc(0);
					}
					finalScore = score;
					scoreLabel.setText("FINAL SCORE: " + String.valueOf(finalScore));
					timer.stop();
					drawPanel.repaint(); //repaint the drawPanel
				}

			}
		}); 

		//loads starting images for game
		startimg[0] = createImage(0); 
		startimg[1] = createImage(1);

		//loads images of animals
		for(int i = 0; i < 3; i++) {
			animalImages[i] = createImage(i + 2);
		}
		//loads images of recycling
		for(int i = 3; i < 6; i++) {
			recycleImages[i-3] = createImage(i + 2);
		}
		//loads images of trash
		for(int i = 6; i < 11; i++) {
			trashImages[i-6] = createImage(i + 2);
		}

		setPreferredSize(new Dimension(frameWidth, frameHeight)); //set size to size of screen
		setExtendedState(JFrame.MAXIMIZED_BOTH); 


		loadGameItems(); //determine which items should be drawn and load them into an ArrayList

		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);  //set orientation of frame
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //set close of frame

		pack(); //pack frame
		setVisible(true); //make frame visible   
	}

	/**
	 * loadGameItems: Chooses items to spawn on screen, creates new objects with associated images and locations, and adds them to an array
	 */
	public void loadGameItems() {
		int xLocToAdd; //integer value for x-location of spawn
		if(gameItems.size() < 5) { //if the ArrayList of on-screen items has fewer than 5 items
			while(gameItems.size() < 5) { //then, until it has a full 5 items,
				xLocToAdd = r.nextInt(frameWidth-rightOffset - 500) + 500; //randomly select a possible location
				int itemType = r.nextInt(2); //determine whether to add an animal, trash object, or recyclable object
				switch(itemType) {
				case 0: //if value is 0
					gameItems.add(new Animal(animalImages[r.nextInt(animalImages.length-1)],xLocToAdd,0)); //add an animal with a random one of the animal images at the chosen x-location
					break;
				case 1: //if value is 1
					gameItems.add(new Recyclable(recycleImages[r.nextInt(recycleImages.length-1)],xLocToAdd,0)); //add a recyclable with a random one of the recycle images at the chosen x-location
					break;
				case 2: //if value is 2
					gameItems.add(new Trash(trashImages[r.nextInt(trashImages.length-1)],xLocToAdd,0)); //add a trash with a random one of the trash images at the chosen x-location
				}
			}
		}
	}


	/**
	 * 
	 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
	 * This class forms JPanel objects with overridden paintComponent method, to draw desired objects and images on screen
	 *
	 */
	public class DrawPanel extends JPanel {


		/**
		 * DrawPanel Constructor
		 * <p>
		 * Sets Layout of DrawPanel objects, adds MouseListener to register mouse clicks on objects drawn on DrawPanel
		 */
		public DrawPanel() {
			setLayout(new BorderLayout()); //set BorderLayout for DrawPanel

			//create and add a mouseListener to DrawPanel object
			mouseListener = createMouseListener(); 
			addMouseListener(mouseListener);

		}

		/**
		 * createMouseListener: creates a MousteListener object and defines what happens when the mouse is clicked
		 * 
		 * @return 1
		 */
		protected MouseListener createMouseListener() {
			MouseListener l = new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent click) {}	


				//other methods which are necessary to override, but which we do not need to implement
				@Override
				public void mouseEntered(MouseEvent enter) {}
				@Override
				public void mouseExited(MouseEvent exit) {}
				/**
				 * mousePressed is different from the mouseClicked method.
				 * mousePressed works better since the action is registered on the mouse button press and
				 * doesn't need the mouse button to be released. Conversely, the mouseClicked method requires
				 * the mouse button to be released to register the action.
				 * @param pressed is the registered MouseEvent recorded by the MouseListener
				 */
				@Override
				/**
				 * mousePressed: Overrides mousePressed method to update score based on objects being clicked
				 * 
				 * @param click - the registered MouseEvent heard by the MouseListener
				 */
				public void mousePressed(MouseEvent click) {
					for(GameItem g: gameItems) { //loop through on-screen items
						if(g.getBounds().contains(click.getX(),click.getY())) { //if the bounds of the rectangle over the object contain the mouse click

							if(SwingUtilities.isRightMouseButton(click)) { //if it's a left click
								g.scoreUpdate("right"); //call the object's updateScore method with left argument
								if(g instanceof Recyclable) {
									g.clickedRight = true;
									g.clickedWrong = false;
								}else if(g instanceof Trash) {
									g.clickedRight = false;
									g.clickedWrong = true;
								}else if(g instanceof Animal) {
									g.clickedRight = false;
									g.clickedWrong = true;
								}
							}
							else if(SwingUtilities.isLeftMouseButton(click)) { //if it's a right click{
								g.scoreUpdate("left"); //call the object's updateScore method with right argument
								if(g instanceof Recyclable) {
									g.clickedRight = false;
									g.clickedWrong = true;
								}else if(g instanceof Trash) {
									g.clickedRight = true;
									g.clickedWrong = false;
								}else if(g instanceof Animal) {
									g.clickedRight = false;
									g.clickedWrong = true;
								}
							}

							score += g.updateToScore; //add the object's updated score value to total score
							g.updateToScore = 0; //reset the object's updated score value
						}

					}

					if(tutorial) {
						if(currentObject.getBounds().contains(click.getX(),click.getY())) {
							if(SwingUtilities.isRightMouseButton(click)) {
								currentObject.scoreUpdate("right");
								if(currentObject instanceof Recyclable) {
									curObjClicked = true;
									currentObject.clickedRight = true;
									currentObject.clickedWrong = false;
								}
								else if(currentObject instanceof Trash) {
									currentObject.yLoc = 0;
									currentObject.image = coffee;
									currentObject.clickedRight = false;
									currentObject.clickedWrong = true;
								}else if(currentObject instanceof Animal) {
									currentObject.yLoc = 0;
									currentObject.image = crab;
									currentObject.clickedRight = false;
									currentObject.clickedWrong = true;
								}
							}
							else if(SwingUtilities.isLeftMouseButton(click)) {
								currentObject.scoreUpdate("left");
								if(currentObject instanceof Trash) {
									curObjClicked = true;
									currentObject.clickedRight = true;
									currentObject.clickedWrong = false;
								}
								else if(currentObject instanceof Recyclable) {
									currentObject.yLoc = 0;
									currentObject.image = bottle;
									currentObject.clickedRight = false;
									currentObject.clickedWrong = true;
								}else if(currentObject instanceof Animal) {
									currentObject.yLoc = 0;
									currentObject.image = crab;
									currentObject.clickedRight = false;
									currentObject.clickedWrong = true;
								}
							}
							score += currentObject.updateToScore;
							currentObject.updateToScore = 0;
						}

					}

					repaint(); //repaint drawPanel
				}	
				@Override
				public void mouseReleased(MouseEvent release) {}

			};
			return l;
		}

		/**
		 * paintComponent: overrides paintComponent method from JPanel 
		 * <p>
		 * Calls the superclass method paintComponent on the same argument, and then draws the appropriate image(s) on the DrawPanel object
		 * depending on the current state of the game.
		 * 
		 * @param g - Graphics object whose drawImage method is used to display objects on the DrawPanel
		 */
		protected void paintComponent(Graphics g) {
			g.setColor(Color.gray);
			super.paintComponent(g);
			if(started) { //if the start button has been clicked
				g.drawImage(startimg[1], 0, 0, getWidth(), getHeight(), Color.gray, this); //display map image as DrawPanel background
			}
			else if(!game1 && !tutorial) //if start hasn't been clicked, and neither game is running
				g.drawImage(startimg[0],0,0, getWidth(), getHeight(), Color.gray,this); //draw starting image as DrawPanel background
			else if(tutorial) {
				g.drawImage(background, 0, 0, getWidth(), getHeight(), Color.gray, this);
				drawClock(g);
				if(!trashOffScreen && !recycleOffScreen && !tutorialFinished) {
					currentObject = tutTrash;
					instructions.setText("Left-click trash objects, like this coffee cup");
					g.drawImage(currentObject.image, getWidth()/2, currentObject.yLoc, this);
					if(currentObject.clickedRight) {
						g.setColor(Color.GREEN);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}else if(currentObject.clickedWrong) {
						g.setColor(Color.RED);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}
				}else if(trashOffScreen && !recycleOffScreen && !tutorialFinished) {
					currentObject = tutRecycle;
					instructions.setText("Right-click recyclable objects, like this bottle");
					g.drawImage(currentObject.image, getWidth()/2, currentObject.yLoc, this);
					if(currentObject.clickedRight) {
						g.setColor(Color.GREEN);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}else if(currentObject.clickedWrong) {
						g.setColor(Color.RED);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}
				}else if(trashOffScreen && recycleOffScreen && !tutorialFinished) {
					currentObject = tutAnimal;
					instructions.setText("Allow animals to pass through to the estuary");
					g.drawImage(currentObject.image, getWidth()/2, currentObject.yLoc, this);
					if(currentObject.clickedRight) {
						g.setColor(Color.GREEN);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}else if(currentObject.clickedWrong) {
						g.setColor(Color.RED);
						g.drawRect(currentObject.xLoc, currentObject.yLoc, (int)currentObject.getBounds().getWidth(), (int)currentObject.getBounds().getHeight());
					}
				}else if(tutorialFinished) {
					timer.stop();
					counter = 0;
					instructions.setText("You'll have two minutes to clean up the estuary."
							+ "			  Click to return to the menu and start the game!");
				}
				currentObject.clickedRight = currentObject.clickedWrong = false;

			}
			else if(game1){ //if one of the games is running
				g.drawImage(background, 0, 0, getWidth(), getHeight(),Color.gray, this);
				drawClock(g);
				for(int i = 0; i < numOnScreen; i++) { //for each item that is supposed to be on-screen
					if(!finished) {//if the game is not finished{
						g.drawImage(gameItems.get(i).image,gameItems.get(i).xLoc,gameItems.get(i).yLoc,this); //draw the object at that index in the array of on-screen items, at its x and y locations
						if(gameItems.get(i).clickedRight) {
							g.setColor(Color.GREEN);
							g.drawRect(gameItems.get(i).xLoc, gameItems.get(i).yLoc, (int)gameItems.get(i).getBounds().getWidth(), (int)gameItems.get(i).getBounds().getHeight());
						}else if(gameItems.get(i).clickedWrong) {
							g.setColor(Color.RED);
							g.drawRect(gameItems.get(i).xLoc, gameItems.get(i).yLoc, (int)gameItems.get(i).getBounds().getWidth(), (int)gameItems.get(i).getBounds().getHeight());
						}
						gameItems.get(i).clickedRight = gameItems.get(i).clickedWrong = false;
					}
				}
				if(counter == 120) {

					g.drawImage(funfact, 0, 0, getWidth(), getHeight(), this);
				}
			}
		}

		/**
		 * drawClock: method which draws a clock on the screen to visualize timer counting down
		 * 
		 * @param g - Graphics object which is used to draw the components of the clock
		 */
		protected void drawClock(Graphics g) {

			int clockRadius = (int)(Math.min(getWidth(), getHeight()) * 0.08 * 1);
			rightOffset = clockRadius * 2;
			int xCenter = frameWidth - 2*clockRadius;
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(6));

			int yCenter = getHeight() / 20 + 60;

			g.setColor(Color.white);
			if (counter >= 75 && counter< 110) {
				if (counter % 5 == 0) 
					g.setColor(Color.red);
			}
			if (counter > 100) {
				if (counter % 2 == 0) 
					g.setColor(Color.red);
			}
			g.drawOval(xCenter - clockRadius, yCenter - clockRadius, 2 * clockRadius, 2 * clockRadius);

			int sLength = (int)(clockRadius * 0.5);
			int xSecond = (int)(xCenter + sLength * Math.sin(counter * (2*Math.PI / 60)));
			int ySecond = (int)(yCenter - sLength * Math.cos(counter * (2*Math.PI / 60)));

			g.setColor(Color.red);
			g2.drawLine(xCenter, yCenter, xSecond, ySecond);

		}

		/**
		 * getPreferredSize: generates a Dimension object based on the starting size of the frame, so that images draw to the correct size
		 * 
		 * @return a new Dimension of the starting size of the frame
		 */
		public Dimension getPreferredSize() {
			return new Dimension(frameWidth,frameHeight - 100);
		}
	}


	/**
	 * resetTutorial: resets the booleans and variables for the tutorial mode so that it can be replayed
	 * 
	 */
	public void resetTutorial() {
		coffee = createImage(10);
		bottle = createImage(5);
		crab = createImage(4);
		tutTrash = new Trash(coffee,getWidth()/2, 0);
		tutRecycle = new Recyclable(bottle, getWidth()/2,0);
		tutAnimal = new Animal(crab, getWidth()/2, 0);
		currentObject = tutTrash;
		trashOffScreen = recycleOffScreen = tutorialFinished = false;
	}


	/**
	 * createImage: method to load in each of the necessary images of the game
	 * 
	 * @param stage an integer representing which image should be loaded in. This is done so images can be loaded into an array using the array index as the argument.
	 * @return a new BufferedImage, which is the result of loading the intended image from a file in another folder at the same height as src.
	 */
	public BufferedImage createImage(int stage){	
		BufferedImage bufferedImage; //arbitrary BufferedImage variable which will be set and then returned. 		
		switch(stage) { //switch over the argument. In all cases, print stack trace if an exception is thrown from loading the image.
		case 0: //if arg is 0, load in the starting image
			try {
				bufferedImage = ImageIO.read(new File("pic/begin_img.jpg"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 1: //if arg is 1, load in the map image
			try {
				bufferedImage = ImageIO.read(new File("pic/instructions.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 2: //if arg is 2, load in the first fish image
			try {
				bufferedImage = ImageIO.read(new File("pic/fish.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 3: //if arg is 3, load in the second fish image
			try {
				bufferedImage = ImageIO.read(new File("pic/fish2.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 4: //if arg is 4, load in the crab image
			try {
				bufferedImage = ImageIO.read(new File("pic/trashGameCrab.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 5: //if arg is 5, load in the first bottle image
			try {
				bufferedImage = ImageIO.read(new File("pic/bottle.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 6: //if arg is 6, load in the second bottle image
			try {
				bufferedImage = ImageIO.read(new File("pic/bottle2.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 7: //if arg is 7, load in the coke can image
			try {
				bufferedImage = ImageIO.read(new File("pic/coke.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 8: //if arg is 8, load in the apple image
			try {
				bufferedImage = ImageIO.read(new File("pic/apple.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 9: //if arg is 9, load in the banana image
			try {
				bufferedImage = ImageIO.read(new File("pic/banana.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 10: //if arg is 10, load in the coffee cup image
			try {
				bufferedImage = ImageIO.read(new File("pic/coffee.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 11: //if arg is 11, load in the first trash bag image
			try {
				bufferedImage = ImageIO.read(new File("pic/trash.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
		case 12://if arg is 12, load in the second trash bag image
			try {
				bufferedImage = ImageIO.read(new File("pic/trash_bag.png"));
				return bufferedImage;
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return null; //if none of the above return statements trigger, return null. This line is included so the code compiles.
	}


	/**
	 * 
	 * @author Andrew Riegner, Sumeet Kothare, Woody Hu, Arnav Bindra, Tong Zhao
	 *
	 */
	public class PlayerPanel extends JPanel{

		/**
		 * paintComponent: overrides paintComponent method from JPanel to implement drawImage to use our background image
		 * 
		 * @param g - Graphics object used to call drawImage
		 */
		protected void paintComponent(Graphics g) {
			g.setColor(Color.gray);
			super.paintComponent(g);
			g.drawImage(woodBackground, 0, 0, getWidth(), getHeight(), this);
		}

	}


	/**
	 * setUpPlayerPanel: method to set an icon on the playerPanel JPanel object, and change its cursor image
	 */
	public void setUpPlayerPanel() {
		ImageIcon icon = new ImageIcon("pic/player.gif"); //load in player image to be drawn on panel
		player.setIcon(icon); //set this as the icon of the JLabel
		playerPanel.add(player); //add player JLabel to playerPanel JPanel

		Cursor cursor_closed = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("pic/cursor_closed.png").getImage(),new Point(10,20), "stick"); //create new cursor with custom image
		playerPanel.setCursor(cursor_closed); //set the JPanel's cursor to the custom cursor 

	}


	/**
	 * actionPerformed: overrides actionPerformed method from ActionListener in order for View class to implement ActionListener. This method deals with button presses when they occur
	 * 
	 * @param e - the ActionEvent registered by the ActionListener. In this case they are button presses in various stages of the game.
	 */
	public void actionPerformed(ActionEvent e) {

		if("start".equals(e.getActionCommand())) { //if the ActionCommand of the event matches that of the start button
			started = true;
			ImageIcon image3 = new ImageIcon("pic/begin.jpg");
			Loc1 = new JButton(image3);
			Loc1.setIcon(image3);
			Loc1.setContentAreaFilled(false);
			buttonPanel.add(Loc1);
			buttonPanel.remove(Start);
			buttonPanel.remove(Quit);

			buttonPanel.remove(Tutorial);
			tutorial = false;

			drawPanel.remove(title);
			if(backAtMenu) {
				playerPanel.remove(Menu);
				backAtMenu = false;
			}
			repaint();

			Loc1.setVerticalTextPosition(AbstractButton.CENTER);
			Loc1.setHorizontalTextPosition(AbstractButton.LEADING);
			Loc1.setActionCommand("start game 1");
			Loc1.addActionListener(this);
			Loc1.setToolTipText("Click to start Game 1");
			getContentPane().setBackground(Color.GRAY);
			buttonPanel.setBackground(Color.gray);
			drawPanel.setBackground(Color.gray);
			buttonPanel.setForeground(Color.gray);

		}else if("tutorial".equals(e.getActionCommand())) {
			resetTutorial();
			drawPanel.remove(title);
			this.add(playerPanel,BorderLayout.SOUTH);
			this.remove(buttonPanel);
			this.add(drawPanel);
			playerPanel.add(Menu,BorderLayout.EAST);

			setUpInstructions();

			drawPanel.add(scoreLabel,BorderLayout.SOUTH);
			Cursor cursor_open = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("pic/cursor_open.png").getImage(),new Point(10,20), "stick");
			drawPanel.setCursor(cursor_open);
			add(playerPanel,BorderLayout.SOUTH);
			if(!finished) {
				scoreLabel.setText("Score: " + String.valueOf(score));
			}
			timer.start();
			started = game1 = finished = false;
			tutorial = true;
			repaint();

		}else if("start game 1".equals(e.getActionCommand())) {
			this.remove(buttonPanel);
			add(playerPanel, BorderLayout.SOUTH);
			playerPanel.revalidate();
			playerPanel.repaint();
			add(drawPanel);
			drawPanel.revalidate();
			drawPanel.add(scoreLabel,BorderLayout.SOUTH);
			playerPanel.add(Menu);
			playerPanel.add(Save);
			playerPanel.add(Load);
			Cursor cursor_open = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("pic/cursor_open.png").getImage(),new Point(10,20), "stick");
			drawPanel.setCursor(cursor_open);
			if(!finished) {
				scoreLabel.setText("Score: " + String.valueOf(score));
			}
			timer.start();
			started = false;
			game1 = true;
			numOnScreen = 1;

			add(drawPanel);
			add(playerPanel, BorderLayout.SOUTH);
			playerPanel.revalidate();
			remove(buttonPanel);
			playerPanel.repaint();

		}else if("finish".equals(e.getActionCommand())) {
			backAtMenu = true;
			playerPanel.remove(Save);
			playerPanel.remove(Load);
			finished = started = game1 = tutorial = false;
			buttonPanel.remove(Finish);
			playerPanel.remove(Menu);

			remove(buttonPanel);

			add(buttonPanel,BorderLayout.SOUTH);

			buttonPanel.add(Start);
			buttonPanel.add(Tutorial);
			buttonPanel.add(Quit);
			buttonPanel.repaint();

			title.setText("Estuary Adventure!");
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.BLACK);
			title.setFont(new Font(title.getFont().getName(),Font.PLAIN,60));
			drawPanel.add(title,BorderLayout.NORTH);
			getContentPane().setBackground(Color.GRAY);
			buttonPanel.setBackground(Color.gray);
			drawPanel.setBackground(Color.gray);
			buttonPanel.setForeground(Color.gray);
			drawPanel.remove(scoreLabel);
			counter = 0;
			drawPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			score = 0;
			for(GameItem g: gameItems) {
				g.setYLoc(0);
			}
			label.setText("120 seconds");
			drawPanel.repaint();

		}else if("quit".equals(e.getActionCommand())) {
			System.exit(0);
		}else if("back to menu".equals(e.getActionCommand())) {
			if(game1) {
				playerPanel.remove(Save);
				playerPanel.remove(Load);
			}
			drawPanel.remove(instructions);
			backAtMenu = true;
			View.this.remove(playerPanel);
			//drawPanel.remove(label);
			drawPanel.remove(scoreLabel);
			drawPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			drawPanel.setBackground(Color.gray);
			title.setText("Estuary Adventure!");
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.BLACK);
			title.setFont(new Font(title.getFont().getName(),Font.BOLD,60));
			drawPanel.add(title,BorderLayout.NORTH);
			drawPanel.getPreferredSize();
			add(drawPanel);
			buttonPanel.add(Start);
			buttonPanel.add(Quit);
			buttonPanel.add(Tutorial);
			buttonPanel.remove(Menu);
			if(!tutorial) {
				buttonPanel.remove(Loc1);
			}
			buttonPanel.setBackground(Color.gray);
			buttonPanel.setForeground(Color.gray);
			add(buttonPanel,BorderLayout.SOUTH);
			finished = started = game1 = false;
			getContentPane().setBackground(Color.GRAY);
			counter = -2;
			score = 0;
			label.setText("120 seconds");
			for(GameItem g: gameItems) {
				g.setYLoc(0);
			}
			tutorial = game1 = false;
			currentObject = tutTrash;
			curObjClicked = false;
			trashOffScreen = false;
			recycleOffScreen = false;
			tutorialFinished = false;
			drawPanel.repaint();
			repaint();
		}else if("save".equals(e.getActionCommand())) {
			saveState = true;
		}else if("load".equals(e.getActionCommand())) {
			loadState = true;
		}
	}


	/**
	 * setUpMenu: initializes and sets values for the button to return to the menu
	 * 
	 */

	public void setUpMenu() {
		ImageIcon image4 = new ImageIcon("pic/menu.png");
		Menu = new JButton(image4);
		Menu.setVerticalTextPosition(AbstractButton.CENTER);
		Menu.setHorizontalTextPosition(AbstractButton.LEADING);
		Menu.setActionCommand("back to menu");
		Menu.setIcon(image4);
		Menu.setContentAreaFilled(false);
		Menu.addActionListener(this);
		Menu.setToolTipText("Click to return to main menu (leaves game)");
	}



	/**
	 * setUpInstructions: Sets up the JLabel for the instructions during the tutorial of the game
	 * 
	 */
	public void setUpInstructions() {
		instructions.setHorizontalAlignment(JLabel.CENTER);
		instructions.setVerticalAlignment(JLabel.BOTTOM);
		instructions.setForeground(Color.WHITE);
		instructions.setFont(new Font(instructions.getFont().getName(),instructions.getFont().getStyle(),30));
		drawPanel.add(instructions);
	}


	/**
	 * isStarted: Method which returns the boolean value to determine whether the start button has already been pressed.
	 * 
	 * @return the boolean representing whether or not the game has been started by pressing the start button.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * randomizeImages: generates new random object, either Animal, Trash, or Recyclable, with random image and location, to be drawn on-screen after another object goes off-screen.
	 * 
	 * @param numIsOff - number of object which has gone off-screen. This number corresponds to the index in the collection of objects of the object which is to be replaced.
	 */
	public void randomizeImages(int numIsOff) {
		int newLocation = r.nextInt(frameWidth - rightOffset - 500) + 500;
		int objectType = r.nextInt(100);
		objectType = objectType % 3;
		int newImage;
		switch(objectType) {
		case 0:
			newImage = r.nextInt(5);
			gameItems.set(numIsOff, new Trash(trashImages[newImage],newLocation,0));
			break;
		case 1:
			newImage = r.nextInt(3);
			gameItems.set(numIsOff, new Animal(animalImages[newImage],newLocation,0));
			break;
		case 2:
			newImage = r.nextInt(3);
			gameItems.set(numIsOff, new Recyclable(recycleImages[newImage],newLocation,0));
		}


		offScreen = false;
	}

	/**
	 * update: method which updates the View class so that the game progresses
	 * 
	 * @param isOff - boolean variable for whether or not an object is off-screen (determined by Model class)
	 * @param numIsOff - integer variable for which index in the collection of objects contains the object which has gone off-screen (determined in Model class)
	 * @param updateToScore - value by which the score should be incremented (whether positive or negative) (determined in Model class)
	 */
	public void update(boolean isOff, int numIsOff, int updateToScore) {
		offScreen = isOff;
		if(offScreen) {
			randomizeImages(numIsOff);
		}
		score += updateToScore;
		if(!finished) {
			scoreLabel.setText("Score: " + String.valueOf(score));
		}
		drawPanel.repaint();
	}

}