package base.sort;

import java.util.Arrays;

/**
 * 选择排序算法 - 基础排序算法之一
 * 
 * 算法原理：
 * 1. 从未排序的数组中找到最小（或最大）元素
 * 2. 将其与数组开头的元素交换位置
 * 3. 重复步骤1-2，每次在剩余未排序的元素中找最小值
 * 4. 直到所有元素都排序完毕
 * 
 * 形象比喻：
 * 就像在一堆扑克牌中，每次都选出最小的牌放到已排序部分的末尾
 * 
 * 算法特点：
 * - 时间复杂度：O(n²) - 最好、最坏、平均情况都是O(n²)
 * - 空间复杂度：O(1) - 原地排序，只需要常数级别的额外空间
 * - 稳定性：不稳定排序 - 相等元素的相对顺序可能改变
 * - 交换次数少：最多进行n-1次交换，适合交换成本高的场景
 * 
 * 适用场景：
 * - 数据量很小的情况
 * - 交换操作成本很高的情况
 * - 教学演示选择思想
 * 
 * 优缺点：
 * 优点：实现简单，交换次数少，原地排序
 * 缺点：效率低，不稳定，不适合大数据量
 */
public class SelectionSort {

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
     * 选择排序主算法
     * 
     * 算法步骤详解：
     * 1. 外层循环控制已排序区间的边界
     * 2. 内层循环在未排序区间中找最小值的索引
     * 3. 将找到的最小值与未排序区间的第一个元素交换
     * 
     * 举例说明（数组[64, 25, 12, 22, 11]）：
     * 初始: [64, 25, 12, 22, 11]
     * 
     * 第1轮 (i=0):
     * - 在[64, 25, 12, 22, 11]中找最小值：11 (索引4)
     * - 交换位置0和4：[11, 25, 12, 22, 64]
     * 
     * 第2轮 (i=1):
     * - 在[25, 12, 22, 64]中找最小值：12 (索引2)
     * - 交换位置1和2：[11, 12, 25, 22, 64]
     * 
     * 第3轮 (i=2):
     * - 在[25, 22, 64]中找最小值：22 (索引3)
     * - 交换位置2和3：[11, 12, 22, 25, 64]
     * 
     * 第4轮 (i=3):
     * - 在[25, 64]中找最小值：25 (索引3)
     * - 无需交换：[11, 12, 22, 25, 64]
     * 
     * 完成排序
     * 
     * @param arr 待排序的数组
     */
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }
        
        // 外层循环：控制已排序区间的右边界
        // i表示当前需要确定的位置（已排序区间的下一个位置）
        for (int i = 0; i < arr.length - 1; i++) {
            int idx = i;  // 记录当前找到的最小值索引，初始设为当前位置
            
            // 内层循环：在未排序区间[i+1, arr.length-1]中寻找最小值
            for (int j = i + 1; j < arr.length; j++) {
                // 如果找到更小的元素，更新最小值索引
                idx = arr[j] < arr[idx] ? j : idx;
            }
            
            // 将找到的最小值与当前位置交换
            // 交换后，位置i就确定了正确的元素
            swap(arr, i, idx);
        }
        // 经过n-1轮选择后，数组完全有序
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
     * 测试方法 - 验证选择排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 选择排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {64, 25, 12, 22, 11};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        int[] testCopy = copy(testArr);
        selectionSort(testCopy);
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
        System.out.println("最大数组长度：" + maxLen);
        System.out.println("最大元素值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(2, 2);
            int[] arr2 = copy(arr1);
            selectionSort(arr1);
            sortSure(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.print("我们的结果：");
                print(arr1);
                System.out.print("正确结果：");
                print(arr2);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！选择排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
