package base.tree4;

import java.util.HashSet;

public class CountRangeSum2 {

    private static int process(long[] sums, int start, int end, int lower, int upper) {
        if (end - start <= 1) {
            return 0;
        }
        int mid = (start + end) / 2;
        int count = process(sums, start, mid, lower, upper) + process(sums, mid, end, lower, upper);
        int j = mid, k = mid, t = mid;
        long[] cache = new long[end - start];
        for (int i = start, r = 0; i < mid; i++, r++) {
            while (k < end && sums[k] - sums[i] < lower) {
                k++;
            }
            while (j < end && sums[j] - sums[i] <= upper) {
                j++;
            }
            while (t < end && sums[t] < sums[i]) {
                cache[r++] = sums[t++];
            }
            cache[r] = sums[i];
            count += j - k;
        }
        System.arraycopy(cache, 0, sums, start, t - start);
        return count;
    }

    public static int count1(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return process(sums, 0, n + 1, lower, upper);
    }

    //

    private static class Node {
        public long k;
        public Node l;
        public Node r;
        public long size;
        public long all;

        public Node(long key) {
            k = key;
            size = 1;
            all = 1;
        }
    }

    private static class SBSet {
        private Node root;
        private HashSet<Long> set = new HashSet<>();

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
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rls > ls) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        private Node add(Node cur, long key, boolean has) {
            if (cur == null) {
                return new Node(key);
            } else {
                cur.all++;
                if (key == cur.k) {
                    return cur;
                } else {
                    if (!has) {
                        cur.size++;
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

        public void add(long sum) {
            boolean has = set.contains(sum);
            root = add(root, sum, has);
            set.add(sum);
        }

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

        public long moreKeySize(long key) {
            return root != null ? (root.all - lessKeySize(key + 1)) : 0;
        }
    }

    public static int count2(int[] nums, int lower, int upper) {
        SBSet sb = new SBSet();
        long sum = 0;
        int ans = 0;
        sb.add(0);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            long a = sb.lessKeySize(sum - lower + 1);
            long b = sb.lessKeySize(sum - upper);
            ans += a - b;
            sb.add(sum);
        }
        return ans;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

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
