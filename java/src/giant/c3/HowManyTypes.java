package giant.c3;

import java.util.HashSet;

/**
 * 字符串分类统计问题
 * 
 * 问题描述：
 * 只由小写字母（a~z）组成的一批字符串，都放在字符类型的数组String[] arr中，
 * 如果其中某两个字符串所含有的字符种类完全一样，就将两个字符串算作一类。
 * 
 * 例如：
 * "baacba" 和 "bac" 算作一类，虽然长度不一样，但是所含字符的种类完全一样（a、b、c）
 * 
 * 要求：返回arr中有多少类？
 * 
 * 算法思路：
 * 使用位运算将每个字符串转换为一个整数，该整数的每一位代表对应字母是否出现。
 * 例如：字符串"abc"可以表示为二进制111（第0位代表a，第1位代表b，第2位代表c）
 * 
 * 核心技巧：
 * 1. 使用int类型的32位来表示26个小写字母的出现情况
 * 2. 对于字符ch，使用 (1 << (ch - 'a')) 得到对应的位标记
 * 3. 使用按位或运算(|=)来累积所有出现的字符
 * 4. 使用HashSet去重，最终的Set大小就是分类数量
 * 
 * 时间复杂度：O(n*m)，其中n是字符串数量，m是字符串平均长度
 * 空间复杂度：O(n)，最坏情况下每个字符串都是一个独立的类
 * 
 * @author algorithm learning
 */
public class HowManyTypes {
    
    /**
     * 统计字符串数组中不同字符集合的数量
     * 
     * 算法步骤：
     * 1. 遍历每个字符串，将其转换为位掩码表示
     * 2. 位掩码的每一位代表一个字母是否在字符串中出现
     * 3. 使用HashSet收集所有不同的位掩码
     * 4. HashSet的大小就是分类的数量
     * 
     * 位运算详解：
     * - ch - 'a' 将字符转换为0-25的索引
     * - 1 << (ch - 'a') 将索引转换为对应位置的位标记
     * - key |= ... 使用按位或累积所有字符的位标记
     * 
     * @param arr 字符串数组
     * @return 不同字符集合的数量
     */
    public static int types(String[] arr) {
        HashSet<Integer> types = new HashSet<>();  // 存储不同的字符集合标识
        
        // 遍历每个字符串
        for (String str : arr) {
            char[] chs = str.toCharArray();
            int key = 0;  // 位掩码，表示当前字符串包含的字符集合
            
            // 遍历字符串中的每个字符
            for (int i = 0; i < chs.length; i++) {
                // 将字符转换为对应的位标记并累积到key中
                // 例如：'a' -> 位置0, 'b' -> 位置1, 'c' -> 位置2
                key |= (1 << (chs[i] - 'a'));
            }
            
            // 将该字符串的字符集合标识加入HashSet
            types.add(key);
        }
        
        return types.size();  // 返回不同集合的数量
    }
    
    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串分类统计测试 ===");
        
        // 测试用例1：包含重复字符集合的情况
        String[] arr1 = {"baacba", "bac", "abc", "cba", "defg", "fed"};
        System.out.println("测试用例1: " + java.util.Arrays.toString(arr1));
        System.out.println("分类数量: " + types(arr1));
        System.out.println("解释:");
        System.out.println("  baacba, bac, abc, cba -> 都包含{a,b,c} -> 1类");
        System.out.println("  defg -> 包含{d,e,f,g} -> 1类");
        System.out.println("  fed -> 包含{d,e,f} -> 1类");
        System.out.println("  总共3类");
        System.out.println();
        
        // 测试用例2：每个字符串都有不同的字符集合
        String[] arr2 = {"a", "ab", "abc", "abcd"};
        System.out.println("测试用例2: " + java.util.Arrays.toString(arr2));
        System.out.println("分类数量: " + types(arr2));
        System.out.println("解释: 每个字符串的字符集合都不同，所以有4类");
        System.out.println();
        
        // 测试用例3：所有字符串都有相同的字符集合
        String[] arr3 = {"abc", "bac", "cab", "aabbcc"};
        System.out.println("测试用例3: " + java.util.Arrays.toString(arr3));
        System.out.println("分类数量: " + types(arr3));
        System.out.println("解释: 所有字符串都包含{a,b,c}，所以只有1类");
        System.out.println();
        
        // 演示位运算过程
        System.out.println("=== 位运算过程演示 ===");
        String demo = "abc";
        int key = 0;
        System.out.println("字符串: " + demo);
        for (char ch : demo.toCharArray()) {
            int bit = 1 << (ch - 'a');
            key |= bit;
            System.out.printf("字符'%c' -> 位置%d -> 位标记%d(二进制:%s) -> 累积结果:%d(二进制:%s)%n", 
                    ch, ch - 'a', bit, Integer.toBinaryString(bit), key, Integer.toBinaryString(key));
        }
        
        System.out.println("\n=== 测试完成 ===");
        System.out.println("核心优势:");
        System.out.println("1. 使用位运算，时间效率高");
        System.out.println("2. 空间占用少，每个字符串只用一个int表示");
        System.out.println("3. 天然去重，相同字符集合产生相同的位掩码");
    }
}
