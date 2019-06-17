package chapter2;

import org.junit.Test;

/**
 * Created by outrun on 4/11/16.
 */
public class MaxSubSum {

    /**
     * worst Theta(N^3)
     * * sigma(i = 0)(N - 1) * sigma(j = i)(N - 1) * Sigma(k = i)(j) * 1
     *
     * @param a
     * @return
     */
    public int maxSubSum1(int[] a) {
        int maxSum = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                int thisSum = 0;

                for (int k = i; k <= j; k++) {
                    thisSum += a[k];
                }

                if (thisSum > maxSum) {
                    maxSum = thisSum;
                }
            }
        }

        return maxSum;
    }


    public int maxSubSum2(int[] a) {
        int maxSum = 0;

        for (int i = 0; i < a.length; i++) {
            int thisSum = 0;
            for (int j = i; j < a.length; j++) {
                thisSum += a[j];

                if (thisSum > maxSum) {
                    maxSum = thisSum;
                }
            }
        }

        return maxSum;
    }

    private int max3(int a, int b, int c) {
        int max = a;
        if (b > max) {
            max = b;
        }

        if (c > max) {
            max = c;
        }

        return max;
    }

    private int maxSumRec(int[] a, int left, int right) {

        if (left >= right) {
            if (a[right] > 0) {
                return a[right];
            } else {
                return 0;
            }
        }

        int midInd = (left + right) / 2;

        int maxLeftSum = maxSumRec(a, left, midInd);
        int maxRightSum = maxSumRec(a, midInd + 1, right);

        int maxLeftBorderSum = 0, leftBorderSum = 0;
        for (int i = midInd; i >= left; i--) {
            leftBorderSum += a[i];

            if (leftBorderSum > maxLeftBorderSum) {
                maxLeftBorderSum = leftBorderSum;
            }
        }

        int maxRightBorderSum = 0, rightBorderSum = 0;
        for (int i = midInd + 1; i <= right; i++) {
            rightBorderSum += a[i];

            if (rightBorderSum > maxRightBorderSum) {
                maxRightBorderSum = rightBorderSum;
            }
        }

        return max3(maxLeftSum, maxRightSum,
                maxLeftBorderSum + maxRightBorderSum);
    }

    public int maxSubSum3(int[] a) {
        return maxSumRec(a, 0, a.length - 1);
    }

    public int maxSubSum4(int[] a) {
        int maxSum = 0, thisSum = 0;

        for (int j = 0; j < a.length; j++) {
            thisSum += a[j];

            if (thisSum > maxSum) {
                maxSum = thisSum;
            } else if (thisSum < 0) {
                thisSum = 0;
            }
        }

        return maxSum;
    }

    @Test
    public void testHere() {
        int[] arr = {4, -3, 5, -2, -1, 2, 6, -2};
        int[] arr2 = {-1, -3, -1, -2, -1, -2, -6, -2};
        System.out.println(maxSubSum3(arr2));
    }
}
