package leetc.top;

import java.util.Arrays;
import java.util.Comparator;

public class P179_LargestNumber {
    public static class Comp implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return (o2 + o1).compareTo(o1 + o2);
        }
    }

    public String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(strs, new Comp());
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append(str);
        }
        String ans = builder.toString();
        char[] str = ans.toCharArray();
        int idx = -1;
        for (int i = 0; i < str.length; i++) {
            if (str[i] != '0') {
                idx = i;
                break;
            }
        }
        return idx == -1 ? "0" : ans.substring(idx);
    }
}
