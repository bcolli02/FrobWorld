package com.putable.frobworld;

import java.awt.Point;
import java.util.Random;

/**
 * The class Frob contains fields and methods that define what a Frob can and
 * cannot do. Since it extends Being, it must contain all the properties and
 * abstract methods from {@link #Being}. On top of this, Frobs can also eat,
 * move, and determine what lies in its neighborhood.
 * 
 * @author Brennan Collins
 */
public final class Frob extends Being {
	// the frobGenotype of our Frob
	public Genotype frobGenotype;
	// the location and old location of our Frob
	public Point location;
	// odds of mutating
	public int mutationOdds;
	// which generation this frob is a part of
	public int generation = 0;

	/**
	 * Constructor for our Frob object. This constructor is used in the creation
	 * of the initial Frobs in the simulation.
	 * 
	 * @param genes
	 *            the Frob's {@link #Genotype}
	 * @param rand
	 *            the random number generator to be passed through all the Frob
	 *            World classes
	 * @param mass
	 *            the new Frob's initial mass
	 * @param birthMass
	 *            the mass at which the frob will attempt to split
	 * @param birthPercent
	 *            the percentage of a frob's mass it gives to its offspring
	 * @param massTaxMills
	 *            a component of the tax equation
	 * @param fixedOverHead
	 *            some fixed value to add to the tax
	 * @param updatePeriod
	 *            the update period of the Frob
	 * @param mutationOdds
	 *            the chances a byte of data in a Frob's genotype will be
	 *            altered
	 */
	public Frob(Genotype genes, Random rand, int mass, int birthMass,
			int birthPercent, int massTaxMills, int fixedOverHead,
			int updatePeriod, int mutationOdds) {
		this.rand = rand;
		this.isAlive = true;
		this.frobGenotype = genes;
		setMass(mass);
		this.birthMass = birthMass;
		this.birthPercent = birthPercent;
		this.massTaxMills = massTaxMills;
		this.fixedOverHead = fixedOverHead;
		setUpdatePeriod(updatePeriod);
		this.mutationOdds = mutationOdds;
	}

	/**
	 * Constructor for our Frob object. This constructor is used in the creation
	 * of the offspring Frobs in the simulation.
	 * 
	 * @param rando
	 *            the random number generator to be passed through all the Frob
	 *            World classes
	 * @param mass
	 *            the new Frob's initial mass
	 * @param massTaxMills
	 *            a component of the tax equation
	 * @param fixedOverHead
	 *            some fixed value to add to the tax
	 * @param mutationOdds
	 *            the chances a byte of data in a Frob's genotype will be
	 *            altered
	 */
	public Frob(Random rando, int mass, int massTaxMills, int fixedOverHead,
			int mutationOdds) {
		this.rand = rando;
		frobGenotype = new Genotype();
		this.isAlive = true;
		setMass(mass);
		makeGenes();
		this.birthMass = frobGenotype.genes[0] / 2 + 20;
		this.birthPercent = frobGenotype.genes[1] * 100 / 255;
		this.massTaxMills = massTaxMills;
		this.fixedOverHead = fixedOverHead;
		int up = (frobGenotype.genes[2] % 32) + 5;
		this.setUpdatePeriod(up);
		this.mutationOdds = mutationOdds;
	}

	@Override
	public void performAction() {
		payTax();
	}

	@Override
	public void payTax() {
		mass -= (massTaxMills * getUpdatePeriod() / 1000) + fixedOverHead;
		if (mass <= 0)
			isAlive = false;
	}

	/**
	 * Method for Frobs to decide in which direction to move.
	 * 
	 * @return a character that determines to move north (n), east (e), south
	 *         (s), or west (w)
	 */
	public char move() {
		int no = getNorthPrefs(), ea = getEastPrefs(), so = getSouthPrefs(), we = getWestPrefs();
		int totalPrefs = no + ea + so + we;
		int r = rand.nextInt(totalPrefs);
		if ((r -= (no + 1)) < 0)
			return 'n';
		else if ((r -= (so + 1)) < 0)
			return 's';
		else if ((r -= (ea + 1)) < 0)
			return 'e';
		else
			return 'w';
	}

	/**
	 * A method for a Frob to consume a {@link #Grass} object.
	 * 
	 * @param b
	 *            the grass object to be consumed
	 */
	public void consume(Being b) {
		int newMass = getMass() + b.getMass();
		if (newMass > 255)
			return;
		else
			this.setMass(newMass);
	}

	/**
	 * Function to create the genes for the initial Frobs in our Frob World
	 * simulation.
	 */
	public void makeGenes() {
		int length = frobGenotype.DNA_LENGTH;
		for (int i = 0; i < length; i++) {
			frobGenotype.genes[i] = rand.nextInt(Integer.MAX_VALUE) % 256;
		}
	}

	@Override
	public Frob reproduce() {
		int childMass = mass * birthPercent / 100;
		mass -= childMass;
		if (this.getMass() <= 0)
			isAlive = false;
		Genotype childGenes = geneMutation();
		Frob child = new Frob(childGenes, rand, childMass,
				childGenes.genes[0] / 2 + 20, childGenes.genes[1] * 100 / 255,
				massTaxMills, fixedOverHead, childGenes.genes[2] % 32 + 5,
				mutationOdds);
		return child;
	}

	/**
	 * Method to determine a Frob's preferences to move north.
	 * 
	 * @return the value for the Frob's north preferences
	 */
	public int getNorthPrefs() {
		int n = 0;
		if (this.neighborhood.n == 0) {
			n = frobGenotype.DNA_NORTH_PREFS + frobGenotype.DNA_EMPTY_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.n == 'R') {
			n = frobGenotype.DNA_NORTH_PREFS + frobGenotype.DNA_ROCK_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.n == 'G') {
			n = frobGenotype.DNA_NORTH_PREFS + frobGenotype.DNA_GRASS_OFFSET;
			return frobGenotype.genes[n];
		} else {
			n = frobGenotype.DNA_NORTH_PREFS + frobGenotype.DNA_FROB_OFFSET;
			return frobGenotype.genes[n];
		}
	}

	/**
	 * Method to determine a Frob's preferences to move east.
	 * 
	 * @return the value for the Frob's east preferences
	 */
	public int getEastPrefs() {
		int n = 0;
		if (this.neighborhood.east == false) {
			n = frobGenotype.DNA_EAST_PREFS + frobGenotype.DNA_EMPTY_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.e == 'R') {
			n = frobGenotype.DNA_EAST_PREFS + frobGenotype.DNA_ROCK_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.e == 'G') {
			n = frobGenotype.DNA_EAST_PREFS + frobGenotype.DNA_GRASS_OFFSET;
			return frobGenotype.genes[n];
		} else {
			n = frobGenotype.DNA_EAST_PREFS + frobGenotype.DNA_FROB_OFFSET;
			return frobGenotype.genes[n];
		}
	}

	/**
	 * Method to determine a Frob's preferences to move south.
	 * 
	 * @return the value for the Frob's south preferences
	 */
	public int getSouthPrefs() {
		int n = 0;
		if (this.neighborhood.south == false) {
			n = frobGenotype.DNA_SOUTH_PREFS + frobGenotype.DNA_EMPTY_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.s == 'R') {
			n = frobGenotype.DNA_SOUTH_PREFS + frobGenotype.DNA_ROCK_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.s == 'G') {
			n = frobGenotype.DNA_SOUTH_PREFS + frobGenotype.DNA_GRASS_OFFSET;
			return frobGenotype.genes[n];
		} else {
			n = frobGenotype.DNA_SOUTH_PREFS + frobGenotype.DNA_FROB_OFFSET;
			return frobGenotype.genes[n];
		}
	}

	/**
	 * Method to determine a Frob's preferences to move west.
	 * 
	 * @return the value for the Frob's west preferences
	 */
	public int getWestPrefs() {
		int n = 0;
		if (this.neighborhood.west == false) {
			n = frobGenotype.DNA_WEST_PREFS + frobGenotype.DNA_EMPTY_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.w == 'R') {
			n = frobGenotype.DNA_WEST_PREFS + frobGenotype.DNA_ROCK_OFFSET;
			return frobGenotype.genes[n];
		} else if (this.neighborhood.w == 'G') {
			n = frobGenotype.DNA_WEST_PREFS + frobGenotype.DNA_GRASS_OFFSET;
			return frobGenotype.genes[n];
		} else {
			n = frobGenotype.DNA_WEST_PREFS + frobGenotype.DNA_FROB_OFFSET;
			return frobGenotype.genes[n];
		}
	}

	/**
	 * Method to create possible mutations in a child's Genotype so that it may
	 * "evolve".
	 * 
	 * @return the child's newly mutated set of genes
	 */
	public Genotype geneMutation() {
		Genotype childGenes = new Genotype();
		for (int i = 0; i < frobGenotype.DNA_LENGTH; i++) {
			int m = rand.nextInt(mutationOdds);
			if (m == 0) {
				int randBitFlip = rand.nextInt(Integer.MAX_VALUE) % 8;
				childGenes.genes[i] = (frobGenotype.genes[i] ^ (1 << randBitFlip));
			} else
				childGenes.genes[i] = frobGenotype.genes[i];
		}
		return childGenes;
	}

	@Override
	public boolean checkForLife() {
		return isAlive;
	}
}
