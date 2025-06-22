package base.greed;

import java.util.PriorityQueue;

/**
 * 切分金条问题 - 哈夫曼编码思想的应用
 * 
 * 问题描述：
 * 给定一个数组，代表要切分出的金条长度
 * 每次切分金条的代价是被切分金条的长度
 * 求将一根长金条切分成给定长度数组的最小代价
 * 
 * 等价问题：
 * 反向思考 - 将小金条合并成大金条
 * 每次合并两根金条的代价是两根金条长度之和
 * 求将给定长度的金条合并成一根的最小代价
 * 
 * 贪心策略：
 * 每次选择最短的两根金条进行合并（哈夫曼编码的思想）
 * 使用小根堆维护当前所有金条的长度
 */
public class LessMoneySplitGold {
    /**
     * 复制数组并合并两个指定位置的元素
     * @param arr 原数组
     * @param i 第一个要合并的索引
     * @param j 第二个要合并的索引
     * @return 合并后的新数组
     */
    private static int[] copyAndMergeTwo(int[] arr, int i, int j) {
        int[] ans = new int[arr.length - 1];
        int ansIdx = 0;
        for (int arrIdx = 0; arrIdx < arr.length; arrIdx++) {
            if (arrIdx != i && arrIdx != j) {
                ans[ansIdx++] = arr[arrIdx];
            }
        }
        ans[ansIdx] = arr[i] + arr[j];
        return ans;
    }

    /**
     * 递归暴力解法 - 尝试所有可能的合并顺序
     * @param arr 当前金条长度数组
     * @param pre 之前的合并代价
     * @return 最小的总合并代价
     */
    private static int process1(int[] arr, int pre) {
        // 基础情况：只剩一根金条，无需再合并
        if (arr.length == 1) {
            return pre;
        }
        int ans = Integer.MAX_VALUE;
        // 尝试每一对金条的合并
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                // 递归计算选择合并第i根和第j根金条后的最小代价
                ans = Math.min(ans, process1(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
            }
        }
        return ans;
    }

    /**
     * 暴力解法 - 枚举所有可能的合并顺序
     * 时间复杂度：O((n-1)! * n) 非常高的复杂度
     * @param arr 金条长度数组
     * @return 最小合并代价
     */
    public static int lessMoney1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return process1(arr, 0);
    }

    //

    /**
     * 贪心算法解法 - 哈夫曼编码思想
     * 
     * 算法原理：
     * 1. 将所有金条长度放入小根堆
     * 2. 每次取出最短的两根金条进行合并
     * 3. 合并代价累加到总代价中
     * 4. 将合并后的金条重新放入堆中
     * 5. 重复直到只剩一根金条
     * 
     * 为什么贪心策略正确：
     * - 越短的金条应该在越深的层次被合并，这样它们的长度被重复计算的次数更多
     * - 越长的金条应该在越浅的层次被合并，减少重复计算的次数
     * - 这与哈夫曼编码中高频字符用短编码的思想一致
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 
     * @param arr 金条长度数组
     * @return 最小合并代价
     */
    public static int lessMoney2(int[] arr) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        // 将所有金条长度放入小根堆
        for (int i = 0; i < arr.length; i++) {
            pq.add(arr[i]);
        }
        int sum = 0;    // 总合并代价
        int cur = 0;    // 当前合并代价
        // 当堆中还有超过1根金条时，继续合并
        while (pq.size() > 1) {
            // 取出最短的两根金条进行合并
            cur = pq.poll() + pq.poll();
            sum += cur;     // 累加合并代价
            pq.add(cur);    // 将合并后的金条重新放入堆中
        }
        return sum;
    }

    //

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大数组长度
     * @param maxVal 最大元素值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法 - 验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 6;
        int maxVal = 1000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            if (lessMoney1(arr) != lessMoney2(arr)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
