package giant.c27;

import java.util.Arrays;

/**
 * 乐队挑选最小花费问题
 * 
 * 问题描述：
 * 有若干个项目，每个项目由三个数表示：[a, b, c]，表示需要乐队a和乐队b参演，花费为c。
 * 每个乐队只能被挑选一次，但可能在多个项目中出现。
 * 给定可以挑选的项目数量nums，这意味着一定会有nums*2只乐队被挑选出来。
 * 乐队的总数量一定是nums*2，且标号一定是0 ~ nums*2-1。
 * 返回挑选nums个项目（请到所有乐队）的最少花费。
 * 
 * 例如：
 * nums = 2，乐队编号0,1,2,3
 * 项目：[[0,1,10], [0,2,20], [1,3,15], [2,3,5]]
 * 需要挑选2个项目，覆盖所有4个乐队
 * 可能的方案：[0,1,10] + [2,3,5] = 15
 * 
 * 解决方案：
 * 使用状态压缩DP + Meet in the Middle优化
 * 
 * 核心思想：
 * 1. 用位掩码表示已选择的乐队集合
 * 2. 使用分治思想将问题分解为两个子问题
 * 3. 分别计算前一半和后一半项目的所有可能状态
 * 4. 合并两个子问题的结果
 * 
 * 算法复杂度：
 * 时间复杂度：O(3^(nums/2))
 * 空间复杂度：O(2^nums)
 */
public class PickBands {
    
    /**
     * 数据预处理：去重和排序
     * 
     * 处理步骤：
     * 1. 对每个项目，确保a <= b（标准化表示）
     * 2. 按(a, b, c)的字典序排序
     * 3. 去除重复的(a, b)对，保留花费最小的
     * 
     * @param programs 原始项目数组
     * @return 处理后的项目数组大小
     */
    private static int clean(int[][] programs) {
        // 标准化每个项目：确保第一个乐队编号 <= 第二个乐队编号
        int x = 0, y = 0;
        for (int[] p : programs) {
            x = Math.min(p[0], p[1]);
            y = Math.max(p[0], p[1]);
            p[0] = x;
            p[1] = y;
        }
        
        // 按字典序排序：先按第一个乐队编号，再按第二个乐队编号，最后按花费
        Arrays.sort(programs, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] != b[1] ? (a[1] - b[1]) : (a[2] - b[2])));
        
        // 去重：对于相同的(a, b)对，标记重复项为null
        x = programs[0][0];
        y = programs[0][1];
        int n = programs.length;
        for (int i = 1; i < n; i++) {
            if (programs[i][0] == x && programs[i][1] == y) {
                programs[i] = null;  // 标记重复项
            } else {
                x = programs[i][0];
                y = programs[i][1];
            }
        }
        
        // 压缩数组：移除null项
        int size = 1;
        for (int i = 1; i < n; i++) {
            if (programs[i] != null) {
                programs[size++] = programs[i];
            }
        }
        return size;
    }

    /**
     * 初始化状态数组
     * 
     * @param size 数组大小
     * @return 初始化为Integer.MAX_VALUE的数组
     */
    private static int[] init(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Integer.MAX_VALUE;
        }
        return arr;
    }

    /**
     * 递归搜索函数：计算所有可能的状态和对应的最小花费
     * 
     * 算法思路：
     * 对于每个项目，有两种选择：
     * 1. 不选择当前项目：直接递归到下一个项目
     * 2. 选择当前项目：如果不冲突（两个乐队都未被选择），则选择并递归
     * 
     * @param programs 项目数组
     * @param size 有效项目数量
     * @param idx 当前考虑的项目索引
     * @param status 当前已选择乐队的状态（位掩码）
     * @param cost 当前已花费的总成本
     * @param rest 还需要选择的项目数量
     * @param map 状态到最小花费的映射表
     */
    private static void f(int[][] programs, int size, int idx, int status, int cost, int rest, int[] map) {
        if (rest == 0) {
            // 已选择足够的项目，更新该状态的最小花费
            map[status] = Math.min(map[status], cost);
        } else {
            if (idx < size) {
                // 选择1：不选择当前项目
                f(programs, size, idx + 1, status, cost, rest, map);
                
                // 选择2：选择当前项目（如果不冲突）
                int pick = (1 << programs[idx][0]) | (1 << programs[idx][1]);  // 当前项目涉及的乐队
                if ((pick & status) == 0) {  // 检查是否有乐队冲突
                    f(programs, size, idx + 1, status | pick, cost + programs[idx][2], rest - 1, map);
                }
            }
        }
    }

    /**
     * 主算法：使用分治思想求解最小花费
     * 
     * 算法步骤：
     * 1. 数据预处理（去重排序）
     * 2. 根据项目数量奇偶性决定分割策略
     * 3. 分别计算前一半和后一半的所有可能状态
     * 4. 枚举所有状态组合，找到互补且总花费最小的方案
     * 
     * 优化思想：
     * - Meet in the Middle：将时间复杂度从O(3^nums)降低到O(3^(nums/2))
     * - 状态压缩：用位掩码高效表示乐队选择状态
     * 
     * @param programs 项目数组 [乐队a, 乐队b, 花费c]
     * @param nums 需要选择的项目数量
     * @return 最小花费，如果无解返回-1
     */
    public static int minCost(int[][] programs, int nums) {
        if (nums == 0 || programs == null || programs.length == 0) {
            return 0;
        }
        
        // 数据预处理
        int size = clean(programs);
        
        // 初始化状态映射表
        int[] map1 = init(1 << (nums << 1));  // 2^(nums*2) 个可能状态
        int[] map2 = null;
        
        if ((nums & 1) == 0) {
            // nums为偶数：前后两半各选nums/2个项目
            f(programs, size, 0, 0, 0, nums >> 1, map1);
            map2 = map1;  // 两半的搜索空间相同
        } else {
            // nums为奇数：前一半选nums/2个，后一半选nums-nums/2个
            f(programs, size, 0, 0, 0, nums >> 1, map1);
            map2 = init(1 << (nums << 1));
            f(programs, size, 0, 0, 0, nums - (nums >> 1), map2);
        }
        
        // 合并两个子问题的结果
        int mask = (1 << (nums << 1)) - 1;  // 全选状态的掩码
        int ans = Integer.MAX_VALUE;
        
        // 枚举所有可能的状态组合
        for (int i = 0; i < map1.length; i++) {
            if (map1[i] != Integer.MAX_VALUE && map2[mask & (~i)] != Integer.MAX_VALUE) {
                // i 和 (~i & mask) 互补，覆盖所有乐队
                ans = Math.min(ans, map1[i] + map2[mask & (~i)]);
            }
        }
        
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    // ========== 以下是验证用的暴力算法 ==========
    
    private static int min = Integer.MAX_VALUE;

    /**
     * 暴力递归算法（用于验证）
     * 
     * @param programs 项目数组
     * @param idx 当前项目索引
     * @param rest 剩余需要选择的项目数
     * @param pick 已选择的乐队状态
     * @param cost 当前花费
     */
    private static void r(int[][] programs, int idx, int rest, int pick, int cost) {
        if (rest == 0) {
            min = Math.min(min, cost);
        } else {
            if (idx < programs.length) {
                // 不选择当前项目
                r(programs, idx + 1, rest, pick, cost);
                
                // 选择当前项目（如果不冲突）
                int cur = (1 << programs[idx][0]) | (1 << programs[idx][1]);
                if ((pick & cur) == 0) {
                    r(programs, idx + 1, rest - 1, pick | cur, cost + programs[idx][2]);
                }
            }
        }
    }

    /**
     * 暴力算法入口（用于验证）
     */
    public static int right(int[][] programs, int nums) {
        min = Integer.MAX_VALUE;
        r(programs, 0, nums, 0, 0);
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // ========== 测试方法 ==========

    /**
     * 生成随机项目数组用于测试
     * 
     * @param picks 需要选择的项目数量
     * @param maxVal 最大花费值
     * @return 随机项目数组
     */
    public static int[][] randomPrograms(int picks, int maxVal) {
        int nums = picks << 1;  // 乐队总数
        int n = nums * (nums - 1);  // 可能的项目总数（任意两个乐队的组合）
        int[][] programs = new int[n][3];
        
        for (int i = 0; i < n; i++) {
            int a = (int) (Math.random() * nums);
            int b = 0;
            do {
                b = (int) (Math.random() * nums);
            } while (b == a);  // 确保两个乐队不同
            
            programs[i][0] = a;
            programs[i][1] = b;
            programs[i][2] = (int) (Math.random() * maxVal) + 1;  // 花费范围[1, maxVal]
        }
        return programs;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 乐队挑选最小花费问题测试 ===");
        
        // 参数设置
        int maxNums = 4;      // 最大项目数量
        int maxVal = 100;     // 最大花费值
        int times = 10000;    // 测试次数
        
        System.out.println("测试参数:");
        System.out.println("最大项目数量: " + maxNums);
        System.out.println("最大花费值: " + maxVal);
        System.out.println("测试次数: " + times);
        System.out.println();
        
        System.out.println("开始随机测试验证...");
        boolean allCorrect = true;
        
        for (int i = 0; i < times; i++) {
            int picks = (int) (Math.random() * maxNums) + 1;  // 随机项目数量[1, maxNums]
            int[][] programs = randomPrograms(picks, maxVal);
            
            int ans1 = right(programs, picks);      // 暴力算法结果
            int ans2 = minCost(programs, picks);    // 优化算法结果
            
            if (ans1 != ans2) {
                System.out.println("发现错误!");
                System.out.println("项目数量: " + picks);
                System.out.println("暴力算法结果: " + ans1);
                System.out.println("优化算法结果: " + ans2);
                allCorrect = false;
                break;
            }
            
            if ((i + 1) % 2000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        // 手动测试用例
        System.out.println("\n手动测试用例:");
        int[][] testPrograms = {
            {0, 1, 10},
            {0, 2, 20}, 
            {1, 3, 15},
            {2, 3, 5}
        };
        int testNums = 2;
        
        System.out.println("项目列表: [[0,1,10], [0,2,20], [1,3,15], [2,3,5]]");
        System.out.println("需要选择项目数: " + testNums);
        int result = minCost(testPrograms, testNums);
        System.out.println("最小花费: " + result);
        System.out.println("期望: 15 (选择[0,1,10]和[2,3,5])");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
