package com.putable.frobworld;

/**
 * The class Genotype determines properties of a {@link #Frob}.
 * 
 * @author Brennan Collins
 * 
 */
public final class Genotype {
	// locations of Frob values in their respective Genes array
	public final int DNA_BIRTH_MASS = 0, // dna[0] controls birthmass
			DNA_BIRTH_PERCENT = 1, // dna[1] controls birthpercent
			DNA_UPDATE_PERIOD = 2, // dna[2] controls updperiod
			DNA_NORTH_PREFS = 3, // dna[3..6] controls north prefs

			DNA_EMPTY_OFFSET = 0, // so,e.g.,
									// dna[DNA_NORTH_PREFS+DNA_ROCK_OFFSET]
			DNA_ROCK_OFFSET = 1, // is how much this frob likes to move
			DNA_GRASS_OFFSET = 2, // north when there's a rock there
			DNA_FROB_OFFSET = 3, // likewise for empty, grass, another frob

			DNA_SOUTH_PREFS = 7, // base index for south preferences
			DNA_EAST_PREFS = 11, // ditto, east
			DNA_WEST_PREFS = 15, // ditto, west
			DNA_LENGTH = 19; // OVERALL LENGTH OF DNA

	// an array of frobGenotype
	// storage?
	public int[] genes = new int[DNA_LENGTH];

	/**
	 * A setter method for the frobGenotype of our Frob.
	 * 
	 * @param genes
	 *            the frobGenotype we want to set for our Frob
	 */
	public void setGenes(int[] genes) {
		this.genes = genes;
	}
}