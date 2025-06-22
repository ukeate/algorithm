package base.str;

/**
 * Manacher算法 - 最长回文子串
 * 问题描述：在给定字符串中找到最长的回文子串的长度
 * 
 * 算法核心思想：
 * 1. 预处理字符串，在每个字符间插入特殊字符（如'#'），统一处理奇偶长度回文
 * 2. 维护回文中心c和回文右边界r
 * 3. 利用回文的对称性质，避免重复计算
 * 4. 对于每个位置i，利用已知信息快速初始化回文半径
 * 
 * 关键优化：
 * - 当i在回文范围内时，可以利用对称位置的信息
 * - 回文右边界r只会向右扩展，保证线性时间复杂度
 * - 每个字符最多被访问常数次
 * 
 * 时间复杂度：O(N) - 线性时间复杂度
 * 空间复杂度：O(N) - 预处理字符串和半径数组
 * 
 * 算法优势：
 * - 相比暴力解法O(N³)，显著提升效率
 * - 相比中心扩展O(N²)，避免重复计算
 * - 一次遍历即可得到所有位置的回文信息
 * 
 * 应用场景：
 * - 最长回文子串查找
 * - 回文字符串相关的字符串处理
 * - DNA序列分析中的对称结构检测
 */
public class Manacher {
    
    /**
     * 字符串预处理：在每个字符间插入特殊字符
     * 将任意字符串转换为便于处理回文的格式
     * 
     * 预处理的作用：
     * 1. 统一奇偶长度回文的处理方式
     * 2. 原串中奇数长度回文 -> 预处理后奇数长度回文
     * 3. 原串中偶数长度回文 -> 预处理后奇数长度回文
     * 
     * 转换规则：
     * 原串："abc" -> 预处理："#a#b#c#"
     * 原串："abba" -> 预处理："#a#b#b#a#"
     * 
     * 长度关系：
     * 如果原串长度为n，预处理后长度为2n+1
     * 
     * @param str 原始字符串
     * @return 预处理后的字符数组
     */
    private static char[] manacherStr(String str) {
        char[] chars = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];  // 预处理后长度为2n+1
        int idx = 0;
        
        // 在奇数位置放'#'，偶数位置放原字符
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chars[idx++];
        }
        return res;
    }

    /**
     * Manacher算法主体：找到最长回文子串的长度
     * 
     * 算法步骤：
     * 1. 预处理字符串
     * 2. 初始化回文中心c、右边界r、半径数组rd
     * 3. 对每个位置i：
     *    a) 利用对称性质初始化rd[i]
     *    b) 尝试扩展回文半径
     *    c) 更新回文中心和右边界
     * 4. 返回最大的回文半径-1（对应原串的回文长度）
     * 
     * 核心优化思想：
     * - 当i < r时，i关于c的对称点是2*c-i
     * - rd[i]至少为min(rd[2*c-i], r-i)
     * - 这避免了从1开始的重复扩展
     * 
     * 变量含义：
     * - c: 当前最优回文中心
     * - r: 当前最优回文右边界
     * - rd[i]: 以位置i为中心的回文半径
     * 
     * @param s 输入字符串
     * @return 最长回文子串的长度
     */
    public static int manacher(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = manacherStr(s);        // 预处理字符串
        int[] rd = new int[str.length];     // 回文半径数组
        int c = -1, r = -1;                 // 回文中心和右边界
        int max = Integer.MIN_VALUE;        // 最大回文半径
        
        for (int i = 0; i < str.length; i++) {
            // 利用对称性质优化初始化
            // 当i在回文范围内时，可以利用对称点的信息
            rd[i] = i < r ? Math.min(rd[2 * c - i], r - i) : 1;
            
            // 尝试扩展回文半径
            while (i - rd[i] > -1 && i + rd[i] < str.length) {
                if (str[i - rd[i]] == str[i + rd[i]]) {
                    rd[i]++;  // 回文半径扩展
                } else {
                    break;    // 无法继续扩展
                }
            }
            
            // 如果当前回文扩展超过了之前的右边界，更新中心和边界
            if (i + rd[i] > r) {
                r = i + rd[i];  // 更新右边界
                c = i;          // 更新回文中心
            }
            
            // 记录最大回文半径
            max = Math.max(max, rd[i]);
        }
        
        // 预处理后的回文半径-1就是原串的回文长度
        return max - 1;
    }

    /**
     * 暴力解法：逐个检查每个位置的回文半径
     * 用于验证Manacher算法的正确性
     * 
     * 算法思路：
     * 1. 对预处理后的字符串，以每个位置为中心
     * 2. 向两边扩展，直到字符不匹配
     * 3. 记录最大的回文半径
     * 
     * 时间复杂度：O(N²) - 每个位置最多扩展N次
     * 空间复杂度：O(N) - 预处理字符串的空间
     * 
     * @param s 输入字符串
     * @return 最长回文子串的长度
     */
    public static int maxSure(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = manacherStr(s);  // 同样需要预处理
        int max = 0;
        
        // 以每个位置为中心进行扩展
        for (int i = 0; i < str.length; i++) {
            int l = i - 1;  // 左指针
            int r = i + 1;  // 右指针
            
            // 向两边扩展，直到字符不匹配或越界
            while (l >= 0 && r < str.length && str[l] == str[r]) {
                l--;
                r++;
            }
            
            // 计算当前位置的回文长度
            max = Math.max(max, r - l - 1);
        }
        
        // 转换为原串的回文长度
        return max / 2;
    }

    /**
     * 生成指定字符种类和长度的随机字符串
     * 用于大规模测试算法正确性
     * 
     * @param maxKind 字符种类数（从'a'开始）
     * @param maxLen 字符串最大长度
     * @return 随机生成的字符串
     */
    private static String randomStr(int maxKind, int maxLen) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((maxKind + 1) * Math.random() + 'a');
        }
        return String.valueOf(ans);
    }

    /**
     * 大规模测试Manacher算法的正确性
     * 通过与暴力解法对比验证算法实现
     * 
     * 测试策略：
     * 1. 生成大量随机字符串
     * 2. 分别用Manacher算法和暴力解法求解
     * 3. 比较两种方法的结果是否一致
     * 4. 统计测试进度和结果
     * 
     * 性能对比：
     * - Manacher算法：O(N)时间复杂度
     * - 暴力解法：O(N²)时间复杂度
     * - 在大规模数据上Manacher算法优势明显
     * 
     * 测试参数说明：
     * - times: 测试次数，确保算法稳定性
     * - maxKind: 字符种类，影响回文出现概率
     * - maxLen: 字符串长度，测试不同规模数据
     */
    public static void main(String[] args) {
        int times = 1000000;      // 100万次测试
        int maxKind = 5;          // 5种字符（a-e）
        int maxLen = 20;          // 字符串最大长度20
        
        System.out.println("Manacher算法正确性测试开始");
        System.out.println("测试参数：次数=" + times + ", 字符种类=" + maxKind + 
                         ", 最大长度=" + maxLen);
        
        for (int i = 0; i < times; i++) {
            // 生成随机测试字符串
            String str = randomStr(maxKind, maxLen);
            
            // 分别用两种算法求解
            int manacherResult = manacher(str);    // Manacher算法
            int bruteResult = maxSure(str);        // 暴力解法
            
            // 验证结果一致性
            if (manacherResult != bruteResult) {
                System.out.println("发现错误！");
                System.out.println("测试字符串: \"" + str + "\"");
                System.out.println("Manacher结果: " + manacherResult);
                System.out.println("暴力解法结果: " + bruteResult);
                return;
            }
            
            // 每10万次测试输出进度
            if ((i + 1) % 100000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("测试完成！所有 " + times + " 次测试都通过");
        System.out.println("Manacher算法实现正确");
        
        // 性能测试示例
        System.out.println("\n=== 性能对比测试 ===");
        String longStr = randomStr(maxKind, 10000);  // 生成长字符串
        
        long start = System.currentTimeMillis();
        int result1 = manacher(longStr);
        long end = System.currentTimeMillis();
        System.out.println("Manacher算法 (长度10000): " + result1 + 
                         ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result2 = maxSure(longStr);
        end = System.currentTimeMillis();
        System.out.println("暴力解法 (长度10000): " + result2 + 
                         ", 耗时: " + (end - start) + "ms");
    }
}
