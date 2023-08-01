package leetc.top;

import java.util.HashSet;

public class P217_ContainsDuplicate {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void heapify(int[] arr, int idx, int heapSize) {
        int left = idx * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[idx] ? largest : idx;
            if (largest == idx) {
                break;
            }
            swap(arr, largest, idx);
            idx = largest;
            left = idx * 2 + 1;
        }
    }

    private static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int heapSize = arr.length;
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, heapSize);
        }
        swap(arr, 0, --heapSize);
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
        }
    }

    public boolean containsDuplicate1(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        heapSort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;
            }
        }
        return false;
    }

    //

    public boolean containsDuplicate2(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }

}
