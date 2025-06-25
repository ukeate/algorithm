package leetc.followup;

import java.util.LinkedList;

/**
 * 扑克牌组合问题 (Cards Combination Problem)
 * 
 * 问题描述：
 * 每张扑克牌有3个属性，每种属性有3种值（A、B、C）
 * 例如："AAA" 表示三个属性都是A，"BCA" 表示属性分别是B、C、A
 * 
 * 达标条件：
 * 从牌组中选择3张牌，对于每种属性，这3张牌必须满足：
 * - 要么3张牌的该属性完全相同
 * - 要么3张牌的该属性完全不同
 * 
 * 示例：
 * "ABC"、"CBC"、"BBC" 这3张牌达标：
 * - 第1个属性：A、C、B（全不一样）✓
 * - 第2个属性：B、B、B（全一样）✓  
 * - 第3个属性：C、C、C（全一样）✓
 * 
 * 解法思路：
 * 方法1：暴力枚举 - 直接枚举所有3张牌的组合
 * 方法2：状态压缩优化 - 将每张牌的3个属性编码为一个数字，统计相同牌的数量
 * 
 * 优化技巧：
 * - 使用三进制编码：每张牌可以表示为 (a*9 + b*3 + c) 的形式
 * - 统计每种状态的牌数，避免重复计算
 * - 分别处理同种牌的组合和不同牌的组合
 * 
 * 时间复杂度：
 * - 方法1：O(n³) - 枚举所有3张牌的组合
 * - 方法2：O(27³) - 最多27种不同状态的牌
 * 
 * 空间复杂度：O(27) - 状态数组
 */
public class CardsProblem {
    /**
     * 检查3张牌是否满足达标条件（方法1：直接检查）
     * 
     * @param picks 选中的3张牌
     * @return 如果达标返回1，否则返回0
     */
    private static int way1(LinkedList<String> picks) {
        char[] s1 = picks.get(0).toCharArray();
        char[] s2 = picks.get(1).toCharArray();
        char[] s3 = picks.get(2).toCharArray();
        
        // 检查每个属性位置
        for (int i = 0; i < 3; i++) {
            // 达标条件：要么全不一样，要么全一样
            boolean allDifferent = (s1[i] != s2[i] && s1[i] != s3[i] && s2[i] != s3[i]);
            boolean allSame = (s1[i] == s2[i] && s1[i] == s3[i]);
            
            if (allDifferent || allSame) {
                continue; // 该属性满足条件
            }
            return 0; // 该属性不满足条件，直接返回0
        }
        return 1; // 所有属性都满足条件
    }

    /**
     * 递归枚举所有3张牌的组合（方法1的递归过程）
     * 
     * @param cards 牌组数组
     * @param idx 当前处理的牌的索引
     * @param picks 当前已选择的牌
     * @return 从当前状态开始能够达标的组合数
     */
    private static int process1(String[] cards, int idx, LinkedList<String> picks) {
        if (picks.size() == 3) {
            // 已选择3张牌，检查是否达标
            return way1(picks);
        }
        if (idx == cards.length) {
            // 遍历完所有牌但未选够3张
            return 0;
        }
        
        // 不选择当前牌
        int ways = process1(cards, idx + 1, picks);
        
        // 选择当前牌
        picks.addLast(cards[idx]);
        ways += process1(cards, idx + 1, picks);
        picks.pollLast(); // 恢复现场
        
        return ways;
    }

    /**
     * 方法1：暴力枚举所有3张牌的组合
     * 
     * @param cards 牌组数组
     * @return 达标的组合数量
     */
    public static int ways1(String[] cards) {
        LinkedList<String> picks = new LinkedList<>();
        return process1(cards, 0, picks);
    }

    //

    /**
     * 检查3种状态的牌是否满足达标条件，并计算组合数（方法2）
     * 
     * @param counts 每种状态的牌数统计
     * @param path 选中的3种牌状态
     * @return 该组合的达标方法数
     */
    private static int way2(int[] counts, LinkedList<Integer> path) {
        int status1 = path.get(0), status2 = path.get(1), status3 = path.get(2);
        int attrs1 = status1, attrs2 = status2, attrs3 = status3;
        
        // 逐个检查3个属性位置（从高位到低位：9、3、1）
        for (int i = 9; i > 0; i /= 3) {
            int cur1 = attrs1 / i, cur2 = attrs2 / i, cur3 = attrs3 / i; // 提取当前属性值
            attrs1 %= i; attrs2 %= i; attrs3 %= i; // 去除已处理的高位
            
            // 检查当前属性是否满足达标条件
            boolean allDifferent = (cur1 != cur2 && cur1 != cur3 && cur2 != cur3);
            boolean allSame = (cur1 == cur2 && cur1 == cur3);
            
            if (allDifferent || allSame) {
                continue; // 该属性满足条件
            }
            return 0; // 该属性不满足条件
        }
        
        // 所有属性都满足条件，计算组合数
        return counts[status1] * counts[status2] * counts[status3];
    }

    /**
     * 递归枚举3种不同状态的牌的组合（方法2的递归过程）
     * 
     * @param counts 每种状态的牌数统计
     * @param pre 上一个选择的状态编号
     * @param path 当前已选择的状态路径
     * @return 从当前状态开始的达标组合数
     */
    private static int process2(int[] counts, int pre, LinkedList<Integer> path) {
        if (path.size() == 3) {
            // 已选择3种状态，检查是否达标并计算组合数
            return way2(counts, path);
        }
        
        int ways = 0;
        // 选择下一个状态（保证有序，避免重复计算）
        for (int next = pre + 1; next < 27; next++) {
            if (counts[next] != 0) { // 该状态有牌存在
                path.addLast(next);
                ways += process2(counts, next, path);
                path.pollLast(); // 恢复现场
            }
        }
        return ways;
    }
    /**
     * 方法2：状态压缩优化算法
     * 
     * @param cards 牌组数组
     * @return 达标的组合数量
     */
    public static int ways2(String[] cards) {
        // 统计每种状态的牌数（三进制编码：0-26共27种状态）
        int[] counts = new int[27];
        for (String s : cards) {
            char[] str = s.toCharArray();
            // 将3个属性编码为一个数字：A=0, B=1, C=2
            int status = (str[0] - 'A') * 9 + (str[1] - 'A') * 3 + (str[2] - 'A') * 1;
            counts[status]++;
        }
        
        int ways = 0;
        
        // 处理相同状态的牌：从n张相同牌中选3张，组合数为C(n,3)
        for (int status = 0; status < 27; status++) {
            int n = counts[status];
            if (n > 2) {
                ways += n * (n - 1) * (n - 2) / 6; // C(n,3) = n!/(3!(n-3)!) = n(n-1)(n-2)/6
            }
        }
        
        // 处理不同状态的牌：枚举3种不同状态的组合
        LinkedList<Integer> path = new LinkedList<>();
        for (int i = 0; i < 27; i++) {
            if (counts[i] != 0) {
                path.addLast(i);
                ways += process2(counts, i, path);
                path.pollLast();
            }
        }
        
        return ways;
    }

    //

    /**
     * 生成随机牌组用于测试
     * 
     * @param size 最大牌数
     * @return 随机生成的牌组
     */
    private static String[] randomCards(int size) {
        int n = (int) (Math.random() * size) + 3; // 至少3张牌
        String[] ans = new String[n];
        for (int i = 0; i < n; i++) {
            char c0 = (char) ((int) (Math.random() * 3) + 'A');
            char c1 = (char) ((int) (Math.random() * 3) + 'A');
            char c2 = (char) ((int) (Math.random() * 3) + 'A');
            ans[i] = String.valueOf(c0) + c1 + c2; // 注意：原代码有bug，这里修正
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int size = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String[] arr = randomCards(size);
            int ans1 = ways1(arr);
            int ans2 = ways2(arr);
            if (ans1 != ans2) {
                for (String str : arr) {
                    System.out.println(str);
                }
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
