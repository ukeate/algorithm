package leetc.followup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 跳跃游戏II变种 - 双向跳跃最短路径 (Jump Game II Variant - Bidirectional Jump Shortest Path)
 * 基于LeetCode 45. Jump Game II的扩展版本
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，数组下标从1开始到n
 * 从起始位置start出发，要到达目标位置end
 * 在位置i时，可以向左跳跃到 i-arr[i-1]，或向右跳跃到 i+arr[i-1]
 * 求从start到end的最少跳跃次数
 * 
 * 与原版Jump Game II的区别：
 * 1. 原版只能向右跳，这里可以双向跳跃
 * 2. 原版总是从0到n-1，这里可以从任意位置到任意位置
 * 3. 增加了越界检查和访问控制
 * 
 * 解法思路：
 * 1. 方法1：回溯算法 + 访问标记，避免循环访问
 * 2. 方法2：递归 + 步数剪枝，提前终止无效路径
 * 3. 方法3：广度优先搜索(BFS)，保证找到最短路径
 * 
 * 时间复杂度：
 * - 方法1/2：O(2^n) - 最坏情况下指数级复杂度
 * - 方法3：O(n) - BFS确保每个位置最多访问一次
 * 
 * 空间复杂度：O(n) - 访问标记数组或BFS队列
 */
public class P45_JumpGameIIChange {
    
    /**
     * 方法1：回溯算法 + 访问标记
     * 
     * 算法思路：
     * 使用深度优先搜索，通过访问标记避免重复访问同一位置
     * 这样可以避免无限循环，但仍然会探索所有可能路径
     * 
     * @param arr 跳跃数组（1-based索引）
     * @param n 数组长度
     * @param end 目标位置
     * @param i 当前位置
     * @param walk 访问标记数组，防止重复访问
     * @return 从当前位置到目标位置的最少步数，-1表示无法到达
     */
    private static int process1(int[] arr, int n, int end, int i, boolean[] walk) {
        // 边界检查：位置越界
        if (i < 1 || i > n) {
            return -1;
        }
        
        // 访问检查：避免重复访问（防止循环）
        if (walk[i - 1]) {
            return -1;
        }
        
        // 到达目标位置
        if (i == end) {
            return 0;
        }
        
        // 标记当前位置为已访问
        walk[i - 1] = true;
        
        // 计算下一步可能的位置
        int left = i - arr[i - 1];   // 向左跳跃
        int right = i + arr[i - 1];  // 向右跳跃
        
        // 递归探索两个方向
        int ans1 = process1(arr, n, end, left, walk);   // 向左跳的结果
        int ans2 = process1(arr, n, end, right, walk);  // 向右跳的结果
        
        // 选择最优路径
        int next = -1;
        if (ans1 != -1 && ans2 != -1) {
            next = Math.min(ans1, ans2);  // 两个方向都可达，选择步数少的
        } else if (ans1 != -1) {
            next = ans1;  // 只有左边可达
        } else if (ans2 != -1) {
            next = ans2;  // 只有右边可达
        }
        
        // 回溯：清除访问标记
        walk[i - 1] = false;
        
        // 返回结果
        if (next == -1) {
            return -1;  // 两个方向都不可达
        }
        return next + 1;  // 当前步数 + 1
    }

    /**
     * 方法1入口函数：使用回溯算法求解
     * 
     * @param n 数组长度
     * @param start 起始位置（1-based）
     * @param end 目标位置（1-based）
     * @param arr 跳跃数组
     * @return 最少跳跃次数，-1表示无法到达
     */
    public static int jump1(int n, int start, int end, int[] arr) {
        boolean[] walk = new boolean[n];  // 访问标记数组
        return process1(arr, n, end, start, walk);
    }

    /**
     * 方法2：递归 + 步数剪枝
     * 
     * 算法思路：
     * 不使用访问标记，而是通过步数剪枝来避免无效搜索
     * 如果当前步数超过n-1，说明路径太长，提前终止
     * 
     * @param arr 跳跃数组
     * @param n 数组长度
     * @param end 目标位置
     * @param i 当前位置
     * @param k 当前已经走的步数
     * @return 到达目标位置时的总步数，-1表示无法到达
     */
    private static int process2(int[] arr, int n, int end, int i, int k) {
        // 边界检查：位置越界
        if (i < 1 || i > n) {
            return -1;
        }
        
        // 剪枝：步数过多，超过理论最大值
        if (k > n - 1) {
            return -1;
        }
        
        // 到达目标位置
        if (i == end) {
            return k;  // 返回当前步数
        }
        
        // 计算下一步可能的位置
        int left = i - arr[i - 1];   // 向左跳跃
        int right = i + arr[i - 1];  // 向右跳跃
        
        // 递归探索两个方向，步数+1
        int ans1 = process2(arr, n, end, left, k + 1);   // 向左跳
        int ans2 = process2(arr, n, end, right, k + 1);  // 向右跳
        
        // 选择最优路径
        int ans = -1;
        if (ans1 != -1 && ans2 != -1) {
            ans = Math.min(ans1, ans2);  // 两个方向都可达，选择步数少的
        } else if (ans1 != -1) {
            ans = ans1;  // 只有左边可达
        } else if (ans2 != -1) {
            ans = ans2;  // 只有右边可达
        }
        
        return ans;
    }

    /**
     * 方法2入口函数：使用递归+剪枝求解
     * 
     * @param n 数组长度
     * @param start 起始位置（1-based）
     * @param end 目标位置（1-based）
     * @param arr 跳跃数组
     * @return 最少跳跃次数，-1表示无法到达
     */
    public static int jump2(int n, int start, int end, int[] arr) {
        return process2(arr, n, end, start, 0);
    }

    /**
     * 方法3：广度优先搜索(BFS)算法
     * 
     * 算法思路：
     * 使用BFS确保找到最短路径，每个位置最多访问一次
     * 利用队列进行层序遍历，levelMap记录到达每个位置的最少步数
     * 
     * BFS的优势：
     * 1. 保证找到最短路径（第一次到达目标就是最优解）
     * 2. 时间复杂度稳定为O(n)
     * 3. 避免了递归的栈空间开销
     * 
     * @param n 数组长度
     * @param start 起始位置（1-based）
     * @param end 目标位置（1-based）
     * @param arr 跳跃数组
     * @return 最少跳跃次数，-1表示无法到达
     */
    public static int jump3(int n, int start, int end, int[] arr) {
        // 参数有效性检查
        if (start < 1 || start > n || end < 1 || end > n) {
            return -1;
        }
        
        // BFS数据结构
        Queue<Integer> que = new LinkedList<>();           // BFS队列
        HashMap<Integer, Integer> levelMap = new HashMap<>();  // 位置 -> 步数映射
        
        // 初始化：将起始位置加入队列
        que.add(start);
        levelMap.put(start, 0);
        
        // BFS主循环
        while (!que.isEmpty()) {
            int cur = que.poll();          // 取出当前位置
            int level = levelMap.get(cur); // 获取到达当前位置的步数
            
            if (cur == end) {
                // 到达目标位置，返回步数
                return level;
            } else {
                // 计算下一步可能的位置
                int left = cur - arr[cur - 1];   // 向左跳跃
                int right = cur + arr[cur - 1];  // 向右跳跃
                
                // 检查左跳是否有效且未访问过
                if (left > 0 && !levelMap.containsKey(left)) {
                    que.add(left);
                    levelMap.put(left, level + 1);
                }
                
                // 检查右跳是否有效且未访问过
                if (right <= n && !levelMap.containsKey(right)) {
                    que.add(right);
                    levelMap.put(right, level + 1);
                }
            }
        }
        
        return -1;  // 无法到达目标位置
    }

    /**
     * 生成随机测试数组
     * 
     * @param n 数组长度
     * @param r 数值范围[0, r)
     * @return 随机生成的数组
     */
    private static int[] randomArr(int n, int r) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (r * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 双向跳跃最短路径问题测试 ===");
        
        int times = 200;
        int maxLen = 20;
        int maxVal = 10;
        
        System.out.println("开始随机测试...");
        System.out.println("测试次数: " + times);
        System.out.println("最大数组长度: " + maxLen);
        System.out.println("最大跳跃值: " + maxVal);
        
        boolean allCorrect = true;
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int n = arr.length;
            int start = (int) (n * Math.random()) + 1;  // 随机起始位置
            int end = (int) (n * Math.random()) + 1;    // 随机目标位置
            
            int ans1 = jump1(n, start, end, arr);  // 回溯算法
            int ans2 = jump2(n, start, end, arr);  // 递归剪枝
            int ans3 = jump3(n, start, end, arr);  // BFS算法
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("发现错误! 测试用例: " + i);
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("起始位置: " + start + ", 目标位置: " + end);
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                System.out.println("方法3结果: " + ans3);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if ((i + 1) % 50 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，三种算法结果一致！");
        }
        
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("1. 方法1（回溯+标记）: 避免循环访问，但会重复探索");
        System.out.println("2. 方法2（递归+剪枝）: 通过步数限制提前终止");
        System.out.println("3. 方法3（BFS）: 最优解法，保证最短路径且时间复杂度最佳");
        System.out.println("4. 推荐使用BFS算法处理此类最短路径问题");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
