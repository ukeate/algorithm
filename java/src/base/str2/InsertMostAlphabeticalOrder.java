package base.str2;

// s2插入s1，使新字符串字典序最大
public class InsertMostAlphabeticalOrder {
    public static String sure(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        String p1 = s1 + s2;
        String p2 = s2 + s1;
        String ans = p1.compareTo(p2) > 0 ? p1 : p2;
        for (int end = 1; end < s1.length(); end++) {
            String cur = s1.substring(0, end) + s2 + s1.substring(end);
            if (cur.compareTo(ans) > 0) {
                ans = cur;
            }
        }
        return ans;
    }

    //


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
            for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
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

    public static int bestSplit(String s1, String s2, int first) {
        int n = s1.length();
        int m = s2.length();
        int end = n;
        for (int i = first, j = 0; i < n && j < m; i++, j++) {
            if (s1.charAt(i) < s2.charAt(j)) {
                end = i;
                break;
            }
        }
        String bestPrefix = s2;
        int bestSplit = first;
        for (int i = first + 1, j = m - 1; i <= end; i++, j--) {
            String curPrefix = s1.substring(first, i) + s2.substring(0, j);
            if (curPrefix.compareTo(bestPrefix) >= 0) {
                bestPrefix = curPrefix;
                bestSplit = i;
            }
        }
        return bestSplit;
    }

    public static String max(String s1, String s2) {
        if (s1 == null || s1.length() == 0) {
            return s2;
        }
        if (s2 == null || s2.length() == 0) {
            return s1;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length;
        int m = str2.length;
        int min = str1[0];
        int max = str1[0];
        for (int i = 1; i < n; i++) {
            min = Math.min(min, str1[i]);
            max = Math.max(max, str1[i]);
        }
        for (int i = 0; i < m; i++) {
            min = Math.min(min, str2[i]);
            max = Math.max(max, str2[i]);
        }
        int[] all = new int[n + m + 1];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            all[idx++] = str1[i] - min + 2;
        }
        all[idx++] = 1;
        for (int i = 0; i < m; i++) {
            all[idx++] = str2[i] - min + 2;
        }
        DC3 dc3 = new DC3(all, max - min + 2);
        int[] rank = dc3.rank;
        for (int i = 0; i < n; i++) {
            if (rank[i] < rank[n + 1]) {
                int best = bestSplit(s1, s2, i);
                return s1.substring(0, best) + s2 + s1.substring(best);
            }
        }
        return s1 + s2;
    }

    //

    public static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (range * Math.random()) + '0');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 30;
        int maxVal = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len1 = (int) ((maxLen + 1) * Math.random());
            int len2 = (int) ((maxLen + 1) * Math.random());
            String s1 = randomStr(len1, maxVal);
            String s2 = randomStr(len2, maxVal);
            String ans1 = sure(s1, s2);
            String ans2 = max(s1, s2);
            if (!ans1.equals(ans2)) {
                System.out.println("Wrong");
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
