package base.str2;

// https://leetcode.com/problems/create-maximum-number/
// 两个集合顺序挑k个，从左向右排列使形成的数最大
public class CreateMaxNum {

    // dp存[i,..]中拿j个，最好的开始位置
    private static int[][] preDp(int[] arr) {
        int size = arr.length;
        int pick = arr.length + 1;
        int[][] dp = new int[size][pick];
        for (int get = 1; get < pick; get++) {
            int maxIdx = size - get;
            for (int i = size - get; i >= 0; i--) {
                if (arr[i] >= arr[maxIdx]) {
                    maxIdx = i;
                }
                dp[i][get] = maxIdx;
            }
        }
        return dp;
    }

    private static int[] maxPick(int[] arr, int[][] dp, int pick) {
        int[] res = new int[pick];
        for (int resIdx = 0, dpRow = 0; pick > 0; pick--, resIdx++) {
            res[resIdx] = arr[dp[dpRow][pick]];
            dpRow = dp[dpRow][pick] + 1;
        }
        return res;
    }

    private static boolean moreThan(int[] arr1, int[] arr2) {
        int i = 0;
        int j = 0;
        while (i < arr1.length && j < arr2.length && arr1[i] == arr2[j]) {
            i++;
            j++;
        }
        return j == arr2.length || (i < arr1.length && arr1[i] > arr2[j]);
    }

    private static int[] mergeBySA(int[] nums1, int[] nums2) {
        int size1 = nums1.length;
        int size2 = nums2.length;
        int[] nums = new int[size1 + 1 + size2];
        for (int i = 0; i < size1; i++) {
            nums[i] = nums1[i] + 2;
        }
        nums[size1] = 1;
        for (int j = 0; j < size2; j++) {
            nums[j + size1 + 1] = nums2[j] + 2;
        }
        DC3 dc3 = new DC3(nums, 11);
        int[] rank = dc3.rank;
        int[] ans = new int[size1 + size2];
        int i = 0;
        int j = 0;
        int r = 0;
        while (i < size1 && j < size2) {
            ans[r++] = rank[i] > rank[j + size1 + 1] ? nums1[i++] : nums2[j++];
        }
        while (i < size1) {
            ans[r++] = nums1[i++];
        }
        while (j < size2) {
            ans[r++] = nums2[j++];
        }
        return ans;
    }

    public static class DC3 {
        public int[] sa;
        public int[] rank;
        public DC3(int[] nums, int max) {
            sa = sa(nums, max);
            rank = rank();
        }

        private int[] sa(int[] nums, int max) {
            int n = nums.length;
            int[] arr = new int[n + 3];
            for (int i = 0; i < n; i++) {
                arr[i] = nums[i];
            }
            return skew(arr, n, max);
        }

        private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
            int[] cnt = new int[k + 1];
            for (int i = 0; i < n; ++i) {
                cnt[nums[input[i] + offset]]++;
            }
            for (int i = 0, sum = 0; i < cnt.length; ++i) {
                int t = cnt[i];
                cnt[i] = sum;
                sum += t;
            }
            for (int i = 0; i < n; ++i) {
                output[cnt[nums[input[i] + offset]]++] = input[i];
            }
        }

        private boolean leq(int a1, int a2, int b1, int b2) {
            return a1 < b1 || (a1 == b1 && a2 <= b2);
        }

        private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
            return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
        }

        private int[] rank() {
            int n = sa.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[sa[i]] = i;
            }
            return ans;
        }

        private int[] skew(int[] nums, int n, int K) {
            int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
            int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
            for (int i = 0, j = 0; i < n + (n0 - n1) ; ++i) {
                if (0 != i % 3) {
                    s12[j++] = i;
                }
            }
            radixPass(nums, s12, sa12, 2, n02, K);
            radixPass(nums, sa12, s12, 1, n02, K);
            radixPass(nums, s12, sa12, 0, n02, K);
            int name = 0, c0 = -1, c1 = -1, c2 = -1;
            for (int i = 0; i < n02; ++i) {
                if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                    name++;
                    c0 = nums[sa12[i]];
                    c1 = nums[sa12[i] + 1];
                    c2 = nums[sa12[i] + 2];
                }
                if (1 == sa12[i] % 3) {
                    s12[sa12[i] / 3] = name;
                } else {
                    s12[sa12[i] / 3 + n0] = name;
                }
            }
            if (name < n02) {
                sa12 = skew(s12, n02, name);
                for (int i = 0; i < n02; i++) {
                    s12[sa12[i]] = i + 1;
                }
            } else {
                for (int i = 0; i < n02; i++) {
                    sa12[s12[i] - 1] = i;
                }
            }
            int[] s0 = new int[n0], sa0 = new int[n0];
            for (int i = 0, j = 0; i < n02; i++) {
                if (sa12[i] < n0) {
                    s0[j++] = 3 * sa12[i];
                }
            }
            radixPass(nums, s0, sa0, 0, n0, K);
            int[] sa = new int[n];
            for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
                int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                int j = sa0[p];
                if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                    sa[k] = i;
                    t++;
                    if (t == n02) {
                        for (k++; p < n0; p++, k++) {
                            sa[k] = sa0[p];
                        }
                    }
                } else {
                    sa[k] = j;
                    p++;
                    if (p == n0) {
                        for (k++; t < n02; t++, k++) {
                            sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                        }
                    }
                }
            }
            return sa;
        }
    }

    public static int[] max(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        if (k < 0 || k > len1 + len2) {
            return null;
        }
        int[] res = new int[k];
        int[][] dp1 = preDp(nums1);
        int[][] dp2 = preDp(nums2);
        for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
           int[] pick1 = maxPick(nums1, dp1, get1);
           int[] pick2 = maxPick(nums2, dp2, k - get1);
           int[] merge = mergeBySA(pick1, pick2);
           res = moreThan(res, merge) ? res : merge;
        }
        return res;
    }
}
