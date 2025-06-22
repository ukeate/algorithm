package base.str;

import java.util.ArrayList;

/**
 * 判断二叉树子树问题
 * 问题描述：判断一个二叉树是否是另一个二叉树的子树
 * 
 * 算法思路：
 * 1. 将二叉树问题转化为字符串匹配问题
 * 2. 对两个二叉树进行先序序列化，包含空节点信息
 * 3. 使用KMP算法判断小树的序列化是否是大树序列化的子串
 * 
 * 核心洞察：
 * - 如果树A是树B的子树，那么A的先序序列化必定是B的先序序列化的子串
 * - 序列化时必须包含空节点信息，确保结构唯一性
 * - 相比递归比较，这种方法时间复杂度更优
 * 
 * 时间复杂度：O(M + N) - M和N分别为两棵树的节点数
 * 空间复杂度：O(M + N) - 存储序列化结果和KMP的next数组
 * 
 * 优势：
 * - 避免了递归方法中的重复子问题
 * - 利用KMP算法的高效性
 * - 将树问题转化为更简单的字符串问题
 * 
 * @see <a href="https://leetcode.cn/problems/subtree-of-another-tree/">LeetCode 572</a>
 */
// https://leetcode.cn/problems/subtree-of-another-tree/
public class IsSubTree {
    
    /**
     * 二叉树节点类
     * 简单的树节点定义，包含值和左右子节点
     */
    private static class TreeNode {
        public int val;           // 节点值
        public TreeNode left;     // 左子节点
        public TreeNode right;    // 右子节点

        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 对二叉树进行先序遍历序列化（递归版本）
     * 将树结构转化为字符串数组，包含完整的结构信息
     * 
     * 序列化规则：
     * - 对于非空节点，记录其值
     * - 对于空节点，记录null
     * - 按照先序遍历顺序：根->左->右
     * 
     * 关键特性：
     * - 包含空节点信息确保序列化的唯一性
     * - 不同结构的树序列化结果必然不同
     * 
     * @param head 当前节点
     * @param ans 存储序列化结果的列表
     */
    private static void pres(TreeNode head, ArrayList<String> ans) {
        if (head == null) {
            ans.add(null);  // 空节点用null表示
        } else {
            ans.add(String.valueOf(head.val));  // 记录节点值
            pres(head.left, ans);               // 递归处理左子树
            pres(head.right, ans);              // 递归处理右子树
        }
    }

    /**
     * 对二叉树进行先序序列化的公共接口
     * 
     * @param head 树的根节点
     * @return 序列化后的字符串列表
     */
    private static ArrayList<String> pre(TreeNode head) {
        ArrayList<String> ans = new ArrayList<>();
        pres(head, ans);
        return ans;
    }

    /**
     * 比较两个字符串是否相等，处理null值情况
     * 
     * null值处理：
     * - null == null 返回true
     * - null != 非null 返回false
     * - 非null值使用equals比较
     * 
     * @param a 字符串a
     * @param b 字符串b
     * @return 是否相等
     */
    private static boolean isEqual(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * 构建KMP算法的next数组（针对字符串数组版本）
     * 与字符数组版本类似，但比较元素时使用isEqual方法
     * 
     * 算法思路：
     * 1. 处理边界情况：长度为1时返回[-1]
     * 2. 初始化：next[0]=-1, next[1]=0
     * 3. 利用已计算结果递推求解后续位置
     * 
     * @param s 模式串（字符串数组）
     * @return next数组，用于失配时的跳转
     */
    private static int[] nextArr(String[] s) {
        if (s.length == 1) {
            return new int[]{-1};
        }
        
        int[] next = new int[s.length];
        next[0] = -1;  // 边界标记
        next[1] = 0;   // 长度为1时无真前后缀
        int i = 2;     // 从位置2开始计算
        int cn = 0;    // 当前匹配长度
        
        while (i < next.length) {
            if (isEqual(s[i - 1], s[cn])) {
                // 匹配成功，扩展匹配长度
                next[i++] = ++cn;
            } else if (cn > 0) {
                // 匹配失败，cn跳转
                cn = next[cn];
            } else {
                // cn为0，无法继续跳转
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * KMP字符串匹配算法（字符串数组版本）
     * 在大字符串数组中查找小字符串数组
     * 
     * 算法流程：
     * 1. 构建模式串的next数组
     * 2. 使用双指针进行匹配
     * 3. 匹配成功时双指针前进
     * 4. 匹配失败时根据next数组跳转
     * 
     * @param str1 文本串（大序列）
     * @param str2 模式串（小序列，要查找的序列）
     * @return 匹配的起始位置，未找到返回-1
     */
    private static int match(String[] str1, String[] str2) {
        if (str1 == null || str2 == null || str2.length < 1 || str1.length < str2.length) {
            return -1;
        }
        
        int x = 0;  // 文本串指针
        int y = 0;  // 模式串指针
        int[] next = nextArr(str2);  // 构建next数组
        
        while (x < str1.length && y < str2.length) {
            if (isEqual(str1[x], str2[y])) {
                // 匹配成功，双指针前进
                x++;
                y++;
            } else if (next[y] == -1) {
                // 模式串第一个字符不匹配，文本串指针前进
                x++;
            } else {
                // 利用next数组进行跳转
                y = next[y];
            }
        }
        
        return y == str2.length ? x - y : -1;
    }

    /**
     * 判断二叉树small是否是二叉树big的子树
     * 
     * 算法步骤：
     * 1. 处理边界情况：空树是任何树的子树
     * 2. 对两棵树进行先序序列化
     * 3. 将序列化结果转换为字符串数组
     * 4. 使用KMP算法判断小树序列是否是大树序列的子串
     * 
     * 核心思想：
     * 如果树A是树B的子树，那么A的序列化结果必定是B的序列化结果的子串
     * 
     * 示例：
     * 大树：    3          小树：  4
     *          / \                / \
     *         4   5              1   2
     *        / \
     *       1   2
     * 
     * 大树序列化：[3, 4, 1, null, null, 2, null, null, 5, null, null]
     * 小树序列化：[4, 1, null, null, 2, null, null]
     * 小树序列确实是大树序列的子串，返回true
     * 
     * @param big 大二叉树（可能包含子树的树）
     * @param small 小二叉树（要查找的子树）
     * @return 如果small是big的子树返回true，否则返回false
     */
    public static boolean isSubTree(TreeNode big, TreeNode small) {
        // 边界情况：空树是任何树的子树
        if (small == null) {
            return true;
        }
        // 非空小树不可能是空大树的子树
        if (big == null) {
            return false;
        }
        
        // 序列化两棵树
        ArrayList<String> b = pre(big);    // 大树的序列化
        ArrayList<String> s = pre(small);  // 小树的序列化
        
        // 转换为字符串数组以便KMP处理
        String[] str = new String[b.size()];
        for (int i = 0; i < str.length; i++) {
            str[i] = b.get(i);
        }
        
        String[] match = new String[s.size()];
        for (int i = 0; i < match.length; i++) {
            match[i] = s.get(i);
        }
        
        // 使用KMP算法判断小树序列是否是大树序列的子串
        return match(str, match) != -1;
    }
}
