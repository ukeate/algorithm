package basic.c36;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.HashMap;
import java.util.HashSet;

// 数组切分成累加和相等的4部分，切分位置去掉，返回是否能够
public class Split4Parts {
    public static boolean can1(int[] arr) {
        if (arr == null || arr.length < 7) {
            return false;
        }
        HashSet<String> set = new HashSet<>();
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        int leftSum = arr[0];
        for (int i = 1; i < arr.length - 1; i++) {
            set.add(leftSum + "_" + (sum - leftSum - arr[i]));
            leftSum += arr[i];
        }
        int l = 1;
        int lsum = arr[0];
        int r = arr.length - 2;
        int rsum = arr[arr.length - 1];
        while (l < r - 3) {
            if (lsum == rsum) {
                String lkey = String.valueOf(lsum * 2 + arr[l]);
                String rkey = String.valueOf(rsum * 2 + arr[r]);
                if (set.contains(lkey + "_" + rkey)) {
                    return true;
                }
                lsum += arr[l++];
            } else if (lsum < rsum) {
                lsum += arr[l++];
            } else {
                rsum += arr[r--];
            }
        }
        return false;
    }

    //

    public static boolean can2(int[] arr) {
        if (arr == null || arr.length < 7) {
            return false;
        }
        // <前累加和, 位置>
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = arr[0];
        for (int i = 1; i < arr.length; i++) {
            map.put(sum, i);
            sum += arr[i];
        }
        int lsum = arr[0];
        for (int s1 = 1; s1 < arr.length - 5; s1++) {
            int checkSum = lsum * 2 + arr[s1];
            if (map.containsKey(checkSum)) {
                int s2 = map.get(checkSum);
                checkSum += lsum + arr[s2];
                if (map.containsKey(checkSum)) {
                    int s3 = map.get(checkSum);
                    if (checkSum + arr[s3] + lsum == sum) {
                        return true;
                    }
                }
            }
            lsum += arr[s1];
        }
        return false;
    }

    //

    private static int[] randomArr() {
        int[] res = new int[(int) (10 * Math.random()) + 7];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (10 * Math.random()) + 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int times = 3000000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr();
            if (can1(arr) ^ can2(arr)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
