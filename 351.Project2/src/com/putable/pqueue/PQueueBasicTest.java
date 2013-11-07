package com.putable.pqueue;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

/**
 * Unit tests for the classes ConcretePQAble and PQueueAdvanced. Tests all
 * notable methods of each class.
 * 
 * @author Brennan Collins
 * 
 */
public class PQueueBasicTest {

	/**
	 * Setup method for a PQueue.
	 * 
	 * @param p
	 *            PQueue to be setup
	 * @param fill
	 *            size to fill the PQueue to
	 */
	public void PQueueSetup(PQueue p, int fill) {
		for (int i = 0; i < fill; i++) {
			ConcretePQAble in = new ConcretePQAble();
			in.setUpdatePeriod(100);
			in.setInitialNextUpdate(0);
			p.insert(in);
		}
	}

	/**
	 * Method to compare two PQAbles.
	 * 
	 * @param gt
	 *            PQAble assumed to have greater priority
	 * @param lt
	 *            PQAble assumed to have lesser priority
	 */
	public void compare(PQAble gt, PQAble lt) {
		assertTrue(gt.compareTo(lt) < 0);
	}

	/**
	 * Time tests for insert function.
	 * 
	 * @param size
	 *            size to fill the PQueue to
	 */
	public void insertTimer(int size) {
		PQueue p = new PQueueAdvanced();
		double time, newTime = 0;

		for (int i = 1; i < size+1; i++) {
			ConcretePQAble in = new ConcretePQAble();
			in.setUpdatePeriod(100);
			in.setInitialNextUpdate(0);
			double t1 = System.nanoTime();
			p.insert(in);
			newTime += (System.nanoTime() - t1) / 1000000000;
		}
		time = newTime / (size - 1);
		// if average of all times lies between 1x10^(-4) seconds & 1x10^(-8)
		// seconds we will assume time follows constant expectations
		assertTrue(time < 0.0001);
		assertTrue(time > 0.00000001);
	}

	/**
	 * Time tests for remove function.
	 * 
	 * @param size
	 *            size to fill the PQueue to
	 */
	public void removeTimer(int size) {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, size);

		double time, newTime = 0;

		for (int i = 0; i < size - 1; i++) {
			double t1 = System.nanoTime();
			p.remove();
			newTime += (System.nanoTime() - t1) / 1000000000;

		}
		time = newTime / (size - 1);
		// if average of all times lies between 1x10^(-4) seconds & 1x10^(-8)
		// seconds we will assume time follows constant expectations
		assertTrue(time < 0.0001);
		assertTrue(time > 0.00000001);
	}

	/**
	 * Time tests for delete function.
	 * 
	 * @param size
	 *            size to fill the PQueue to
	 */
	public void deleteTimer(int size) {
		Random rand = new Random();
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, size);
		PQAble[] peeks = p.getHeap();

		double time, newTime = 0;

		for (int i = 0; i < size - 1; i++) {
			int r = rand.nextInt(p.size() - 1) + 1;
			double t1 = System.nanoTime();
			p.delete(peeks[r]);
			newTime += (System.nanoTime() - t1) / 1000000000;
		}
		time = newTime / (size - 1);
		// if average of all times lies between 1x10^(-4) seconds & 1x10^(-8)
		// seconds we will assume time follows constant expectations
		assertTrue(time < 0.0001);
		assertTrue(time > 0.00000001);
	}

	@Test
	public void testInsert1() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testInsert2() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testInsert3() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testRemove1() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		PQAble un = peeks[1];
		PQAble di = p.remove();
		assertTrue(un.equals(di));
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testRemove2() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		PQAble un = peeks[1];
		PQAble di = p.remove();
		assertTrue(un.equals(di));
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testRemove3() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		PQAble un = peeks[1];
		PQAble di = p.remove();
		assertTrue(un.equals(di));
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testRemove4() {
		PQueue p = new PQueueAdvanced();
		assertTrue(p.remove() == null);
	}

	@Test
	public void testTop1() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		PQAble un = peeks[1];
		PQAble di = p.top();
		assertTrue(un.equals(di));
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testTop2() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		PQAble un = peeks[1];
		PQAble di = p.top();
		assertTrue(un.equals(di));
		assertTrue(p.isHeap(1));
	}

	@Test
	public void testTop3() {
		PQueueAdvanced p = new PQueueAdvanced();
		assertTrue(p.top() == null);
	}

	@Test
	public void testSize() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		assertTrue(p.size() == 15);
	}

	@Test
	public void TestDelete1() {
		Random rand = new Random();
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		for (int i = 0; i < 14; i++) {
			int randDex = p.size() - rand.nextInt(p.size());
			p.delete(peeks[randDex]);
			assertTrue(p.isHeap(1));
		}
	}

	@Test
	public void TestDelete2() {
		Random rand = new Random();
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		for (int i = 0; i < 4; i++) {
			int randDex = p.size() - rand.nextInt(p.size());
			p.delete(peeks[randDex]);
			assertTrue(p.isHeap(1));
		}
	}

	@Test
	public void TestDelete3() {
		Random rand = new Random();
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		PQAble[] peeks = p.getHeap();
		for (int i = 0; i < 4; i++) {
			int randDex = p.size() - rand.nextInt(p.size());
			p.delete(peeks[randDex]);
			assertTrue(p.isHeap(1));
		}
	}

	@Test
	public void showToString() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 63);
		System.out.println("Output for toString(): \n" + p.toString()
				+ "\n________________________________________________\n\n");
	}

	@Test
	public void testToString1() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 15);
		double t1 = System.nanoTime();
		p.toString();
		double fin = (System.nanoTime() - t1) / 1000000000;
		assertTrue(fin < 0.001 * p.size());
		assertTrue(fin > 0.0000001 * p.size());
	}

	@Test
	public void testToString2() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 63);
		double t1 = System.nanoTime();
		p.toString();
		double fin = (System.nanoTime() - t1) / 1000000000;
		assertTrue(fin < 0.001 * p.size());
		assertTrue(fin > 0.0000001 * p.size());
	}

	@Test
	public void testToString3() {
		PQueueAdvanced p = new PQueueAdvanced();
		PQueueSetup(p, 255);
		double t1 = System.nanoTime();
		p.toString();
		double fin = (System.nanoTime() - t1) / 1000000000;
		assertTrue(fin < 0.001 * p.size());
		assertTrue(fin > 0.0000001 * p.size());
	}

	@Test
	public void insertTimeTest1() {
		insertTimer(8);
	}

	@Test
	public void insertTimeTest2() {
		insertTimer(2048);
	}

	@Test
	public void insertTimeTest3() {
		insertTimer(65536);
	}

	@Test
	public void removeTimeTest1() {
		removeTimer(8);
	}

	@Test
	public void removeTimeTest2() {
		removeTimer(2048);
	}

	@Test
	public void removeTimeTest3() {
		removeTimer(65536);
	}

	@Test
	public void deleteTimeTest1() {
		deleteTimer(8);
	}

	@Test
	public void deleteTimeTest2() {
		deleteTimer(2048);
	}

	@Test
	public void deleteTimeTest3() {
		deleteTimer(65536);
	}

	@Test(expected = NullPointerException.class)
	public void insertNullPointerTest() {
		PQueueAdvanced pq = new PQueueAdvanced();
		PQAble p = null;
		pq.insert(p);
	}

	@Test(expected = IllegalStateException.class)
	public void insertIllegalStateTest() {
		PQueueAdvanced pq1 = new PQueueAdvanced();
		PQueueAdvanced pq2 = new PQueueAdvanced();
		PQAble p = new ConcretePQAble();
		pq1.insert(p);
		pq2.insert(p);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void deleteUsOTest() {
		PQueueAdvanced pq = new PQueueAdvanced(4);
		PQueueSetup(pq, 4);
		pq.delete(pq.getHeap()[2]);
	}

	@Test(expected = NullPointerException.class)
	public void deleteNullPointerTest() {
		PQueue pq = new PQueueAdvanced();
		PQAble p = null;
		pq.delete(p);
	}

	@Test(expected = IllegalStateException.class)
	public void deleteIllegalStateTest() {
		PQueue pq1 = new PQueueAdvanced();
		PQueue pq2 = new PQueueAdvanced();
		PQAble p = new ConcretePQAble();
		pq1.insert(p);
		pq2.delete(p);
	}
}