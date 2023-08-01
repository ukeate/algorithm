package leetc.top;

public class P277_FindTheCelebrity {
    public static boolean knows(int x, int i) {
        return true;
    }

    public int findCelebrity(int n) {
        int cand = 0;
        for (int i = 0; i < n; i++) {
            if (knows(cand, i)) {
                cand = i;
            }
        }
        for (int i = 0; i < cand; i++) {
            if (knows(cand, i)) {
                return -1;
            }
        }
        for (int i = 0; i < n; i++) {
            if (!knows(i, cand)) {
                return -1;
            }
        }
        return cand;
    }
}
