package basic.c31;

/**
 * 括号匹配问题
 * 
 * 问题描述：
 * 1. 判断给定的括号字符串是否有效
 * 2. 计算需要添加多少个括号才能使字符串有效
 * 
 * 解题思路：
 * 1. 有效性检查：使用计数器，遇到'('加1，遇到')'减1，过程中不能为负，最终必须为0
 * 2. 补全计算：分别计算需要补充的左括号和右括号数量
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(1)，只使用常数额外空间
 */
public class NeedParentheses {
    
    /**
     * 判断括号字符串是否有效
     * @param s 只包含'('和')'的字符串
     * @return true表示括号匹配有效，false表示无效
     */
    public static boolean valid(String s) {
        char[] str = s.toCharArray();
        int count = 0; // 记录当前未匹配的左括号数量
        
        for (int i = 0; i < str.length; i++) {
            // 遇到左括号计数+1，遇到右括号计数-1
            count += str[i] == '(' ? 1 : -1;
            
            // 如果计数为负，说明右括号多于左括号，无效
            if (count < 0) {
                return false;
            }
        }
        
        // 最终计数必须为0才表示完全匹配
        return count == 0;
    }

    /**
     * 计算需要添加多少个括号才能使字符串有效
     * @param s 只包含'('和')'的字符串
     * @return 需要添加的括号总数
     */
    public static int need(String s) {
        char[] str = s.toCharArray();
        int count = 0; // 记录当前未匹配的左括号数量
        int need = 0;  // 记录需要补充的左括号数量（对应多余的右括号）
        
        for (int i = 0; i < str.length; i++) {
            if (str[i] == '(') {
                count++; // 遇到左括号，未匹配左括号数+1
            } else {
                // 遇到右括号
                if (count == 0) {
                    // 没有左括号可匹配，需要补充一个左括号
                    need++;
                } else {
                    // 有左括号可匹配，未匹配左括号数-1
                    count--;
                }
            }
        }
        
        // 总需要数量 = 需要补充的左括号数 + 剩余未匹配的左括号数（需要补充对应的右括号）
        return count + need;
    }

    public static void main(String[] args) {
        String s = "(()))";
        System.out.println(valid(s));
        System.out.println(need(s));
    }
}