package base.dp4_quad;

// 划分数组，使小的部分最大
public class BestSplit {
    public static int split1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int ans = 0;
        for (int s = 0; s < n - 1; s++) {
            int sumL = 0;
            for (int l = 0; l <= s; l++) {
                sumL += arr[l];
            }
            int sumR = 0;
            for (int r = s + 1; r < n; r++) {
                sumR += arr[r];
            }
            ans = Math.max(ans, Math.min(sumL, sumR));
        }
        return ans;
    }

    public static int split2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int sumAll = 0;
        for (int num : arr) {
            sumAll += num;
        }
        int ans = 0;
        int sumL = 0;
        for (int s = 0; s < n - 1; s++) {
            sumL += arr[s];
            int sumR = sumAll - sumL;
            ans = Math.max(ans, Math.min(sumL, sumR));
        }
        return ans;
    }

    public static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal =  30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
           int len = (int) (Math.random() * (maxLen + 1));
           int[] arr = randomArr(len, maxVal);
           int ans1 = split1(arr);
           int ans2 = split2(arr);
           if (ans1 != ans2) {
               System.out.println("Wrong");
               break;
           }
        }
        System.out.println("test end");
    }

}
