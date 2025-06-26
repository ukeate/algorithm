package basic.c43;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 寻找多数元素问题
 * 
 * 问题描述：
 * 1. 寻找数组中出现次数超过n/2的元素（多数元素）
 * 2. 寻找数组中所有出现次数超过n/k的元素
 * 
 * 算法核心思想：
 * Boyer-Moore多数投票算法及其扩展
 * 基于"对消"原理：不同元素互相抵消，剩余的可能是多数元素
 * 
 * 关键洞察：
 * - 出现超过n/2的元素最多只有1个
 * - 出现超过n/k的元素最多只有k-1个
 * 
 * 算法特点：
 * - 时间复杂度：O(n)
 * - 空间复杂度：O(1)（对于n/2问题）或O(k)（对于n/k问题）
 * - 只需要遍历数组两次
 * 
 * @author 算法学习
 */
public class FindKMajority {
    
    /**
     * 寻找出现次数超过n/2的元素（经典多数元素问题）
     * 使用Boyer-Moore多数投票算法
     * 
     * @param arr 输入数组
     * 
     * 算法思路：
     * 阶段1：投票阶段 - 用对消的方式找到候选者
     * 阶段2：验证阶段 - 确认候选者真的出现超过n/2次
     * 
     * 投票规则：
     * 1. 如果票数为0，当前元素成为候选者，票数设为1
     * 2. 如果当前元素等于候选者，票数+1
     * 3. 如果当前元素不等于候选者，票数-1
     * 
     * 核心原理：
     * 多数元素与其他所有元素对消后仍有剩余
     */
    public static void printHalfMajor(int[] arr) {
        // 阶段1：投票阶段，找到候选者
        int cand = 0;  // 候选者
        int hp = 0;    // 候选者的票数（hp = hit points）
        
        for (int i = 0; i < arr.length; i++) {
            if (hp == 0) {
                // 票数为0，选择新的候选者
                cand = arr[i];
                hp = 1;
            } else if (arr[i] == cand) {
                // 遇到相同元素，票数+1
                hp++;
            } else {
                // 遇到不同元素，票数-1（对消）
                hp--;
            }
        }
        
        // 如果最终票数为0，说明没有多数元素
        if (hp == 0) {
            System.out.println("no");
            return;
        }
        
        // 阶段2：验证阶段，确认候选者真的是多数元素
        hp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                hp++;
            }
        }
        
        // 检查是否真的超过一半
        if (hp > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("no");
        }
    }

    /**
     * 辅助方法：将HashMap中所有值减1，并移除值为0的键
     * 这个操作相当于"一次对消k个不同的元素"
     * 
     * @param map 候选者计数表
     * 
     * 实现细节：
     * 1. 先将所有值减1
     * 2. 记录需要删除的键（值变为0的）
     * 3. 最后统一删除，避免迭代过程中修改Map
     */
    public static void allMinusOne(HashMap<Integer, Integer> map) {
        List<Integer> removeList = new LinkedList<>();
        
        // 遍历所有候选者，将计数减1
        for (Map.Entry<Integer, Integer> set : map.entrySet()) {
            Integer key = set.getKey();
            Integer val = set.getValue();
            
            if (val == 1) {
                // 记录需要删除的键
                removeList.add(key);
            }
            map.put(key, val - 1);
        }
        
        // 删除计数为0的候选者
        for (Integer key : removeList) {
            map.remove(key);
        }
    }

    /**
     * 统计候选者在原数组中的真实出现次数
     * 
     * @param arr 原数组
     * @param cands 候选者Map
     * @return 候选者的真实出现次数Map
     * 
     * 目的：
     * 投票阶段只能筛选出可能的候选者，
     * 需要重新统计来确认真实的出现次数
     */
    public static HashMap<Integer, Integer> getReals(int[] arr, HashMap<Integer, Integer> cands) {
        HashMap<Integer, Integer> reals = new HashMap<>();
        
        // 遍历数组，统计候选者的真实出现次数
        for (int i = 0; i < arr.length; i++) {
            int cur = arr[i];
            if (cands.containsKey(cur)) {
                // 只统计候选者的出现次数
                if (reals.containsKey(cur)) {
                    reals.put(cur, reals.get(cur) + 1);
                } else {
                    reals.put(cur, 1);
                }
            }
        }
        return reals;
    }

    /**
     * 寻找出现次数超过n/k的所有元素
     * Boyer-Moore算法的扩展版本
     * 
     * @param arr 输入数组
     * @param k 除数（寻找出现次数超过n/k的元素）
     * 
     * 算法思路：
     * 1. 维护最多k-1个候选者（因为超过n/k的元素最多k-1个）
     * 2. 当候选者满了且来了新元素时，进行"群体对消"
     * 3. 最后验证候选者的真实出现次数
     * 
     * 核心原理：
     * 每次对消k个不同的元素，真正的n/k多数元素能够存活下来
     */
    public static void printKMajor(int[] arr, int k) {
        if (k < 2) {
            System.out.println("k < 2");
            return;
        }
        
        // 候选者Map，最多保存k-1个候选者
        HashMap<Integer, Integer> cands = new HashMap<>();
        
        // 阶段1：投票阶段
        for (int i = 0; i < arr.length; i++) {
            if (cands.containsKey(arr[i])) {
                // 候选者已存在，计数+1
                cands.put(arr[i], cands.get(arr[i]) + 1);
            } else {
                if (cands.size() == k - 1) {
                    // 候选者已满，进行群体对消
                    // 相当于一次删除k个不同的数（包括当前的arr[i]）
                    allMinusOne(cands);
                } else {
                    // 候选者未满，直接加入
                    cands.put(arr[i], 1);
                }
            }
        }
        
        // 阶段2：验证阶段，统计候选者的真实出现次数
        HashMap<Integer, Integer> reals = getReals(arr, cands);
        
        // 输出真正超过n/k的元素
        boolean hasPrint = false;
        for (Map.Entry<Integer, Integer> set : cands.entrySet()) {
            Integer key = set.getKey();
            if (reals.get(key) > arr.length / k) {
                hasPrint = true;
                System.out.print(key + " ");
            }
        }
        System.out.println(hasPrint ? "" : "no >k number");
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 1};
        
        System.out.println("数组: [1, 2, 3, 1, 1, 2, 1]");
        System.out.print("超过n/2的元素: ");
        printHalfMajor(arr);
        
        int k = 4;
        System.out.print("超过n/" + k + "的元素: ");
        printKMajor(arr, k);
        
        // 额外测试用例
        System.out.println("\n额外测试:");
        int[] arr2 = {1, 1, 1, 2, 2, 3};
        System.out.println("数组: [1, 1, 1, 2, 2, 3]");
        System.out.print("超过n/2的元素: ");
        printHalfMajor(arr2);
        System.out.print("超过n/3的元素: ");
        printKMajor(arr2, 3);
    }
}
