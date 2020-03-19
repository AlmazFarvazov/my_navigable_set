import java.util.*;

public class NSet<T extends Comparable> extends AbstractSet<T> implements NavigableSet<T> {
    private ArrayList<T> al;
    Comparator<T> c;

    /**
     * Constructor with init comparator
     * @param c init comparator
     */
    public NSet(Comparator<T> c) {
        this.c = c;
        al = new ArrayList<>();
    }

    /**
     * Constructor with init comparator and unsorted collection
     * @param c init comparator
     * @param newCollection unsorted collection
     */
    public NSet(Comparator<T> c, Collection<T> newCollection) {
        this.c = c;
        al = (ArrayList<T>) newCollection;
        al.sort(c);
    }

    /**
     * Method for adding an element
     * @param el adding element
     * @return false, if adding failed
     */
    public boolean add (T el) {
        if (al.indexOf(el) != -1) return false;
        for (int i = 0; i < size(); i++){
            if (c.compare(el, al.get(i)) <= 0) {
                al.add(i, el);
                return true;
            }
        }
        al.add(el);
        return true;
    }

    /**
     * Method return the greatest element which lower than hte given element or null
     * @param t given element
     * @return the element
     */
    @Override
    public T lower(T t) {
        if (c.compare(first(), t) > 0) return null;
        for (int i = al.size() - 1; i >=0; i--) {
            if (c.compare(al.get(i), t) < 0) return al.get(i + 1);
        }
        return null;
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element or null
     * @param t given element
     * @return the element
     */
    @Override
    public T floor(T t) {
        if (c.compare(first(), t) > 0) return null;
        for (int i = al.size() - 1; i >= 0; i--) {
            if (c.compare(al.get(i), t) <= 0) return al.get(i + 1);
        }
        return null;
    }

    /**
     * Returns the least element in this set greater than or equal to the given element or null
     * @param t given element
     * @return the element
     */
    @Override
    public T ceiling(T t) {
        if (c.compare(last(), t) < 0) return null;
        for (int i = 0; i < al.size(); i++) {
            if (c.compare(al.get(i), t) >= 0) return al.get(i);
        }
        return null;
    }

    /**
     * Returns the least element in this set strictly greater than the given element or null
     * @param t given element
     * @return the element
     */
    @Override
    public T higher(T t) {
        if (c.compare(last(), t) < 0) return null;
        for (int i = 0; i < al.size(); i++) {
            if (c.compare(al.get(i), t) > 0) return al.get(i);
        }
        return null;
    }

    /**
     * Retrieves and removes the first (lowest) element, or returns null if this set is empty.
     * @return first element
     */
    @Override
    public T pollFirst() {
        if (al.size() == 0) return null;
        T temp = first();
        al.remove(0);
        return temp;
    }

    /**
     * Retrieves and removes the last (highest) element, or returns null if this set is empty.
     * @return last element
     */
    @Override
    public T pollLast() {
        if (al.size() == 0) return null;
        T temp = last();
        al.remove(al.size() - 1);
        return temp;
    }

    /**
     * Return iterator
     * @return iterator
     */
    @Override
    public Iterator iterator() {
        return al.iterator();
    }

    /**
     * Return descending set
     * @return set
     */
    @Override
    public NavigableSet<T> descendingSet() {
        ArrayList<T> newArr = new ArrayList<>();
        for (int i = 0; i < size(); i++){
            newArr.add(al.get(size() - 1 - i));
        }
        return new NSet<T>(new Comparator<T>() {
            @Override
            public int compare(T t, T t1) {
                return c.compare(t1, t);
            }
        }, newArr);
    }

    /**
     * Return descending set iterator
     * @return iterator
     */
    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    /**
     * Returns a view of the portion of this set whose elements range from fromElement to toElement.
     * @param t from element
     * @param b from inclusive
     * @param e1 to element
     * @param b1 to inclusive
     * @return set
     */
    @Override
    public NavigableSet<T> subSet(T t, boolean b, T e1, boolean b1) {
        NSet<T> newSet = new NSet<T>(c, tailSet(t ,b));
        return newSet.headSet(e1, b1);
    }

    /**
     * Returns a view of the portion of this set whose elements are less than (or equal to, if inclusive is true) toElement.
     * @param t to element
     * @param b inclusive
     * @return set
     */
    @Override
    public NavigableSet<T> headSet(T t, boolean b) {
        NSet<T> hs= (NSet<T>) headSet(t);
        if (!b) return hs;
        hs.add(hs.ceiling(t));
        return hs;
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than (or equal to, if inclusive is true) fromElement.
     * @param t from element
     * @param b inclusive
     * @return set
     */
    @Override
    public NavigableSet<T> tailSet(T t, boolean b) {
        NSet<T> ts = (NSet<T>) tailSet(t);
        if (!b) return ts;
        ts.add(floor(t));
        return ts;
    }

    /**
     * Return comparator
     * @return comparator
     */
    @Override
    public Comparator<? super T> comparator() {
        return c;
    }

    @Override
    public SortedSet<T> subSet(T t, T e1) {
        return subSet(t, true, e1, false);
    }

    /**
     * Returns a view of the portion of this set whose elements are strictly less than toElement.
     * @param t to element
     * @return set
     */
    @Override
    public SortedSet<T> headSet(T t) {
        ArrayList<T> newArr = new ArrayList<>();
        for (T el : al) {
            if (c.compare(el, t) < 0) newArr.add(t);
        }
        return new NSet<T>(c, newArr);
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
     * @param t from element
     * @return set
     */
    @Override
    public SortedSet<T> tailSet(T t) {
        ArrayList<T> newArr = new ArrayList<>();
        for (T el : al) {
            if (c.compare(el, t) > 0) newArr.add(t);
        }
        return new NSet<T>(c, newArr);
    }

    /**
     * Return the first(lowest) element
     * @return
     */
    @Override
    public T first() {
        return al.get(0);
    }

    /**
     * Return the last(highest) element
     * @return element
     */
    @Override
    public T last() {
        return al.get(al.size() - 1);
    }

    /**
     * Return size of the set
     * @return size
     */
    @Override
    public int size() {
        return al.size();
    }

    /**
     * Returns a string representation of the contents of the specified set.
     * @return string
     */
    public String toString() {
        return Arrays.toString(al.toArray());
    }

}
