package engine.abstraction;

public interface Converter<A, B> {
    B convert(A a);
}
