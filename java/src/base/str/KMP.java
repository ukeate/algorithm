package base.str;

/**
 * KMP字符串匹配算法
 * 问题描述：在文本串中高效查找模式串的出现位置
 * 
 * 算法核心思想：
 * 1. 预处理模式串，构建next数组（部分匹配表）
 * 2. 利用已匹配的信息，避免不必要的回退
 * 3. 当匹配失败时，根据next数组智能地调整匹配位置
 * 
 * 关键优势：
 * - 文本串指针从不回退，只会向前移动
 * - 避免了暴力匹配中的重复比较
 * - 时间复杂度O(N+M)，空间复杂度O(M)
 * 
 * next数组含义：
 * next[i] = 模式串[0...i-1]的最长相等前后缀长度
 * 当位置i匹配失败时，模式串应该跳转到next[i]位置继续匹配
 * 
 * 时间复杂度：O(N + M) - N为文本串长度，M为模式串长度
 * 空间复杂度：O(M) - next数组的空间
 * 
 * 应用场景：
 * - 文本搜索、编辑器查找功能
 * - 字符串处理、DNA序列匹配
 * - 网络数据包过滤、日志分析
 */
public class KMP {
    
    /**
     * 构建KMP算法的next数组（前缀函数）
     * next[i]表示模式串前i个字符的最长相等前后缀长度
     * 
     * 算法原理：
     * 1. next[0] = -1（特殊标记，表示无处可跳）
     * 2. next[1] = 0（单字符无真前后缀）
     * 3. 对于i >= 2，利用已求出的next值递推
     * 
     * 核心递推思想：
     * - 如果str2[i-1] == str2[cn]，则next[i] = cn + 1
     * - 否则，cn跳转到next[cn]，继续尝试匹配
     * - 若cn为0还不匹配，则next[i] = 0
     * 
     * 示例：
     * 模式串："ababa"
     * next数组：[-1, 0, 0, 1, 2]
     * 解释：
     * - next[0] = -1（边界）
     * - next[1] = 0（"a"无真前后缀）
     * - next[2] = 0（"ab"最长相等前后缀长度为0）
     * - next[3] = 1（"aba"最长相等前后缀为"a"，长度1）
     * - next[4] = 2（"abab"最长相等前后缀为"ab"，长度2）
     * 
     * @param str2 模式串的字符数组
     * @return next数组，用于失配时的跳转
     */
    private static int[] nextArr(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1};  // 单字符特殊处理
        }
        
        int[] next = new int[str2.length];
        next[0] = -1;  // 边界值，表示无处可跳
        next[1] = 0;   // 单字符串无真前后缀
        int i = 2;     // 从第2位开始计算
        int cn = 0;    // 当前正在比较的位置
        
        while (i < next.length) {
            if (str2[i - 1] == str2[cn]) {
                // 匹配成功，最长前后缀长度增加
                next[i++] = ++cn;
            } else if (cn > 0) {
                // 匹配失败，cn跳转到next[cn]
                // 这是KMP的核心：利用已知信息避免重复比较
                cn = next[cn];
            } else {
                // cn已经为0，无法再跳转
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * KMP字符串匹配主算法
     * 在文本串s1中查找模式串s2的第一次出现位置
     * 
     * 算法流程：
     * 1. 预处理：构建模式串的next数组
     * 2. 匹配过程：使用双指针x（文本串）和y（模式串）
     * 3. 匹配成功：两指针同时前进
     * 4. 匹配失败：根据next数组调整模式串指针
     * 
     * 关键洞察：
     * - 文本串指针x永远不回退，保证线性时间复杂度
     * - 模式串指针y根据next数组智能跳转
     * - next[y] == -1时表示模式串第一个字符都不匹配
     * 
     * 匹配过程示例：
     * 文本串："ababcababa"
     * 模式串："ababa"
     * next:   [-1,0,0,1,2]
     * 
     * 匹配过程：
     * 1. x=0,y=0: 'a'=='a' ✓ -> x=1,y=1
     * 2. x=1,y=1: 'b'=='b' ✓ -> x=2,y=2  
     * 3. x=2,y=2: 'a'=='a' ✓ -> x=3,y=3
     * 4. x=3,y=3: 'b'=='b' ✓ -> x=4,y=4
     * 5. x=4,y=4: 'c'!='a' ✗ -> y=next[4]=2
     * 6. x=4,y=2: 'c'!='a' ✗ -> y=next[2]=0
     * 7. x=4,y=0: 'c'!='a' ✗ -> y=next[0]=-1, x=5
     * 继续匹配...最终在位置5找到匹配
     * 
     * @param s1 文本串（在其中搜索）
     * @param s2 模式串（要搜索的字符串）
     * @return 模式串在文本串中的起始位置，未找到返回-1
     */
    public static int match(String s1, String s2) {
        // 边界检查
        if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
            return -1;
        }
        
        char[] str1 = s1.toCharArray();     // 文本串字符数组
        char[] str2 = s2.toCharArray();     // 模式串字符数组
        int x = 0;                          // 文本串指针
        int y = 0;                          // 模式串指针
        int[] next = nextArr(str2);         // 构建next数组
        
        // 开始匹配过程
        while (x < str1.length && y < str2.length) {
            if (str1[x] == str2[y]) {
                // 字符匹配成功，双指针同时前进
                x++;
                y++;
            } else if (next[y] == -1) {
                // 模式串第一个字符就不匹配，文本串指针前进
                // 此时y会重置为0
                x++;
            } else {
                // 利用next数组进行智能跳转
                // 文本串指针不动，只调整模式串指针
                y = next[y];
            }
        }
        
        // 判断是否找到完整匹配
        return y == str2.length ? x - y : -1;
    }

    /**
     * 生成指定长度和字符种类的随机字符串
     * 用于大规模测试KMP算法的正确性
     * 
     * @param maxLen 最大字符串长度
     * @param charKind 字符种类数（从'a'开始）
     * @return 随机生成的字符串
     */
    private static String randomStr(int maxLen, int charKind) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((int) (charKind * Math.random()) + 'a');
        }
        return String.valueOf(ans);
    }

    /**
     * 大规模测试KMP算法的正确性
     * 通过与Java内置的indexOf方法对比验证结果
     * 
     * 测试策略：
     * 1. 生成大量随机的文本串和模式串
     * 2. 分别用KMP算法和Java内置方法查找
     * 3. 比较两种方法的结果是否一致
     * 4. 如有不一致则报告错误
     * 
     * 测试参数说明：
     * - times: 测试次数，验证算法稳定性
     * - charKind: 字符种类，影响字符串的复杂度
     * - strLen: 文本串长度，测试大规模数据性能
     * - matchLen: 模式串长度，测试不同长度模式串
     */
    public static void main(String[] args) {
        int times = 10000000;     // 1000万次测试，确保算法稳定性
        int charKind = 5;         // 5种字符（a-e），增加重复匹配概率
        int strLen = 20;          // 文本串最大长度
        int matchLen = 5;         // 模式串最大长度
        
        System.out.println("KMP算法正确性测试开始");
        System.out.println("测试参数：次数=" + times + ", 字符种类=" + charKind + 
                         ", 文本串长度=" + strLen + ", 模式串长度=" + matchLen);
        
        for (int i = 0; i < times; i++) {
            // 生成随机测试数据
            String str = randomStr(strLen, charKind);        // 随机文本串
            String match = randomStr(matchLen, charKind);    // 随机模式串
            
            // 分别用两种方法查找
            int kmpResult = match(str, match);               // KMP算法结果
            int javaResult = str.indexOf(match);             // Java内置方法结果
            
            // 验证结果一致性
            if (kmpResult != javaResult) {
                System.out.println("发现错误！");
                System.out.println("文本串: " + str);
                System.out.println("模式串: " + match);
                System.out.println("KMP结果: " + kmpResult);
                System.out.println("Java结果: " + javaResult);
                return;
            }
            
            // 每百万次测试输出进度
            if ((i + 1) % 1000000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("测试完成！所有 " + times + " 次测试都通过");
        System.out.println("KMP算法实现正确");
    }
}
