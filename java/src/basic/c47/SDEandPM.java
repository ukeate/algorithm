package basic.c47;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

// 项目信息：项目经理、产生时间点、优先级、花费时间
// 输入项目经理数量，项目经理一次只能提一个项目。提出顺序：优先级，花费时间少，产生时间点
// 输入程序员的数量，消费项目顺序：花费时间少，负责人编号小
// 返回n长度的数组表示项目结束时间
public class SDEandPM {

    private static class Program {
        public int idx;
        public int pm;
        public int start;
        public int rank;
        public int cost;

        public Program(int idx, int pm, int start, int rank, int cost) {
            this.idx = idx;
            this.pm = pm;
            this.start = start;
            this.rank = rank;
            this.cost = cost;
        }
    }

    private static class StartRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;
        }
    }

    private static class PmRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            if (o1.rank != o2.rank) {
                return o1.rank - o2.rank;
            } else if (o1.cost != o2.cost) {
                return o1.cost - o2.cost;
            } else {
                return o1.start - o2.start;
            }
        }
    }

    private static class NextQueue {
        // 每个pm一个堆
        private List<PriorityQueue<Program>> pmQueues;
        // 每个pm堆顶，组成程序员堆
        private Program[] sdeHeap;
        private int heapSize;
        // i号pm的堆顶项目，在sdeHeap的位置
        private int[] indexes;

        public NextQueue(int pmNum) {
            heapSize = 0;
            sdeHeap = new Program[pmNum];
            indexes = new int[pmNum + 1];
            for (int i = 0; i <= pmNum; i++) {
                indexes[i] = -1;
            }
            pmQueues = new ArrayList<>();
            for (int i = 0; i <= pmNum; i++) {
                pmQueues.add(new PriorityQueue<>(new PmRule()));
            }
        }


        private void swap(int i1, int i2) {
            Program p1 = sdeHeap[i1];
            Program p2 = sdeHeap[i2];
            sdeHeap[i1] = p2;
            sdeHeap[i2] = p1;
            indexes[p1.pm] = i2;
            indexes[p2.pm] = i1;
        }

        private int sdeRule(Program p1, Program p2) {
            if (p1.cost != p2.cost) {
                return p1.cost - p2.cost;
            } else {
                return p1.pm - p2.pm;
            }
        }
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

        private void heapify(int i) {
            int left = i * 2 + 1;
            int right = i *2 + 2;
            int best = i;
            while (left < heapSize) {
                if (sdeRule(sdeHeap[left], sdeHeap[i]) < 0) {
                    best = left;
                }
                if (right < heapSize &&sdeRule(sdeHeap[right], sdeHeap[best]) < 0) {
                    best = right;
                }
                if (best == i) {
                    break;
                }
                swap(best, i);
                i = best;
                left = i * 2 + 1;
                right = i * 2 + 2;
            }
        }

        public void add(Program program) {
            PriorityQueue<Program> pmHeap = pmQueues.get(program.pm);
            pmHeap.add(program);
            Program head = pmHeap.peek();
            int heapIdx = indexes[head.pm];
            if (heapIdx == -1) {
                sdeHeap[heapSize] = head;
                indexes[head.pm] = heapSize;
                heapInsert(heapSize++);
            } else {
                sdeHeap[heapIdx] = head;
                heapInsert(heapIdx);
                heapify(heapIdx);
            }
        }


        public Program pop() {
            Program head = sdeHeap[0];
            PriorityQueue<Program> pmHeap = pmQueues.get(head.pm);
            pmHeap.poll();
            if (pmHeap.isEmpty()) {
                swap(0, heapSize - 1);
                sdeHeap[--heapSize] = null;
                indexes[head.pm] = -1;
            } else {
                sdeHeap[0] = pmHeap.peek();
            }
            heapify(0);
            return head;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }
    }

    public static int[] finish(int pms, int sdes, int[][] programs) {
        PriorityQueue<Program> startQue = new PriorityQueue<>(new StartRule());
        for (int i = 0; i < programs.length; i++) {
            Program p = new Program(i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
            startQue.add(p);
        }
        PriorityQueue<Integer> wakeQue = new PriorityQueue<>();
        for (int i = 0; i < sdes; i++) {
            wakeQue.add(1);
        }
        NextQueue nxtQue = new NextQueue(pms);
        int finish = 0;
        int[] ans = new int[programs.length];
        while (finish != ans.length) {
            int sdeWakeTime = wakeQue.poll();
            while (!startQue.isEmpty()) {
                if (startQue.peek().start > sdeWakeTime) {
                    break;
                }
                nxtQue.add(startQue.poll());
            }
            if (nxtQue.isEmpty()) {
                // 无项目可做, sdeWakeTime休息到下个项目
                wakeQue.add(startQue.peek().start);
            } else {
                Program program = nxtQue.pop();
                ans[program.idx] = sdeWakeTime + program.cost;
                wakeQue.add(ans[program.idx]);
                finish++;
            }
        }
        return ans;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void main(String[] args) {
        int pms = 2;
        int sde = 2;
        int[][] programs = {{1,1,1,2},{1,2,1,1},{1,3,2,2},{2,1,1,2},{2,3,5,5}};
        int[] ans = finish(pms, sde, programs);
        print(ans);
    }
}
