package leetc.top;

/**
 * LeetCode 365. 水壶问题 (Water and Jug Problem)
 * 
 * 问题描述：
 * 有两个水壶，容量分别为 jug1Capacity 和 jug2Capacity 升。水的供应是无限的。
 * 确定是否有可能使用这两个水壶准确得到 targetCapacity 升。
 * 
 * 如果可以得到 targetCapacity 升水，最后请用以下方式之一输出：
 * - 两个水壶中至少有一个的水量是 targetCapacity 升。
 * - 两个水壶的水量之和是 targetCapacity 升。
 * 
 * 允许的操作：
 * 1. 装满任意一个水壶
 * 2. 清空任意一个水壶  
 * 3. 从一个水壶向另外一个水壶倒水，直到装满或者倒空
 * 
 * 示例：
 * 输入: jug1Capacity = 3, jug2Capacity = 5, targetCapacity = 4
 * 输出: true
 * 解释: 可以通过以下步骤得到4升水：
 * 1. 装满5升水壶 (0, 5)
 * 2. 从5升水壶倒入3升水壶 (3, 2)
 * 3. 清空3升水壶 (0, 2)
 * 4. 将5升水壶剩余的水倒入3升水壶 (2, 0)
 * 5. 再次装满5升水壶 (2, 5)
 * 6. 从5升水壶倒入3升水壶，直到3升水壶装满 (3, 4)
 * 
 * 解法思路：
 * 提供两种解法：
 * 1. 数学方法（贝祖等式）：O(log(min(a,b))) 时间复杂度
 * 2. 状态搜索（BFS）：O(a*b) 时间复杂度
 * 
 * 方法1 - 数学方法（推荐）：
 * 根据贝祖等式（扩展欧几里得算法），对于方程 ax + by = target：
 * - 当且仅当 gcd(a,b) 能整除 target 时，方程有整数解
 * - 在水壶问题中，这意味着可以通过倒水操作得到目标容量
 * - 同时需要满足：target ≤ a + b（总容量限制）
 * 
 * 方法2 - 状态搜索：
 * 使用BFS搜索所有可能的水壶状态(x,y)，其中x和y分别表示两个水壶的水量。
 * 每个状态可以进行6种操作，搜索直到找到目标状态或搜索完所有状态。
 * 
 * 时间复杂度：
 * - 数学方法：O(log(min(a,b))) - 欧几里得算法的复杂度
 * - 状态搜索：O(a*b) - 最多搜索a*b个状态
 * 
 * 空间复杂度：
 * - 数学方法：O(1) - 只使用常数空间
 * - 状态搜索：O(a*b) - 需要记录访问过的状态
 * 
 * LeetCode链接：https://leetcode.com/problems/water-and-jug-problem/
 */
public class P365_WaterAndJugProblem {
    
    /**
     * 方法1：数学方法 - 贝祖等式
     * 
     * 核心思想：
     * 水壶问题本质上是求解不定方程 ax + by = target 是否有整数解
     * 根据贝祖等式，当且仅当 gcd(a,b) 能整除 target 时，方程有解
     * 
     * 数学证明：
     * 1. 任何倒水操作的结果都可以表示为 ax + by 的形式
     * 2. 其中 a,b 是两个水壶的容量，x,y 是操作次数（可为负数）
     * 3. gcd(a,b) 是能够通过倒水操作得到的最小正容量
     * 4. 因此只有当 target 是 gcd(a,b) 的倍数时才可能达到
     * 
     * @param jug1Capacity 第一个水壶容量
     * @param jug2Capacity 第二个水壶容量  
     * @param targetCapacity 目标容量
     * @return 是否可能得到目标容量
     */
    public boolean canMeasureWater1(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        // 边界情况：目标容量超过两个水壶总容量
        if (targetCapacity > jug1Capacity + jug2Capacity) {
            return false;
        }
        
        // 特殊情况：目标容量为0，或等于任一水壶容量，或等于总容量
        if (targetCapacity == 0 || targetCapacity == jug1Capacity || 
            targetCapacity == jug2Capacity || targetCapacity == jug1Capacity + jug2Capacity) {
            return true;
        }
        
        // 应用贝祖等式：检查 gcd(a,b) 是否能整除 target
        return targetCapacity % gcd(jug1Capacity, jug2Capacity) == 0;
    }
    
    /**
     * 计算两个数的最大公约数（欧几里得算法）
     * 
     * 算法原理：gcd(a,b) = gcd(b, a mod b)
     * 递归终止条件：当 b = 0 时，gcd(a,0) = a
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return 最大公约数
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 方法2：状态搜索 - BFS
     * 
     * 核心思想：
     * 将问题建模为状态搜索，每个状态表示两个水壶的水量(x,y)
     * 从初始状态(0,0)开始，通过BFS搜索所有可能到达的状态
     * 
     * 六种操作：
     * 1. 装满水壶1：(x,y) → (jug1Capacity,y)
     * 2. 装满水壶2：(x,y) → (x,jug2Capacity)  
     * 3. 清空水壶1：(x,y) → (0,y)
     * 4. 清空水壶2：(x,y) → (x,0)
     * 5. 从水壶1倒入水壶2：(x,y) → (x-pour,y+pour)，pour = min(x, jug2Capacity-y)
     * 6. 从水壶2倒入水壶1：(x,y) → (x+pour,y-pour)，pour = min(y, jug1Capacity-x)
     * 
     * @param jug1Capacity 第一个水壶容量
     * @param jug2Capacity 第二个水壶容量
     * @param targetCapacity 目标容量
     * @return 是否可能得到目标容量
     */
    public boolean canMeasureWater2(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        // 边界情况检查
        if (targetCapacity > jug1Capacity + jug2Capacity) {
            return false;
        }
        if (targetCapacity == 0) {
            return true;
        }
        
        // 使用HashSet记录访问过的状态，避免重复搜索
        java.util.Set<String> visited = new java.util.HashSet<>();
        // 使用队列进行BFS
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        
        // 初始状态：两个水壶都为空
        queue.offer(new int[]{0, 0});
        visited.add("0,0");
        
        // BFS搜索所有可能状态
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];  // 水壶1当前水量
            int y = current[1];  // 水壶2当前水量
            
            // 检查是否达到目标：任一水壶的水量或总水量等于目标
            if (x == targetCapacity || y == targetCapacity || x + y == targetCapacity) {
                return true;
            }
            
            // 生成所有可能的下一状态
            int[][] nextStates = {
                {jug1Capacity, y},  // 装满水壶1
                {x, jug2Capacity},  // 装满水壶2
                {0, y},             // 清空水壶1
                {x, 0},             // 清空水壶2
                // 从水壶1倒入水壶2
                {x - Math.min(x, jug2Capacity - y), y + Math.min(x, jug2Capacity - y)},
                // 从水壶2倒入水壶1  
                {x + Math.min(y, jug1Capacity - x), y - Math.min(y, jug1Capacity - x)}
            };
            
            // 将未访问的新状态加入队列
            for (int[] next : nextStates) {
                String stateKey = next[0] + "," + next[1];
                if (!visited.contains(stateKey)) {
                    visited.add(stateKey);
                    queue.offer(next);
                }
            }
        }
        
        return false;  // 搜索完所有状态都未找到目标
    }
    
    /**
     * 主方法：使用数学方法（推荐）
     * 
     * @param jug1Capacity 第一个水壶容量
     * @param jug2Capacity 第二个水壶容量
     * @param targetCapacity 目标容量
     * @return 是否可能得到目标容量
     */
    public boolean canMeasureWater(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        return canMeasureWater1(jug1Capacity, jug2Capacity, targetCapacity);
    }
} 