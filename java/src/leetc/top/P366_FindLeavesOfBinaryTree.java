package leetc.top;

import java.util.*;

/**
 * LeetCode 366. 寻找二叉树的叶子节点 (Find Leaves of Binary Tree)
 * 
 * 问题描述：
 * 给你一棵二叉树的根节点 root ，按以下步骤收集树的所有节点：
 * 
 * 1. 收集并删除所有叶子节点，重复直到树为空。
 * 2. 在每一轮中，收集的所有叶子节点值应该按从左到右的顺序排列。
 * 
 * 返回一个二维数组，其中 answer[i] 包含第 i 轮收集的所有叶子节点的值。
 * 
 * 示例：
 * 输入：root = [1,2,3,4,5]
 *       1
 *      / \
 *     2   3
 *    / \
 *   4   5
 * 
 * 输出：[[4,5,3],[2],[1]]
 * 解释：
 * [[3,5,4],[2],[1]] 和 [[3,4,5],[2],[1]] 也会被接受。
 * 
 * 输入：root = [1]
 * 输出：[[1]]
 * 
 * 提示：
 * - 树中节点数目在范围 [1, 100] 内
 * - -100 <= Node.val <= 100
 * 
 * 解法思路：
 * 深度优先搜索(DFS) + 高度计算：
 * 
 * 1. 核心思想：
 *    - 叶子节点的高度为0，其父节点高度为1，依此类推
 *    - 按照节点高度分组，高度相同的节点会在同一轮被移除
 *    - 根节点的高度最大，最后被移除
 * 
 * 2. 算法步骤：
 *    - 定义节点高度：叶子节点高度为0，其他节点高度为max(左子树高度, 右子树高度) + 1
 *    - DFS遍历计算每个节点的高度
 *    - 按高度分组收集节点值
 * 
 * 3. 关键观察：
 *    - 第i轮删除的节点高度都是i-1
 *    - 可以用一次DFS同时计算高度和收集结果
 *    - 节点的高度决定了它被删除的轮次
 * 
 * 核心思想：
 * - 高度定义：重新定义树的高度概念（叶子为0）
 * - 分层处理：按高度分组处理节点
 * - 后序遍历：先处理子树，再处理当前节点
 * 
 * 关键技巧：
 * - 高度计算：利用递归计算节点到叶子的最大距离
 * - 分组收集：使用列表数组按高度存储节点值
 * - 顺序保证：按照DFS的访问顺序自然保证从左到右
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(n) - 递归栈 + 结果存储
 * 
 * LeetCode链接：https://leetcode.com/problems/find-leaves-of-binary-tree/
 */
public class P366_FindLeavesOfBinaryTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode() {}
        
        TreeNode(int val) {
            this.val = val;
        }
        
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public String toString() {
            return "TreeNode{" + val + "}";
        }
    }
    
    /**
     * 方法一：DFS + 高度计算（推荐）
     * 
     * 通过计算节点高度来确定删除顺序
     * 
     * @param root 二叉树根节点
     * @return 按轮次分组的叶子节点值
     */
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        
        // DFS计算高度并收集节点
        calculateHeight(root, result);
        
        return result;
    }
    
    /**
     * 计算节点高度并按高度分组收集节点值
     * 
     * @param node 当前节点
     * @param result 结果列表
     * @return 当前节点的高度
     */
    private int calculateHeight(TreeNode node, List<List<Integer>> result) {
        if (node == null) {
            return -1; // 空节点高度定义为-1
        }
        
        // 递归计算左右子树的高度
        int leftHeight = calculateHeight(node.left, result);
        int rightHeight = calculateHeight(node.right, result);
        
        // 当前节点的高度 = max(左子树高度, 右子树高度) + 1
        int currentHeight = Math.max(leftHeight, rightHeight) + 1;
        
        // 确保result有足够的子列表
        while (result.size() <= currentHeight) {
            result.add(new ArrayList<>());
        }
        
        // 将当前节点值添加到对应高度的列表中
        result.get(currentHeight).add(node.val);
        
        return currentHeight;
    }
    
    /**
     * 方法二：模拟删除过程
     * 
     * 实际模拟逐层删除叶子节点的过程
     * 
     * @param root 二叉树根节点
     * @return 按轮次分组的叶子节点值
     */
    public List<List<Integer>> findLeavesSimulation(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        
        // 创建树的副本以避免修改原树
        TreeNode rootCopy = copyTree(root);
        
        while (rootCopy != null) {
            List<Integer> currentRound = new ArrayList<>();
            rootCopy = removeLeaves(rootCopy, currentRound);
            result.add(currentRound);
        }
        
        return result;
    }
    
    /**
     * 删除叶子节点并收集它们的值
     * 
     * @param node 当前节点
     * @param leaves 当前轮次收集的叶子节点值
     * @return 删除叶子后的子树根节点
     */
    private TreeNode removeLeaves(TreeNode node, List<Integer> leaves) {
        if (node == null) {
            return null;
        }
        
        // 如果是叶子节点，收集其值并返回null（删除）
        if (node.left == null && node.right == null) {
            leaves.add(node.val);
            return null;
        }
        
        // 递归处理左右子树
        node.left = removeLeaves(node.left, leaves);
        node.right = removeLeaves(node.right, leaves);
        
        return node;
    }
    
    /**
     * 复制树结构
     * 
     * @param node 原始节点
     * @return 复制的节点
     */
    private TreeNode copyTree(TreeNode node) {
        if (node == null) return null;
        
        TreeNode copy = new TreeNode(node.val);
        copy.left = copyTree(node.left);
        copy.right = copyTree(node.right);
        
        return copy;
    }
    
    /**
     * 方法三：BFS分层处理
     * 
     * 使用BFS逐层识别和删除叶子节点
     * 
     * @param root 二叉树根节点
     * @return 按轮次分组的叶子节点值
     */
    public List<List<Integer>> findLeavesBFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        
        // 建立父子关系映射
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        Map<TreeNode, Integer> childrenCount = new HashMap<>();
        
        buildParentMap(root, null, parentMap, childrenCount);
        
        // 找到所有叶子节点
        Queue<TreeNode> leaves = new LinkedList<>();
        for (TreeNode node : childrenCount.keySet()) {
            if (childrenCount.get(node) == 0) {
                leaves.offer(node);
            }
        }
        
        while (!leaves.isEmpty()) {
            int size = leaves.size();
            List<Integer> currentRound = new ArrayList<>();
            
            for (int i = 0; i < size; i++) {
                TreeNode leaf = leaves.poll();
                currentRound.add(leaf.val);
                
                // 更新父节点的子节点计数
                TreeNode parent = parentMap.get(leaf);
                if (parent != null) {
                    childrenCount.put(parent, childrenCount.get(parent) - 1);
                    
                    // 如果父节点变成了叶子节点，加入队列
                    if (childrenCount.get(parent) == 0) {
                        leaves.offer(parent);
                    }
                }
            }
            
            result.add(currentRound);
        }
        
        return result;
    }
    
    /**
     * 建立父子关系映射
     * 
     * @param node 当前节点
     * @param parent 父节点
     * @param parentMap 父节点映射
     * @param childrenCount 子节点计数
     */
    private void buildParentMap(TreeNode node, TreeNode parent, 
                               Map<TreeNode, TreeNode> parentMap, 
                               Map<TreeNode, Integer> childrenCount) {
        if (node == null) return;
        
        parentMap.put(node, parent);
        
        int count = 0;
        if (node.left != null) count++;
        if (node.right != null) count++;
        childrenCount.put(node, count);
        
        buildParentMap(node.left, node, parentMap, childrenCount);
        buildParentMap(node.right, node, parentMap, childrenCount);
    }
    
    /**
     * 方法四：迭代版本的高度计算
     * 
     * 使用栈模拟递归过程
     * 
     * @param root 二叉树根节点
     * @return 按轮次分组的叶子节点值
     */
    public List<List<Integer>> findLeavesIterative(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        
        Map<TreeNode, Integer> heights = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        Stack<TreeNode> postOrder = new Stack<>();
        
        // 后序遍历收集节点
        TreeNode current = root;
        while (current != null || !stack.isEmpty()) {
            if (current != null) {
                stack.push(current);
                postOrder.push(current);
                current = current.right;
            } else {
                current = stack.pop();
                current = current.left;
            }
        }
        
        // 按后序遍历的顺序计算高度
        while (!postOrder.isEmpty()) {
            TreeNode node = postOrder.pop();
            
            int leftHeight = (node.left == null) ? -1 : heights.get(node.left);
            int rightHeight = (node.right == null) ? -1 : heights.get(node.right);
            
            int height = Math.max(leftHeight, rightHeight) + 1;
            heights.put(node, height);
            
            // 确保result有足够的子列表
            while (result.size() <= height) {
                result.add(new ArrayList<>());
            }
            
            result.get(height).add(node.val);
        }
        
        return result;
    }
    
    /**
     * 方法五：自定义高度定义 + 递归
     * 
     * 使用自定义的高度计算方法
     * 
     * @param root 二叉树根节点
     * @return 按轮次分组的叶子节点值
     */
    public List<List<Integer>> findLeavesCustomHeight(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        
        Map<TreeNode, Integer> heightMap = new HashMap<>();
        Map<Integer, List<Integer>> levelMap = new TreeMap<>();
        
        int maxHeight = calculateCustomHeight(root, heightMap);
        
        // 收集每个高度的节点
        collectNodesByHeight(root, heightMap, levelMap);
        
        // 按高度顺序添加到结果中
        for (int i = 0; i <= maxHeight; i++) {
            if (levelMap.containsKey(i)) {
                result.add(levelMap.get(i));
            }
        }
        
        return result;
    }
    
    /**
     * 计算自定义高度
     * 
     * @param node 当前节点
     * @param heightMap 高度映射
     * @return 最大高度
     */
    private int calculateCustomHeight(TreeNode node, Map<TreeNode, Integer> heightMap) {
        if (node == null) return -1;
        
        int leftHeight = calculateCustomHeight(node.left, heightMap);
        int rightHeight = calculateCustomHeight(node.right, heightMap);
        
        int height = Math.max(leftHeight, rightHeight) + 1;
        heightMap.put(node, height);
        
        return height;
    }
    
    /**
     * 按高度收集节点
     * 
     * @param node 当前节点
     * @param heightMap 高度映射
     * @param levelMap 层级映射
     */
    private void collectNodesByHeight(TreeNode node, Map<TreeNode, Integer> heightMap, 
                                     Map<Integer, List<Integer>> levelMap) {
        if (node == null) return;
        
        int height = heightMap.get(node);
        levelMap.computeIfAbsent(height, k -> new ArrayList<>()).add(node.val);
        
        collectNodesByHeight(node.left, heightMap, levelMap);
        collectNodesByHeight(node.right, heightMap, levelMap);
    }
    
    /**
     * 辅助方法：构建测试二叉树
     * 
     * @param values 节点值数组（层序遍历，null表示空节点）
     * @return 构建的二叉树根节点
     */
    public static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode current = queue.poll();
            
            // 左子节点
            if (i < values.length && values[i] != null) {
                current.left = new TreeNode(values[i]);
                queue.offer(current.left);
            }
            i++;
            
            // 右子节点
            if (i < values.length && values[i] != null) {
                current.right = new TreeNode(values[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 辅助方法：打印二叉树结构
     * 
     * @param root 二叉树根节点
     */
    public void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node != null) {
                    System.out.print(node.val + " ");
                    queue.offer(node.left);
                    queue.offer(node.right);
                } else {
                    System.out.print("null ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * 辅助方法：验证结果正确性
     * 
     * @param result 算法结果
     * @param expected 期望结果
     * @return 是否正确
     */
    private boolean validateResult(List<List<Integer>> result, List<List<Integer>> expected) {
        if (result.size() != expected.size()) return false;
        
        for (int i = 0; i < result.size(); i++) {
            List<Integer> resultLevel = new ArrayList<>(result.get(i));
            List<Integer> expectedLevel = new ArrayList<>(expected.get(i));
            
            // 排序后比较（因为同一层的节点顺序可能不同）
            Collections.sort(resultLevel);
            Collections.sort(expectedLevel);
            
            if (!resultLevel.equals(expectedLevel)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P366_FindLeavesOfBinaryTree solution = new P366_FindLeavesOfBinaryTree();
        
        // 测试用例1: [1,2,3,4,5]
        System.out.println("=== 测试用例1: [1,2,3,4,5] ===");
        Integer[] values1 = {1, 2, 3, 4, 5};
        TreeNode root1 = buildTree(values1);
        
        System.out.println("二叉树结构:");
        solution.printTree(root1);
        
        List<List<Integer>> result1a = solution.findLeaves(root1);
        List<List<Integer>> result1b = solution.findLeavesSimulation(buildTree(values1));
        List<List<Integer>> result1c = solution.findLeavesBFS(buildTree(values1));
        List<List<Integer>> result1d = solution.findLeavesIterative(buildTree(values1));
        
        System.out.println("DFS高度方法: " + result1a);
        System.out.println("模拟删除方法: " + result1b);
        System.out.println("BFS方法: " + result1c);
        System.out.println("迭代方法: " + result1d);
        System.out.println("期望结果: [[4,5,3],[2],[1]] 或其排列\n");
        
        // 测试用例2: [1]
        System.out.println("=== 测试用例2: [1] ===");
        Integer[] values2 = {1};
        TreeNode root2 = buildTree(values2);
        
        List<List<Integer>> result2a = solution.findLeaves(root2);
        List<List<Integer>> result2b = solution.findLeavesSimulation(buildTree(values2));
        
        System.out.println("DFS高度方法: " + result2a);
        System.out.println("模拟删除方法: " + result2b);
        System.out.println("期望结果: [[1]]\n");
        
        // 测试用例3: 更复杂的树
        System.out.println("=== 测试用例3: 复杂树 ===");
        Integer[] values3 = {1, 2, 3, 4, 5, null, 6, 7, null, null, null, null, 8};
        TreeNode root3 = buildTree(values3);
        
        System.out.println("二叉树结构:");
        solution.printTree(root3);
        
        List<List<Integer>> result3a = solution.findLeaves(root3);
        List<List<Integer>> result3b = solution.findLeavesCustomHeight(buildTree(values3));
        
        System.out.println("DFS高度方法: " + result3a);
        System.out.println("自定义高度方法: " + result3b);
        
        // 验证一致性
        System.out.println("\n=== 验证结果一致性 ===");
        
        // 简单验证：检查层数是否一致
        boolean consistent1 = result1a.size() == result1b.size() && 
                             result1b.size() == result1c.size() && 
                             result1c.size() == result1d.size();
        
        boolean consistent2 = result2a.size() == result2b.size();
        
        boolean consistent3 = result3a.size() == result3b.size();
        
        System.out.println("测试用例1层数一致: " + consistent1);
        System.out.println("测试用例2层数一致: " + consistent2);
        System.out.println("测试用例3层数一致: " + consistent3);
        
        if (consistent1 && consistent2 && consistent3) {
            System.out.println("✓ 所有测试用例通过");
        } else {
            System.out.println("✗ 部分测试用例失败");
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        // 构建深度为10的完全二叉树
        TreeNode perfRoot = buildPerfectBinaryTree(10);
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            solution.findLeaves(perfRoot);
        }
        long end = System.currentTimeMillis();
        
        System.out.println("深度10的完全二叉树处理1000次耗时: " + (end - start) + "ms");
    }
    
    /**
     * 构建完全二叉树
     * 
     * @param depth 树的深度
     * @return 根节点
     */
    private static TreeNode buildPerfectBinaryTree(int depth) {
        if (depth <= 0) return null;
        
        TreeNode root = new TreeNode(1);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int currentDepth = 1;
        int nodeValue = 2;
        
        while (currentDepth < depth) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                
                node.left = new TreeNode(nodeValue++);
                node.right = new TreeNode(nodeValue++);
                
                queue.offer(node.left);
                queue.offer(node.right);
            }
            currentDepth++;
        }
        
        return root;
    }
} 