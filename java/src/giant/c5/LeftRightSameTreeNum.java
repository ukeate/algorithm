package giant.c5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 相同左右子树节点统计问题
 * 
 * 问题描述：
 * 给定一棵二叉树，统计有多少个节点的左子树和右子树在结构和节点值上完全相同。
 * 相等的定义：两棵子树的结构相同且对应位置的节点值相等。
 * 
 * 例如：
 *       1
 *      / \
 *     2   2
 *    / \ / \
 *   3  4 3  4
 * 根节点1的左右子树完全相同，所以答案为1。
 * 
 * 解决方案：
 * 1. 方法1：暴力比较法 - 时间复杂度O(N*H*logN)，其中H是树的高度
 * 2. 方法2：哈希优化法 - 时间复杂度O(N)，使用哈希值比较子树
 * 
 * 核心思想：
 * 方法1：对每个节点递归比较其左右子树
 * 方法2：为每棵子树计算唯一哈希值，相同哈希值表示相同子树
 * 
 * 空间复杂度：O(N)（递归调用栈）
 */
public class LeftRightSameTreeNum {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点

        /**
         * 构造函数
         * @param v 节点值
         */
        public Node(int v) {
            val = v;
        }
    }

    /**
     * 判断两棵子树是否完全相同
     * 
     * 算法思路：
     * 递归比较两棵树的结构和节点值
     * 1. 如果两个节点都为空，返回true
     * 2. 如果只有一个节点为空，返回false  
     * 3. 如果节点值不同，返回false
     * 4. 递归比较左子树和右子树
     * 
     * @param h1 第一棵子树的根节点
     * @param h2 第二棵子树的根节点
     * @return 两棵子树是否完全相同
     */
    private static boolean same(Node h1, Node h2) {
        // 异或操作：如果一个为null另一个不为null，返回false
        if (h1 == null ^ h2 == null) {
            return false;
        }
        
        // 两个都为null，认为相同
        if (h1 == null && h2 == null) {
            return true;
        }
        
        // 递归比较：节点值相同且左右子树都相同
        return h1.val == h2.val && 
               same(h1.left, h2.left) && 
               same(h1.right, h2.right);
    }

    /**
     * 方法1：暴力比较法
     * 
     * 算法思路：
     * 对树中每个节点，递归比较其左右子树是否相同
     * 
     * 时间复杂度：O(N*H*logN)
     * - N个节点需要遍历
     * - 每个节点比较左右子树需要O(H*logN)时间
     * 
     * 空间复杂度：O(H)，递归调用栈深度
     * 
     * @param head 树的根节点
     * @return 左右子树相同的节点数量
     */
    public static int num1(Node head) {
        if (head == null) {
            return 0;
        }
        
        // 递归统计左右子树中相同节点的数量，再加上当前节点（如果其左右子树相同）
        return num1(head.left) + 
               num1(head.right) + 
               (same(head.left, head.right) ? 1 : 0);
    }

    /**
     * 哈希工具类
     * 用于计算字符串的哈希值，避免重复比较相同的子树结构
     */
    private static class Hash {
        private MessageDigest digest;  // 消息摘要算法实例

        /**
         * 构造函数
         * @param algorithm 哈希算法名称（如"SHA-256"）
         */
        public Hash(String algorithm) {
            try {
                digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        /**
         * 计算字符串的哈希值
         * @param s 输入字符串
         * @return 十六进制表示的哈希值
         */
        public String hashCode(String s) {
            byte[] hash = digest.digest(s.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        }
    }

    /**
     * 递归信息类
     * 用于方法2中返回子树的统计信息和哈希标识
     */
    private static class Info {
        public int ans;    // 当前子树中左右子树相同的节点数量
        public String str; // 当前子树的哈希标识字符串

        /**
         * 构造函数
         * @param a 相同节点数量
         * @param s 哈希标识字符串
         */
        public Info(int a, String s) {
            ans = a;
            str = s;
        }
    }

    /**
     * 方法2的递归处理函数
     * 
     * 算法思路：
     * 1. 对每个节点，递归获取左右子树的信息（包含答案和哈希值）
     * 2. 比较左右子树的哈希值是否相同
     * 3. 计算当前子树的哈希值（基于节点值和左右子树哈希值）
     * 4. 返回包含答案和哈希值的信息对象
     * 
     * 哈希值计算策略：
     * - 空节点：hash("#,")
     * - 非空节点：hash(节点值 + "," + 左子树哈希 + 右子树哈希)
     * 
     * @param head 当前节点
     * @param hash 哈希工具实例
     * @return 包含统计结果和哈希标识的Info对象
     */
    private static Info process(Node head, Hash hash) {
        if (head == null) {
            // 空节点的哈希值统一为"#,"的哈希值
            return new Info(0, hash.hashCode("#,"));
        }
        
        // 递归获取左右子树的信息
        Info l = process(head.left, hash);
        Info r = process(head.right, hash);
        
        // 计算当前节点的答案：左右子树的答案之和，加上当前节点（如果左右子树相同）
        int ans = (l.str.equals(r.str) ? 1 : 0) + l.ans + r.ans;
        
        // 计算当前子树的哈希标识
        // 格式：节点值 + "," + 左子树哈希 + 右子树哈希
        String str = hash.hashCode(head.val + "," + l.str + r.str);
        
        return new Info(ans, str);
    }

    /**
     * 方法2：哈希优化法
     * 
     * 算法思路：
     * 使用哈希值来标识每棵子树，避免重复的结构比较
     * 相同的子树会产生相同的哈希值，从而O(1)时间完成比较
     * 
     * 优势：
     * - 每个节点只需要访问一次
     * - 子树比较从O(子树大小)降低到O(1)
     * - 总体时间复杂度：O(N)
     * 
     * 注意：
     * 理论上存在哈希冲突的可能性，但概率极低
     * 可以使用更强的哈希算法（如SHA-256）来降低冲突概率
     * 
     * @param head 树的根节点
     * @return 左右子树相同的节点数量
     */
    public static int num2(Node head) {
        String algorithm = "SHA-256";  // 使用SHA-256哈希算法
        Hash hash = new Hash(algorithm);
        return process(head, hash).ans;
    }

    /**
     * 生成随机二叉树用于测试
     * 
     * @param restLevel 剩余的层数
     * @param maxVal 节点值的最大值
     * @return 随机生成的二叉树根节点
     */
    private static Node randomTree(int restLevel, int maxVal) {
        if (restLevel == 0) {
            return null;
        }
        
        // 20%的概率生成空节点，80%的概率生成非空节点
        Node head = Math.random() < 0.2 ? null : new Node((int) (Math.random() * maxVal));
        
        if (head != null) {
            head.left = randomTree(restLevel - 1, maxVal);
            head.right = randomTree(restLevel - 1, maxVal);
        }
        
        return head;
    }
    
    /**
     * 手动构建测试用例
     * @return 测试用的二叉树
     */
    private static Node buildTestTree() {
        /*
         * 构建如下测试树：
         *       1
         *      / \
         *     2   2
         *    / \ / \
         *   3  4 3  4
         * 
         * 根节点1的左右子树完全相同，答案应该为1
         */
        Node root = new Node(1);
        
        // 左子树
        root.left = new Node(2);
        root.left.left = new Node(3);
        root.left.right = new Node(4);
        
        // 右子树（与左子树相同）
        root.right = new Node(2);
        root.right.left = new Node(3);
        root.right.right = new Node(4);
        
        return root;
    }

    /**
     * 测试方法：验证两种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 相同左右子树节点统计测试 ===");
        
        // 测试手动构建的用例
        System.out.println("1. 手动测试用例:");
        Node testTree = buildTestTree();
        System.out.println("方法1结果: " + num1(testTree));
        System.out.println("方法2结果: " + num2(testTree));
        System.out.println("预期结果: 1");
        System.out.println();
        
        // 大量随机测试验证算法正确性
        int times = 100000;   // 测试次数
        int maxLevel = 8;     // 最大树深度
        int maxVal = 4;       // 最大节点值
        
        System.out.println("2. 随机测试验证算法正确性:");
        System.out.println("测试次数: " + times);
        System.out.println("最大树深度: " + maxLevel);
        System.out.println("最大节点值: " + maxVal);
        System.out.println("开始测试...");
        
        boolean allCorrect = true;
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            int ans1 = num1(head);
            int ans2 = num2(head);
            
            if (ans1 != ans2) {
                System.out.println("发现错误! 测试用例: " + i);
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if ((i + 1) % 20000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        long end = System.currentTimeMillis();
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        System.out.println("总耗时: " + (end - start) + "ms");
        System.out.println();
        
        // 性能比较测试
        System.out.println("3. 性能比较测试:");
        Node perfTestTree = randomTree(12, 10); // 更大的测试树
        
        start = System.currentTimeMillis();
        int result1 = num1(perfTestTree);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = num2(perfTestTree);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（暴力比较）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（哈希优化）: " + result2 + ", 耗时: " + time2 + "ms");
        System.out.println("性能提升: " + (time1 / (double) time2) + "倍");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
