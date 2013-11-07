package com.putable.frobworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.putable.pqueue.PQueue;
import com.putable.pqueue.PQueueAdvanced;

/**
 * The class World is our container for "{@link #Thing}" and "{@link #Being}"
 * objects. It is also our drawing object for Frob World. It contains all final
 * fields that describe aspects of our "World" and methods to produce our Frob
 * World implementation.
 * 
 * @author Brennan Collins
 * 
 */
public final class World extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	// final fields of Frob World
	public final int WORLD_WIDTH = 100, // World Width
			WORLD_HEIGHT = 50, // World Height
			MAX_SIMULATION_LENGTH = 25000, // Time to quit even if frobs still
											// live

			ROCK_BUMP_PENALTY = 30, // Mass penalty when Frob hits Rock
			FROB_HIT_PENALTY = 10, // Mass penalty (of hittee) when Frob hits
									// Frob

			INIT_FROBS = 50, // Number of Frobs in initial world
			INIT_GRASSES = 250, // Number of Grasses in initial world

			GRASS_FIXED_OVERHEAD = 0, // Grass fixed mass cost per action
			GRASS_GENESIS_MASS = 10, // Initial Grass mass
			GRASS_BIRTH_MASS = 30, // Mass at which Grasses wish to split
			GRASS_INITIAL_UPDATE_PERIOD = 10, // Days between Grass activities
			GRASS_CROWD_LIMIT = 2, // 4-neighborhood Grass count for no
									// splitting
			GRASS_MAX_UPDATE_PERIOD = 100, // Max inactive days on failed
											// splitting
			GRASS_BIRTH_PERCENT = 40, // Percent of mass given to offspring at
										// split

			FROB_FIXED_OVERHEAD = 2, // Frob fixed mass cost per action
			FROB_GENESIS_MASS = 100, // Initial Frob mass

			DNA_MUTATION_ODDS_PER_BYTE = 20, // 1-in-this chance of a bit flip
												// per byte

			GRASS_MASS_TAX_MILLS = -200, // Grass mass loss per day
			FROB_MASS_TAX_MILLS = 100; // Frob mass loss per day

	// Timer for updating screen
	private Timer timer;
	// the random number generator for output
	private Random rand;
	// a two-dimensional grid for determining what "Thing" lies where
	private Thing[][] grid;
	// our Priority Queue of grassBeings to determine who makes the next move
	private PQueue grassBeings;
	// our Priority Queue of grassBeings to determine who makes the next move
	private PQueue frobBeings;
	// fields for our current day, total frobs, and total grass
	private int day = 0;
	// boolean value for whether our frobs are alive
	private boolean extinct = false;
	// our seed value for PRNG and scale for sizing up the JFrame object that we
	// paint Frob World to
	public int inSeed, scale;
	// values to track important output
	private int frobCount = 0, totalFrobHops = 0, totalGrassConsumption = 0,
			taxDeathCount = 0, rockDeathCount = 0, frobDeathCount = 0,
			deepestGeneration = 0;

	/**
	 * Constructor for our World object. On instantiation it starts the timer
	 * and then sets all the initial rocks, grass, and frobs. The seed value is
	 * used to produce a certain behavior given it takes the same value.
	 * 
	 * @param seed
	 *            our seed value
	 */
	public World(int seed) {
		inSeed = seed;
		timer = new Timer(0, this);
		timer.start();
		initiateWorld();
	}

	/**
	 * A method to properly initiate all edge and interior locations for all "
	 * {@link Thing}" objects, our grid, and both the {@link Frob}
	 * {@link PQueue} and {@link Grass} {@link PQueue}.
	 */
	public void initiateWorld() {
		day = 0;
		grid = new Thing[WORLD_WIDTH + 1][WORLD_HEIGHT + 1];
		grassBeings = new PQueueAdvanced();
		frobBeings = new PQueueAdvanced();
		rand = new Random(inSeed);
		setRocks();
		setGrass();
		setFrobs();
	}

	/**
	 * Method to set all the {@link Rock} objects at edge and interior locations
	 * in our Frob World.
	 */
	public void setRocks() {
		for (int x = 0; x <= WORLD_WIDTH; x++) {
			Point p = findRandomOpenLocation();
			Thing rock1 = new Rock();
			rock1.setLocation(new Point(x, 0));
			Thing rock2 = new Rock();
			rock2.setLocation(new Point(x, WORLD_HEIGHT));
			Thing rock3 = new Rock();
			rock3.setLocation(p);
			grid[x][0] = rock1;
			grid[x][WORLD_HEIGHT] = rock2;
			grid[p.x][p.y] = rock3;
		}
		for (int y = 0; y <= WORLD_HEIGHT; y++) {
			Thing rock1 = new Rock();
			rock1.setLocation(new Point(0, y));
			Thing rock2 = new Rock();
			rock2.setLocation(new Point(WORLD_WIDTH, y));
			grid[0][y] = rock1;
			grid[WORLD_WIDTH][y] = rock2;
		}
	}

	/**
	 * A method to set all the initial {@link Grass} objects in interior
	 * locations of our Frob World simulation.
	 */
	public void setGrass() {
		for (int i = 0; i < INIT_GRASSES; i++) {
			Point p = findRandomOpenLocation();
			Being grass = new Grass(GRASS_GENESIS_MASS, GRASS_BIRTH_MASS,
					GRASS_BIRTH_PERCENT, GRASS_MASS_TAX_MILLS,
					GRASS_FIXED_OVERHEAD, GRASS_INITIAL_UPDATE_PERIOD);
			grass.setLocation(p);
			grass.rand = rand;
			grass.setUpdatePeriod(GRASS_INITIAL_UPDATE_PERIOD);
			grass.setInitialNextUpdate(0);
			grid[p.x][p.y] = grass;
			grassBeings.insert(grass);
		}
	}

	/**
	 * A method to set all the initial {@link Frob} objects in interior
	 * locations of our Frob World simulation.
	 */
	public void setFrobs() {
		for (int i = 0; i < INIT_FROBS; i++) {
			Point p = findRandomOpenLocation();
			Being frob = new Frob(rand, FROB_GENESIS_MASS, FROB_MASS_TAX_MILLS,
					FROB_FIXED_OVERHEAD, DNA_MUTATION_ODDS_PER_BYTE);
			frob.setLocation(p);
			frob.setInitialNextUpdate(0);
			((Frob) frob).generation = 0;
			grid[p.x][p.y] = frob;
			frobBeings.insert(frob);
			frobCount++;
		}
	}

	/**
	 * When a {@link Being} is removed from the top of the queue, it is sent
	 * here to perform its actions for the day. If paying the tax doesn't kill
	 * the being, it sends it to a {@link Being}'s second method for
	 * moving/eating/reproducing.
	 * 
	 * @param life
	 *            our {@link Being} that will be performing different actions
	 */
	public void doAction(Being life) {
		life.performAction();
		if (life instanceof Frob && life.mass <= 0) {
			taxDeathCount++;
			if (((Frob) life).generation > deepestGeneration)
				deepestGeneration = ((Frob) life).generation;
		}

		if (life.checkForLife()) {
			if (life instanceof Grass) {
				grassAction(life);
				life.reschedule();
				grassBeings.insert(life);
			} else if (life instanceof Frob) {
				frobAction(life);
				life.reschedule();
				frobBeings.insert(life);
			}
		} else {
			grid[life.getLocation().x][life.getLocation().y] = null;
		}
	}

	/**
	 * If a grass is alive, it comes here to try and reproduce. If it fulfills
	 * spec requirements for (S.3.3.3) then it reproduces, otherwise it doubles
	 * its metabolic rate and no child is born.
	 * 
	 * @param life
	 *            our {@link Being} that is attempting to reproduce
	 */
	public void grassAction(Being life) {
		int check = grassCheckNeighborhood(life);
		if (life.getMass() > life.birthMass && check < GRASS_CROWD_LIMIT+2) {
			Point p = setGrassChildLocation(life);
			if (p == null)
				return;
			Being child = life.reproduce();
			child.rand = rand;
			child.setUpdatePeriod(GRASS_INITIAL_UPDATE_PERIOD);
			child.setInitialNextUpdate(day);
			child.setLocation(p);
			grid[p.x][p.y] = child;
			grassBeings.insert(child);
		} else if (check >= GRASS_CROWD_LIMIT) {
			if (2 * life.getUpdatePeriod() < GRASS_MAX_UPDATE_PERIOD)
				life.setUpdatePeriod(2 * life.getUpdatePeriod());
			life.setMass(life.birthMass);
		}
	}

	/**
	 * If the Frob is alive after paying its tax, it comes here. In this method
	 * the Frob checks its surroundings ({@link #frobCheckNeighborhood(Being)})
	 * and then decides which direction to move ({@link #frobMove(Being)}). If
	 * the Frob is greater than its birth mass then it reproduces.
	 * 
	 * @param life
	 *            the {@link Frob} performing the action
	 */
	public void frobAction(Being life) {
		frobCheckNeighborhood(life);
		Point oldLoc = life.getLocation();
		frobMove(life);
		if (life.getMass() >= life.birthMass && life.getLocation() != oldLoc) {
			Being child = life.reproduce();
			child.setLocation(oldLoc);
			grid[oldLoc.x][oldLoc.y] = child;
			child.setInitialNextUpdate(day);
			((Frob) child).generation = ((Frob) life).generation + 1;
			frobBeings.insert(child);
			frobCount++;
		}
	}

	/**
	 * Method for Frob movement. After the Frob checks its surroundings (
	 * {@link CentralFrobcessingUnit#checkSurroundings(Being)}), it decides
	 * which direction to move {@link CentralFrobcessingUnit#iHop(Being)}).
	 * 
	 * @param life
	 *            the {@link Frob} that is going to move
	 */
	public void frobMove(Being life) {
		char c = ((Frob) life).move();
		if (c == 'e')
			CentralFrobUnitEast.iHop(life);
		else if (c == 'w')
			CentralFrobUnitWest.iHop(life);
		else if (c == 'n')
			CentralFrobUnitNorth.iHop(life);
		else
			CentralFrobUnitSouth.iHop(life);
	}

	/**
	 * A method for {@link Grass} to check its surroundings
	 * {@link GrassNeighborhoodWatch#checkSurroundings(Being)}. If a
	 * neighboring location of the grass is not null then the grass determines
	 * that there is some "{@link Thing}" in the location. It then checks if
	 * this location is a Grass object. If it is, then it increments the value
	 * of the area (the number of neighbors the grass has) by 1. It then returns
	 * this value after all neighborhood locations are checked.
	 * 
	 * @param life
	 *            our {@link Grass} object
	 * @return an integer value > 0
	 */
	public int grassCheckNeighborhood(Being life) {
		int area = 0;
		area += WatchmenNorth.checkSurroundings(life);
		area += WatchmenEast.checkSurroundings(life);
		area += WatchmenSouth.checkSurroundings(life);
		area += WatchmenWest.checkSurroundings(life);
		return area;
	}

	/**
	 * This is a method for {@link Frob} objects to check its surroundings
	 * {@link CentralFrobcessingUnit#checkSurroundings(Being)}. It checks each
	 * of its neighboring locations and sees if the location contains a
	 * {@link #Rock}, a {@link #Grass}, a Frob, or nothing. It then gives the
	 * ability to determine what happens when it moves in a given direction.
	 * 
	 * @param life
	 *            our Frob to be searching its neighborhood
	 */
	public void frobCheckNeighborhood(Being life) {
		// the location of our frob
		int xLoc = life.getLocation().x;
		int yLoc = life.getLocation().y;
		// check east
		if (grid[xLoc + 1][yLoc] != null) {
			CentralFrobUnitEast.checkSurroundings(life);
		} else
			life.neighborhood.e = 0;
		// check west
		if (grid[xLoc - 1][yLoc] != null) {
			CentralFrobUnitWest.checkSurroundings(life);
		} else
			life.neighborhood.w = 0;
		// check south
		if (grid[xLoc][yLoc + 1] != null) {
			CentralFrobUnitSouth.checkSurroundings(life);
		} else
			life.neighborhood.s = 0;
		// check north
		if (grid[xLoc][yLoc - 1] != null) {
			CentralFrobUnitNorth.checkSurroundings(life);
		} else
			life.neighborhood.n = 0;
	}

	/**
	 * A method to set where a {@link #Grass} object reproduces. It picks a
	 * random direction and tries to reproduce there. If the location in this
	 * direction is null, it puts a child there. Else if the neighborhood is
	 * full, then it returns null. If neither of these apply, then it repeatedly
	 * searches for another neighboring location that is null to put a child in.
	 * 
	 * @param life
	 *            our grass that is reproducing
	 * @return a point that is safe to reproduce in
	 */
	public Point setGrassChildLocation(Being life) {
		int r = rand.nextInt(Integer.MAX_VALUE - 1) % 4;
		// is north empty? put child to north
		if (r == 0 && !life.neighborhood.north)
			return new Point(life.getLocation().x, life.getLocation().y - 1);
		// is east empty? put child to east
		else if (r == 1 && !life.neighborhood.east)
			return new Point(life.getLocation().x + 1, life.getLocation().y);
		// is south empty? put child to south
		else if (r == 2 && !life.neighborhood.south)
			return new Point(life.getLocation().x, life.getLocation().y + 1);
		// is west empty? put child to west
		else if (r == 3 && !life.neighborhood.west)
			return new Point(life.getLocation().x - 1, life.getLocation().y);
		// is neighborhood full? return null indicating theres no room for a
		// baby
		else if (life.neighborhood.north && life.neighborhood.east
				&& life.neighborhood.south && life.neighborhood.west)
			return null;
		else
			return setGrassChildLocation(life);
	}

	/**
	 * Method to find a random open location anywhere in our Frob World.
	 * 
	 * @return a random open location
	 */
	public Point findRandomOpenLocation() {
		int xR = Math.abs(rand.nextInt(Integer.MAX_VALUE)) % WORLD_WIDTH;
		int yR = Math.abs(rand.nextInt(Integer.MAX_VALUE)) % WORLD_HEIGHT;
		if (grid[xR][yR] == null)
			return new Point(xR, yR);
		else
			return findRandomOpenLocation();
	}

	/**
	 * Method to determine what day we are currently on.
	 * 
	 * @return our current day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Method for getting the dimensions of our Frob World simulation.
	 * 
	 * @return a dimension for width and height of Frob World
	 */
	public Dimension getWorldDimensions() {
		return new Dimension(WORLD_WIDTH, WORLD_HEIGHT);
	}

	/**
	 * Method for updating and repainting our Frob World.
	 * 
	 * @param g
	 *            graphics object
	 */
	public void repaintWorld(Graphics g) {
		Color color = new Color(245, 222, 179);
		g.setColor(color);
		g.fillRect(0, 0, (WORLD_WIDTH + 1) * scale, (WORLD_HEIGHT + 1) * scale);
		for (int y = 0; y < WORLD_HEIGHT + 1; y++) {
			for (int x = 0; x < WORLD_WIDTH + 1; x++) {
				if (grid[x][y] instanceof Grass) {
					g.setColor(new Color(34, 139, 34));
					g.fillOval((x * scale) + (scale / 8), (y * scale)
							+ (scale / 8), (int) (scale * 0.75),
							(int) (scale * 0.75));
				} else if (grid[x][y] instanceof Rock) {
					g.setColor(new Color(139, 69, 19));
					g.fill3DRect((x * scale) + (scale / 8) + 1, (y * scale)
							+ (scale / 8) + 1, (int) (scale * 0.75),
							(int) (scale * 0.75), true);
				} else if (grid[x][y] instanceof Frob) {
					int frobMass = Math.abs(((Being) grid[x][y]).mass);
					int frobJect = grid[x][y].hashCode() % 64;
					g.setColor(new Color(172 - frobJect, 60,
							(255 - frobMass) / 2 + 80));
					int xVal = x * scale + scale / 2;
					int yVal = y * scale + scale / 2;
					int[] xAr = { xVal, xVal - 2, xVal - 4, xVal - 5, xVal - 5,
							xVal - 4, xVal - 3, xVal - 1, xVal + 1, xVal + 3,
							xVal + 4, xVal + 5, xVal + 5, xVal + 4, xVal + 2,
							xVal };
					int[] yAr = { yVal + 3, yVal + 10, yVal + 3, yVal + 4,
							yVal, yVal - 8, yVal - 1, yVal - 4, yVal - 4,
							yVal - 1, yVal - 8, yVal, yVal + 4, yVal + 3,
							yVal + 10, yVal + 3 };
					g.fillPolygon(xAr, yAr, xAr.length);
					g.setColor(new Color(255 - frobMass, 255 - frobJect, 32));
					g.fillOval((x * scale) + (scale / 3), (y * scale)
							+ (scale / 3), (int) (scale * 0.35),
							(int) (scale * 0.35));
				}
			}
		}
	}

	/**
	 * A function to run our Frob World simulation and then print important
	 * stats to standard output.
	 */
	public void gatherResults() {
		initiateWorld();
		while (day < 25000) {
			if (day == MAX_SIMULATION_LENGTH) {
				break;
			} else if (extinct) {
				break;
			} else {
				runSimulation();
			}
		}
		timer.stop();
		printResults();
	}

	/**
	 * Prints important figures to output such as the number of days the Frobs
	 * lasted, average distance travelled by the Frobs, how many generations of
	 * Frobs have been produced, average grass consumption, and largest factor
	 * of death. If the Frobs made it all the way to the end of the simulation
	 * then it also outputs the metabolic rates of the surviving Frobs.
	 */
	public void printResults() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		if (extinct) {
			System.out.println("The Frobs are no more after " + day
					+ " days.\n\n");

			double avgDistance = (double) totalFrobHops / frobCount;
			System.out.println("Average distance travelled: \n--- "
					+ nf.format(avgDistance) + " hops ---\n");

			System.out.println("Generations past:\n--- " + deepestGeneration
					+ " ---\n");

			double avgGrassConsumption = (double) totalGrassConsumption
					/ frobCount;
			System.out
					.println("Average amount of grass consumption during Simulation:\n--- "
							+ nf.format(avgGrassConsumption) + " ---\n");

			String d1 = (taxDeathCount > rockDeathCount) ? "Paying the tax."
					: "Hitting the rock.";
			if (d1 == "Paying the tax." && taxDeathCount > frobDeathCount)
				System.out
						.println("Main cause of death:\n--- " + d1 + " ---\n");
			else if (d1 == "Hitting the rock."
					&& rockDeathCount > frobDeathCount)
				System.out
						.println("Main cause of death:\n--- " + d1 + " ---\n");
			else
				System.out
						.println("Main cause of death:\n--- Death by Frob! ---\n");
		} else {
			System.out.println("Long live the Almighty Frob!\n\n");
			int size = frobBeings.size();
			double frobPercentageAlive = (double) size / frobCount * 100;
			System.out
					.println("Percentage of Frobs left compared to total Frobs to ever exist:\n--- Frobs still alive = "
							+ size
							+ ", Percentage of all Frobs still alive = "
							+ nf.format(frobPercentageAlive) + "% ---\n");

			int[] metaRates = metabolicRates();
			System.out
					.println("Average metabolic rate and standard deviation of survivors:\n--- Average = "
							+ metaRates[0]
							+ ", Standard Deviation = "
							+ metaRates[1] + " ---\n");

			double avgDistance = (double) totalFrobHops / frobCount;
			System.out.println("Average distance travelled: \n--- "
					+ nf.format(avgDistance) + " hops ---\n");

			System.out.println("Generations past:\n--- " + deepestGeneration
					+ " ---\n");

			double avgGrassConsumption = (double) totalGrassConsumption
					/ frobCount;
			System.out
					.println("Average amount of grass consumption during Simulation:\n--- "
							+ nf.format(avgGrassConsumption) + " ---\n");

			String d1 = (taxDeathCount > rockDeathCount) ? "Paying the tax."
					: "Hitting the rock.";
			if (d1 == "Paying the tax." && taxDeathCount > frobDeathCount)
				System.out
						.println("Main cause of death:\n--- " + d1 + " ---\n");
			else if (d1 == "Hitting the rock."
					&& rockDeathCount > frobDeathCount)
				System.out
						.println("Main cause of death:\n--- " + d1 + " ---\n");
			else
				System.out
						.println("Main cause of death:\n--- Death by Frob! ---\n");
		}
	}

	/**
	 * A method to run the Frob World simulation at the current day and adjust
	 * our PQueue's and grid array accordingly, along with other global
	 * variables we need to take into consideration when outputting statistics
	 * of a given simulation.
	 */
	public void runSimulation() {
		boolean dayComplete = false;
		while (!dayComplete) {
			if (frobBeings.size() == 0) {
				extinct = true;
				return;
			}
			if (((Being) grassBeings.top()).getNextUpdate() == day) {
				Being life = (Being) grassBeings.remove();
				if (life != null && life.getMass() > 0) {
					doAction(life);
				} else
					grid[life.getLocation().x][life.getLocation().y] = null;
			}
			if (((Being) frobBeings.top()).getNextUpdate() == day) {
				Being life = (Being) frobBeings.remove();
				if (life != null && life.getMass() > 0) {
					doAction(life);
				} else
					grid[life.getLocation().x][life.getLocation().y] = null;
			}
			if (((Being) grassBeings.top()).getNextUpdate() != day
					&& ((Being) frobBeings.top()).getNextUpdate() != day) {
				day++;
				dayComplete = true;
			}
		}
	}

	/**
	 * Method to determine the average metabolic rate of surviving Frobs and
	 * also the standard deviant.
	 * 
	 * @return an array of length two containing the average Frob metabolic rate
	 *         and standard deviant
	 */
	public int[] metabolicRates() {
		int avg = 0, stdDev = ((Being) frobBeings.top()).getUpdatePeriod(), fCount = frobBeings
				.size();
		while (frobBeings.top() != null) {
			Being b = (Being) frobBeings.remove();
			int val = b.getUpdatePeriod();
			if (Math.abs(avg - val) > Math.abs(avg - stdDev))
				stdDev = val;
			avg += val;
		}
		return new int[] { avg / fCount, stdDev };
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (day == MAX_SIMULATION_LENGTH) {
			System.out.println("Long live the Almighty Frob!");
			initiateWorld();
		} else if (extinct) {
			System.out.println("The Frobs are no more after " + day + " days.");
			extinct = false;
			initiateWorld();
		} else {
			runSimulation();
			repaintWorld(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	/**
	 * An interface used by 4 anonymous classes: CentralFrobUnitNorth,
	 * CentralFrobUnitEast, CentralFrobUnitSouth, and CentralFrobUnitWest. All
	 * of these class must implement the methods {@link #iHop(Being)} and
	 * {@link #checkSurroundings(Being)} in order for a {@link #Frob} to
	 * determine its neighboring locations and make a decision in which
	 * direction to move.
	 * 
	 * @author Brennan Collins
	 * 
	 */
	public interface CentralFrobcessingUnit {
		/**
		 * Method for a {@link #Frob} object to determine what happens when it
		 * moves to its desired location.
		 * 
		 * @param life
		 *            the Frob that is hopping
		 */
		public abstract void iHop(Being life);

		/**
		 * Method for a {@link #Frob} object to determine what lies in its
		 * neighboring locations.
		 * 
		 * @param life
		 *            the Frob that is checking its neighborhood
		 */
		public abstract void checkSurroundings(Being life);
	}

	// our anonymous class for making decisions for north frob movement
	private CentralFrobcessingUnit CentralFrobUnitNorth = new CentralFrobcessingUnit() {
		@Override
		public void iHop(Being life) {
			int x = life.getLocation().x;
			int y = life.getLocation().y;

			if (life.neighborhood.n == 0) {
				life.setLocation(new Point(x, y - 1));
				grid[x][y] = null;
				grid[x][y - 1] = life;
				totalFrobHops++;
			} else if (life.neighborhood.n == 'R') {
				int curMass = life.getMass();
				life.setMass(curMass - ROCK_BUMP_PENALTY);
				if (life.mass < 0) {
					rockDeathCount++;
					if (((Frob) life).generation > deepestGeneration)
						deepestGeneration = ((Frob) life).generation;
				}
			} else if (life.neighborhood.n == 'G') {
				((Frob) life).consume((Grass) grid[x][y - 1]);
				if (life.mass > life.birthMass)
					life.mass = life.birthMass;
				grassBeings.delete((Grass) grid[x][y - 1]);
				life.setLocation(new Point(x, y - 1));
				grid[x][y] = null;
				grid[x][y - 1] = life;
				totalFrobHops++;
				totalGrassConsumption++;
			} else {
				((Being) grid[x][y - 1]).mass -= FROB_HIT_PENALTY;
				if (((Being) grid[x][y - 1]).mass < 0) {
					frobDeathCount++;
					if (((Frob) grid[x][y - 1]).generation > deepestGeneration)
						deepestGeneration = ((Frob) grid[x][y - 1]).generation;
				}
			}

		}

		@Override
		public void checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;
			life.neighborhood.north = true;

			if (grid[xLoc][yLoc - 1] instanceof Grass) {
				life.neighborhood.n = 'G';
			} else if (grid[xLoc][yLoc - 1] instanceof Rock) {
				life.neighborhood.n = 'R';
			} else {
				life.neighborhood.n = 'F';
			}
		}

	};

	// our anonymous class for making decisions for east frob movement
	private CentralFrobcessingUnit CentralFrobUnitEast = new CentralFrobcessingUnit() {
		@Override
		public void iHop(Being life) {
			int x = life.getLocation().x;
			int y = life.getLocation().y;

			if (life.neighborhood.e == 0) {
				life.setLocation(new Point(x + 1, y));
				grid[x][y] = null;
				grid[x + 1][y] = life;
				totalFrobHops++;
			} else if (life.neighborhood.e == 'R') {
				int curMass = life.getMass();
				life.setMass(curMass - ROCK_BUMP_PENALTY);
				if (life.mass < 0) {
					rockDeathCount++;
					if (((Frob) life).generation > deepestGeneration)
						deepestGeneration = ((Frob) life).generation;
				}
			} else if (life.neighborhood.e == 'G') {
				((Frob) life).consume((Grass) grid[x + 1][y]);
				if (life.mass > life.birthMass)
					life.mass = life.birthMass;
				grassBeings.delete((Grass) grid[x + 1][y]);
				life.setLocation(new Point(x + 1, y));
				grid[x][y] = null;
				grid[x + 1][y] = life;
				totalFrobHops++;
				totalGrassConsumption++;
			} else {
				((Being) grid[x + 1][y]).mass -= FROB_HIT_PENALTY;
				if (((Being) grid[x + 1][y]).mass < 0) {
					frobDeathCount++;
					if (((Frob) grid[x + 1][y]).generation > deepestGeneration)
						deepestGeneration = ((Frob) grid[x + 1][y]).generation;
				}
			}

		}

		@Override
		public void checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;
			life.neighborhood.east = true;

			if (grid[xLoc + 1][yLoc] instanceof Grass) {
				life.neighborhood.e = 'G';
			} else if (grid[xLoc + 1][yLoc] instanceof Rock) {
				life.neighborhood.e = 'R';
			} else {
				life.neighborhood.e = 'F';
			}
		}
	};

	// our anonymous class for making decisions for south frob movement
	private CentralFrobcessingUnit CentralFrobUnitSouth = new CentralFrobcessingUnit() {
		@Override
		public void iHop(Being life) {
			int x = life.getLocation().x;
			int y = life.getLocation().y;

			if (life.neighborhood.s == 0) {
				life.setLocation(new Point(x, y + 1));
				grid[x][y] = null;
				grid[x][y + 1] = life;
				totalFrobHops++;
			} else if (life.neighborhood.s == 'R') {
				int curMass = life.getMass();
				life.setMass(curMass - ROCK_BUMP_PENALTY);
				if (life.mass < 0) {
					rockDeathCount++;
					if (((Frob) life).generation > deepestGeneration)
						deepestGeneration = ((Frob) life).generation;
				}
			} else if (life.neighborhood.s == 'G') {
				((Frob) life).consume((Grass) grid[x][y + 1]);
				if (life.mass > life.birthMass)
					life.mass = life.birthMass;
				grassBeings.delete((Grass) grid[x][y + 1]);
				life.setLocation(new Point(x, y + 1));
				grid[x][y] = null;
				grid[x][y + 1] = life;
				totalFrobHops++;
				totalGrassConsumption++;
			} else {
				((Being) grid[x][y + 1]).mass -= FROB_HIT_PENALTY;
				if (((Being) grid[x][y + 1]).mass < 0) {
					frobDeathCount++;
					if (((Frob) grid[x][y + 1]).generation > deepestGeneration)
						deepestGeneration = ((Frob) grid[x][y + 1]).generation;
				}
			}
		}

		@Override
		public void checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;
			life.neighborhood.south = true;

			if (grid[xLoc][yLoc + 1] instanceof Grass) {
				life.neighborhood.s = 'G';
			} else if (grid[xLoc][yLoc + 1] instanceof Rock) {
				life.neighborhood.s = 'R';
			} else {
				life.neighborhood.s = 'F';
			}
		}
	};

	// our anonymous class for making decisions for west frob movement
	private CentralFrobcessingUnit CentralFrobUnitWest = new CentralFrobcessingUnit() {
		@Override
		public void iHop(Being life) {
			int x = life.getLocation().x;
			int y = life.getLocation().y;

			if (life.neighborhood.w == 0) {
				life.setLocation(new Point(x - 1, y));
				grid[x][y] = null;
				grid[x - 1][y] = life;
				totalFrobHops++;
			} else if (life.neighborhood.w == 'R') {
				int curMass = life.getMass();
				life.setMass(curMass - ROCK_BUMP_PENALTY);
				if (life.mass < 0) {
					rockDeathCount++;
					if (((Frob) life).generation > deepestGeneration)
						deepestGeneration = ((Frob) life).generation;
				}
			} else if (life.neighborhood.w == 'G') {
				((Frob) life).consume((Grass) grid[x - 1][y]);
				grassBeings.delete((Grass) grid[x - 1][y]);
				if (life.mass > life.birthMass)
					life.mass = life.birthMass;
				life.setLocation(new Point(x - 1, y));
				grid[x][y] = null;
				grid[x - 1][y] = life;
				totalFrobHops++;
				totalGrassConsumption++;
			} else {
				((Being) grid[x - 1][y]).mass -= FROB_HIT_PENALTY;
				if (((Being) grid[x - 1][y]).mass < 0) {
					frobDeathCount++;
					if (((Frob) grid[x - 1][y]).generation > deepestGeneration)
						deepestGeneration = ((Frob) grid[x - 1][y]).generation;
				}
			}
		}

		@Override
		public void checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;
			life.neighborhood.west = true;

			if (grid[xLoc - 1][yLoc] instanceof Grass) {
				life.neighborhood.w = 'G';
			} else if (grid[xLoc - 1][yLoc] instanceof Rock) {
				life.neighborhood.w = 'R';
			} else {
				life.neighborhood.w = 'F';
			}
		}
	};

	/**
	 * An interface used by 4 anonymous classes: WatchmenNorth, WatchmenEast,
	 * WatchmenSouth, and WatchmenWest. These classes all implement the method
	 * {@link #checkSurroundings(Being)} to figure out what lies in each of the
	 * neighboring {@link #Grass} locations and also determine if the
	 * {@link World#GRASS_CROWD_LIMIT} is exceeded.
	 * 
	 * @author Brennan Collins
	 * 
	 */
	public interface GrassNeighborhoodWatch {
		/**
		 * Method to check a {@link #Grass} object's neighboring locations.
		 * Determines what lies in the location and returns 1 if it is another
		 * grass object and a 0 if it is not.
		 * 
		 * @param life
		 *            the grass object that is checking its surroundings
		 * @return an int: 0 for a location that does not a contain grass, 1 if
		 *         it does contain a grass
		 */
		public int checkSurroundings(Being life);
	}

	// our anonymous class for deciding what lies north of a grass object
	private GrassNeighborhoodWatch WatchmenNorth = new GrassNeighborhoodWatch() {
		@Override
		public int checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;

			if (grid[xLoc][yLoc - 1] != null) {
				life.neighborhood.north = true;
				if (grid[xLoc][yLoc - 1] instanceof Grass) {
					life.neighborhood.n = 'G';
					return 1;
				}
			} else
				life.neighborhood.north = false;
			return 0;
		}
	};

	// our anonymous class for deciding what lies east of a grass object
	private GrassNeighborhoodWatch WatchmenEast = new GrassNeighborhoodWatch() {
		@Override
		public int checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;

			if (grid[xLoc + 1][yLoc] != null) {
				life.neighborhood.east = true;
				if (grid[xLoc + 1][yLoc] instanceof Grass) {
					life.neighborhood.e = 'G';
					return 1;
				}
			} else
				life.neighborhood.east = false;
			return 0;
		}
	};

	// our anonymous class for deciding what lies south of a grass object
	private GrassNeighborhoodWatch WatchmenSouth = new GrassNeighborhoodWatch() {
		@Override
		public int checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;

			if (grid[xLoc][yLoc + 1] != null) {
				life.neighborhood.south = true;
				if (grid[xLoc][yLoc + 1] instanceof Grass) {
					life.neighborhood.s = 'G';
					return 1;
				}
			} else
				life.neighborhood.south = false;
			return 0;
		}
	};

	// our anonymous class for deciding what lies west of a grass object
	private GrassNeighborhoodWatch WatchmenWest = new GrassNeighborhoodWatch() {
		@Override
		public int checkSurroundings(Being life) {
			int xLoc = life.getLocation().x;
			int yLoc = life.getLocation().y;

			if (grid[xLoc - 1][yLoc] != null) {
				life.neighborhood.west = true;
				if (grid[xLoc - 1][yLoc] instanceof Grass) {
					life.neighborhood.w = 'G';
					return 1;
				}

			} else
				life.neighborhood.west = false;
			return 0;
		}
	};
}