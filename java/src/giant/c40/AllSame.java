package giant.c40;

/**
 * 数组元素联动变化问题
 * 
 * 问题描述：
 * 给定一个数组arr，你可以选择一个下标进行操作，一旦选定了下标，以后都只能在这个下标上操作
 * 当你改变某个下标的值时，所有与该值相同且位置连续的元素都会一起变化（联动效果）
 * 目标是让整个数组的所有元素变成同一个值，求最少的变化次数
 * 
 * 示例：
 * arr = {3,1,2,4}，选择下标1（值为1）
 * 1. 将下标1的值1变成2 → {3,2,2,4}（下标1,2联动）
 * 2. 将下标1的值2变成3 → {3,3,3,4}（下标0,1,2联动）
 * 3. 将下标1的值3变成4 → {4,4,4,4}（下标0,1,2,3联动）
 * 总共变化3次
 * 
 * 问题约束：
 * - arr长度 ≤ 200
 * - arr中的值 ≤ 10^6
 * 
 * 解题思路：
 * 使用递归+DFS遍历所有可能的操作序列：
 * 1. 选择一个起始下标
 * 2. 递归模拟联动扩展过程
 * 3. 每次选择左边界或右边界的值进行扩展
 * 4. 直到整个数组变成同一值
 * 
 * 算法核心：
 * - 联动规则：相同值且位置连续的元素会一起变化
 * - 扩展策略：每次选择边界元素的值进行扩展
 * - 最优选择：左右扩展中选择代价最小的
 * 
 * 时间复杂度：O(2^n)，最坏情况下需要尝试所有可能的操作序列
 * 空间复杂度：O(n)，递归栈深度
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class AllSame {

    /**
     * 递归求解最少变化次数（方法1）
     * 
     * 算法思路：
     * 1. 根据当前中心值midV，找到连续相同值的范围[left+1, right-1]
     * 2. 如果范围覆盖整个数组，返回0（任务完成）
     * 3. 否则选择左边界或右边界的值进行下一次扩展
     * 4. 递归计算两种选择的最小代价
     * 
     * @param arr 原数组
     * @param left 当前连续区间的左边界（不包含）
     * @param midV 当前连续区间的值
     * @param right 当前连续区间的右边界（不包含）
     * @return 使数组全部变成同一值的最少变化次数
     */
    private static int process1(int[] arr, int left, int midV, int right) {
        // 向左扩展：找到所有与midV相同的连续元素
        for (; left >= 0 && arr[left] == midV; ) {
            left--;
        }
        
        // 向右扩展：找到所有与midV相同的连续元素
        for (; right < arr.length && arr[right] == midV; ) {
            right++;
        }
        
        // 如果整个数组都变成了midV，任务完成
        if (left == -1 && right == arr.length) {
            return 0;
        }
        
        // 尝试选择左边界的值进行扩展
        int p1 = Integer.MAX_VALUE;
        if (left >= 0) {
            p1 = process1(arr, left - 1, arr[left], right) + 1;
        }
        
        // 尝试选择右边界的值进行扩展
        int p2 = Integer.MAX_VALUE;
        if (right < arr.length) {
            p2 = process1(arr, left, arr[right], right + 1) + 1;
        }
        
        return Math.min(p1, p2);
    }

    /**
     * 计算最少变化次数（方法1入口）
     * 
     * 算法思路：
     * 尝试每个下标作为起始操作位置，选择最优的起始位置
     * 
     * @param arr 输入数组
     * @return 最少变化次数
     */
    public static int min1(int[] arr) {
        int ans = Integer.MAX_VALUE;
        
        // 尝试每个位置作为起始操作位置
        for (int i = 0; i < arr.length; i++) {
            ans = Math.min(ans, process1(arr, i - 1, arr[i], i + 1));
        }
        
        return ans;
    }

    /**
     * 递归求解最少变化次数（方法2，优化版）
     * 
     * 算法思路：
     * 通过isLeft标记来决定使用哪个边界的值进行扩展
     * 这种方式避免了重复的边界值查找
     * 
     * @param arr 原数组
     * @param l 左边界位置
     * @param isLeft 是否使用左边界的值（true：使用arr[l+1]，false：使用arr[r-1]）
     * @param r 右边界位置
     * @return 最少变化次数
     */
    private static int process2(int[] arr, int l, boolean isLeft, int r) {
        int left = l;
        int targetVal = isLeft ? arr[l + 1] : arr[r - 1];
        
        // 向左扩展所有相同值
        for (; left >= 0 && arr[left] == targetVal; ) {
            left--;
        }
        
        int right = r;
        // 向右扩展所有相同值
        for (; right < arr.length && arr[right] == targetVal; ) {
            right++;
        }
        
        // 整个数组都变成同一值
        if (left == -1 && right == arr.length) {
            return 0;
        }
        
        // 选择左边界扩展
        int p1 = Integer.MAX_VALUE;
        if (left >= 0) {
            p1 = process2(arr, left - 1, true, right) + 1;
        }
        
        // 选择右边界扩展
        int p2 = Integer.MAX_VALUE;
        if (right < arr.length) {
            p2 = process2(arr, left, false, right + 1) + 1;
        }
        
        return Math.min(p1, p2);
    }

    /**
     * 计算最少变化次数（方法2入口）
     * 
     * @param arr 输入数组
     * @return 最少变化次数
     */
    public static int min2(int[] arr) {
        int ans = Integer.MAX_VALUE;
        
        // 尝试每个位置作为起始操作位置
        for (int i = 0; i < arr.length; i++) {
            ans = Math.min(ans, process2(arr, i - 1, true, i + 1));
        }
        
        return ans;
    }

    /**
     * 生成随机测试数组
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int size = 2 + (int) (Math.random() * maxLen);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = 1 + (int) (Math.random() * maxLen);
        }
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数组元素联动变化问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：题目示例");
        int[] arr1 = {3, 1, 2, 4};
        int result1 = min1(arr1);
        System.out.println("数组: [3, 1, 2, 4]");
        System.out.println("最少变化次数: " + result1);
        System.out.println("分析: 选择下标1，1→2→3→4，需要3次变化");
        System.out.println();
        
        // 测试用例2：已经相同的数组
        System.out.println("测试用例2：已经相同的数组");
        int[] arr2 = {5, 5, 5, 5};
        int result2 = min1(arr2);
        System.out.println("数组: [5, 5, 5, 5]");
        System.out.println("最少变化次数: " + result2);
        System.out.println("分析: 数组已经全部相同，不需要变化");
        System.out.println();
        
        // 测试用例3：两个不同值
        System.out.println("测试用例3：两个不同值");
        int[] arr3 = {1, 2, 1, 2};
        int result3 = min1(arr3);
        System.out.println("数组: [1, 2, 1, 2]");
        System.out.println("最少变化次数: " + result3);
        System.out.println("分析: 需要通过联动效果统一所有元素");
        System.out.println();
        
        // 测试用例4：递增序列
        System.out.println("测试用例4：递增序列");
        int[] arr4 = {1, 2, 3, 4, 5};
        int result4 = min1(arr4);
        System.out.println("数组: [1, 2, 3, 4, 5]");
        System.out.println("最少变化次数: " + result4);
        System.out.println("分析: 每个元素都不同，需要逐步联动扩展");
        System.out.println();
        
        // 测试用例5：部分相同
        System.out.println("测试用例5：部分相同");
        int[] arr5 = {1, 1, 2, 2, 3};
        int result5 = min1(arr5);
        System.out.println("数组: [1, 1, 2, 2, 3]");
        System.out.println("最少变化次数: " + result5);
        System.out.println("分析: 已有部分连续相同元素，利用联动效果");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        System.out.println("开始随机测试...");
        boolean allPassed = true;
        
        for (int i = 0; i < 10000; i++) {
            int[] arr = randomArr(20, 10);
            int ans1 = min1(arr);
            int ans2 = min2(arr);
            
            if (ans1 != ans2) {
                System.out.println("测试失败: 结果不一致");
                System.out.print("数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + (j == arr.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 固定操作位置：一旦选定下标，只能在该下标操作");
        System.out.println("   - 联动效果：相同值且连续的元素会一起变化");
        System.out.println("   - 扩展性：每次操作会扩大相同值的连续区域");
        System.out.println();
        System.out.println("2. 递归策略：");
        System.out.println("   - 状态定义：当前连续相同值的区间范围");
        System.out.println("   - 转移选择：选择左边界或右边界的值进行扩展");
        System.out.println("   - 终止条件：整个数组变成同一值");
        System.out.println();
        System.out.println("3. 联动机制：");
        System.out.println("   - 值匹配：只有相同值的元素才会联动");
        System.out.println("   - 位置连续：必须是相邻位置的元素");
        System.out.println("   - 范围扩展：联动会扩大操作影响范围");
        System.out.println();
        System.out.println("4. 优化思路：");
        System.out.println("   - 起始位置选择：尝试每个位置作为起始操作点");
        System.out.println("   - 贪心选择：每次选择代价最小的扩展方向");
        System.out.println("   - 边界处理：正确处理数组边界情况");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(2^n)，最坏情况指数级");
        System.out.println("   - 空间复杂度：O(n)，递归栈深度");
        System.out.println("   - 实际表现：小规模数组效率可接受");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 游戏机制设计（连锁消除）");
        System.out.println("   - 数组变换问题");
        System.out.println("   - 递归决策优化");
    }
}
