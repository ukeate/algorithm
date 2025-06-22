package base.xor;

/**
 * 使用异或运算交换两个数
 * 不需要额外的临时变量，但要求两个数的内存地址不同
 * 原理：利用异或运算的性质 a ^ b ^ b = a
 */
public class Swap {
    /**
     * 使用异或运算交换数组中两个位置的元素
     * 
     * 算法原理：
     * 设 a = arr[i], b = arr[j]
     * 1. arr[i] = a ^ b
     * 2. arr[j] = (a ^ b) ^ b = a ^ (b ^ b) = a ^ 0 = a
     * 3. arr[i] = (a ^ b) ^ a = (a ^ a) ^ b = 0 ^ b = b
     * 
     * 注意事项：
     * - 必须保证 i != j，否则会将该位置的值变为0
     * - 因为如果 i == j，相当于 arr[i] = arr[i] ^ arr[i] = 0
     * 
     * @param arr 数组
     * @param i 第一个位置的索引
     * @param j 第二个位置的索引
     */
    public static void swap(int[] arr, int i, int j) {
        // 前提条件：i != j，否则会出错
        arr[i] = arr[i] ^ arr[j];  // arr[i] = a ^ b
        arr[j] = arr[i] ^ arr[j];  // arr[j] = (a ^ b) ^ b = a
        arr[i] = arr[i] ^ arr[j];  // arr[i] = (a ^ b) ^ a = b
    }
}
