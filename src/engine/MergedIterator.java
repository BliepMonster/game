package engine;

import java.util.Iterator;
import java.util.List;

public class MergedIterator<E> implements Iterator<E> {
    private final List<Iterator<E>> is;
    private int current = 0;
    public MergedIterator(List<Iterator<E>> iterators) {
        this.is = iterators;
    }
    private void advance() {
        while ( current < is.size() && !is.get(current).hasNext() )
            current++;
    }
    public boolean hasNext() {
        advance();
        return current < is.size();
    }
    public E next() {
        advance();
        return is.get(current++).next();
    }
}
