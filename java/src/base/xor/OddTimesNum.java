package base.xor;

/**
 * 找出数组中出现奇数次的数字
 * 利用异或运算的性质：相同的数异或为0，任何数与0异或为自身
 * 时间复杂度：O(n)，空间复杂度：O(1)
 */
public class OddTimesNum {
    
    /**
     * 找出数组中唯一出现奇数次的数字
     * 问题前提：数组中只有一种数出现奇数次，其他数都出现偶数次
     * 
     * 算法原理：
     * 1. 异或运算满足交换律和结合律
     * 2. 任何数与自己异或结果为0：a ^ a = 0
     * 3. 任何数与0异或结果为自己：a ^ 0 = a
     * 4. 出现偶数次的数字异或后为0，只剩下出现奇数次的数字
     * 
     * @param arr 输入数组
     * @return 出现奇数次的数字
     */
    public static int oddTimesNum(int[] arr) {
        int eor = 0;  // 异或结果，初始为0
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];  // 将所有数字进行异或运算
        }
        return eor;  // 最终结果就是出现奇数次的数字
    }

    /**
     * 找出数组中两个出现奇数次的数字
     * 问题前提：数组中有且仅有两种数出现奇数次，其他数都出现偶数次
     * 
     * 算法原理：
     * 1. 先对所有数字异或，得到两个目标数字的异或结果eor = a ^ b
     * 2. eor中为1的位表示a和b在该位上不同
     * 3. 提取eor最右边的1，用它将数组分为两组
     * 4. 在该位上为1的数字分为一组，为0的数字分为另一组
     * 5. 两个目标数字必然分在不同组中
     * 6. 对其中一组进行异或运算，得到其中一个目标数字
     * 7. 用eor异或这个数字，得到另一个目标数字
     * 
     * @param arr 输入数组
     * @return 包含两个出现奇数次数字的数组
     */
    public static int[] oddTimesTwoNum(int[] arr) {
        int eor = 0;
        // 第一步：对所有数字异或，得到两个目标数字的异或结果
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        
        // 第二步：提取eor最右边的1
        // eor & (-eor) 可以提取最右边的1
        // 这个1表示两个目标数字在这一位上不同
        int rightOne = eor & (-eor);

        int num = 0;  // 用于存储其中一个目标数字
        // 第三步：根据rightOne位将数组分组，对其中一组进行异或
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) != 0) {
                // 在rightOne位上为1的数字分为一组
                num ^= arr[i];
            }
            // 在rightOne位上为0的数字自动分为另一组
        }
        
        // 第四步：计算另一个目标数字
        // 因为eor = a ^ b，而num = a，所以eor ^ num = b
        return new int[]{num, eor ^ num};
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 可以在这里添加测试代码
    }
}
