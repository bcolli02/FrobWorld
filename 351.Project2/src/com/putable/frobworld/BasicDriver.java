package com.putable.frobworld;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * A driver class for the Frob World simulation. Based on command line
 * arguments, it will either produce a GUI version of Frob World or it will
 * output important stats to standard output.
 * 
 * @author Brennan Collins
 * 
 */
public final class BasicDriver {
	// field to determine which run the drive is currently on
	private int runPoint = 0;

	/**
	 * Method to perform a "run count" simulation. This takes an integer greater
	 * than 0 and runs the Frob World simulation this many times with randomly
	 * generated seeds.
	 * 
	 * @param count
	 *            the number of runs to perform.
	 */
	public void runCount(int count) {
		Random rand = new Random();
		for (int i = 1; i < count + 1; i++) {
			int seedVal = rand.nextInt(Integer.MAX_VALUE - 1);
			System.out
					.println("__________________________________________________________________________\n");
			System.out
					.println("Run #" + i + ", Seed Value = " + seedVal + "\n");
			World world = new World(seedVal);
			world.gatherResults();
			world = null;
		}
	}

	/**
	 * Method to perform a "run these" simulation. This takes a seed value to
	 * plug into the Frob World Simulation and runs it on that seed.
	 * 
	 * @param input
	 *            the seed value to run the simulation on
	 */
	public void runThese(int input) {
		++runPoint;
		System.out
				.println("__________________________________________________________________________\n");
		System.out.println("Run #" + runPoint + ", Seed Value = " + input
				+ "\n");
		World world = new World(input);
		world.gatherResults();
		world = null;
	}

	public static void main(String[] args) throws Throwable {
		// running Frob World in GUI mode
		if (args.length == 0) {
			// produce a random generator and a random seed value to run the
			// simulation on
			Random rand = new Random();
			int seed = Math.abs(rand.nextInt());
			// creates a frame and new World to draw our simulation in
			JFrame mainFrame = new JFrame(" F R O B   W O R L D ");
			World worldPanel = new World(seed);
			// set screen dimensions
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getScreenSize();
			worldPanel.scale = d.width / 100;
			worldPanel.setPreferredSize(new Dimension(worldPanel.WORLD_WIDTH
					* worldPanel.scale + worldPanel.scale,
					worldPanel.WORLD_HEIGHT * worldPanel.scale
							+ worldPanel.scale));

			// add the JPanel to the pane
			mainFrame.getContentPane().add(worldPanel, BorderLayout.CENTER);
			// clean up
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.pack();
			mainFrame.setResizable(false);
			mainFrame.setVisible(true);
		}
		// running Frob World in batch mode
		else if (args[0].equals("batch")) {
			BasicDriver bd = new BasicDriver();
			Scanner in = new Scanner(System.in);
			int v = 0;
			// perform a run these simulation
			if ((v = in.nextInt()) == 0) {
				v = in.nextInt();
				while (v != 0) {
					bd.runThese(v);
					v = in.nextInt();
				}
			}
			// perform a run count simulation
			else
				bd.runCount(v);
			in.close();
		} else
			throw new IllegalArgumentException();
	}
}
