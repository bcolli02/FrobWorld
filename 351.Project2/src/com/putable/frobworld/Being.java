package com.putable.frobworld;

import java.awt.Point;

import com.putable.pqueue.ConcretePQAble;

/**
 * The abstract class Being stands as a model for what beings should have and
 * what they can do. Since every Being is also a {@link #Thing}, it contains a
 * Point for its current location. It also has an inner class for deciding what
 * else lies in its neighborhood, along with abstract methods that every Being
 * must do. Also contains fields for the Being's mass, birth mass, the
 * percentage of the Being's mass that it gives to its child, its mass tax
 * mills, and fixed overhead.
 * 
 * @author Brennan Collins
 * 
 */
public abstract class Being extends ConcretePQAble implements Thing {
	// the location of our being
	private Point location;
	// our being's mass
	public int mass;
	public boolean isAlive = true;
	public int birthMass, birthPercent, massTaxMills, fixedOverHead;
	// a defining neighborhood of what surrounds our being
	public Neighborhood neighborhood = new Neighborhood();

	@Override
	public void setLocation(Point p) {
		this.location = p;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	/**
	 * Any time it is a being's turn on the Queue, they must perform an action.
	 * This varies between Grass and Frobs. Grass action consists of paying a
	 * tax and attempting to reproduce. Frob actions include any actions that
	 * grass must perform as well as moving and eating.
	 */
	public abstract void performAction();

	/**
	 * First action taken by a being.
	 */
	public abstract void payTax();

	/**
	 * A method to determine if our Being is alive
	 * 
	 * @return
	 */
	public abstract boolean checkForLife();

	/**
	 * A method for Beings to reproduce. Returns a being to be thrown on the
	 * priority queue.
	 * 
	 * @return a child of our Being
	 */
	public abstract Being reproduce();

	/**
	 * Setter method for a being's mass.
	 * 
	 * @param m
	 *            value to set the mass to
	 */
	public void setMass(int m) {
		this.mass = m;
	}

	/**
	 * Getter method for the mass of a being.
	 * 
	 * @return the being's mass
	 */
	public int getMass() {
		return mass;
	}

	/**
	 * A that consists of 4 booleans that represent each of the cardinal
	 * directions. If a direction is false that means the location in this
	 * direction is empty. Otherwise the location contains some other "Thing".
	 * 
	 * @author Brennan Collins
	 * 
	 */
	public class Neighborhood {
		char n, e, s, w;
		public boolean north = false, south = false, east = false,
				west = false;
	}
}
