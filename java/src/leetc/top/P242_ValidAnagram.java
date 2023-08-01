package leetc.top;

public class P242_ValidAnagram {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] str = s.toCharArray();
        char[] tr = t.toCharArray();
        int[] count = new int[256];
        for (char cha : str) {
            count[cha]++;
        }
        for (char cha : tr) {
            if (--count[cha] < 0) {
                return false;
            }
        }
        return true;
    }
}
