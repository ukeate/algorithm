package leetc.followup;

/**
 * 开关灯问题 (Light Switch Problem)
 * 
 * 问题描述：
 * 有一排灯泡，初始状态为给定的数组（0表示关闭，1表示开启）
 * 每次可以按下一个开关，会改变该位置及其相邻位置的灯泡状态
 * - 按下第0个开关：改变第0和第1个灯泡
 * - 按下最后一个开关：改变倒数第2和最后一个灯泡  
 * - 按下中间开关i：改变第i-1、i、i+1个灯泡
 * 
 * 目标：使用最少的按键次数让所有灯泡都亮起（状态为1）
 * 
 * 解法思路：
 * 贪心算法 + 状态推导：
 * 1. 关键观察：每个开关按两次等于没按，所以每个开关最多按一次
 * 2. 从左到右贪心：如果当前灯泡是暗的，必须按下能影响它的开关
 * 3. 对于位置i的灯泡，只有开关i-1、i、i+1能影响它
 * 4. 当处理到位置i时，开关i-1已经决定了，所以只能决定是否按开关i
 * 
 * 算法实现：
 * - 方法1：递归回溯，尝试每个开关按或不按
 * - 方法2：贪心优化，从左到右决定每个开关
 * - 方法3：空间优化，只保存必要的状态信息
 * 
 * 时间复杂度：
 * - 方法1：O(2^n) - 每个开关两种选择
 * - 方法2/3：O(n) - 线性贪心算法
 * 
 * 空间复杂度：O(1) - 只需要常数额外空间
 */
public class LightProblem {
    /**
     * 检查是否所有灯泡都亮起（0表示关闭，1表示开启）
     * 
     * @param arr 灯泡状态数组
     * @return 所有灯泡都亮起返回true，否则返回false
     */
    private static boolean valid(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                return false; // 发现有灯泡未亮起
            }
        }
        return true; // 所有灯泡都亮起
    }

    /**
     * 按下第i位置的开关，改变相关灯泡的状态
     * 
     * @param arr 灯泡状态数组
     * @param i 开关位置
     */
    public static void change(int[] arr, int i) {
        if (i == 0) {
            // 按下第0个开关：影响第0和第1个灯泡
            arr[0] ^= 1;
            arr[1] ^= 1;
        } else if (i == arr.length - 1) {
            // 按下最后一个开关：影响倒数第2和最后一个灯泡
            arr[i - 1] ^= 1;
            arr[i] ^= 1;
        } else {
            // 按下中间的开关：影响前、当前、后三个灯泡
            arr[i - 1] ^= 1;
            arr[i] ^= 1;
            arr[i + 1] ^= 1;
        }
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
            // 所有开关都已决定，检查是否所有灯泡都亮起
            return valid(arr) ? 0 : Integer.MAX_VALUE;
        }
        
        // 选择1：不按第i位置的开关
        int p1 = process1(arr, i + 1);
        
        // 选择2：按下i位置的开关
        change(arr, i);                    // 按下开关
        int p2 = process1(arr, i + 1);
        change(arr, i);                    // 恢复现场
        
        // 如果按下开关能实现目标，按键次数+1
        p2 = (p2 == Integer.MAX_VALUE) ? p2 : (p2 + 1);
        
        return Math.min(p1, p2); // 返回最少按键次数
    }

    /**
     * 方法1：递归回溯算法的入口函数
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数，无法实现返回Integer.MAX_VALUE
     */
    public static int noLoop1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0; // 空数组无需操作
        }
        if (arr.length == 1) {
            return arr[0] ^ 1; // 只有1个灯泡：如果亮起返回0，否则返回1
        }
        if (arr.length == 2) {
            // 2个灯泡：如果状态不同无法实现，状态相同时看是否需要按键
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        return process1(arr, 0); // 3个及以上灯泡使用递归算法
    }

    //

    /**
     * 方法2：优化递归算法，从左到右贪心决策
     * 
     * @param arr 原始灯泡数组
     * @param idx 当前处理的位置
     * @param prepre 前前个位置的灯泡状态
     * @param pre 前一个位置的灯泡状态
     * @return 从当前位置开始的最少按键次数
     */
    private static int process2(int[] arr, int idx, int prepre, int pre) {
        if (idx == arr.length) {
            // 达到末尾，检查最后两个灯泡状态
            return prepre != pre ? Integer.MAX_VALUE : (pre ^ 1);
        }
        
        if (prepre == 0) {
            // 前前个灯泡是暗的，必须按下前一个开关来点亮它
            int next = process2(arr, idx + 1, pre ^ 1, arr[idx] ^ 1);
            return next == Integer.MAX_VALUE ? next : (next + 1);
        } else {
            // 前前个灯泡已经亮起，不需要按前一个开关
            return process2(arr, idx + 1, pre, arr[idx]);
        }
    }

    /**
     * 方法2：优化递归算法的入口函数
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数
     */
    public static int noLoop2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        
        // 尝试两种情况：第0个开关按或不按
        int p1 = process2(arr, 2, arr[0], arr[1]);                    // 不按第0个开关
        int p2 = process2(arr, 2, arr[0] ^ 1, arr[1] ^ 1);            // 按第0个开关
        if (p2 != Integer.MAX_VALUE) {
            p2++; // 如果按第0个开关可行，按键次数+1
        }
        return Math.min(p1, p2);
    }

    //

    /**
     * 方法3：空间优化的迭代算法，将递归改为循环
     * 
     * @param arr 原始灯泡数组
     * @param i 开始处理的位置
     * @param prepre 前前个位置的灯泡状态
     * @param pre 前一个位置的灯泡状态
     * @return 最少按键次数
     */
    public static int process3(int[] arr, int i, int prepre, int pre) {
        int ans = 0; // 记录按键次数
        
        // 从左到右迭代处理
        while (i < arr.length) {
            if (prepre == 0) {
                // 前前个灯泡是暗的，必须按下前一个开关
                ans++;
                prepre = pre ^ 1;      // 按下开关后前一个灯泡状态变化
                pre = arr[i++] ^ 1;    // 当前灯泡状态也变化
            } else {
                // 前前个灯泡已经亮起，不需要按开关
                prepre = pre;
                pre = arr[i++];
            }
        }
        
        // 检查最后两个灯泡状态和最后一个开关的需要
        return (prepre != pre) ? Integer.MAX_VALUE : (ans + (pre ^ 1));
    }


    /**
     * 方法3：空间优化的迭代算法入口函数
     * 
     * @param arr 初始灯泡状态数组
     * @return 最少按键次数
     */
    public static int noLoop3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0] == 1 ? 0 : 1;
        }
        if (arr.length == 2) {
            return arr[0] != arr[1] ? Integer.MAX_VALUE : (arr[0] ^ 1);
        }
        
        // 尝试两种情况：第0个开关按或不按
        int p1 = process3(arr, 2, arr[0], arr[1]);                    // 不按第0个开关
        int p2 = process3(arr, 2, arr[0] ^ 1, arr[1] ^ 1);            // 按第0个开关
        if (p2 != Integer.MAX_VALUE) {
            p2++; // 如果按第0个开关可行，按键次数+1
        }
        return Math.min(p1, p2);
    }

    //


    /**
     * 生成随机灯泡状态数组用于测试
     * 
     * @param len 数组长度
     * @return 随机生成的灯泡状态数组
     */
    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 2); // 随机生成0或1
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        int times = 20000;
        int maxLen = 12;
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArr(len);
            int ans1 = noLoop1(arr);
            int ans2 = noLoop2(arr);
            int ans3 = noLoop3(arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong1");
                break;
            }
        }
        System.out.println("test end");

        int len = 100000000;
        int[] arr = randomArr(len);
        long start = 0, end = 0;
        start = System.currentTimeMillis();
        noLoop3(arr);
        end = System.currentTimeMillis();
        System.out.println("noLoop3 run time" + (end - start) + " ms");
    }
}
