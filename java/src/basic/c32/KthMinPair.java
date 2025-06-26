package basic.c32;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 数组中第K小的数值对问题
 * 
 * 问题描述：给定一个数组，从中选择任意两个元素组成数对（可以是同一个元素），
 * 所有可能的数对按字典序排序后，找出第K小的数对
 * 
 * 解题方案：提供三种不同复杂度的解决方法
 * 1. 暴力法：生成所有数对并排序 - O(N²logN²)
 * 2. 排序优化法：先排序数组，利用数学公式定位 - O(NlogN)
 * 3. 快速选择法：使用快速选择算法 - O(N)
 * 
 * 核心思想：对于排序后的数组，第K小的数对中第一个数可以通过位置计算得出
 */
public class KthMinPair {
    
    /**
     * 数对结构体
     */
    public static class Pair {
        public int x;  // 数对的第一个数
        public int y;  // 数对的第二个数

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 数对比较器：先比较第一个数，再比较第二个数
     */
    public static class Comp implements Comparator<Pair> {
        @Override
        public int compare(Pair o1, Pair o2) {
            return o1.x != o2.x ? o1.x - o2.x : o1.y - o2.y;
        }
    }

    /**
     * 方法1：暴力法 - 生成所有数对并排序
     * 时间复杂度：O(N²logN²)
     * 空间复杂度：O(N²)
     * 
     * @param arr 输入数组
     * @param k 第k小（从1开始计数）
     * @return 第k小的数对，如果k超出范围则返回null
     */
    public static int[] kth1(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        
        // 生成所有可能的数对
        Pair[] pairs = new Pair[n * n];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pairs[idx++] = new Pair(arr[i], arr[j]);
            }
        }
        
        // 排序所有数对
        Arrays.sort(pairs, new Comp());
        
        // 返回第k小的数对
        return new int[] {pairs[k - 1].x, pairs[k - 1].y};
    }

    //

    /**
     * 方法2：排序优化法 - 数学方法定位第k小数对
     * 时间复杂度：O(NlogN)
     * 空间复杂度：O(1)
     * 
     * 核心思想：
     * 1. 对数组排序
     * 2. 数对的第一个数由位置(k-1)/n确定
     * 3. 计算该数的出现次数和小于该数的数量
     * 4. 通过数学公式确定第二个数的位置
     * 
     * @param arr 输入数组
     * @param k 第k小（从1开始计数）
     * @return 第k小的数对
     */
    public static int[] kth2(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        
        // 对数组排序
        Arrays.sort(arr);
        
        // 确定第一个数：第k个数对的第一个数在排序数组中的位置
        int firstNum = arr[(k - 1) / n];
        
        // 统计小于firstNum的数的个数和firstNum本身的个数
        int lessFirstNumSize = 0;
        int firstNumSize = 0;
        for (int i = 0; i < n && arr[i] <= firstNum; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            } else {
                firstNumSize++;
            }
        }
        
        // 计算在以firstNum为第一个数的数对中，目标是第几个
        int rest = k - (lessFirstNumSize * n);
        
        // 确定第二个数
        return new int[] {firstNum, arr[(rest - 1) / firstNumSize]};
    }

    //

    /**
     * 交换数组中两个位置的元素
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 荷兰国旗问题的分区操作
     * 将数组分为三部分：小于pivot、等于pivot、大于pivot
     * 
     * @param arr 待分区的数组
     * @param l 左边界
     * @param r 右边界
     * @param pivot 基准值
     * @return 等于pivot的区域的左右边界
     */
    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1;  // 小于pivot区域的右边界
        int more = r + 1;  // 大于pivot区域的左边界
        int cur = l;       // 当前处理位置
        
        while (cur < more) {
            if (arr[cur] < pivot) {
                // 当前值小于pivot，放入左区域
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                // 当前值大于pivot，放入右区域
                swap(arr, cur, --more);
            } else {
                // 当前值等于pivot，继续处理下一个
                cur++;
            }
        }
        
        // 返回等于pivot区域的左右边界
        return new int[] {less + 1, more - 1};
    }

    /**
     * 快速选择算法：在无序数组中找到第idx小的元素（从0开始计数）
     * 平均时间复杂度：O(N)
     * 
     * @param arr 数组
     * @param idx 目标位置（从0开始）
     * @return 第idx小的元素值
     */
    private static int kMin(int[] arr, int idx) {
        int l = 0;
        int r = arr.length - 1;
        int pivot = 0;
        int[] range = null;
        
        while (l < r) {
            // 随机选择基准值
            pivot = arr[l + (int) ((r - l + 1) * Math.random())];
            
            // 进行分区操作
            range = partition(arr, l, r, pivot);
            
            if (idx < range[0]) {
                // 目标在左区域
                r = range[0] - 1;
            } else if (idx > range[1]) {
                // 目标在右区域
                l = range[1] + 1;
            } else {
                // 目标就在等于pivot的区域
                return pivot;
            }
        }
        
        return arr[l];
    }

    /**
     * 方法3：快速选择法 - 最优解
     * 时间复杂度：O(N)
     * 空间复杂度：O(1)
     * 
     * 核心思想：
     * 1. 使用快速选择算法找到第一个数
     * 2. 统计相关数量
     * 3. 使用快速选择算法找到第二个数
     * 
     * @param arr 输入数组
     * @param k 第k小（从1开始计数）
     * @return 第k小的数对
     */
    public static int[] kth3(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        
        // 使用快速选择找到第一个数
        int firstNum = kMin(arr, (k - 1) / n);
        
        // 统计小于firstNum的数的个数和等于firstNum的数的个数
        int lessFirstNumSize = 0;
        int firstNumSize = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            }
            if (arr[i] == firstNum) {
                firstNumSize++;
            }
        }
        
        // 计算在以firstNum为第一个数的数对中，目标是第几个
        int rest = k - (lessFirstNumSize * n);
        
        // 使用快速选择找到第二个数
        return new int[] {firstNum, kMin(arr, (rest - 1) / firstNumSize)};
    }

    //

    private static int[] randomArr(int len, int max) {
        int[] arr = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((max + 1) * Math.random() - (int) ((max + 1) * Math.random()));
        }
        return arr;
    }

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

    public static void main(String[] args) {
        int times = 1000000;
        int len = 30;
        int max = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, max);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int n = arr.length * arr.length;
            int k = (int) (n * Math.random()) + 1;
            int[] ans1 = kth1(arr1, k);
            int[] ans2 = kth2(arr2, k);
            int[] ans3 = kth3(arr3, k);
            if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
