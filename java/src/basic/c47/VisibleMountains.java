package basic.c47;

import java.util.HashSet;
import java.util.Stack;

/**
 * 环形山可见对数问题
 * 
 * 问题描述：
 * 给定一个代表环形山高度的数组arr，站在每个山峰上，问有多少对山峰之间可以相互看见
 * 两个山峰能相互看见的条件：它们之间的路径上没有更高的山峰阻挡
 * 
 * 关键约束：
 * 1. 数组是环形的，即arr[0]和arr[n-1]相邻
 * 2. 可以沿着环形路径从两个方向到达另一个山峰
 * 3. 只要有一个方向的路径上没有更高的山峰，两座山就能相互看见
 * 
 * 例子：
 * arr = [3, 1, 2, 4, 5]
 * - 山峰3和山峰1可见（直接相邻）
 * - 山峰3和山峰2可见（路径3->1->2，1不高于3和2）
 * - 其他情况类似分析...
 * 
 * 解法思路：
 * 方法1：暴力检验法 - 对每对山峰检查是否可见 O(n³)
 * 方法2：单调栈优化法 - 利用单调栈的性质 O(n)
 * 
 * 核心观察：
 * 1. 如果所有山峰高度不同，答案固定为2*(n-2) + 1 = 2n-3
 * 2. 相同高度的山峰之间需要特殊处理
 * 3. 单调栈可以高效处理"小找大"的配对关系
 * 
 * 算法精华：
 * 使用单调栈处理，当较小的山峰被弹出时，计算它的可见对数
 * - 内部配对：相同高度山峰之间的配对
 * - 外部配对：与栈中其他山峰的配对
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @author 算法学习
 */
public class VisibleMountains {
    
    /**
     * 记录相同高度山峰的信息
     */
    private static class Record {
        public int val;    // 山峰高度值
        public int times;  // 相同高度的山峰数量
        
        public Record(int v) {
            val = v;
            times = 1;
        }
    }

    /**
     * 获取环形数组的下一个位置
     * 
     * @param i 当前位置
     * @param size 数组大小
     * @return 下一个位置的索引
     */
    private static int nextIdx(int i, int size) {
        return i < (size - 1) ? (i + 1) : 0;
    }

    /**
     * 计算k个相同高度山峰内部的可见对数
     * 
     * @param k 相同高度的山峰数量
     * @return 内部可见对数
     * 
     * 算法原理：
     * k个相同高度的山峰，任意两个都可以相互看见
     * 总的配对数是C(k,2) = k*(k-1)/2
     * 但是由于环形结构，每对山峰之间有两条路径
     * 所以实际的可见对数是k*(k-1)/2，但这里返回的是边数？
     * 
     * 注意：当k=1时返回0，符合单个山峰没有内部配对的逻辑
     */
    private static int getInternalSum(int k) {
        return k == 1 ? 0 : (k * (k - 1) / 2);
    }

    /**
     * 方法2：单调栈优化解法（主要算法）
     * 
     * @param arr 环形山高度数组
     * @return 可见的山峰对数
     * 
     * 算法思路：
     * 1. 找到最高的山峰作为起始点（保证算法的正确性）
     * 2. 使用单调递减栈，从起始点按环形顺序遍历
     * 3. 当遇到更高的山峰时，弹出栈中较低的山峰，并计算它们的可见对数
     * 4. 处理栈中剩余的山峰（特殊情况）
     * 
     * 核心思想：
     * - 被弹出的山峰，它的可见关系已经确定
     * - 内部配对：相同高度山峰之间的配对
     * - 外部配对：与栈中其他山峰的配对（通常是2个，即栈顶的下方山峰）
     */
    public static int num(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        
        // 找到最高山峰的位置（如果有多个最高点，选择任意一个）
        int maxIdx = 0;
        for (int i = 0; i < n; i++) {
            maxIdx = arr[maxIdx] < arr[i] ? i : maxIdx;
        }
        
        // 单调递减栈
        Stack<Record> stack = new Stack<>();
        stack.push(new Record(arr[maxIdx]));  // 最高山峰入栈
        
        int idx = nextIdx(maxIdx, n);  // 从最高山峰的下一个位置开始
        int res = 0;  // 可见对数计数器
        
        // 环形遍历，直到回到起始点
        while (idx != maxIdx) {
            // 当前山峰比栈顶山峰高时，弹出栈顶
            while (stack.peek().val < arr[idx]) {
                int k = stack.pop().times;
                // 被弹出山峰的可见对数 = 内部配对 + 外部配对
                // 外部配对：每个被弹出的山峰都能看到栈中剩余的2个山峰
                res += getInternalSum(k) + 2 * k;
            }
            
            if (stack.peek().val == arr[idx]) {
                // 高度相同，合并到栈顶记录
                stack.peek().times++;
            } else {
                // 高度更高，新建记录入栈
                stack.push(new Record(arr[idx]));
            }
            
            idx = nextIdx(idx, n);
        }
        
        // 处理栈中剩余的山峰（除了栈底的最高山峰）
        while (stack.size() > 2) {
            int times = stack.pop().times;
            // 栈中间的山峰，外部配对数量是2*times
            res += getInternalSum(times) + 2 * times;
        }
        
        // 处理栈中最后两个记录的特殊情况
        if (stack.size() == 2) {
            int times = stack.pop().times;
            // 次高山峰的配对数：内部配对 + 外部配对
            // 外部配对数量取决于栈底（最高山峰）的数量
            res += getInternalSum(times) + (stack.peek().times == 1 ? times : 2 * times);
        }
        
        // 处理栈底最高山峰的内部配对
        res += getInternalSum(stack.pop().times);
        
        return res;
    }

    /**
     * 获取环形数组的前一个位置
     * 
     * @param i 当前位置
     * @param size 数组大小
     * @return 前一个位置的索引
     */
    private static int lastIdx(int i, int size) {
        return i > 0 ? (i - 1) : (size - 1);
    }

    /**
     * 检查两个山峰之间是否可见
     * 
     * @param arr 山峰高度数组
     * @param lowIdx 较低山峰的索引
     * @param highIdx 较高山峰的索引
     * @return 如果可见返回true，否则返回false
     * 
     * 算法思路：
     * 由于数组是环形的，有两条路径连接两个山峰
     * 只要其中一条路径上没有比较低山峰更高的山峰，两座山就可见
     */
    private static boolean isVisible(int[] arr, int lowIdx, int highIdx) {
        // 确保lowIdx对应的山峰不高于highIdx对应的山峰
        if (arr[lowIdx] > arr[highIdx]) {
            return false;
        }
        
        int size = arr.length;
        
        // 检查顺时针路径
        boolean walkNext = true;
        int mid = nextIdx(lowIdx, size);
        while (mid != highIdx) {
            if (arr[mid] > arr[lowIdx]) {
                walkNext = false;  // 路径被阻挡
                break;
            }
            mid = nextIdx(mid, size);
        }
        
        // 检查逆时针路径
        boolean walkLast = true;
        mid = lastIdx(lowIdx, size);
        while (mid != highIdx) {
            if (arr[mid] > arr[lowIdx]) {
                walkLast = false;  // 路径被阻挡
                break;
            }
            mid = lastIdx(mid, size);
        }
        
        // 只要有一条路径畅通就可见
        return walkNext || walkLast;
    }

    /**
     * 计算从指定山峰能看到的其他山峰数量
     * 
     * @param arr 山峰高度数组
     * @param idx 指定山峰的索引
     * @param equalCounted 用于避免相同高度山峰的重复计数
     * @return 从该山峰能看到的其他山峰数量
     */
    private static int visibleNum(int[] arr, int idx, HashSet<String> equalCounted) {
        int res = 0;
        
        for (int i = 0; i < arr.length; i++) {
            if (i != idx) {
                if (arr[i] == arr[idx]) {
                    // 相同高度的山峰：避免重复计算
                    String key = Math.min(idx, i) + "_" + Math.max(idx, i);
                    if (equalCounted.add(key) && isVisible(arr, idx, i)) {
                        res++;
                    }
                } else if (isVisible(arr, idx, i)) {
                    // 不同高度的山峰：直接检查可见性
                    res++;
                }
            }
        }
        
        return res;
    }

    /**
     * 方法1：暴力验证解法（用于对比验证）
     * 
     * @param arr 环形山高度数组
     * @return 可见的山峰对数
     * 
     * 算法思路：
     * 枚举所有山峰，计算每个山峰能看到的其他山峰数量
     * 使用集合避免相同高度山峰的重复计数
     * 最终结果需要除以2（因为每对山峰被计算了两次）
     * 
     * 时间复杂度：O(n³) - 对每对山峰检查路径
     * 空间复杂度：O(n²) - 存储已计算的山峰对
     */
    public static int numSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int res = 0;
        HashSet<String> equalCounted = new HashSet<>();
        
        // 枚举每个山峰
        for (int i = 0; i < arr.length; i++) {
            res += visibleNum(arr, i, equalCounted);
        }
        
        return res;
    }

    /**
     * 生成随机测试数组
     */
    private static int[] randomArr(int size, int max) {
        int[] arr = new int[(int) (size * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (max * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 环形山可见对数问题测试 ===");
        
        // 手工测试用例
        int[] testCase1 = {3, 1, 2, 4, 5};
        System.out.println("测试用例1: [3, 1, 2, 4, 5]");
        System.out.println("优化算法结果: " + num(testCase1));
        System.out.println("暴力算法结果: " + numSure(testCase1));
        
        int[] testCase2 = {1, 2, 4, 5, 3};
        System.out.println("\n测试用例2: [1, 2, 4, 5, 3]");
        System.out.println("优化算法结果: " + num(testCase2));
        System.out.println("暴力算法结果: " + numSure(testCase2));
        
        // 特殊情况测试
        int[] testCase3 = {5, 5, 5, 5};  // 所有山峰等高
        System.out.println("\n测试用例3: [5, 5, 5, 5] (所有等高)");
        System.out.println("优化算法结果: " + num(testCase3));
        System.out.println("暴力算法结果: " + numSure(testCase3));
        
        int[] testCase4 = {1, 2, 3, 4, 5};  // 严格递增
        System.out.println("\n测试用例4: [1, 2, 3, 4, 5] (严格递增)");
        System.out.println("优化算法结果: " + num(testCase4));
        System.out.println("暴力算法结果: " + numSure(testCase4));
        
        // 大规模随机测试
        System.out.println("\n=== 大规模随机测试 ===");
        int times = 3000000;
        int size = 10;     // 数组长度上限
        int max = 10;      // 山峰高度上限
        
        System.out.println("测试参数：");
        System.out.println("测试次数: " + times);
        System.out.println("数组长度: 0-" + size);
        System.out.println("山峰高度: 0-" + max);
        
        System.out.println("\n开始测试...");
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(size, max);
            
            if (num(arr) != numSure(arr)) {
                System.out.println("测试失败！");
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("优化算法: " + num(arr));
                System.out.println("暴力算法: " + numSure(arr));
                return;
            }
            
            // 每完成50万次测试输出进度
            if ((i + 1) % 500000 == 0) {
                System.out.printf("已完成 %d 次测试\n", i + 1);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("测试完成！耗时 %d ms\n", endTime - startTime);
        System.out.println("所有测试通过，算法实现正确！");
        
        // 性能对比
        System.out.println("\n=== 性能对比测试 ===");
        int[] largeArr = randomArr(1000, 100);
        
        startTime = System.currentTimeMillis();
        int result1 = num(largeArr);
        long time1 = System.currentTimeMillis() - startTime;
        
        // 暴力算法在大数组上会很慢，这里用小数组测试
        int[] smallArr = randomArr(50, 50);
        startTime = System.currentTimeMillis();
        int result2 = numSure(smallArr);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.printf("优化算法（数组长度%d）: 结果=%d, 耗时=%dms\n", largeArr.length, result1, time1);
        System.out.printf("暴力算法（数组长度%d）: 结果=%d, 耗时=%dms\n", smallArr.length, result2, time2);
        
        // 算法特点总结
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("环形山可见对数问题的关键点：");
        System.out.println("1. 环形结构：两个山峰间有两条路径");
        System.out.println("2. 可见条件：至少一条路径上没有更高的山峰阻挡");
        System.out.println("3. 单调栈优化：利用'小找大'的配对关系");
        System.out.println("4. 特殊处理：相同高度山峰的内部配对");
        
        System.out.println("\n复杂度分析：");
        System.out.println("优化算法 - 时间复杂度: O(n), 空间复杂度: O(n)");
        System.out.println("暴力算法 - 时间复杂度: O(n³), 空间复杂度: O(n²)");
        System.out.println("适用场景: 大规模环形数组的可见性计算");
    }
}
