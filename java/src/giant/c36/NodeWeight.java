package giant.c36;

import java.util.HashMap;

/**
 * 树节点权重计算问题
 * 
 * 问题描述：
 * 来自美团算法题
 * 给定一棵树，每个节点都有颜色，初始时只有叶节点有权值。
 * 现在要计算所有非叶节点的权值。
 * 
 * 数据结构说明：
 * - h: 头节点编号
 * - m[i]: 节点i的所有直接孩子列表
 * - c[i]: 节点i的颜色
 * - w[i]: 节点i的权值（叶节点初始有值，非叶节点需要计算）
 * 
 * 例如：
 * h = 1, m = [[], [2,3], [4], [5,6], [], [], []]
 * 表示：1的孩子是2、3；2的孩子是4；3的孩子是5、6；4、5、6是叶节点
 * 
 * 权值计算规则：
 * 对于非叶节点i，根据其所有直接孩子的颜色分组计算：
 * w[i] = Max{
 *   (颜色为a的孩子个数 + 颜色为a的孩子权值之和),
 *   (颜色为b的孩子个数 + 颜色为b的孩子权值之和),
 *   (颜色为c的孩子个数 + 颜色为c的孩子权值之和),
 *   ...
 * }
 * 
 * 解决方案：
 * 使用后序遍历（DFS），先计算子节点权值，再计算父节点权值
 * 
 * 核心思想：
 * 1. 叶节点：权值已知，直接返回
 * 2. 非叶节点：递归计算所有孩子的权值后，按颜色分组统计
 * 3. 分组统计：相同颜色的孩子个数+权值和，取所有颜色组合中的最大值
 * 
 * 算法复杂度：
 * 时间复杂度：O(N)，每个节点访问一次
 * 空间复杂度：O(H + C)，其中H是树高，C是颜色种类数
 */
public class NodeWeight {
    // m是孩子数组, c是颜色数组, w是权值数组
    public static void w(int h, int[][] m, int[] w, int[] c) {
        if (m[h].length == 0) {
            return;
        }
        HashMap<Integer, Integer> colors = new HashMap<>();
        HashMap<Integer, Integer> weights = new HashMap<>();
        for (int child : m[h]) {
            w(child, m, w, c);
            colors.put(c[child], colors.getOrDefault(c[child], 0) + 1);
            weights.put(c[child], weights.getOrDefault(c[child], 0) + w[child]);
        }
        for (int color : colors.keySet()) {
            w[h] = Math.max(w[h], colors.get(color) + weights.get(color));
        }
    }
}
