package giant.c2;

import java.util.HashMap;

/**
 * 有序消息接收系统
 * 
 * 问题描述：
 * 设计一个消息接收系统，消息带有序号，需要按序号顺序打印消息。
 * 消息可能乱序到达，系统需要缓存乱序消息，等到能够连续打印时再输出。
 * 
 * 核心功能：
 * 1. 接收带序号的消息
 * 2. 自动检测是否可以连续打印消息
 * 3. 一旦发现连续序列，立即打印并清除缓存
 * 
 * 数据结构设计：
 * - headMap: 存储每个连续段的头节点
 * - tailMap: 存储每个连续段的尾节点  
 * - waitPoint: 下一个期待的消息序号
 * 
 * 算法核心思想：
 * 使用链表将连续的消息段连接起来，通过头尾指针快速合并相邻段，
 * 当某个段的起始序号等于waitPoint时立即打印。
 * 
 * 时间复杂度：O(1)每次接收操作，O(k)打印操作（k为连续消息数）
 * 空间复杂度：O(n)，n为缓存的消息数量
 * 
 * @author algorithm learning
 */
public class ReceiveOrderLine {
    
    /**
     * 消息节点类：链表结构存储消息内容
     */
    private static class Node {
        public String info;  // 消息内容
        public Node next;    // 指向下一个消息节点
        
        public Node(String str) {
            info = str;
        }
    }

    /**
     * 消息接收盒子：核心的消息管理系统
     */
    public static class MessageBox {
        private HashMap<Integer, Node> headMap;  // 每个连续段的头节点映射
        private HashMap<Integer, Node> tailMap;  // 每个连续段的尾节点映射  
        private int waitPoint;                   // 下一个期待打印的消息序号
        
        /**
         * 构造函数：初始化消息接收系统
         */
        public MessageBox() {
            headMap = new HashMap<>();
            tailMap = new HashMap<>();
            waitPoint = 1;  // 从序号1开始期待消息
        }
        
        /**
         * 打印连续的消息序列
         * 从waitPoint开始打印，直到序列中断
         */
        private void print() {
            Node node = headMap.get(waitPoint);  // 获取当前期待序号的头节点
            headMap.remove(waitPoint);           // 从头节点映射中移除
            
            // 遍历链表，连续打印消息
            while (node != null) {
                System.out.print(node.info + " ");
                node = node.next;
                waitPoint++;  // 更新下一个期待的序号
            }
            
            // 清理尾节点映射（因为整个段已经打印完毕）
            tailMap.remove(waitPoint - 1);
            System.out.println();  // 打印完成后换行
        }

        /**
         * 接收消息的核心方法
         * 
         * 算法流程：
         * 1. 创建新消息节点
         * 2. 检查是否可以与前一个段合并（序号为num-1）
         * 3. 检查是否可以与后一个段合并（序号为num+1）
         * 4. 如果新消息的序号等于期待序号，立即触发打印
         * 
         * @param num 消息序号
         * @param info 消息内容
         */
        public void receive(int num, String info) {
            if (num < 1) {
                return;  // 序号必须为正数
            }
            
            // 创建新的消息节点
            Node cur = new Node(info);
            headMap.put(num, cur);  // 暂时将新节点作为一个独立段的头尾
            tailMap.put(num, cur);
            
            // 尝试与前一个段合并（检查序号num-1是否存在）
            if (tailMap.containsKey(num - 1)) {
                // 将前一段的尾节点连接到当前节点
                tailMap.get(num - 1).next = cur;
                tailMap.remove(num - 1);  // 移除前一段的尾节点映射
                headMap.remove(num);      // 当前节点不再是头节点
            }
            
            // 尝试与后一个段合并（检查序号num+1是否存在）
            if (headMap.containsKey(num + 1)) {
                // 将当前节点连接到后一段的头节点
                cur.next = headMap.get(num + 1);
                tailMap.remove(num);      // 当前节点不再是尾节点
                headMap.remove(num + 1);  // 后一段的头节点映射失效
            }
            
            // 检查是否可以触发打印（新形成的段起始序号等于期待序号）
            if (num == waitPoint) {
                print();
            }
        }
    }

    /**
     * 测试方法：演示消息接收系统的工作流程
     */
    public static void main(String[] args) {
        System.out.println("=== 有序消息接收系统演示 ===");
        
        MessageBox box = new MessageBox();
        
        System.out.println("接收消息 2:B");
        box.receive(2,"B");  // 缓存消息2，等待消息1
        
        System.out.println("接收消息 1:A");  
        box.receive(1,"A");  // 触发打印：1 2 -> "A B"
        
        System.out.println("接收消息 4:D");
        box.receive(4,"D");  // 缓存消息4，等待消息3
        
        System.out.println("接收消息 5:E");
        box.receive(5,"E");  // 与消息4合并：4-5段
        
        System.out.println("接收消息 7:G");
        box.receive(7,"G");  // 缓存消息7，独立段
        
        System.out.println("接收消息 8:H");
        box.receive(8,"H");  // 与消息7合并：7-8段
        
        System.out.println("接收消息 6:F");
        box.receive(6,"F");  // 连接4-5段和7-8段：4-5-6-7-8段
        
        System.out.println("接收消息 3:C");
        box.receive(3,"C");  // 触发打印：3 4 5 6 7 8 -> "C D E F G H"
        
        System.out.println("接收消息 9:I");
        box.receive(9,"I");  // 触发打印：9 -> "I"
        
        System.out.println("接收消息 10:J");
        box.receive(10,"J"); // 触发打印：10 -> "J"
        
        System.out.println("接收消息 12:L");
        box.receive(12,"L"); // 缓存消息12，等待消息11
        
        System.out.println("接收消息 13:M");
        box.receive(13,"M"); // 与消息12合并：12-13段
        
        System.out.println("接收消息 11:K");
        box.receive(11,"K"); // 触发打印：11 12 13 -> "K L M"
        
        System.out.println("\n=== 演示完成 ===");
        System.out.println("核心特点：");
        System.out.println("1. 消息乱序到达时自动缓存");
        System.out.println("2. 形成连续序列时立即打印");
        System.out.println("3. 使用链表和哈希表实现O(1)合并操作");
    }
}
