package chapter8;

/**
 * Created by outrun on 5/31/16.
 */
public class DisjSets {
    public DisjSets(int numElemets) {
        s = new int[numElemets];

        for (int i = 0; i < s.length; i++) {
            s[i] = -1;
        }
    }

    public void union(int root1, int root2) {
        if (s[root2] < s[root1]) {
            s[root1] = root2;
        } else {
            if (s[root1] == s[root2]) {
                s[root1] --;
            }
            s[root2] = root1;
        }
    }

    public int find(int x) {
        if (s[x] < 0) {
            return x;
        } else {
            return find(s[x]);
        }
    }

    private int[] s;
}
