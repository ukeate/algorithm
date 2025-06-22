package base.sort;

import java.util.Arrays;

/**
 * 希尔排序算法 - 插入排序的高效改进版本
 * 
 * 算法原理：
 * 1. 将数组按照一定的间隔（增量）分组，对每组使用插入排序
 * 2. 逐步缩小间隔，重复分组排序过程
 * 3. 最后间隔为1时，相当于对整个数组进行插入排序
 * 
 * 核心思想：
 * 通过多次"粗调"（大间隔）让数组接近有序，最后"细调"（间隔1）完成排序
 * 这样可以显著减少插入排序中的元素移动次数
 * 
 * 形象比喻：
 * 就像盖房子，先搭好框架（大间隔排序），再精装修（小间隔排序），
 * 最后刷漆收尾（间隔1的插入排序）
 * 
 * 算法特点：
 * - 时间复杂度：依赖于间隔序列的选择
 *   * 希尔增量：O(n²)
 *   * Knuth增量：O(n^(3/2))
 *   * 最优增量：O(n log²n)
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：不稳定排序 - 远距离交换可能改变相等元素的相对顺序
 * - 自适应性：对部分有序的数组表现良好
 * 
 * 适用场景：
 * - 中等规模数据排序（几千到几万个元素）
 * - 对稳定性要求不高的场合
 * - 内存受限的环境（原地排序）
 * 
 * 优缺点：
 * 优点：比简单插入排序快得多，原地排序，实现相对简单
 * 缺点：不稳定，增量序列的选择影响性能，理论分析复杂
 */
public class ShellSort {
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
     * 希尔排序主算法 - 使用预定义的增量序列
     * 
     * 使用固定的增量序列：{5, 2, 1}
     * 这是一个简单的增量序列，最后必须以1结尾
     * 
     * 算法步骤：
     * 1. 按照增量序列{5, 2, 1}依次进行分组排序
     * 2. 对于每个增量，将数组分为若干子序列
     * 3. 对每个子序列使用插入排序
     * 
     * 举例说明（数组[9, 5, 1, 4, 3, 8, 6, 2, 7]，增量序列{5, 2, 1}）：
     * 
     * 原数组：[9, 5, 1, 4, 3, 8, 6, 2, 7]  (索引: 0, 1, 2, 3, 4, 5, 6, 7, 8)
     * 
     * 步长5分组：
     * - 组1: [9(0), 8(5)] → [8, 9]
     * - 组2: [5(1), 6(6)] → [5, 6]  
     * - 组3: [1(2), 2(7)] → [1, 2]
     * - 组4: [4(3), 7(8)] → [4, 7]
     * - 组5: [3(4)]
     * 结果：[8, 5, 1, 4, 3, 9, 6, 2, 7]
     * 
     * 步长2分组：
     * - 组1: [8(0), 1(2), 3(4), 6(6), 7(8)] → [1, 3, 6, 7, 8]
     * - 组2: [5(1), 4(3), 9(5), 2(7)] → [2, 4, 5, 9]
     * 结果：[1, 2, 3, 4, 6, 5, 7, 9, 8]
     * 
     * 步长1（标准插入排序）：
     * 结果：[1, 2, 3, 4, 5, 6, 7, 8, 9]
     * 
     * @param arr 待排序的数组
     */
    public static void shellSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }
        
        // 预定义的增量序列，必须以1结尾确保最终完全排序
        int[] step = {5, 2, 1};
        
        // 依次使用不同的增量进行分组排序
        for (int s = 0; s < step.length; s++) {
            int currentStep = step[s];
            
            // 对于当前增量，从第currentStep个元素开始处理
            for (int i = currentStep; i < arr.length; i++) {
                // 对当前元素在其所属的子序列中进行插入排序
                // j -= currentStep 表示在间隔为currentStep的子序列中向前移动
                for (int j = i - currentStep; j >= 0 && arr[j] > arr[j + currentStep]; j -= currentStep) {
                    // 在子序列中交换元素，实现插入排序
                    swap(arr, j, j + currentStep);
                }
            }
        }
    }

    /**
     * 标准插入排序 - 用于比较和验证
     * 
     * @param arr 待排序数组
     */
    private static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }

    /**
     * 希尔排序算法 - 使用动态增量序列
     * 
     * 使用 Knuth 增量序列的简化版本：h = n/2, h = h/2, ..., h = 1
     * 这种增量序列在实践中表现良好
     * 
     * 算法特点：
     * - 增量序列是动态计算的，适应不同大小的数组
     * - 每次增量减半，保证覆盖所有可能的分组情况
     * - 时间复杂度大约为 O(n^1.5)
     * 
     * @param arr 待排序的数组
     */
    public static void shellSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }
        
        // 使用动态增量序列：数组长度的一半开始，每次除以2
        for (int step = arr.length / 2; step > 0; step /= 2) {
            // 对于当前增量，进行分组插入排序
            for (int i = step; i < arr.length; i++) {
                // 在当前增量的子序列中进行插入排序
                for (int j = i - step; j >= 0 && arr[j] > arr[j + step]; j -= step) {
                    swap(arr, j, j + step);
                }
            }
        }
        // 当step变为0时循环结束，此时数组已完全排序
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
     * 测试方法 - 验证希尔排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 希尔排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {9, 5, 1, 4, 3, 8, 6, 2, 7};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        // 测试shellSort方法
        int[] testCopy1 = Arrays.copyOf(testArr, testArr.length);
        shellSort(testCopy1);
        System.out.println("shellSort结果：" + Arrays.toString(testCopy1));
        
        // 测试shellSort2方法
        int[] testCopy2 = Arrays.copyOf(testArr, testArr.length);
        shellSort2(testCopy2);
        System.out.println("shellSort2结果：" + Arrays.toString(testCopy2));
        
        // 验证排序正确性
        Arrays.sort(testArr);
        System.out.println("期望结果：" + Arrays.toString(testArr));
        System.out.println("shellSort正确：" + Arrays.equals(testArr, testCopy1));
        System.out.println("shellSort2正确：" + Arrays.equals(testArr, testCopy2));
        
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
            shellSort2(arr1);  // 测试动态增量版本
            insertionSort(arr2);  // 使用插入排序作为基准
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
            System.out.println("所有测试通过！希尔排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
