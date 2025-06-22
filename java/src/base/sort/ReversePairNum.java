package base.sort;

/**
 * 逆序对数量问题 - 归并排序的经典应用
 * 
 * 问题描述：
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对
 * 输入一个数组，求出这个数组中的逆序对的总数
 * 
 * 例如：数组[7,5,6,4]
 * 逆序对有：(7,5)、(7,6)、(7,4)、(5,4)、(6,4)
 * 总共5个逆序对
 * 
 * 解决思路：
 * 使用归并排序的思想，在合并过程中统计逆序对的数量
 * 关键在于利用归并排序中左右两部分已经有序的特性
 * 
 * 算法原理：
 * 1. 将数组分为左右两部分，递归处理
 * 2. 在合并过程中，当左半部分元素 > 右半部分元素时，
 *    左半部分从当前位置到末尾的所有元素都能与右半部分当前元素构成逆序对
 * 3. 累加逆序对数量
 * 
 * 时间复杂度：O(N*logN) - 归并排序的时间复杂度
 * 空间复杂度：O(N) - 归并排序需要的辅助空间
 * 
 * 应用场景：
 * - 数组有序程度的衡量
 * - 排序算法性能分析
 * - 分治算法的经典应用
 */
public class ReversePairNum {
    
    /**
     * 归并过程中统计逆序对数量
     * 
     * 算法详解：
     * 在标准归并排序的基础上，增加逆序对的统计
     * 核心思想：当arr[p1] > arr[p2]时，左半部分从p1到m的所有元素都能与arr[p2]构成逆序对
     * 
     * 举例说明（左半部分[5,7]，右半部分[4,6]）：
     * - 比较5和4：5>4，左半部分[5,7]都能与4构成逆序对，贡献2个
     * - 比较5和6：5<6，选择5，无逆序对贡献
     * - 比较7和6：7>6，左半部分[7]能与6构成逆序对，贡献1个
     * 总逆序对：2+1=3个，即(5,4)、(7,4)、(7,6)
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 合并过程中产生的逆序对数量
     */
    public static int merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];  // 辅助数组
        int i = 0;
        int p1 = l;      // 左半部分指针
        int p2 = m + 1;  // 右半部分指针
        int ans = 0;     // 累积的逆序对数量
        
        // 合并两个有序部分，同时统计逆序对
        while (p1 <= m && p2 <= r) {
            if (arr[p1] > arr[p2]) {
                // 关键：左半部分从p1到m的所有元素都能与arr[p2]构成逆序对
                // 逆序对数量为：m - p1 + 1
                ans += m - p1 + 1;
                help[i++] = arr[p2++];
            } else {
                // arr[p1] <= arr[p2]，选择左半部分元素，无逆序对贡献
                help[i++] = arr[p1++];
            }
        }
        
        // 处理剩余元素（剩余元素不会产生新的逆序对）
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        
        // 将合并结果复制回原数组
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        
        return ans;
    }

    /**
     * 递归处理函数 - 分治求解逆序对数量
     * 
     * 分治思想：
     * 1. 递归边界：只有一个元素时，逆序对数量为0
     * 2. 分解：将数组分为左右两部分
     * 3. 解决：递归计算左半部分和右半部分的逆序对数量
     * 4. 合并：计算跨越左右两部分的逆序对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]内的逆序对数量
     */
    private static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;  // 只有一个元素，没有逆序对
        }
        
        // 计算中点，避免整数溢出
        int mid = l + ((r - l) >> 1);
        
        // 分治：左半部分 + 右半部分 + 跨越部分
        return process(arr, l, mid) +
               process(arr, mid + 1, r) +
               merge(arr, l, mid, r);
    }

    /**
     * 逆序对数量主函数
     * 
     * @param arr 待处理数组
     * @return 数组中逆序对的数量
     */
    public static int reversePairNum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;  // 空数组或单元素数组没有逆序对
        }
        return process(arr, 0, arr.length - 1);
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 直接双重循环检查每一对(i,j)是否构成逆序对
     * 时间复杂度：O(N²)
     * 
     * @param arr 数组
     * @return 逆序对数量
     */
    private static int reversePairNumSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int ans = 0;
        // 对于每个位置i
        for (int i = 0; i < arr.length; i++) {
            // 检查位置i后面的所有元素
            for (int j = i + 1; j < arr.length; j++) {
                // 如果arr[i] > arr[j]，则构成逆序对
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

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
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 逆序对数量问题测试 ===");
        
        // 手工测试用例
        int[] testArr = {7, 5, 6, 4};
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        
        int[] testCopy1 = copy(testArr);
        int[] testCopy2 = copy(testArr);
        
        int result1 = reversePairNum(testCopy1);
        int result2 = reversePairNumSure(testCopy2);
        
        System.out.println("归并排序解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
        // 详细计算过程展示
        System.out.println("\n详细计算过程：");
        System.out.println("数组：[7, 5, 6, 4]");
        System.out.println("逆序对：");
        System.out.println("(7,5) - 7>5");
        System.out.println("(7,6) - 7>6");
        System.out.println("(7,4) - 7>4");
        System.out.println("(5,4) - 5>4");
        System.out.println("(6,4) - 6>4");
        System.out.println("总计：5个逆序对");
        
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
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            
            int ans1 = reversePairNum(arr1);
            int ans2 = reversePairNumSure(arr2);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("归并排序结果：" + ans1);
                System.out.println("暴力解法结果：" + ans2);
                System.out.print("测试数组：");
                print(arr1);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！逆序对算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
