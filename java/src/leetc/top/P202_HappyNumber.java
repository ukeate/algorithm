package leetc.top;

import java.util.HashSet;

/**
 * LeetCode 202. 快乐数 (Happy Number)
 * 
 * 问题描述：
 * 编写一个算法来判断一个数 n 是不是快乐数。
 * 
 * 快乐数定义为：
 * - 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和
 * - 然后重复这个过程直到这个数变为 1（此时这个数就是快乐数）
 * - 或者是无限循环但始终变不到 1（不是快乐数）
 * 
 * 示例：
 * 输入：n = 19
 * 输出：true
 * 解释：
 * 1² + 9² = 82
 * 8² + 2² = 68  
 * 6² + 8² = 100
 * 1² + 0² + 0² = 1
 * 
 * 解法思路：
 * 提供两种解法：
 * 1. 哈希集合法：使用HashSet检测循环
 * 2. 数学规律法：利用快乐数的数学性质
 * 
 * 核心观察：
 * - 如果一个数不是快乐数，它会进入循环
 * - 通过大量计算发现，所有不快乐数最终都会进入包含4的循环
 * - 因此可以简化判断：只要遇到4就说明不是快乐数
 * 
 * LeetCode链接：https://leetcode.com/problems/happy-number/
 */
public class P202_HappyNumber {
    
    /**
     * 方法1：哈希集合法判断快乐数
     * 
     * 算法思路：
     * 1. 使用HashSet记录已经出现过的数字
     * 2. 不断计算各位数字的平方和
     * 3. 如果平方和为1，返回true
     * 4. 如果平方和已经出现过，说明进入循环，返回false
     * 5. 否则继续计算下一轮
     * 
     * 优点：通用性强，逻辑清晰
     * 缺点：需要额外空间存储已访问的数字
     * 
     * @param n 待判断的正整数
     * @return 是否为快乐数
     */
    public static boolean isHappy1(int n) {
        HashSet<Integer> set = new HashSet<>();
        
        // 持续计算直到找到结果
        while (n != 1) {
            int sum = 0;
            
            // 计算当前数字各位数字的平方和
            while (n != 0) {
                int r = n % 10;    // 取个位数字
                sum += r * r;      // 累加平方
                n /= 10;           // 去掉个位数字
            }
            
            n = sum;  // 更新为平方和
            
            // 检查是否进入循环
            if (set.contains(n)) {
                break;  // 发现循环，不是快乐数
            }
            
            set.add(n);  // 记录当前数字
        }
        
        return n == 1;  // 如果最终为1则是快乐数
    }

    /**
     * 方法2：数学规律法判断快乐数
     * 
     * 算法思路：
     * 1. 利用数学规律：所有不快乐数最终都会进入包含4的循环
     * 2. 因此只需要检查：数字是否变为1（快乐数）或4（不快乐数）
     * 3. 大大简化了判断逻辑，不需要记录所有访问过的数字
     * 
     * 数学证明（简化）：
     * - 通过计算可以证明，所有不快乐数都会进入以下循环：
     *   4 → 16 → 37 → 58 → 89 → 145 → 42 → 20 → 4
     * - 因此只要遇到4，就可以确定不是快乐数
     * 
     * 优点：空间复杂度O(1)，基于数学规律，高效简洁
     * 缺点：依赖特定的数学性质，通用性稍差
     * 
     * @param n 待判断的正整数
     * @return 是否为快乐数
     */
    public static boolean isHappy2(int n) {
        // 持续计算直到遇到1（快乐数）或4（不快乐数）
        while (n != 1 && n != 4) {
            int sum = 0;
            
            // 计算当前数字各位数字的平方和
            while (n != 0) {
                int r = n % 10;    // 取个位数字
                sum += r * r;      // 累加平方
                n /= 10;           // 去掉个位数字
            }
            
            n = sum;  // 更新为平方和
        }
        
        // 如果结果为1则是快乐数，为4则不是快乐数
        return n == 1;
    }

    /**
     * 测试方法：验证两种算法的一致性
     * 
     * 这个方法可以用来验证数学规律的正确性
     * 通过大量测试确认两种方法的结果是否一致
     */
    public static void main(String[] args) {
        System.out.println("测试前1000个数字的快乐数判断：");
        int happyCount = 0;
        
        for (int i = 1; i < 1000; i++) {
            boolean result1 = isHappy1(i);
            boolean result2 = isHappy2(i);
            
            // 验证两种方法结果是否一致
            if (result1 != result2) {
                System.out.println("方法不一致：" + i);
            }
            
            if (result1) {
                happyCount++;
                if (happyCount <= 20) {  // 只打印前20个快乐数
                    System.out.println(i + " 是快乐数");
                }
            }
        }
        
        System.out.println("前1000个数字中共有 " + happyCount + " 个快乐数");
    }
}
