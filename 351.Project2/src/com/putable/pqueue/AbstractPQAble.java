package com.putable.pqueue;

/**
 * An incomplete implementation of an object that can reside on a PQueue.
 * Concrete classes that extend this must implement Comparable, to define what
 * 'priority' means for these objects.
 * 
 * <p>
 * The implementation of Comparable by extensions of AbstractPQAble <i>must</i>
 * be such that if A &lt; B (i.e., A.compareTo(B) &lt; 0), then A is <i>higher
 * priority</i> than B. Implementors should take care to ensure that they don't
 * get this backwards -- in particular, it's possible to extend AbstractPQAble
 * and simultaneously implement the PQueue interface in such a way that they are
 * both wrong according to spec but their errors cancel out. If the implementor
 * then implements tests based on such a backwards interpretation, the fantasy
 * of correct operation will be complete -- <i>until it fails external
 * testing!</i>
 * 
 * <p>
 * <b>Watch out!</b> Get it right!
 * 
 * @author ackley
 * @version 1.0
 * @see PQAble
 * @see PQueue
 */

public abstract class AbstractPQAble implements PQAble {
    /**
     * Construct an AbstractPQAble. The result will not be located on any PQueue
     * (i.e., getPQueue() will initially return null)
     */
    public AbstractPQAble() {  }

    /**
     * Our current location in the PQueue
     */
    private int index = 0;

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * The PQueue that we are on, or null if we are not on a pqueue.
     */
    private PQueue pqueue = null;

    @Override
    public PQueue getPQueue() {
        return pqueue;
    }

    @Override
    public void setPQueue(PQueue pq) {
        pqueue = pq;
    }
}
