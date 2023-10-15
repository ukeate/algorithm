package leetc.top;

public class P440_KthSmallestInLexicographicalOrder {
    private static int len(int n) {
        int len = 0;
        while (n != 0) {
            n /= 10;
            len++;
        }
        return len;
    }


    public static int[] offset = {0, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    // x开头 <= 3位的111个：x 1个, [x0,x9] 10个, [x00,x99] 100个
    public static int[] number = {0, 1, 11, 111, 1111, 11111, 111111, 1111111, 11111111, 111111111, 1111111111};

    // max固定开头位，求kth位数
    private static int kth(int max, int len, int kth) {
        // 是否命中中间位置
        boolean closeToMax = true;
        int ans = max / offset[len];
        // 单有首位时返回。减去x000
        while (--kth > 0) {
            // 去首位, pick第二位，之后还剩len位
            max %= offset[len--];
            int pick = 0;
            // 非中间位置时, 每次循环都结算
            if (!closeToMax) {
                // len>0, k只有1个时，pick=0
                pick = (kth - 1) / number[len];
                ans = ans * 10 + pick;
                kth -= pick * number[len];
            } else {
                int first = max / offset[len];
                int left = first * number[len];
                if (kth <= left) {
                    closeToMax = false;
                    // len>0, k只有1个时，pick=0
                    pick = (kth - 1) / number[len];
                    ans = ans * 10 + pick;
                    kth -= pick * number[len];
                    continue;
                }
                kth -= left;
                int mid = number[len - 1] + (max % offset[len]) + 1;
                if (kth <= mid) {
                    ans = ans * 10 + first;
                    continue;
                }
                closeToMax = false;
                kth -= mid;
                len--;
                // pick > 0
                pick = (kth + number[len] - 1) / number[len] + first;
                ans = ans * 10 + pick;
                kth -= (pick - first - 1) * number[len];
            }
        }
        return ans;
    }

    // n=65237, 求第64000个数字是什么
    public static int findKthNumber(int n, int k) {
        // 5 待定位数, pick按11111结算
        int len = len(n);
        // 6 原数首位
        int first = n / offset[len];
        // 55555
        int left = (first - 1) * number[len];
        // 已结算数字
        int pick = 0;
        // 已结算排名
        int already = 0;
        // [1,5]开头, 结算pick
        if (k <= left) {
            // <=5
            pick = (k + number[len] - 1) / number[len];
            // <=55555
            already = (pick - 1) * number[len];
            // pick拼999,待定位数,剩余排名
            return kth((pick + 1) * offset[len] - 1, len, k - already);
        }
        // 6开头 + x5237的余数 + x0000 = 6349
        int mid = number[len - 1] + (n % offset[len]) + 1;
        // 排名在中, 结算pick
        if (k - left <= mid) {
            return kth(n, len, k - left);
        }
        // [7,9]开头, 结算pick
        // k = 64000 - (55555 + 6349) = 2096
        k -= left + mid;
        // 4, 4位待定，已定的pick应该按1111结算
        len--;
        // 结算pick: 加2x1111，可到8
        pick = (k + number[len] - 1) / number[len] + first;
        // k中已结算排名: k中的2222
        already = (pick - first - 1) * number[len];
        // 239999, 4,
        return kth((pick + 1) * offset[len] - 1, len, k - already);
    }

    public static void main(String[] args) {
        System.out.println( findKthNumber(65237, 64000));
    }
}
