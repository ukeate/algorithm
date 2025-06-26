package giant.c36;

/**
 * 括号表达式分值计算问题
 * 
 * 问题描述：
 * 来自美团算法题
 * 给定一个合法的括号字符串，按照特定规则计算其分值：
 * 
 * 包裹规则：每包裹一层，分数就是里面的分值+1
 * - () 分值为2
 * - (()) 分值为3  
 * - ((())) 分值为4
 * 
 * 连接规则：每连接一段，分数就是各部分相乘
 * - ()() 分值为2 * 2 = 4
 * - (())() 分值为3 * 2 = 6
 * 
 * 综合例子：
 * (()())()(()) = (2*2+1) * 2 * 3 = 5 * 2 * 3 = 30
 * 解析：
 * - (()()) = (2*2+1) = 5 (内部有两个()连接，然后外层包裹+1)
 * - () = 2
 * - (()) = 3
 * - 最终：5 * 2 * 3 = 30
 * 
 * 解决方案：
 * 使用递归解析，每层递归处理一个括号层级
 * 
 * 核心思想：
 * 1. 遇到'('，递归处理内部内容，返回值+1表示包裹效果
 * 2. 同层级的多个部分采用乘法连接
 * 3. 遇到')'，返回当前层级的计算结果
 * 
 * 算法复杂度：
 * 时间复杂度：O(N)，每个字符访问一次
 * 空间复杂度：O(D)，其中D是括号的最大嵌套深度
 */
public class ComputeExpressionValue {
    
    /**
     * 递归计算括号表达式的分值
     * 
     * 算法思路：
     * 1. 如果当前字符是')'，说明到达了当前层级的结束，返回基础分值1
     * 2. 否则处理当前层级的所有子表达式：
     *    - 遇到'('，递归计算子表达式，结果+1（包裹效果）
     *    - 将同层级的所有子表达式分值相乘（连接效果）
     * 
     * 递归结构说明：
     * - 每次递归调用处理一对匹配的括号
     * - 返回值包含：[计算结果, 处理结束的位置]
     * - 上层递归根据结束位置继续处理后续字符
     * 
     * 示例分析 "(()())":
     * 1. 外层调用：遇到'('，递归处理内部
     * 2. 内层递归：
     *    - 遇到'('，再次递归得到基础值1，+1=2
     *    - 遇到'('，再次递归得到基础值1，+1=2  
     *    - 同层连接：2 * 2 = 4
     *    - 遇到')'，返回[4, 位置]
     * 3. 外层收到返回值4，+1=5（包裹效果）
     * 
     * @param s 括号字符数组
     * @param i 当前处理位置
     * @return [计算结果, 处理结束位置]
     */
    private static int[] compute(char[] s, int i) {
        // 递归终止条件：遇到')'表示当前层级结束
        if (s[i] == ')') {
            return new int[]{1, i};  // 返回基础分值1和当前位置
        }
        
        int ans = 1;  // 当前层级的累积分值，初始为1（乘法单位元）
        
        // 处理当前层级的所有子表达式，直到遇到')'或字符串结束
        while (i < s.length && s[i] != ')') {
            // 遇到'('，递归处理子表达式
            int[] info = compute(s, i + 1);  // 跳过当前'('，从下一个字符开始
            
            // 子表达式的分值+1（包裹效果），然后与当前层级累积分值相乘（连接效果）
            ans *= info[0] + 1;
            
            // 更新位置到子表达式处理结束的下一个位置
            i = info[1] + 1;
        }
        
        return new int[]{ans, i};
    }

    /**
     * 计算括号表达式的总分值
     * 
     * @param s 括号字符串
     * @return 计算得到的分值
     */
    public static int sores(String s) {
        return compute(s.toCharArray(), 0)[0];
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 括号表达式分值计算问题测试 ===");
        
        // 测试用例1：基础包裹
        System.out.println("1. 基础包裹测试:");
        System.out.println("() = " + sores("()") + " (期望: 2)");
        System.out.println("(()) = " + sores("(())") + " (期望: 3)");
        System.out.println("((())) = " + sores("((())) ") + " (期望: 4)");
        System.out.println();
        
        // 测试用例2：基础连接
        System.out.println("2. 基础连接测试:");
        System.out.println("()() = " + sores("()()") + " (期望: 4, 计算: 2*2)");
        System.out.println("(())() = " + sores("(())()") + " (期望: 6, 计算: 3*2)");
        System.out.println("()()() = " + sores("()()()") + " (期望: 8, 计算: 2*2*2)");
        System.out.println();
        
        // 测试用例3：复合情况
        System.out.println("3. 复合情况测试:");
        String complex1 = "(()())";
        System.out.println(complex1 + " = " + sores(complex1) + " (期望: 5, 计算: (2*2)+1)");
        
        String complex2 = "(()())()(())";
        System.out.println(complex2 + " = " + sores(complex2) + " (期望: 30, 计算: 5*2*3)");
        System.out.println("详细计算:");
        System.out.println("  (()()) = (2*2)+1 = 5");
        System.out.println("  () = 2");
        System.out.println("  (()) = 2+1 = 3");
        System.out.println("  最终: 5*2*3 = 30");
        System.out.println();
        
        // 测试用例4：深度嵌套
        System.out.println("4. 深度嵌套测试:");
        String nested = "(((())))";
        System.out.println(nested + " = " + sores(nested) + " (期望: 5, 计算: ((1+1)+1)+1)+1)");
        System.out.println();
        
        // 测试用例5：复杂组合
        System.out.println("5. 复杂组合测试:");
        String complex3 = "(())(())()";
        System.out.println(complex3 + " = " + sores(complex3) + " (期望: 18, 计算: 3*3*2)");
        
        String complex4 = "((())())()";
        System.out.println(complex4 + " = " + sores(complex4) + " (期望: 10, 计算: ((3+1)*1+1)*2)");
        System.out.println("详细计算:");
        System.out.println("  内层: (()) = 3");
        System.out.println("  中层: ((())()) = (3+1)*1+1 = 5");
        System.out.println("  外层: ((())())() = 5*2 = 10");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
