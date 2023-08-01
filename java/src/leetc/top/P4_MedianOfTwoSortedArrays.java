package leetc.top;

public class P4_MedianOfTwoSortedArrays {
    private static int downMedian(int[] a1, int s1, int e1, int[] a2, int s2, int e2) {
        while (s1 < e1) {
            int m1 = (s1 + e1) / 2;
            int m2 = (s2 + e2) / 2;
            // 偶数时1, 奇数时0
            int offset = ((e1 - s1 + 1) & 1) ^ 1;
            if (a1[m1] > a2[m2]) {
                e1 = m1;
                s2 = m2 + offset;
            } else if (a1[m1] < a2[m2]) {
                s1 = m1 + offset;
                e2 = m2;
            } else {
                return a1[m1];
            }
        }
        return Math.min(a1[s1], a2[s2]);
    }

    private static int findKthNum(int[] arr1, int[] arr2, int kth) {
        int[] longs = arr1.length >= arr2.length ? arr1 : arr2;
        int[] shorts = arr1.length < arr2.length ? arr1 : arr2;
        int l = longs.length, s = shorts.length;
        if (kth <= s) {
            return downMedian(shorts, 0, kth - 1, longs, 0, kth - 1);
        }
        if (kth > l) {
            if (shorts[kth - l - 1] >= longs[l - 1]) {
                return shorts[kth - l - 1];
            }
            if (longs[kth - s - 1] >= shorts[s - 1]){
                return longs[kth - s - 1];
            }
            return downMedian(shorts, kth - l, s - 1, longs, kth - s, l - 1);
        }
        if (longs[kth - s - 1] >= shorts[s - 1]) {
            return longs[kth - s - 1];
        }
        return downMedian(shorts, 0, s - 1, longs, kth - s, kth - 1);
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int size = nums1.length + nums2.length;
        boolean even = (size & 1) == 0;
        if (nums1.length != 0 & nums2.length != 0) {
            if (even) {
                return (double) (findKthNum(nums1, nums2, size / 2) + findKthNum(nums1, nums2, size / 2 + 1)) / 2;
            } else {
                return findKthNum(nums1, nums2, size / 2 + 1);
            }
        } else if (nums1.length != 0) {
            if (even) {
                return (double) (nums1[(size - 1) / 2] + nums1[size / 2]) / 2;
            } else {
                return nums1[size / 2];
            }
        } else if (nums2.length != 0) {
            if (even) {
                return (double) (nums2[(size - 1) / 2] + nums2[size / 2]) / 2;
            } else {
                return nums2[size / 2];
            }
        } else {
            return 0;
        }
    }
}
