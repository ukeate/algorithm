package base.sort;

import java.util.Arrays;

/**
 * 计数排序算法 - 非比较排序算法
 * 
 * 算法原理：
 * 1. 统计数组中每个值出现的次数
 * 2. 根据统计结果重新构造有序数组
 * 3. 不需要比较元素大小，直接通过计数实现排序
 * 
 * 核心思想：
 * 用空间换时间，通过额外的计数数组来避免元素间的比较操作
 * 
 * 形象比喻：
 * 就像统计选票，先数出每个候选人得了多少票，
 * 然后按票数从少到多重新排列选票
 * 
 * 算法特点：
 * - 时间复杂度：O(n+k) - n是元素个数，k是数据范围
 * - 空间复杂度：O(k) - 需要大小为k的计数数组
 * - 稳定性：稳定排序（如果正确实现）
 * - 局限性：只适用于已知数据范围且范围不大的非负整数
 * 
 * 适用场景：
 * - 数据范围较小的整数排序
 * - 年龄排序、成绩统计等场景
 * - 作为基数排序的子程序
 * 
 * 局限性：
 * - 只能排序非负整数
 * - 数据范围必须已知且不能太大
 * - 空间消耗与数据范围成正比，不与数据量成正比
 * 
 * 优缺点：
 * 优点：时间复杂度线性，稳定排序
 * 缺点：空间消耗大，适用范围有限
 */
public class CountSort {
    
    /**
     * 计数排序主算法
     * 
     * 算法步骤：
     * 1. 找出数组中的最大值，确定计数数组的大小
     * 2. 创建计数数组，统计每个值的出现次数
     * 3. 根据计数数组重新构造有序数组
     * 
     * 举例说明（数组[4, 2, 2, 8, 3, 3, 1]）：
     * 
     * 原数组：[4, 2, 2, 8, 3, 3, 1]
     * 最大值：8
     * 
     * 统计计数：
     * bucket[0] = 0  (值0出现0次)
     * bucket[1] = 1  (值1出现1次)
     * bucket[2] = 2  (值2出现2次)
     * bucket[3] = 2  (值3出现2次)
     * bucket[4] = 1  (值4出现1次)
     * bucket[5] = 0  (值5出现0次)
     * bucket[6] = 0  (值6出现0次)
     * bucket[7] = 0  (值7出现0次)
     * bucket[8] = 1  (值8出现1次)
     * 
     * 重新构造数组：
     * 1出现1次 → [1]
     * 2出现2次 → [1, 2, 2]
     * 3出现2次 → [1, 2, 2, 3, 3]
     * 4出现1次 → [1, 2, 2, 3, 3, 4]
     * 8出现1次 → [1, 2, 2, 3, 3, 4, 8]
     * 
     * @param arr 待排序数组（只支持非负整数）
     */
    public static void countSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }
        
        // 第一步：找出数组中的最大值，确定数据范围
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        
        // 第二步：创建计数数组，大小为max+1（因为要包含0到max的所有值）
        int[] bucket = new int[max + 1];
        
        // 第三步：统计每个值的出现次数
        for (int i = 0; i < arr.length; i++) {
            bucket[arr[i]]++;  // 将值作为索引，次数加1
        }
        
        // 第四步：根据计数数组重新构造有序数组
        int i = 0;  // 原数组的索引
        for (int j = 0; j < bucket.length; j++) {
            // 值j出现bucket[j]次，依次放入原数组
            while (bucket[j]-- > 0) {
                arr[i++] = j;
            }
        }
        // 排序完成，原数组现在是有序的
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
     * 生成随机非负整数数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) (Math.random() * (maxLen + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxVal + 1));  // 只生成非负整数
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
     * 测试方法 - 验证计数排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 计数排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {4, 2, 2, 8, 3, 3, 1};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        int[] testCopy = copy(testArr);
        countSort(testCopy);
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
        System.out.println("注意：计数排序只支持非负整数，且数据范围不能太大");
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(2, 2);
            int[] arr2 = copy(arr1);
            countSort(arr1);
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
            System.out.println("所有测试通过！计数排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
