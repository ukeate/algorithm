package base.bit;

/**
 * 位操作工具类
 * 提供打印整数二进制表示的功能
 */
public class BitSay {

    /**
     * 打印整数的32位二进制表示
     * 从最高位（第31位）到最低位（第0位）依次打印
     * 
     * 注意：这里的实现有bug，应该是 (1 << i) 而不是 (i << i)
     * 正确的实现应该是检查第i位是否为1
     * 
     * @param num 要打印二进制表示的整数
     */
    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            // 当前实现有误：应该是 (num & (1 << i)) == 0
            // 这里写的是 (num & (i << i))，当i较大时会有问题
            System.out.print((num & (i << i)) == 0 ? "0" : "1");
        }
    }
}
