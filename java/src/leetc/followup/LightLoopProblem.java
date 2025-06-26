package leetc.followup;

/**
 * 环形开关灯问题 (Circular Light Switch Problem)
 * 
 * 问题描述：
 * 有一排灯泡排成环形，初始状态为给定数组（0表示关闭，1表示开启）
 * 每次按下开关会改变该位置及其相邻两个位置的灯泡状态
 * 由于是环形排列，第一个和最后一个灯泡相邻
 * 
 * 目标：使用最少的按键次数让所有灯泡都亮起（状态为1）
 * 
 * 与直线排列的区别：
 * - 直线排列：两端的灯泡只有一个邻居
 * - 环形排列：每个灯泡都有两个邻居（首尾相连）
 * 
 * 解法思路：
 * 贪心算法 + 状态推导：
 * 1. 关键观察：每个开关按两次等于没按，所以每个开关最多按一次
 * 2. 环形排列的特点：第0个开关会影响第n-1、0、1位置的灯泡
 * 3. 需要考虑第0个开关的按与不按两种情况
 * 4. 对于每种情况，从左到右贪心决策其他开关
 * 
 * 算法步骤：
 * 1. 分别尝试按下和不按下第0个开关
 * 2. 对于每种情况，从位置1开始贪心：
 *    - 如果前一个灯泡是暗的，必须按下当前开关来点亮它
 *    - 否则不按当前开关
 * 3. 最后检查所有灯泡是否都亮起
 * 4. 返回两种情况中的最小按键次数
 * 
 * 时间复杂度：O(n) - 需要尝试两种情况，每种情况线性扫描
 * 空间复杂度：O(1) - 只需要常数额外空间
 */
public class LightLoopProblem {

    /**
     * 检查是否所有灯泡都亮起
     * 
     * @param arr 灯泡状态数组
     * @return 所有灯泡都亮起返回true，否则返回false
     */
    private static boolean valid(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取位置i的前一个位置（环形数组）
     * 
     * @param i 当前位置
     * @param n 数组大小
     * @return 前一个位置的索引
     */
    private static int lastIdx(int i, int n) {
        return i == 0 ? (n - 1) : (i - 1);
    }

    /**
     * 获取位置i的后一个位置（环形数组）
     * 
     * @param i 当前位置
     * @param n 数组大小
     * @return 后一个位置的索引
     */
    private static int nextIdx(int i, int n) {
        return i == n - 1 ? 0 : (i + 1);
    }

    /**
     * 按下第i位置的开关，改变前、当前、后三个位置的灯泡状态（环形）
     * 
     * @param arr 灯泡状态数组
     * @param i 开关位置
     */
    private static void change(int[] arr, int i) {
        arr[lastIdx(i, arr.length)] ^= 1;  // 前一个位置
        arr[i] ^= 1;                       // 当前位置
        arr[nextIdx(i, arr.length)] ^= 1;  // 后一个位置
    }

    /**
     * 方法1：递归回溯算法，尝试每个开关按或不按
     * 
     * @param arr 当前灯泡状态
     * @param i 当前处理的开关位置
     * @return 从位置i开始的最少按键次数，无法实现返回Integer.MAX_VALUE
     */
    private static int process1(int[] arr, int i) {
        if (i == arr.length) {
            return valid(arr) ? 0 : Integer.MAX_VALUE;
        }
        int p1 = process1(arr, i + 1);
        change(arr, i);
        int p2 = process1(arr, i + 1);
        change(arr, i);
        p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
        return Math.min(p1, p2);
    }

    /**
     * 方法1：递归回溯算法的入口函数
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数，无法实现返回Integer.MAX_VALUE
     */
    public static int loop1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        return process1(arr, 0);
    }

    /**
     * 方法2：优化递归算法，从位置2开始贪心决策
     * 
     * 算法思路：
     * 环形开关灯的核心难点是处理首尾相连的约束条件。
     * 由于第0个开关会影响第n-1、0、1位置，形成了循环依赖。
     * 
     * 解决方案：
     * 1. 预先决定前两个开关（0和1）的状态，共4种组合
     * 2. 从位置2开始，使用贪心策略：如果前前个灯泡是暗的，必须按下前一个开关
     * 3. 最后检查末尾的约束条件是否满足
     * 
     * @param arr 原始灯泡数组
     * @param idx 当前处理的位置
     * @param prepre 前前个位置的灯泡状态
     * @param pre 前一个位置的灯泡状态
     * @param end 最后一个灯泡的状态
     * @param first 第一个灯泡的状态
     * @return 从当前位置开始的最少按键次数
     */
    private static int process2(int[] arr, int idx, int prepre, int pre, int end, int first) {
        if (idx == arr.length) {
            // 处理到末尾，检查环形约束：
            // end表示最后一个灯泡状态，它会被最后一个开关影响
            // first表示第一个灯泡状态，它会被第0个开关影响
            // prepre表示倒数第二个灯泡状态
            // 按下最后一个开关的决策：是否需要按来点亮prepre
            return (end != first || end != prepre) ? Integer.MAX_VALUE : (end ^ 1);
        }
        if (idx < arr.length - 1) {
            // 处理中间位置：使用标准贪心策略
            if (prepre == 0) {
                // 前前个灯泡是暗的，必须按下前一个开关来点亮它
                int next = process2(arr, idx + 1, pre ^ 1, arr[idx] ^ 1, end, first);
                return next == Integer.MAX_VALUE ? next : (next + 1);
            } else {
                // 前前个灯泡已经亮起，不需要按前一个开关
                return process2(arr, idx + 1, pre, arr[idx], end, first);
            }
        } else {
            // 处理倒数第二个位置：需要考虑对末尾灯泡的影响
            if (prepre == 0) {
                // 按下当前开关，会影响end（最后一个灯泡）
                int next = process2(arr, idx + 1, pre ^ 1, end ^ 1, end ^ 1, first);
                return next == Integer.MAX_VALUE ? next : (next + 1);
            } else {
                return process2(arr, idx + 1, pre, end, end, first);
            }
        }
    }

    /**
     * 方法2：优化递归算法的入口函数
     * 
     * 枚举前两个开关的4种可能状态：
     * 1. 都不按：(0不变，1不变)
     * 2. 只按0：(0变，1不变) 
     * 3. 只按1：(0不变，1变)
     * 4. 都按：(0变，1变)
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数
     */
    public static int loop2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        if (arr.length == 3) {
            return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        
        // 枚举前两个开关的4种状态
        // 情况1：0不变，1不变（都不按）
        int p1 = process2(arr, 3, arr[1], arr[2], arr[arr.length - 1], arr[0]);
        
        // 情况2：0变，1不变（只按第0个开关）
        // 按第0个开关会影响：arr[n-1] ^= 1, arr[0] ^= 1, arr[1] ^= 1
        int p2 = process2(arr, 3, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
        
        // 情况3：0不变，1变（只按第1个开关）
        // 按第1个开关会影响：arr[0] ^= 1, arr[1] ^= 1, arr[2] ^= 1
        int p3 = process2(arr, 3, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
        
        // 情况4：0变，1变（都按）
        // 按第0和第1个开关的组合效果
        int p4 = process2(arr, 3, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
        
        // 计算每种情况的总按键次数
        p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;   // 按了第0个开关，+1
        p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;   // 按了第1个开关，+1
        p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;   // 按了两个开关，+2
        
        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
    }

    /**
     * 方法3：空间优化的迭代算法，将递归改为循环
     * 
     * 这是方法2的迭代优化版本，避免了递归调用的开销
     * 
     * @param arr 原始灯泡数组
     * @param i 开始处理的位置
     * @param prepre 前前个位置的灯泡状态
     * @param pre 前一个位置的灯泡状态
     * @param end 最后一个灯泡的状态
     * @param first 第一个灯泡的状态
     * @return 最少按键次数
     */
    private static int process3(int[] arr, int i, int prepre, int pre, int end, int first) {
        int ans = 0; // 记录按键次数
        
        // 从左到右迭代处理中间部分
        while (i < arr.length - 1) {
            if (prepre == 0) {
                // 前前个灯泡是暗的，必须按下前一个开关
                ans++;
                prepre = pre ^ 1;        // 更新前前个状态
                pre = (arr[i++] ^ 1);    // 更新前一个状态并移动到下一位置
            } else {
                // 前前个灯泡已经亮起，不需要按开关
                prepre = pre;
                pre = arr[i++];
            }
        }
        
        // 处理倒数第二个位置（i == arr.length - 1）
        if (prepre == 0) {
            // 需要按下当前开关，这会影响最后一个灯泡
            ans++;
            prepre = pre ^ 1;
            end ^= 1;    // 最后一个灯泡状态改变
            pre = end;
        } else {
            prepre = pre;
            pre = end;
        }
        
        // 检查最终约束条件并决定最后一个开关
        return (end != first || end != prepre) ? Integer.MAX_VALUE : (ans + (end ^ 1));
    }

    /**
     * 方法3：空间优化的迭代算法入口函数
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数
     */
    public static int loop3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        if (arr.length == 3) {
            return (arr[0] != arr[1] || arr[0] != arr[2]) ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        
        // 枚举前两个开关的4种状态，使用迭代算法
        int p1 = process3(arr, 3, arr[1], arr[2], arr[arr.length - 1], arr[0]);
        int p2 = process3(arr, 3, arr[1] ^ 1, arr[2], arr[arr.length - 1] ^ 1, arr[0] ^ 1);
        int p3 = process3(arr, 3, arr[1] ^ 1, arr[2] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
        int p4 = process3(arr, 3, arr[1], arr[2] ^ 1, arr[arr.length - 1] ^ 1, arr[0]);
        
        p2 = p2 != Integer.MAX_VALUE ? (p2 + 1) : p2;
        p3 = p3 != Integer.MAX_VALUE ? (p3 + 1) : p3;
        p4 = p4 != Integer.MAX_VALUE ? (p4 + 2) : p4;
        
        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
    }

    /**
     * 生成随机灯泡状态数组用于测试
     * 
     * @param len 数组长度
     * @return 随机生成的灯泡状态数组
     */
    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 2);
        }
        return arr;
    }

    /**
     * 测试方法：验证三种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 环形开关灯问题测试 ===");
        System.out.println("test begin");
        
        int times = 20000;
        int maxLen = 12;
        boolean allCorrect = true;
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArr(len);
            int ans1 = loop1(arr);
            int ans2 = loop2(arr);
            int ans3 = loop3(arr);
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("发现错误! 测试用例: " + i);
                System.out.println("方法1: " + ans1 + ", 方法2: " + ans2 + ", 方法3: " + ans3);
                allCorrect = false;
                break;
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        }
        System.out.println("test end");

        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int len = 100000000;
        int[] arr = randomArr(len);
        
        long start = System.currentTimeMillis();
        loop3(arr);
        long end = System.currentTimeMillis();
        System.out.println("大规模数据（" + len + "个灯泡）耗时: " + (end - start) + "ms");
        
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("1. 环形约束使问题复杂度增加");
        System.out.println("2. 需要枚举前两个开关的状态来破解循环依赖");
        System.out.println("3. 方法1: O(2^n) - 递归枚举所有可能");
        System.out.println("4. 方法2: O(n) - 优化递归，预处理+贪心");
        System.out.println("5. 方法3: O(n) - 空间优化的迭代版本");
        
        System.out.println("\n=== 测试完成 ===");
    }

}
