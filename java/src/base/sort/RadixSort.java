package base.sort;

import java.util.Arrays;

/**
 * 基数排序算法 - 非比较排序算法
 * 
 * 算法原理：
 * 1. 将所有待排序数字统一为同样的数位长度，不足的前面补零
 * 2. 从最低位开始，依次进行一次排序
 * 3. 从最低位到最高位，完成若干次按位排序后，整个序列就变成有序序列
 * 
 * 核心思想：
 * 按照数字的每一位进行排序，利用计数排序的稳定性，
 * 从低位到高位依次排序，最终得到完全有序的数组
 * 
 * 形象比喻：
 * 就像整理学生的学号，先按个位数分组，再按十位数分组，
 * 最后按百位数分组，每次分组都保持之前的相对顺序
 * 
 * 算法特点：
 * - 时间复杂度：O(d*(n+k)) - d是数字位数，k是每位的取值范围（0-9）
 * - 空间复杂度：O(n+k) - 需要额外的辅助数组
 * - 稳定性：稳定排序 - 相等元素的相对顺序不会改变
 * - 局限性：只能排序非负整数，且数字位数不能太大
 * 
 * 适用场景：
 * - 排序非负整数
 * - 数字位数较少的情况
 * - 需要稳定排序的场合
 * - 数据量很大，但数字范围相对较小
 * 
 * 优缺点：
 * 优点：时间复杂度低（在合适场景下），稳定排序
 * 缺点：只能排序非负整数，空间消耗较大，适用范围有限
 */
public class RadixSort {
    
    /**
     * 获取数字x的第d位数字（从右往左，从1开始计数）
     * 
     * 例如：getDigit(1234, 1) = 4（个位）
     *      getDigit(1234, 2) = 3（十位）
     *      getDigit(1234, 3) = 2（百位）
     *      getDigit(1234, 4) = 1（千位）
     * 
     * @param x 要获取位数的数字
     * @param d 第几位（从右往左，从1开始）
     * @return 第d位的数字（0-9）
     */
    private static int getDigit(int x, int d) {
        return ((x / ((int) Math.pow(10, d - 1))) % 10);
    }

    /**
     * 基数排序的核心实现
     * 
     * 算法步骤：
     * 1. 对于每一位数字（从个位到最高位）
     * 2. 统计每个数字（0-9）出现的次数
     * 3. 计算累积次数，确定每个数字在结果中的位置范围
     * 4. 从右到左遍历原数组，将元素放到正确位置（保证稳定性）
     * 5. 将排序结果复制回原数组
     * 
     * 举例说明（数组[170, 45, 75, 90, 2, 802, 24, 66]，按个位排序）：
     * 
     * 原数组：[170, 45, 75, 90, 2, 802, 24, 66]
     * 个位数：[0,   5,  5,  0,  2, 2,   4,  6]
     * 
     * 统计个位数出现次数：
     * count[0]=2, count[1]=0, count[2]=2, count[3]=0, count[4]=1, count[5]=2, count[6]=1, count[7]=0, count[8]=0, count[9]=0
     * 
     * 计算累积次数（确定位置范围）：
     * count[0]=2, count[1]=2, count[2]=4, count[3]=4, count[4]=5, count[5]=7, count[6]=8, count[7]=8, count[8]=8, count[9]=8
     * 
     * 按个位排序结果：[170, 90, 2, 802, 24, 45, 75, 66]
     * 
     * @param arr 待排序数组
     * @param l 左边界
     * @param r 右边界
     * @param digit 最大数字的位数
     */
    private static void radixSort(int[] arr, int l, int r, int digit) {
        final int radix = 10;  // 十进制基数
        int[] help = new int[r - l + 1];  // 辅助数组，存储排序结果
        
        // 对每一位进行排序（从个位开始到最高位）
        for (int d = 1; d <= digit; d++) {
            // 计数数组，统计当前位每个数字（0-9）出现的次数
            int[] count = new int[radix];
            
            // 统计当前位每个数字的出现次数
            for (int i = l; i <= r; i++) {
                int j = getDigit(arr[i], d);  // 获取当前位的数字
                count[j]++;
            }
            
            // 计算累积次数，count[i]表示 ≤ i 的数字总共有多少个
            // 这样可以确定每个数字在排序结果中的位置范围
            for (int i = 1; i < radix; i++) {
                count[i] = count[i] + count[i - 1];
            }
            
            // 从右向左遍历原数组，将元素放入正确位置
            // 从右向左是为了保证排序的稳定性
            for (int i = r; i >= l; i--) {
                int j = getDigit(arr[i], d);    // 获取当前位的数字
                help[count[j] - 1] = arr[i];    // 放入对应位置
                count[j]--;                     // 更新计数
            }
            
            // 将排序结果复制回原数组
            for (int i = l, j = 0; i <= r; i++, j++) {
                arr[i] = help[j];
            }
        }
    }

    /**
     * 计算数组中最大数的位数
     * 
     * 例如：maxDigit([1, 23, 456]) = 3（因为456有3位）
     * 
     * @param arr 数组
     * @return 最大数的位数
     */
    private static int maxDigit(int[] arr) {
        int max = Integer.MIN_VALUE;
        
        // 找到数组中的最大值
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        
        // 计算最大值的位数
        int res = 0;
        while (max != 0) {
            res++;
            max /= 10;
        }
        return res;
    }

    /**
     * 基数排序的主方法
     * 
     * @param arr 待排序数组（只支持非负整数）
     */
    public static void radixSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;  // 空数组或单元素数组无需排序
        }
        radixSort(arr, 0, arr.length - 1, maxDigit(arr));
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
     * 测试方法 - 验证基数排序的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 基数排序算法测试 ===");
        
        // 手工测试用例
        int[] testArr = {170, 45, 75, 90, 2, 802, 24, 66};
        System.out.println("测试数组：" + Arrays.toString(testArr));
        
        int[] testCopy = copy(testArr);
        radixSort(testCopy);
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
        System.out.println("注意：基数排序只支持非负整数");
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(2, 2);
            int[] arr2 = copy(arr1);
            radixSort(arr1);
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
            System.out.println("所有测试通过！基数排序算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
