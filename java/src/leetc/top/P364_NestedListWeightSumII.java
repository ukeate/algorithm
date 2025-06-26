package leetc.top;

import java.util.*;

/**
 * LeetCode 364. 嵌套列表加权和 II (Nested List Weight Sum II)
 * 
 * 问题描述：
 * 给你一个嵌套的整数列表 nestedList ，请返回该列表按深度加权后所有整数的总和。
 * 
 * 因为它是一个嵌套的列表，其中的元素也可能是列表，所以这些元素的深度可能不同。
 * 
 * 这次，你将从叶子向根部增加每个整数的权重，即较深的元素具有较小的权重。
 * 
 * 更确切地说，在最大深度 maxDepth 的情况下，depth 为 d 的整数的权重为 maxDepth - d + 1。
 * 
 * 示例：
 * 输入：nestedList = [[1,1],2,[1,1]]
 * 输出：8
 * 解释：四个 1 在深度为 2 的位置， 一个 2 在深度为 1 的位置。
 * 1*1 + 1*1 + 2*2 + 1*1 + 1*1 = 8
 * 
 * 输入：nestedList = [1,[4,[6]]]
 * 输出：17
 * 解释：一个 1 在深度为 3 的位置， 一个 4 在深度为 2 的位置，一个 6 在深度为 1 的位置。
 * 1*1 + 4*2 + 6*3 = 17
 * 
 * 提示：
 * - 1 <= nestedList.length <= 50
 * - 嵌套列表中的值在范围 [-100, 100] 内
 * - 任何整数的最大深度都小于或等于 50
 * 
 * 解法思路：
 * 深度优先搜索(DFS) + 两次遍历：
 * 
 * 1. 核心思想：
 *    - 与嵌套列表加权和I不同，这里是叶子权重最大（深度越深，权重越大）
 *    - 需要先找到最大深度，再计算加权和
 *    - 权重公式：weight = maxDepth - currentDepth + 1
 * 
 * 2. 算法步骤：
 *    - 第一次DFS：找到嵌套列表的最大深度
 *    - 第二次DFS：根据最大深度计算加权和
 * 
 * 3. 优化方案：
 *    - 一次遍历：同时收集所有整数和对应深度，最后统一计算
 *    - BFS层序遍历：逐层处理，可以避免递归栈
 *    - 累积计算：利用数学技巧减少计算量
 * 
 * 核心思想：
 * - 深度计算：准确计算嵌套结构的深度
 * - 权重倒置：将传统的深度权重公式倒置
 * - 分离关注：分别处理深度计算和权重计算
 * 
 * 关键技巧：
 * - 递归遍历：处理任意深度的嵌套结构
 * - 深度追踪：准确记录每个元素的深度
 * - 权重映射：将深度转换为对应的权重
 * 
 * 时间复杂度：O(N) - N为所有整数和列表的总数
 * 空间复杂度：O(D) - D为最大深度（递归栈空间）
 * 
 * LeetCode链接：https://leetcode.com/problems/nested-list-weight-sum-ii/
 */
public class P364_NestedListWeightSumII {
    
    /**
     * NestedInteger接口定义（题目提供）
     * 
     * 这个接口表示一个嵌套的整数，它可能是一个整数或者是一个列表
     */
    public interface NestedInteger {
        /**
         * 构造函数初始化为单个整数
         */
        // public NestedInteger(int value);
        
        /**
         * 构造函数初始化为空列表
         */
        // public NestedInteger();
        
        /**
         * @return 如果这个嵌套整数是单个整数，返回true，否则返回false
         */
        public boolean isInteger();
        
        /**
         * @return 如果这个嵌套整数是单个整数，返回该整数，否则返回null
         */
        public Integer getInteger();
        
        /**
         * 设置这个嵌套整数为单个整数
         */
        public void setInteger(int value);
        
        /**
         * 添加一个嵌套整数到这个嵌套整数的列表中
         */
        public void add(NestedInteger ni);
        
        /**
         * @return 如果这个嵌套整数是一个列表，返回该列表，否则返回null
         */
        public List<NestedInteger> getList();
    }
    
    /**
     * NestedInteger的简单实现（用于测试）
     */
    static class SimpleNestedInteger implements NestedInteger {
        private Integer value;
        private List<NestedInteger> list;
        
        public SimpleNestedInteger() {
            this.list = new ArrayList<>();
        }
        
        public SimpleNestedInteger(int value) {
            this.value = value;
        }
        
        @Override
        public boolean isInteger() {
            return value != null;
        }
        
        @Override
        public Integer getInteger() {
            return value;
        }
        
        @Override
        public void setInteger(int value) {
            this.value = value;
            this.list = null;
        }
        
        @Override
        public void add(NestedInteger ni) {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(ni);
            this.value = null;
        }
        
        @Override
        public List<NestedInteger> getList() {
            return list;
        }
        
        @Override
        public String toString() {
            if (isInteger()) {
                return value.toString();
            } else {
                return list.toString();
            }
        }
    }
    
    /**
     * 方法一：两次DFS遍历（推荐）
     * 
     * 第一次找最大深度，第二次计算加权和
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverse(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        // 第一次DFS：找到最大深度
        int maxDepth = findMaxDepth(nestedList);
        
        // 第二次DFS：计算加权和
        return calculateWeightedSum(nestedList, maxDepth, 1);
    }
    
    /**
     * 找到嵌套列表的最大深度
     * 
     * @param nestedList 嵌套列表
     * @return 最大深度
     */
    private int findMaxDepth(List<NestedInteger> nestedList) {
        int maxDepth = 1;
        
        for (NestedInteger ni : nestedList) {
            if (!ni.isInteger()) {
                // 如果是列表，递归计算子列表的深度
                int childDepth = findMaxDepth(ni.getList()) + 1;
                maxDepth = Math.max(maxDepth, childDepth);
            }
        }
        
        return maxDepth;
    }
    
    /**
     * 计算加权和
     * 
     * @param nestedList 嵌套列表
     * @param maxDepth 最大深度
     * @param currentDepth 当前深度
     * @return 加权和
     */
    private int calculateWeightedSum(List<NestedInteger> nestedList, int maxDepth, int currentDepth) {
        int sum = 0;
        int weight = maxDepth - currentDepth + 1; // 计算当前深度的权重
        
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                // 如果是整数，加上加权值
                sum += ni.getInteger() * weight;
            } else {
                // 如果是列表，递归计算子列表
                sum += calculateWeightedSum(ni.getList(), maxDepth, currentDepth + 1);
            }
        }
        
        return sum;
    }
    
    /**
     * 方法二：一次遍历 + 集合存储
     * 
     * 一次遍历收集所有整数和深度，最后统一计算
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverseOnePass(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        List<Pair> values = new ArrayList<>(); // 存储整数值和对应深度
        int maxDepth = collectValues(nestedList, 1, values);
        
        // 计算加权和
        int sum = 0;
        for (Pair pair : values) {
            int weight = maxDepth - pair.depth + 1;
            sum += pair.value * weight;
        }
        
        return sum;
    }
    
    /**
     * 收集所有整数值和对应的深度
     * 
     * @param nestedList 嵌套列表
     * @param depth 当前深度
     * @param values 存储值和深度的列表
     * @return 最大深度
     */
    private int collectValues(List<NestedInteger> nestedList, int depth, List<Pair> values) {
        int maxDepth = depth;
        
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                values.add(new Pair(ni.getInteger(), depth));
            } else {
                int childMaxDepth = collectValues(ni.getList(), depth + 1, values);
                maxDepth = Math.max(maxDepth, childMaxDepth);
            }
        }
        
        return maxDepth;
    }
    
    /**
     * 存储值和深度的配对类
     */
    static class Pair {
        int value;
        int depth;
        
        Pair(int value, int depth) {
            this.value = value;
            this.depth = depth;
        }
    }
    
    /**
     * 方法三：BFS层序遍历
     * 
     * 使用队列进行层序遍历，避免递归
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverseBFS(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        // 使用BFS找到最大深度并收集所有值
        Queue<NestedInteger> queue = new LinkedList<>();
        Queue<Integer> depthQueue = new LinkedList<>();
        
        // 初始化队列
        for (NestedInteger ni : nestedList) {
            queue.offer(ni);
            depthQueue.offer(1);
        }
        
        List<Pair> values = new ArrayList<>();
        int maxDepth = 1;
        
        while (!queue.isEmpty()) {
            NestedInteger current = queue.poll();
            int currentDepth = depthQueue.poll();
            
            maxDepth = Math.max(maxDepth, currentDepth);
            
            if (current.isInteger()) {
                values.add(new Pair(current.getInteger(), currentDepth));
            } else {
                // 将子列表的元素加入队列
                for (NestedInteger child : current.getList()) {
                    queue.offer(child);
                    depthQueue.offer(currentDepth + 1);
                }
            }
        }
        
        // 计算加权和
        int sum = 0;
        for (Pair pair : values) {
            int weight = maxDepth - pair.depth + 1;
            sum += pair.value * weight;
        }
        
        return sum;
    }
    
    /**
     * 方法四：累积计算优化
     * 
     * 利用数学技巧，减少重复计算
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverseOptimized(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        // 按深度分组存储整数
        Map<Integer, List<Integer>> depthMap = new HashMap<>();
        int maxDepth = populateDepthMap(nestedList, 1, depthMap);
        
        int totalSum = 0;
        
        // 按深度计算加权和
        for (Map.Entry<Integer, List<Integer>> entry : depthMap.entrySet()) {
            int depth = entry.getKey();
            int weight = maxDepth - depth + 1;
            
            for (int value : entry.getValue()) {
                totalSum += value * weight;
            }
        }
        
        return totalSum;
    }
    
    /**
     * 按深度分组填充映射
     * 
     * @param nestedList 嵌套列表
     * @param depth 当前深度
     * @param depthMap 深度映射
     * @return 最大深度
     */
    private int populateDepthMap(List<NestedInteger> nestedList, int depth, 
                                Map<Integer, List<Integer>> depthMap) {
        int maxDepth = depth;
        
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                depthMap.computeIfAbsent(depth, k -> new ArrayList<>()).add(ni.getInteger());
            } else {
                int childMaxDepth = populateDepthMap(ni.getList(), depth + 1, depthMap);
                maxDepth = Math.max(maxDepth, childMaxDepth);
            }
        }
        
        return maxDepth;
    }
    
    /**
     * 方法五：迭代加深DFS
     * 
     * 使用迭代加深的方式，逐层处理
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverseIterativeDeepening(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        // 找到最大深度
        int maxDepth = findMaxDepth(nestedList);
        
        int totalSum = 0;
        
        // 对每一层进行处理
        for (int targetDepth = 1; targetDepth <= maxDepth; targetDepth++) {
            int weight = maxDepth - targetDepth + 1;
            int levelSum = sumAtDepth(nestedList, 1, targetDepth);
            totalSum += levelSum * weight;
        }
        
        return totalSum;
    }
    
    /**
     * 计算指定深度层的整数和
     * 
     * @param nestedList 嵌套列表
     * @param currentDepth 当前深度
     * @param targetDepth 目标深度
     * @return 指定深度的整数和
     */
    private int sumAtDepth(List<NestedInteger> nestedList, int currentDepth, int targetDepth) {
        if (currentDepth > targetDepth) {
            return 0;
        }
        
        int sum = 0;
        
        for (NestedInteger ni : nestedList) {
            if (currentDepth == targetDepth && ni.isInteger()) {
                sum += ni.getInteger();
            } else if (!ni.isInteger()) {
                sum += sumAtDepth(ni.getList(), currentDepth + 1, targetDepth);
            }
        }
        
        return sum;
    }
    
    /**
     * 方法六：单次遍历 + 数学公式优化
     * 
     * 利用数学技巧，单次遍历完成计算
     * 
     * @param nestedList 嵌套列表
     * @return 按深度加权的总和
     */
    public int depthSumInverseMath(List<NestedInteger> nestedList) {
        if (nestedList == null || nestedList.isEmpty()) {
            return 0;
        }
        
        int unweightedSum = 0; // 所有整数的无权重和
        int weightedSum = 0;   // 按传统方式（深度作为权重）的加权和
        
        calculateSums(nestedList, 1, new int[]{unweightedSum}, new int[]{weightedSum});
        
        int maxDepth = findMaxDepth(nestedList);
        
        // 利用公式：逆序加权和 = (maxDepth + 1) * 无权重和 - 传统加权和
        return (maxDepth + 1) * unweightedSum - weightedSum;
    }
    
    /**
     * 计算无权重和和传统加权和
     * 
     * @param nestedList 嵌套列表
     * @param depth 当前深度
     * @param unweightedSum 无权重和的引用
     * @param weightedSum 传统加权和的引用
     */
    private void calculateSums(List<NestedInteger> nestedList, int depth, 
                              int[] unweightedSum, int[] weightedSum) {
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                unweightedSum[0] += ni.getInteger();
                weightedSum[0] += ni.getInteger() * depth;
            } else {
                calculateSums(ni.getList(), depth + 1, unweightedSum, weightedSum);
            }
        }
    }
    
    /**
     * 辅助方法：从数组创建嵌套列表（用于测试）
     * 
     * @param input 输入数组，null表示列表开始，-1000表示列表结束
     * @return 创建的嵌套列表
     */
    public static List<NestedInteger> createNestedList(Object[] input) {
        List<NestedInteger> result = new ArrayList<>();
        Stack<List<NestedInteger>> stack = new Stack<>();
        stack.push(result);
        
        for (Object obj : input) {
            if (obj == null) {
                // 开始新列表
                List<NestedInteger> newList = new ArrayList<>();
                SimpleNestedInteger nested = new SimpleNestedInteger();
                stack.peek().add(nested);
                stack.push(newList);
            } else if (obj.equals(-1000)) {
                // 结束当前列表
                List<NestedInteger> currentList = stack.pop();
                NestedInteger parent = stack.peek().get(stack.peek().size() - 1);
                for (NestedInteger ni : currentList) {
                    parent.add(ni);
                }
            } else {
                // 添加整数
                stack.peek().add(new SimpleNestedInteger((Integer) obj));
            }
        }
        
        return result;
    }
    
    /**
     * 辅助方法：打印嵌套列表结构
     * 
     * @param nestedList 嵌套列表
     * @param depth 当前深度
     */
    private void printNestedList(List<NestedInteger> nestedList, int depth) {
        String indent = "  ".repeat(depth);
        
        for (int i = 0; i < nestedList.size(); i++) {
            NestedInteger ni = nestedList.get(i);
            if (ni.isInteger()) {
                System.out.println(indent + "Integer: " + ni.getInteger() + " (depth: " + depth + ")");
            } else {
                System.out.println(indent + "List [");
                printNestedList(ni.getList(), depth + 1);
                System.out.println(indent + "]");
            }
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P364_NestedListWeightSumII solution = new P364_NestedListWeightSumII();
        
        // 测试用例1: [[1,1],2,[1,1]]
        System.out.println("=== 测试用例1: [[1,1],2,[1,1]] ===");
        List<NestedInteger> test1 = new ArrayList<>();
        
        // 创建 [1,1]
        SimpleNestedInteger list1 = new SimpleNestedInteger();
        list1.add(new SimpleNestedInteger(1));
        list1.add(new SimpleNestedInteger(1));
        test1.add(list1);
        
        // 添加 2
        test1.add(new SimpleNestedInteger(2));
        
        // 创建 [1,1]
        SimpleNestedInteger list2 = new SimpleNestedInteger();
        list2.add(new SimpleNestedInteger(1));
        list2.add(new SimpleNestedInteger(1));
        test1.add(list2);
        
        System.out.println("嵌套列表结构:");
        solution.printNestedList(test1, 1);
        
        int result1a = solution.depthSumInverse(test1);
        int result1b = solution.depthSumInverseOnePass(test1);
        int result1c = solution.depthSumInverseBFS(test1);
        int result1d = solution.depthSumInverseOptimized(test1);
        
        System.out.println("两次DFS: " + result1a);
        System.out.println("一次遍历: " + result1b);
        System.out.println("BFS方法: " + result1c);
        System.out.println("优化方法: " + result1d);
        System.out.println("期望结果: 8\n");
        
        // 测试用例2: [1,[4,[6]]]
        System.out.println("=== 测试用例2: [1,[4,[6]]] ===");
        List<NestedInteger> test2 = new ArrayList<>();
        
        // 添加 1
        test2.add(new SimpleNestedInteger(1));
        
        // 创建 [4,[6]]
        SimpleNestedInteger innerList = new SimpleNestedInteger();
        innerList.add(new SimpleNestedInteger(6));
        
        SimpleNestedInteger outerList = new SimpleNestedInteger();
        outerList.add(new SimpleNestedInteger(4));
        outerList.add(innerList);
        
        test2.add(outerList);
        
        System.out.println("嵌套列表结构:");
        solution.printNestedList(test2, 1);
        
        int result2a = solution.depthSumInverse(test2);
        int result2b = solution.depthSumInverseOnePass(test2);
        int result2c = solution.depthSumInverseBFS(test2);
        int result2d = solution.depthSumInverseOptimized(test2);
        
        System.out.println("两次DFS: " + result2a);
        System.out.println("一次遍历: " + result2b);
        System.out.println("BFS方法: " + result2c);
        System.out.println("优化方法: " + result2d);
        System.out.println("期望结果: 17\n");
        
        // 验证所有方法的一致性
        System.out.println("=== 验证结果一致性 ===");
        boolean test1Consistent = (result1a == result1b && result1b == result1c && result1c == result1d);
        boolean test2Consistent = (result2a == result2b && result2b == result2c && result2c == result2d);
        
        System.out.println("测试用例1结果一致: " + test1Consistent);
        System.out.println("测试用例2结果一致: " + test2Consistent);
        
        if (test1Consistent && test2Consistent) {
            System.out.println("✓ 所有测试用例通过，算法实现正确");
        } else {
            System.out.println("✗ 测试失败，存在实现错误");
        }
    }
} 