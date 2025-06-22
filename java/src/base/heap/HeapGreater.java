package base.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 增强堆实现 - 支持随意修改和删除元素的堆
 * 
 * 相比于普通堆的增强功能：
 * 1. 支持修改堆中任意元素的值（resign操作）
 * 2. 支持删除堆中任意元素（remove操作）
 * 3. 支持检查元素是否在堆中（contains操作）
 * 4. 支持获取所有元素
 * 
 * 实现原理：
 * - 使用HashMap维护元素到索引的映射关系
 * - 每次堆调整后同步更新HashMap中的索引
 * - 通过索引映射实现O(1)的定位和O(log n)的调整
 * 
 * 适用场景：
 * - 需要频繁修改堆中元素的场景
 * - 需要删除堆中任意元素的场景
 * - 如Dijkstra算法、任务调度等
 * 
 * @param <T> 堆中元素的类型
 */
public class HeapGreater<T> {
    private ArrayList<T> heap;              // 堆数组，存储堆中的元素
    private HashMap<T, Integer> indexMap;   // 元素到索引的映射，支持O(1)定位
    private int heapSize;                   // 堆的当前大小
    private Comparator<? super T> comp;     // 比较器，定义堆的排序规则

    /**
     * 构造函数
     * @param c 比较器，定义元素的比较规则
     */
    public HeapGreater(Comparator<T> c) {
        heap = new ArrayList<>();
        indexMap = new HashMap<>();
        heapSize = 0;
        comp = c;
    }

    /**
     * 交换堆中两个位置的元素
     * 同时更新HashMap中的索引映射关系
     * 
     * @param i 位置i
     * @param j 位置j
     */
    private void swap(int i, int j) {
        T o1 = heap.get(i);
        T o2 = heap.get(j);
        heap.set(i, o2);
        heap.set(j, o1);
        // 关键：同步更新索引映射
        indexMap.put(o2, i);
        indexMap.put(o1, j);
    }

    /**
     * 堆插入过程（上浮操作）
     * 将指定位置的元素向上调整，直到满足堆性质
     * 
     * @param i 需要上浮的元素索引
     */
    private void heapInsert(int i) {
        // 与父节点比较，如果满足比较器条件则交换
        while (comp.compare(heap.get(i), heap.get((i - 1) / 2)) < 0) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    /**
     * 堆化过程（下沉操作）
     * 将指定位置的元素向下调整，直到满足堆性质
     * 
     * @param i 需要下沉的元素索引
     */
    private void heapify(int i) {
        int left = i * 2 + 1;   // 左子节点索引
        while (left < heapSize) {
            // 找到子节点中最优的（根据比较器）
            int best = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? (left + 1) : left;
            // 找到父子节点中最优的
            best = comp.compare(heap.get(best), heap.get(i)) < 0 ? best : i;
            // 如果父节点就是最优的，则已满足堆性质
            if (best == i) {
                break;
            }
            swap(best, i);
            i = best;
            left = i * 2 + 1;
        }
    }

    /**
     * 重新调整元素位置（核心增强功能）
     * 当堆中某个元素的值发生变化时，重新调整其在堆中的位置
     * 
     * 调整策略：
     * 1. 先尝试上浮（heapInsert）
     * 2. 再尝试下沉（heapify）
     * 3. 只有一个方向会真正执行调整
     * 
     * @param o 值发生变化的元素对象
     */
    public void resign(T o) {
        heapInsert(indexMap.get(o));
        heapify(indexMap.get(o));
    }

    /**
     * 向堆中添加元素
     * 
     * @param obj 要添加的元素
     */
    public void push(T obj) {
        heap.add(obj);
        indexMap.put(obj, heapSize);    // 建立元素到索引的映射
        heapInsert(heapSize++);         // 插入后执行上浮调整
    }

    /**
     * 弹出堆顶元素
     * 
     * @return 堆顶元素（优先级最高的元素）
     */
    public T pop() {
        T ans = heap.get(0);
        swap(0, heapSize - 1);          // 将堆顶与最后一个元素交换
        indexMap.remove(ans);           // 移除返回元素的索引映射
        heap.remove(--heapSize);        // 删除最后一个元素并减少堆大小
        heapify(0);                     // 对新的堆顶执行下沉调整
        return ans;
    }

    /**
     * 删除堆中指定元素（核心增强功能）
     * 
     * 删除策略：
     * 1. 将要删除的元素与最后一个元素交换
     * 2. 删除最后一个元素
     * 3. 对交换到删除位置的元素进行堆调整
     * 
     * @param o 要删除的元素
     */
    public void remove(T o) {
        T replace = heap.get(heapSize - 1);     // 最后一个元素
        heap.remove(--heapSize);                // 删除最后一个元素
        int i = indexMap.get(o);                // 获取要删除元素的索引
        indexMap.remove(o);                     // 移除元素的索引映射
        
        // 如果删除的不是最后一个元素，需要调整替换元素的位置
        if (o != replace) {
            heap.set(i, replace);
            indexMap.put(replace, i);
            resign(replace);    // 重新调整替换元素的位置
        }
    }

    /**
     * 查看堆顶元素（不删除）
     * @return 堆顶元素
     */
    public T peek() {
        return heap.get(0);
    }

    /**
     * 判断堆是否为空
     * @return 堆为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     * 获取堆的大小
     * @return 堆中元素的数量
     */
    public int size() {
        return heapSize;
    }

    /**
     * 检查元素是否在堆中（增强功能）
     * 通过HashMap实现O(1)时间复杂度的查找
     * 
     * @param obj 要检查的元素
     * @return 元素在堆中返回true，否则返回false
     */
    public boolean contains(T obj) {
        return indexMap.containsKey(obj);
    }

    /**
     * 获取堆中所有元素的副本
     * 注意：返回的是副本，修改不会影响堆的结构
     * 
     * @return 包含所有堆元素的列表
     */
    public List<T> getAllElements() {
        List<T> ans = new ArrayList<>();
        for (T c : heap) {
            ans.add(c);
        }
        return ans;
    }
}
