package basic.c54;

/**
 * 反转操作后的逆序对计算问题
 * 
 * 问题描述：
 * 给定一个长度为2^power的数组arr，按照ops数组中的操作序列进行反转：
 * - 每次操作ops[i]，将数组按2^ops[i]个元素为一组进行组内反转
 * - 返回每次操作后数组中逆序对的数量
 * 
 * 逆序对定义：对于i < j，如果arr[i] > arr[j]，则(i,j)构成一个逆序对
 * 
 * 算法思路：
 * 1. 暴力法：每次操作后重新统计逆序对（O(n²)）
 * 2. 优化法：预计算各层级的逆序对和顺序对，动态交换更新
 * 
 * 时间复杂度：
 * - 暴力法：O(ops.length * n²)
 * - 优化法：O(n*log(n) + ops.length*power)
 * 
 * 空间复杂度：O(n + power)
 * 
 * @author 算法学习
 */
public class MergeRecord {
    
    /**
     * 反转数组指定区间的元素
     * 
     * @param arr 目标数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     */
    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            int tmp = arr[l];
            arr[l++] = arr[r];
            arr[r--] = tmp;
        }
    }

    /**
     * 按指定大小k对数组进行分组反转
     * 将数组每k个元素为一组，对每组内部进行反转
     * 
     * @param arr 目标数组
     * @param k 分组大小
     */
    private static void reverse(int[] arr, int k) {
        if (k < 2) {
            return;  // 分组大小小于2时无需反转
        }
        
        // 每k个元素为一组进行反转
        for (int i = 0; i < arr.length; i += k) {
            reverse(arr, i, i + k - 1);
        }
    }

    /**
     * 暴力统计数组中的逆序对数量
     * 
     * @param arr 输入数组
     * @return 逆序对的数量
     */
    private static int count(int[] arr) {
        int ans = 0;
        
        // 双重循环暴力统计
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        
        return ans;
    }

    /**
     * 方法1：暴力解法
     * 每次操作后重新统计逆序对数量
     * 
     * @param arr 输入数组
     * @param ops 操作序列
     * @param power 数组长度的幂次（数组长度为2^power）
     * @return 每次操作后的逆序对数量数组
     */
    public static int[] reversePair1(int[] arr, int[] ops, int power) {
        int[] ans = new int[ops.length];
        
        for (int i = 0; i < ops.length; i++) {
            // 执行反转操作：按2^ops[i]个元素为一组反转
            reverse(arr, 1 << ops[i]);
            
            // 统计操作后的逆序对数量
            ans[i] = count(arr);
        }
        
        return ans;
    }

    /**
     * 复制数组的辅助方法
     * 
     * @param arr 源数组
     * @return 复制后的数组
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
     * 归并过程，统计跨区间的逆序对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中间位置
     * @param r 右边界
     * @return 跨区间的逆序对数量
     */
    private static int merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;      // 左半部分指针
        int p2 = m + 1;  // 右半部分指针
        int ans = 0;     // 逆序对计数
        
        // 归并过程中统计逆序对
        while (p1 <= m && p2 <= r) {
            // 如果左边元素大于右边元素，产生逆序对
            ans += arr[p1] > arr[p2] ? (m - p1 + 1) : 0;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        
        // 处理剩余元素
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        
        // 将排序结果复制回原数组
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        
        return ans;
    }

    /**
     * 递归计算各层级的逆序对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param power 当前层级
     * @param record 记录各层级逆序对数量的数组
     */
    private static void process(int[] arr, int l, int r, int power, int[] record) {
        if (l == r) {
            return;  // 递归边界
        }
        
        int mid = l + ((r - l) >> 1);
        
        // 递归处理左右子区间
        process(arr, l, mid, power - 1, record);
        process(arr, mid + 1, r, power - 1, record);
        
        // 统计当前层级的跨区间逆序对
        record[power] += merge(arr, l, mid, r);
    }

    /**
     * 方法2：优化解法
     * 预计算各层级的逆序对和顺序对，通过交换实现快速更新
     * 
     * @param arr 输入数组
     * @param ops 操作序列
     * @param power 数组长度的幂次
     * @return 每次操作后的逆序对数量数组
     */
    public static int[] reversePair2(int[] arr, int[] ops, int power) {
        // 创建反转后的数组用于计算顺序对
        int[] arr2 = copy(arr);
        reverse(arr2, 0, arr2.length - 1);
        
        // 预计算各层级的逆序对和顺序对数量
        int[] desc = new int[power + 1];  // 各层级逆序对数量
        int[] asc = new int[power + 1];   // 各层级顺序对数量
        
        process(arr, 0, arr.length - 1, power, desc);
        process(arr2, 0, arr2.length - 1, power, asc);
        
        int[] ans = new int[ops.length];
        
        // 处理每个操作
        for (int i = 0; i < ops.length; i++) {
            int curPower = ops[i];
            
            // 反转操作影响的层级：1到curPower
            // 对于这些层级，逆序对和顺序对需要交换
            for (int p = 1; p <= curPower; p++) {
                int tmp = desc[p];
                desc[p] = asc[p];
                asc[p] = tmp;
            }
            
            // 累计所有层级的逆序对数量
            for (int p = 1; p <= power; p++) {
                ans[i] += desc[p];
            }
        }
        
        return ans;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param power 数组长度的幂次
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int power, int maxVal) {
        int[] ans = new int[1 << power];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (maxVal * Math.random());
        }
        return ans;
    }

    /**
     * 生成随机操作序列用于测试
     * 
     * @param len 操作序列长度
     * @param power 最大幂次
     * @return 随机操作序列
     */
    private static int[] randomOps(int len, int power) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((power + 1) * Math.random());
        }
        return ans;
    }

    /**
     * 比较两个数组是否相等
     * 
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 是否相等
     */
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null ^ arr2 == null) {
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
     * 测试方法：验证两种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 反转操作逆序对问题测试 ===");
        
        // 正确性测试
        int times = 50000;
        int maxPower = 8;
        int maxOpsLen = 10;
        int maxVal = 30;
        
        System.out.println("正确性测试开始...");
        boolean allPassed = true;
        
        for (int i = 0; i < times; i++) {
            int power = (int) (maxPower * Math.random()) + 1;
            int opsLen = (int) (maxOpsLen * Math.random()) + 1;
            
            int[] arr = randomArr(power, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] ops = randomOps(opsLen, power);
            int[] ops1 = copy(ops);
            int[] ops2 = copy(ops);
            
            int[] ans1 = reversePair1(arr1, ops1, power);
            int[] ans2 = reversePair2(arr2, ops2, power);
            
            if (!isEqual(ans1, ans2)) {
                System.out.println("测试失败！");
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("正确性测试通过！共测试 " + times + " 个用例");
        }
        
        // 性能测试
        System.out.println("\n性能测试开始...");
        maxPower = 20;
        maxOpsLen = 1000000;
        maxVal = 1000;
        
        int[] arr = randomArr(maxPower, maxVal);
        int[] ops = randomOps(maxOpsLen, maxPower);
        
        long start = System.currentTimeMillis();
        reversePair2(arr, ops, maxPower);
        long end = System.currentTimeMillis();
        
        System.out.println("优化算法运行时间: " + (end - start) + " ms");
        System.out.println("数组长度: 2^" + maxPower + " = " + (1 << maxPower));
        System.out.println("操作次数: " + maxOpsLen);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("暴力法 - 时间复杂度: O(ops.length * n²)");
        System.out.println("优化法 - 时间复杂度: O(n*log(n) + ops.length*power)");
        System.out.println("核心思想: 预计算 + 分治 + 动态更新");
        System.out.println("关键优化: 将反转操作转化为逆序对与顺序对的交换");
    }
}

