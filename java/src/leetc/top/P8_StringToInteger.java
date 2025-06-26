package leetc.top;

/**
 * LeetCode 8. 字符串转换整数 (atoi) (String to Integer)
 * 
 * 问题描述：
 * 请你来实现一个 myAtoi(string s) 函数，使其能将字符串转换成一个 32 位有符号整数。
 * 
 * 函数 myAtoi(string s) 的算法如下：
 * 1. 读入字符串并丢弃无用的前导空格
 * 2. 检查下一个字符是否为正或者负号，读取该字符确定最终结果是负数还是正数
 * 3. 读入后续字符，直到到达下一个非数字字符或到达输入的结尾
 * 4. 将前面步骤读入的字符串转换为整数
 * 5. 如果整数数超过 32 位有符号整数范围，需要截断这个整数
 * 
 * 示例：
 * - 输入：s = "42"，输出：42
 * - 输入：s = "   -42"，输出：-42  
 * - 输入：s = "4193 with words"，输出：4193
 * 
 * 解法思路：
 * 字符串处理 + 溢出检测：
 * 1. 去除前导空格和前导零
 * 2. 验证字符串格式的合法性
 * 3. 使用负数进行计算避免溢出问题
 * 4. 边界检查：在每次累加前检查是否会溢出
 * 
 * 时间复杂度：O(n) - 需要遍历字符串
 * 空间复杂度：O(n) - 需要存储处理后的字符串
 */
public class P8_StringToInteger {
    
    /**
     * 去除字符串的前导零和无效后缀
     * 
     * 处理步骤：
     * 1. 保留符号位（如果有）
     * 2. 去除前导零
     * 3. 去除非数字的后缀
     * 
     * @param str 输入字符串
     * @return 处理后的字符串
     */
    private static String removeHeadZero(String str) {
        boolean r = (str.startsWith("+") || str.startsWith("-"));  // 是否有符号位
        int s = r ? 1 : 0;  // 开始扫描的位置
        
        // 跳过前导零
        for (; s < str.length(); s++) {
            if (str.charAt(s) != '0') {
                break;
            }
        }
        
        // 找到第一个非数字字符的位置
        int e = -1;
        for (int i = str.length() - 1; i >= (r ? 1 : 0); i--) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                e = i;
            }
        }
        
        // 构建结果：符号位 + 有效数字部分
        return (r ? String.valueOf(str.charAt(0)) : "") + 
               str.substring(s, e == -1 ? str.length() : e);
    }

    /**
     * 验证字符数组是否表示一个有效的整数
     * 
     * 验证规则：
     * 1. 第一个字符必须是数字或正负号
     * 2. 如果只有符号位，则无效
     * 3. 符号位后面的字符都必须是数字
     * 
     * @param chas 字符数组
     * @return 是否为有效整数格式
     */
    private static boolean isValid(char[] chas) {
        // 检查第一个字符
        if (chas[0] != '-' && chas[0] != '+' && (chas[0] < '0' || chas[0] > '9')) {
            return false;
        }
        
        // 如果只有符号位，无效
        if ((chas[0] == '-' || chas[0] == '+') && chas.length == 1) {
            return false;
        }
        
        // 检查符号位后的所有字符都是数字
        for (int i = 1; i < chas.length; i++) {
            if (chas[i] < '0' || chas[i] > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转整数的主算法
     * 
     * 算法思路：
     * 1. 预处理：去空格、去前导零、验证格式
     * 2. 使用负数计算避免正数溢出问题
     * 3. 在每次累加前检查溢出：
     *    - 如果 res < MIN_VALUE/10，下次乘10必定溢出
     *    - 如果 res == MIN_VALUE/10 且当前数字过大，也会溢出
     * 4. 根据原始符号返回正确结果
     * 
     * @param s 输入字符串
     * @return 转换后的整数，溢出时返回边界值
     */
    public int myAtoi(String s) {
        // 边界情况
        if (s == null || s.equals("")) {
            return 0;
        }
        
        // 去除前导空格并预处理
        s = removeHeadZero(s.trim());
        if (s == null || s.equals("")) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        
        // 验证格式
        if (!isValid(str)) {
            return 0;
        }
        
        // 判断符号（注意：这里neg表示最终结果是否为正数）
        boolean neg = str[0] == '-' ? false : true;
        
        // 溢出检查的边界值
        int minq = Integer.MIN_VALUE / 10;    // -214748364
        int minr = Integer.MIN_VALUE % 10;    // -8
        
        int res = 0, cur = 0;
        
        // 从数字开始的位置遍历
        for (int i = (str[0] == '-' || str[0] == '+') ? 1 : 0; i < str.length; i++) {
            cur = '0' - str[i];  // 转换为负数
            
            // 溢出检查
            if ((res < minq) || (res == minq && cur < minr)) {
                return neg ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            
            // 累加结果
            res = res * 10 + cur;
        }
        
        // 处理 -2^31 转正数的特殊情况
        if (neg && res == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        
        // 根据符号返回结果
        return neg ? -res : res;
    }
}
