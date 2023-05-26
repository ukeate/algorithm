package base.binary;

public class MostLeftNotLess {

    public static int mostLeftNotLess(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int l = 0;
        int r = arr.length - 1;
        int ans = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] >= num) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = new int[] {1, 1, 2, 2, 3, 3, 4, 4};
        System.out.println(mostLeftNotLess(arr, 3));
    }
}
