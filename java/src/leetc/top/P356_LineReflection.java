package leetc.top;

import java.util.*;

/**
 * LeetCode 356. 直线上的点反射 (Line Reflection)
 * 
 * 问题描述：
 * 给定一个二维平面上的 n 个点，判断这些点是否存在一条垂直线，使得每个点关于这条线的反射点也在给定的点集中。
 * 
 * 注意：
 * 1. 反射线不必经过任何点。
 * 2. 给定的点可能有重复。
 * 3. 坐标的顺序不重要。
 * 
 * 示例：
 * 输入: points = [[1,1],[-1,1],[-1,-1],[1,-1]]
 * 输出: true
 * 解释: 反射线是 x = 0。
 * 
 * 输入: points = [[1,1],[-1,-1]]
 * 输出: false
 * 解释: 不存在这样的反射线。
 * 
 * 解法思路：
 * 哈希集合 + 数学分析：
 * 
 * 1. 核心思想：
 *    - 如果存在反射线 x = k，那么对于每个点 (x, y)，都必须存在对应的反射点 (2k-x, y)
 *    - 反射线的位置 k = (min_x + max_x) / 2，即所有点x坐标的中点
 *    - 使用哈希集合快速查找点是否存在
 * 
 * 2. 算法步骤：
 *    - 找出所有点中x坐标的最小值和最大值
 *    - 计算可能的反射线位置：k = (min_x + max_x) / 2
 *    - 将所有点存入哈希集合
 *    - 对每个点验证其反射点是否在集合中
 * 
 * 3. 特殊情况处理：
 *    - 只有一个点：可以有无数条反射线，返回true
 *    - 所有点在同一条垂直线上：反射线就是这条垂直线，返回true
 *    - 重复点：不影响判断结果
 * 
 * 4. 数学原理：
 *    - 点 (x, y) 关于直线 x = k 的反射点是 (2k-x, y)
 *    - 反射线必须经过所有x坐标的中点，即 k = (min_x + max_x) / 2
 *    - 如果存在反射线，则每个点都能在集合中找到对应的反射点
 * 
 * 核心思想：
 * - 对称性检查：利用反射的对称性质
 * - 中心点计算：反射线位置的数学推导
 * - 集合查找：高效验证点的存在性
 * 
 * 关键技巧：
 * - 边界计算：通过最值确定反射线位置
 * - 浮点处理：避免浮点数精度问题
 * - 集合存储：用String或Pair存储点坐标
 * 
 * 时间复杂度：O(n) - 遍历点集两次
 * 空间复杂度：O(n) - 哈希集合存储所有点
 * 
 * LeetCode链接：https://leetcode.com/problems/line-reflection/
 */
public class P356_LineReflection {
    
    /**
     * 方法一：哈希集合 + 数学计算（推荐）
     * 
     * 使用数学方法确定反射线位置，然后验证每个点的反射点是否存在
     * 
     * @param points 二维点数组
     * @return 是否存在反射线
     */
    public boolean isReflected(int[][] points) {
        if (points == null || points.length == 0) {
            return true; // 空集合可以有反射线
        }
        
        if (points.length == 1) {
            return true; // 单个点可以有无数条反射线
        }
        
        // 找出x坐标的最小值和最大值
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        Set<String> pointSet = new HashSet<>();
        
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            
            // 将点存入集合，使用字符串表示避免哈希冲突
            pointSet.add(x + "," + y);
        }
        
        // 计算反射线的位置：x = (minX + maxX) / 2
        // 为避免浮点数，我们使用 sum = minX + maxX
        int sum = minX + maxX;
        
        // 验证每个点的反射点是否存在
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            // 计算反射点的x坐标：reflectedX = 2 * k - x = sum - x
            int reflectedX = sum - x;
            String reflectedPoint = reflectedX + "," + y;
            
            // 如果反射点不在集合中，则不存在反射线
            if (!pointSet.contains(reflectedPoint)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法二：使用Pair类的版本
     * 
     * 创建自定义Pair类来存储点坐标，避免字符串拼接
     */
    class Pair {
        int x, y;
        
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair pair = (Pair) obj;
            return x == pair.x && y == pair.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
    
    public boolean isReflectedWithPair(int[][] points) {
        if (points == null || points.length == 0) {
            return true;
        }
        
        if (points.length == 1) {
            return true;
        }
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        Set<Pair> pointSet = new HashSet<>();
        
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            pointSet.add(new Pair(x, y));
        }
        
        int sum = minX + maxX;
        
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            int reflectedX = sum - x;
            Pair reflectedPoint = new Pair(reflectedX, y);
            
            if (!pointSet.contains(reflectedPoint)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法三：排序 + 双指针验证（备选方案）
     * 
     * 对于相同y坐标的点，使用双指针验证对称性
     * 
     * @param points 二维点数组
     * @return 是否存在反射线
     */
    public boolean isReflectedSorting(int[][] points) {
        if (points == null || points.length <= 1) {
            return true;
        }
        
        // 按照y坐标分组，然后对每组的x坐标排序
        Map<Integer, List<Integer>> yToXs = new HashMap<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            
            yToXs.computeIfAbsent(y, k -> new ArrayList<>()).add(x);
        }
        
        int sum = minX + maxX;
        
        // 对每个y坐标对应的x坐标列表进行验证
        for (List<Integer> xs : yToXs.values()) {
            Collections.sort(xs);
            
            // 使用双指针验证对称性
            int left = 0, right = xs.size() - 1;
            while (left <= right) {
                if (xs.get(left) + xs.get(right) != sum) {
                    return false;
                }
                left++;
                right--;
            }
        }
        
        return true;
    }
    
    /**
     * 方法四：处理特殊情况的优化版本
     * 
     * 增加了对特殊情况的优化处理
     * 
     * @param points 二维点数组
     * @return 是否存在反射线
     */
    public boolean isReflectedOptimized(int[][] points) {
        if (points == null || points.length <= 1) {
            return true;
        }
        
        // 去重并找边界
        Set<String> uniquePoints = new HashSet<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            
            String pointStr = x + "," + y;
            uniquePoints.add(pointStr);
            
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        
        // 特殊情况：所有点在同一条垂直线上
        if (minX == maxX) {
            return true;
        }
        
        int sum = minX + maxX;
        
        // 验证每个唯一点的反射点
        for (String pointStr : uniquePoints) {
            String[] coords = pointStr.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            
            int reflectedX = sum - x;
            String reflectedPoint = reflectedX + "," + y;
            
            if (!uniquePoints.contains(reflectedPoint)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 辅助方法：可视化点和反射线
     * 
     * 用于调试和理解算法的辅助方法
     * 
     * @param points 点数组
     */
    public void visualizeReflection(int[][] points) {
        if (points == null || points.length == 0) {
            System.out.println("无点可显示");
            return;
        }
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        
        for (int[] point : points) {
            minX = Math.min(minX, point[0]);
            maxX = Math.max(maxX, point[0]);
        }
        
        double reflectionLine = (minX + maxX) / 2.0;
        
        System.out.println("点集: " + Arrays.deepToString(points));
        System.out.println("X坐标范围: [" + minX + ", " + maxX + "]");
        System.out.println("可能的反射线: x = " + reflectionLine);
        System.out.println("是否存在反射线: " + isReflected(points));
        
        // 显示每个点的反射点
        Set<String> pointSet = new HashSet<>();
        for (int[] point : points) {
            pointSet.add(point[0] + "," + point[1]);
        }
        
        System.out.println("点的反射验证:");
        int sum = minX + maxX;
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            int reflectedX = sum - x;
            
            String reflectedPoint = reflectedX + "," + y;
            boolean exists = pointSet.contains(reflectedPoint);
            
            System.out.printf("  (%d,%d) -> (%d,%d) %s%n", 
                x, y, reflectedX, y, exists ? "✓" : "✗");
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P356_LineReflection solution = new P356_LineReflection();
        
        // 测试用例1：存在反射线
        int[][] points1 = {{1,1},{-1,1},{-1,-1},{1,-1}};
        System.out.println("测试1 - 正方形: " + solution.isReflected(points1));
        solution.visualizeReflection(points1);
        
        System.out.println();
        
        // 测试用例2：不存在反射线
        int[][] points2 = {{1,1},{-1,-1}};
        System.out.println("测试2 - 对角线: " + solution.isReflected(points2));
        solution.visualizeReflection(points2);
        
        System.out.println();
        
        // 测试用例3：单个点
        int[][] points3 = {{0,0}};
        System.out.println("测试3 - 单点: " + solution.isReflected(points3));
        
        // 测试用例4：垂直线上的点
        int[][] points4 = {{1,1},{1,2},{1,3}};
        System.out.println("测试4 - 垂直线: " + solution.isReflected(points4));
        
        // 测试用例5：包含重复点
        int[][] points5 = {{1,1},{-1,1},{1,1},{-1,1}};
        System.out.println("测试5 - 重复点: " + solution.isReflected(points5));
        
        // 测试用例6：复杂情况
        int[][] points6 = {{0,0},{1,0},{3,0}};
        System.out.println("测试6 - 不对称: " + solution.isReflected(points6));
        solution.visualizeReflection(points6);
    }
} 