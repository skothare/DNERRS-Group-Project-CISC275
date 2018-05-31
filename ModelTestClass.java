import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.Test.*;

public class ModelTestClass {

	Model m = new Model(0,0,0,0);

	boolean offscreentest;


	/**
	 * updateDirectionTest: tests Model's updateLocationAndDirection method thoroughly
	 * 
	 */
	@Test
	public void updateDirectionTest() {
		ArrayList<GameItem> myItems = new ArrayList<GameItem>();
		myItems.add(new GameItem(null, 0, 0));
		myItems.add(new Trash(null,0,701));
		myItems.add(new Recyclable(null,0,703));
		myItems.add(new Animal(null,0,710));
		m.updateLocationAndDirection(myItems.size(),myItems,700);
		// The y-location is updated randomly. Hence, the test below tests for the location value within an interval.
		assertTrue(myItems.get(0).yLoc >= 5 && myItems.get(0).yLoc <= 8); 
		assertEquals(0,myItems.get(1).yLoc);
		assertEquals(0,myItems.get(2).yLoc);
		assertEquals(0,myItems.get(3).yLoc);
		assertEquals(-3,m.updateToScore);
		assertEquals(true,m.offScreen);

	}

	/**
	 * setOffScreenTest: tests Model's setOffScreen method thoroughly
	 * 
	 */
	@Test
	public void setOffScreenTest() {
		Model n = new Model(255,255,255,255);
		n.setOffScreen(true);
		assertEquals(true,n.offScreen);
		n.setOffScreen(false);
		assertEquals(false,n.offScreen);
		assertEquals(100,n.numOffScreen);
	}

	/**
	 * setXTest: tests Model's setX method thoroughly
	 * 
	 */
	@Test
	public void setXTest() {
		m.setX(23);
		int result = m.getX();
		assertEquals(23,result);
		m.setX(24);
		int result2 = m.getX();
		assertEquals(24,result2);
	}


	/**
	 * getXTest: tests Model's getX method thoroughly
	 * 
	 */
	@Test
	public void getXTest() {
		m.setX(55);
		assertEquals(55,m.getX());
		m.setX(100);
		assertEquals(100,m.getX());
	}


	/**
	 * getYTest: tests Model's getY method thoroughly
	 * 
	 */
	@Test
	public void getYTest() {
		int[] test = {0,0,0,0,0};
		m.setY(test);
		assertEquals(test,m.getY());
	}

	/**
	 * tutorialUpdateTest: test's Model's tutorialUpdate method thoroughly
	 * 
	 */
	@Test
	public void tutorialUpdateTest() {
		Model m = new Model(100,100,1000,1000);
		ArrayList<GameItem> myItems = new ArrayList<GameItem>();
		myItems.add(new Trash(null,0,600));
		myItems.add(new Recyclable(null,0,703));
		myItems.add(new Animal(null,0,710));
		m.tutorialUpdate(myItems.get(2), 700, false);
		assertEquals(true,m.tutorialFinished);
		assertEquals(0,myItems.get(2).yLoc);
		m.tutorialUpdate(myItems.get(0),700,true);
		assertEquals(true,m.trashOffScreen);
		assertEquals(605,myItems.get(0).yLoc);
		m.tutorialUpdate(myItems.get(1),700,true);
		assertEquals(true,m.recycleOffScreen);
	}

}
