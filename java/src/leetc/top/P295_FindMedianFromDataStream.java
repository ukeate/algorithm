package leetc.top;

import java.util.PriorityQueue;

/**
 * LeetCode 295. 数据流的中位数 (Find Median from Data Stream)
 * 
 * 问题描述：
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 * 
 * 设计一个支持以下两个操作的数据结构：
 * - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 * - double findMedian() - 返回目前所有元素的中位数。
 * 
 * 示例：
 * addNum(1)
 * addNum(2)
 * findMedian() -> 1.5
 * addNum(3) 
 * findMedian() -> 2
 * 
 * 解法思路：
 * 双堆法 - 使用两个优先队列（堆）：
 * 1. 大根堆(maxh)：存储较小的一半数据，堆顶是较小半部分的最大值
 * 2. 小根堆(minh)：存储较大的一半数据，堆顶是较大半部分的最小值
 * 3. 维护两个堆的大小平衡：|maxh.size() - minh.size()| ≤ 1
 * 4. 中位数就是堆顶元素（奇数个）或两个堆顶的平均值（偶数个）
 * 
 * 核心思想：
 * - 通过两个堆的分治，将数据分为两部分
 * - 大根堆维护左半部分的最大值
 * - 小根堆维护右半部分的最小值
 * - 保持两堆大小平衡，确保中位数位于堆顶
 * 
 * 时间复杂度：
 * - addNum: O(log n) - 堆插入操作
 * - findMedian: O(1) - 直接访问堆顶
 * 
 * 空间复杂度：O(n) - 存储所有数据
 * 
 * LeetCode链接：https://leetcode.com/problems/find-median-from-data-stream/
 */
public class P295_FindMedianFromDataStream {
    class MedianFinder {
        private PriorityQueue<Integer> maxh; // 大根堆：存储较小的一半数据
        private PriorityQueue<Integer> minh; // 小根堆：存储较大的一半数据

        /**
         * 构造函数：初始化两个堆
         */
        public MedianFinder() {
            maxh = new PriorityQueue<>((a, b) -> b - a); // 大根堆：降序排列
            minh = new PriorityQueue<>((a, b) -> a - b); // 小根堆：升序排列
        }

        /**
         * 平衡两个堆的大小
         * 确保两堆大小差不超过1，维护中位数的性质
         */
        private void balance() {
            if (maxh.size() == minh.size() + 2) {
                // 大根堆过多，将堆顶移动到小根堆
                minh.add(maxh.poll());
            }
            if (maxh.size() == minh.size() - 2) {
                // 小根堆过多，将堆顶移动到大根堆
                maxh.add(minh.poll());
            }
        }

        /**
         * 添加一个数字到数据结构中
         * 
         * 算法步骤：
         * 1. 如果大根堆为空，直接添加到大根堆
         * 2. 如果新数字 ≤ 大根堆堆顶，添加到大根堆
         * 3. 否则添加到小根堆
         * 4. 调用balance()保持两堆平衡
         * 
         * @param num 要添加的数字
         */
        public void addNum(int num) {
            if (maxh.isEmpty()) {
                maxh.add(num);
            } else {
                if (maxh.peek() >= num) {
                    maxh.add(num); // 添加到较小的一半
                } else {
                    minh.add(num); // 添加到较大的一半
                }
            }
            balance(); // 维护堆的平衡
        }

        /**
         * 返回当前所有元素的中位数
         * 
         * 中位数计算规则：
         * 1. 如果两堆大小相等：返回两个堆顶的平均值
         * 2. 如果某个堆大小更大：返回较大堆的堆顶
         * 
         * @return 中位数
         */
        public double findMedian() {
            if (maxh.size() == minh.size()) {
                // 偶数个元素：返回两个堆顶的平均值
                return (double) (maxh.peek() + minh.peek()) / 2;
            } else {
                // 奇数个元素：返回较大堆的堆顶
                return maxh.size() > minh.size() ? maxh.peek() : minh.peek();
            }
        }
    }
}
