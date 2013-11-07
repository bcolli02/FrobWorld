package com.putable.pqueue;

/**
 * Implementation of the {@link #PQueue} to be used in the Frob World program.
 * Priority Queue follows rules of a heap priority queue. Notable functions are
 * insert, remove, and delete. All should follow run time requirements.
 * 
 * @author Brennan Collins
 * 
 */
public class PQueueAdvanced implements PQueue {
	// our heap as an array
	private PQAble[] pq;
	// the actual size of our PQueue along with the count of
	// how many PQAbles are inside of it
	private int pqSize, pqAbleCount = 1;
	// boolean expression for whether our PQueue is advanced or not
	private boolean isAdvanced = false;

	/**
	 * Default constructor for our PQueue. Sets the initial size of the heap to
	 * be 4, and sets our PQueue to be advanced.
	 */
	public PQueueAdvanced() {
		this.isAdvanced = true;
		this.pqSize = 4;
		this.pq = new PQAble[pqSize];
	}

	/**
	 * Secondary constructor that gives the user ability to set the size of the
	 * PQueue but the PQueue will not be advanced.
	 * 
	 * @param psize
	 *            the size to initiate our heap to
	 */
	public PQueueAdvanced(int psize) {
		this.pqSize = psize;
		this.pq = new PQAble[psize];
		pq[0] = null;
	}

	/**
	 * Method to resize the queue when it reaches a max capacity.
	 * 
	 * @param sizeFactor
	 *            the factor by which to increase or decrease the size of our
	 *            heap
	 */
	public void resize() {
		int newPqSize = pqAbleCount * 2;
		PQAble[] tempQ = new PQAble[newPqSize];
		for (int i = 1; i < pqAbleCount; i++) {
			tempQ[i] = pq[i];
		}
		pq = tempQ;
		pqSize = newPqSize;
	}

	/**
	 * Method to get parent index of a node
	 * 
	 * @param k
	 *            index of node
	 * @return index of parent
	 */
	private int parentIndex(int k) {
		return k / 2;
	}

	/**
	 * Method to get a nodes index for its left child
	 * 
	 * @param k
	 *            index of node
	 * @return index of left child
	 */

	private int leftChildIndex(int k) {
		return 2 * k;
	}

	/**
	 * Method to get a nodes index for its right child
	 * 
	 * @param k
	 *            index of node
	 * @return index of right child
	 */
	private int rightChildIndex(int k) {
		return (2 * k) + 1;
	}

	@Override
	public PQAble remove() {
		if (size() == 0)
			return null;
		else {
			PQAble top = pq[1];
			pq[1] = pq[--pqAbleCount];
			pq[pqAbleCount] = null;
			top.setPQueue(null);
			if (size() > 1)
				sinkDown(1);
			return top;
		}
	}

	@Override
	public PQAble top() {
		if (size() == 0)
			return null;
		else
			return pq[1];
	}

	@Override
	public void insert(PQAble newPq) {
		// a queue that is full is our 'queue' to resize
		if (pqAbleCount >= pqSize - 1)
			resize();
		if (newPq == null)
			throw new NullPointerException();
		if (newPq.getPQueue() != null)
			throw new IllegalStateException();
		newPq.setPQueue(this);
		newPq.setIndex(pqAbleCount);
		pq[pqAbleCount] = newPq;
		bubbleUp(pqAbleCount++);
	}

	/**
	 * When we get a node that has higher priority than its parent node we move
	 * the parent node to the node at our given index then change our index to
	 * the index that the parent was at and then we repeat the process until the
	 * node has a parent with greater priority. When we find this parent node,
	 * we will let our specified node settle at its new index.
	 * 
	 * @param index
	 *            to bubble up from
	 */
	public void bubbleUp(int index) {
		PQAble newPq = pq[index];
		int pDex = parentIndex(index);
		while (pDex != 0 && newPq.compareTo(pq[pDex]) < 0) {
			pq[index] = pq[pDex];
			pq[index].setIndex(index);
			index = pDex;
			pDex = parentIndex(index);
		}
		newPq.setIndex(index);
		pq[index] = newPq;
	}

	/**
	 * When a node has lesser priority than one or both its children, we will
	 * move the child with the higher priority to our current index and then set
	 * the new index to the one of the child that had higher priority. We repeat
	 * this process until our node is at an index where it has higher priority
	 * than its children at which point we will settle into the spot in the heap
	 * at this index.
	 * 
	 * @param index
	 *            the index to start sinking from
	 */
	public void sinkDown(int index) {
		PQAble top = pq[index];

		while (index * 2 < pqAbleCount) {
			PQAble hpNode;
			int left = leftChildIndex(index);
			int right = rightChildIndex(index);

			if (right < pqAbleCount && pq[right].compareTo(pq[left]) < 0) {
				hpNode = pq[right];
				hpNode.setIndex(right);
			} else {
				hpNode = pq[left];
				hpNode.setIndex(left);
			}

			if (top.compareTo(hpNode) < 0 || top.compareTo(hpNode) == 0)
				break;

			pq[index] = hpNode;
			index = hpNode.getIndex();
			hpNode.setIndex(parentIndex(index));
		}
		top.setIndex(index);
		pq[index] = top;
	}

	@Override
	public void delete(PQAble p) {
		if (!isAdvanced())
			throw new UnsupportedOperationException();
		if (p.equals(null))
			throw new NullPointerException();
		if (p.getPQueue() != this)
			throw new IllegalStateException();

		int pDex = p.getIndex();
		p.setPQueue(null);
		if (pDex == size()) {
			pq[--pqAbleCount] = null;
			return;
		} else {
			pq[pDex] = pq[--pqAbleCount];
			pq[pqAbleCount] = null;
			if (p.compareTo(pq[pDex]) > 0)
				bubbleUp(pDex);
			else
				sinkDown(pDex);
		}
	}

	@Override
	public int size() {
		return pqAbleCount - 1;
	}

	@Override
	public boolean isAdvanced() {
		return isAdvanced;
	}

	/**
	 * A helper method for the toString() function. This takes our heap and puts
	 * it into a string that when printed out resembles a tree of all the
	 * PQAbles in our PQueue in order of priority with nodes extending up being
	 * the right child of any given node and nodes extending downward being the
	 * left child of any given node. Reformatted from Patrick Kelly's version of
	 * a print binary tree function given to us in CS 251.
	 * 
	 * @param head
	 *            the node we are currently expanding from
	 * @param left
	 *            the spacing to be applied before a left child node
	 * @param root
	 *            the spacing to be applied before a root child node
	 * @param right
	 *            the spacing to be applied before a right child node
	 * @return a tree diagram of our heap
	 */
	public String visualHeap(PQAble head, String left, String root, String right) {

		if (head.getIndex() >= (pqAbleCount-1) / 2)
			return "\n" + (root + "(" + Integer.toString(((ConcretePQAble) head).getNextUpdate()) + ")");

		int leftChild = leftChildIndex(head.getIndex());
		int rightChild = rightChildIndex(head.getIndex());

		String rootSpaces = String.format("%"
				+ String.valueOf(head.getIndex()).length() + "s", "");
		left += rootSpaces;
		right += rootSpaces;

		return visualHeap(pq[rightChild], left + "     ", left + "  ,----",
				left + "  |  ")
				+ "\n"
				+ (root + "(" + Integer.toString(((ConcretePQAble) head).getNextUpdate()))
				+ ")"
				+ visualHeap(pq[leftChild], right + "  |  ",
						right + "   `----", right + "     ");

	}

	@Override
	public String toString() {
		return visualHeap(pq[1], "", " ", "");
	}

	/**
	 * Boolean method to check if our heap is truly a heap. Returns true if our
	 * PQueue satisfies the heap condition and false otherwise.
	 * 
	 * @param dex
	 *            the index at which to check our heap
	 * @return boolean expression for whether or not the PQueue represents a
	 *         heap
	 */
	public boolean isHeap(int dex) {
		int lDex = leftChildIndex(dex);
		int rDex = rightChildIndex(dex);
		if (dex >= size() / 2)
			return true;
		else if (pq[dex].compareTo(pq[lDex]) <= 0
				&& pq[dex].compareTo(pq[rDex]) <= 0)
			return isHeap(lDex) && isHeap(rDex);
		else
			return false;
	}

	/**
	 * An array representation of our heap.
	 * 
	 * @return the heap or PQueue as an array
	 */
	public PQAble[] getHeap() {
		return pq;
	}
}
