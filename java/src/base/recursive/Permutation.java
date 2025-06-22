package base.recursive;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串全排列问题 - 递归算法实现
 * 
 * 问题描述：
 * 给定一个字符串，打印出该字符串中字符的所有排列。
 * 例如：输入字符串"abc"，输出"abc"、"acb"、"bac"、"bca"、"cab"、"cba"
 * 
 * 算法思路：
 * 使用递归的回溯法（Backtracking）：
 * 1. 固定第一个位置的字符，求剩余字符的全排列
 * 2. 将第一个位置与后面每个位置的字符交换，递归求解
 * 3. 递归返回后，恢复原来的状态（回溯）
 * 
 * 递归过程：
 * - 对于位置i，尝试将字符串中i及之后的每个字符放到位置i
 * - 交换字符，递归处理位置i+1
 * - 递归返回后，交换回来（恢复现场）
 * 
 * 数学分析：
 * - n个不同字符的全排列数为n!
 * - 时间复杂度：O(n! × n)，生成n!个排列，每个排列需要O(n)时间
 * - 空间复杂度：O(n)，递归调用栈的深度
 * 
 * 应用场景：
 * - 密码破解中的暴力枚举
 * - 组合优化问题的搜索空间构建
 * - 游戏中的状态空间搜索
 * - 算法竞赛中的枚举类问题
 */
public class Permutation {
    private static void f1(ArrayList<Character> rest, String path, List<String> ans) {
        if (rest.isEmpty()) {
            ans.add(path);
        } else {
            for (int i = 0; i < rest.size(); i++) {
                char c = rest.get(i);
                rest.remove(i);
                f1(rest, path + c, ans);
                rest.add(i, c);
            }
        }
    }

    public static List<String> permutation1(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        ArrayList<Character> rest = new ArrayList<>();
        for (char cha : str) {
            rest.add(cha);
        }
        String path = "";
        f1(rest, path, ans);
        return ans;
    }

    //

    private static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }

    private static void f2(char[] str, int idx, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(str));
        } else {
            for (int i = idx; i < str.length; i++) {
                swap(str, idx, i);
                f2(str, idx + 1, ans);
                swap(str, idx, i);
            }
        }
    }

    public static List<String> permutation2(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        f2(str, 0, ans);
        return ans;
    }

    //

    private static void f3(char[] str, int idx, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(str));
        } else {
            boolean[] visited = new boolean[256];
            for (int i = idx; i < str.length; i++) {
                if (!visited[str[i]]) {
                    visited[str[i]] = true;
                    swap(str, idx, i);
                    f3(str, idx + 1, ans);
                    swap(str, idx, i);
                }
            }
        }
    }
    public static List<String> permutation3(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        f3(str, 0, ans);
        return ans;
    }

    /**
     * 字符串全排列主函数
     * 
     * @param str 待排列的字符串
     * @return 包含所有排列的列表
     */
    public static ArrayList<String> permutation(String str) {
        ArrayList<String> res = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return res;
        }
        char[] chs = str.toCharArray();
        process(chs, 0, res);
        return res;
    }

    /**
     * 递归生成排列的核心函数
     * 
     * 算法步骤：
     * 1. 如果已经处理到最后一个位置，说明形成了一个完整的排列
     * 2. 否则，尝试将当前位置与后面每个位置的字符交换
     * 3. 交换后递归处理下一个位置
     * 4. 递归返回后交换回来（回溯）
     * 
     * 关键思想：
     * - 位置i表示当前正在确定的位置
     * - 对于位置i，我们从[i, n-1]范围内选择一个字符放在位置i
     * - 通过交换来实现"选择"，通过再次交换来实现"撤销选择"
     * 
     * @param str 字符数组（工作数组）
     * @param i 当前处理的位置索引
     * @param res 存储结果的列表
     */
    public static void process(char[] str, int i, ArrayList<String> res) {
        // 递归边界：已经处理完所有位置
        if (i == str.length) {
            // 形成了一个完整的排列，加入结果集
            res.add(String.valueOf(str));
            return;
        }
        
        // 递归处理：尝试在位置i放置不同的字符
        for (int j = i; j < str.length; j++) {
            // 步骤1：选择 - 将位置j的字符交换到位置i
            swap(str, i, j);
            
            // 步骤2：递归 - 处理下一个位置
            process(str, i + 1, res);
            
            // 步骤3：撤销选择（回溯）- 恢复原来的状态
            swap(str, i, j);
        }
    }

    /**
     * 去重版本的字符串全排列
     * 
     * 当字符串中有重复字符时，上述算法会产生重复的排列
     * 这个版本通过排序+剪枝来去除重复排列
     * 
     * @param str 待排列的字符串
     * @return 包含所有不重复排列的列表
     */
    public static ArrayList<String> permutationUnique(String str) {
        ArrayList<String> res = new ArrayList<>();
        if (str == null || str.length() == 0) {
            return res;
        }
        char[] chs = str.toCharArray();
        java.util.Arrays.sort(chs); // 先排序，便于去重
        boolean[] used = new boolean[chs.length];
        processUnique(chs, 0, new StringBuilder(), used, res);
        return res;
    }

    /**
     * 去重版本的递归函数
     * 
     * 剪枝策略：
     * 1. 如果当前字符已经被使用，跳过
     * 2. 如果当前字符与前一个字符相同，且前一个字符未被使用，跳过
     *    这样确保相同字符的使用顺序是固定的，避免重复
     * 
     * @param str 字符数组
     * @param depth 当前递归深度
     * @param current 当前构建的排列
     * @param used 字符使用状态数组
     * @param res 结果列表
     */
    private static void processUnique(char[] str, int depth, StringBuilder current, 
                                    boolean[] used, ArrayList<String> res) {
        if (depth == str.length) {
            res.add(current.toString());
            return;
        }
        
        for (int i = 0; i < str.length; i++) {
            if (used[i]) continue; // 已经使用过，跳过
            
            // 去重剪枝：如果当前字符与前一个字符相同，且前一个字符未被使用
            if (i > 0 && str[i] == str[i - 1] && !used[i - 1]) {
                continue;
            }
            
            // 选择
            used[i] = true;
            current.append(str[i]);
            
            // 递归
            processUnique(str, depth + 1, current, used, res);
            
            // 撤销选择（回溯）
            current.deleteCharAt(current.length() - 1);
            used[i] = false;
        }
    }

    /**
     * 测试方法 - 演示字符串全排列算法
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串全排列算法演示 ===");
        
        // 测试无重复字符的情况
        String str1 = "abc";
        System.out.println("1. 无重复字符测试：\"" + str1 + "\"");
        ArrayList<String> result1 = permutation(str1);
        System.out.println("排列数量：" + result1.size() + "（期望：" + factorial(str1.length()) + "）");
        System.out.println("所有排列：" + result1);
        
        // 测试有重复字符的情况
        String str2 = "aab";
        System.out.println("\n2. 有重复字符测试：\"" + str2 + "\"");
        ArrayList<String> result2 = permutation(str2);
        System.out.println("排列数量（可能有重复）：" + result2.size());
        System.out.println("所有排列：" + result2);
        
        // 测试去重版本
        ArrayList<String> result2Unique = permutationUnique(str2);
        System.out.println("去重后排列数量：" + result2Unique.size());
        System.out.println("去重后排列：" + result2Unique);
        
        // 测试单字符
        String str3 = "a";
        System.out.println("\n3. 单字符测试：\"" + str3 + "\"");
        ArrayList<String> result3 = permutation(str3);
        System.out.println("排列：" + result3);
        
        // 测试空字符串
        String str4 = "";
        System.out.println("\n4. 空字符串测试：\"" + str4 + "\"");
        ArrayList<String> result4 = permutation(str4);
        System.out.println("排列：" + result4);
        
        // 性能测试
        System.out.println("\n=== 性能分析 ===");
        System.out.println("字符数\t排列数\t\t时间复杂度");
        for (int n = 1; n <= 8; n++) {
            long count = factorial(n);
            String complexity = "O(" + count + " × " + n + ")";
            System.out.printf("%d\t%,d\t\t%s\n", n, count, 
                n <= 5 ? "快速" : (n <= 7 ? "中等" : "较慢"));
        }
        
        System.out.println("\n注意：全排列是指数级复杂度，不适合大规模数据");
    }

    /**
     * 计算阶乘
     * 
     * @param n 输入数字
     * @return n的阶乘
     */
    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
