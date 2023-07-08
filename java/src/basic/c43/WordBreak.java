package basic.c43;

import java.util.HashSet;

// arr中的word拼str, 可使用任意次, 有多少种拼法
public class WordBreak {
    private static int process(String str, int i, HashSet<String> set) {
        if (i == str.length()) {
            return 1;
        }
        int ways = 0;
        for (int end = i; end < str.length(); end++) {
            String pre = str.substring(i, end + 1);
            if (set.contains(pre)) {
                ways += process(str, end + 1, set);
            }
        }
        return ways;
    }

    public static int ways1(String str, String[] arr) {
        HashSet<String> set = new HashSet<>();
        for (String w : arr) {
            set.add(w);
        }
        return process(str, 0, set);
    }

    //

    public static int ways2(String str, String[] arr) {
        if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        HashSet<String> set = new HashSet<>();
        for (String s : arr) {
            set.add(s);
        }
        int n = str.length();
        int[] dp = new int[n + 1];
        dp[n] = 1;
        for (int i = n - 1; i >= 0; i--) {
            for (int end = i; end < n; end++) {
                if (set.contains(str.substring(i, end + 1))) {
                    dp[i] += dp[end + 1];
                }
            }
        }
        return dp[0];
    }

    //

    private static class Node {
        public boolean end;
        public Node[] nexts;
        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    private static int process3(char[] str, Node root, int i) {
        if (i == str.length) {
            return 1;
        }
        int ways = 0;
        Node cur = root;
        for (int end = i; end < str.length; end++) {
            int path = str[end] - 'a';
            if (cur.nexts[path] == null) {
                break;
            }
            cur = cur.nexts[path];
            if (cur.end) {
                ways += process3(str, root, end + 1);
            }
        }
        return ways;
    }

    public static int ways3(String str, String[] arr) {
        if (str == null ||str.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        Node root = new Node();
        for (String s : arr) {
            char[] ss = s.toCharArray();
            Node node = root;
            int idx = 0;
            for (int i = 0; i < ss.length; i++) {
                idx = ss[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true;
        }
        return process3(str.toCharArray(), root, 0);
    }

    //

    public static int ways4(String s, String[] arr) {
        if (s == null || s.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        Node root = new Node();
        for (String str : arr) {
            char[] ss = str.toCharArray();
            Node node = root;
            int idx = 0;
            for (int i = 0; i < ss.length; i++) {
                idx = ss[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[] dp = new int[n + 1];
        dp[n] = 1;
        for (int i = n - 1; i >= 0; i--) {
            Node cur = root;
            for (int end = i; end < n; end++) {
                int path = str[end] - 'a';
                if (cur.nexts[path] == null) {
                    break;
                }
                cur = cur.nexts[path];
                if (cur.end) {
                    dp[i] += dp[end + 1];
                }
            }
        }
        return dp[0];
    }

    //

    private static class Sample {
        public String str;
        public String[] arr;
        public Sample(String s, String[] a) {
            str = s;
            arr = a;
        }
    }

    private static String[] randomSeeds(char[] candidates, int num, int len) {
        String[] arr = new String[(int) (num * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            char[] str = new char[(int) (len * Math.random()) + 1];
            for (int j = 0; j < str.length; j++) {
                str[j] = candidates[(int) (candidates.length * Math.random())];
            }
            arr[i] = String.valueOf(str);
        }
        return arr;
    }

    private static Sample randomSample(char[] candidates, int num, int len, int joint) {
        String[] seeds = randomSeeds(candidates, num, len);
        HashSet<String> set = new HashSet<>();
        for (String str : seeds) {
            set.add(str);
        }
        String[] arr = new String[set.size()];
        int idx = 0;
        for (String str : set) {
            arr[idx++] = str;
        }
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < joint; i++) {
            all.append(arr[(int) (Math.random() * arr.length)]);
        }
        return new Sample(all.toString(), arr);
    }

    public static void main(String[] args) {
        int times = 30000;
        char[] candidates = {'a', 'b'};
        int num = 20;
        int len = 4;
        int joint = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Sample sample = randomSample(candidates, num, len, joint);
            int ans1 = ways1(sample.str, sample.arr);
            int ans2 = ways2(sample.str, sample.arr);
            int ans3 = ways3(sample.str, sample.arr);
            int ans4 = ways4(sample.str, sample.arr);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans4) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");
    }
}
