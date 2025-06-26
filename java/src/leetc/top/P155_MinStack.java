package leetc.top;

import java.util.Stack;

/**
 * LeetCode 155. 最小栈 (Min Stack)
 * 
 * 问题描述：
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 * 
 * 实现 MinStack 类:
 * - MinStack() 初始化堆栈对象
 * - void push(int val) 将元素 val 推入堆栈
 * - void pop() 删除堆栈顶部的元素
 * - int top() 获取堆栈顶部的元素
 * - int getMin() 获取堆栈中的最小元素
 * 
 * 解法思路：
 * 辅助栈法：
 * 1. 使用两个栈：数据栈存储实际数据，最小栈存储最小值信息
 * 2. 数据栈正常进行push和pop操作
 * 3. 最小栈同步维护每个状态下的最小值
 * 4. 入栈时：最小栈压入当前最小值
 * 5. 出栈时：两个栈同步弹出
 * 
 * 最小栈的维护策略：
 * - push时：比较新元素与当前最小值，取较小者入栈
 * - pop时：直接弹出栈顶（因为同步维护）
 * - getMin时：直接返回最小栈的栈顶
 * 
 * 设计优势：
 * - 所有操作都是O(1)时间复杂度
 * - 不需要遍历查找最小值
 * - 空间换时间的经典应用
 * 
 * 关键思想：
 * - 每个位置都记录当前状态下的最小值
 * - 保证数据栈和最小栈的同步性
 * - 利用栈的后进先出特性维护历史最小值
 * 
 * 时间复杂度：所有操作都是O(1)
 * 空间复杂度：O(n) - 需要额外的最小栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/min-stack/
 */
public class P155_MinStack {
    private Stack<Integer> data;  // 数据栈，存储实际数据
    private Stack<Integer> min;   // 最小栈，存储每个状态的最小值
    
    /**
     * 初始化栈对象
     */
    public P155_MinStack() {
        data = new Stack<>();
        min = new Stack<>();
    }

    /**
     * 将元素推入栈顶
     * 
     * @param x 要推入的元素
     */
    public void push(int x) {
        data.push(x);  // 数据正常入栈
        
        if (min.isEmpty()) {
            // 最小栈为空，直接入栈
            min.push(x);
        } else {
            // 将当前最小值入栈（新元素与之前最小值的较小者）
            min.push(Math.min(min.peek(), x));
        }
    }

    /**
     * 删除栈顶元素
     */
    public void pop() {
        data.pop();  // 数据栈弹出
        min.pop();   // 最小栈同步弹出
    }
    
    /**
     * 获取栈顶元素
     * 
     * @return 栈顶元素的值
     */
    public int top() {
        return data.peek();
    }
    
    /**
     * 获取栈中的最小元素
     * 
     * @return 当前栈中的最小值
     */
    public int getMin() {
        return min.peek();  // 最小栈的栈顶就是当前最小值
    }
}
