package com.putable.frobworld;

/**
 * The class Grass contains fields and methods that define what a Grass can and
 * cannot do. Since it extends Being, it must contain all the properties and
 * abstract methods from Being. Since grass has no more ability than what is
 * given by Being it only has methods and fields given from extending Being.
 * 
 * @author Brennan Collins
 * 
 */
public final class Grass extends Being {

	/**
	 * Constructor for our grass object.
	 * 
	 * @param mass
	 *            the mass of grass
	 * @param birthMass
	 *            the mass at which grass attempts to split
	 * @param birthPercent
	 *            the percentage of the grass' mass to give to its child
	 * @param massTaxMills
	 *            a component of the tax equation
	 * @param fixedOverHead
	 *            some fixed value to add to the tax
	 * @param updatePeriod
	 *            the initial update period of our grass
	 */
	public Grass(int mass, int birthMass, int birthPercent, int massTaxMills,
			int fixedOverHead, int updatePeriod) {
		this.isAlive = true;
		setMass(mass);
		this.birthMass = birthMass;
		this.birthPercent = birthPercent;
		this.massTaxMills = massTaxMills;
		this.fixedOverHead = fixedOverHead;
		setUpdatePeriod(updatePeriod);
	}
	
	@Override
	public void performAction() {
		payTax();
	}

	@Override
	public void payTax() {
		int m = getMass() - massTaxMills * getUpdatePeriod() / 1000
				+ fixedOverHead;
		setMass(m);
		if (getMass() <= 0)
			isAlive = false;
	}

	@Override
	public Grass reproduce() {
		Grass child = new Grass((getMass() * birthPercent) / 100, birthMass,
				birthPercent, massTaxMills, fixedOverHead, getUpdatePeriod());
		int m = getMass() - ((getMass() * birthPercent) / 100);
		setMass(m);
		return child;
	}

	@Override
	public boolean checkForLife() {
		return isAlive;
	}
}