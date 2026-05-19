package engine.abstraction;

import java.util.Objects;

/// A class for assertions that should be tested outside of debug code
public class Assertions {
    public static void assertTrue(boolean b) {
        if (!b) throw new AssertionException();
    }

    public static void assertFalse(boolean b) {
        if (b) throw new AssertionException();
    }

    public static void assertNotNull(Object o) {
        if (o == null) throw new AssertionException();
    }

    public static void assertNull(Object o) {
        if (o != null) throw new AssertionException();
    }

    public static void assertPositive(double f) {
        if (f < 0) throw new AssertionException();
    }

    public static void assertNegative(double f) {
        if (f > 0) throw new AssertionException();
    }

    public static void assertEqual(double a, double b) {
        if (a != b) throw new AssertionException();
    }

    public static void assertEqual(boolean a, boolean b) {
        if (a != b) throw new AssertionException();
    }

    public static void assertEqual(long a, long b) {
        if (a != b) throw new AssertionException();
    }

    public static void assertEqual(char a, char b) {
        if (a != b) throw new AssertionException();
    }

    public static void assertNotEqual(double a, double b) {
        if (a == b) throw new AssertionException();
    }
    public static void assertNotEqual(boolean a, boolean b) {
        if (a == b) throw new AssertionException();
    }
    public static void assertNotEqual(long a, long b) {
        if (a == b) throw new AssertionException();
    }
    public static void assertNotEqual(char a, char b) {
        if (a == b) throw new AssertionException();
    }
    public static void assertNotEqual(Object a, Object b) {
        if (Objects.equals(a, b)) throw new AssertionException();
    }
    public static void assertEqual(Object a, Object b) {
        if (!Objects.equals(a, b)) throw new AssertionException();
    }
}
