package base.tree4;

import java.util.TreeMap;

/**
 * 有序表性能对比测试类
 * 
 * 本类用于测试和比较以下几种有序表实现的功能正确性和性能表现：
 * 1. Java原生TreeMap (红黑树实现)
 * 2. AVL平衡二叉搜索树
 * 3. SizeBalanced Tree (SB树)
 * 4. SkipList (跳表)
 * 
 * 测试包括功能测试和性能测试两部分：
 * - 功能测试：验证各种操作的正确性，包括增删改查、边界查找等
 * - 性能测试：在不同数据模式下比较各实现的运行效率
 */
public class CompareTest {
    
    /**
     * 功能测试方法
     * 
     * 通过大量随机操作测试各种有序表实现的功能正确性：
     * 1. 随机插入和删除键值对
     * 2. 验证containsKey操作的一致性
     * 3. 验证get操作的一致性
     * 4. 验证floorKey操作的一致性（小于等于给定key的最大key）
     * 5. 验证ceilingKey操作的一致性（大于等于给定key的最小key）
     * 6. 验证firstKey和lastKey操作的一致性
     * 7. 验证size操作的一致性
     * 
     * 如果发现任何不一致，将输出错误信息并终止测试
     */
    public static void functionTest() {
        System.out.println("功能测试开始");
        // 创建四种不同的有序表实现
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        AVL.AVLTreeMap<Integer, Integer> avl = new AVL.AVLTreeMap<>();
        SB.SizeBalancedTreeMap<Integer, Integer> sbt = new SB.SizeBalancedTreeMap<>();
        SkipList.SkipListMap<Integer, Integer> skip = new SkipList.SkipListMap<>();
        
        // 测试参数设置
        int maxK = 500;        // 键的最大值
        int maxV = 50000;      // 值的最大值
        int testTime = 1000000; // 测试次数
        
        for (int i = 0; i < testTime; i++) {
            // 随机插入操作
            int addK = (int) (Math.random() * maxK);
            int addV = (int) (Math.random() * maxV);
            treeMap.put(addK, addV);
            avl.put(addK, addV);
            sbt.put(addK, addV);
            skip.put(addK, addV);

            // 随机删除操作
            int removeK = (int) (Math.random() * maxK);
            treeMap.remove(removeK);
            avl.remove(removeK);
            sbt.remove(removeK);
            skip.remove(removeK);

            // 随机查询操作，验证containsKey的一致性
            int querryK = (int) (Math.random() * maxK);
            if (treeMap.containsKey(querryK) != avl.containsKey(querryK)
                    || sbt.containsKey(querryK) != skip.containsKey(querryK)
                    || treeMap.containsKey(querryK) != sbt.containsKey(querryK)) {
                System.out.println("containsKey Oops");
                System.out.println(treeMap.containsKey(querryK));
                System.out.println(avl.containsKey(querryK));
                System.out.println(sbt.containsKey(querryK));
                System.out.println(skip.containsKey(querryK));
                break;
            }

            // 如果key存在，验证get、floorKey、ceilingKey操作的一致性
            if (treeMap.containsKey(querryK)) {
                // 验证get操作
                int v1 = treeMap.get(querryK);
                int v2 = avl.get(querryK);
                int v3 = sbt.get(querryK);
                int v4 = skip.get(querryK);
                if (v1 != v2 || v3 != v4 || v1 != v3) {
                    System.out.println("get Oops");
                    System.out.println(treeMap.get(querryK));
                    System.out.println(avl.get(querryK));
                    System.out.println(sbt.get(querryK));
                    System.out.println(skip.get(querryK));
                    break;
                }
                
                // 验证floorKey操作（小于等于给定key的最大key）
                Integer f1 = treeMap.floorKey(querryK);
                Integer f2 = avl.floorKey(querryK);
                Integer f3 = sbt.floorKey(querryK);
                Integer f4 = skip.floorKey(querryK);
                if (f1 == null && (f2 != null || f3 != null || f4 != null)) {
                    System.out.println("floorKey Oops");
                    System.out.println(treeMap.floorKey(querryK));
                    System.out.println(avl.floorKey(querryK));
                    System.out.println(sbt.floorKey(querryK));
                    System.out.println(skip.floorKey(querryK));
                    break;
                }
                if (f1 != null && (f2 == null || f3 == null || f4 == null)) {
                    System.out.println("floorKey Oops");
                    System.out.println(treeMap.floorKey(querryK));
                    System.out.println(avl.floorKey(querryK));
                    System.out.println(sbt.floorKey(querryK));
                    System.out.println(skip.floorKey(querryK));
                    break;
                }
                if (f1 != null) {
                    int ans1 = f1;
                    int ans2 = f2;
                    int ans3 = f3;
                    int ans4 = f4;
                    if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                        System.out.println("floorKey Oops");
                        System.out.println(treeMap.floorKey(querryK));
                        System.out.println(avl.floorKey(querryK));
                        System.out.println(sbt.floorKey(querryK));
                        System.out.println(skip.floorKey(querryK));
                        break;
                    }
                }
                
                // 验证ceilingKey操作（大于等于给定key的最小key）
                f1 = treeMap.ceilingKey(querryK);
                f2 = avl.ceilingKey(querryK);
                f3 = sbt.ceilingKey(querryK);
                f4 = skip.ceilingKey(querryK);
                if (f1 == null && (f2 != null || f3 != null || f4 != null)) {
                    System.out.println("ceilingKey Oops");
                    System.out.println(treeMap.ceilingKey(querryK));
                    System.out.println(avl.ceilingKey(querryK));
                    System.out.println(sbt.ceilingKey(querryK));
                    System.out.println(skip.ceilingKey(querryK));
                    break;
                }
                if (f1 != null && (f2 == null || f3 == null || f4 == null)) {
                    System.out.println("ceilingKey Oops");
                    System.out.println(treeMap.ceilingKey(querryK));
                    System.out.println(avl.ceilingKey(querryK));
                    System.out.println(sbt.ceilingKey(querryK));
                    System.out.println(skip.ceilingKey(querryK));
                    break;
                }
                if (f1 != null) {
                    int ans1 = f1;
                    int ans2 = f2;
                    int ans3 = f3;
                    int ans4 = f4;
                    if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                        System.out.println("ceilingKey Oops");
                        System.out.println(treeMap.ceilingKey(querryK));
                        System.out.println(avl.ceilingKey(querryK));
                        System.out.println(sbt.ceilingKey(querryK));
                        System.out.println(skip.ceilingKey(querryK));
                        break;
                    }
                }
            }

            // 验证firstKey操作（最小key）
            Integer f1 = treeMap.firstKey();
            Integer f2 = avl.firstKey();
            Integer f3 = sbt.firstKey();
            Integer f4 = skip.firstKey();
            if (f1 == null && (f2 != null || f3 != null || f4 != null)) {
                System.out.println("firstKey Oops");
                System.out.println(treeMap.firstKey());
                System.out.println(avl.firstKey());
                System.out.println(sbt.firstKey());
                System.out.println(skip.firstKey());
                break;
            }
            if (f1 != null && (f2 == null || f3 == null || f4 == null)) {
                System.out.println("firstKey Oops");
                System.out.println(treeMap.firstKey());
                System.out.println(avl.firstKey());
                System.out.println(sbt.firstKey());
                System.out.println(skip.firstKey());
                break;
            }
            if (f1 != null) {
                int ans1 = f1;
                int ans2 = f2;
                int ans3 = f3;
                int ans4 = f4;
                if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                    System.out.println("firstKey Oops");
                    System.out.println(treeMap.firstKey());
                    System.out.println(avl.firstKey());
                    System.out.println(sbt.firstKey());
                    System.out.println(skip.firstKey());
                    break;
                }
            }

            // 验证lastKey操作（最大key）
            f1 = treeMap.lastKey();
            f2 = avl.lastKey();
            f3 = sbt.lastKey();
            f4 = skip.lastKey();
            if (f1 == null && (f2 != null || f3 != null || f4 != null)) {
                System.out.println("lastKey Oops");
                System.out.println(treeMap.lastKey());
                System.out.println(avl.lastKey());
                System.out.println(sbt.lastKey());
                System.out.println(skip.lastKey());
                break;
            }
            if (f1 != null && (f2 == null || f3 == null || f4 == null)) {
                System.out.println("firstKey Oops");
                System.out.println(treeMap.lastKey());
                System.out.println(avl.lastKey());
                System.out.println(sbt.lastKey());
                System.out.println(skip.lastKey());
                break;
            }
            if (f1 != null) {
                int ans1 = f1;
                int ans2 = f2;
                int ans3 = f3;
                int ans4 = f4;
                if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                    System.out.println("lastKey Oops");
                    System.out.println(treeMap.lastKey());
                    System.out.println(avl.lastKey());
                    System.out.println(sbt.lastKey());
                    System.out.println(skip.lastKey());
                    break;
                }
            }
            
            // 验证size操作（元素个数）
            if (treeMap.size() != avl.size() || sbt.size() != skip.size() || treeMap.size() != sbt.size()) {
                System.out.println("size Oops");
                System.out.println(treeMap.size());
                System.out.println(avl.size());
                System.out.println(sbt.size());
                System.out.println(skip.size());
                break;
            }
        }
        System.out.println("功能测试结束");
    }

    /**
     * 性能测试方法
     * 
     * 在不同的数据操作模式下测试各种有序表实现的性能表现：
     * 1. 顺序递增插入和删除
     * 2. 顺序递减插入和删除  
     * 3. 随机插入和删除
     * 
     * 每种模式都会测试所有四种实现，并记录运行时间进行对比
     */
    public static void performanceTest() {
        System.out.println("性能测试开始");
        TreeMap<Integer, Integer> treeMap;
        AVL.AVLTreeMap<Integer, Integer> avl;
        SB.SizeBalancedTreeMap<Integer, Integer> sbt;
        long start;
        long end;
        int max = 10000000; // 测试数据规模
        
        // 初始化各种有序表
        treeMap = new TreeMap<>();
        avl = new AVL.AVLTreeMap<>();
        sbt = new SB.SizeBalancedTreeMap<>();
        SkipList.SkipListMap<Integer, Integer> skip = new SkipList.SkipListMap<>();
        
        // 测试1：顺序递增插入
        System.out.println("顺序递增加入测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            treeMap.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            avl.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            sbt.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            skip.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        // 测试2：顺序递增删除
        System.out.println("顺序递增删除测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            treeMap.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            avl.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            sbt.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            skip.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        // 测试3：顺序递减插入
        System.out.println("顺序递减加入测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            treeMap.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            avl.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            sbt.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            skip.put(i, i);
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        // 测试4：顺序递减删除
        System.out.println("顺序递减删除测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            treeMap.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            avl.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            sbt.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            skip.remove(i);
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        // 测试5：随机插入
        System.out.println("随机加入测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            treeMap.put((int) (Math.random() * i), i);
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            avl.put((int) (Math.random() * i), i);
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            sbt.put((int) (Math.random() * i), i);
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            skip.put((int) (Math.random() * i), i);
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        // 测试6：随机删除
        System.out.println("随机删除测试，数据规模 : " + max);
        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            treeMap.remove((int) (Math.random() * i));
        }
        end = System.currentTimeMillis();
        System.out.println("treeMap 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            avl.remove((int) (Math.random() * i));
        }
        end = System.currentTimeMillis();
        System.out.println("avl 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            sbt.remove((int) (Math.random() * i));
        }
        end = System.currentTimeMillis();
        System.out.println("sbt 运行时间 : " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = max; i >= 0; i--) {
            skip.remove((int) (Math.random() * i));
        }
        end = System.currentTimeMillis();
        System.out.println("skip 运行时间 : " + (end - start) + "ms");

        System.out.println("性能测试结束");
    }

    /**
     * 主方法
     * 依次执行功能测试和性能测试
     */
    public static void main(String[] args) {
        functionTest();
        System.out.println("======");
        performanceTest();
    }
}
