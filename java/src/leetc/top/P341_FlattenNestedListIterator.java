package leetc.top;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * LeetCode 341. 扁平化嵌套列表迭代器 (Flatten Nested List Iterator)
 * 
 * 问题描述：
 * 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；
 * 该列表的元素也可能是整数或者是其他列表。请你实现一个迭代器将其扁平化，
 * 使之能够遍历这个列表中的所有整数。
 * 
 * 实现扁平迭代器类 NestedIterator ：
 * - NestedIterator(List<NestedInteger> nestedList) 用嵌套列表初始化迭代器。
 * - int next() 返回嵌套列表的下一个整数。
 * - boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
 * 
 * 示例：
 * 输入：nestedList = [[1,1],2,[1,1]]
 * 输出：[1,1,2,1,1]
 * 解释：通过重复调用 next 直到 hasNext 返回 false，next 返回的元素的顺序应该是: [1,1,2,1,1]。
 * 
 * 输入：nestedList = [1,[4,[6]]]
 * 输出：[1,4,6]
 * 解释：通过重复调用 next 直到 hasNext 返回 false，next 返回的元素的顺序应该是: [1,4,6]。
 * 
 * 解法思路：
 * 栈 + 迭代器设计：
 * 
 * 1. 核心思想：
 *    - 使用栈来处理嵌套结构，栈中存储待处理的 NestedInteger
 *    - 当遇到列表时，将其展开并压入栈中
 *    - 当遇到整数时，直接返回
 * 
 * 2. 设计策略：
 *    - 延迟处理：在 hasNext() 时才真正处理嵌套结构
 *    - 栈存储：使用栈保存待处理的元素，支持深度优先遍历
 *    - 逆序压栈：保证左到右的遍历顺序
 * 
 * 3. 算法步骤：
 *    - 初始化：将 nestedList 逆序压入栈
 *    - hasNext()：检查并处理栈顶元素，确保栈顶是整数或栈为空
 *    - next()：弹出并返回栈顶整数
 * 
 * 4. 关键操作：
 *    - 展开列表：将列表中的元素逆序压入栈
 *    - 懒加载：只在需要时才展开嵌套结构
 *    - 状态维护：确保迭代器的一致性
 * 
 * 核心思想：
 * - 栈结构：利用栈的后进先出特性处理嵌套
 * - 深度优先：优先处理深层的嵌套结构
 * - 迭代器模式：提供统一的遍历接口
 * 
 * 关键技巧：
 * - 逆序处理：保证正确的遍历顺序
 * - 延迟展开：避免一次性展开所有内容
 * - 状态检查：在 hasNext 中处理复杂逻辑
 * 
 * 时间复杂度：
 * - hasNext(): O(1) 均摊 - 每个元素最多被处理一次
 * - next(): O(1) - 直接弹栈操作
 * 
 * 空间复杂度：O(d) - d为嵌套的最大深度
 * 
 * LeetCode链接：https://leetcode.com/problems/flatten-nested-list-iterator/
 */
public class P341_FlattenNestedListIterator {
    
    /**
     * 嵌套整数接口
     * 这是题目给定的接口，表示可能是整数或列表的嵌套结构
     */
    public interface NestedInteger {
        /**
         * @return 如果当前对象是整数则返回true，如果是列表则返回false
         */
        public boolean isInteger();

        /**
         * @return 如果当前对象是整数，返回该整数；否则返回null
         */
        public Integer getInteger();

        /**
         * @return 如果当前对象是列表，返回该列表；否则返回null
         */
        public List<NestedInteger> getList();
    }
    
    /**
     * 嵌套列表迭代器实现
     */
    public class NestedIterator implements Iterator<Integer> {
        
        private Stack<NestedInteger> stack; // 栈：存储待处理的嵌套整数

        /**
         * 构造函数：初始化迭代器
         * 
         * 将嵌套列表的元素逆序压入栈中，这样可以保证
         * 从左到右的遍历顺序（栈是后进先出）
         * 
         * @param nestedList 嵌套整数列表
         */
        public NestedIterator(List<NestedInteger> nestedList) {
            stack = new Stack<>();
            
            // 逆序压入栈，保证正确的遍历顺序
            for (int i = nestedList.size() - 1; i >= 0; i--) {
                stack.push(nestedList.get(i));
            }
        }

        /**
         * 返回下一个整数
         * 
         * 调用此方法前应先调用 hasNext() 确保存在下一个元素
         * 
         * @return 下一个整数值
         */
        @Override
        public Integer next() {
            // 在调用 next() 之前应该先调用 hasNext()，
            // 这样可以确保栈顶是一个整数
            if (hasNext()) {
                return stack.pop().getInteger();
            }
            return null; // 理论上不会到达这里
        }

        /**
         * 检查是否还有下一个整数
         * 
         * 这个方法承担了主要的处理逻辑：
         * 1. 如果栈顶是整数，直接返回 true
         * 2. 如果栈顶是列表，将其展开并继续检查
         * 3. 如果栈为空，返回 false
         * 
         * @return 是否还有下一个整数
         */
        @Override
        public boolean hasNext() {
            // 循环处理栈顶元素，直到找到整数或栈为空
            while (!stack.isEmpty()) {
                NestedInteger top = stack.peek();
                
                if (top.isInteger()) {
                    // 栈顶是整数，存在下一个元素
                    return true;
                } else {
                    // 栈顶是列表，需要展开
                    stack.pop(); // 弹出当前列表
                    List<NestedInteger> list = top.getList();
                    
                    // 将列表中的元素逆序压入栈
                    // 逆序是为了保证从左到右的遍历顺序
                    for (int i = list.size() - 1; i >= 0; i--) {
                        stack.push(list.get(i));
                    }
                }
            }
            
            // 栈为空，没有更多元素
            return false;
        }
    }
    
    /*
     * 使用示例：
     * 
     * List<NestedInteger> nestedList = ...;
     * NestedIterator iterator = new NestedIterator(nestedList);
     * 
     * while (iterator.hasNext()) {
     *     int value = iterator.next();
     *     System.out.println(value);
     * }
     */
}
