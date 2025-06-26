package giant.c36;

/**
 * 递归字符串构造与查询问题
 * 
 * 问题描述：
 * 根据以下规则构造字符串序列S1, S2, ..., S25：
 * - L[1]对应a，L[2]对应b，L[3]对应c，...，L[25]对应y
 * - S1 = a
 * - S(i) = S(i-1) + L[i] + reverse(invert(S(i-1)))
 * 
 * invert操作定义：
 * - 对于字符c，invert(c) = (char)(('a' << 1) + 24 - c)
 * - 例如：invert('a') = 'y', invert('b') = 'x', invert('y') = 'a'
 * 
 * 示例过程：
 * - S1 = "a"
 * - S2 = "a" + "b" + reverse(invert("a")) = "a" + "b" + "y" = "aby" 
 * - S3 = "aby" + "c" + reverse(invert("aby")) = "aby" + "c" + "axy" = "abycaxy"
 * 
 * 问题目标：
 * 给定n和k，返回Sn的第k位字符（n从1开始，k从1开始）
 * 
 * 解题思路：
 * 使用递归 + 数学优化：
 * 1. 预计算每个Si的长度：lens[i] = 2 * lens[i-1] + 1
 * 2. 对于查询Sn[k]，根据k的位置判断它在哪个部分：
 *    - 如果k在前半部分：递归查询Sn-1[k]  
 *    - 如果k是中间位置：返回第n个字母
 *    - 如果k在后半部分：递归查询前半部分对应位置，然后取invert
 * 3. 避免实际构造字符串，只通过递归计算目标字符
 * 
 * 算法优势：
 * - 时间复杂度：O(n)，每层递归减少问题规模
 * - 空间复杂度：O(n)，递归栈空间 + 长度数组
 * - 相比直接构造字符串O(2^n)的复杂度，效率极大提升
 * 
 * 来源：网易面试题
 * 
 * @author Zhu Runqi
 */
public class ReverseInvertString {
    
    // 预计算的长度数组，lens[i]表示Si的长度
    public static int[] lens = null;

    /**
     * 初始化长度数组
     * lens[i] = 2 * lens[i-1] + 1，其中lens[1] = 1
     */
    private static void fillLens() {
        lens = new int[26];
        lens[1] = 1;  // S1 = "a"的长度为1
        for (int i = 2; i <= 25; i++) {
            // Si的长度 = S(i-1)的长度 + 1个字符 + reverse(invert(S(i-1)))的长度
            lens[i] = (lens[i - 1] << 1) + 1;
        }
    }

    /**
     * 字符取反操作
     * 
     * invert规则：a和y互换，b和x互换，c和w互换，...
     * 公式：invert(c) = (char)(('a' << 1) + 24 - c)
     * 
     * @param c 输入字符
     * @return 取反后的字符
     */
    private static char invert(char c) {
        return (char) (('a' << 1) + 24 - c);
    }

    /**
     * 递归查找Sn的第k位字符
     * 
     * 核心思想：
     * Sn的结构为：S(n-1) + L[n] + reverse(invert(S(n-1)))
     * 根据k的位置判断在哪个部分，然后递归求解
     * 
     * @param n 字符串序号（1-25）
     * @param k 要查找的位置（从1开始）
     * @return Sn的第k位字符
     */
    public static char find(int n, int k) {
        if (lens == null) {
            fillLens();
        }
        
        // 递归基：S1 = "a"
        if (n == 1) {
            return 'a';
        }
        
        int half = lens[n - 1];  // S(n-1)的长度
        
        if (k <= half) {
            // k在前半部分S(n-1)中
            return find(n - 1, k);
        } else if (k == half + 1) {
            // k在中间位置，是第n个字母
            return (char) ('a' + n - 1);
        } else {
            // k在后半部分reverse(invert(S(n-1)))中
            // 需要找到在S(n-1)中对应的位置，然后取invert
            // reverse操作：第i位对应原来的第(length-i+1)位
            int posInOriginal = ((half + 1) << 1) - k;  // 在S(n-1)中对应的位置
            return invert(find(n - 1, posInOriginal));
        }
    }

    /**
     * 字符串取反操作（用于验证）
     */
    private static String invert(String s) {
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length; i++) {
            str[i] = invert(str[i]);
        }
        return String.valueOf(str);
    }

    /**
     * 字符串取反并反转操作（用于验证）
     */
    private static String reverseInvert(String s) {
        char[] invert = invert(s).toCharArray();
        for (int l = 0, r = invert.length - 1; l < r; l++, r--) {
            char tmp = invert[l];
            invert[l] = invert[r];
            invert[r] = tmp;
        }
        return String.valueOf(invert);
    }

    /**
     * 直接生成Sn字符串（用于验证小规模测试）
     */
    private static String generateStr(int n) {
        String s = "a";
        for (int i = 2; i <= n; i++) {
            s = s + (char) ('a' + i - 1) + reverseInvert(s);
        }
        return s;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 递归字符串构造与查询问题 ===\n");
        
        // 手动测试小规模用例
        System.out.println("=== 手动测试用例 ===");
        for (int n = 1; n <= 5; n++) {
            String actual = generateStr(n);
            System.out.println("S" + n + " = " + actual + " (长度: " + actual.length() + ")");
            
            // 验证几个位置的字符
            for (int k = 1; k <= Math.min(actual.length(), 5); k++) {
                char expected = actual.charAt(k - 1);
                char result = find(n, k);
                System.out.println("  第" + k + "位: 期望=" + expected + ", 实际=" + result + 
                                 (expected == result ? " ✓" : " ✗"));
            }
            System.out.println();
        }
        
        // 验证算法正确性
        System.out.println("=== 算法正确性验证 ===");
        int testN = 20;
        String str = generateStr(testN);
        int len = str.length();
        System.out.println("验证S" + testN + "的所有位置...");
        
        boolean allCorrect = true;
        for (int i = 1; i <= len; i++) {
            if (str.charAt(i - 1) != find(testN, i)) {
                System.out.println("位置" + i + "验证失败！");
                allCorrect = false;
                break;
            }
        }
        
        if (allCorrect) {
            System.out.println("所有 " + len + " 个位置验证通过！");
        }
        
        // 展示长度增长规律
        System.out.println("\n=== 长度增长规律 ===");
        fillLens();
        System.out.println("Sn的长度变化：");
        for (int i = 1; i <= 10; i++) {
            System.out.println("S" + i + "长度: " + lens[i]);
        }
        
        // 大数据测试
        System.out.println("\n=== 大数据测试 ===");
        int bigN = 25;
        int[] testPositions = {1, 100, 1000, 10000, 100000};
        
        for (int pos : testPositions) {
            if (pos <= lens[bigN]) {
                long startTime = System.currentTimeMillis();
                char result = find(bigN, pos);
                long endTime = System.currentTimeMillis();
                System.out.println("S" + bigN + "[" + pos + "] = " + result + 
                                 " (用时: " + (endTime - startTime) + "ms)");
            }
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 递归结构：");
        System.out.println("   - Sn = S(n-1) + L[n] + reverse(invert(S(n-1)))");
        System.out.println("   - 长度递推：lens[n] = 2 * lens[n-1] + 1");
        System.out.println();
        System.out.println("2. 字符取反规则：");
        System.out.println("   - invert('a') = 'y', invert('b') = 'x', ...");
        System.out.println("   - 公式：invert(c) = (char)(('a' << 1) + 24 - c)");
        System.out.println();
        System.out.println("3. 位置映射：");
        System.out.println("   - k ≤ half: 在前半部分S(n-1)中");
        System.out.println("   - k = half+1: 中间字符L[n]");
        System.out.println("   - k > half+1: 在后半部分，需要映射到前半部分后取反");
        System.out.println();
        System.out.println("4. 复杂度优势：");
        System.out.println("   - 递归算法：时间O(n)，空间O(n)");
        System.out.println("   - 直接构造：时间O(2^n)，空间O(2^n)");
        System.out.println("   - 当n=25时，字符串长度达到2^25-1 ≈ 3300万字符！");
    }
}
