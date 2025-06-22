package base.direct;

/**
 * 最少袋子装苹果问题
 * 
 * 问题：只有6公斤和8公斤的袋子，要装apple公斤苹果，求最少需要多少个袋子
 * 
 * 解题思路：
 * 1. 暴力解法：枚举8公斤袋子的数量，检查剩余是否能被6整除
 * 2. 规律解法：通过找规律得出直接公式
 * 
 * 数学分析：
 * - 6和8的最大公约数是2，所以奇数苹果无解
 * - 对于偶数，当apple>=18时，都有解，且可以用公式直接计算
 */
// 只用6号8号袋，要装满，求最小袋子数
public class MinBags {
    /**
     * 暴力解法：枚举8公斤袋子数量
     * 时间复杂度：O(apple/8)
     * 
     * 思路：从最多的8公斤袋子开始尝试，如果剩余重量能被6整除，则找到解
     * 
     * @param apple 苹果总重量
     * @return 最少袋子数，如果无解返回-1
     */
    public static int min(int apple) {
        if (apple < 0) {
            return -1;
        }
        
        int bag8 = (apple >> 3);    // 最多能用多少个8公斤袋子 (apple / 8)
        int rest = apple - (bag8 << 3);  // 剩余重量 (apple % 8)
        
        while (bag8 >= 0) {
            if (rest % 6 == 0) {
                // 剩余重量能被6整除，找到解
                return bag8 + (rest / 6);
            } else {
                // 减少一个8公斤袋子，增加8公斤到剩余重量
                bag8--;
                rest += 8;
            }
        }
        return -1;  // 无解
    }

    /**
     * 规律解法：基于找到的数学规律
     * 时间复杂度：O(1)
     * 
     * 规律分析：
     * - 奇数：无解
     * - apple < 18的偶数：特殊处理
     * - apple >= 18的偶数：(apple - 18) / 8 + 3
     * 
     * 规律解释：
     * - 当apple >= 18时，可以先用3个6公斤袋子(18公斤)
     * - 剩余部分每8公斤用一个8公斤袋子替换，总袋子数不变
     * - 所以公式为：(apple - 18) / 8 + 3
     * 
     * @param apple 苹果总重量
     * @return 最少袋子数，如果无解返回-1
     */
    public static int min2(int apple) {
        // 奇数无解（6和8都是偶数，无法组成奇数）
        if ((apple & 1) != 0) {
            return -1;
        }
        
        if (apple < 18) {
            // 小于18的特殊情况直接判断
            return apple == 0 ? 0 : 
                   (apple == 6 || apple == 8) ? 1 :
                   (apple == 12 || apple == 14 || apple == 16) ? 2 : -1;
        }
        
        // apple >= 18的情况使用公式
        // 先用3个6公斤袋子(18公斤)，剩余每8公斤用1个8公斤袋子
        return (apple - 18) / 8 + 3;
    }

    /**
     * 测试方法：打印结果观察规律
     */
    public static void main(String[] args) {
        // 打印前200个结果，观察规律
        for (int apple = 1; apple < 200; apple++) {
            System.out.println(apple + ":" + min(apple));
        }
    }
}
