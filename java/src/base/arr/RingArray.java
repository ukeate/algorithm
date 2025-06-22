package base.arr;

/**
 * 环形数组（环形缓冲区）实现
 * 使用固定大小的数组模拟循环队列
 * 特点：空间固定，当数组填满时会覆盖旧数据
 */
public class RingArray {

    /**
     * 环形数组数据结构
     * 核心思想：使用两个指针（pushIdx和pollIdx）在固定大小的数组中循环移动
     * 当指针到达数组末尾时，会回到数组开头，形成环形结构
     */
    public static class Ring {
        // 存储数据的数组
        private int[] arr;
        // 下一次push操作的位置索引
        private int pushIdx;
        // 下一次poll操作的位置索引  
        private int pollIdx;
        // 当前环形数组中的元素个数
        private int size;
        // 环形数组的容量限制
        private final int limit;

        /**
         * 构造函数
         * @param limit 环形数组的最大容量
         */
        public Ring(int limit) {
            arr = new int[limit];
            this.limit = limit;
            // pushIdx、pollIdx、size都默认初始化为0
        }

        /**
         * 计算下一个索引位置
         * 实现环形结构的关键方法
         * @param i 当前索引
         * @return 下一个索引（如果到达末尾则回到开头）
         */
        private int nextIdx(int i) {
            return i < limit - 1 ? i + 1 : 0;
        }

        /**
         * 向环形数组中添加元素
         * @param val 要添加的值
         * @throws RuntimeException 当数组已满时抛出异常
         */
        public void push(int val) {
            if (size == limit) {
                throw new RuntimeException("fulfilled");
            }
            size++;
            // 在pushIdx位置放入新元素
            arr[pushIdx] = val;
            // pushIdx移动到下一个位置
            pushIdx = nextIdx(pushIdx);
        }

        /**
         * 从环形数组中取出元素（FIFO方式）
         * @return 取出的元素值
         * @throws RuntimeException 当数组为空时抛出异常
         */
        public int pop() {
            if (size == 0) {
                throw new RuntimeException("fulfilled");
            }
            size--;
            // 从pollIdx位置取出元素
            int ans = arr[pollIdx];
            // pollIdx移动到下一个位置
            pollIdx = nextIdx(pollIdx);
            return ans;
        }

        /**
         * 判断环形数组是否为空
         * @return 如果为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return size == 0;
        }
    }
}
