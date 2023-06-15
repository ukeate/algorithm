package base.str2;

public class DC3 {

    // 下标是排名, 值是后缀开始位置
    public int[] sa;

    // 下标是后缀开始位置, 值是排名
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

    private boolean leq(int a1, int a2, int b1, int b2) {
        return a1 < b1 || (a1 == b1 && a2 <= b2);
    }

    private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
        return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
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

    private int[] skew(int[] nums, int n, int K) {
        // 0组 >= 12组, 0组结尾时数组结尾补1个0, 12组结尾时，结尾的1组不足3字符，天然有一个或两个0
        int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
        int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
        for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
            if (0 != i % 3) {
                s12[j++] = i;
            }
        }
        // 三个字符标签排序放到sa12, sa12中下标为排名，值为位置
        radixPass(nums, s12, sa12, 2, n02, K);
        radixPass(nums, sa12, s12, 1, n02, K);
        radixPass(nums, s12, sa12, 0, n02, K);
        // 按排名遍历sa12,看nums中3个字符, name为排名
        // 填s12, 左边放1组, 右边放2组, '小0'放在了1组的末尾
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
            // 有重复排名
            sa12 = skew(s12, n02, name);
            for (int i = 0; i < n02; i++) {
                s12[sa12[i]] = i + 1;
            }
        } else {
            // name为s12[i] - 1
            for (int i = 0; i < n02; i++) {
                sa12[s12[i] - 1] = i;
            }
        }
        int[] s0 = new int[n0], sa0 = new int[n0];
        // sa12指导下的s0的排名, s0只是位置下标
        for (int i = 0, j = 0; i < n02; i++) {
            if (sa12[i] < n0) {
                s0[j++] = 3 * sa12[i];
            }
        }
        // 生成sa0
        radixPass(nums, s0, sa0, 0, n0, K);
        int[] sa = new int[n];
        // 合并sa12与sa0成为sa
        // t = n0 - n1 ,跳过0组结尾时多加在末尾排在前边的'小0'
        for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
            // sa12小排名，1组或2组的下标
            int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
            int j = sa0[p];
            // 0组1组最多比两次, 0组2组最多比3次
            /// s0 (s1) | s0 s1 (s2)
            /// s1 (s2) | s2 s0 (s1)
            // sa12[t] + n0 是1组下一个2组的位置, s12[...]取排名, 越界时为0
            // j / 3 找到s12中左边1组的位置, s12[...]取排名
            if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                    : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                //  12组胜
                sa[k] = i;
                t++;
                // 加剩余sa0
                if (t == n02) {
                    for (k++; p < n0; p++, k++) {
                        sa[k] = sa0[p];
                    }
                }
            } else {
                // 0组胜
                sa[k] = j;
                p++;
                // 加剩余sa12
                if (p == n0) {
                    for (k++; t < n02; t++, k++) {
                        sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                    }
                }
            }
        }
        return sa;
    }

    private int[] rank() {
        int n = sa.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[sa[i]] = i + 1;
        }
        return ans;
    }

}
