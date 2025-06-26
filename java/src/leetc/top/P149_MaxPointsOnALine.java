package leetc.top;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 149. 直线上最多的点数 (Max Points on a Line)
 * 
 * 问题描述：
 * 给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。
 * 求最多有多少个点在同一条直线上。
 * 
 * 解法思路：
 * 枚举基准点 + 斜率统计：
 * 1. 对于每个点作为基准点，统计其他点与该点形成的直线
 * 2. 使用斜率作为直线的唯一标识
 * 3. 特殊情况处理：
 *    - 重合点：坐标完全相同的点
 *    - 垂直线：x坐标相同但y坐标不同
 *    - 水平线：y坐标相同但x坐标不同
 * 
 * 斜率计算与化简：
 * - 使用 (y2-y1)/(x2-x1) 表示斜率
 * - 为避免浮点数精度问题，使用分数形式存储
 * - 通过最大公约数化简分数到最简形式
 * - 使用二维HashMap: map[分子][分母] = 点的数量
 * 
 * 关键优化：
 * - 分数化简：gcd(x, y) 确保斜率的唯一性
 * - 特殊情况分别统计：避免除零和精度问题
 * - 对于每个基准点，重新初始化统计
 * 
 * 边界情况：
 * - 所有点重合
 * - 所有点在同一条直线上
 * - 只有1或2个点
 * 
 * 时间复杂度：O(n²) - 对每个点统计其他所有点
 * 空间复杂度：O(n) - HashMap存储斜率信息
 * 
 * LeetCode链接：https://leetcode.com/problems/max-points-on-a-line/
 */
public class P149_MaxPointsOnALine {
    
    /**
     * 计算最大公约数（辗转相除法）
     * 用于化简分数，确保斜率表示的唯一性
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return 最大公约数
     */
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 找出最多有多少个点在同一条直线上
     * 
     * @param points 点的坐标数组
     * @return 同一直线上的最多点数
     */
    public static int maxPoints(int[][] points) {
        if (points == null) {
            return 0;
        }
        if (points.length <= 2) {
            return points.length;
        }
        
        // 外层Map：分子 -> 内层Map
        // 内层Map：分母 -> 该斜率的点数
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        int rst = 0;
        
        // 枚举每个点作为基准点
        for (int i = 0; i < points.length; i++) {
            map.clear();  // 每个基准点重新统计
            
            int samePos = 1;    // 与基准点重合的点数（包括基准点本身）
            int sameX = 0;      // 与基准点x坐标相同的点数（垂直线）
            int sameY = 0;      // 与基准点y坐标相同的点数（水平线）
            int line = 0;       // 其他斜率直线上的最大点数
            
            // 检查其他所有点
            for (int j = i + 1; j < points.length; j++) {
                int x = points[j][0] - points[i][0];  // x方向差值
                int y = points[j][1] - points[i][1];  // y方向差值
                
                if (x == 0 && y == 0) {
                    // 重合点
                    samePos++;
                } else if (x == 0) {
                    // 垂直线（x坐标相同）
                    sameX++;
                } else if (y == 0) {
                    // 水平线（y坐标相同）
                    sameY++;
                } else {
                    // 一般情况：计算斜率并化简
                    int gcd = gcd(x, y);
                    x /= gcd;  // 化简后的分子
                    y /= gcd;  // 化简后的分母
                    
                    // 统计该斜率的点数
                    if (!map.containsKey(x)) {
                        map.put(x, new HashMap<>());
                    }
                    if (!map.get(x).containsKey(y)) {
                        map.get(x).put(y, 0);
                    }
                    map.get(x).put(y, map.get(x).get(y) + 1);
                    
                    // 更新该斜率直线上的最大点数
                    line = Math.max(line, map.get(x).get(y));
                }
            }
            
            // 计算以当前点为基准的最大直线点数
            // 比较垂直线、水平线、其他斜率线的最大值，再加上重合点
            rst = Math.max(rst, Math.max(Math.max(sameX, sameY), line) + samePos);
        }
        
        return rst;
    }
}
