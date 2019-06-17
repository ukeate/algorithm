package sort;

import java.util.Arrays;

import org.junit.Test;

public class QuickSort {

	public void arrSwap(int[] arr, int posA, int posB) {
		int tmp = arr[posA];
		arr[posA] = arr[posB];
		arr[posB] = tmp;
	}

	public int partition(int[] arr, int start, int end) {
		int pivotPos = start;
		int pivot = arr[start];
		for (int i = start; i <= end; i++) {
			if (arr[i] < pivot) {
				pivotPos++;
				arrSwap(arr, pivotPos, i);
			}
		}
		arrSwap(arr, start, pivotPos);
		return pivotPos;
	};

	public void quicksort(int[] arr, int start, int end) {
		if (start < end) {
			int pivotPos = partition(arr, start, end);
			quicksort(arr, start, pivotPos - 1);
			quicksort(arr, pivotPos + 1, end);
		}
	}

	@Test
	public void testQuicksort() {
		int[] arr = new int[] { 8, 2, 4, 65, 2, 4, 7, 1, 9, 0, 2, 34, 12 };
		quicksort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	public void sort2(int[] arr, int l, int r) {
		if (l < r) {
			int i = l, j = r, x = arr[l];

			while (i < j) {
				while (i < j && arr[j] >= x) {
					j--;
				}
				if (i < j) {
					arr[i] = arr[j];
					i++;
				}
				while (i < j && arr[i] < x) {
					i++;
				}
				if (i < j) {
					arr[j] = arr[i];
					j--;
				}
			}

			arr[i] = x;
			sort2(arr, l, i - 1);
			sort2(arr, i + 1, r);
		}
	}
	
	@Test
	public void testSort2() {
		int[] arr = new int[] { 8, 2, 4, 65, 2, 4, 7, 1, 9, 0, 2, 34, 12 };
		sort2(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}
}
