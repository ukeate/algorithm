package basic.c47;

import java.util.*;

/**
 * 软件工程师和项目经理调度问题 - NowCoder平台版本
 * 
 * 问题链接：
 * https://www.nowcoder.com/questionTerminal/f7efb182b285403a84c10ee4e6f6075a
 * 
 * 问题描述：
 * 与SDEandPM.java相同的问题，但针对NowCoder平台的输入输出格式进行了适配
 * 
 * 输入格式：
 * 第一行：PM数量 SDE数量 项目数量
 * 接下来N行：每行4个数字表示项目信息 [PM编号, 产生时间, 优先级, 耗时]
 * 
 * 输出格式：
 * N行，每行一个数字表示对应项目的完成时间
 * 
 * 算法核心：
 * 与SDEandPM.java完全相同的调度算法，只是增加了标准输入输出处理
 * 
 * 关键优化：
 * 动态计算实际需要的PM数量，避免创建过多的空队列
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n + p)
 * 
 * @author 算法学习
 * @see SDEandPM
 */
public class SDEandPMNowCoder {
    
    /**
     * 主方法：处理输入输出
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // 读取基本参数
        int pm = sc.nextInt();    // PM数量
        int sde = sc.nextInt();   // SDE数量
        int n = sc.nextInt();     // 项目数量
        
        // 读取项目信息
        int[][] programs = new int[n][4];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                programs[i][j] = sc.nextInt();
            }
            // 动态更新实际需要的PM数量（优化空间使用）
            pm = Math.max(programs[i][0], pm);
        }
        
        // 计算项目完成时间
        int[] ans = finish(pm, sde, programs);
        
        // 输出结果
        for (int i = 0; i < n; i++) {
            System.out.println(ans[i]);
        }
        
        sc.close();
    }

    /**
     * 项目信息类
     * 与SDEandPM.java中的定义完全相同
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
     * PM选择项目的比较器
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
     * SDE可选项目的管理队列
     * 实现逻辑与SDEandPM.java完全相同
     */
    private static class NextQueue {
        // 每个PM一个优先级队列
        private List<PriorityQueue<Program>> pmQueues;
        
        // SDE可选项目的小根堆
        private Program[] sdeHeap;
        private int heapSize;
        
        // PM在sdeHeap中的位置索引
        private int[] indexes;
        
        public NextQueue(int pmNum) {
            heapSize = 0;
            sdeHeap = new Program[pmNum];
            indexes = new int[pmNum + 1];
            
            // 初始化位置索引
            for (int i = 0; i <= pmNum; i++) {
                indexes[i] = -1;
            }
            
            // 初始化PM队列
            pmQueues = new ArrayList<>();
            for (int i = 0; i <= pmNum; i++) {
                pmQueues.add(new PriorityQueue<>(new PmRule()));
            }
        }

        /**
         * 交换堆中两个位置的项目
         */
        private void swap(int i1, int i2) {
            Program p1 = sdeHeap[i1];
            Program p2 = sdeHeap[i2];
            sdeHeap[i1] = p2;
            sdeHeap[i2] = p1;
            indexes[p1.pm] = i2;
            indexes[p2.pm] = i1;
        }

        /**
         * SDE选择项目的比较规则
         * 优先级：耗时短 > PM编号小
         */
        private int sdeRule(Program p1, Program p2) {
            if (p1.cost != p2.cost) {
                return p1.cost - p2.cost;  // 耗时短的优先
            } else {
                return p1.pm - p2.pm;      // PM编号小的优先
            }
        }

        /**
         * 堆的下沉操作
         */
        private void heapify(int idx) {
            int left = idx * 2 + 1;
            int right = idx * 2 + 2;
            int best = idx;
            
            while (left < heapSize) {
                if (sdeRule(sdeHeap[left], sdeHeap[idx]) < 0) {
                    best = left;
                }
                if (right < heapSize && sdeRule(sdeHeap[right], sdeHeap[best]) < 0) {
                    best = right;
                }
                if (best == idx) {
                    break;
                }
                swap(best, idx);
                idx = best;
                left = idx * 2 + 1;
                right = idx * 2 + 2;
            }
        }

        /**
         * 堆的上浮操作
         */
        private void heapInsert(int idx) {
            while (idx != 0) {
                int parent = (idx - 1) / 2;
                if (sdeRule(sdeHeap[parent], sdeHeap[idx]) > 0) {
                    swap(parent, idx);
                    idx = parent;
                } else {
                    break;
                }
            }
        }

        /**
         * 添加新项目到队列
         */
        public void add(Program program) {
            // 添加到对应PM的队列
            PriorityQueue<Program> pmHeap = pmQueues.get(program.pm);
            pmHeap.add(program);
            
            // 获取该PM的当前最优项目
            Program head = pmHeap.peek();
            int heapIdx = indexes[head.pm];
            
            if (heapIdx == -1) {
                // 该PM第一次有项目进入sdeHeap
                sdeHeap[heapSize] = head;
                indexes[head.pm] = heapSize;
                heapInsert(heapSize++);
            } else {
                // 该PM已有项目在sdeHeap中，更新
                sdeHeap[heapIdx] = head;
                heapInsert(heapIdx);
                heapify(heapIdx);
            }
        }

        /**
         * 获取SDE的最优选择项目
         */
        public Program pop() {
            Program head = sdeHeap[0];
            PriorityQueue<Program> pmHeap = pmQueues.get(head.pm);
            pmHeap.poll();
            
            if (pmHeap.isEmpty()) {
                // PM队列空了，从sdeHeap中移除
                swap(0, heapSize - 1);
                sdeHeap[--heapSize] = null;
                indexes[head.pm] = -1;
            } else {
                // PM队列非空，用新堆顶替换
                sdeHeap[0] = pmHeap.peek();
            }
            
            heapify(0);
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
     * 项目按产生时间排序的比较器
     */
    private static class StartRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;
        }
    }

    /**
     * 计算所有项目的完成时间
     * 与SDEandPM.java中的实现完全相同
     * 
     * @param pms 项目经理数量
     * @param sdes 软件工程师数量
     * @param programs 项目信息数组
     * @return 每个项目的完成时间数组
     */
    public static int[] finish(int pms, int sdes, int[][] programs) {
        // 按项目产生时间排序
        PriorityQueue<Program> startQue = new PriorityQueue<>(new StartRule());
        for (int i = 0; i < programs.length; i++) {
            Program program = new Program(i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
            startQue.add(program);
        }
        
        // SDE的可用时间队列
        PriorityQueue<Integer> wakeQue = new PriorityQueue<>();
        for (int i = 0; i < sdes; i++) {
            wakeQue.add(1);  // 所有SDE从时间1开始可用
        }
        
        // 项目管理队列
        NextQueue nextQue = new NextQueue(pms);
        
        int finish = 0;  // 已完成项目数
        int[] ans = new int[programs.length];
        
        // 事件驱动的模拟过程
        while (finish < ans.length) {
            // 取出最早可用的SDE
            int sdeWakeTime = wakeQue.poll();
            
            // 将该时间点之前产生的所有项目加入候选队列
            while (!startQue.isEmpty()) {
                if (startQue.peek().start > sdeWakeTime) {
                    break;  // 后续项目还未产生
                }
                nextQue.add(startQue.poll());
            }
            
            if (nextQue.isEmpty()) {
                // 没有可选项目，SDE等待到下个项目产生
                wakeQue.add(startQue.peek().start);
            } else {
                // 有可选项目，分配给当前SDE
                Program program = nextQue.pop();
                ans[program.idx] = sdeWakeTime + program.cost;
                wakeQue.add(ans[program.idx]);
                finish++;
            }
        }
        
        return ans;
    }
}
