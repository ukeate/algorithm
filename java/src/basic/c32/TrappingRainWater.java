package basic.c32;

// 直方图最大存水量
public class TrappingRainWater {
    public static int water1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int water = 0;
        for (int i = 1; i < n - 1; i++) {
            int leftMax = Integer.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                leftMax = Math.max(leftMax, arr[j]);
            }
            int rightMax = Integer.MIN_VALUE;
            for (int j = i + 1; j < n; j++) {
                rightMax = Math.max(rightMax, arr[j]);
            }
            water += Math.max(Math.min(leftMax, rightMax) - arr[i], 0);
        }
        return water;
    }

    //

    public static int water2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] leftMaxs = new int[n];
        leftMaxs[0] = arr[0];
        for (int i = 1; i < n; i++) {
            leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
        }
        int[] rightMaxs = new int[n];
        rightMaxs[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
        }
        int water = 0;
        for (int i = 1; i < n - 1; i++) {
            water += Math.max(Math.min(leftMaxs[i - 1], rightMaxs[i + 1]) - arr[i], 0);
        }
        return water;
    }

    //

    public static int water3(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] rightMaxs = new int[n];
        rightMaxs[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
        }
        int water = 0;
        int leftMax = arr[0];
        for (int i = 1; i < n - 1; i++) {
            water += Math.max(Math.min(leftMax, rightMaxs[i + 1]) - arr[i], 0);
            leftMax = Math.max(leftMax, arr[i]);
        }
        return water;
    }

    //

    public static int water4(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int l = 1;
        int leftMax = arr[0];
        int r = n - 2;
        int rightMax = arr[n - 1];
        int water = 0;
        while (l <= r) {
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

    //

    public static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[(int) (Math.random() * len) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * maxVal) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 200;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = water1(arr);
            int ans2 = water2(arr);
            int ans3 = water3(arr);
            int ans4 = water4(arr);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
