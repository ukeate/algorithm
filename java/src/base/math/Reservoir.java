package base.math;

// 蓄水池, 在线等概率抽样
// bagN/allN概率进袋, 袋内等概率替代
public class Reservoir {
    public static class RandomBox {
        private int[] bag;
        private int n;
        private int count;

        public RandomBox(int capacity) {
            bag = new int[capacity];
            n = capacity;
            count = 0;
        }

        private int rand(int max) {
            return (int) (max * Math.random()) + 1;
        }

        public void add(int num) {
            count++;
            if (count <= n) {
                bag[count - 1] = num;
            } else {
                if (rand(count) <= n) {
                    bag[rand(n) - 1] = num;
                }
            }
        }

        public int[] choices() {
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[i] = bag[i];
            }
            return ans;
        }

    }

    private static int random(int i) {
        return (int) (Math.random() * i) + 1;
    }

    public static void main(String[] args) {
        int times = 100000;
        int allN = 155;
        int bagN = 10;
        int[] counts = new int[allN + 1];
        for (int i = 0; i < times; i++) {
            int[] bag = new int[bagN];
            int idx = 0;
            for (int num = 1; num <= allN; num++) {
                if (num <= 10) {
                    bag[idx++] = num;
                } else {
                    if (random(num) <= bagN) {
                        idx = (int) (Math.random() * bagN);
                        bag[idx] = num;
                    }
                }
            }
            for (int num : bag) {
                counts[num]++;
            }
        }
        for (int i = 0; i <= allN; i++) {
            System.out.println(counts[i]);
        }

        System.out.println("====");
        counts = new int[allN + 1];
        for (int i = 0; i < times; i++) {
            RandomBox box = new RandomBox(bagN);
            for (int num = 1; num <= allN; num++) {
                box.add(num);
            }
            int[] ans = box.choices();
            for (int j = 0; j < ans.length; j++) {
                counts[ans[j]]++;
            }
        }
        for (int i = 0; i < counts.length; i++) {
            System.out.println(counts[i]);
        }
    }
}
