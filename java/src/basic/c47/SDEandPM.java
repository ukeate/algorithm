package basic.c47;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 软件工程师和项目经理任务调度问题
 * 
 * 问题描述：
 * 有若干个项目经理(PM)和软件工程师(SDE)，每个项目由以下信息组成：
 * - 项目编号(idx)
 * - 负责的项目经理编号(pm) 
 * - 项目产生时间(start)
 * - 项目优先级(rank)
 * - 项目开发耗时(cost)
 * 
 * 约束条件：
 * 1. 每个项目经理一次只能提出一个项目给工程师
 * 2. 项目经理提出项目的顺序：优先级高 > 耗时短 > 产生时间早
 * 3. 工程师选择项目的顺序：耗时短 > 负责人编号小
 * 4. 项目只有在产生时间之后才能被开始
 * 
 * 目标：
 * 计算每个项目的完成时间
 * 
 * 算法思路：
 * 使用事件驱动的模拟方法：
 * 1. 按时间顺序处理项目产生事件
 * 2. 维护项目经理的项目队列（优先级队列）
 * 3. 维护工程师的可用时间队列
 * 4. 模拟项目分配和完成过程
 * 
 * 核心数据结构：
 * - 项目按产生时间排序的队列
 * - 每个PM一个优先级队列（按PM规则排序）
 * - SDE可选项目的优先级队列（按SDE规则排序）
 * - SDE的可用时间队列
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n + p) 其中n是项目数，p是PM数
 * 
 * @author 算法学习
 */
public class SDEandPM {

    /**
     * 项目信息类
     */
    private static class Program {
        public int idx;    // 项目编号
        public int pm;     // 负责的项目经理编号
        public int start;  // 项目产生时间
        public int rank;   // 项目优先级（数字越小优先级越高）
        public int cost;   // 项目开发耗时

        public Program(int idx, int pm, int start, int rank, int cost) {
            this.idx = idx;
            this.pm = pm;
            this.start = start;
            this.rank = rank;
            this.cost = cost;
        }
    }

    /**
     * 项目按产生时间排序的比较器
     */
    private static class StartRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;  // 按产生时间升序
        }
    }

    /**
     * 项目经理选择项目的比较器
     * 优先级：优先级高 > 耗时短 > 产生时间早
     */
    private static class PmRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            if (o1.rank != o2.rank) {
                return o1.rank - o2.rank;    // 优先级高的优先（数字小的优先）
            } else if (o1.cost != o2.cost) {
                return o1.cost - o2.cost;    // 耗时短的优先
            } else {
                return o1.start - o2.start;  // 产生时间早的优先
            }
        }
    }

    /**
     * 工程师可选项目的管理队列
     * 
     * 核心设计思想：
     * 1. 每个PM维护一个优先级队列，存储该PM的所有项目
     * 2. 从所有PM的队列顶部选择一个项目，组成SDE的选择队列
     * 3. SDE选择队列按照SDE规则排序：耗时短 > PM编号小
     * 
     * 数据结构：
     * - pmQueues：每个PM一个优先级队列
     * - sdeHeap：SDE可选项目的小根堆
     * - indexes：记录每个PM的堆顶项目在sdeHeap中的位置
     */
    private static class NextQueue {
        // 每个PM一个堆，存储该PM的所有项目
        private List<PriorityQueue<Program>> pmQueues;
        
        // SDE可选项目的小根堆（每个PM最多一个项目在这里）
        private Program[] sdeHeap;
        private int heapSize;
        
        // indexes[i] = j 表示i号PM的堆顶项目在sdeHeap[j]位置
        // indexes[i] = -1 表示i号PM没有项目在sdeHeap中
        private int[] indexes;

        public NextQueue(int pmNum) {
            heapSize = 0;
            sdeHeap = new Program[pmNum];
            indexes = new int[pmNum + 1];
            
            // 初始化indexes数组
            for (int i = 0; i <= pmNum; i++) {
                indexes[i] = -1;
            }
            
            // 初始化每个PM的优先级队列
            pmQueues = new ArrayList<>();
            for (int i = 0; i <= pmNum; i++) {
                pmQueues.add(new PriorityQueue<>(new PmRule()));
            }
        }

        /**
         * 交换sdeHeap中两个位置的项目，并更新indexes
         */
        private void swap(int i1, int i2) {
            Program p1 = sdeHeap[i1];
            Program p2 = sdeHeap[i2];
            sdeHeap[i1] = p2;
            sdeHeap[i2] = p1;
            indexes[p1.pm] = i2;  // 更新PM的位置索引
            indexes[p2.pm] = i1;
        }

        /**
         * SDE选择项目的比较规则
         * 优先级：耗时短 > PM编号小
         * 
         * @return 负数表示p1优先级更高，正数表示p2优先级更高
         */
        private int sdeRule(Program p1, Program p2) {
            if (p1.cost != p2.cost) {
                return p1.cost - p2.cost;  // 耗时短的优先
            } else {
                return p1.pm - p2.pm;      // PM编号小的优先
            }
        }

        /**
         * 堆的上浮操作（插入时维护堆性质）
         */
        private void heapInsert(int i) {
            while (i != 0) {
                int parent = (i - 1) / 2;
                if (sdeRule(sdeHeap[parent], sdeHeap[i]) > 0) {
                    swap(parent, i);
                    i = parent;
                } else {
                    break;
                }
            }
        }

        /**
         * 堆的下沉操作（删除时维护堆性质）
         */
        private void heapify(int i) {
            int left = i * 2 + 1;
            int right = i * 2 + 2;
            int best = i;
            
            while (left < heapSize) {
                // 找到父节点和两个子节点中最优的
                if (sdeRule(sdeHeap[left], sdeHeap[i]) < 0) {
                    best = left;
                }
                if (right < heapSize && sdeRule(sdeHeap[right], sdeHeap[best]) < 0) {
                    best = right;
                }
                
                if (best == i) {
                    break;  // 已经满足堆性质
                }
                
                swap(best, i);
                i = best;
                left = i * 2 + 1;
                right = i * 2 + 2;
            }
        }

        /**
         * 添加一个新项目
         * 
         * @param program 要添加的项目
         * 
         * 算法步骤：
         * 1. 将项目添加到对应PM的队列中
         * 2. 获取该PM队列的新堆顶
         * 3. 如果该PM之前没有项目在sdeHeap中，则添加
         * 4. 如果该PM之前有项目在sdeHeap中，则更新并维护堆性质
         */
        public void add(Program program) {
            // 添加到对应PM的队列
            PriorityQueue<Program> pmHeap = pmQueues.get(program.pm);
            pmHeap.add(program);
            
            // 获取该PM的当前最优项目
            Program head = pmHeap.peek();
            int heapIdx = indexes[head.pm];
            
            if (heapIdx == -1) {
                // 该PM之前没有项目在sdeHeap中，新增
                sdeHeap[heapSize] = head;
                indexes[head.pm] = heapSize;
                heapInsert(heapSize++);
            } else {
                // 该PM之前有项目在sdeHeap中，更新
                sdeHeap[heapIdx] = head;
                heapInsert(heapIdx);  // 可能需要上浮
                heapify(heapIdx);     // 可能需要下沉
            }
        }

        /**
         * 获取并移除SDE的最优选择项目
         * 
         * @return SDE应该选择的项目
         * 
         * 算法步骤：
         * 1. 取出sdeHeap的堆顶（SDE的最优选择）
         * 2. 从对应PM的队列中移除该项目
         * 3. 如果PM队列空了，从sdeHeap中移除该PM
         * 4. 如果PM队列非空，用新的堆顶替换并维护堆性质
         */
        public Program pop() {
            Program head = sdeHeap[0];
            PriorityQueue<Program> pmHeap = pmQueues.get(head.pm);
            pmHeap.poll();  // 从PM队列中移除
            
            if (pmHeap.isEmpty()) {
                // PM队列空了，从sdeHeap中移除该PM
                swap(0, heapSize - 1);
                sdeHeap[--heapSize] = null;
                indexes[head.pm] = -1;
            } else {
                // PM队列非空，用新堆顶替换
                sdeHeap[0] = pmHeap.peek();
            }
            
            heapify(0);  // 维护堆性质
            return head;
        }

        /**
         * 检查是否还有可选项目
         */
        public boolean isEmpty() {
            return heapSize == 0;
        }
    }

    /**
     * 计算所有项目的完成时间
     * 
     * @param pms 项目经理数量
     * @param sdes 软件工程师数量  
     * @param programs 项目信息数组，每行包含[pm, start, rank, cost]
     * @return 每个项目的完成时间数组
     * 
     * 算法流程：
     * 1. 初始化各种队列
     * 2. 模拟时间推进：
     *    - 从最早可用的SDE开始
     *    - 将该时间点之前产生的所有项目加入候选队列
     *    - 如果有可选项目，分配给该SDE
     *    - 如果没有可选项目，该SDE等待到下个项目产生
     * 3. 重复直到所有项目完成
     */
    public static int[] finish(int pms, int sdes, int[][] programs) {
        // 按项目产生时间排序
        PriorityQueue<Program> startQue = new PriorityQueue<>(new StartRule());
        for (int i = 0; i < programs.length; i++) {
            Program p = new Program(i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
            startQue.add(p);
        }
        
        // SDE的可用时间队列（小根堆）
        PriorityQueue<Integer> wakeQue = new PriorityQueue<>();
        for (int i = 0; i < sdes; i++) {
            wakeQue.add(1);  // 所有SDE从时间1开始可用
        }
        
        // SDE可选项目的管理队列
        NextQueue nxtQue = new NextQueue(pms);
        
        int finish = 0;  // 已完成项目数
        int[] ans = new int[programs.length];  // 存储每个项目的完成时间
        
        // 模拟项目分配和完成过程
        while (finish != ans.length) {
            // 取出最早可用的SDE
            int sdeWakeTime = wakeQue.poll();
            
            // 将该时间点之前产生的所有项目加入候选队列
            while (!startQue.isEmpty()) {
                if (startQue.peek().start > sdeWakeTime) {
                    break;  // 后续项目还未产生
                }
                nxtQue.add(startQue.poll());
            }
            
            if (nxtQue.isEmpty()) {
                // 没有可选项目，SDE等待到下个项目产生时间
                wakeQue.add(startQue.peek().start);
            } else {
                // 有可选项目，分配给当前SDE
                Program program = nxtQue.pop();
                ans[program.idx] = sdeWakeTime + program.cost;  // 计算完成时间
                wakeQue.add(ans[program.idx]);  // SDE在完成时间后重新可用
                finish++;
            }
        }
        
        return ans;
    }

    /**
     * 打印数组（用于输出结果）
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 软件工程师和项目经理调度测试 ===");
        
        // 测试数据：2个PM，2个SDE，5个项目
        int pms = 2;
        int sde = 2;
        
        // 项目信息：[PM编号, 产生时间, 优先级, 耗时]
        int[][] programs = {
            {1, 1, 1, 2},  // 项目0：PM1，时间1产生，优先级1，耗时2
            {1, 2, 1, 1},  // 项目1：PM1，时间2产生，优先级1，耗时1  
            {1, 3, 2, 2},  // 项目2：PM1，时间3产生，优先级2，耗时2
            {2, 1, 1, 2},  // 项目3：PM2，时间1产生，优先级1，耗时2
            {2, 3, 5, 5}   // 项目4：PM2，时间3产生，优先级5，耗时5
        };
        
        System.out.println("输入参数：");
        System.out.println("项目经理数量: " + pms);
        System.out.println("软件工程师数量: " + sde);
        System.out.println("项目信息：");
        System.out.println("项目编号 | PM编号 | 产生时间 | 优先级 | 耗时");
        for (int i = 0; i < programs.length; i++) {
            System.out.printf("   %d     |   %d    |    %d     |   %d   |  %d%n", 
                            i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
        }
        
        int[] ans = finish(pms, sde, programs);
        
        System.out.println("\n项目完成时间：");
        System.out.println("项目编号 | 完成时间");
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("   %d     |    %d%n", i, ans[i]);
        }
        
        // 算法说明
        System.out.println("\n=== 算法执行过程分析 ===");
        System.out.println("1. PM规则：优先级高 > 耗时短 > 产生时间早");
        System.out.println("2. SDE规则：耗时短 > PM编号小");
        System.out.println("3. 约束：每个PM一次只能提出一个项目");
        System.out.println("4. 时间模拟：按SDE可用时间推进，分配最优项目");
        
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("时间复杂度：O(n log n) - 主要是堆操作");
        System.out.println("空间复杂度：O(n + p) - n个项目 + p个PM队列");
        System.out.println("适用场景：资源调度、任务分配、项目管理");
    }
}
