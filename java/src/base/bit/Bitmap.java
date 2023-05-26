package base.bit;

import java.util.HashSet;
import java.util.Set;

public class Bitmap {

    private long[] bits;

    public Bitmap(int max) {
        this.bits = new long[((max >> 6) + 1)];
    }

    public void add(int num) {
        bits[num >> 6] |= (1L << (num & 63));
    }

    public void delete(int num) {
        bits[num >> 6] &= ~(1L << (num & 63));
    }

    public boolean contains(int num) {
        return (bits[num >> 6] & (1L << (num & 63))) != 0;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        int max = 10000;
        Bitmap bitmap = new Bitmap(max);
        Set<Integer> set = new HashSet<>();
        int testTimes = (int) 1e8;
        for (int i = 0; i < testTimes; i++) {
            int num = (int) (Math.random() * (max + 1));
            double decide = Math.random();
            if (decide < 0.333) {
                bitmap.add(num);
                set.add(num);
            } else if (decide < 0.666) {
                bitmap.delete(num);
                set.remove(num);
            } else {
                if (bitmap.contains(num) != set.contains(num)) {
                    System.out.println("Oops");
                    break;
                }
            }
        }
        for (int num = 0; num <= max; num++) {
            if (bitmap.contains(num) != set.contains(num)) {
                System.out.println("Oops");
            }
        }
        System.out.println("test end");
    }
}
