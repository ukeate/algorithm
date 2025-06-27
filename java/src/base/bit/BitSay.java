package base.bit;

/**
 * 位操作工具类
 * 提供打印整数二进制表示的功能
 * 
 * 核心功能：将32位整数的二进制表示以字符串形式输出
 * 用于调试和理解位运算操作
 */
public class BitSay {

    /**
     * 打印整数的32位二进制表示
     * 从最高位（第31位）到最低位（第0位）依次打印
     * 
     * 算法原理：
     * 1. 从第31位开始遍历到第0位
     * 2. 对于每一位i，创建掩码 (1 << i) 
     * 3. 使用 & 操作检查该位是否为1
     * 4. 如果结果为0则打印"0"，否则打印"1"
     * 
     * 示例：
     * - print(5) 会输出：00000000000000000000000000000101
     * - print(-1) 会输出：11111111111111111111111111111111
     * 
     * @param num 要打印二进制表示的整数
     */
    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            // 使用位掩码检查第i位是否为1
            // (1 << i) 创建只有第i位为1的掩码
            // (num & (1 << i)) 检查num的第i位
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println(); // 换行便于阅读
    }

    /**
     * 测试方法：演示二进制打印功能
     */
    public static void main(String[] args) {
        System.out.println("测试二进制打印功能：");
        
        System.out.print("0的二进制表示：");
        print(0);
        
        System.out.print("1的二进制表示：");
        print(1);
        
        System.out.print("5的二进制表示：");
        print(5);
        
        System.out.print("-1的二进制表示：");
        print(-1);
        
        System.out.print("Integer.MAX_VALUE的二进制表示：");
        print(Integer.MAX_VALUE);
        
        System.out.print("Integer.MIN_VALUE的二进制表示：");
        print(Integer.MIN_VALUE);
    }
}
