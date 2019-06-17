package sort;

import java.util.Arrays;

import org.junit.Test;

public class ShellSort {
	
	public void swap (int[] arr, int posA, int posB) {
		int swap = arr[posA];
		arr[posA] = arr[posB];
		arr[posB] = swap;
	}

	public void sort(int[] arr) {
		int len = arr.length;
		for (int gap = len / 2; gap > 0; gap /= 2) {
			for (int i = 0; i < gap; i++) {
				for (int j = i + gap; j < len; j += gap) {
					int k = j - gap;
					if (arr[j] < arr[k]) {
						int temp = arr[j];
						while (k >= 0 && arr[k] > temp) {
							arr[k + gap] = arr[k];
							k -= gap;
						}
						arr[k + gap] = temp;
					}
				}
			}
		}
	}

	@Test
	public void testSort() {
		int[] arr = new int[] { 49, 38, 65, 97, 26, 13, 27, 49, 55, 4, 0 };
		sort(arr);
		System.out.println(Arrays.toString(arr));
	}

	public void sort2(int[] arr) {
		int len = arr.length;
		for (int gap = len / 2; gap > 0; gap /= 2) {
			for (int j = gap; j < len; j++) {
				int k = j - gap;
				if (arr[j] < arr[k]) {
					int temp = arr[j];
					while (k >= 0 && arr[k] > temp) {
						arr[k + gap] = arr[k];
						k -= gap;
					}
					arr[k + gap] = temp;
				}
			}
		}
	}

	@Test
	public void testSort2() {
		int[] arr = new int[] { 49, 38, 65, 97, 26, 13, 27, 49, 55, 4, 0 };
		sort2(arr);
		System.out.println(Arrays.toString(arr));
	}
	
	public void sort3(int[] arr) {
		int len = arr.length;
		for(int gap = len / 2; gap > 0; gap /= 2) {
			for(int i = gap; i < len; i++) {
				for (int j = i - gap; j >= 0 && arr[j] > arr[j + gap]; j -= gap) {
					swap(arr, j, j + gap);
				}
			}
		}
	}
	
	@Test
	public void testSort3() {
		int[] arr = new int[] { 49, 38, 65, 97, 26, 13, 27, 49, 55, 4, 0 };
		sort3(arr);
		System.out.println(Arrays.toString(arr));
	}
}
