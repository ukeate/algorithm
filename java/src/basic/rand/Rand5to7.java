package basic.rand;

public class Rand5to7 {

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

}
