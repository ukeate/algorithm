package giant.c2;

/**
 * 自动售货机买可乐问题（携程毕业旅行题）
 * 
 * 问题背景：
 * 日本毕业旅行后，需要去自动售货机买可乐。售货机只支持硬币支付，
 * 且收退都只支持10、50、10三种面额。一次购买行为只能出一瓶可乐，
 * 且每次购买后总是找零最小枚数的硬币。
 * 
 * 输入参数：
 * - m: 需要购买的可乐数量
 * - a: 10元硬币的枚数
 * - b: 50元硬币的枚数  
 * - c: 100元硬币的枚数
 * - x: 一瓶可乐的价格（x是10的倍数）
 * 
 * 输出：总共需要投入硬币的次数（包括找零）
 * 
 * 示例：
 * 购买2瓶可乐，每瓶250元，手里有100元3枚，50元4枚，10元1枚。
 * 购买第1瓶：投递100元*3枚，找50元*1枚（操作4次）
 * 购买第2瓶：投遒50元*5枚（操作5次）
 * 总计：8次操作
 * 
 * 算法特点：
 * 1. 优先使用大面额硬币
 * 2. 找零时使用最小枚数的硬币组合
 * 3. 需要考虑多种面额组合的情况
 * 
 * 算法优化：
 * 相比暴力递归，优化版本通过数学分析减少了大量重复计算。
 */
public class Cola {
    /*
     * 买饮料 时间限制： 3000MS 内存限制： 589824KB 题目描述：
     * 游游今年就要毕业了，和同学们在携程上定制了日本毕业旅行。愉快的一天行程结束后大家回到了酒店房间，这时候同学们都很口渴，
     * 石头剪刀布选出游游去楼下的自动贩卖机给大家买可乐。 贩卖机只支持硬币支付，且收退都只支持10 ，50，100
     * 三种面额。一次购买行为只能出一瓶可乐，且每次购买后总是找零最小枚数的硬币。（例如投入100圆，可乐30圆，则找零50圆一枚，10圆两枚）
     * 游游需要购买的可乐数量是 m，其中手头拥有的 10,50,100 面额硬币的枚数分别是 a,b,c，可乐的价格是x(x是10的倍数)。
     * 如果游游优先使用大面额购买且钱是够的情况下,请计算出需要投入硬币次数？ 输入描述 依次输入， 需要可乐的数量为 m 10元的张数为 a 50元的张数为 b
     * 100元的张树为 c 1瓶可乐的价格为 x 输出描述 输出当前金额下需要投入硬币的次数
     * 例如需要购买2瓶可乐，每瓶可乐250圆，手里有100圆3枚，50圆4枚，10圆1枚。 购买第1瓶投递100圆3枚，找50圆 购买第2瓶投递50圆5枚
     * 所以是总共需要操作8次金额投递操作 样例输入 2 1 4 3 250 样例输出 8
     */


    /**
     * 模拟找零过程：按照贪心策略从大面额到小面额进行找零
     * 
     * @param qian 面额数组 [100, 50, 10]
     * @param zhang 对应面额的硬币数量数组
     * @param i 从第i种面额开始找零
     * @param rest 需要找零的金额
     * @param times 找零的次数（通常为1，但优化版本可能批量处理）
     */
    private static void giveRest(int[] qian, int[] zhang, int i, int rest, int times) {
        // 从第i种面额开始，按照贪心策略进行找零
        for (; i < 3; i++) {
            zhang[i] += (rest / qian[i]) * times; // 添加对应面额的硬币数量
            rest %= qian[i];                      // 更新剩余金额
        }
    }

    /**
     * 递归购买一瓶可乐（暴力解法）
     * 优先使用最大面额的硬币，如果不够就递归使用更小面额
     * 
     * @param qian 面额数组 [100, 50, 10]
     * @param zhang 对应面额的硬币数量数组
     * @param rest 还需要支付的金额
     * @return 返回投入硬币的次数，-1表示不能完成购买
     */
    private static int buyOne(int[] qian, int[] zhang, int rest) {
        // 找到第一个有库存的硬币面额
        int first = -1;
        for (int i = 0; i < 3; i++) {
            if (zhang[i] != 0) {
                first = i;
                break;
            }
        }
        if (first == -1) {
            return -1; // 没有任何硬币，无法购买
        }
        
        if (qian[first] >= rest) {
            // 当前面额已经足够支付
            zhang[first]--;
            giveRest(qian, zhang, first + 1, qian[first] - rest, 1); // 找零
            return 1;
        } else {
            // 当前面额不够，需要继续使用其他硬币
            zhang[first]--;
            int next = buyOne(qian, zhang, rest - qian[first]);
            if (next == -1) {
                return -1; // 递归失败，还原状态
            }
            return 1 + next;
        }
    }

    /**
     * 暴力解法：逐个购买可乐，每次都进行一次完整的递归计算
     * 用于对拍验证优化算法的正确性
     * 
     * @param m 需要购买的可乐数量
     * @param a 10元硬币枚数
     * @param b 50元硬币枚数
     * @param c 100元硬币枚数
     * @param x 一瓶可乐的价格
     * @return 总共需要投入硬币的次数，-1表示不能完成
     */
    public static int sure(int m, int a, int b, int c, int x) {
        int[] qian = {100, 50, 10};  // 面额数组（从大到小）
        int[] zhang = {c, b, a};     // 对应的硬币数量
        int puts = 0;                // 累计投入硬币次数
        
        while (m != 0) {
            int cur = buyOne(qian, zhang, x);
            if (cur == -1) {
                return -1; // 无法购买
            }
            puts += cur;
            m--;
        }
        return puts;
    }

    /**
     * 优化算法：通过数学分析减少重复计算，大幅提高效率
     * 
     * 核心思想：
     * 1. 对于同一种面额，先买第一瓶（可能需要组合多种面额）
     * 2. 后续的可乐可以批量购买（因为每瓶的成本和找零都相同）
     * 3. 利用前一种面额的剩余金额来辅助下一种面额的购买
     * 
     * @param m 需要购买的可乐数量
     * @param a 10元硬币枚数
     * @param b 50元硬币枚数
     * @param c 100元硬币枚数
     * @param x 一瓶可乐的价格
     * @return 总共需要投入硬币的次数，-1表示不能完成
     */
    public static int times(int m, int a, int b, int c, int x) {
        int[] qian = {100, 50, 10};  // 面额数组
        int[] zhang = {c, b, a};     // 对应的硬币数量
        int puts = 0;                // 累计投入硬币次数
        int preRest = 0;             // 上一种面额的剩余金额
        int preZhang = 0;            // 上一种面额的剩余硬币数
        
        // 按照面额从大到小的顺序处理
        for (int i = 0; i < 3 && m != 0; i++) {
            // 第一步：买第一瓶（可能需要组合多种面额）
            // 计算需要多少当前面额的硬币（向上取整）
            int firstBuyZhang = (x - preRest + qian[i] - 1) / qian[i];
            
            if (zhang[i] >= firstBuyZhang) {
                // 当前面额足够买第一瓶
                giveRest(qian, zhang, i + 1, (preRest + qian[i] * firstBuyZhang) - x, 1);
                puts += firstBuyZhang + preZhang; // 加上之前积累的硬币数
                zhang[i] -= firstBuyZhang;
                m--;
            } else {
                // 当前面额不够，继续积累到下一种面额
                preRest += qian[i] * zhang[i];
                preZhang += zhang[i];
                continue;
            }

            // 第二步：批量购买剩余的可乐（每瓶成本相同）
            int buyZhang = (x + qian[i] - 1) / qian[i];  // 每瓶需要的当前面额硬币数
            int cola = Math.min(zhang[i] / buyZhang, m); // 最多能买多少瓶
            int rest = qian[i] * buyZhang - x;           // 每瓶的找零金额
            
            giveRest(qian, zhang, i + 1, rest, cola);   // 批量找零
            puts += buyZhang * cola;                     // 批量投入
            zhang[i] -= buyZhang * cola;                 // 更新硬币数量
            m -= cola;                                   // 更新剩余可乐数
            
            // 准备下一轮的初始状态
            preRest = qian[i] * zhang[i];
            preZhang = zhang[i];
        }
        return m == 0 ? puts : -1;
    }

    /**
     * 测试方法：对拍验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000;    // 测试次数
        int zhangMax = 10;   // 最大硬币数量
        int colaMax = 10;    // 最大可乐数量
        int priceMax = 20;   // 最大价格系数
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int m = (int) (colaMax * Math.random());                    // 随机可乐数量
            int a = (int) (zhangMax * Math.random());                   // 随机10元硬币数
            int b = (int) (zhangMax * Math.random());                   // 随机50元硬币数
            int c = (int) (zhangMax * Math.random());                   // 随机100元硬币数
            int x = ((int) (priceMax * Math.random()) + 1) * 10;        // 随机价格（保证是10的倍数）
            
            int ans1 = times(m, a, b, c, x);  // 优化算法
            int ans2 = sure(m, a, b, c, x);   // 暴力算法
            
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
