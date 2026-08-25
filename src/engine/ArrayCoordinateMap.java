package engine;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import java.util.*;

public class ArrayCoordinateMap<E> implements CoordinateMap<E> {
    private record Wrapper<E>(E element, int x) { }
    private static class Space<E> implements Iterable<ArrayCoordinateMap.Wrapper<E>> {
        Object[] positive;
        Object[] negative;
        public Space() {
            positive = new Object[16];
            negative = new Object[16];
        }
        E getRaw(int pos) {
            if (pos >= 0) {
                if (positive.length <= pos)
                    return null;
                return (E) positive[pos];
            }
            if (negative.length <= -pos - 1)
                return null;
            return (E) negative[-pos - 1];
        }
        void put(int pos, E e) {
            if (pos >= 0) {
                if (positive.length <= pos)
                    positive = Arrays.copyOf(positive, pos + 1);
                positive[pos] = e;
            }
            else {
                if (negative.length <= -pos - 1)
                    negative = Arrays.copyOf(negative, -pos);
                negative[-pos - 1] = e;
            }
        }
        void remove(int pos) {
            if (pos >= 0) {
                if (positive.length <= pos)
                    return;
                positive[pos] = null;
            }
            else {
                if (negative.length <= -pos - 1)
                    return;
                negative[-pos - 1] = null;
            }
        }
        public @NotNull Iterator<ArrayCoordinateMap.Wrapper<E>> iterator() {
            ArrayList<ArrayCoordinateMap.Wrapper<E>> list = new ArrayList<>();
            for (int i = 0; i < negative.length; i++) {
                if (negative[i] != null)
                    list.add(new Wrapper<>((E) negative[i], -i-1));
            } for (int i = 0; i < positive.length; i++) {
                if (positive[i] != null)
                    list.add(new Wrapper<>((E) positive[i], i));
            }
            return list.iterator();
        }
    }
    private final Space<Space<E>> map = new Space<>();
    public Optional<E> get(int x, int y) {
        return Optional.ofNullable(getUnsafe(x, y));
    }
    @Override
    @SuppressWarnings("unchecked")
    public E getUnsafe(int x, int y) {
        Space<E> space = map.getRaw(x);
        return space == null ? null : space.getRaw(y);
    }
    public Optional<E> remove(int x, int y) {
        changed = true;
        var e = get(x, y);
        Space<E> s = map.getRaw(x);
        if (s != null) s.remove(y);
        return e;
    }
    public void put(int x, int y, @NotNull E e) {
        changed = true;
        Space<E> space = map.getRaw(x);
        if (space != null) {
            space.put(y, e);
        } else {
            var a = new Space<E>();
            map.put(x, a);
            a.put(y, e);
        }
    }

    @Override
    public Vector4i getBounds() {
        int minY = 0;
        int maxY = 0;
        for (int i = 0; i < map.positive.length; i++) {
            Space<E> space = (Space<E>) map.positive[i];
            if (space != null) {
                minY = Math.min(minY, -space.negative.length-1);
                maxY = Math.max(maxY, space.positive.length-1);
            }
        }
        for (int i = 0; i < map.negative.length; i++) {
            Space<E> space = (Space<E>) map.negative[i];
            if (space != null) {
                minY = Math.min(minY, -space.negative.length-1);
                maxY = Math.max(maxY, space.positive.length-1);
            }
        }
        return new Vector4i(-map.negative.length-1, map.positive.length+map.negative.length, minY, maxY-minY);
    }
    private ArrayList<CoordinateMap.Wrapper<E>> elements;
    private boolean changed = true;
    public @NotNull Iterator<CoordinateMap.Wrapper<E>> iterator() {
        if (!changed) return elements.iterator();
        changed = false;
        var list = new ArrayList<CoordinateMap.Wrapper<E>>();
        for (Wrapper<Space<E>> element : map) {
            int x = element.x;
            for (Wrapper<E> e : element.element) {
                list.add(new CoordinateMap.Wrapper<>(e.element, x, e.x));
            }
        }
        elements = list;
        return elements.iterator();
    }
}
