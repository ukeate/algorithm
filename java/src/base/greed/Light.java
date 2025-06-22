package base.greed;

import java.util.HashSet;

/**
 * 路灯照明问题 - 贪心算法经典问题
 * 
 * 问题描述：
 * 给定一个字符串，其中'.'表示可以放路灯的位置，'X'表示墙壁（不能放路灯）
 * 每盏路灯可以照亮自己的位置以及相邻的位置（照亮范围为3）
 * 求照亮所有'.'位置所需的最少路灯数量
 * 
 * 贪心策略：
 * 从左到右遍历，遇到未被照亮的'.'时，尽可能向右放置路灯
 * 这样可以覆盖更多的后续位置，减少总的路灯数量
 */
public class Light {
    /**
     * 递归暴力解法 - 尝试在每个'.'位置放或不放路灯
     * @param str 道路字符数组
     * @param idx 当前处理的位置
     * @param lights 已放置路灯的位置集合
     * @return 最少需要的路灯数量
     */
    private static int process1(char[] str, int idx, HashSet<Integer> lights) {
        // 基础情况：已经处理完所有位置
        if (idx == str.length) {
            // 检查是否所有'.'位置都被照亮
            for (int i = 0; i < str.length; i++) {
                if (str[i] == '.') {
                    // 检查位置i是否被照亮（i-1, i, i+1任一位置有路灯）
                    if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
                        return Integer.MAX_VALUE;   // 未被照亮，方案无效
                    }
                }
            }
            return lights.size();   // 返回使用的路灯数量
        }
        
        // 选择不在当前位置放路灯
        int no = process1(str, idx + 1, lights);
        int yes = Integer.MAX_VALUE;
        
        // 如果当前位置是'.'，可以选择放路灯
        if (str[idx] == '.') {
            lights.add(idx);
            yes = process1(str, idx + 1, lights);
            lights.remove(idx);     // 回溯
        }
        return Math.min(no, yes);
    }

    /**
     * 暴力解法 - 枚举所有可能的路灯放置方案
     * 时间复杂度：O(2^n) 指数级
     * @param road 道路字符串
     * @return 最少需要的路灯数量
     */
    public static int minLight1(String road) {
        if (road == null || road.length() == 0) {
            return 0;
        }
        return process1(road.toCharArray(), 0, new HashSet<>());
    }

    //

    /**
     * 贪心算法解法1 - 逐步决策版本
     * 
     * 算法思路：
     * 从左到右遍历，当遇到未被照亮的'.'时：
     * - 如果是最后一个位置，直接在该位置放路灯
     * - 如果下一个位置是'X'，在当前位置放路灯
     * - 否则在下一个位置放路灯（这样可以照亮更多位置）
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param road 道路字符串
     * @return 最少需要的路灯数量
     */
    public static int minLight2(String road) {
        char[] str = road.toCharArray();
        int i = 0;
        int light = 0;
        while (i < str.length) {
            if (str[i] == 'X') {
                i++;    // 遇到墙壁，跳过
            } else {
                light++;    // 需要放一盏路灯
                if (i + 1 == str.length) {
                    // 最后一个位置，直接结束
                    break;
                } else {
                    if (str[i + 1] == 'X') {
                        // 下一个位置是墙壁，在当前位置放路灯
                        i = i + 2;
                    } else {
                        // 在i+1位置放路灯，可以照亮i, i+1, i+2
                        i = i + 3;
                    }
                }
            }
        }
        return light;
    }

    //

    /**
     * 贪心算法解法2 - 分段处理版本
     * 
     * 算法思路：
     * 将道路按'X'分割成多个连续的'.'段
     * 对每个长度为n的连续'.'段，需要的路灯数量为(n+2)/3
     * 
     * 数学推导：
     * - 长度1：需要1盏灯 -> (1+2)/3 = 1
     * - 长度2：需要1盏灯 -> (2+2)/3 = 1
     * - 长度3：需要1盏灯 -> (3+2)/3 = 1
     * - 长度4：需要2盏灯 -> (4+2)/3 = 2
     * - 长度5：需要2盏灯 -> (5+2)/3 = 2
     * - 长度6：需要2盏灯 -> (6+2)/3 = 2
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param road 道路字符串
     * @return 最少需要的路灯数量
     */
    public static int minLight3(String road) {
        char[] str = road.toCharArray();
        int cur = 0;    // 当前连续'.'段的长度
        int light = 0;  // 总路灯数量
        for (char c : str) {
            if (c == 'X') {
                // 遇到墙壁，结算当前'.'段需要的路灯数
                light += (cur + 2) / 3;
                cur = 0;    // 重置计数器
            } else {
                cur++;      // 连续'.'段长度增加
            }
        }
        // 处理最后一个'.'段
        light += (cur + 2) / 3;
        return light;
    }

    /**
     * 生成随机道路字符串用于测试
     * @param maxLen 最大长度
     * @return 随机道路字符串
     */
    private static String randomStr(int maxLen) {
        char[] res = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = Math.random() < 0.5 ? 'X' : '.';
        }
        return String.valueOf(res);
    }

    /**
     * 测试方法 - 验证三种算法的正确性
     */
    public static void main(String[] args) {
        int maxLen = 20;
        int times = 100000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String str = randomStr(maxLen);
            int ans1 = minLight1(str);
            int ans2 = minLight2(str);
            int ans3 = minLight3(str);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
