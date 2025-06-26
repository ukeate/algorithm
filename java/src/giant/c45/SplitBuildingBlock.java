package giant.c45;

import java.util.Arrays;

/**
 * 积木堆叠分组问题
 * 
 * 问题描述：
 * 小明手中有n块积木，并且小明知道每块积木的重量。现在小明希望将这些积木堆起来
 * 
 * 堆叠约束条件：
 * 任意一块积木如果想堆在另一块积木上面，那么要求：
 * 1) 上面的积木重量不能小于下面的积木重量
 * 2) 上面积木的重量减去下面积木的重量不能超过x
 * 3) 每堆中最下面的积木没有重量要求
 * 
 * 魔法积木：
 * 现在小明有一个机会，除了这n块积木，还可以获得k块任意重量的积木
 * 
 * 求解目标：
 * 小明希望将积木堆在一起，同时希望积木堆的数量越少越好，你能帮他找到最好的方案么？
 * 
 * 输入格式：
 * 第一行三个整数n,k,x，1<=n<=200000，0<=x,k<=1000000000
 * 第二行n个整数，表示积木的重量，任意整数范围都在[1,1000000000]
 * 
 * 解题思路：
 * 贪心算法：
 * 1. 首先对积木按重量排序
 * 2. 找出所有需要分割的地方（相邻积木重量差>x）
 * 3. 计算每个分割点需要的魔法积木数量
 * 4. 优先填补需要魔法积木最少的分割点
 * 
 * 关键洞察：
 * - 如果相邻两个积木重量差≤x，它们可以在同一堆
 * - 如果重量差>x，需要用魔法积木连接或分为两堆
 * - 填补一个重量差为d的缺口需要⌈d/x⌉-1块魔法积木
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 来源：京东笔试题
 * 
 * @author Zhu Runqi
 */
public class SplitBuildingBlock {
    
    /**
     * 计算最少积木堆数量
     * 
     * 算法思路：
     * 1. 对积木按重量排序（从小到大）
     * 2. 扫描相邻积木，找出所有重量差>x的位置
     * 3. 记录每个分割点需要的魔法积木数量
     * 4. 对需要的魔法积木数量排序
     * 5. 贪心选择：优先填补需要魔法积木最少的分割点
     * 6. 直到魔法积木用完或所有分割点都被填补
     * 
     * 特殊情况处理：
     * - 如果已经只有1堆，直接返回1
     * - 如果x=0，无法通过魔法积木连接，返回原始分割数
     * - 如果k=0，没有魔法积木，返回原始分割数
     * 
     * @param arr 积木重量数组
     * @param k 魔法积木数量
     * @param x 重量差上限
     * @return 最少积木堆数量
     */
    public static int min(int[] arr, int k, int x) {
        // 对积木按重量排序
        Arrays.sort(arr);
        int n = arr.length;
        
        // 记录每个分割点需要的魔法积木数量
        int[] needs = new int[n];
        int size = 0;
        int splits = 1;  // 初始至少有1堆
        
        // 找出所有需要分割的地方
        for (int i = 1; i < n; i++) {
            if (arr[i] - arr[i - 1] > x) {
                // 计算填补这个缺口需要的魔法积木数量
                // 重量差为d，需要⌈d/x⌉-1块魔法积木
                needs[size++] = (arr[i] - arr[i - 1] - 1) / x;
                splits++;  // 增加一个分割
            }
        }
        
        // 特殊情况：已经只有1堆，或无法改善
        if (splits == 1 || x == 0 || k == 0) {
            return splits;
        }
        
        // 对需要的魔法积木数量排序，优先填补需要最少的
        Arrays.sort(needs, 0, size);
        
        // 贪心选择：优先填补需要魔法积木最少的分割点
        for (int i = 0; i < size; i++) {
            int need = needs[i];
            if (k >= need) {
                splits--;  // 成功填补一个分割点
                k -= need;  // 消耗魔法积木
            } else {
                break;  // 魔法积木不足，无法继续填补
            }
        }
        
        return splits;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 积木堆叠分组问题 ===\n");
        
        // 测试用例1：示例用例
        System.out.println("测试用例1：题目示例");
        int[] arr1 = {20, 20, 80, 70, 70, 70, 420, 5, 1, 5, 1, 60, 90};
        int k1 = 1, x1 = 38;
        int result1 = min(arr1.clone(), k1, x1);
        System.out.print("积木重量: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + (i == arr1.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("魔法积木数量k: " + k1);
        System.out.println("重量差限制x: " + x1);
        System.out.println("最少积木堆数: " + result1);
        System.out.println("分析: 排序后为[1,1,5,5,20,20,60,70,70,70,80,90,420]");
        System.out.println("      在20和60之间插入重量50的魔法积木可以连接两段");
        System.out.println();
        
        // 测试用例2：无需魔法积木
        System.out.println("测试用例2：无需魔法积木");
        int[] arr2 = {1, 2, 3, 4, 5};
        int k2 = 10, x2 = 1;
        int result2 = min(arr2.clone(), k2, x2);
        System.out.print("积木重量: [");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + (i == arr2.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("魔法积木数量k: " + k2);
        System.out.println("重量差限制x: " + x2);
        System.out.println("最少积木堆数: " + result2);
        System.out.println("分析: 相邻重量差都≤1，所有积木可以堆成一堆");
        System.out.println();
        
        // 测试用例3：魔法积木不足
        System.out.println("测试用例3：魔法积木不足");
        int[] arr3 = {1, 10, 20, 100};
        int k3 = 1, x3 = 5;
        int result3 = min(arr3.clone(), k3, x3);
        System.out.print("积木重量: [");
        for (int i = 0; i < arr3.length; i++) {
            System.out.print(arr3[i] + (i == arr3.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("魔法积木数量k: " + k3);
        System.out.println("重量差限制x: " + x3);
        System.out.println("最少积木堆数: " + result3);
        System.out.println("分析: 有3个分割点(1-10, 10-20, 20-100)，魔法积木只能填补1个");
        System.out.println();
        
        // 测试用例4：充足的魔法积木
        System.out.println("测试用例4：充足的魔法积木");
        int[] arr4 = {1, 50, 100, 200};
        int k4 = 100, x4 = 10;
        int result4 = min(arr4.clone(), k4, x4);
        System.out.print("积木重量: [");
        for (int i = 0; i < arr4.length; i++) {
            System.out.print(arr4[i] + (i == arr4.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("魔法积木数量k: " + k4);
        System.out.println("重量差限制x: " + x4);
        System.out.println("最少积木堆数: " + result4);
        System.out.println("分析: 魔法积木充足，可以填补所有分割点，堆成一堆");
        System.out.println();
        
        // 测试用例5：特殊情况 - x=0
        System.out.println("测试用例5：特殊情况 - x=0");
        int[] arr5 = {1, 1, 2, 2, 3};
        int k5 = 10, x5 = 0;
        int result5 = min(arr5.clone(), k5, x5);
        System.out.print("积木重量: [");
        for (int i = 0; i < arr5.length; i++) {
            System.out.print(arr5[i] + (i == arr5.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("魔法积木数量k: " + k5);
        System.out.println("重量差限制x: " + x5);
        System.out.println("最少积木堆数: " + result5);
        System.out.println("分析: x=0时无法连接不同重量的积木，按重量分组");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题建模：");
        System.out.println("   - 积木排序：确保满足重量递增约束");
        System.out.println("   - 分割识别：找出重量差>x的相邻对");
        System.out.println("   - 成本计算：每个分割点的填补成本");
        System.out.println();
        System.out.println("2. 贪心策略：");
        System.out.println("   - 优先级：填补成本最低的分割点");
        System.out.println("   - 资源约束：魔法积木数量有限");
        System.out.println("   - 最优性：局部最优导致全局最优");
        System.out.println();
        System.out.println("3. 关键计算：");
        System.out.println("   - 分割条件：arr[i] - arr[i-1] > x");
        System.out.println("   - 填补成本：⌈(重量差-1)/x⌉块魔法积木");
        System.out.println("   - 连接方式：在缺口中等间距插入魔法积木");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n log n)，主要是排序开销");
        System.out.println("   - 空间复杂度：O(n)，存储分割点信息");
        System.out.println("   - 实际性能：对于大规模数据表现良好");
        System.out.println();
        System.out.println("5. 边界情况：");
        System.out.println("   - 单一积木：直接返回1");
        System.out.println("   - x=0：无法连接，按原有分组");
        System.out.println("   - k=0：无魔法积木，返回原始分割数");
        System.out.println("   - k充足：可以连接成一堆");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 资源分配优化");
        System.out.println("   - 网络连接问题");
        System.out.println("   - 聚类算法");
        System.out.println("   - 区间合并问题");
    }
}
