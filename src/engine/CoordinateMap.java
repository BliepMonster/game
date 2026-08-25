package engine;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import java.util.Optional;

public interface CoordinateMap<E> extends Iterable<CoordinateMap.Wrapper<E>> {
    record Wrapper<E>(E element, int x, int y) {}
    Optional<E> get(int x, int y);
    default E getUnsafe(int x, int y) {
        return get(x, y).orElse(null);
    }
    void put(int x, int y, @NotNull E e);
    Optional<E> remove(int x, int y);
    static <E> CoordinateMap<E> newDefaultPackedMap() {
        return new ArrayCoordinateMap<>();
    }
    Vector4i getBounds();
}
