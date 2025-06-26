package leetc.top;

/**
 * LeetCode 621. 任务调度器 (Task Scheduler)
 * 
 * 问题描述：
 * 给你一个用字符数组 tasks 表示的任务列表，其中不同的字符表示不同的任务类型。
 * 每一个任务都需要一个单位时间完成，在任何一个单位时间，CPU可以完成一个任务，或者处于待命状态。
 * 
 * 但是，两个相同类型的任务之间必须有长度为整数 n 的冷却时间，
 * 因此至少有连续 n 个单位时间内CPU在执行不同的任务，或者在待命状态。
 * 
 * 你需要计算完成所有任务所需要的最短时间。
 * 
 * 示例：
 * - 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * - 输出：8
 * - 解释：A -> B -> idle -> A -> B -> idle -> A -> B
 *         在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，
 *         而执行一个任务只需要一个单位时间，所以中间出现了（idle）状态。
 * 
 * 解法思路：
 * 数学公式推导：
 * 1. 找出出现次数最多的任务（maxCount）和有多少种这样的任务（maxKinds）
 * 2. 以出现次数最多的任务为基准安排时间槽：
 *    - 需要 (maxCount-1) 个完整的执行周期
 *    - 每个周期长度为 (n+1)，包括1个该任务 + n个冷却时间
 *    - 最后还需要 maxKinds 个时间单位来执行最后一轮的最频繁任务
 * 3. 计算是否有足够的其他任务填满冷却时间
 * 4. 如果不够就需要idle时间，如果够了就只需任务本身的时间
 * 
 * 关键公式：
 * - 基础框架时间：(maxCount-1) * (n+1) + maxKinds
 * - 其他任务数量：tasks.length - maxKinds
 * - 可用空闲槽位：(maxCount-1) * n
 * - 实际需要的idle时间：max(0, 可用空闲槽位 - 其他任务数量)
 * 
 * 时间复杂度：O(|tasks|) - 遍历任务数组统计频次
 * 空间复杂度：O(1) - 使用固定大小的字符计数数组
 * 
 * LeetCode链接：https://leetcode.com/problems/task-scheduler/
 */
public class P621_TaskScheduler {
    
    /**
     * 计算完成所有任务所需的最短时间
     * 
     * 算法步骤：
     * 1. 统计每种任务的出现次数
     * 2. 找出最大出现次数（maxCount）和达到最大次数的任务种类数（maxKinds）
     * 3. 计算除了最频繁任务之外的其他任务数量
     * 4. 计算可用的空闲时间槽位
     * 5. 计算实际需要的idle时间
     * 6. 返回 总任务数 + idle时间
     * 
     * @param tasks 任务数组，每个字符代表一种任务类型
     * @param free 相同任务之间的冷却时间（注意：这里参数名是free，实际是冷却间隔n）
     * @return 完成所有任务所需的最短时间
     */
    public static int leastInterval(char[] tasks, int free) {
        // 统计每种任务的出现次数
        int[] count = new int[256];  // ASCII字符集
        int maxCount = 0;  // 最大出现次数
        
        for (char task : tasks) {
            count[task]++;
            maxCount = Math.max(maxCount, count[task]);
        }
        
        // 统计有多少种任务达到了最大出现次数
        int maxKinds = 0;
        for (int task = 0; task < 256; task++) {
            if(count[task] == maxCount) {
                maxKinds++;
            }
        }
        
        // 计算除了最频繁任务外的其他任务数量
        int exceptRight = tasks.length - maxKinds;
        
        // 计算可用的空闲时间槽位
        // (free + 1) 是每个执行周期的长度，(maxCount - 1) 是完整周期数
        int spaces = (free + 1) * (maxCount - 1);
        
        // 计算实际需要的idle时间
        // 如果其他任务足够填满所有空闲槽位，就不需要额外的idle时间
        int restSpaces = Math.max(0, spaces - exceptRight);
        
        // 返回总时间：所有任务时间 + 必要的idle时间
        return tasks.length + restSpaces;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        char[] tasks1 = {'A','A','A','B','B','B'};
        int n1 = 2;
        System.out.println("测试用例1:");
        System.out.println("任务: " + java.util.Arrays.toString(tasks1));
        System.out.println("冷却时间: " + n1);
        System.out.println("最短时间: " + leastInterval(tasks1, n1));
        System.out.println("期望结果: 8");
        System.out.println("调度方案: A -> B -> idle -> A -> B -> idle -> A -> B");
        System.out.println();
        
        // 测试用例2：不需要idle时间
        char[] tasks2 = {'A','A','A','B','B','B','C','C'};
        int n2 = 2;
        System.out.println("测试用例2 (不需要idle):");
        System.out.println("任务: " + java.util.Arrays.toString(tasks2));
        System.out.println("冷却时间: " + n2);
        System.out.println("最短时间: " + leastInterval(tasks2, n2));
        System.out.println("期望结果: 8");
        System.out.println("调度方案: A -> B -> C -> A -> B -> C -> A -> B");
        System.out.println();
        
        // 测试用例3：冷却时间为0
        char[] tasks3 = {'A','A','A','B','B','B'};
        int n3 = 0;
        System.out.println("测试用例3 (无冷却时间):");
        System.out.println("任务: " + java.util.Arrays.toString(tasks3));
        System.out.println("冷却时间: " + n3);
        System.out.println("最短时间: " + leastInterval(tasks3, n3));
        System.out.println("期望结果: 6");
        System.out.println("调度方案: A -> A -> A -> B -> B -> B");
        System.out.println();
        
        // 测试用例4：单一任务
        char[] tasks4 = {'A','A','A','A'};
        int n4 = 3;
        System.out.println("测试用例4 (单一任务):");
        System.out.println("任务: " + java.util.Arrays.toString(tasks4));
        System.out.println("冷却时间: " + n4);
        System.out.println("最短时间: " + leastInterval(tasks4, n4));
        System.out.println("期望结果: 13");
        System.out.println("调度方案: A -> idle -> idle -> idle -> A -> idle -> idle -> idle -> A -> idle -> idle -> idle -> A");
        System.out.println();
        
        // 算法说明
        System.out.println("算法核心思想：");
        System.out.println("- 以出现次数最多的任务为基准建立时间框架");
        System.out.println("- 计算是否有足够的其他任务填满冷却间隔");
        System.out.println("- 数学公式直接计算，避免模拟调度过程");
        System.out.println("- 时间复杂度：O(n)，空间复杂度：O(1)");
    }
}
