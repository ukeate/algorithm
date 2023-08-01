package giant.c7;

import java.util.Arrays;
import java.util.HashSet;

// 有序数组，每个数平方的种数
public class Power2Diffs {
    public static int diff1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int cur : arr) {
            set.add(cur * cur);
        }
        return set.size();
    }

    //

    // abs先递减再递增
    public static int diff2(int[] arr) {
        int n = arr.length;
        int l = 0, r = n - 1;
        int count = 0, leftAbs = 0, rightAbs = 0;
        while (l <= r) {
            count++;
            leftAbs = Math.abs(arr[l]);
            rightAbs = Math.abs(arr[r]);
            if (leftAbs < rightAbs) {
                while (r >= 0 && Math.abs(arr[r]) == rightAbs) {
                    r--;
                }
            } else if (leftAbs > rightAbs) {
                while (l < n &&Math.abs(arr[l]) == leftAbs) {
                    l++;
                }
            } else {
                while (l < n && Math.abs(arr[l]) == leftAbs) {
                    l++;
                }
                while (r >= 0 && Math.abs(arr[r]) == rightAbs) {
                    r--;
                }
            }
        }
        return count;
    }

    //

    private static int[] randomSortedArr(int len, int val) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i <ans.length; i++) {
            ans[i] = (int) (Math.random() * val) - (int) (Math.random() * val);
        }
        Arrays.sort(ans);
        return ans;
    }

    public static void main(String[] args) {
        int times = 200000;
        int len = 100;
        int val = 500;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomSortedArr(len, val);
            int ans1 = diff1(arr);
            int ans2 = diff2(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
