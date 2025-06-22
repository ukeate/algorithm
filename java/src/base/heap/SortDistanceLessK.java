package base.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 距离小于K的排序问题
 * 
 * 问题描述：
 * 给定一个数组，其中每个元素距离其最终排序位置不超过K个位置
 * 要求对这个数组进行排序
 * 
 * 例如：数组[3,2,1,5,4,7,6,5]，K=3
 * 元素3的最终位置可能在索引0-2之间，元素2的最终位置可能在索引0-3之间，以此类推
 * 
 * 解题思路：
 * 利用小根堆的特性，每次从前K+1个元素中取出最小值放到正确位置
 * 这样可以保证排序的正确性，并且时间复杂度为O(n log k)
 * 
 * 算法优势：
 * 1. 时间复杂度：O(n log k)，比普通排序O(n log n)更优（当k << n时）
 * 2. 空间复杂度：O(k)，只需要存储k+1个元素的小根堆
 * 3. 适合处理几乎有序的数组
 */
public class SortDistanceLessK {
    
    /**
     * 对距离小于K的数组进行排序
     * 
     * 算法步骤：
     * 1. 将前k个元素（或所有元素，如果数组长度小于k）放入小根堆
     * 2. 依次处理剩余元素：
     *    - 将当前元素加入堆
     *    - 从堆中取出最小元素放到当前要填充的位置
     * 3. 处理完所有元素后，依次取出堆中剩余元素
     * 
     * 为什么这样做是正确的：
     * - 由于每个元素距离其最终位置不超过k，所以位置i的最终元素一定在前i+k+1个元素中
     * - 小根堆保证我们总是能取到当前可选范围内的最小元素
     * - 这样就能保证每次取出的元素都是当前位置的正确元素
     * 
     * @param arr 待排序数组
     * @param k 最大距离
     */
    public static void sortDistanceLessK(int[] arr, int k) {
        // 边界情况：k为0表示数组已经有序
        if (k == 0) {
            return;
        }
        
        // 创建小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        
        // 将前k个元素（或所有元素）加入堆中
        int r = 0;  // 右边界指针，表示下一个要加入堆的元素位置
        for (; r <= Math.min(arr.length - 1, k - 1); r++) {
            heap.add(arr[r]);
        }
        
        // 开始排序过程
        int l = 0;  // 左边界指针，表示下一个要填充的位置
        for (; r < arr.length; l++, r++) {
            heap.add(arr[r]);          // 将新元素加入堆
            arr[l] = heap.poll();      // 取出最小元素放到当前位置
        }
        
        // 处理堆中剩余的元素
        while (!heap.isEmpty()) {
            arr[l++] = heap.poll();
        }
    }

    /**
     * 生成距离小于K的随机数组用于测试
     * 
     * 生成策略：
     * 1. 先生成完全有序的数组
     * 2. 对数组中的元素进行有限制的交换（交换距离不超过k）
     * 3. 这样可以保证生成的数组满足"距离小于K"的条件
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 最大元素绝对值
     * @param k 最大距离限制
     * @return 距离小于K的随机数组
     */
    private static int[] randomArrDistanceLessK(int maxLen, int maxVal, int k) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        
        // 生成随机数组
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxLen + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        
        // 先排序，得到基准有序数组
        Arrays.sort(arr);
        
        // 记录哪些位置已经参与过交换，避免重复交换
        boolean[] hasSwap = new boolean[arr.length];
        
        // 进行有限制的随机交换
        for (int i = 0; i < arr.length; i++) {
            // 选择交换目标：在距离k范围内随机选择
            int j = Math.min(i + (int) ((k + 1) * Math.random()), arr.length - 1);
            
            // 只有当两个位置都没有参与过交换时才进行交换
            if (!hasSwap[i] && !hasSwap[j]) {
                hasSwap[i] = true;
                hasSwap[j] = true;
                // 交换元素
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
        return arr;
    }

    /**
     * 复制数组
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 判断两个数组是否相等
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null ^ arr2 == null) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
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
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证排序算法的正确性
     * 
     * 测试策略：
     * 1. 生成距离小于K的随机数组
     * 2. 分别使用自定义排序算法和标准排序算法处理
     * 3. 比较两种算法的结果是否一致
     */
    public static void main(String[] args) {
        int times = 100000;     // 测试次数
        int maxLen = 100;       // 最大数组长度
        int maxVal = 100;       // 最大元素绝对值
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int k = (int) ((maxLen + 1) * Math.random());   // 随机生成K值
            int[] arr = randomArrDistanceLessK(maxLen, maxVal, k);
            
            // 创建两个相同的数组副本
            int[] arr1 = copy(arr);     // 用于自定义排序算法
            int[] arr2 = copy(arr);     // 用于标准排序算法
            
            // 分别使用两种排序算法
            sortDistanceLessK(arr1, k);
            Arrays.sort(arr2);
            
            // 验证结果是否一致
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong");
                print(arr);     // 打印原数组
                print(arr1);    // 打印自定义排序结果
                print(arr2);    // 打印标准排序结果
                break;
            }
        }
        System.out.println("test end");
    }
}
