package com.putable.pqueue;


/**
 * This subinterface is to be implemented by anything that wishes to be
 * placeable into an implementation of a PQueue.
 * 
 * <p>
 * Note: All the methods in this subinterface, except possibly getPQueue, are
 * intended for the exclusive use of package com.putable.pqueue and should
 * treated as if they were declared with package access (except of course for
 * the fact that all interface methods are implicitly public whether we like it
 * or not).
 * 
 * <p>
 * Most particularly, if anything <i>other</i> than a PQueue calls either of the
 * mutator methods, that is a contract violation and the resulting behavior of
 * the system is undefined.
 * 
 * @author ackley
 * @version 1.0
 * @param <T>
 * @see PQueue
 * @see AbstractPQAble
 * 
 */
public interface PQAble extends Comparable<PQAble> {

    /**
     * Get the most recent index value saved for PQueue's use.
     * 
     * @return The value most recently passed to setIndex, or 0 if setIndex has
     *         not yet been called on this PQAble-implementing object.
     */
    int getIndex();

    /**
     * Save an int value to be returned in response to a later getIndex() call.
     * To be called only by implementations of PQueue.
     * 
     * <p>
     * <b>Note:</b> In normal usage by normal implementations of PQueue's, it
     * will typically be the case that index will be positive, or never be
     * negative, but there are <i>no constraints</i> on the value of index
     * included in the setIndex contract. Implementations of PQueue are free to
     * use the index value as desired; setIndex will never throw an exception.
     * 
     * @param index
     *            the value to be saved
     */
    void setIndex(int index);

    /**
     * Get the most recent PQueue reference, or null, saved for PQueue's use. A
     * null value implies the PQAble is currently not located on any PQueue.
     * 
     * @return The null or non-null reference most recently passed to setPQueue,
     *         or null if setPQueue has not yet been called on this
     *         PQAble-implementing object.
     */
    public PQueue getPQueue();

    /**
     * Save a reference to a PQueue to be returned in response to a later
     * getPQueue() call. To be called only by implementations of PQueue.
     * 
     * @param pq
     *            the reference to be saved, which may or may not be null.
     */
    void setPQueue(PQueue pq);
}
