package base.str;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class Lexicography {
    private static String[] removeIndex(String[] arr, int idx) {
        String[] ans = new String[arr.length - 1];
        int ansIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != idx) {
                ans[ansIdx++] = arr[i];
            }
        }
        return ans;
    }

    private static TreeSet<String> process(String[] ss) {
        TreeSet<String> ans = new TreeSet<>();
        if (ss.length == 0) {
            ans.add("");
            return ans;
        }
        for (int i = 0; i < ss.length; i++) {
            String first = ss[i];
            String[] nexts = removeIndex(ss, i);
            TreeSet<String> next = process(nexts);
            for (String cur : next) {
                ans.add(first + cur);
            }
        }
        return ans;
    }

    // 全排列
    public static String lowestString1(String[] ss) {
        if (ss == null || ss.length == 0) {
            return "";
        }
        TreeSet<String> ans = process(ss);
        return ans.size() == 0 ? "" : ans.first();
    }

    //

    private static class Comp implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return (a + b).compareTo(b + a);
        }
    }

    public static String lowestString2(String[] ss) {
        if (ss == null || ss.length == 0) {
            return "";
        }
        Arrays.sort(ss, new Comp());
        String res = "";
        for (int i = 0; i < ss.length; i++) {
            res += ss[i];
        }
        return res;
    }

    private static String randomString(int maxLen) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            int val = (int) (Math.random() * 5);
            ans[i] = (Math.random() <= 0.5) ? (char) (65 + val) : (char) (97 + val);
        }
        return String.valueOf(ans);
    }

    private static String[] randomStrings(int maxArrLen, int maxLen) {
        String[] ans = new String[(int) ((maxArrLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = randomString(maxLen);
        }
        return ans;
    }

    private static String[] copy(String[] arr) {
        String[] ans = new String[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = String.valueOf(arr[i]);
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxArrLen = 6;
        int maxLen = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String[] arr1 = randomStrings(maxArrLen, maxLen);
            String[] arr2 = copy(arr1);
            String ans1 = lowestString1(arr1);
            String ans2 = lowestString2(arr2);
            if (!ans1.equals(ans2)) {
                System.out.println("Wrong");
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
