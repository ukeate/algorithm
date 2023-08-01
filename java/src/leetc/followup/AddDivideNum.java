package leetc.followup;

import java.util.HashMap;

/*
 * 100 = 3 + 69258 / 714
 * 100 = 82 + 3546 / 197
 *
 * 等号右边的部分，可以写成 p1 + p2 / p3的形式
 * 要求p1和p2和p3，所使用的数字，必须把1~9使用完全，并且不重复
 * 满足的话，我们就说，形如p1 + p2 / p3，一个有效的"带分数"形式
 * 要求，p2 / p3 必须整除
 *
 * 输入N，返回N有多少种带分数形式
 * 100，有11种带分数形式
 *
 * 输入的N，N  < 10的8次方
 *
 *
 * */
public class AddDivideNum {
    public static HashMap<Integer, Integer> map = new HashMap<>();
    public static int[] arr = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    // 交换末l1+1与l2+1的数字
    private static int swap(int num, int l1, int l2) {
        int n1 = (num / arr[l1]) % 10;
        int n2 = (num / arr[l2]) % 10;
        return num + (n2 - n1) * arr[l1] - (n2 - n1) * arr[l2];
    }

    // [0,idx]排列
    private static void process(int num, int idx) {
        if (idx == -1) {
            for (int add = 8; add >= 2; add--) {
                int p1 = num / arr[add];
                int rest = num % arr[add];
                for (int dev = (add >> 1); dev >= 1; dev--) {
                    int p2 = rest / arr[dev];
                    int p3 = rest % arr[dev];
                    if (p2 % p3 == 0) {
                        int ans = p1 + (p2 / p3);
                        if (!map.containsKey(ans)) {
                            map.put(ans, 1);
                        } else {
                            map.put(ans, map.get(ans) + 1);
                        }
                    }
                }
            }
        } else {
            // idx[0,8]全排列
            for (int i = idx; i >= 0; i--) {
                process(swap(num, idx, i), idx - 1);
            }
        }
    }

    public static int ways(int n) {
        if (map.size() == 0) {
            process(123456789, 8);
        }
        return map.containsKey(n) ? map.get(n) : 0;
    }

    public static void main(String[] args) {
        int n = 100;
        long start, end;
        start = System.currentTimeMillis();
        System.out.println(n + ":" + ways(n));
        end = System.currentTimeMillis();
        System.out.println("run time: " + (end - start));
        n = 10000;
        start = System.currentTimeMillis();
        System.out.println(n + ":" + ways(n));
        end = System.currentTimeMillis();
        System.out.println("run time: " + (end - start));
    }
}
