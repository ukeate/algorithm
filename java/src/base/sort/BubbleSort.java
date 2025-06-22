package base.sort;

import java.util.Arrays;

/**
 * 冒泡排序算法 - 基础排序算法之一
 * 
 * 算法原理：
 * 1. 比较相邻的两个元素，如果前面的元素比后面的大，就交换它们
 * 2. 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对
 * 3. 这样一轮下来，最大的元素会"冒泡"到数组的最后位置
 * 4. 重复步骤1-3，每次排序的范围减少一个元素，直到没有需要交换的元素
 * 
 * 形象比喻：
 * 就像水中的气泡从水底慢慢浮到水面一样，每轮排序都会有一个最大值"冒泡"到正确位置
 * 
 * 算法特点：
 * - 时间复杂度：O(n²) - 最好、最坏、平均情况都是O(n²)
 * - 空间复杂度：O(1) - 原地排序，只需要常数级别的额外空间
 * - 稳定性：稳定排序 - 相等元素的相对顺序不会改变
 * - 适应性：对于已经部分有序的数组可以提前终止
 * 
 * 适用场景：
 * - 数据量很小的情况
 * - 教学演示排序算法的基本思想
 * - 作为其他复杂算法的子程序
 * 
 * 优缺点：
 * 优点：实现简单，稳定，原地排序
 * 缺点：效率低，不适合大数据量
 */
public class BubbleSort {
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
     * 冒泡排序主算法
     * 
     * 算法步骤详解：
     * 1. 外层循环控制排序的轮数，从最后一个位置开始向前
     * 2. 内层循环进行相邻元素的比较和交换
     * 3. 每轮排序后，最大的元素会被"冒泡"到当前轮的最后位置
     * 4. 下一轮排序的范围会减少一个元素
     * 
     * 举例说明（数组[4,3,2,1]）：
     * 初始: [4, 3, 2, 1]
     * 
     * 第1轮 (j=3):
     * - 比较4和3：[3, 4, 2, 1]
     * - 比较4和2：[3, 2, 4, 1]  
     * - 比较4和1：[3, 2, 1, 4] ← 4冒泡到最后
     * 
     * 第2轮 (j=2):
     * - 比较3和2：[2, 3, 1, 4]
     * - 比较3和1：[2, 1, 3, 4] ← 3冒泡到倒数第二位
     * 
     * 第3轮 (j=1):
     * - 比较2和1：[1, 2, 3, 4] ← 2冒泡到倒数第三位
     * 
     * 第4轮 (j=0):
     * - 无需比较，排序完成
     * 
     * @param arr 待排序的数组
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }

        // 外层循环：控制排序轮数
        // j表示当前轮排序的右边界（最后一个需要排序的位置）
        for (int j = arr.length - 1; j >= 0; j--) {
            // 内层循环：进行相邻元素的比较和交换
            // 从数组开始位置比较到当前轮的右边界
            for (int i = 0; i < j; i++) {
                if (arr[i] > arr[i + 1]) {
                    // 如果前面的元素大于后面的元素，交换它们
                    swap(arr, i, i + 1);
                }
            }
            // 每轮结束后，arr[j]位置上就是当前未排序部分的最大值
        }
    }

    /**
     * 使用Java内置排序进行验证
     * 
     * @param arr 待排序数组
     */
    public static void sortSure(int[] arr) {
        Arrays.sort(arr);
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) (Math.random() * (maxLen + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxVal + 1)) - (int) (Math.random() * (maxVal + 1));
        }
        return arr;
    }

    /**
     * 打印数组内容
     * 
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
     * 测试方法 - 验证冒泡排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 冒泡排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        int[] testCopy = copy(testArr);
        bubbleSort(testCopy);
        System.out.println("排序结果：" + Arrays.toString(testCopy));
        
        // 验证排序正确性
        Arrays.sort(testArr);
        System.out.println("期望结果：" + Arrays.toString(testArr));
        System.out.println("结果正确：" + Arrays.equals(testArr, testCopy));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 1000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(2, 2);  // 小数组避免超时
            int[] arr2 = copy(arr1);
            bubbleSort(arr1);
            sortSure(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("测试失败！");
                print(arr1);
                print(arr2);
                return;
            }
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("所有测试通过！");
        System.out.println("耗时：" + (endTime - startTime) + "ms");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度：");
        System.out.println("- 最好情况：O(n²) - 即使数组已排序，仍需进行所有比较");
        System.out.println("- 最坏情况：O(n²) - 数组完全逆序");
        System.out.println("- 平均情况：O(n²)");
        
        System.out.println("\n空间复杂度：O(1) - 原地排序");
        
        System.out.println("\n稳定性：稳定排序");
        System.out.println("- 相等元素的相对位置不会改变");
        
        System.out.println("\n适用场景：");
        System.out.println("- 小规模数据排序");
        System.out.println("- 教学和理解排序算法");
        System.out.println("- 对稳定性有要求的场景");
        
        System.out.println("\n优化思路：");
        System.out.println("- 可以添加标志位检测是否已经有序，提前结束");
        System.out.println("- 实际应用中通常使用更高效的排序算法");
    }
}
