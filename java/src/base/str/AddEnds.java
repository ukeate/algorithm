package base.str;

/**
 * 添加最短后缀变回文问题
 * 问题描述：给定一个字符串，在字符串末尾添加最少的字符使其变成回文串
 * 
 * 算法思路：
 * 1. 使用Manacher算法找到包含字符串末尾的最长回文半径
 * 2. 当回文边界r恰好覆盖到字符串末尾时，说明找到了包含末尾的最长回文
 * 3. 将未被回文覆盖的前缀部分逆序后添加到末尾即可
 * 
 * 核心思想：
 * - 利用Manacher算法的特性：当r == str.length时，表示回文边界触及字符串末尾
 * - 此时的回文半径rdEnd表示从末尾开始的最长回文长度
 * - 需要添加的字符数 = 原字符串长度 - 从末尾开始的最长回文长度
 * 
 * 时间复杂度：O(N) - Manacher算法的时间复杂度
 * 空间复杂度：O(N) - 存储预处理后的字符串和半径数组
 * 
 * 示例：
 * 输入："abcd123321"
 * 预处理："#a#b#c#d#1#2#3#3#2#1#"
 * 找到末尾回文："123321"，长度为6
 * 需要添加前缀："abcd"的逆序 = "dcba"
 * 结果："abcd123321dcba"
 * 
 * 验证："abcd123321dcba" 确实是回文串
 */
// 添加最短后缀变回文
public class AddEnds {

    /**
     * 将原始字符串转换为Manacher算法的预处理格式
     * 在每个字符之间以及首尾插入特殊字符'#'
     * 
     * 转换规律：
     * 原串：abc -> 预处理：#a#b#c#
     * 这样处理后所有回文都变成奇数长度，统一处理方式
     * 
     * @param str 原始字符串
     * @return 预处理后的字符数组
     */
    private static char[] manacherStr(String str) {
        char[] chars = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];  // 预处理后长度为2n+1
        int idx = 0;
        
        // 奇数位置放'#'，偶数位置放原字符
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chars[idx++];
        }
        return res;
    }

    /**
     * 找到使字符串变成回文串需要添加的最短后缀
     * 
     * 算法步骤：
     * 1. 将字符串进行Manacher预处理
     * 2. 使用Manacher算法计算每个位置的回文半径
     * 3. 当回文右边界r恰好到达字符串末尾时，记录此时的回文半径
     * 4. 根据回文半径计算需要添加的字符
     * 5. 将未被回文覆盖的前缀逆序作为后缀添加
     * 
     * 关键洞察：
     * - 当r == str.length时，说明当前回文可以延伸到字符串末尾
     * - 此时rdEnd就是从某个位置到末尾的最长回文半径
     * - 原字符串长度 - rdEnd + 1 = 需要补充的字符数
     * 
     * @param s 输入字符串
     * @return 添加最短后缀后的回文字符串
     */
    public static String addEnds(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        
        char[] str = manacherStr(s);        // 预处理字符串
        int[] rd = new int[str.length];     // 回文半径数组
        int c = -1, r = -1;                 // 回文中心和右边界
        int rdEnd = -1;                     // 包含末尾的回文半径
        
        // Manacher算法主体
        for (int i = 0; i != str.length; i++) {
            // 根据回文性质初始化rd[i]
            rd[i] = i < r ? Math.min(rd[2 * c - i], r - i) : 1;
            
            // 尝试扩展回文
            while (i - rd[i] > -1 && i + rd[i] < str.length) {
                if (str[i - rd[i]] == str[i + rd[i]]) {
                    rd[i]++;
                } else {
                    break;
                }
            }
            
            // 更新回文中心和右边界
            if (i + rd[i] > r) {
                r = i + rd[i];
                c = i;
            }
            
            // 关键：当回文右边界恰好到达字符串末尾时
            if (r == str.length) {
                rdEnd = rd[i];  // 记录此时的回文半径
                break;          // 找到答案，提前结束
            }
        }
        
        // 构造需要添加的后缀
        // 需要添加的字符数 = s.length() - rdEnd + 1
        char[] res = new char[s.length() - rdEnd + 1];
        
        // 将未被回文覆盖的前缀逆序作为后缀
        for (int i = 0; i < res.length; i++) {
            res[res.length - 1 - i] = str[i * 2 + 1];  // 从预处理字符串中提取原字符
        }
        
        return String.valueOf(res);
    }

    /**
     * 测试方法：验证算法正确性
     * 
     * 测试用例："abcd123321"
     * 期望结果：添加"dcba"后变成"abcd123321dcba"
     * 验证：这确实是一个回文串
     */
    public static void main(String[] args) {
        String str = "abcd123321";
        System.out.println("原字符串: " + str);
        
        String result = addEnds(str);
        System.out.println("需要添加的后缀: " + result);
        
        String palindrome = str + result;
        System.out.println("最终回文串: " + palindrome);
        
        // 验证是否为回文
        String reversed = new StringBuilder(palindrome).reverse().toString();
        System.out.println("是否为回文: " + palindrome.equals(reversed));
    }
}
