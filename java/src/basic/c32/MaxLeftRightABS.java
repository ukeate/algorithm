package basic.c32;

// 划分数组，使左右最大值差最大
public class MaxLeftRightABS {

    public static int max1(int[] arr) {
        int res = Integer.MIN_VALUE;
        int maxLeft = 0;
        int maxRight = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            maxLeft = Integer.MIN_VALUE;
            for (int j = 0; j <= i; j++) {
                maxLeft = Math.max(arr[j], maxLeft);
            }
            maxRight = Integer.MIN_VALUE;
            for (int j = i + 1; j < arr.length; j++) {
                maxRight = Math.max(arr[j], maxRight);
            }
            res = Math.max(Math.abs(maxLeft - maxRight), res);
        }
        return res;
    }

    //

    public static int max2(int[] arr) {
        int[] lm = new int[arr.length];
        int[] rm = new int[arr.length];
        lm[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            lm[i] = Math.max(lm[i - 1], arr[i]);
        }
        rm[arr.length - 1] = arr[arr.length - 1];
        for (int i = arr.length - 2; i >= 0; i--) {
            rm[i] = Math.max(rm[i + 1], arr[i]);
        }
        int max = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            max = Math.max(max, Math.abs(lm[i] - rm[i + 1]));
        }
        return max;
    }

    //

    // 累加数组最小值在两头
    public static int max3(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(arr[i], max);
        }
        return max - Math.min(arr[0], arr[arr.length - 1]);
    }

    //

    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 1000) - 499;
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = randomArr(200);
        System.out.println(max1(arr));
        System.out.println(max2(arr));
        System.out.println(max3(arr));
    }

}
