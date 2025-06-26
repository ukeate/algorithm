package leetc.top;

/**
 * LeetCode 344. 反转字符串 (Reverse String)
 * 
 * 问题描述：
 * 编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 s 的形式给出。
 * 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。
 * 
 * 示例：
 * 输入：s = ["h","e","l","l","o"]
 * 输出：["o","l","l","e","h"]
 * 
 * 输入：s = ["H","a","n","n","a","h"]
 * 输出：["h","a","n","n","a","H"]
 * 
 * 解法思路：
 * 双指针 + 原地交换：
 * 
 * 1. 核心思想：
 *    - 使用两个指针分别指向数组的首尾
 *    - 交换两个指针所指向的字符
 *    - 向中间移动指针，直到相遇
 * 
 * 2. 算法步骤：
 *    - 初始化左指针指向数组开始，右指针指向数组结尾
 *    - 当左指针小于右指针时：
 *      * 交换左右指针指向的字符
 *      * 左指针右移，右指针左移
 *    - 重复直到两指针相遇或交错
 * 
 * 3. 边界处理：
 *    - 空数组或单字符数组：无需处理
 *    - 偶数长度：指针最终会交错
 *    - 奇数长度：指针最终指向中间元素
 * 
 * 核心思想：
 * - 双指针：从两端向中间逼近
 * - 原地交换：不使用额外空间
 * - 对称性：利用字符串反转的对称特性
 * 
 * 关键技巧：
 * - 循环条件：left < right，避免重复交换
 * - 交换操作：使用临时变量保证正确性
 * - 指针移动：每次交换后同时移动两个指针
 * 
 * 时间复杂度：O(n) - 需要遍历数组的一半
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/reverse-string/
 */
public class P344_ReverseString {
    
    /**
     * 反转字符数组（原地修改）
     * 
     * 使用双指针法，从数组两端向中间交换字符
     * 
     * 算法流程：
     * 1. 设置左指针指向数组开始，右指针指向数组结尾
     * 2. 当左指针小于右指针时，交换两个位置的字符
     * 3. 左指针右移，右指针左移
     * 4. 重复步骤2-3直到指针相遇
     * 
     * @param s 待反转的字符数组
     */
    public void reverseString(char[] s) {
        if (s == null || s.length <= 1) {
            return; // 边界情况：空数组或单字符数组无需处理
        }
        
        int left = 0;           // 左指针：指向数组开始
        int right = s.length - 1; // 右指针：指向数组结尾
        
        // 双指针向中间移动，交换字符
        while (left < right) {
            // 交换左右指针指向的字符
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            
            // 双指针向中间移动
            left++;
            right--;
        }
    }
    
    /**
     * 反转字符数组（使用异或交换优化版本）
     * 
     * 使用异或运算进行交换，可以避免使用临时变量
     * 但需要注意两个位置不能相同（异或自身为0）
     * 
     * @param s 待反转的字符数组
     */
    public void reverseStringXOR(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }
        
        int left = 0;
        int right = s.length - 1;
        
        while (left < right) {
            // 使用异或运算交换（当left != right时才有效）
            s[left] ^= s[right];
            s[right] ^= s[left];
            s[left] ^= s[right];
            
            left++;
            right--;
        }
    }
    
    /**
     * 反转字符数组（递归版本）
     * 
     * 使用递归的思想来解决问题，虽然会使用O(n)的栈空间
     * 
     * @param s 待反转的字符数组
     */
    public void reverseStringRecursive(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }
        reverseHelper(s, 0, s.length - 1);
    }
    
    /**
     * 递归辅助函数
     * 
     * @param s 字符数组
     * @param left 左边界
     * @param right 右边界
     */
    private void reverseHelper(char[] s, int left, int right) {
        // 递归终止条件：指针相遇或交错
        if (left >= right) {
            return;
        }
        
        // 交换当前位置的字符
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;
        
        // 递归处理内部子数组
        reverseHelper(s, left + 1, right - 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P344_ReverseString solution = new P344_ReverseString();
        
        // 测试用例1
        char[] s1 = {'h', 'e', 'l', 'l', 'o'};
        System.out.println("原数组: " + java.util.Arrays.toString(s1));
        solution.reverseString(s1);
        System.out.println("反转后: " + java.util.Arrays.toString(s1));
        
        // 测试用例2
        char[] s2 = {'H', 'a', 'n', 'n', 'a', 'h'};
        System.out.println("原数组: " + java.util.Arrays.toString(s2));
        solution.reverseString(s2);
        System.out.println("反转后: " + java.util.Arrays.toString(s2));
    }
}
