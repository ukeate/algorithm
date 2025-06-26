package basic.c36;

/**
 * 最大子数组异或和问题
 * 
 * 问题描述：
 * 给定一个整数数组，找到其中异或和最大的连续子数组，返回最大异或和。
 * 连续子数组至少包含一个元素。
 * 
 * 核心性质：
 * 异或运算有个重要性质：A ^ A = 0, A ^ 0 = A
 * 利用前缀异或和，子数组[i,j]的异或和 = prefixXor[j] ^ prefixXor[i-1]
 * 
 * 算法思路：
 * 方法1：暴力枚举所有子数组 - O(N²)
 * 方法2：前缀异或和 + 前缀树优化 - O(N*logV)，V是数值范围
 * 
 * 前缀树优化的核心思想：
 * 对于每个前缀异或和，在前缀树中找到能产生最大异或值的前缀
 * 贪心策略：从高位到低位，优先选择能使结果位为1的路径
 */
public class MaxSubEOR {
    
    /**
     * 方法1：暴力解法
     * 使用前缀异或和数组，枚举所有可能的子数组
     * 
     * 时间复杂度：O(N²)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最大子数组异或和
     */
    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 构建前缀异或和数组
        int[] eor = new int[arr.length];
        eor[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            eor[i] = eor[i - 1] ^ arr[i];
        }
        
        int max = Integer.MIN_VALUE;
        
        // 枚举所有子数组[i, j]
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i <= j; i++) {
                // 子数组[i, j]的异或和
                // 如果i=0，直接用eor[j]；否则用eor[j] ^ eor[i-1]
                int subXor = (i == 0) ? eor[j] : (eor[j] ^ eor[i - 1]);
                max = Math.max(max, subXor);
            }
        }
        
        return max;
    }

    /**
     * 前缀树节点类
     * 每个节点有两个子节点，分别对应二进制的0和1
     */
    private static class Node {
        public Node[] nexts = new Node[2];  // nexts[0]表示0路径，nexts[1]表示1路径
    }

    /**
     * 数字前缀树类
     * 专门用于存储和查询数字的二进制表示
     */
    private static class NumTrie {
        public Node head = new Node();  // 根节点

        /**
         * 向前缀树中添加一个数字
         * 
         * @param newNum 要添加的数字
         */
        public void add(int newNum) {
            Node cur = head;
            
            // 从最高位（第31位）到最低位遍历
            for (int move = 31; move >= 0; move--) {
                int path = ((newNum >> move) & 1);  // 取出当前位的值（0或1）
                
                // 如果路径不存在，创建新节点
                cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
                cur = cur.nexts[path];
            }
        }

        /**
         * 在前缀树中找到与给定数字异或能得到最大值的数字
         * 
         * 贪心策略：
         * 1. 对于符号位（第31位），希望异或结果为0（正数）
         * 2. 对于其他位，希望异或结果为1（使数值更大）
         * 
         * @param sum 给定的数字
         * @return 与sum异或能得到的最大值
         */
        public int maxXor(int sum) {
            Node cur = head;
            int res = 0;
            
            // 从最高位到最低位贪心选择
            for (int move = 31; move >= 0; move--) {
                int path = (sum >> move) & 1;  // sum在当前位的值
                
                // 期望的路径选择
                int best = (move == 31) ? path : (path ^ 1);
                // 第31位是符号位，希望结果为正数，所以选择相同路径
                // 其他位希望异或结果为1，所以选择相反路径
                
                // 实际能走的路径（如果期望路径不存在，走另一条）
                best = cur.nexts[best] != null ? best : (best ^ 1);
                
                // 更新结果：当前位的异或结果
                res |= (path ^ best) << move;
                
                // 移动到下一个节点
                cur = cur.nexts[best];
            }
            
            return res;
        }
    }

    /**
     * 方法2：前缀树优化解法
     * 使用前缀异或和 + 前缀树，高效找到最大异或值
     * 
     * 算法步骤：
     * 1. 初始化前缀树，加入0（代表空前缀）
     * 2. 遍历数组，计算前缀异或和
     * 3. 在前缀树中查找能与当前前缀异或和产生最大值的前缀
     * 4. 将当前前缀异或和加入前缀树
     * 
     * 时间复杂度：O(N*logV)，V是数值的位数（这里是32）
     * 空间复杂度：O(N*logV)
     * 
     * @param arr 输入数组
     * @return 最大子数组异或和
     */
    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int max = Integer.MIN_VALUE;
        int eor = 0;  // 前缀异或和
        NumTrie trie = new NumTrie();
        
        // 先加入0，代表空前缀（用于处理从开头开始的子数组）
        trie.add(0);
        
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];  // 更新前缀异或和
            
            // 在前缀树中找到与当前前缀异或和能产生最大值的前缀
            max = Math.max(max, trie.maxXor(eor));
            
            // 将当前前缀异或和加入前缀树
            trie.add(eor);
        }
        
        return max;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大绝对值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
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

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最大子数组异或和测试 ===");
        
        // 手动测试用例
        int[] testArr = {3, 10, 5, 25, 2, 8};
        System.out.println("手动测试用例:");
        print(testArr);
        System.out.println("暴力解法结果: " + max1(testArr));
        System.out.println("前缀树解法结果: " + max2(testArr));
        System.out.println();
        
        // 大规模随机测试
        int times = 5000000;
        int maxLen = 30;
        int maxVal = 50;
        
        System.out.println("开始大规模随机测试...");
        System.out.println("测试次数: " + times);
        System.out.println("最大数组长度: " + maxLen);
        System.out.println("最大数值: " + maxVal);
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = max1(arr);  // 暴力解法
            int ans2 = max2(arr);  // 前缀树解法
            
            if (ans1 != ans2) {
                System.out.println("发现错误!");
                System.out.print("错误数组: ");
                print(arr);
                System.out.println("暴力解法: " + ans1 + " | 前缀树解法: " + ans2);
                break;
            }
            
            // 每100万次输出进度
            if ((i + 1) % 1000000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("测试完成！");
    }
}
