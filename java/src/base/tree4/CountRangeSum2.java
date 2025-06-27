package base.tree4;

import java.util.HashSet;

/**
 * 区间和计数问题
 * 
 * 问题描述：给定一个整数数组nums和两个整数lower、upper，
 * 求有多少个连续子数组的和在[lower, upper]范围内。
 * 
 * 本类提供两种解法：
 * 1. count1：归并排序+前缀和，时间复杂度O(n log n)
 * 2. count2：SB树+前缀和，时间复杂度O(n log n)
 */
public class CountRangeSum2 {

    /**
     * 归并排序过程中统计区间和
     * 
     * 核心思想：
     * 1. 使用前缀和数组，将子数组和问题转化为两个前缀和之差
     * 2. 在归并排序过程中，对于左半部分的每个前缀和sums[i]，
     *    统计右半部分中有多少个前缀和sums[j]使得lower <= sums[j] - sums[i] <= upper
     * 3. 即统计右半部分中[sums[i] + lower, sums[i] + upper]范围内的前缀和个数
     * 
     * @param sums 前缀和数组
     * @param start 开始索引
     * @param end 结束索引
     * @param lower 下界
     * @param upper 上界
     * @return 满足条件的区间和个数
     */
    private static int process(long[] sums, int start, int end, int lower, int upper) {
        if (end - start <= 1) {
            return 0;
        }
        int mid = (start + end) / 2;
        // 递归处理左右两部分
        int count = process(sums, start, mid, lower, upper) + process(sums, mid, end, lower, upper);
        
        // 统计跨越mid的区间和
        int j = mid, k = mid, t = mid;
        long[] cache = new long[end - start];
        for (int i = start, r = 0; i < mid; i++, r++) {
            // 对于左半部分的sums[i]，在右半部分找满足条件的范围
            // k指向第一个使得sums[k] - sums[i] >= lower的位置
            while (k < end && sums[k] - sums[i] < lower) {
                k++;
            }
            // j指向第一个使得sums[j] - sums[i] > upper的位置
            while (j < end && sums[j] - sums[i] <= upper) {
                j++;
            }
            // 归并排序的merge过程
            while (t < end && sums[t] < sums[i]) {
                cache[r++] = sums[t++];
            }
            cache[r] = sums[i];
            // [k, j)范围内的元素都满足条件
            count += j - k;
        }
        System.arraycopy(cache, 0, sums, start, t - start);
        return count;
    }

    /**
     * 方法1：使用归并排序解决区间和计数问题
     * 
     * @param nums 原数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    public static int count1(int[] nums, int lower, int upper) {
        int n = nums.length;
        // 构建前缀和数组，sums[i]表示nums[0..i-1]的和
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return process(sums, 0, n + 1, lower, upper);
    }

    /**
     * SB树的节点类
     * 
     * 每个节点维护以下信息：
     * - k: 节点的键值
     * - l, r: 左右子节点
     * - size: 以当前节点为根的子树中不同键值的个数
     * - all: 以当前节点为根的子树中所有节点的总个数（包括重复）
     */
    private static class Node {
        public long k;      // 键值
        public Node l;      // 左子节点
        public Node r;      // 右子节点
        public long size;   // 子树中不同键值的个数
        public long all;    // 子树中所有节点的总个数

        public Node(long key) {
            k = key;
            size = 1;
            all = 1;
        }
    }

    /**
     * Size Balanced Tree实现的有序集合
     * 
     * 支持以下操作：
     * 1. add: 添加元素
     * 2. lessKeySize: 统计小于指定key的元素总个数
     * 3. moreKeySize: 统计大于指定key的元素总个数
     */
    private static class SBSet {
        private Node root;                    // 树根
        private HashSet<Long> set = new HashSet<>();  // 用于快速判断元素是否存在

        /**
         * 右旋操作
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node rightRotate(Node cur) {
            long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
            Node l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            l.all = cur.all;
            cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same;
            return l;
        }

        /**
         * 左旋操作
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node leftRotate(Node cur) {
            long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
            Node r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            r.all = cur.all;
            cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same;
            return r;
        }

        /**
         * SB树的调整操作
         * 
         * 通过旋转维护SB树的平衡性质：
         * 对于任意节点x，有size[x.child] <= size[x.sibling]
         * 
         * @param cur 当前节点
         * @return 调整后的节点
         */
        private Node maintain(Node cur) {
            if (cur == null) {
                return null;
            }
            long ls = cur.l != null ? cur.l.size : 0;
            long lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            long lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            long rs = cur.r != null ? cur.r.size : 0;
            long rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            long rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            
            if (lls > rs) {
                // LL违规，右旋
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                // LR违规，先左旋左子树，再右旋当前节点
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                // RR违规，左旋
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rls > ls) {
                // RL违规，先右旋右子树，再左旋当前节点
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        /**
         * 向SB树中添加节点
         * 
         * @param cur 当前节点
         * @param key 要添加的键值
         * @param has 该键值是否已经存在
         * @return 添加后的子树根节点
         */
        private Node add(Node cur, long key, boolean has) {
            if (cur == null) {
                return new Node(key);
            } else {
                cur.all++;  // 无论是否重复，都增加总计数
                if (key == cur.k) {
                    return cur;  // 重复键值，只增加计数
                } else {
                    if (!has) {
                        cur.size++;  // 新键值，增加不同键值计数
                    }
                    if (key < cur.k) {
                        cur.l = add(cur.l, key, has);
                    } else {
                        cur.r = add(cur.r, key, has);
                    }
                    return maintain(cur);
                }
            }
        }

        /**
         * 添加元素到集合中
         * 
         * @param sum 要添加的元素
         */
        public void add(long sum) {
            boolean has = set.contains(sum);
            root = add(root, sum, has);
            set.add(sum);
        }

        /**
         * 统计小于指定key的元素个数
         * 
         * @param key 指定的键值
         * @return 小于key的元素总个数
         */
        public long lessKeySize(long key) {
            Node cur = root;
            long ans = 0;
            while (cur != null) {
                if (key == cur.k) {
                    return ans + (cur.l != null ? cur.l.all : 0);
                } else if (key < cur.k) {
                    cur = cur.l;
                } else {
                    ans += cur.all - (cur.r != null ? cur.r.all : 0);
                    cur = cur.r;
                }
            }
            return ans;
        }

        /**
         * 统计大于指定key的元素个数
         * 
         * @param key 指定的键值
         * @return 大于key的元素总个数
         */
        public long moreKeySize(long key) {
            return root != null ? (root.all - lessKeySize(key + 1)) : 0;
        }
    }

    /**
     * 方法2：使用SB树解决区间和计数问题
     * 
     * 核心思想：
     * 1. 使用前缀和，遍历数组时维护当前前缀和sum
     * 2. 对于当前位置i，需要统计之前有多少个前缀和preSum满足：
     *    lower <= sum - preSum <= upper
     *    即：sum - upper <= preSum <= sum - lower
     * 3. 使用SB树维护之前出现过的前缀和，支持范围查询
     * 
     * @param nums 原数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    public static int count2(int[] nums, int lower, int upper) {
        SBSet sb = new SBSet();
        long sum = 0;
        int ans = 0;
        sb.add(0);  // 添加前缀和为0的情况（空前缀）
        
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            // 统计满足 sum - upper <= preSum <= sum - lower 的preSum个数
            long a = sb.lessKeySize(sum - lower + 1);  // 小于sum-lower+1的个数
            long b = sb.lessKeySize(sum - upper);      // 小于sum-upper的个数
            ans += a - b;  // [sum-upper, sum-lower]范围内的个数
            sb.add(sum);   // 将当前前缀和加入集合
        }
        return ans;
    }

    /**
     * 打印数组（调试用）
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 生成随机数组（测试用）
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 主方法：对比测试两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 10;
        int maxVal = 50;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int lower = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            int upper = lower + (int) ((maxVal + 1) * Math.random());
            int ans1 = count1(arr, lower, upper);
            int ans2 = count2(arr, lower, upper);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(lower);
                System.out.println(upper);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
