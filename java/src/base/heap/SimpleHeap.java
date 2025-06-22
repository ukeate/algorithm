package base.heap;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 简单堆实现 - 手写小根堆
 * 
 * 堆的特性：
 * 1. 完全二叉树结构
 * 2. 父节点的值总是小于等于子节点的值（小根堆）
 * 3. 支持高效的插入和删除最小值操作
 * 
 * 数组表示的完全二叉树性质：
 * - 对于索引i的节点：
 *   - 父节点索引：(i-1)/2
 *   - 左子节点索引：2*i+1
 *   - 右子节点索引：2*i+2
 * 
 * 时间复杂度：
 * - 插入：O(log n)
 * - 删除最小值：O(log n)
 * - 查看最小值：O(1)
 */
public class SimpleHeap {
    /**
     * 自定义小根堆实现
     */
    public static class MyHeap {
        private int[] heap;     // 堆数组
        private final int limit;    // 堆容量限制
        private int size;       // 当前堆大小

        /**
         * 构造函数
         * @param limit 堆的最大容量
         */
        public MyHeap(int limit) {
            heap = new int[limit];
            this.limit = limit;
        }

        /**
         * 交换数组中两个位置的元素
         * @param arr 数组
         * @param i 位置i
         * @param j 位置j
         */
        private void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        /**
         * 堆插入操作（上浮过程）
         * 
         * 插入过程：
         * 1. 将新元素放在堆的末尾
         * 2. 与父节点比较，如果小于父节点则交换
         * 3. 重复步骤2，直到满足堆性质或到达根节点
         * 
         * @param arr 堆数组
         * @param i 新插入元素的索引
         */
        private void heapInsert(int[] arr, int i) {
            int pi = (i - 1) / 2;   // 父节点索引
            // 当前节点小于父节点时，需要上浮
            while (arr[i] < arr[pi]) {
                swap(arr, i, pi);
                i = pi;
                pi = (i - 1) / 2;
            }
        }

        /**
         * 向堆中插入元素
         * @param val 要插入的值
         */
        public void push(int val) {
            if (size == limit) {
                throw new RuntimeException("heap is full");
            }
            heap[size] = val;
            heapInsert(heap, size++);   // 插入后执行上浮操作
        }

        /**
         * 堆化操作（下沉过程）
         * 
         * 下沉过程：
         * 1. 将当前节点与其子节点中的最小值比较
         * 2. 如果当前节点大于最小子节点，则交换
         * 3. 重复步骤1-2，直到满足堆性质或到达叶子节点
         * 
         * @param arr 堆数组
         * @param i 需要下沉的节点索引
         * @param n 堆的有效大小
         */
        private void heapify(int[] arr, int i, int n) {
            int li = i * 2 + 1;     // 左子节点索引
            while (li < n) {
                // 找到子节点中的最小值索引
                int mi = li + 1 < n && arr[li + 1] < arr[li] ? li + 1 : li;
                // 如果最小子节点仍然大于等于当前节点，则已满足堆性质
                if (arr[mi] >= arr[i]) {
                    break;
                }
                // 交换当前节点与最小子节点
                swap(arr, mi, i);
                i = mi;
                li = i * 2 + 1;
            }
        }

        /**
         * 弹出堆顶元素（最小值）
         * 
         * 删除过程：
         * 1. 保存堆顶元素作为返回值
         * 2. 将最后一个元素移到堆顶
         * 3. 堆大小减1
         * 4. 对新的堆顶执行下沉操作
         * 
         * @return 堆中的最小值
         */
        public int pop() {
            int ans = heap[0];
            swap(heap, 0, --size);  // 将最后元素移到堆顶，并减少堆大小
            heapify(heap, 0, size); // 对新堆顶执行下沉操作
            return ans;
        }

        /**
         * 判断堆是否为空
         * @return 堆为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * 判断堆是否已满
         * @return 堆已满返回true，否则返回false
         */
        public boolean isFull() {
            return size == limit;
        }
    }

    /**
     * 用于测试的最小值比较器
     */
    private static class MinComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    /**
     * 测试方法 - 验证自定义堆与Java内置PriorityQueue的一致性
     */
    public static void main(String[] args) {
        int times = 10000;      // 测试次数
        int maxVal = 1000;      // 最大元素值
        int maxLimit = 100;     // 最大堆容量
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int limit = (int) ((maxLimit + 1) * Math.random());
            MyHeap heap1 = new MyHeap(limit);
            PriorityQueue<Integer> heap2 = new PriorityQueue<>(new MinComparator());
            int opTimes = (int) ((limit + 1) * Math.random());
            
            // 随机执行插入和删除操作
            for (int j = 0; j < opTimes; j++) {
                // 检查两个堆的空状态是否一致
                if (heap1.isEmpty() != heap2.isEmpty()) {
                    System.out.println("Wrong - 空状态不一致");
                    break;
                }
                
                if (heap1.isEmpty()) {
                    // 堆为空时只能插入
                    int v = (int) ((maxVal + 1) * Math.random());
                    heap1.push(v);
                    heap2.add(v);
                } else if (heap1.isFull()) {
                    // 堆已满时只能删除
                    if (heap1.pop() != heap2.poll()) {
                        System.out.println("Wrong - 删除结果不一致");
                        break;
                    }
                } else {
                    // 随机选择插入或删除操作
                    if (Math.random() < 0.5) {
                        int v = (int) ((maxVal + 1) * Math.random());
                        heap1.push(v);
                        heap2.add(v);
                    } else {
                        int ans1 = heap1.pop();
                        int ans2 = heap2.poll();
                        if (ans1 != ans2) {
                            System.out.println("Wrong - 删除元素不一致: " + ans1 + " vs " + ans2);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
