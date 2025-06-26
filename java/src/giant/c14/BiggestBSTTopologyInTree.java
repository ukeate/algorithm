package giant.c14;

import java.io.*;

/**
 * 树中最大BST拓扑结构问题
 * 
 * 问题描述：
 * 给定一棵二叉树，找出其中最大的BST（二叉搜索树）拓扑结构。
 * BST拓扑结构是指满足BST性质的连通子图，但不要求是连续的子树。
 * 
 * 例如：
 *       6
 *      / \
 *     1   12
 *        /  \
 *       10   13
 *      /      \
 *     4       20
 *    / \      /
 *   2   5    16
 * 
 * 最大BST拓扑结构可能包含节点：1, 2, 4, 5, 6, 10, 12, 13, 16, 20
 * 
 * NowCoder链接：https://www.nowcoder.com/practice/e13bceaca5b14860b83cbcc4912c5d4a
 * 
 * 解决方案：
 * 使用动态规划的思想，对每个节点计算以它为根的最大BST拓扑结构大小
 * 
 * 核心思想：
 * 1. 对于每个节点，首先递归处理左右子树
 * 2. 然后尝试将当前节点加入到BST拓扑结构中
 * 3. 需要确保加入当前节点后仍然满足BST性质
 * 4. 使用记忆化避免重复计算
 * 
 * 算法复杂度：
 * 时间复杂度：O(N^2)
 * 空间复杂度：O(N)
 */
public class BiggestBSTTopologyInTree {
    
    /**
     * 计算以节点h为根的子树中最大BST拓扑结构的大小
     * 
     * 算法思路：
     * 1. 递归处理左右子树，获取它们的最大BST拓扑结构大小
     * 2. 尝试将当前节点h加入BST拓扑结构
     * 3. 向左子树寻找可以连接的最大节点
     * 4. 向右子树寻找可以连接的最小节点
     * 5. 更新当前节点的最大BST拓扑结构大小
     * 
     * 关键技巧：
     * - m[i] 表示以节点i为根能形成的最大BST拓扑结构大小
     * - 通过比较节点值来确定BST性质
     * - 使用回溯思想来处理拓扑结构
     * 
     * @param h 当前处理的节点编号
     * @param t 树结构数组，t[i][0]=父节点，t[i][1]=左子节点，t[i][2]=右子节点
     * @param m 记忆化数组，m[i]表示以节点i为根的最大BST拓扑结构大小
     * @return 整棵树中最大BST拓扑结构的大小
     */
    private static int max(int h, int[][] t, int[] m) {
        if (h == 0) {
            return 0;  // 空节点返回0
        }
        
        int l = t[h][1], r = t[h][2];  // 获取左右子节点
        int c = 0;  // 用于记录需要减少的拓扑结构大小
        
        // 递归处理左右子树，获取它们的最大BST拓扑结构大小
        int p1 = max(l, t, m);
        int p2 = max(r, t, m);

        // 处理左子树：寻找可以与当前节点h形成BST的部分
        // 向右走找到左子树中最大的可连接节点
        while (l < h && m[l] != 0) {
            l = t[l][2];  // 继续向右子树走
        }
        if (m[l] != 0) {
            // 如果l > h，说明存在违反BST性质的节点
            c = m[l];  // 记录需要减少的大小
            // 回溯路径，减少相应的拓扑结构大小
            while (l != h) {
                m[l] -= c;
                l = t[l][0];  // 向父节点回溯
            }
        }
        
        // 处理右子树：寻找可以与当前节点h形成BST的部分  
        // 向左走找到右子树中最小的可连接节点
        while (r > h && m[r] != 0) {
            r = t[r][1];  // 继续向左子树走
        }
        if (m[r] != 0) {
            // 如果r < h，说明存在违反BST性质的节点
            c = m[r];  // 记录需要减少的大小
            // 回溯路径，减少相应的拓扑结构大小
            while (r != h) {
                m[r] -= c;
                r = t[r][0];  // 向父节点回溯
            }
        }
        
        // 计算以当前节点h为根的最大BST拓扑结构大小
        m[h] = m[t[h][1]] + m[t[h][2]] + 1;
        
        // 返回全局最大值
        return Math.max(Math.max(p1, p2), m[h]);
    }

    /**
     * 主方法：处理输入输出
     * 
     * 输入格式：
     * - 第一行：n（节点数）和 h（根节点编号）
     * - 接下来n行：每行三个数字 c l r，表示节点c的左子节点是l，右子节点是r
     *   （0表示空节点）
     * 
     * 输出：
     * 最大BST拓扑结构的大小
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            // 读取节点数和根节点
            int n = (int) in.nval;
            in.nextToken();
            int h = (int) in.nval;
            
            // 构建树结构：tree[节点][父，左，右]
            int[][] tree = new int[n + 1][3];
            
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                int c = (int) in.nval;  // 当前节点
                in.nextToken(); 
                int l = (int) in.nval;  // 左子节点
                in.nextToken();
                int r = (int) in.nval;  // 右子节点
                
                // 建立父子关系
                tree[l][0] = c;  // l的父节点是c
                tree[r][0] = c;  // r的父节点是c
                tree[c][1] = l;  // c的左子节点是l
                tree[c][2] = r;  // c的右子节点是r
            }
            
            // 计算并输出最大BST拓扑结构大小
            out.print(max(h, tree, new int[n + 1]));
            out.flush();
        }
    }
}
