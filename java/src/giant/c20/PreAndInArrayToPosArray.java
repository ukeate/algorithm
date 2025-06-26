package giant.c20;

import java.util.HashMap;

/**
 * 前序+中序遍历数组转后序遍历数组问题
 * 
 * 问题描述：
 * 给定二叉树的前序遍历数组和中序遍历数组，构造出该二叉树的后序遍历数组。
 * 不需要真正构建二叉树，直接通过数组操作得到结果。
 * 
 * 例如：
 * 前序遍历: [3, 9, 20, 15, 7]
 * 中序遍历: [9, 3, 15, 20, 7]
 * 对应的二叉树:
 *     3
 *    / \
 *   9   20
 *      /  \
 *     15   7
 * 后序遍历: [9, 15, 7, 20, 3]
 * 
 * 核心思想：
 * 1. 前序遍历的第一个元素总是根节点
 * 2. 在中序遍历中找到根节点位置，可以分割出左右子树
 * 3. 递归处理左右子树，后序遍历的顺序是：左子树 -> 右子树 -> 根
 * 4. 利用数组区间操作，避免实际构建树结构
 * 
 * 算法复杂度：
 * 时间复杂度：O(N)，每个节点访问一次
 * 空间复杂度：O(N)，哈希表存储中序遍历的位置信息 + 递归栈
 */
public class PreAndInArrayToPosArray {
    
    /**
     * 递归构造后序遍历数组
     * 
     * 算法思路：
     * 1. 前序遍历的第一个元素pre[l1]就是当前子树的根节点
     * 2. 在中序遍历中找到根节点的位置idx，将中序数组分为左右两部分
     * 3. 左子树：中序[l2...idx-1]，对应前序[l1+1...l1+idx-l2]
     * 4. 右子树：中序[idx+1...r2]，对应前序[l1+idx-l2+1...r1]
     * 5. 后序遍历顺序：先填充左子树，再填充右子树，最后放根节点
     * 
     * 参数说明：
     * @param pre 前序遍历数组
     * @param l1 前序数组当前处理的左边界
     * @param r1 前序数组当前处理的右边界
     * @param in 中序遍历数组
     * @param l2 中序数组当前处理的左边界
     * @param r2 中序数组当前处理的右边界
     * @param pos 后序遍历结果数组
     * @param l3 后序数组当前填充的左边界
     * @param r3 后序数组当前填充的右边界
     * @param inMap 中序遍历元素到位置的映射，用于O(1)查找
     */
    private static void process(int[] pre, int l1, int r1, int[] in, int l2, int r2, 
                               int[] pos, int l3, int r3, HashMap<Integer, Integer> inMap) {
        if (l1 > r1) {
            // 边界条件：没有节点需要处理
            return;
        }
        
        if (l1 == r1) {
            // 只有一个节点的情况，直接放入后序数组
            pos[l3] = pre[l1];
        } else {
            // 多个节点的情况，需要递归处理
            
            // 后序遍历的特点：根节点在最后，所以放在r3位置
            pos[r3] = pre[l1];
            
            // 在中序遍历中找到根节点的位置
            int idx = inMap.get(pre[l1]);
            
            // 计算左子树的节点数量
            int leftSize = idx - l2;
            
            // 递归处理左子树
            // 前序：[l1+1, l1+leftSize] 对应中序：[l2, idx-1]
            // 后序：[l3, l3+leftSize-1]
            process(pre, l1 + 1, l1 + leftSize, 
                   in, l2, idx - 1, 
                   pos, l3, l3 + leftSize - 1, inMap);
            
            // 递归处理右子树
            // 前序：[l1+leftSize+1, r1] 对应中序：[idx+1, r2]
            // 后序：[l3+leftSize, r3-1]
            process(pre, l1 + leftSize + 1, r1, 
                   in, idx + 1, r2, 
                   pos, l3 + leftSize, r3 - 1, inMap);
        }
    }

    /**
     * 将前序和中序遍历数组转换为后序遍历数组
     * 
     * 算法步骤：
     * 1. 验证输入的有效性
     * 2. 构建中序遍历的哈希映射，实现O(1)查找
     * 3. 初始化后序遍历结果数组
     * 4. 调用递归函数进行转换
     * 
     * 优化技巧：
     * - 使用HashMap预存储中序遍历的位置信息，避免每次线性查找
     * - 通过数组区间操作，避免创建大量子数组
     * - 递归时直接在目标数组上操作，节省空间
     * 
     * 时间复杂度：O(N)，每个节点处理一次，哈希查找O(1)
     * 空间复杂度：O(N)，哈希表 + 递归栈 + 结果数组
     * 
     * @param pre 前序遍历数组
     * @param in 中序遍历数组
     * @return 后序遍历数组，如果输入无效则返回null
     */
    public static int[] preInToPos1(int[] pre, int[] in) {
        // 输入验证
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        
        int n = pre.length;
        
        // 构建中序遍历的值到索引的映射表
        // 用于O(1)时间内查找根节点在中序遍历中的位置
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            inMap.put(in[i], i);
        }
        
        // 初始化后序遍历结果数组
        int[] pos = new int[n];
        
        // 开始递归转换
        process(pre, 0, n - 1, in, 0, n - 1, pos, 0, n - 1, inMap);
        
        return pos;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 前序+中序转后序遍历数组测试 ===");
        
        // 测试用例1：标准二叉树
        System.out.println("1. 标准二叉树测试:");
        int[] pre1 = {3, 9, 20, 15, 7};
        int[] in1 = {9, 3, 15, 20, 7};
        int[] pos1 = preInToPos1(pre1, in1);
        
        System.out.print("前序遍历: ");
        printArray(pre1);
        System.out.print("中序遍历: ");
        printArray(in1);
        System.out.print("后序遍历: ");
        printArray(pos1);
        System.out.println("期望后序: [9, 15, 7, 20, 3]");
        System.out.println();
        
        // 测试用例2：只有左子树的二叉树
        System.out.println("2. 左偏二叉树测试:");
        int[] pre2 = {1, 2, 3};
        int[] in2 = {3, 2, 1};
        int[] pos2 = preInToPos1(pre2, in2);
        
        System.out.print("前序遍历: ");
        printArray(pre2);
        System.out.print("中序遍历: ");
        printArray(in2);
        System.out.print("后序遍历: ");
        printArray(pos2);
        System.out.println("期望后序: [3, 2, 1]");
        System.out.println();
        
        // 测试用例3：只有右子树的二叉树
        System.out.println("3. 右偏二叉树测试:");
        int[] pre3 = {1, 2, 3};
        int[] in3 = {1, 2, 3};
        int[] pos3 = preInToPos1(pre3, in3);
        
        System.out.print("前序遍历: ");
        printArray(pre3);
        System.out.print("中序遍历: ");
        printArray(in3);
        System.out.print("后序遍历: ");
        printArray(pos3);
        System.out.println("期望后序: [3, 2, 1]");
        System.out.println();
        
        // 测试用例4：单节点二叉树
        System.out.println("4. 单节点二叉树测试:");
        int[] pre4 = {1};
        int[] in4 = {1};
        int[] pos4 = preInToPos1(pre4, in4);
        
        System.out.print("前序遍历: ");
        printArray(pre4);
        System.out.print("中序遍历: ");
        printArray(in4);
        System.out.print("后序遍历: ");
        printArray(pos4);
        System.out.println("期望后序: [1]");
        System.out.println();
        
        // 测试用例5：输入无效的情况
        System.out.println("5. 异常输入测试:");
        int[] result = preInToPos1(null, new int[]{1, 2});
        System.out.println("null输入结果: " + (result == null ? "null (正确)" : "错误"));
        
        result = preInToPos1(new int[]{1, 2}, new int[]{1});
        System.out.println("长度不匹配结果: " + (result == null ? "null (正确)" : "错误"));
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 打印数组的辅助方法
     */
    private static void printArray(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
