package basic.c34;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 俄罗斯套娃信封问题
 * 
 * 问题描述：
 * 给定一些标记了宽度和高度的信封，求最多能有多少个信封能够俄罗斯套娃式地装在一起。
 * 一个信封能够装进另一个信封，当且仅当两个维度都严格大于另一个信封。
 * 
 * 例如：信封[(5,4), (6,7), (6,4), (2,3)]
 * 最大嵌套层数为3：(2,3) → (5,4) → (6,7)
 * 
 * 算法思路：
 * 这是二维的最长递增子序列(LIS)问题，核心策略：
 * 1. 按宽度升序排列，宽度相同时按高度降序排列
 * 2. 对高度序列求最长递增子序列
 * 
 * 为什么宽度相同时要按高度降序？
 * 避免宽度相同的信封被错误地认为可以嵌套，因为实际上宽度相同的信封无法嵌套。
 * 
 * 时间复杂度：O(n*log(n))，其中n为信封数量
 * 空间复杂度：O(n)
 */
public class Envelopes {
    
    /**
     * 信封内部类，表示一个信封的宽度和高度
     */
    private static class Envelope {
        public int w; // 宽度
        public int h; // 高度
        
        /**
         * 构造函数
         * @param width 信封宽度
         * @param height 信封高度
         */
        public Envelope(int width, int height) {
            w = width;
            h = height;
        }
    }

    /**
     * 信封排序比较器
     * 排序规则：
     * 1. 首先按宽度升序排列
     * 2. 宽度相同时按高度降序排列（关键优化）
     * 
     * 为什么这样排序？
     * - 宽度升序确保前面的信封宽度不大于后面的
     * - 宽度相同时高度降序，避免宽度相同的信封在LIS中被连续选中
     */
    public static class Comp implements Comparator<Envelope> {
        @Override
        public int compare(Envelope o1, Envelope o2) {
            return o1.w != o2.w ? o1.w - o2.w : o2.h - o1.h;
        }
    }

    /**
     * 将二维矩阵转换为信封对象数组并排序
     * @param matrix 输入矩阵，matrix[i] = [width, height]
     * @return 排序后的信封数组
     */
    private static Envelope[] sort(int[][] matrix) {
        Envelope[] res = new Envelope[matrix.length];
        
        // 将矩阵转换为信封对象
        for (int i = 0; i < matrix.length; i++) {
            res[i] = new Envelope(matrix[i][0], matrix[i][1]);
        }
        
        // 按照比较器规则排序
        Arrays.sort(res, new Comp());
        return res;
    }

    /**
     * 计算最大信封嵌套层数
     * 
     * 算法核心：
     * 1. 对信封按特定规则排序（宽度升序，相同宽度时高度降序）
     * 2. 对排序后的高度序列应用最长递增子序列(LIS)算法
     * 3. 使用二分查找优化的LIS算法，时间复杂度O(n*log(n))
     * 
     * @param matrix 信封数组，每个元素为[宽度, 高度]
     * @return 最大嵌套层数
     */
    public static int max(int[][] matrix) {
        // 步骤1：排序信封
        Envelope[] es = sort(matrix);
        
        // 步骤2：使用优化的LIS算法处理高度序列
        // ends[i]表示长度为i+1的递增子序列的最小结尾元素
        int[] ends = new int[matrix.length];
        ends[0] = es[0].h; // 初始化：第一个信封的高度
        int endR = 0;      // ends数组的有效长度-1
        
        // 对每个信封的高度应用LIS算法
        for (int i = 1; i < es.length; i++) {
            int l = 0, r = endR, m = 0;
            
            // 二分查找：找到第一个大于等于当前高度的位置
            while (l <= r) {
                m = (l + r) / 2;
                if (es[i].h > ends[m]) {
                    l = m + 1; // 当前高度更大，在右半部分查找
                } else {
                    r = m - 1; // 当前高度更小或相等，在左半部分查找
                }
            }
            
            // 更新ends数组和有效长度
            endR = Math.max(endR, l);  // 扩展有效长度
            ends[l] = es[i].h;         // 更新位置l的最小结尾元素
        }
        
        // 返回最长递增子序列长度，即最大嵌套层数
        return endR + 1;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例：[(3,4), (2,3), (4,5), (1,3), (2,2), (3,6), (1,2), (3,2), (2,4)]
        int[][] m = {{3, 4}, {2, 3}, {4, 5}, {1, 3}, {2, 2}, {3, 6}, {1, 2}, {3, 2}, {2, 4}};
        
        System.out.println("信封数据（宽度，高度）：");
        for (int i = 0; i < m.length; i++) {
            System.out.print("(" + m[i][0] + "," + m[i][1] + ") ");
        }
        System.out.println();
        
        int result = max(m);
        System.out.println("最大嵌套层数：" + result);
        
        // 显示排序后的信封顺序（用于理解算法）
        Envelope[] sorted = sort(m);
        System.out.println("\n排序后的信封顺序（宽度升序，相同宽度高度降序）：");
        for (Envelope e : sorted) {
            System.out.print("(" + e.w + "," + e.h + ") ");
        }
        System.out.println();
        
        // 额外测试用例
        System.out.println("\n额外测试：");
        int[][] test2 = {{5,4}, {6,4}, {6,7}, {2,3}};
        System.out.println("测试用例2最大嵌套层数：" + max(test2));
        
        int[][] test3 = {{1,1}, {1,1}, {1,1}};
        System.out.println("测试用例3最大嵌套层数：" + max(test3));
    }
}
