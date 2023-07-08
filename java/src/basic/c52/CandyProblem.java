package basic.c52;

// 按孩子的得分至少分一块糖，要求得分高的糖果比相邻低的数量多，相等时无要求(相等)，求最少总糖果数
// https://leetcode.com/problems/candy/
public class CandyProblem {
    public static int candy1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        int[] left = new int[n];
        for (int i = 1; i < n; i++) {
            if (arr[i - 1] < arr[i]) {
                left[i] = left[i - 1] + 1;
            }
        }
        int[] right = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.max(left[i], right[i]);
        }
        return ans + n;
    }

    //

    private static int nextMinIdx1(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] <= arr[i + 1]) {
                return i;
            }
        }
        return arr.length - 1;
    }

    private static int rightCands(int[] arr, int left, int right) {
        int n = right - left + 1;
        return (n + 1) * n / 2;
    }

    public static int candy2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int idx = nextMinIdx1(arr, 0);
        int res = rightCands(arr, 0, idx++);
        // 左坡度
        int lBase = 1;
        while (idx < arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                res += ++lBase;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                int next = nextMinIdx1(arr, idx - 1);
                int rCands = rightCands(arr, idx - 1, next++);
                // 右坡度
                int rBase = next - idx + 1;
                res += rCands - (rBase > lBase ? lBase : rBase);
                lBase = 1;
                idx = next;
            } else {
                res += 1;
                lBase = 1;
                idx++;
            }
        }
        return res;
    }

    //

    private static int nextMinIdx2(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] < arr[i + 1]) {
                return i;
            }
        }
        return arr.length - 1;
    }

    private static int[] rightCandsAndBase(int[] arr, int left, int right) {
        int base = 1;
        int cands = 1;
        for (int i = right - 1; i >= left; i--) {
            if (arr[i] == arr[i + 1]) {
                cands += base;
            } else {
                cands += ++base;
            }
        }
        return new int[]{cands, base};
    }

    // 相等分数糖果相等
    public static int candy3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int idx = nextMinIdx2(arr, 0);
        int[] data = rightCandsAndBase(arr, 0, idx++);
        int res = data[0];
        int lBase = 1;
        int same = 1;
        int next = 0;
        while (idx < arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                res += ++lBase;
                same = 1;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                next = nextMinIdx2(arr, idx - 1);
                data = rightCandsAndBase(arr, idx - 1, next++);
                if (data[1] <= lBase) {
                    res += data[0] - data[1];
                } else {
                    res += -lBase * same + data[0] - data[1] + data[1] * same;
                }
                idx = next;
                lBase = 1;
                same = 1;
            } else {
                res += lBase;
                same++;
                idx++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {3, 0, 5, 5, 4, 4, 0};
        System.out.println(candy1(arr));
        System.out.println(candy2(arr));
        System.out.println(candy3(arr));
    }
}
