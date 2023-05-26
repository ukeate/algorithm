package basic.rand;

public class Rand01 {

    private static int notFair() {
        return Math.random() > 0.8 ? 0 : 1;
    }

    public static int rand01() {
        int ans = 0;
        do {
            ans = notFair();
        } while (ans == notFair());
        return ans;
    }

    public static void main(String[] args) {

        int count = 0;
        for (int i = 0; i < 10000; i++) {
            if (rand01() < 0.5) {
                count++;
            }
        }

        System.out.println((double) count / (double) 10000);
    }
}
