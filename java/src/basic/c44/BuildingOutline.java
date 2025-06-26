package basic.c44;

import java.util.*;

/**
 * 天际线问题（Skyline Problem）
 * LeetCode: https://leetcode.com/problems/the-skyline-problem/
 * 
 * 问题描述：
 * 给定若干建筑物的信息[左边界, 右边界, 高度]，
 * 求出这些建筑物形成的天际线轮廓线
 * 
 * 输出格式：
 * 每个轮廓线段用[起始x, 结束x, 高度]表示
 * 
 * 算法思路：
 * 扫描线算法 + TreeMap
 * 1. 将每个建筑物拆分为进入和离开两个事件
 * 2. 按x坐标排序所有事件，处理同坐标的特殊情况
 * 3. 使用TreeMap维护当前活跃的高度集合
 * 4. 遍历事件，动态维护最大高度，记录高度变化点
 * 
 * 关键难点：
 * - 同坐标事件的处理顺序
 * - 高度重复的处理（使用计数）
 * - 轮廓线段的合并逻辑
 * 
 * 时间复杂度：O(n*logn) - 排序 + TreeMap操作
 * 空间复杂度：O(n) - 事件数组 + TreeMap
 * 
 * @author 算法学习
 */
public class BuildingOutline {
    
    /**
     * 事件类：表示建筑物的进入或离开事件
     */
    private static class Op {
        public int x;       // 事件发生的x坐标
        public boolean isAdd; // true表示建筑物开始，false表示建筑物结束
        public int h;       // 建筑物高度
        
        public Op(int x, boolean isAdd, int h) {
            this.x = x;
            this.isAdd = isAdd;
            this.h = h;
        }
    }

    /**
     * 事件比较器：定义事件的处理顺序
     * 
     * 排序规则：
     * 1. 按x坐标升序
     * 2. 同x坐标时，进入事件(isAdd=true)优先于离开事件
     * 3. 这样可以正确处理建筑物相邻的情况
     */
    private static class Comp implements Comparator<Op> {
        @Override
        public int compare(Op o1, Op o2) {
            // 首先按x坐标排序
            if (o1.x != o2.x) {
                return o1.x - o2.x;
            }
            
            // x坐标相同时，进入事件优先（避免高度瞬间降为0再升高）
            if (o1.isAdd != o2.isAdd) {
                return o1.isAdd ? -1 : 1;
            }
            
            return 0;
        }
    }

    /**
     * 计算建筑物天际线轮廓
     * 
     * @param matrix 建筑物数组，每个元素为[左边界, 右边界, 高度]
     * @return 轮廓线段列表，每个元素为[起始x, 结束x, 高度]
     * 
     * 算法步骤：
     * 1. 将建筑物转换为事件序列
     * 2. 排序事件序列
     * 3. 扫描事件，维护活跃高度集合
     * 4. 记录每个x坐标对应的最大高度
     * 5. 生成轮廓线段
     */
    public static List<List<Integer>> outline(int[][] matrix) {
        int n = matrix.length;
        
        // 创建事件数组：每个建筑物产生2个事件
        Op[] ops = new Op[n << 1];
        for (int i = 0; i < matrix.length; i++) {
            // 进入事件：建筑物开始
            ops[i * 2] = new Op(matrix[i][0], true, matrix[i][2]);
            // 离开事件：建筑物结束
            ops[i * 2 + 1] = new Op(matrix[i][1], false, matrix[i][2]);
        }
        
        // 按定义的规则排序事件
        Arrays.sort(ops, new Comp());
        
        // ht: <高度, 出现次数> - 维护当前活跃的高度
        TreeMap<Integer, Integer> ht = new TreeMap<>();
        // xh: <x坐标, 该位置的最大高度> - 记录每个关键点的高度
        TreeMap<Integer, Integer> xh = new TreeMap<>();
        
        // 扫描所有事件
        for (int i = 0; i < ops.length; i++) {
            if (ops[i].isAdd) {
                // 进入事件：添加高度
                if (!ht.containsKey(ops[i].h)) {
                    ht.put(ops[i].h, 1);
                } else {
                    ht.put(ops[i].h, ht.get(ops[i].h) + 1);
                }
            } else {
                // 离开事件：移除高度
                if (ht.get(ops[i].h) == 1) {
                    ht.remove(ops[i].h);
                } else {
                    ht.put(ops[i].h, ht.get(ops[i].h) - 1);
                }
            }
            
            // 记录当前x坐标的最大高度
            if (ht.isEmpty()) {
                xh.put(ops[i].x, 0); // 没有建筑物时高度为0
            } else {
                xh.put(ops[i].x, ht.lastKey()); // TreeMap的lastKey是最大值
            }
        }
        
        // 生成轮廓线段
        List<List<Integer>> res = new ArrayList<>();
        int preX = 0;   // 前一个线段的起始x坐标
        int preH = 0;   // 前一个线段的高度
        
        for (Map.Entry<Integer, Integer> entry : xh.entrySet()) {
            int x = entry.getKey();   // 当前x坐标
            int h = entry.getValue(); // 当前高度
            
            // 高度发生变化时，生成新的线段
            if (preH != h) {
                if (preH != 0) {
                    // 添加前一个线段：[起始x, 结束x, 高度]
                    res.add(new ArrayList<>(Arrays.asList(preX, x, preH)));
                }
                preX = x; // 更新新线段的起始位置
                preH = h; // 更新新线段的高度
            }
        }
        
        return res;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：经典案例
        int[][] buildings1 = {
            {2, 9, 10},   // 建筑物1：x=2到9，高度10
            {3, 7, 15},   // 建筑物2：x=3到7，高度15
            {5, 12, 12},  // 建筑物3：x=5到12，高度12
            {15, 20, 10}, // 建筑物4：x=15到20，高度10
            {19, 24, 8}   // 建筑物5：x=19到24，高度8
        };
        
        System.out.println("建筑物信息：");
        for (int[] building : buildings1) {
            System.out.println("  [" + building[0] + ", " + building[1] + ", " + building[2] + "]");
        }
        
        List<List<Integer>> result = outline(buildings1);
        System.out.println("\n天际线轮廓：");
        for (List<Integer> segment : result) {
            System.out.println("  [" + segment.get(0) + ", " + segment.get(1) + ", " + segment.get(2) + "]");
        }
        
        // 测试用例2：简单案例
        int[][] buildings2 = {{0, 3, 3}, {1, 5, 3}, {2, 4, 4}, {3, 7, 1}};
        System.out.println("\n简单测试用例：");
        List<List<Integer>> result2 = outline(buildings2);
        for (List<Integer> segment : result2) {
            System.out.println("  [" + segment.get(0) + ", " + segment.get(1) + ", " + segment.get(2) + "]");
        }
    }
}
