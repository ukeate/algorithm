package base.catalan;

import java.util.LinkedList;

// 1和0各n个, 1前要有匹配的0
public class Ways10 {
    private static void process1(int zero, int one, LinkedList<Integer> path, LinkedList<LinkedList<Integer>> ans) {
        if (zero == 0 && one == 0) {
            LinkedList<Integer> cur = new LinkedList<>();
            for (Integer num : path) {
                cur.add(num);
            }
            ans.add(cur);
        } else {
            if (zero == 0) {
                path.addLast(1);
                process1(zero, one - 1, path, ans);
                path.removeLast();
            } else if (one == 0) {
                path.addLast(0);
                process1(zero - 1, one, path, ans);
                path.removeLast();
            } else {
                path.addLast(1);
                process1(zero, one - 1, path, ans);
                path.removeLast();
                path.addLast(0);
                process1(zero - 1, one, path, ans);
                path.removeLast();
            }
        }
    }

    public static long ways1(int n) {
        int zero = n;
        int one = n;
        LinkedList<Integer> path = new LinkedList<>();
        LinkedList<LinkedList<Integer>> ans = new LinkedList<>();
        process1(zero, one, path, ans);
        long count = 0;
        for (LinkedList<Integer> cur : ans) {
            int status = 0;
            for (Integer num : cur) {
                if (num == 0) {
                    status++;
                } else {
                    status--;
                }
                if (status < 0) {
                    break;
                }
            }
            if (status == 0) {
                count++;
            }
        }
        return count;
    }

    //

    public static long ways2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        long a = 1;
        long b = 1;
        long limit = n << 1;
        for (long i = n + 1; i <= limit; i++) {
            if (i <= n) {
                a *= i;
            } else {
                b *= i;
            }
        }
        return (b / a) / (n + 1);
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 10; i++) {
            long ans1 = ways1(i);
            long ans2 = ways2(i);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
