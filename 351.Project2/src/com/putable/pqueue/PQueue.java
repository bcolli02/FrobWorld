package com.putable.pqueue;

/**
 * A specialized interface representing a container that can only hold things
 * that implement 'PQAble', and can return them in priority queue order --
 * obeying strict time/space performance criteria documented with each method.
 * 
 * <p>
 * This is one of two interfaces plus one abstract class that together provide
 * the infrastructure for the fast intrusive priority queue implementations to
 * be used for the discrete event simulation project.
 * 
 * <p>
 * The use of and relationships among those three types can be summarized in
 * this figure:
 * 
 * <pre>
 *                              +-------------------------------+
 *                              |interface java.lang.Comparable |
 *                              |-------------------------------|
 *                              |int compareTo(Object)          |
 *                              |-------------------------------|
 *                              +-------------------------------+
 * +--------------------+                  &circ;
 * |interface PQueue    |                 / \
 * |--------------------|                 ---
 * |void insert(PQAble) |                  | extends
 * |void delete(PQAble) |          +-----------------------+
 * |PQAble remove()     | 0..1   * |interface PQAble       |
 * |int size()          |&lt;--------&gt;|-----------------------|
 * |boolean isAdvanced()|          |                       |
 * |String toString()   |          |int getIndex()         |
 * |--------------------|          |void setIndex(int)     |
 * +--------------------+          |PQueue getPQueue()     |
 *        &circ;                        |void setPQueue(PQueue) |
 *       / \                       |-----------------------|
 *       ---                       +-----------------------+
 *        . implements                     &circ;
 *    +-------------------+               / \
 *    |ConcretePQueueJones|               ---  implements
 *    |-------------------|        +-----------------------+
 *    | ..whatever..      |        |AbstractPQAble         |
 *    |-------------------|        |-----------------------|
 *    |void insert(PQAble)|        |-----------------------|
 *    |void delete(PQAble)|        |int getIndex()         |
 *    |PQAble remove()    |        |void setIndex(int)     |
 *    |int size()         |        |PQueue getPQueue()     |
 *    |String toString()  |        |void setPQueue(PQueue) |
 *    +-------------------+        +-----------------------+
 *                                         &circ;
 *                                        / \
 *                                        ---
 *    Picture key:                         | extends
 *       +-----------------+        +---------------------+
 *       |type             |        |ConcretePQAbleJones  |
 *       |-----------------|        |---------------------|
 *       |declared methods |        | ..whatever..        |
 *       |-----------------|        |---------------------|
 *       |defined methods  |        |int compareTo(Object)|
 *       +-----------------+        +---------------------+
 * </pre>
 * 
 * In that figure the two Concrete* classes represent the ones that have to be
 * implemented by the project contractor to produce a complete working fast
 * intrusive priority queue.
 * 
 * @author ackley
 * @version 1.0
 * @see PQAble
 * @see AbstractPQAble
 */

public interface PQueue {
    /**
     * Removes and returns the highest priority PQAble on the PQueue, or returns
     * null if the PQueue is empty. Time: O(log |PQueue|).
     * 
     * @return a reference to the now-unlocated PQAble with the highest
     *         priority, or null.
     */
    public PQAble remove();

    /**
     * Returns the highest priority PQAble on the PQueue <i>without</i> removing
     * it from the PQueue, or returns null if the PQueue is empty. Time: O(log
     * |PQueue|).
     * 
     * @return a reference to the PQAble with the highest priority, which is
     *         <i>still located</i> in the PQueue, or null.
     */
    public PQAble top();

    /**
     * Inserts p, which must be currently not located on any PQueue, into the
     * PQueue. If p refers to something that is already located anywhere
     * (including in this PQueue), an IllegalStateException is thrown and p is
     * not inserted. Time: Amortized O(log |PQueue|).
     * 
     * @param p
     *            The PQAble to be inserted
     *            
     * @throws NullPointerException
     *             If p is null
     *             
     * @throws IllegalStateException
     *             If p is already located on any PQueue
     */
    public void insert(PQAble p);

    /**
     * (Advanced PQueue only.) Deletes p, which must already be located in the
     * PQueue, from this PQueue, and leaves 'p' located on no PQueue. Time:
     * O(log |PQueue|)
     * 
     * @param p
     *            The PQAble to be removed from this PQueue
     * 
     * @throws UnsupportedOperationException
     *             iff {@link #isAdvanced()} returns false
     * 
     * @throws NullPointerException
     *             If p is null
     * 
     * @throws IllegalStateException
     *             If p is not currently located on this PQueue.
     * 
     * 
     */
    public void delete(PQAble p);

    /**
     * Get the size of the PQueue.
     * 
     * @return the number of PQAble's currently contained by this PQueue. Time:
     *         O(1)
     */
    public int size();

    /**
     * Determine whether this is an advanced PQueue
     * 
     * @return true iff this is an advanced PQueue
     */
    public boolean isAdvanced();

    /**
     * @return An (otherwise unspecified) String representation of the contents
     *         of the PQueue. Time: O(n)
     */
    public String toString();
}
