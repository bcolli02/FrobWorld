package com.putable.frobworld;

import java.awt.Point;

/**
 * Class for a "Rock" object. Since a rock doesn't have any properties other
 * than its location, it only implements the interface "{@link #Thing}" and does
 * not have any other methods other than the ones supplied by "Thing" and the
 * rock's only field is location.
 * 
 * @author Brennan Collins
 * 
 */
public final class Rock implements Thing {
	// The rock's location
	Point location;

	@Override
	public void setLocation(Point p) {
		this.location = p;
	}

	@Override
	public Point getLocation() {
		return location;
	}

}
