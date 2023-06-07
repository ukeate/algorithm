package base.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EveryStepDaddies {
    private static class Customer {
        public int id;
        public int buy;
        public int enterTime;

        public Customer(int id, int buy, int time) {
            this.id = id;
            this.buy = buy;
            this.enterTime = time;
        }
    }

    private static class CComp implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o2.buy - o1.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    public static class DComp implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buy != o2.buy ? (o1.buy - o2.buy) : (o1.enterTime - o2.enterTime);
        }
    }

    public static class WhosYourDaddy {
        private HashMap<Integer, Customer> customers;
        private HeapGreater<Customer> candHeap;
        private HeapGreater<Customer> daddyHeap;
        private final int daddyLimit;

        public WhosYourDaddy(int limit) {
            customers = new HashMap<>();
            candHeap = new HeapGreater<>(new CComp());
            daddyHeap = new HeapGreater<>(new DComp());
            this.daddyLimit = limit;
        }

        private void daddyMove(int time) {
            if (candHeap.isEmpty()) {
                return;
            }
            if (daddyHeap.size() < daddyLimit) {
                Customer p = candHeap.pop();
                p.enterTime = time;
                daddyHeap.push(p);
            } else {
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

        public void operate(int time, int id, boolean buyOrRefund) {
            // 新用户退款
            if (!buyOrRefund && !customers.containsKey(id)) {
                return;
            }
            // 新用户
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
            // 新入选
            if (!candHeap.contains(c) && !daddyHeap.contains(c)) {
                if (daddyHeap.size() < daddyLimit) {
                    c.enterTime = time;
                    daddyHeap.push(c);
                } else {
                    c.enterTime = time;
                    candHeap.push(c);
                }
            } else if (candHeap.contains(c)) {
                // 在候选
                if (c.buy == 0) {
                    candHeap.remove(c);
                } else {
                    candHeap.resign(c);
                }
            } else {
                // 在daddy
                if (c.buy == 0) {
                    daddyHeap.remove(c);
                } else {
                    daddyHeap.resign(c);
                }
            }
            daddyMove(time);
        }

        public List<Integer> getDaddies() {
            List<Customer> customers = daddyHeap.getAllElements();
            List<Integer> ans = new ArrayList<>();
            for (Customer c : customers) {
                ans.add(c.id);
            }
            return ans;
        }
    }

    public static List<List<Integer>> topK(int[] arr, boolean[] op, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        WhosYourDaddy whoDaddies = new WhosYourDaddy(k);
        for (int i = 0; i < arr.length; i++) {
            whoDaddies.operate(i, arr[i], op[i]);
            ans.add(whoDaddies.getDaddies());
        }
        return ans;
    }

    private static List<Integer> getCurAns(ArrayList<Customer> daddy) {
        List<Integer> ans = new ArrayList<>();
        for (Customer c : daddy) {
            ans.add(c.id);
        }
        return ans;
    }

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

    //

    private static class Data {
        public int[] arr;
        public boolean[] op;

        public Data(int[] a, boolean[] o) {
            arr = a;
            op = o;
        }
    }

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
