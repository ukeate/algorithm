package giant.c43;

import java.util.Arrays;

/**
 * 数组达标最小代价问题（Yeah!数组）
 * 
 * 问题描述：
 * 给定一个正数数组arr，长度为n，下标0~n-1
 * arr中的0、n-1位置不需要达标，它们分别是最左、最右的位置
 * 中间位置i需要达标，达标的条件是：arr[i-1] > arr[i] 或者 arr[i+1] > arr[i]（任一条件即可）
 * 
 * 操作规则：
 * 你每一步可以进行如下操作：对任何位置的数让其-1
 * 你的目的是让arr[1~n-2]都达标，这时arr称之为Yeah！数组
 * 
 * 求解目标：
 * 返回至少要多少步可以让arr变成Yeah！数组
 * 
 * 数据规模：
 * - 数组长度 <= 10000
 * - 数组中的值 <= 500
 * 
 * 解题思路：
 * 方法1：递归+回溯（暴力解法）
 * - 枚举所有可能的减值操作
 * - 时间复杂度极高，仅用于验证
 * 
 * 方法2-4：动态规划
 * - 状态定义：dp[i][j][k] = 处理到位置i，前一个位置值为j，是否已达标为k的最小代价
 * - 状态转移：考虑当前位置的所有可能取值
 * - 优化：使用单调性和滚动数组
 * 
 * 方法5：双向贪心（最优解法）
 * - 从左到右和从右到左分别计算最小代价
 * - 在每个位置选择最优的分割点
 * 
 * 时间复杂度：O(n^2) 到 O(n)
 * 空间复杂度：O(n^2) 到 O(n)
 * 
 * 来源：360笔试题
 * 
 * @author Zhu Runqi
 */
public class MinCostToYeahArray {
    /** 表示无效状态的常量 */
    public static final int INVALID = Integer.MAX_VALUE;

    /**
     * 递归暴力求解（方法0：基准解法）
     * 
     * 算法思路：
     * 1. 对每个位置尝试所有可能的减值操作
     * 2. 递归检查所有可能的状态组合
     * 3. 在递归回溯中找到最小代价
     * 
     * 注意：时间复杂度极高，仅用于小规模验证
     * 
     * @param arr 当前数组状态
     * @param base 最小可能值的下界
     * @param idx 当前处理的位置
     * @return 最小代价，无效返回INVALID
     */
    private static int process0(int[] arr, int base, int idx) {
        if (idx == arr.length) {
            // 检查所有中间位置是否达标
            for (int i = 1; i < arr.length - 1; i++) {
                if (arr[i - 1] <= arr[i] && arr[i] >= arr[i + 1]) {
                    return INVALID;  // 位置i未达标
                }
            }
            return 0;
        } else {
            int ans = INVALID;
            int tmp = arr[idx];
            // 尝试当前位置的所有可能值
            for (int cost = 0; arr[idx] >= base; cost++, arr[idx]--) {
                int next = process0(arr, base, idx + 1);
                if (next != INVALID) {
                    ans = Math.min(ans, cost + next);
                }
            }
            arr[idx] = tmp;  // 回溯
            return ans;
        }
    }

    /**
     * 暴力递归方法的入口函数
     * 
     * @param arr 输入数组
     * @return 最小代价
     */
    public static int min0(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int base = min - n;  // 设置下界，避免无限递归
        return process0(arr, base, 0);
    }

    /**
     * 动态规划递归求解（方法1：记忆化搜索）
     * 
     * 算法思路：
     * 1. 状态定义：process1(idx, pre, preOk)
     *    - idx: 当前处理位置
     *    - pre: 前一个位置的值
     *    - preOk: 前一个位置是否已达标
     * 2. 状态转移：根据preOk决定当前位置的取值范围
     * 3. 递归计算最小代价
     * 
     * @param arr 数组
     * @param idx 当前位置
     * @param pre 前一个位置的值
     * @param preOk 前一个位置是否已达标
     * @return 最小代价
     */
    private static int process1(int[] arr, int idx, int pre, boolean preOk) {
        if (idx == arr.length - 1) {
            // 最后一个位置，检查前一个位置是否达标
            return preOk || pre < arr[idx] ? 0 : INVALID;
        }
        int ans = INVALID;
        if (preOk) {
            // 前一个位置已达标，当前位置可以取任意值
            for (int cur = arr[idx]; cur >= 0; cur--) {
                int next = process1(arr, idx + 1, cur, cur < pre);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[idx] - cur + next);
                }
            }
        } else {
            // 前一个位置未达标，当前位置必须小于前一个位置
            for (int cur = arr[idx]; cur > pre; cur--) {
                int next = process1(arr, idx + 1, cur, false);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[idx] - cur + next);
                }
            }
        }
        return ans;
    }

    /**
     * 动态规划记忆化搜索方法
     * 
     * @param arr 输入数组
     * @return 最小代价
     */
    public static int min1(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        // 数组值向上平移，确保都是正数
        for (int i = 0; i < arr.length; i++) {
            arr[i] += arr.length - min;
        }
        return process1(arr, 1, arr[0], true);
    }

    /**
     * 动态规划迭代求解（方法2：标准DP）
     * 
     * 算法思路：
     * 1. 定义dp[i][j][k]：
     *    - i: 位置索引
     *    - j: 0表示前一个位置未达标，1表示已达标
     *    - k: 前一个位置的值
     * 2. 状态转移：根据达标条件转移
     * 3. 最终答案：dp[n-1][1][arr[0]]
     * 
     * @param arr 输入数组
     * @return 最小代价
     */
    public static int min2(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        // 数组值向上平移
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        
        // dp[位置][是否达标][前一个位置的值]
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            dp[i][0] = new int[arr[i - 1] + 1];  // 未达标状态
            dp[i][1] = new int[arr[i - 1] + 1];  // 已达标状态
            Arrays.fill(dp[i][0], INVALID);
            Arrays.fill(dp[i][1], INVALID);
        }
        
        // 初始化最后一个位置
        for (int pre = 0; pre <= arr[n - 2]; pre++) {
            dp[n - 1][0][pre] = pre < arr[n - 1] ? 0 : INVALID;
            dp[n - 1][1][pre] = 0;
        }
        
        // 从后往前填表
        for (int idx = n - 2; idx >= 1; idx--) {
            for (int pre = 0; pre <= arr[idx - 1]; pre++) {
                // 前一个位置未达标的情况
                for (int cur = arr[idx]; cur > pre; cur--) {
                    int next = dp[idx + 1][0][cur];
                    if (next != INVALID) {
                        dp[idx][0][pre] = Math.min(dp[idx][0][pre], arr[idx] - cur + next);
                    }
                }
                // 前一个位置已达标的情况
                for (int cur = arr[idx]; cur >= 0; cur--) {
                    int next = dp[idx + 1][cur < pre ? 1 : 0][cur];
                    if (next != INVALID) {
                        dp[idx][1][pre] = Math.min(dp[idx][1][pre], arr[idx] - cur + next);
                    }
                }
            }
        }
        
        return dp[1][1][arr[0]];
    }

    /**
     * 计算最优选择的辅助函数
     * 
     * @param dp 动态规划表
     * @param i 位置
     * @param v 值
     * @return 最优选择表
     */
    private static int[][] best(int[][][] dp, int i, int v) {
        int[][] best = new int[2][v + 1];
        best[0][v] = dp[i][0][v];
        
        // 计算未达标状态的最优选择
        for (int p = v - 1; p >= 0; p--) {
            best[0][p] = dp[i][0][p] == INVALID ? INVALID : v - p + dp[i][0][p];
            best[0][p] = Math.min(best[0][p], best[0][p + 1]);
        }
        
        // 计算已达标状态的最优选择
        best[1][0] = dp[i][1][0] == INVALID ? INVALID : v + dp[i][1][0];
        for (int p = 1; p <= v; p++) {
            best[1][p] = dp[i][1][p] == INVALID ? INVALID : v - p + dp[i][1][p];
            best[1][p] = Math.min(best[1][p], best[1][p - 1]);
        }
        
        return best;
    }

    /**
     * 优化的动态规划求解（方法3：状态优化）
     * 
     * 算法思路：
     * 1. 预计算每个位置的最优选择
     * 2. 利用单调性质减少状态转移的计算量
     * 3. 空间和时间复杂度都得到优化
     * 
     * @param arr 输入数组
     * @return 最小代价
     */
    public static int min3(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            dp[i][0] = new int[arr[i - 1] + 1];
            dp[i][1] = new int[arr[i - 1] + 1];
        }
        
        // 初始化最后一个位置
        for (int p = 0; p <= arr[n - 2]; p++) {
            dp[n - 1][0][p] = p < arr[n - 1] ? 0 : INVALID;
        }
        
        int[][] best = best(dp, n - 1, arr[n - 2]);
        
        // 从后往前填表，使用预计算的最优选择
        for (int i = n - 2; i >= 1; i--) {
            for (int p = 0; p <= arr[i - 1]; p++) {
                if (arr[i] < p) {
                    dp[i][1][p] = best[1][arr[i]];
                } else {
                    dp[i][1][p] = Math.min(best[0][p], p > 0 ? best[1][p - 1] : INVALID);
                }
                dp[i][0][p] = arr[i] <= p ? INVALID : best[0][p + 1];
            }
            best = best(dp, i, arr[i - 1]);
        }
        
        return dp[1][1][arr[0]];
    }

    /**
     * 双向贪心算法（方法4：最优解法）
     * 
     * 算法思路：
     * 1. 将数组扩展，首尾添加无穷大值
     * 2. 从左到右计算每个位置的最小代价（保证递减）
     * 3. 从右到左计算每个位置的最小代价（保证递减）
     * 4. 在每个分割点选择左右代价之和的最小值
     * 
     * 核心思想：
     * - 左边保持严格递减序列
     * - 右边保持严格递减序列
     * - 在中间某个位置分割，使总代价最小
     * 
     * @param arr 输入数组
     * @return 最小代价
     */
    public static int min4(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        
        // 扩展数组，首尾添加无穷大
        int[] nums = new int[n + 2];
        nums[0] = Integer.MAX_VALUE;
        nums[n + 1] = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            nums[i + 1] = arr[i];
        }
        
        // 从左到右计算最小代价（保证递减）
        int[] leftCost = new int[n + 2];
        int pre = nums[0];
        int change = 0;
        for (int i = 1; i <= n; i++) {
            change = Math.min(pre - 1, nums[i]);  // 保证严格递减
            leftCost[i] = nums[i] - change + leftCost[i - 1];
            pre = change;
        }
        
        // 从右到左计算最小代价（保证递减）
        int[] rightCost = new int[n + 2];
        pre = nums[n + 1];
        for (int i = n; i >= 1; i--) {
            change = Math.min(pre - 1, nums[i]);  // 保证严格递减
            rightCost[i] = nums[i] - change + rightCost[i + 1];
            pre = change;
        }
        
        // 找到最优分割点
        int ans = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            ans = Math.min(ans, leftCost[i] + rightCost[i + 1]);
        }
        
        return ans;
    }

    /**
     * 生成随机数组
     * 
     * @param len 数组长度
     * @param v 数值上界
     * @return 随机数组
     */
    private static int[] randomArr(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
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
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数组达标最小代价问题（Yeah!数组）===\n");
        
        // 测试用例1：简单示例
        System.out.println("测试用例1：简单示例");
        int[] test1 = {5, 4, 3, 2, 1};
        int result1 = min4(copy(test1));
        System.out.print("数组: [");
        for (int i = 0; i < test1.length; i++) {
            System.out.print(test1[i] + (i == test1.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小代价: " + result1);
        System.out.println("分析: 数组已经递减，中间位置都满足达标条件");
        System.out.println();
        
        // 测试用例2：需要调整的数组
        System.out.println("测试用例2：需要调整的数组");
        int[] test2 = {1, 2, 3, 4, 5};
        int result2 = min4(copy(test2));
        System.out.print("数组: [");
        for (int i = 0; i < test2.length; i++) {
            System.out.print(test2[i] + (i == test2.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小代价: " + result2);
        System.out.println("分析: 递增数组需要大量调整才能达标");
        System.out.println();
        
        // 测试用例3：混合数组
        System.out.println("测试用例3：混合数组");
        int[] test3 = {3, 1, 4, 1, 5};
        int result3 = min4(copy(test3));
        System.out.print("数组: [");
        for (int i = 0; i < test3.length; i++) {
            System.out.print(test3[i] + (i == test3.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("最小代价: " + result3);
        System.out.println("分析: 部分位置已达标，需要少量调整");
        System.out.println();
        
        // 性能测试和正确性验证
        System.out.println("=== 性能测试和正确性验证 ===");
        int times = 100;
        int len = 7;
        int v = 10;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArr(n, v);
            
            // 只比较能在合理时间内完成的方法
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int[] arr4 = copy(arr);
            
            int ans1 = min1(arr1);
            int ans2 = min2(arr2);
            int ans3 = min3(arr3);
            int ans4 = min4(arr4);
            
            if (ans1 != ans2 || ans1 != ans3 || ans1 != ans4) {
                System.out.println("测试失败!");
                System.out.print("数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + (j == arr.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                System.out.println("方法3结果: " + ans3);
                System.out.println("方法4结果: " + ans4);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        // 大规模性能测试
        System.out.println("\n=== 大规模性能测试 ===");
        int[] largeArr = randomArr(1000, 100);
        
        long start = System.currentTimeMillis();
        int largeResult = min4(largeArr);
        long end = System.currentTimeMillis();
        
        System.out.println("数组长度: 1000");
        System.out.println("最小代价: " + largeResult);
        System.out.println("执行时间: " + (end - start) + " 毫秒");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题本质：");
        System.out.println("   - 达标条件：中间位置要么左邻居大，要么右邻居大");
        System.out.println("   - 操作限制：只能减少元素值，不能增加");
        System.out.println("   - 优化目标：最小化总的减值操作次数");
        System.out.println();
        System.out.println("2. 动态规划方法：");
        System.out.println("   - 状态设计：位置+前值+达标状态");
        System.out.println("   - 状态转移：根据达标条件选择当前值");
        System.out.println("   - 优化技巧：单调性质和预计算");
        System.out.println();
        System.out.println("3. 双向贪心方法（最优）：");
        System.out.println("   - 核心思想：左递减+右递减的组合");
        System.out.println("   - 分割策略：找到最优的分割点");
        System.out.println("   - 时间复杂度：O(n)，空间复杂度：O(n)");
        System.out.println();
        System.out.println("4. 关键洞察：");
        System.out.println("   - 最优解必定可以分为两个递减段");
        System.out.println("   - 每个段内部的处理是独立的");
        System.out.println("   - 贪心选择：在每个位置选择最小可能值");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 方法1-3：O(n^2)时间，O(n^2)空间");
        System.out.println("   - 方法4：O(n)时间，O(n)空间");
        System.out.println("   - 实际性能：方法4明显优于其他方法");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 数组调整优化问题");
        System.out.println("   - 约束满足问题");
        System.out.println("   - 序列规划问题");
    }
}
