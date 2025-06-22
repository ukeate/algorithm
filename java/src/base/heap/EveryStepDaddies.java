package base.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 每一步的得奖名单问题 - 动态维护Top K客户
 * 
 * 问题描述：
 * 实时维护购买次数最多的前K名客户（得奖名单）
 * 每次有客户购买或退款时，都要更新得奖名单
 * 
 * 算法核心思想：
 * 1. 使用两个堆来维护客户：候选区堆和得奖区堆
 * 2. 得奖区：购买次数高的客户，按购买次数从小到大排序（小根堆）
 * 3. 候选区：未得奖的客户，按购买次数从大到小排序（大根堆）
 * 4. 当得奖区不满时，从候选区提升客户到得奖区
 * 5. 当得奖区满时，比较候选区堆顶和得奖区堆顶，可能发生交换
 * 
 * 时间复杂度：每次操作O(logK)，总体O(N*logK)
 * 空间复杂度：O(N)
 */
public class EveryStepDaddies {
    /**
     * 客户类 - 封装客户信息
     */
    private static class Customer {
        public int id;          // 客户ID
        public int buy;         // 购买次数
        public int enterTime;   // 进入当前状态的时间

        public Customer(int id, int buy, int time) {
            this.id = id;
            this.buy = buy;
            this.enterTime = time;
        }
    }

    /**
     * 候选区比较器 - 购买次数越多越优先，相同时间越早越优先
     * 用于候选区的大根堆排序
     */
    private static class CComp implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o2.buy - o1.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    /**
     * 得奖区比较器 - 购买次数越少越优先，相同时间越早越优先
     * 用于得奖区的小根堆排序
     */
    public static class DComp implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o1.buy - o2.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    /**
     * 动态维护Top K客户的核心类
     */
    public static class WhosYourDaddy {
        private HashMap<Integer, Customer> customers;    // 所有客户信息
        private HeapGreater<Customer> candHeap;          // 候选区堆（大根堆）
        private HeapGreater<Customer> daddyHeap;         // 得奖区堆（小根堆）
        private final int daddyLimit;                    // 得奖名单容量限制

        public WhosYourDaddy(int limit) {
            customers = new HashMap<>();
            candHeap = new HeapGreater<>(new CComp());
            daddyHeap = new HeapGreater<>(new DComp());
            this.daddyLimit = limit;
        }

        /**
         * 尝试从候选区提升客户到得奖区
         * 
         * 提升规则：
         * 1. 如果得奖区未满，直接提升候选区堆顶
         * 2. 如果得奖区已满，比较候选区堆顶和得奖区堆顶：
         *    - 候选区堆顶购买次数更多，则交换两者位置
         *    - 否则不变
         * 
         * @param time 当前时间
         */
        private void daddyMove(int time) {
            if (candHeap.isEmpty()) {
                return;
            }
            if (daddyHeap.size() < daddyLimit) {
                // 得奖区未满，直接提升
                Customer p = candHeap.pop();
                p.enterTime = time;
                daddyHeap.push(p);
            } else {
                // 得奖区已满，比较是否需要交换
                if (candHeap.peek().buy > daddyHeap.peek().buy) {
                    Customer oldDaddy = daddyHeap.pop();
                    Customer newDaddy = candHeap.pop();
                    oldDaddy.enterTime = time;
                    newDaddy.enterTime = time;
                    daddyHeap.push(newDaddy);
                    candHeap.push(oldDaddy);
                }
            }
        }

        /**
         * 处理客户购买或退款操作
         * 
         * @param time 操作时间
         * @param id 客户ID
         * @param buyOrRefund true表示购买，false表示退款
         */
        public void operate(int time, int id, boolean buyOrRefund) {
            // 新用户退款，直接忽略
            if (!buyOrRefund && !customers.containsKey(id)) {
                return;
            }
            // 新用户，创建客户记录
            if (!customers.containsKey(id)) {
                customers.put(id, new Customer(id, 0, 0));
            }
            Customer c = customers.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            // 购买次数为0，移除客户
            if (c.buy == 0) {
                customers.remove(id);
            }
            // 新入选的客户
            if (!candHeap.contains(c) && !daddyHeap.contains(c)) {
                if (daddyHeap.size() < daddyLimit) {
                    c.enterTime = time;
                    daddyHeap.push(c);
                } else {
                    c.enterTime = time;
                    candHeap.push(c);
                }
            } else if (candHeap.contains(c)) {
                // 客户在候选区
                if (c.buy == 0) {
                    candHeap.remove(c);
                } else {
                    candHeap.resign(c);  // 重新调整堆位置
                }
            } else {
                // 客户在得奖区
                if (c.buy == 0) {
                    daddyHeap.remove(c);
                } else {
                    daddyHeap.resign(c);  // 重新调整堆位置
                }
            }
            daddyMove(time);  // 尝试调整得奖区和候选区
        }

        /**
         * 获取当前得奖名单
         * @return 得奖客户ID列表
         */
        public List<Integer> getDaddies() {
            List<Customer> customers = daddyHeap.getAllElements();
            List<Integer> ans = new ArrayList<>();
            for (Customer c : customers) {
                ans.add(c.id);
            }
            return ans;
        }
    }

    /**
     * 主要算法接口 - 返回每一步的得奖名单
     * 
     * @param arr 客户ID数组
     * @param op 操作数组，true表示购买，false表示退款
     * @param k 得奖名单大小
     * @return 每一步的得奖名单列表
     */
    public static List<List<Integer>> topK(int[] arr, boolean[] op, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        WhosYourDaddy whoDaddies = new WhosYourDaddy(k);
        for (int i = 0; i < arr.length; i++) {
            whoDaddies.operate(i, arr[i], op[i]);
            ans.add(whoDaddies.getDaddies());
        }
        return ans;
    }

    /**
     * 获取当前得奖名单（辅助方法）
     */
    private static List<Integer> getCurAns(ArrayList<Customer> daddy) {
        List<Integer> ans = new ArrayList<>();
        for (Customer c : daddy) {
            ans.add(c.id);
        }
        return ans;
    }

    /**
     * 清理购买次数为0的客户（辅助方法）
     */
    private static void cleanZeroBuy(ArrayList<Customer> arr) {
        List<Customer> noZero = new ArrayList<>();
        for (Customer c : arr) {
            if (c.buy != 0) {
                noZero.add(c);
            }
        }
        arr.clear();
        for (Customer c : noZero) {
            arr.add(c);
        }
    }

    /**
     * 尝试从候选区移动客户到得奖区（辅助方法）
     */
    private static void moveDaddies(ArrayList<Customer> cands, ArrayList<Customer> daddies, int k, int time) {
        if (cands.isEmpty()) {
            return;
        }
        if (daddies.size() < k) {
            Customer c = cands.get(0);
            c.enterTime = time;
            daddies.add(c);
            cands.remove(0);
        } else {
            if (cands.get(0).buy > daddies.get(0).buy) {
                Customer oldDaddy = daddies.get(0);
                daddies.remove(0);
                Customer newDaddy = cands.get(0);
                cands.remove(0);
                newDaddy.enterTime = time;
                oldDaddy.enterTime = time;
                daddies.add(newDaddy);
                cands.add(oldDaddy);
            }
        }
    }

    /**
     * 暴力解法 - 用于验证正确性
     * 每一步都重新排序计算得奖名单
     * 
     * 时间复杂度：O(N^2 * logN)
     * 
     * @param arr 客户ID数组
     * @param op 操作数组
     * @param k 得奖名单大小
     * @return 每一步的得奖名单列表
     */
    private static List<List<Integer>> topKSure(int[] arr, boolean[] op, int k) {
        HashMap<Integer, Customer> customers = new HashMap<>();
        ArrayList<Customer> cands = new ArrayList<>();
        ArrayList<Customer> daddies = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        for (int time = 0; time < arr.length; time++) {
            int id = arr[time];
            boolean buyOrRefund = op[time];
            if (!buyOrRefund && !customers.containsKey(id)) {
                ans.add(getCurAns(daddies));
                continue;
            }
            if (!customers.containsKey(id)) {
                customers.put(id, new Customer(id, 0, 0));
            }
            Customer c = customers.get(id);
            if (buyOrRefund) {
                c.buy++;
            } else {
                c.buy--;
            }
            if (c.buy == 0) {
                customers.remove(id);
            }
            if (!cands.contains(c) && !daddies.contains(c)) {
                if (daddies.size() < k) {
                    c.enterTime = time;
                    daddies.add(c);
                } else {
                    c.enterTime = time;
                    cands.add(c);
                }
            }
            cleanZeroBuy(cands);
            cleanZeroBuy(daddies);
            cands.sort(new CComp());
            daddies.sort(new DComp());
            moveDaddies(cands, daddies, k, time);
            ans.add(getCurAns(daddies));
        }
        return ans;
    }

    /**
     * 测试数据生成类
     */
    private static class Data {
        public int[] arr;      // 客户ID数组
        public boolean[] op;   // 操作数组

        public Data(int[] a, boolean[] o) {
            arr = a;
            op = o;
        }
    }

    /**
     * 生成随机测试数据
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 最大客户ID值
     * @return 随机测试数据
     */
    public static Data randomData(int maxLen, int maxVal) {
        int len = (int) ((maxLen + 1) * Math.random());
        int[] arr = new int[len];
        boolean[] op = new boolean[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (maxVal + 1));
            op[i] = Math.random() < 0.5 ? true : false;
        }
        return new Data(arr, op);
    }

    /**
     * 判断两个结果是否相等
     * 
     * @param ans1 结果1
     * @param ans2 结果2
     * @return 是否相等
     */
    public static boolean isEqual(List<List<Integer>> ans1, List<List<Integer>> ans2) {
        if (ans1.size() != ans2.size()) {
            return false;
        }
        for (int i = 0; i < ans1.size(); i++) {
            List<Integer> l1 = ans1.get(i);
            List<Integer> l2 = ans2.get(i);
            if (l1.size() != l2.size()) {
                return false;
            }
            l1.sort((a, b) -> a - b);
            l2.sort((a, b) -> a - b);
            for (int j = 0; j < l1.size(); j++) {
                if (!l1.get(j).equals(l2.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxVal = 10;
        int maxLen = 100;
        int maxK = 6;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Data data = randomData(maxLen, maxVal);
            int k = (int) ((maxK + 1) * Math.random()) + 1;
            int[] arr = data.arr;
            boolean[] op = data.op;
            List<List<Integer>> ans1 = topK(arr, op, k);
            List<List<Integer>> ans2 = topKSure(arr, op, k);
            if (!isEqual(ans1, ans2)) {
                System.out.println("Wrong");
                for (int j = 0; j < arr.length; j++) {
                    System.out.println(arr[j] + "," + op[j]);
                }
                System.out.println(k);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
