package base.tree;

public class PaperFolds {
    private static void process(int i, int n, boolean down) {
        if (i > n) {
            return;
        }
        process(i + 1, n, true);
        System.out.println(down ? "凹" : "凸");
        process(i + 1, n, false);
    }
    public static void paperFolds(int n) {
        process(1, n, true);
    }

    public static void main(String[] args) {
        paperFolds(4);
    }
}
