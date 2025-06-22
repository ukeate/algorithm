package base.bit;

import java.util.HashSet;
import java.util.Set;

/**
 * 位图(BitMap)数据结构实现
 * 用于高效存储和查询大量整数的存在性
 * 
 * 核心思想：
 * 1. 使用long数组，每个long有64位
 * 2. 对于数字n，存储在 bits[n/64] 的第 (n%64) 位上
 * 3. 空间复杂度：O(max/64)，操作时间复杂度：O(1)
 */
public class Bitmap {

    // 位图存储数组，每个long包含64位
    private long[] bits;

    /**
     * 构造函数：根据最大值初始化位图
     * @param max 要存储的最大数值
     */
    public Bitmap(int max) {
        // 计算需要多少个long：(max >> 6) 相当于 max / 64
        // +1是为了处理边界情况
        this.bits = new long[((max >> 6) + 1)];
    }

    /**
     * 添加数字到位图中
     * @param num 要添加的数字
     */
    public void add(int num) {
        // num >> 6 计算应该存储在哪个long中（相当于num / 64）
        // num & 63 计算在该long中的第几位（相当于num % 64）
        // 1L << (num & 63) 创建对应位为1的掩码
        // |= 操作将对应位设置为1
        bits[num >> 6] |= (1L << (num & 63));
    }

    /**
     * 从位图中删除数字
     * @param num 要删除的数字
     */
    public void delete(int num) {
        // ~(1L << (num & 63)) 创建对应位为0、其他位为1的掩码
        // &= 操作将对应位设置为0
        bits[num >> 6] &= ~(1L << (num & 63));
    }

    /**
     * 查询数字是否存在于位图中
     * @param num 要查询的数字
     * @return 如果存在返回true，否则返回false
     */
    public boolean contains(int num) {
        // & 操作检查对应位是否为1
        return (bits[num >> 6] & (1L << (num & 63))) != 0;
    }

    /**
     * 测试方法：对比BitMap和HashSet的功能一致性
     */
    public static void main(String[] args) {
        System.out.println("test begin");
        int max = 10000;
        Bitmap bitmap = new Bitmap(max);
        Set<Integer> set = new HashSet<>();
        int testTimes = (int) 1e8;
        
        for (int i = 0; i < testTimes; i++) {
            int num = (int) (Math.random() * (max + 1));
            double decide = Math.random();
            
            if (decide < 0.333) {
                // 测试添加操作
                bitmap.add(num);
                set.add(num);
            } else if (decide < 0.666) {
                // 测试删除操作
                bitmap.delete(num);
                set.remove(num);
            } else {
                // 测试查询操作
                if (bitmap.contains(num) != set.contains(num)) {
                    System.out.println("Oops");
                    break;
                }
            }
        }
        
        // 最终验证所有数字的状态一致性
        for (int num = 0; num <= max; num++) {
            if (bitmap.contains(num) != set.contains(num)) {
                System.out.println("Oops");
            }
        }
        System.out.println("test end");
    }
}
