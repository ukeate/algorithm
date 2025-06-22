package base.sort;

/**
 * 小和问题 - 归并排序的经典应用
 * 
 * 问题描述：
 * 在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和
 * 
 * 例如：数组[1,3,4,2,5]
 * - 1左边比1小的数：无，贡献0
 * - 3左边比3小的数：1，贡献1
 * - 4左边比4小的数：1,3，贡献1+3=4
 * - 2左边比2小的数：1，贡献1
 * - 5左边比5小的数：1,3,4,2，贡献1+3+4+2=10
 * 小和 = 0+1+4+1+10 = 16
 * 
 * 解决思路：
 * 转换思维：不计算每个数左边比它小的数，而是计算每个数能作为多少个数的"左边较小数"
 * 即对于每个数，统计它右边有多少个比它大的数，然后将该数乘以这个数量
 * 
 * 算法原理：
 * 使用归并排序，在合并过程中统计小和
 * 1. 当左半部分元素 < 右半部分元素时，左半部分元素可以与右半部分剩余所有元素配对
 * 2. 累加左半部分元素 * 右半部分剩余元素个数
 * 
 * 时间复杂度：O(N*logN) - 归并排序的时间复杂度
 * 空间复杂度：O(N) - 归并排序需要的辅助空间
 * 
 * 应用场景：
 * - 数组逆序对的变种问题
 * - 分治算法的经典应用
 * - 统计类问题的优化解法
 */
public class SmallSum {
    
    /**
     * 归并过程中计算小和
     * 
     * 算法详解：
     * 在标准归并排序的基础上，增加小和的计算
     * 核心思想：当arr[p1] < arr[p2]时，arr[p1]可以与右半部分从p2到r的所有元素配对
     * 
     * 举例说明（左半部分[1,3]，右半部分[2,4]）：
     * - 比较1和2：1<2，1可以与[2,4]配对，贡献1*(4-2+1)=1*2=2
     * - 比较3和2：3>2，选择2，无贡献
     * - 比较3和4：3<4，3可以与[4]配对，贡献3*(4-4+1)=3*1=3
     * 总贡献：2+3=5
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 合并过程中产生的小和
     */
    public static int merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];  // 辅助数组
        int i = 0;
        int p1 = l;      // 左半部分指针
        int p2 = m + 1;  // 右半部分指针
        int ans = 0;     // 累积的小和
        
        // 合并两个有序部分，同时计算小和
        while (p1 <= m && p2 <= r) {
            if (arr[p1] < arr[p2]) {
                // 关键：arr[p1]可以与右半部分从p2到r的所有元素配对
                // 右半部分剩余元素个数为：r - p2 + 1
                ans += (r - p2 + 1) * arr[p1];
                help[i++] = arr[p1++];
            } else {
                // arr[p1] >= arr[p2]，选择右半部分元素，无小和贡献
                help[i++] = arr[p2++];
            }
        }
        
        // 处理剩余元素（剩余元素不会产生新的小和）
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
     * 递归处理函数 - 分治求解小和
     * 
     * 分治思想：
     * 1. 递归边界：只有一个元素时，小和为0
     * 2. 分解：将数组分为左右两部分
     * 3. 解决：递归计算左半部分和右半部分的小和
     * 4. 合并：计算跨越左右两部分的小和贡献
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]内的小和
     */
    private static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;  // 只有一个元素，小和为0
        }
        
        // 计算中点，避免整数溢出
        int mid = l + ((r - l) >> 1);
        
        // 分治：左半部分小和 + 右半部分小和 + 跨越部分小和
        return process(arr, l, mid) +
               process(arr, mid + 1, r) +
               merge(arr, l, mid, r);
    }

    /**
     * 小和问题主函数
     * 
     * @param arr 待处理数组
     * @return 数组的小和
     */
    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;  // 空数组或单元素数组的小和为0
        }
        return process(arr, 0, arr.length - 1);
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 直接按照定义计算：对于每个位置，累加其左边所有比它小的数
     * 时间复杂度：O(N²)
     * 
     * @param arr 数组
     * @return 数组的小和
     */
    private static int smallSumSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int ans = 0;
        // 对于每个位置i（从1开始，因为第0个位置左边没有元素）
        for (int i = 1; i < arr.length; i++) {
            // 检查位置i左边的所有元素
            for (int j = 0; j < i; j++) {
                // 如果左边元素小于当前元素，累加到小和中
                ans += arr[j] < arr[i] ? arr[j] : 0;
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
        System.out.println("=== 小和问题测试 ===");
        
        // 手工测试用例
        int[] testArr = {1, 3, 4, 2, 5};
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        
        int[] testCopy1 = copy(testArr);
        int[] testCopy2 = copy(testArr);
        
        int result1 = smallSum(testCopy1);
        int result2 = smallSumSure(testCopy2);
        
        System.out.println("归并排序解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
        // 详细计算过程展示
        System.out.println("\n详细计算过程：");
        int[] demo = {1, 3, 4, 2, 5};
        System.out.println("数组：[1, 3, 4, 2, 5]");
        System.out.println("1左边比1小的数：无，贡献0");
        System.out.println("3左边比3小的数：1，贡献1");
        System.out.println("4左边比4小的数：1,3，贡献1+3=4");
        System.out.println("2左边比2小的数：1，贡献1");
        System.out.println("5左边比5小的数：1,3,4,2，贡献1+3+4+2=10");
        System.out.println("小和 = 0+1+4+1+10 = 16");
        
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
            
            int ans1 = smallSum(arr1);
            int ans2 = smallSumSure(arr2);
            
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
            System.out.println("所有测试通过！小和算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
