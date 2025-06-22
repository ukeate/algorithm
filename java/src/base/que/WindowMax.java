package base.que;

import java.util.LinkedList;

/**
 * 滑动窗口最大值问题 - 双端队列优化解法
 * 
 * 问题描述：
 * 给定一个数组arr和窗口大小w，返回每个滑动窗口中的最大值
 * 
 * 朴素解法：
 * 对每个窗口暴力求最大值，时间复杂度O(n*w)
 * 
 * 优化解法：
 * 使用双端队列维护单调递减序列，时间复杂度O(n)
 * 
 * 核心思想：
 * 1. 队列中存储数组下标，按对应值的大小保持单调递减
 * 2. 队头始终是当前窗口的最大值下标
 * 3. 新元素入队时，从队尾移除所有比它小的元素
 * 4. 队头元素超出窗口范围时，从队头移除
 */
public class WindowMax {
    /**
     * 滑动窗口最大值 - 双端队列优化版
     * 
     * 算法步骤：
     * 1. 使用双端队列存储数组下标
     * 2. 维护队列中下标对应值的单调递减性
     * 3. 队头始终是当前窗口的最大值
     * 4. 处理每个位置时：
     *    - 从队尾移除所有值小于等于当前值的下标
     *    - 将当前下标加入队尾
     *    - 如果队头下标超出窗口范围，移除队头
     *    - 如果窗口形成，记录当前窗口最大值
     * 
     * 时间复杂度：O(n) - 每个元素最多入队出队一次
     * 空间复杂度：O(w) - 队列最多存储w个元素
     * 
     * @param arr 输入数组
     * @param w 窗口大小
     * @return 每个窗口的最大值数组
     */
    public static int[] maxWindow(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        int[] ans = new int[arr.length - w + 1];    // 结果数组
        LinkedList<Integer> deque = new LinkedList<>();  // 双端队列存储下标
        
        for (int r = 0; r < arr.length; r++) {
            // 维护单调递减队列：移除队尾所有小于等于当前值的下标
            while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[r]) {
                deque.pollLast();
            }
            deque.addLast(r);   // 当前下标入队
            
            // 检查队头是否超出窗口范围
            if (deque.peekFirst() <= r - w) {
                deque.pollFirst();
            }
            
            // 如果窗口已经形成，记录当前窗口最大值
            if (r >= w - 1) {
                ans[r - w + 1] = arr[deque.peekFirst()];
            }
        }
        return ans;
    }

    /**
     * 滑动窗口最大值 - 暴力解法（用于对比验证）
     * 
     * 对每个窗口位置都重新计算最大值
     * 时间复杂度：O(n*w)
     * 空间复杂度：O(1)
     * 
     * @param arr 输入数组
     * @param w 窗口大小
     * @return 每个窗口的最大值数组
     */
    public static int[] maxWindowSure(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        int[] ans = new int[arr.length - w + 1];
        // 对每个窗口位置，暴力计算最大值
        for (int l = 0; l < ans.length; l++) {
            int max = Integer.MIN_VALUE;
            for (int ll = l; ll < l + w; ll++) {
                max = Math.max(arr[ll], max);
            }
            ans[l] = max;
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     * @param ms 最大数组长度
     * @param mv 最大元素值
     * @return 随机数组
     */
    private static int[] randomArr(int ms, int mv) {
        int[] arr = new int[(int) (Math.random() * (ms + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (mv + 1));
        }
        return arr;
    }

    /**
     * 判断两个数组是否相等
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean equal(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if ((arr1 == null ^ arr2 == null)) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印数组
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int w = (int) (Math.random() * (arr.length + 1));
            int[] ans1 = maxWindow(arr, w);         // 优化解法
            int[] ans2 = maxWindowSure(arr, w);     // 暴力解法
            if (!equal(ans1, ans2)) {
                System.out.println("Wrong");
                System.out.println(w);
                print(arr);
                print(ans1);
                print(ans2);
                break;
            }
        }
        System.out.println("test finish");
    }
}
