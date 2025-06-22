package base.str;

/**
 * 字符串旋转问题
 * 问题描述：判断字符串a是否是字符串b的旋转结果
 * 
 * 算法思路：
 * 如果字符串a是字符串b的旋转结果，那么a一定是b+b的子串
 * 例如：a="abcde", b="cdeab"
 * b+b = "cdeabcdeab"，可以看到a在其中出现
 * 
 * 核心思想：
 * 1. 首先检查两个字符串长度是否相等（旋转不改变长度）
 * 2. 将字符串b与自身拼接得到b2 = b + b
 * 3. 在b2中查找字符串a是否存在
 * 4. 使用KMP算法进行高效的字符串匹配
 * 
 * 时间复杂度：O(N) - KMP算法的时间复杂度
 * 空间复杂度：O(M) - KMP的next数组空间，M为模式串长度
 * 
 * 示例：
 * a = "aabbbbbb", b = "bbbbbbaa"
 * b + b = "bbbbbbaabbbbbbaa"
 * 在其中可以找到"aabbbbbb"，所以返回true
 */
public class IsRotation {
    
    /**
     * 构建KMP算法的next数组（前缀函数）
     * next[i]表示模式串[0...i-1]的最长相等前后缀长度
     * 
     * 算法思路：
     * 1. next[0] = -1（特殊标记）
     * 2. next[1] = 0（长度为1的字符串没有真前缀和真后缀）
     * 3. 对于i >= 2，利用已计算的结果递推求解
     * 
     * 关键洞察：
     * 如果s[i-1] == s[cn]，则next[i] = cn + 1
     * 否则，cn跳转到next[cn]继续尝试
     * 
     * @param s 模式串字符数组
     * @return next数组，用于KMP算法的失配跳转
     */
    private static int[] nextArr(char[] s) {
        if (s.length == 1) {
            return new int[]{-1};  // 长度为1的特殊情况
        }
        
        int[] next = new int[s.length];
        next[0] = -1;  // 第0位特殊标记
        next[1] = 0;   // 第1位必定为0
        int i = 2;     // 从第2位开始计算
        int cn = 0;    // 当前匹配位置
        
        while (i < next.length) {
            if (s[i - 1] == s[cn]) {
                // 匹配成功，扩展匹配长度
                next[i++] = ++cn;
            } else if (cn > 0) {
                // 匹配失败，cn跳转到next[cn]
                cn = next[cn];
            } else {
                // cn已经为0，无法再跳转
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * KMP字符串匹配算法
     * 在文本串s中查找模式串m的第一次出现位置
     * 
     * 算法步骤：
     * 1. 构建模式串的next数组
     * 2. 使用双指针x（文本串）和y（模式串）进行匹配
     * 3. 匹配成功时两指针同时前进
     * 4. 匹配失败时根据next数组调整模式串指针
     * 
     * 优势：
     * - 避免了暴力匹配中的回退
     * - 最坏情况下时间复杂度仍为O(N+M)
     * 
     * @param s 文本串（在其中查找）
     * @param m 模式串（要查找的字符串）
     * @return 模式串在文本串中的起始位置，未找到返回-1
     */
    private static int match(String s, String m) {
        if (s.length() < m.length()) {
            return -1;  // 文本串比模式串短，不可能匹配
        }
        
        char[] ss = s.toCharArray();    // 文本串字符数组
        char[] mm = m.toCharArray();    // 模式串字符数组
        int[] next = nextArr(mm);       // 构建next数组
        int x = 0, y = 0;               // 双指针
        
        // 开始匹配过程
        while (x < ss.length && y < mm.length) {
            if (ss[x] == mm[y]) {
                // 字符匹配，双指针同时前进
                x++;
                y++;
            } else if (next[y] == -1) {
                // 模式串第一个字符就不匹配，文本串指针前进
                x++;
            } else {
                // 利用next数组进行智能跳转
                y = next[y];
            }
        }
        
        // 判断是否完整匹配
        return y == mm.length ? x - y : -1;
    }
    
    /**
     * 判断字符串a是否是字符串b的旋转结果
     * 
     * 算法核心思想：
     * 如果a是b的旋转，那么一定存在一个分割点k，使得：
     * a = b[k:] + b[:k]
     * 这等价于a是(b+b)的子串
     * 
     * 例如：
     * b = "abcde"，所有可能的旋转：
     * k=0: "abcde"
     * k=1: "bcdea" 
     * k=2: "cdeab"
     * k=3: "deabc"
     * k=4: "eabcd"
     * 
     * 这些旋转都会出现在"abcdeabcde"中
     * 
     * @param a 待检查的字符串
     * @param b 基准字符串
     * @return 如果a是b的旋转返回true，否则返回false
     */
    public static boolean isRotation(String a, String b) {
        // 前置条件检查
        if (a == null || b == null || a.length() != b.length()) {
            return false;  // 空串或长度不等都不可能是旋转关系
        }
        
        // 核心思想：a是b的旋转 ⟺ a是(b+b)的子串
        String b2 = b + b;
        return match(b2, a) != -1;
    }

    /**
     * 测试方法：验证旋转检查算法的正确性
     * 
     * 测试用例：
     * str1 = "aabbbbbb"
     * str2 = "bbbbbbaa"
     * 预期结果：true（str1是str2的旋转）
     * 
     * 验证过程：
     * str2 + str2 = "bbbbbbaabbbbbbaa"
     * 在其中确实可以找到"aabbbbbb"
     */
    public static void main(String[] args) {
        String str1 = "aabbbbbb";
        String str2 = "bbbbbbaa";
        
        System.out.println("字符串1: " + str1);
        System.out.println("字符串2: " + str2);
        
        boolean result = isRotation(str1, str2);
        System.out.println("是否为旋转关系: " + result);
        
        // 验证思路展示
        String doubled = str2 + str2;
        System.out.println("str2 + str2 = " + doubled);
        System.out.println("str1在其中的位置: " + match(doubled, str1));
    }
}
