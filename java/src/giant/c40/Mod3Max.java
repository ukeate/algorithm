package giant.c40;

import java.util.Arrays;
import java.util.TreeSet;

// 来自去哪儿网
// 给定一个arr，里面的数字都是0~9
// 你可以随意使用arr中的数字，哪怕打乱顺序也行
// 请拼出一个能被3整除的，最大的数字，用str形式返回
public class Mod3Max {
    private static void process1(int[] arr, int idx, StringBuilder builder, TreeSet<String> set) {
        if (idx == arr.length) {
            if (builder.length() != 0 && Integer.valueOf(builder.toString()) % 3 == 0) {
                set.add(builder.toString());
            }
        } else {
            process1(arr, idx + 1, builder, set);
            builder.append(arr[idx]);
            process1(arr, idx + 1, builder, set);
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    public static String max1(int[] arr) {
        Arrays.sort(arr);
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        StringBuilder builder = new StringBuilder();
        TreeSet<String> set = new TreeSet<>((a, b) -> Integer.valueOf(b).compareTo(Integer.valueOf(a)));
        process1(arr, 0, builder, set);
        return set.isEmpty() ? "" : set.first();
    }

    //

    private static int nextMod(int require, int current) {
        if (require == 0) {
            if (current == 0) {
                return 0;
            } else if (current == 1) {
                return 2;
            } else {
                return 1;
            }
        } else if (require == 1) {
            if (current == 0) {
                return 1;
            } else if (current == 1) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (current == 0) {
                return 2;
            } else if (current == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static boolean smaller(String p1, String p2) {
        if (p1.length() != p2.length()) {
            return p1.length() < p2.length();
        }
        return p1.compareTo(p2) < 0;
    }
    private static String process2(int[] arr, int idx, int mod) {
        if (idx == arr.length) {
            return mod == 0 ? "" : "$";
        }
        String p1 = "$";
        int nextMod = nextMod(mod, arr[idx] % 3);
        String next = process2(arr, idx + 1, nextMod);
        if (!next.equals("$")) {
            p1 = arr[idx] + next;
        }
        String p2 = process2(arr, idx + 1, mod);
        if (p1.equals("$") && p2.equals("$")) {
            return "$";
        }
        if (!p1.equals("$") && !p2.equals("$")) {
            return smaller(p1, p2) ? p2 : p1;
        }
        return p1.equals("$") ? p2 : p1;
    }

    public static String max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        Arrays.sort(arr);
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        if (arr[0] == 0) {
            return "0";
        }
        String ans = process2(arr, 0, 0);
        String res = ans.replaceAll("^(0+)", "");
        if (!res.equals("")) {
            return res;
        }
        return ans.equals("") ? ans : "0";
    }
}
