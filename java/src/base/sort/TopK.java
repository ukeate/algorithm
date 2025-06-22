package base.sort;

import java.util.Arrays;

/**
 * TopK问题 - 寻找数组中第K大的元素
 * 
 * 问题描述：
 * 给定一个无序数组，找出其中第K大的元素（K从1开始计数）
 * 例如：数组[3,2,1,5,6,4]，第2大的元素是5
 * 
 * 解决方案：
 * 使用快速选择算法（QuickSelect），基于快速排序的分区思想
 * 不需要完全排序，只需要找到第K大的元素位置
 * 
 * 算法原理：
 * 1. 随机选择一个基准元素进行分区
 * 2. 分区后，基准元素左边都比它大，右边都比它小
 * 3. 根据基准元素的位置决定在哪一边继续查找
 * 4. 递归处理直到找到第K大的元素
 * 
 * 算法优势：
 * - 平均时间复杂度O(N)，比完全排序O(N*logN)更优
 * - 原地算法，空间复杂度O(1)
 * - 实际应用中性能优秀
 * 
 * 时间复杂度：
 * - 平均情况：O(N) - 每次分区大约减少一半的搜索范围
 * - 最坏情况：O(N²) - 每次都选到最值作为基准
 * 空间复杂度：O(1) - 原地算法
 * 
 * 应用场景：
 * - 寻找中位数
 * - Top K问题
 * - 百分位数计算
 * - 数据分析中的排名问题
 */
public class TopK {
    // O(N*logN)
    public static int[] topK1(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        k = Math.min(n, k);
        Arrays.sort(arr);
        int[] ans = new int[k];
        for (int i = n - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    //

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void heapify(int[] arr, int idx, int heapSize) {
        int left = idx * 2 + 1;
        while (left < heapSize) {
            int big = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            if (arr[idx] > arr[big]) {
                break;
            }
            swap(arr, big, idx);
            idx = big;
            left = idx * 2 + 1;
        }
    }

    // O(N + K*logN)
    public static int[] topK2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        k = Math.min(n, k);
        for (int i = n - 1; i >= 0; i--) {
            heapify(arr, i, n);
        }
        int heapSize = n;
        swap(arr, 0, --heapSize);
        int count = 1;
        while (heapSize > 0 && count < k) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
            count++;
        }
        int[] ans = new int[k];
        for (int i = n - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    //

    /**
     * 寻找第K大元素的主函数
     * 
     * @param arr 数组
     * @param k 第k大（从1开始计数）
     * @return 第k大的元素值
     */
    public static int findKthLargest(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("无效的输入参数");
        }
        
        // 转换为从0开始的索引：第k大元素在排序后数组的位置是k-1
        return quickSelect(arr, 0, arr.length - 1, k - 1);
    }

    /**
     * 快速选择算法 - 寻找第K大元素的核心实现
     * 
     * 算法详解：
     * 1. 随机选择基准元素，避免最坏情况
     * 2. 使用荷兰国旗分区，将数组分为三部分：大于、等于、小于基准值
     * 3. 根据目标位置k与分区边界的关系，决定在哪个区域继续查找
     * 4. 递归处理直到找到目标元素
     * 
     * 分区策略（按降序排列）：
     * [大于基准] [等于基准] [小于基准]
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标位置（第k大元素的索引）
     * @return 第k大的元素值
     */
    private static int quickSelect(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];  // 只有一个元素
        }
        
        // 随机选择基准元素，避免最坏情况
        int randomIndex = left + (int) (Math.random() * (right - left + 1));
        int pivot = arr[randomIndex];
        
        // 荷兰国旗分区：将数组分为大于、等于、小于三部分
        int[] bounds = partition(arr, left, right, pivot);
        int leftBound = bounds[0];   // 等于区域的左边界
        int rightBound = bounds[1];  // 等于区域的右边界
        
        // 根据k的位置决定在哪个区域继续查找
        if (k < leftBound) {
            // 目标在大于区域，递归查找左半部分
            return quickSelect(arr, left, leftBound - 1, k);
        } else if (k > rightBound) {
            // 目标在小于区域，递归查找右半部分
            return quickSelect(arr, rightBound + 1, right, k);
        } else {
            // 目标在等于区域，直接返回基准值
            return pivot;
        }
    }

    /**
     * 荷兰国旗分区算法 - 将数组按基准值分为三部分
     * 
     * 分区结果（按降序）：
     * [left, leftBound-1] - 大于基准值的元素
     * [leftBound, rightBound] - 等于基准值的元素
     * [rightBound+1, right] - 小于基准值的元素
     * 
     * 算法过程：
     * 1. 使用三个指针：less（小于区域右边界）、more（大于区域左边界）、cur（当前位置）
     * 2. 遍历数组，根据元素与基准值的比较结果进行交换
     * 3. 维护三个区域的边界
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @param pivot 基准值
     * @return 等于区域的边界 [leftBound, rightBound]
     */
    private static int[] partition(int[] arr, int left, int right, int pivot) {
        int less = left - 1;     // 大于区域的右边界（因为我们要降序排列）
        int more = right + 1;    // 小于区域的左边界
        int cur = left;          // 当前处理位置
        
        // 三路分区过程
        while (cur < more) {
            if (arr[cur] > pivot) {
                // 当前元素大于基准值，放入大于区域
                swap(arr, ++less, cur++);
            } else if (arr[cur] < pivot) {
                // 当前元素小于基准值，放入小于区域
                swap(arr, --more, cur);
                // 注意：cur不自增，因为交换过来的元素还未处理
            } else {
                // 当前元素等于基准值，保持在等于区域
                cur++;
            }
        }
        
        // 返回等于区域的边界
        return new int[]{less + 1, more - 1};
    }

    //

    /**
     * 暴力解法 - 用于验证快速选择算法的正确性
     * 
     * 通过完全排序找到第K大元素
     * 时间复杂度：O(N*logN)
     * 
     * @param arr 数组
     * @param k 第k大（从1开始计数）
     * @return 第k大的元素值
     */
    private static int findKthLargestSure(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("无效的输入参数");
        }
        
        // 复制数组避免修改原数组
        int[] copy = arr.clone();
        
        // 使用Java内置排序（降序）
        java.util.Arrays.sort(copy);
        
        // 返回第k大元素（注意：排序后是升序，所以第k大在倒数第k个位置）
        return copy[copy.length - k];
    }

    //

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 复制数组
     * 
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
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 测试方法 - 验证TopK算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== TopK问题测试 ===");
        
        // 手工测试用例1
        int[] testArr1 = {3, 2, 1, 5, 6, 4};
        System.out.println("\n测试用例1：");
        System.out.print("数组：");
        print(testArr1);
        
        for (int k = 1; k <= testArr1.length; k++) {
            int[] copy1 = copy(testArr1);
            int[] copy2 = copy(testArr1);
            
            int result1 = findKthLargest(copy1, k);
            int result2 = findKthLargestSure(copy2, k);
            
            System.out.println("第" + k + "大元素 - 快速选择：" + result1 + "，暴力解法：" + result2 + 
                             "，正确：" + (result1 == result2));
        }
        
        // 手工测试用例2 - 包含重复元素
        int[] testArr2 = {3, 3, 3, 3, 3};
        System.out.println("\n测试用例2（重复元素）：");
        System.out.print("数组：");
        print(testArr2);
        
        int k2 = 3;
        int[] copy3 = copy(testArr2);
        int[] copy4 = copy(testArr2);
        
        int result3 = findKthLargest(copy3, k2);
        int result4 = findKthLargestSure(copy4, k2);
        
        System.out.println("第" + k2 + "大元素 - 快速选择：" + result3 + "，暴力解法：" + result4 + 
                         "，正确：" + (result3 == result4));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        System.out.println("最大数组长度：" + maxLen);
        System.out.println("最大元素绝对值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            if (arr.length == 0) continue;  // 跳过空数组
            
            int k = (int) (Math.random() * arr.length) + 1;  // 随机选择k
            
            int[] copy1 = copy(arr);
            int[] copy2 = copy(arr);
            
            int ans1 = findKthLargest(copy1, k);
            int ans2 = findKthLargestSure(copy2, k);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("k = " + k);
                System.out.println("快速选择结果：" + ans1);
                System.out.println("暴力解法结果：" + ans2);
                System.out.print("测试数组：");
                print(arr);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！TopK算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
