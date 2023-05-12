package chapter10;

import chapter7.Sort;

/**
 * Created by outrun on 6/6/16.
 */
public class Random {

    private static final int A = 48271;
    private static final int M = 2147483647;
    private static final int Q = M / A;
    private static final int R = M % A;

    public Random() {
        this((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
    }

    public Random(int initialValue) {
        if (initialValue < 0) {
            initialValue += M;
        }

        state = initialValue;
        if (state == 0) {
            state = 1;
        }
    }

    public int randomInt() {
        int tmpState = A * (state % Q) - R * (state / Q);

        if (tmpState >= 0) {
            state = tmpState;
        } else {
            state = tmpState + M;
        }

        return state;
    }

    public int randomIntWRONG() {
        return state = (A * state) % M;
    }

    public double random0_1() {
        return (double) randomInt() / M;
    }

    public int randomInt(int low, int high) {
        double partitionSize = (double) M / (high - low + 1);

        return (int) (randomInt() / partitionSize) + low;
    }

    public long randomLong(long low, long high) {
        long longVal = ((long) randomInt() << 31) + randomInt();
        long longM = ((long) M << 31) + M;

        double partitionSize = (double) longM / (high - low + 1);
        return (long) (longVal / partitionSize) + low;
    }

    public static void permute(Object[] a) {
        Random r = new Random();

        for (int j = 1; j < a.length; j++) {
            Sort.swapReferences(a, j, r.randomInt(0, j));
        }
    }

    private int state;
}
