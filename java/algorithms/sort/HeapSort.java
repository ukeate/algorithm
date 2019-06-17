package sort;

import java.util.Arrays;

import org.junit.Test;

/**
 * (i - 1) / 2 j * 2 + 1, j * 2 + 2
 * 
 * @author outrun
 *
 */
public class HeapSort {

	public void swap(int[] arr, int posA, int posB) {
		int swap = arr[posA];
		arr[posA] = arr[posB];
		arr[posB] = swap;
	}

	public int child1Pos(int pos) {
		return pos * 2 + 1;
	}

	public int parentPos(int pos) {
		return (int) Math.floor((pos - 1) / 2);
	}

	public void fixDown(int[] arr, int pos, int len) {
		int swap = arr[pos];
		int childPos = child1Pos(pos);
		while (childPos < len) {
			if (childPos + 1 < len && arr[childPos + 1] > arr[childPos]) {
				childPos++;
			}
			if (arr[childPos] <= swap) {
				break;
			} else {
				arr[pos] = arr[childPos];
				pos = childPos;
				childPos = child1Pos(pos);
			}
		}
		arr[pos] = swap;
	}

	public void buildHeap(int[] arr) {
		int lastParentPos = parentPos(arr.length - 1);
		for (int i = lastParentPos; i >= 0; i--) {
			fixDown(arr, i, arr.length);
		}
	}

	public void sort(int[] arr) {
		buildHeap(arr);
		for (int i = arr.length - 1; i >= 1; i--) {
			swap(arr, 0, i);
			fixDown(arr, 0, i);
		}
	}

	@Test
	public void testSort() {
		int[] arr = new int[] { 3, 1, 5, 7, 2, 4, 9, 6, 10, 8, 33, 2, 21, 2, 15, 22, 77, 11, 0, -1, 23345, 12 };
		sort(arr);
		System.out.println(Arrays.toString(arr));
	}

	public void fixUp(int[] arr, int childPos) {
		int swap = arr[childPos];
		int pos = parentPos(childPos);
		while (pos >= 0 && childPos != 0) {
			if (arr[pos] >= swap) {
				break;
			} else {
				arr[childPos] = arr[pos];
				childPos = pos;
				pos = parentPos(childPos);
			}
		}
		arr[childPos] = swap;
	}
}
