package chapter1;

import org.junit.Test;

/**
 * Created by outrun on 4/7/16.
 */
public class Ex1_1 {

    private int[] arr = new int[]{5, 9, 8, 1, 2, 3, 4};
    private int arrLen = arr.length;

    private void squeeze (int [] arr, int ind, int num) {
        int len = arr.length;
        int preNum;
        for (int i = ind; i < len; i++) {
            preNum = arr[ind];
            arr[ind] = num;
            num = preNum;
        }
    }

    private void orderlySqueeze(int[] arr, int num) {
        int len = arr.length;

        for(int i = 0; i < len; i++) {
            if (arr[i] < num) {
                squeeze (arr, i, num);
                break;
            }
        }
    }

    /**
     * time k(n - k) + k
     * O(kn)
     */
    @Test
    public void selectK() {
        int k = 4;
        int[] resultArr = new int[k];

        for (int i = 0; i < k; i++) {
            resultArr[i] = arr[i];
        }

        for (int i = k; i < arrLen; i++) {
             orderlySqueeze(resultArr, arr[i]);
        }

        System.out.println(resultArr[k - 1]);

    }

}
