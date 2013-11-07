package com.putable.frobworld;

import java.awt.Point;

/**
 * The interface Thing is simple. It defines what all "things" must have (a
 * point on the grid), and what all things must be able to set or retrieve (a
 * point).
 * 
 * @author Brennan Collins
 * 
 */
public interface Thing {

	/**
	 * Setter method for the location of this "Thing".
	 * 
	 * @param p
	 *            the point on the grid where the Thing is
	 */
	public void setLocation(Point p);

	/**
	 * Getter method for this Thing's location.
	 * 
	 * @return this "Thing's" location
	 */
	public Point getLocation();
}
