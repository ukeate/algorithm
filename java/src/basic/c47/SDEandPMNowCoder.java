package basic.c47;


import java.util.*;

// https://www.nowcoder.com/questionTerminal/f7efb182b285403a84c10ee4e6f6075a
public class SDEandPMNowCoder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int pm = sc.nextInt();
        int sde = sc.nextInt();
        int n = sc.nextInt();
        int[][] programs = new int[n][4];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) {
                programs[i][j] = sc.nextInt();
            }
            pm = Math.max(programs[i][0], pm);
        }
        int[] ans = finish(pm, sde, programs);
        for (int i = 0; i < n; i++) {
            System.out.println(ans[i]);
        }
        sc.close();
    }

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
        private List<PriorityQueue<Program>> pmQueues;
        private Program[] sdeHeap;
        private int heapSize;
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

    private static class StartRule implements Comparator<Program> {
        @Override
        public int compare(Program o1, Program o2) {
            return o1.start - o2.start;
        }
    }

    public static int[] finish(int pms, int sdes, int[][] programs) {
        PriorityQueue<Program> startQue = new PriorityQueue<>(new StartRule());
        for (int i = 0; i < programs.length; i++) {
            Program program = new Program(i, programs[i][0], programs[i][1], programs[i][2], programs[i][3]);
            startQue.add(program);
        }
        PriorityQueue<Integer> wakeQue = new PriorityQueue<>();
        for (int i = 0; i < sdes; i++) {
            wakeQue.add(1);
        }
        NextQueue nextQue = new NextQueue(pms);
        int finish = 0;
        int[] ans = new int[programs.length];
        while (finish < ans.length) {
            int sdeWakeTime = wakeQue.poll();
            while (!startQue.isEmpty()) {
                if (startQue.peek().start > sdeWakeTime) {
                    break;
                }
                nextQue.add(startQue.poll());
            }
            if (nextQue.isEmpty()) {
                wakeQue.add(startQue.peek().start);
            } else {
                Program program = nextQue.pop();
                ans[program.idx] = sdeWakeTime + program.cost;
                wakeQue.add(ans[program.idx]);
                finish++;
            }
        }
        return ans;
    }

}
