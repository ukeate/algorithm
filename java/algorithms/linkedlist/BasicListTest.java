package linkedlist;

import org.junit.Test;

public class BasicListTest {
    @Test
    public void testHere() {
//		Integer[] array = {1,2,3};
//		BasicList<Integer> list = new BasicList<Integer>(array);
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{0, 1, 2, 3});
//		list.insert(1, 1);
//		list.remove(3);
        list.reverse2();
        list.print();
//		list.printInverse2();
//		System.out.println(list.max());

    }

    @Test
    public void testFind() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6});
        System.out.println(list.findReverse2(4).value);
    }

    @Test
    public void testRemove() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 4, 4, 5, 5, 6});
//		list.removeVal(1);
        list.sortedListRemoveDuplicatedVal2();
        list.print();
    }

    @Test
    public void testReverse() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        list.reverseBetween(3, 5);
        list.print();
    }

    @Test
    public void testRotate() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        list.rotateRight(2);
        list.print();
    }

    @Test
    public void testPalindrome() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 3, 2, 1});
        System.out.println(list.isPalindrome());
    }

    @Test
    public void testSwapPairs() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        list.swapPairs();
        list.print();
    }

    @Test
    public void testPartition() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 2, 1, 6, 7});
        list.partition(3);
        list.print();
    }

    @Test
    public void testReorder() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        list.reorder();
        list.print();
    }

    @Test
    public void testJosephus() {
        BasicList<Integer> list = new BasicList<Integer>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        list.josephusCircle(3, 4);
    }

    @Test
    public void testIntersection() {
        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8}, arr2 = {9, 10};
        Node[] heads = BasicList.array2Intersection(arr1, arr2, 5);
//        BasicList.print(heads[0]);
//        BasicList.print(heads[1]);
        System.out.println(BasicList.getIntersectionNodeBruteForce(heads[0], heads[1]));
        System.out.println(BasicList.getIntersectionNodeHash(heads[0], heads[1]));
        System.out.println(BasicList.getIntersection(heads[0], heads[1]));
    }
    @Test
    public void testCircle() {
        Node list = BasicList.arr2cycle(new int[] {1, 2, 3, 4, 5, 6}, 3);
        System.out.println(BasicList.hasCycle(list));
        System.out.println(BasicList.detectCycleNode(list).value);
    }
    @Test
    public void testFindDuplicated() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 5, 7, 8};
        BasicList<Integer> list = new BasicList<Integer>(arr);
        System.out.println(list.findDuplicateValueHash().value);
        System.out.println(arr[BasicList.findDuplicateValueCircle(arr)]);
    }
}