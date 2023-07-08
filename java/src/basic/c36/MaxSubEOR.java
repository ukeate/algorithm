package basic.c36;

// 最大子数组异或和
public class MaxSubEOR {
    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 前缀异或和数组
        int[] eor = new int[arr.length];
        eor[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            eor[i] = eor[i - 1] ^ arr[i];
        }
        int max = Integer.MIN_VALUE;
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i <= j; i++) {
                max = Math.max(max, i == 0 ? eor[j] : eor[j] ^ eor[i - 1]);
            }
        }
        return max;
    }

    //

    private static class Node {
        public Node[] nexts = new Node[2];
    }

    private static class NumTrie {
        public Node head = new Node();

        public void add(int newNum) {
            Node cur = head;
            for (int move = 31; move >= 0; move--) {
                int path = ((newNum >> move) & 1);
                cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
                cur = cur.nexts[path];
            }
        }

        public int maxXor(int sum) {
            Node cur = head;
            int res = 0;
            for (int move = 31; move >= 0; move--) {
                int path = (sum >> move) & 1;
                // 期望
                int best = move == 31 ? path : (path ^ 1);
                // 实际
                best = cur.nexts[best] != null ? best : (best ^ 1);
                res |= (path ^ best) << move;
                cur = cur.nexts[best];
            }
            return res;
        }
    }

    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int eor = 0;
        NumTrie trie = new NumTrie();
        trie.add(0);
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
            max = Math.max(max, trie.maxXor(eor));
            trie.add(eor);
        }
        return max;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 5000000;
        int maxLen = 30;
        int maxVal = 50;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = max1(arr);
            int ans2 = max2(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(ans1 + "|" + ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
