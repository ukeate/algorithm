package basic.c37;

import java.util.HashMap;
import java.util.Map;

/**
 * 一条直线最多穿过几个点问题
 * 
 * 问题描述：
 * 给定平面上的n个点，求一条直线最多能穿过多少个点。
 * 
 * 核心思想：
 * 对于每个点作为"基准点"，计算它与其他所有点形成的直线的斜率，
 * 相同斜率的点在同一条直线上，统计每种斜率的点数量。
 * 
 * 算法要点：
 * 1. 斜率的表示：使用最简分数形式（分子/分母，最大公约数为1）
 * 2. 特殊情况处理：垂直线（x相同）、水平线（y相同）、重合点
 * 3. 避免浮点数精度问题：使用整数对(x,y)表示斜率
 * 
 * 时间复杂度：O(N²*log(坐标范围))，log因子来自最大公约数计算
 * 空间复杂度：O(N)，用于存储斜率统计
 */
public class MaxPointsOneLine {
    
    /**
     * 点类：表示平面上的一个点
     */
    public static class Point {
        public int x;  // x坐标
        public int y;  // y坐标

        /**
         * 默认构造函数：创建原点(0,0)
         */
        Point() {
            x = 0;
            y = 0;
        }

        /**
         * 参数构造函数：创建指定坐标的点
         * @param a x坐标
         * @param b y坐标
         */
        Point(int a, int b) {
            x = a;
            y = b;
        }
    }

    /**
     * 计算两个数的最大公约数（欧几里得算法）
     * 用于将斜率化简为最简分数形式
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return 最大公约数
     */
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 计算一条直线最多穿过多少个点
     * 
     * 算法流程：
     * 1. 枚举每个点作为基准点
     * 2. 对于基准点，计算它与其他所有点的位置关系：
     *    - 重合点：坐标完全相同
     *    - 垂直线：x坐标相同，y坐标不同
     *    - 水平线：y坐标相同，x坐标不同
     *    - 斜线：使用最简分数表示斜率
     * 3. 统计每种关系的点数，取最大值
     * 4. 所有基准点的结果取最大值
     * 
     * 关键技巧：
     * - 斜率用最简分数(dx/dy)表示，避免浮点数精度问题
     * - 使用嵌套HashMap存储斜率统计：Map<dx, Map<dy, count>>
     * - 重合点要加到任何直线的统计中
     * 
     * @param points 点数组
     * @return 一条直线最多穿过的点数
     */
    public static int max(Point[] points) {
        // 边界条件处理
        if (points == null) {
            return 0;
        }
        if (points.length <= 2) {
            return points.length;  // 2个点或更少，必然在同一直线上
        }
        
        // 嵌套HashMap存储斜率统计：外层key=dx，内层key=dy，value=该斜率的点数
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        int rst = 0;  // 全局最大值
        
        // 枚举每个点作为基准点
        for (int i = 0; i < points.length; i++) {
            map.clear();  // 清空上一个基准点的统计
            
            int samePosition = 1;  // 与基准点重合的点数（包括基准点自己）
            int sameX = 0;         // 与基准点在同一垂直线上的点数
            int sameY = 0;         // 与基准点在同一水平线上的点数
            int lineMax = 0;       // 当前基准点形成的最长斜线的点数
            
            // 计算基准点i与其他所有点j的关系
            for (int j = i + 1; j < points.length; j++) {
                // 计算点j相对于基准点i的坐标差
                int x = points[j].x - points[i].x;
                int y = points[j].y - points[i].y;
                
                if (x == 0 && y == 0) {
                    // 情况1：重合点
                    samePosition++;
                } else if (x == 0) {
                    // 情况2：垂直线（x坐标相同）
                    sameX++;
                } else if (y == 0) {
                    // 情况3：水平线（y坐标相同）
                    sameY++;
                } else {
                    // 情况4：斜线，需要计算并统计斜率
                    
                    // 化简斜率为最简分数形式
                    int gcd = gcd(x, y);
                    x /= gcd;
                    y /= gcd;
                    
                    // 统计该斜率的点数
                    if (!map.containsKey(x)) {
                        map.put(x, new HashMap<>());
                    }
                    if (!map.get(x).containsKey(y)) {
                        map.get(x).put(y, 0);
                    }
                    
                    // 增加该斜率的点数计数
                    map.get(x).put(y, map.get(x).get(y) + 1);
                    
                    // 更新当前最长斜线的点数
                    lineMax = Math.max(lineMax, map.get(x).get(y));
                }
            }
            
            // 计算以点i为基准的最大直线点数：
            // max(垂直线点数, 水平线点数, 最长斜线点数) + 重合点数
            rst = Math.max(rst, Math.max(Math.max(sameX, sameY), lineMax) + samePosition);
        }
        
        return rst;
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 一条直线最多穿过几个点测试 ===");
        
        // 测试用例1：基本情况
        Point[] points1 = {
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            new Point(4, 5),
            new Point(5, 6)
        };
        
        System.out.println("测试用例1：");
        System.out.println("点集：(1,1), (2,2), (3,3), (4,5), (5,6)");
        System.out.println("最多穿过点数: " + max(points1));
        System.out.println("分析：(1,1), (2,2), (3,3)在同一直线上，最多3个点");
        System.out.println();
        
        // 测试用例2：垂直线
        Point[] points2 = {
            new Point(1, 1),
            new Point(1, 2),
            new Point(1, 3),
            new Point(2, 1),
            new Point(2, 2)
        };
        
        System.out.println("测试用例2：");
        System.out.println("点集：(1,1), (1,2), (1,3), (2,1), (2,2)");
        System.out.println("最多穿过点数: " + max(points2));
        System.out.println("分析：x=1的垂直线穿过3个点");
        System.out.println();
        
        // 测试用例3：重合点
        Point[] points3 = {
            new Point(0, 0),
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 2)
        };
        
        System.out.println("测试用例3：");
        System.out.println("点集：(0,0), (0,0), (1,1), (2,2)");
        System.out.println("最多穿过点数: " + max(points3));
        System.out.println("分析：所有点都在y=x直线上，包含重合点");
        System.out.println();
        
        // 测试用例4：单点
        Point[] points4 = {new Point(1, 1)};
        
        System.out.println("测试用例4：");
        System.out.println("点集：(1,1)");
        System.out.println("最多穿过点数: " + max(points4));
        System.out.println();
        
        // 测试用例5：空数组
        Point[] points5 = null;
        
        System.out.println("测试用例5：");
        System.out.println("点集：null");
        System.out.println("最多穿过点数: " + max(points5));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
