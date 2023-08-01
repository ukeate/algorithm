package leetc.top;

public class P33_SearchInRotatedSortedArray {
    public static int search(int[] arr, int num) {
        int l = 0, r = arr.length - 1, mid = 0;
        while (l <= r) {
            mid = (l + r) / 2;
            if (arr[mid] == num) {
                return mid;
            }
            if (arr[l] == arr[mid] && arr[mid] == arr[r]) {
                while (l != mid && arr[l] == arr[mid]) {
                    l++;
                }
                if (l == mid) {
                    l = mid + 1;
                    continue;
                }
            }
            if (arr[l] != arr[mid]) {
                if (arr[mid] > arr[l]) {
                    if (num >= arr[l] && num < arr[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                } else {
                    if (num > arr[mid] && num <= arr[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                }
            } else {
                // arr[low] == arr[mid] && arr[mid] != arr[high]
                if (arr[mid] < arr[r]) {
                    if (num > arr[mid] && num <= arr[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                } else {
                    if (num >= arr[l] && num < arr[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                }
            }
        }
        return -1;
    }
}
