package basic.c55;

import java.util.HashMap;

// 无序数组中，最长连续序列长度
public class LongestConsecutive {
    private static int merge(HashMap<Integer, Integer> map, int preEnd, int curStart) {
        int preStart = preEnd - map.get(preEnd) + 1;
        int curEnd = curStart + map.get(curStart) - 1;
        int len = curEnd - preStart + 1;
        map.put(preStart, len);
        map.put(curEnd, len);
        return len;
    }

    public static int longest(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = 1;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                continue;
            }
            map.put(arr[i], 1);
            if (map.containsKey(arr[i] - 1)) {
                max = Math.max(max, merge(map, arr[i] - 1, arr[i]));
            }
            if (map.containsKey(arr[i] + 1)) {
                max = Math.max(max, merge(map, arr[i], arr[i] + 1));
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {100, 4, 200, 1, 3, 2};
        System.out.println(longest(arr));
    }
}
