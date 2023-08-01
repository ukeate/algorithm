package leetc.top;

public class P11_ContainerWithMostWater {
    public static int maxArea(int[] h) {
        int max = 0;
        int l = 0, r = h.length - 1;
        while (l < r) {
            max = Math.max(max, Math.min(h[l], h[r]) * (r - l));
            if (h[l] > h[r]) {
                r--;
            } else {
                l++;
            }
        }
        return max;
    }
}
