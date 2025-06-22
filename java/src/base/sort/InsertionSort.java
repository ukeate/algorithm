package base.sort;

import java.util.Arrays;

/**
 * 插入排序算法 - 基础排序算法之一
 * 
 * 算法原理：
 * 1. 将数组分为已排序和未排序两部分，初始时已排序部分只有第一个元素
 * 2. 从未排序部分取出第一个元素，在已排序部分找到合适位置插入
 * 3. 重复步骤2，直到未排序部分为空
 * 
 * 形象比喻：
 * 就像整理扑克牌，左手已经有一些有序的牌，右手每次拿一张牌，
 * 找到合适的位置插入到左手已有序的牌中
 * 
 * 算法特点：
 * - 时间复杂度：
 *   * 最好情况：O(n) - 数组已经有序时
 *   * 最坏情况：O(n²) - 数组逆序时
 *   * 平均情况：O(n²)
 * - 空间复杂度：O(1) - 原地排序，只需要常数级别的额外空间
 * - 稳定性：稳定排序 - 相等元素的相对顺序不会改变
 * - 自适应性：对部分有序的数组表现良好
 * 
 * 适用场景：
 * - 小规模数据排序（通常n < 50）
 * - 数据已经部分有序的情况
 * - 作为快速排序等高级算法的优化子程序
 * - 在线排序（数据逐个到达时）
 * 
 * 优缺点：
 * 优点：实现简单，稳定，自适应，原地排序，在线算法
 * 缺点：对于大规模随机数据效率低
 */
public class InsertionSort {
    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 插入排序主算法
     * 
     * 算法步骤详解：
     * 1. 从第二个元素开始（索引1），依次处理每个元素
     * 2. 对于当前元素，向左比较并交换，直到找到合适位置
     * 3. 重复步骤1-2，直到所有元素处理完毕
     * 
     * 举例说明（数组[5, 2, 4, 6, 1, 3]）：
     * 初始: [5, 2, 4, 6, 1, 3]  (已排序部分：[5])
     * 
     * 第1轮 (j=1, 处理元素2):
     * - 2 < 5，交换：[2, 5, 4, 6, 1, 3]  (已排序部分：[2, 5])
     * 
     * 第2轮 (j=2, 处理元素4):
     * - 4 < 5，交换：[2, 4, 5, 6, 1, 3]
     * - 4 > 2，停止    (已排序部分：[2, 4, 5])
     * 
     * 第3轮 (j=3, 处理元素6):
     * - 6 > 5，无需移动  (已排序部分：[2, 4, 5, 6])
     * 
     * 第4轮 (j=4, 处理元素1):
     * - 1 < 6，交换：[2, 4, 5, 1, 6, 3]
     * - 1 < 5，交换：[2, 4, 5, 1, 6, 3]
     * - 1 < 4，交换：[2, 4, 1, 5, 6, 3]
     * - 1 < 2，交换：[2, 1, 4, 5, 6, 3]
     * - 1 < 1，停止    (已排序部分：[2, 1, 4, 5, 6])
     * 
     * 第5轮 (j=5, 处理元素3):
     * - 3 < 6，交换：[2, 1, 4, 5, 3, 6]
     * - 3 < 5，交换：[2, 1, 4, 3, 5, 6]
     * - 3 < 4，交换：[2, 1, 3, 4, 5, 6]
     * - 3 > 2，停止    (已排序部分：[2, 1, 3, 4, 5, 6])
     * 
     * 排序完成：[1, 2, 3, 4, 5, 6]
     * 
     * @param arr 待排序的数组
     */
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }

        // 外层循环：从第二个元素开始，依次处理每个元素
        // j表示当前要插入到已排序部分的元素位置
        for (int j = 1; j < arr.length; j++) {
            // 内层循环：将当前元素向左移动到合适位置
            // 条件：i > 0 确保不越界，arr[i-1] > arr[i] 确保需要交换
            for (int i = j; i > 0 && arr[i - 1] > arr[i]; i--) {
                // 交换相邻的两个元素，让较小的元素向左移动
                swap(arr, i - 1, i);
            }
            // 当前元素已经找到合适位置，继续处理下一个元素
        }
    }

    /**
     * 打印数组内容
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param size 数组大小范围
     * @param val 元素值范围
     * @return 随机数组
     */
    private static int[] generateRandom(int size, int val) {
        int[] arr = new int[(int) ((size + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((val + 1) * Math.random()) - (int) (val * Math.random());
        }
        return arr;
    }

    /**
     * 判断两个数组是否相等
     * 
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
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
     * 测试方法 - 验证插入排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 插入排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {5, 2, 4, 6, 1, 3};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        int[] testCopy = Arrays.copyOf(testArr, testArr.length);
        insertionSort(testCopy);
        System.out.println("排序结果：" + Arrays.toString(testCopy));
        
        // 验证排序正确性
        Arrays.sort(testArr);
        System.out.println("期望结果：" + Arrays.toString(testArr));
        System.out.println("结果正确：" + Arrays.equals(testArr, testCopy));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 500000;
        int maxSize = 100;
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        System.out.println("最大数组长度：" + maxSize);
        System.out.println("最大元素值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr1 = generateRandom(maxSize, maxVal);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            insertionSort(arr1);
            Arrays.sort(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.print("我们的结果：");
                print(arr1);
                System.out.println();
                System.out.print("正确结果：");
                print(arr2);
                System.out.println();
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！插入排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
