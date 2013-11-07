package com.putable.pqueue;

import java.util.Random;

/**
 * Implementation of PQAble. Has values and setters for the updates of the
 * ConcretePQAble to determine it's priority on the queue.
 * 
 * @author Brennan Collins
 * 
 */
public class ConcretePQAble extends AbstractPQAble {
	// fields for next updates and update periods
	private int nextUpdate, updatePeriod;
	// a random number generator for setting updates
	public Random rand = new Random();

	/**
	 * Setter method for changing how long a PQAble's update period is.
	 * 
	 * @param upPeriod
	 *            update period
	 */
	public void setUpdatePeriod(int upPeriod) {
		this.updatePeriod = upPeriod;
	}

	/**
	 * Getter method for the update period of this PQAble.
	 * 
	 * @return update period
	 */
	public int getUpdatePeriod() {
		return updatePeriod;
	}

	/**
	 * Schedules the PQAbles next action: the next update is today +
	 * updatePeriod.
	 * 
	 * @param upPeriod
	 *            the update period of this object
	 */
	public void reschedule() {
		nextUpdate += updatePeriod;
	}

	/**
	 * Method for setting the initial update for a PQAble. Follows the equation
	 * from the spec (S.4.2.1.5).
	 * 
	 * @param day
	 *            the current day we are on in the simulation
	 */
	public void setInitialNextUpdate(int day) {
		nextUpdate = ((rand.nextInt(25000) + 1) % updatePeriod) + day;
	}

	/**
	 * Getter method for when our next update is.
	 * 
	 * @return next update
	 */
	public int getNextUpdate() {
		return nextUpdate;
	}

	@Override
	public int compareTo(PQAble p) throws ClassCastException {
		final int LV = -1, EQ = 0, GV = 1;
		if (this.nextUpdate < ((ConcretePQAble) p).getNextUpdate())
			return LV;
		else if (nextUpdate == ((ConcretePQAble) p).getNextUpdate())
			return EQ;
		else
			return GV;
	}

}
