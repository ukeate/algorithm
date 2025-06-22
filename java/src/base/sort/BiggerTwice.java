package base.sort;

/**
 * 重要反转对问题 - 归并排序的经典应用
 * 
 * 问题描述：
 * 给定一个数组，求有多少个重要反转对
 * 重要反转对定义：对于i < j，如果arr[i] > 2 * arr[j]，则称(i,j)是一个重要反转对
 * 
 * 例如：数组[1,3,2,3,1]
 * 重要反转对有：(1,4) 因为3 > 2*1，(3,4) 因为3 > 2*1
 * 总共2个重要反转对
 * 
 * 解决思路：
 * 使用归并排序的思想，在合并过程中统计反转对的数量
 * 关键在于利用归并排序中左右两部分已经有序的特性
 * 
 * 算法原理：
 * 1. 将数组分为左右两部分，递归处理
 * 2. 在合并过程中，对于左半部分的每个元素arr[i]，
 *    找出右半部分中有多少个元素arr[j]满足arr[i] > 2*arr[j]
 * 3. 由于右半部分已经有序，可以用双指针技巧快速统计
 * 
 * 时间复杂度：O(N*logN) - 归并排序的时间复杂度
 * 空间复杂度：O(N) - 归并排序需要的辅助空间
 * 
 * 应用场景：
 * - LeetCode 493. Reverse Pairs
 * - 统计数组中的特殊逆序对
 * - 分治算法的经典应用
 */
public class BiggerTwice {
    
    /**
     * 归并过程中统计重要反转对
     * 
     * 算法详解：
     * 1. 先统计跨越左右两部分的重要反转对数量
     * 2. 再进行标准的归并排序合并操作
     * 
     * 统计逻辑：
     * - 对于左半部分的每个元素arr[i]，找出右半部分中满足条件的元素个数
     * - 使用双指针rr，因为右半部分有序，所以找到一个位置后，后面的都满足条件
     * - 例如：左半部分[1,3,5]，右半部分[1,2,4]
     *   - 对于1：找出右半部分中<1/2=0.5的元素，没有，贡献0
     *   - 对于3：找出右半部分中<3/2=1.5的元素，有[1]，贡献1
     *   - 对于5：找出右半部分中<5/2=2.5的元素，有[1,2]，贡献2
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 跨越左右两部分的重要反转对数量
     */
    private static int merge(int[] arr, int l, int m, int r) {
        int ans = 0;
        int rr = m + 1;  // 右半部分的指针
        
        // 统计跨越左右两部分的重要反转对
        for (int i = l; i <= m; i++) {
            // 找出右半部分中有多少个元素满足 arr[i] > 2 * arr[j]
            // 即找出有多少个arr[j] < arr[i]/2
            while (rr <= r && arr[i] > (arr[rr] * 2)) {
                rr++;
            }
            // 从m+1到rr-1的所有元素都满足条件
            ans += rr - m - 1;
        }
        
        // 标准的归并排序合并过程
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;      // 左半部分指针
        int p2 = m + 1;  // 右半部分指针
        
        // 合并两个有序部分
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        
        // 处理剩余元素
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
     * 递归处理函数 - 分治求解重要反转对数量
     * 
     * 分治思想：
     * 1. 递归边界：只有一个元素时，反转对数量为0
     * 2. 分解：将数组分为左右两部分
     * 3. 解决：递归计算左半部分和右半部分的反转对数量
     * 4. 合并：计算跨越左右两部分的反转对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]内的重要反转对数量
     */
    private static int process(int[] arr, int l, int r) {
        if (l == r) {
            return 0;  // 只有一个元素，没有反转对
        }
        
        // 计算中点，避免整数溢出
        int mid = l + ((r - l) >> 1);
        
        // 分治：左半部分 + 右半部分 + 跨越部分
        return process(arr, l, mid) + 
               process(arr, mid + 1, r) + 
               merge(arr, l, mid, r);
    }

    /**
     * 重要反转对主函数
     * 
     * @param arr 待处理数组
     * @return 重要反转对的数量
     */
    public static int biggerTwice(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;  // 空数组或单元素数组没有反转对
        }
        return process(arr, 0, arr.length - 1);
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 直接双重循环检查每一对(i,j)是否构成重要反转对
     * 时间复杂度：O(N²)
     * 
     * @param arr 数组
     * @return 重要反转对数量
     */
    private static int biggerTwiceSure(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > (arr[j] << 1)) {  // arr[j] << 1 等价于 2 * arr[j]
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
    public static int[] randomArr(int maxLen, int maxVal) {
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
    public static int[] copy(int[] arr) {
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
    public static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 重要反转对问题测试 ===");
        
        // 手工测试用例
        int[] testArr = {1, 3, 2, 3, 1};
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        
        int[] testCopy1 = copy(testArr);
        int[] testCopy2 = copy(testArr);
        
        int result1 = biggerTwice(testCopy1);
        int result2 = biggerTwiceSure(testCopy2);
        
        System.out.println("归并排序解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
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
            
            int ans1 = biggerTwice(arr1);
            int ans2 = biggerTwiceSure(arr2);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.print("数组：");
                print(arr1);
                System.out.println("归并排序结果：" + ans1);
                System.out.println("暴力解法结果：" + ans2);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！重要反转对算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
