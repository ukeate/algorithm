package giant.c2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * 按能力选择最高收入工作问题
 * 
 * 问题描述：
 * 给定一系列工作，每个工作有难度值(hard)和薪水(money)。
 * 给定一系列人的能力值(ability)。
 * 求每个人在其能力范围内能获得的最高薪水。
 * 
 * 约束条件：
 * 只能选择难度值小于等于自己能力值的工作。
 * 
 * 算法思路：
 * 1. 对工作按难度升序排列，相同难度按薪水降序排列
 * 2. 使用TreeMap维护一个“单调递增”的映射：难度 -> 能获得的最高薪水
 * 3. 对于每个能力查询，使用TreeMap.floorKey()快速找到答案
 * 
 * 核心优化：
 * 只保留那些“难度增加且薪水也增加”的工作，其他工作都是被“严格占优”的。
 * 
 * 时间复杂度：
 * - 排序：O(n*log(n))
 * - 构建TreeMap：O(n*log(n))
 * - 查询：O(m*log(n))，m为查询次数
 * 
 * 空间复杂度：O(n)
 */
public class ChooseWork {
    /**
     * 工作类：封装工作的薪水和难度信息
     */
    public static class Job {
        public int money; // 薪水
        public int hard;  // 雾度值
        
        public Job(int m, int h) {
            money = m;
            hard = h;
        }
    }

    /**
     * 工作排序比较器：
     * 1. 首先按雾度升序排列
     * 2. 雾度相同时按薪水降序排列（保证相同雾度下薪水最高的在前面）
     */
    private static class Comp implements Comparator<Job> {
        @Override
        public int compare(Job o1, Job o2) {
            return o1.hard != o2.hard ? (o1.hard - o2.hard) : (o2.money - o1.money);
        }
    }

    /**
     * 主算法：为每个能力值找到对应的最高薪水
     * 
     * @param job 工作数组
     * @param ability 能力数组
     * @return 每个能力对应的最高薪水数组
     */
    public static int[] choose(Job[] job, int[] ability) {
        // 步骤1：对工作按雾度升序，相同雾度按薪水降序排列
        Arrays.sort(job, new Comp());
        
        // 步骤2：构建单调递增的TreeMap（雾度增加，薪水也增加）
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(job[0].hard, job[0].money);
        Job pre = job[0];
        
        // 只保留那些“雾度增加且薪水也增加”的工作
        for (int i = 1; i < job.length; i++) {
            // 只有当雾度变大且薪水也变大时，才值得加入映射
            if (job[i].hard != pre.hard && job[i].money > pre.money) {
                pre = job[i];
                map.put(pre.hard, pre.money);
            }
        }
        
        // 步骤3：对每个能力查询进行快速匹配
        int[] ans = new int[ability.length];
        for (int i = 0; i < ability.length; i++) {
            // 使用floorKey找到小于等于能力值的最大雾度
            Integer key = map.floorKey(ability[i]);
            ans[i] = key != null ? map.get(key) : 0;
        }
        return ans;
    }
}
