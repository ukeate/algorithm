package leetc.top;

import java.util.*;

/**
 * LeetCode 362. 设计计数器 (Design Hit Counter)
 * 
 * 问题描述：
 * 设计一个击中计数器，它可以统计在过去5分钟内被击中的次数。
 * 
 * 每个函数会接收一个时间戳参数（以秒为单位），你可以假设最早的时间戳从1开始，
 * 且都是按照时间顺序对系统进行调用（即时间戳是单调递增的）。
 * 
 * 在同一时刻有可能会有多次点击。
 * 
 * 实现 HitCounter 类：
 * - HitCounter() 初始化击中计数器系统。
 * - void hit(int timestamp) 记录在 timestamp 时刻有一次击中。假设 timestamp 按单调递增顺序调用。
 * - int getHits(int timestamp) 返回在过去 5 分钟内（即从 timestamp-300+1 到 timestamp，含两端）的击中次数。
 * 
 * 示例：
 * 输入：
 * ["HitCounter", "hit", "hit", "hit", "getHits", "hit", "getHits", "getHits"]
 * [[], [1], [2], [3], [4], [300], [300], [301]]
 * 输出：
 * [null, null, null, null, 3, null, 4, 3]
 * 
 * 解释：
 * HitCounter hitCounter = new HitCounter();
 * hitCounter.hit(1);       // 在时间戳 1 处击中。
 * hitCounter.hit(2);       // 在时间戳 2 处击中。
 * hitCounter.hit(3);       // 在时间戳 3 处击中。
 * hitCounter.getHits(4);   // 在时间戳 4 统计过去 5 分钟内的击中次数, 函数返回 3 。
 * hitCounter.hit(300);     // 在时间戳 300 处击中。
 * hitCounter.getHits(300); // 在时间戳 300 统计过去 5 分钟内的击中次数，函数返回 4 。
 * hitCounter.getHits(301); // 在时间戳 301 统计过去 5 分钟内的击中次数，函数返回 3 。
 * 
 * 提示：
 * - 1 <= timestamp <= 2 * 10^9
 * - 所有对系统的调用都是按时间顺序进行的（即 timestamp 是单调递增的）
 * - 在同一时刻可能会有多次击中
 * - 1 <= timestamp <= 2 * 10^9
 * - 最多调用 getHits 和 hit 方法 300 次
 * 
 * 进阶：
 * 如果每秒的击中次数是一个很大的数字，你的设计能否应对？
 * 
 * 解法思路：
 * 
 * 根据不同的需求和约束，有多种实现方式：
 * 
 * 1. 队列方法：
 *    - 使用队列存储所有击中的时间戳
 *    - getHits时移除过期的时间戳
 *    - 适合击中频率不高的场景
 * 
 * 2. 数组+循环利用：
 *    - 使用固定大小的数组（300个）
 *    - 每个位置存储对应秒数的击中次数
 *    - 通过取模实现循环利用
 * 
 * 3. 滑动窗口：
 *    - 维护一个5分钟的滑动窗口
 *    - 使用TreeMap或其他数据结构优化
 * 
 * 时间复杂度和空间复杂度因实现方式而异
 * 
 * LeetCode链接：https://leetcode.com/problems/design-hit-counter/
 */
public class P362_DesignHitCounter {
    
    /**
     * 方法一：队列实现（简单直观）
     * 
     * 使用队列存储所有击中时间戳，获取时清理过期数据
     */
    static class HitCounter1 {
        private Queue<Integer> hits;
        private static final int WINDOW_SIZE = 300; // 5分钟 = 300秒
        
        /**
         * 初始化击中计数器
         */
        public HitCounter1() {
            hits = new LinkedList<>();
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            hits.offer(timestamp);
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            // 移除过期的击中记录（超过5分钟）
            while (!hits.isEmpty() && timestamp - hits.peek() >= WINDOW_SIZE) {
                hits.poll();
            }
            return hits.size();
        }
    }
    
    /**
     * 方法二：循环数组实现（空间优化）
     * 
     * 使用固定大小的数组，通过取模实现循环利用
     */
    static class HitCounter2 {
        private int[] times;   // 存储时间戳
        private int[] hits;    // 存储对应时间的击中次数
        private static final int SIZE = 300; // 数组大小
        
        /**
         * 初始化击中计数器
         */
        public HitCounter2() {
            times = new int[SIZE];
            hits = new int[SIZE];
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            int index = timestamp % SIZE;
            
            // 如果当前位置的时间戳不是当前时间，说明需要重置
            if (times[index] != timestamp) {
                times[index] = timestamp;
                hits[index] = 1;
            } else {
                // 同一时间戳，增加击中次数
                hits[index]++;
            }
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            int totalHits = 0;
            
            for (int i = 0; i < SIZE; i++) {
                // 检查该位置的时间戳是否在有效窗口内
                if (timestamp - times[i] < SIZE) {
                    totalHits += hits[i];
                }
            }
            
            return totalHits;
        }
    }
    
    /**
     * 方法三：TreeMap实现（处理高频击中）
     * 
     * 使用TreeMap存储时间戳和对应的击中次数，适合处理大量击中的场景
     */
    static class HitCounter3 {
        private TreeMap<Integer, Integer> hitMap;
        private static final int WINDOW_SIZE = 300;
        
        /**
         * 初始化击中计数器
         */
        public HitCounter3() {
            hitMap = new TreeMap<>();
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            hitMap.put(timestamp, hitMap.getOrDefault(timestamp, 0) + 1);
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            // 清理过期数据
            cleanExpiredHits(timestamp);
            
            // 计算有效窗口内的总击中次数
            int totalHits = 0;
            int startTime = timestamp - WINDOW_SIZE + 1;
            
            for (Map.Entry<Integer, Integer> entry : hitMap.tailMap(startTime).entrySet()) {
                if (entry.getKey() <= timestamp) {
                    totalHits += entry.getValue();
                }
            }
            
            return totalHits;
        }
        
        /**
         * 清理过期的击中记录
         * 
         * @param currentTime 当前时间戳
         */
        private void cleanExpiredHits(int currentTime) {
            int expireTime = currentTime - WINDOW_SIZE;
            
            // 移除所有过期的记录
            Iterator<Integer> iterator = hitMap.keySet().iterator();
            while (iterator.hasNext()) {
                int time = iterator.next();
                if (time <= expireTime) {
                    iterator.remove();
                } else {
                    break; // TreeMap是有序的，后面的都不会过期
                }
            }
        }
    }
    
    /**
     * 方法四：双指针 + 列表实现
     * 
     * 使用列表存储击中时间戳，通过双指针维护有效窗口
     */
    static class HitCounter4 {
        private List<Integer> hitTimes;
        private static final int WINDOW_SIZE = 300;
        
        /**
         * 初始化击中计数器
         */
        public HitCounter4() {
            hitTimes = new ArrayList<>();
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            hitTimes.add(timestamp);
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            int startTime = timestamp - WINDOW_SIZE + 1;
            
            // 使用二分查找找到有效窗口的起始位置
            int left = findFirstValidIndex(startTime);
            int right = findLastValidIndex(timestamp);
            
            return Math.max(0, right - left + 1);
        }
        
        /**
         * 二分查找找到第一个有效时间戳的索引
         * 
         * @param startTime 窗口开始时间
         * @return 第一个有效索引
         */
        private int findFirstValidIndex(int startTime) {
            int left = 0, right = hitTimes.size() - 1;
            int result = hitTimes.size();
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (hitTimes.get(mid) >= startTime) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            return result;
        }
        
        /**
         * 二分查找找到最后一个有效时间戳的索引
         * 
         * @param endTime 窗口结束时间
         * @return 最后一个有效索引
         */
        private int findLastValidIndex(int endTime) {
            int left = 0, right = hitTimes.size() - 1;
            int result = -1;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (hitTimes.get(mid) <= endTime) {
                    result = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            return result;
        }
    }
    
    /**
     * 方法五：优化的循环数组（推荐实现）
     * 
     * 改进的循环数组，更好地处理时间戳
     */
    static class HitCounter5 {
        private int[] buckets;     // 存储每秒的击中次数
        private int[] timestamps;  // 存储每个桶对应的时间戳
        private static final int SIZE = 300;
        
        /**
         * 初始化击中计数器
         */
        public HitCounter5() {
            buckets = new int[SIZE];
            timestamps = new int[SIZE];
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            int index = timestamp % SIZE;
            
            // 如果是新的时间戳，重置该桶
            if (timestamps[index] != timestamp) {
                timestamps[index] = timestamp;
                buckets[index] = 0;
            }
            
            buckets[index]++;
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            int total = 0;
            
            for (int i = 0; i < SIZE; i++) {
                // 检查该桶是否在有效时间窗口内
                if (timestamp - timestamps[i] < SIZE) {
                    total += buckets[i];
                }
            }
            
            return total;
        }
    }
    
    /**
     * 方法六：分层时间轮实现（高级优化）
     * 
     * 适合处理非常高频的击中情况
     */
    static class HitCounter6 {
        private Map<Integer, Integer> secondBuckets;  // 秒级桶
        private Map<Integer, Integer> minuteBuckets;  // 分钟级桶
        private int lastCleanTime;
        private static final int WINDOW_SIZE = 300;
        
        /**
         * 初始化击中计数器
         */
        public HitCounter6() {
            secondBuckets = new HashMap<>();
            minuteBuckets = new HashMap<>();
            lastCleanTime = 0;
        }
        
        /**
         * 记录一次击中
         * 
         * @param timestamp 击中时间戳
         */
        public void hit(int timestamp) {
            secondBuckets.put(timestamp, secondBuckets.getOrDefault(timestamp, 0) + 1);
            
            // 定期整理数据
            if (timestamp - lastCleanTime >= 60) {
                consolidateData(timestamp);
                lastCleanTime = timestamp;
            }
        }
        
        /**
         * 获取过去5分钟的击中次数
         * 
         * @param timestamp 当前时间戳
         * @return 过去5分钟的击中次数
         */
        public int getHits(int timestamp) {
            cleanExpiredData(timestamp);
            
            int total = 0;
            int startTime = timestamp - WINDOW_SIZE + 1;
            
            // 计算秒级桶的贡献
            for (Map.Entry<Integer, Integer> entry : secondBuckets.entrySet()) {
                if (entry.getKey() >= startTime && entry.getKey() <= timestamp) {
                    total += entry.getValue();
                }
            }
            
            return total;
        }
        
        /**
         * 整理数据，将旧的秒级数据合并到分钟级
         * 
         * @param currentTime 当前时间戳
         */
        private void consolidateData(int currentTime) {
            Iterator<Map.Entry<Integer, Integer>> iterator = secondBuckets.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = iterator.next();
                int time = entry.getKey();
                
                // 将超过1分钟的数据移到分钟级桶
                if (currentTime - time >= 60) {
                    int minuteKey = time / 60;
                    minuteBuckets.put(minuteKey, minuteBuckets.getOrDefault(minuteKey, 0) + entry.getValue());
                    iterator.remove();
                }
            }
        }
        
        /**
         * 清理过期数据
         * 
         * @param currentTime 当前时间戳
         */
        private void cleanExpiredData(int currentTime) {
            int expireTime = currentTime - WINDOW_SIZE;
            
            // 清理过期的秒级数据
            secondBuckets.entrySet().removeIf(entry -> entry.getKey() <= expireTime);
            
            // 清理过期的分钟级数据
            minuteBuckets.entrySet().removeIf(entry -> (entry.getKey() + 1) * 60 <= expireTime);
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试不同实现的HitCounter ===");
        
        // 测试方法1：队列实现
        System.out.println("\n--- 测试方法1：队列实现 ---");
        testHitCounter("队列实现", () -> new HitCounter1());
        
        // 测试方法2：循环数组实现
        System.out.println("\n--- 测试方法2：循环数组实现 ---");
        testHitCounter("循环数组实现", () -> new HitCounter2());
        
        // 测试方法3：TreeMap实现
        System.out.println("\n--- 测试方法3：TreeMap实现 ---");
        testHitCounter("TreeMap实现", () -> new HitCounter3());
        
        // 测试方法5：优化循环数组
        System.out.println("\n--- 测试方法5：优化循环数组 ---");
        testHitCounter("优化循环数组", () -> new HitCounter5());
    }
    
    /**
     * 通用测试接口
     */
    interface HitCounterFactory {
        Object create();
    }
    
    /**
     * 测试击中计数器的通用方法
     * 
     * @param name 实现名称
     * @param factory 创建计数器的工厂方法
     */
    private static void testHitCounter(String name, HitCounterFactory factory) {
        Object counter = factory.create();
        
        try {
            // 反射调用方法进行测试
            Class<?> clazz = counter.getClass();
            java.lang.reflect.Method hitMethod = clazz.getMethod("hit", int.class);
            java.lang.reflect.Method getHitsMethod = clazz.getMethod("getHits", int.class);
            
            // 执行测试案例
            hitMethod.invoke(counter, 1);
            hitMethod.invoke(counter, 2);
            hitMethod.invoke(counter, 3);
            
            int hits1 = (Integer) getHitsMethod.invoke(counter, 4);
            System.out.println("getHits(4): " + hits1 + " (期望: 3)");
            
            hitMethod.invoke(counter, 300);
            
            int hits2 = (Integer) getHitsMethod.invoke(counter, 300);
            System.out.println("getHits(300): " + hits2 + " (期望: 4)");
            
            int hits3 = (Integer) getHitsMethod.invoke(counter, 301);
            System.out.println("getHits(301): " + hits3 + " (期望: 3)");
            
            // 验证结果
            if (hits1 == 3 && hits2 == 4 && hits3 == 3) {
                System.out.println("✓ " + name + " 测试通过");
            } else {
                System.out.println("✗ " + name + " 测试失败");
            }
            
        } catch (Exception e) {
            System.out.println("✗ " + name + " 测试出现异常: " + e.getMessage());
        }
    }
} 