package basic;

public class Rand {
    private static int gen5() {
        return (int) (Math.random() * 5) + 1;
    }

    private static int gen1() {
        int ans = 0;
        do {
            ans = gen5();
        } while (ans == 3);
        return ans < 3 ? 0 : 1;
    }

    public static int genTo7() {
        int ans = 0;
        do {
            ans = (gen1() << 2) + (gen1() << 1) + gen1();
        } while (ans == 7);
        return ans + 1;
    }

    public static double randomSqrt() {
        return Math.max(Math.random(), Math.random());
    }

    private static int notFair() {
        return Math.random() > 0.8 ? 0 : 1;
    }

    public static int fair() {
        int ans = 0;
        do {
            ans = notFair();
        } while (ans == notFair());
        return ans;
    }

    public static void main(String[] args) {
        int count = 0;
        for (int i = 0; i < 10000; i++) {
            if (fair() < 0.5) {
                count++;
            }
        }

        System.out.println((double) count / (double) 10000);
    }
}
