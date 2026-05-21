package main;

public final class YieldUtils {
    private YieldUtils() {}
    public static int getYieldEffect(int original, float yield) {
        if (yield < 0) return original;
        yield /= 100;
        yield += 1;
        var o = original * (int) yield;
        yield -= (int) yield;
        boolean extra = Math.random() < yield;
        return extra ? o + original : o;
    }
}
