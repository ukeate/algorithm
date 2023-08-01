package leetc.top;

public class P42_TrappingRainWater {
    public static int trap(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int l = 1;
        int leftMax = arr[0];
        int r = n - 2;
        int rightMax = arr[n - 1];
        int water = 0;
        while(l <= r) {
            if (leftMax <= rightMax) {
                water += Math.max(0, leftMax - arr[l]);
                leftMax = Math.max(leftMax, arr[l++]);
            } else {
                water += Math.max(0, rightMax - arr[r]);
                rightMax = Math.max(rightMax, arr[r--]);
            }
        }
        return water;
    }
}
