package sort;

import java.util.Arrays;

import org.junit.Test;

public class SelectSort {

	public void swap(int[] arr, int posA, int posB) {
		int swap = arr[posA];
		arr[posA] = arr[posB];
		arr[posB] = swap;
	}

	public void sort(int[] arr) {
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			int ind = i;
			for (int j = i + 1; j < len; j++) {
				if (arr[j] < arr[ind]) {
					ind = j;
				}
			}
			swap(arr, i, ind);
		}
	}
	
	@Test
	public void testSort() {
		int[] arr = new int[] {3,1,4,2};
		sort(arr);
		System.out.println(Arrays.toString(arr));
	}
}
