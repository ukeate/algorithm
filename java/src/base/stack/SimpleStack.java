package base.stack;

/**
 * 简单栈实现 - 基于数组的栈数据结构
 * 
 * 栈（Stack）是一种后进先出（LIFO - Last In First Out）的线性数据结构
 * 
 * 基本特性：
 * 1. 只允许在栈顶进行插入和删除操作
 * 2. 遵循后进先出原则
 * 3. 主要操作：push（入栈）、pop（出栈）、peek（查看栈顶）
 * 
 * 生活例子：
 * - 洗盘子：最后洗的盘子放在最上面，取的时候也是从最上面取
 * - 浏览器历史：后访问的页面先返回
 * - 程序调用栈：后调用的函数先返回
 * 
 * 应用场景：
 * - 表达式求值和语法分析
 * - 函数调用和递归实现
 * - 深度优先搜索（DFS）
 * - 撤销操作（Undo）
 * - 括号匹配检查
 * 
 * 时间复杂度：
 * - push：O(1)
 * - pop：O(1)
 * - peek：O(1)
 * - isEmpty：O(1)
 * 
 * 空间复杂度：O(n) - n为栈中元素个数
 */
public class SimpleStack {
    private int[] stack;    // 存储栈元素的数组
    private int limit;      // 栈的容量限制
    private int index;      // 栈顶指针，指向下一个要插入的位置

    /**
     * 构造函数 - 创建指定容量的栈
     * 
     * @param limit 栈的最大容量
     */
    public SimpleStack(int limit) {
        this.stack = new int[limit];    // 创建指定大小的数组
        this.limit = limit;             // 设置容量限制
        this.index = 0;                 // 初始时栈为空，栈顶指针指向0
    }

    /**
     * 入栈操作 - 将元素压入栈顶
     * 
     * 算法步骤：
     * 1. 检查栈是否已满
     * 2. 如果未满，将元素放入index位置
     * 3. 栈顶指针index向上移动一位
     * 
     * @param value 要入栈的元素
     * @throws RuntimeException 当栈已满时抛出异常
     */
    public void push(int value) {
        if (index == limit) {
            // 栈已满，无法继续入栈
            throw new RuntimeException("栈已满，无法入栈元素：" + value);
        }
        
        // 将元素放入栈顶位置，然后移动栈顶指针
        stack[index++] = value;
    }

    /**
     * 出栈操作 - 从栈顶移除并返回元素
     * 
     * 算法步骤：
     * 1. 检查栈是否为空
     * 2. 如果非空，栈顶指针向下移动一位
     * 3. 返回原栈顶位置的元素
     * 
     * @return 栈顶元素
     * @throws RuntimeException 当栈为空时抛出异常
     */
    public int pop() {
        if (index == 0) {
            // 栈为空，无法出栈
            throw new RuntimeException("栈为空，无法执行出栈操作");
        }
        
        // 栈顶指针向下移动，返回原栈顶元素
        return stack[--index];
    }

    /**
     * 查看栈顶元素 - 不移除栈顶元素，只返回其值
     * 
     * @return 栈顶元素的值
     * @throws RuntimeException 当栈为空时抛出异常
     */
    public int peek() {
        if (index == 0) {
            // 栈为空，无法查看栈顶
            throw new RuntimeException("栈为空，无法查看栈顶元素");
        }
        
        // 返回栈顶元素（index-1位置）
        return stack[index - 1];
    }

    /**
     * 检查栈是否为空
     * 
     * @return 如果栈为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return index == 0;
    }

    /**
     * 获取栈中元素的个数
     * 
     * @return 栈中元素个数
     */
    public int size() {
        return index;
    }

    /**
     * 获取栈的容量
     * 
     * @return 栈的最大容量
     */
    public int getCapacity() {
        return limit;
    }

    /**
     * 测试方法 - 演示栈的基本操作
     */
    public static void main(String[] args) {
        System.out.println("=== 简单栈实现测试 ===");
        
        // 创建一个容量为5的栈
        SimpleStack stack = new SimpleStack(5);
        
        System.out.println("1. 创建栈，容量：" + stack.getCapacity());
        System.out.println("   初始状态 - 栈是否为空：" + stack.isEmpty());
        System.out.println("   栈大小：" + stack.size());
        
        // 测试入栈操作
        System.out.println("\n2. 入栈操作：");
        try {
            for (int i = 1; i <= 3; i++) {
                stack.push(i * 10);
                System.out.println("   入栈 " + (i * 10) + "，栈大小：" + stack.size());
                System.out.println("   栈顶元素：" + stack.peek());
            }
        } catch (RuntimeException e) {
            System.out.println("   入栈失败：" + e.getMessage());
        }
        
        // 测试出栈操作
        System.out.println("\n3. 出栈操作：");
        try {
            while (!stack.isEmpty()) {
                int value = stack.pop();
                System.out.println("   出栈 " + value + "，栈大小：" + stack.size());
                if (!stack.isEmpty()) {
                    System.out.println("   当前栈顶：" + stack.peek());
                }
            }
        } catch (RuntimeException e) {
            System.out.println("   出栈失败：" + e.getMessage());
        }
        
        // 测试边界情况
        System.out.println("\n4. 边界情况测试：");
        
        // 空栈出栈
        try {
            stack.pop();
        } catch (RuntimeException e) {
            System.out.println("   空栈出栈异常：" + e.getMessage());
        }
        
        // 空栈查看栈顶
        try {
            stack.peek();
        } catch (RuntimeException e) {
            System.out.println("   空栈查看栈顶异常：" + e.getMessage());
        }
        
        // 栈满时入栈
        try {
            for (int i = 1; i <= 6; i++) {  // 超过容量5
                stack.push(i);
            }
        } catch (RuntimeException e) {
            System.out.println("   栈满入栈异常：" + e.getMessage());
        }
        
        // 栈的实际应用示例
        System.out.println("\n5. 应用示例 - 括号匹配：");
        testBracketMatching("()()");
        testBracketMatching("(())");
        testBracketMatching("(()");
        testBracketMatching("())");
        
        System.out.println("\n=== 栈的优势和应用 ===");
        System.out.println("优势：");
        System.out.println("- 所有基本操作都是O(1)时间复杂度");
        System.out.println("- 实现简单，内存使用效率高");
        System.out.println("- 天然支持LIFO语义");
        
        System.out.println("\n经典应用：");
        System.out.println("- 递归算法的迭代实现");
        System.out.println("- 表达式求值（中缀转后缀）");
        System.out.println("- 深度优先搜索（DFS）");
        System.out.println("- 程序调用栈管理");
        System.out.println("- 浏览器前进后退功能");
    }

    /**
     * 括号匹配示例 - 展示栈在实际问题中的应用
     * 
     * @param s 包含括号的字符串
     */
    private static void testBracketMatching(String s) {
        SimpleStack stack = new SimpleStack(s.length());
        boolean isValid = true;
        
        try {
            for (char c : s.toCharArray()) {
                if (c == '(') {
                    stack.push(1);  // 左括号入栈
                } else if (c == ')') {
                    if (stack.isEmpty()) {
                        isValid = false;
                        break;
                    }
                    stack.pop();    // 右括号时出栈
                }
            }
            
            if (!stack.isEmpty()) {
                isValid = false;
            }
        } catch (RuntimeException e) {
            isValid = false;
        }
        
        System.out.println("   \"" + s + "\" -> " + (isValid ? "匹配" : "不匹配"));
    }
}
