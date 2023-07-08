package basic.c51;

// s1含有s2所有字符的最小子串长度
public class MinWindowLength {
    public static int min(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() < s2.length()) {
            return Integer.MAX_VALUE;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int[] map = new int[256];
        for (int i = 0; i < str2.length; i++) {
            map[str2[i]]++;
        }
        int left = 0;
        int right = 0;
        int all = str2.length;
        int min = Integer.MAX_VALUE;
        while (right < str1.length) {
            map[str1[right]]--;
            if (map[str1[right]] >= 0) {
                all--;
            }
            if (all == 0) {
                while (map[str1[left]] < 0) {
                    map[str1[left++]]++;
                }
                min = Math.min(min, right - left + 1);
                all++;
                map[str1[left++]]++;
            }
            right++;
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public static void main(String[] args) {
        String s1 = "adabbca";
        String s2 = "acb";
        System.out.println(min(s1, s2));
    }

}
