package base.sort;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 寻找第K小元素的三种经典算法
 * 
 * 问题描述：
 * 给定一个无序数组，找出其中第K小的元素（从1开始计数）
 * 
 * 三种解法对比：
 * 1. 大根堆法：维护大小为K的大根堆，时间复杂度O(N*logK)，空间复杂度O(K)
 * 2. 快速选择法：基于快排思想，平均时间复杂度O(N)，最坏O(N²)，空间复杂度O(logN)
 * 3. BFPRT算法：改进的快速选择，严格O(N)时间复杂度，空间复杂度O(logN)
 * 
 * 实际应用场景：
 * - 数据流中的第K小元素
 * - TopK问题的变种
 * - 分位数计算
 */
public class MinKth {
    
    /**
     * 大根堆比较器，用于维护最大堆
     */
    public static class Comp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;  // 大根堆：较大的元素优先级更高
        }
    }

    /**
     * 方法1：大根堆法
     * 
     * 思路：
     * 1. 维护一个大小为K的大根堆
     * 2. 先将前K个元素入堆
     * 3. 对于后续元素，如果小于堆顶，则替换堆顶
     * 4. 最终堆顶就是第K小元素
     * 
     * 时间复杂度：O(N*logK) - N个元素，每次堆操作logK
     * 空间复杂度：O(K) - 维护大小为K的堆
     * 适用场景：K相对较小，内存有限制的情况
     */
    public static int minKth1(int[] arr, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(new Comp());
        
        // 先将前K个元素加入大根堆
        for (int i = 0; i < k; i++) {
            heap.add(arr[i]);
        }
        
        // 对于剩余元素，如果小于堆顶，则替换
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < heap.peek()) {
                heap.poll();
                heap.add(arr[i]);
            }
        }
        
        return heap.peek();  // 堆顶就是第K小元素
    }

    // ======================== 方法2：快速选择算法 ========================
    
    /**
     * 数组复制工具方法
     */
    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 交换数组中两个位置的元素
     */
    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }

    /**
     * 荷兰国旗问题 - 三路划分
     * 将数组按pivot分为三部分：<pivot, =pivot, >pivot
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界  
     * @param pivot 基准值
     * @return 等于pivot的区间[left, right]
     */
    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1;  // 小于pivot的区域右边界
        int more = r + 1;  // 大于pivot的区域左边界
        int cur = l;       // 当前遍历位置
        
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);  // 放到小于区域
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);    // 放到大于区域，cur不动
            } else {
                cur++;  // 等于pivot，继续遍历
            }
        }
        return new int[] { less + 1, more - 1};  // 返回等于pivot的区间
    }

    /**
     * 快速选择核心递归过程
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param idx 目标索引位置
     * @return 第idx+1小的元素
     */
    private static int process2(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];  // 只有一个元素
        }
        
        // 随机选择pivot，避免最坏情况
        int pivot = arr[l + (int) ((r - l + 1) * Math.random())];
        int[] range = partition(arr, l, r, pivot);
        
        if (idx >= range[0] && idx <= range[1]) {
            return arr[idx];  // 目标在等于pivot的区域
        } else if (idx < range[0]) {
            return process2(arr, l, range[0] - 1, idx);  // 在小于pivot的区域
        } else {
            return process2(arr, range[1] + 1, r, idx);  // 在大于pivot的区域
        }
    }

    /**
     * 方法2：快速选择算法（随机版本）
     * 
     * 思路：基于快排的分治思想，但只处理包含目标的一侧
     * 1. 随机选择pivot进行三路划分
     * 2. 判断目标位置在哪个区域，递归处理该区域
     * 3. 直到找到目标元素
     * 
     * 时间复杂度：平均O(N)，最坏O(N²) - 取决于pivot选择
     * 空间复杂度：O(logN) - 递归栈深度
     * 适用场景：一般情况下性能最优
     */
    public static int minKth2(int[] arr, int k) {
        int[] arr2 = copy(arr);  // 保护原数组
        return process2(arr2, 0, arr.length - 1, k - 1);  // k-1是因为索引从0开始
    }

    // ======================== 方法3：BFPRT算法 ========================
    
    /**
     * 对小数组进行插入排序
     * BFPRT算法的基础工具，用于对5个元素的小组进行排序
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     */
    private static void insertionSort(int[] arr, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            for (int j = i - 1; j >= l && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }
    /**
     * 获取小数组的中位数
     * 先排序，然后返回中间位置的元素
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 中位数
     */
    private static int median(int[] arr, int l, int r) {
        insertionSort(arr, l, r);  // 对小数组排序
        return arr[(l + r) / 2];   // 返回中位数
    }

    /**
     * BFPRT算法的核心：中位数的中位数
     * 
     * 算法步骤：
     * 1. 将数组分成每5个元素一组
     * 2. 对每组进行排序，找出每组的中位数
     * 3. 递归地找出所有中位数的中位数
     * 4. 这个中位数的中位数作为优秀的pivot
     * 
     * 为什么选择5个元素一组？
     * - 保证了在最坏情况下，能够淘汰至少30%的元素
     * - 使得时间复杂度严格为O(N)
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 中位数的中位数
     */
    private static int medianOfMedians(int[] arr, int l, int r) {
        int size = r - l + 1;
        int offset = size % 5 == 0 ? 0 : 1;  // 不足5个元素的组需要额外处理
        int[] medians = new int[size / 5 + offset];  // 存储每组的中位数
        
        // 对每组5个元素求中位数
        for (int team = 0; team < medians.length; team++) {
            int teamFirst = l + team * 5;  // 每组的起始位置
            // 每组最多5个元素，最后一组可能不足5个
            medians[team] = median(arr, teamFirst, Math.min(r, teamFirst + 4));
        }
        
        // 递归求中位数的中位数
        return bfprt(medians, 0, medians.length - 1, medians.length / 2);
    }

    /**
     * BFPRT算法的主体递归过程
     * 
     * 与普通快速选择的区别：
     * - 使用medianOfMedians选择pivot，而不是随机选择
     * - 保证了每次至少能淘汰30%的元素
     * - 从而保证了严格的O(N)时间复杂度
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param idx 目标索引
     * @return 第idx+1小的元素
     */
    private static int bfprt(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];  // 只有一个元素
        }
        
        // 使用中位数的中位数作为pivot，这是BFPRT的核心
        int pivot = medianOfMedians(arr, l, r);
        int[] range = partition(arr, l, r, pivot);
        
        if (idx >= range[0] && idx <= range[1]) {
            return arr[idx];  // 目标在等于pivot的区域
        } else if (idx < range[0]) {
            return bfprt(arr, l, range[0] - 1, idx);  // 在小于pivot的区域
        } else {
            return bfprt(arr, range[1] + 1, r, idx);  // 在大于pivot的区域
        }
    }

    /**
     * 方法3：BFPRT算法
     * 
     * 思路：改进的快速选择算法，核心改进是pivot的选择策略
     * 1. 不随机选择pivot，而是使用"中位数的中位数"
     * 2. 这样选择的pivot能保证每次至少淘汰30%的元素
     * 3. 从而实现严格的O(N)时间复杂度
     * 
     * 算法名称来源：
     * BFPRT是五位作者的姓氏首字母：Blum、Floyd、Pratt、Rivest、Tarjan
     * 
     * 时间复杂度：严格O(N) - 无论输入如何都是线性时间
     * 空间复杂度：O(logN) - 递归栈深度
     * 适用场景：对时间复杂度有严格要求的情况
     */
    public static int minKth3(int[] arr, int k) {
        int[] arr2 = copy(arr);  // 保护原数组
        return bfprt(arr2, 0, arr2.length - 1, k - 1);  // k-1因为索引从0开始
    }

    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int k = (int) (arr.length * Math.random()) + 1;
            int ans1 = minKth1(arr, k);
            int ans2 = minKth2(arr, k);
            int ans3 = minKth3(arr, k);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
