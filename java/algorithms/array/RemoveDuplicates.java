package array;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * 有序数组去重 指针和临时变量是解决数组问题的两大法宝
 * 
 * @author outrun
 *
 */
public class RemoveDuplicates {

	/**
	 * 时间复杂度为O(n), 空间复杂度为O(n)
	 * 
	 * @param nums
	 * @return
	 */
	public int remove01(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;
		else if (nums.length == 1)
			return 1;
		else {
			int end = nums.length - 1;
			ArrayList<Integer> list = new ArrayList<Integer>();
			int i = 0;
			while (i <= end) {
				if (i == end) {
					list.add(nums[i]);
					i++;
				} else {
					int j = i + 1;
					if (nums[i] == nums[j]) {
						while (j <= end && nums[i] == nums[j]) {
							j++;
						}
					}
					list.add(nums[i]);
					i = j;
				}
			}
			for (i = 0; i < list.size(); i++) {
				nums[i] = list.get(i);
			}
			return list.size();
		}
	}

	@Test
	public void testRemove01() {
		int[] nums = { 1, 2, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8 };
		System.out.println(remove01(nums));
		System.out.println(Arrays.toString(nums));
	}

	/**
	 * 
	 * 时间复杂度为n^2, 空间复杂度为n arraycopy 中创建并遍历数组
	 * 
	 * @param nums
	 * @return
	 */
	public int remove02(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;
		else if (nums.length == 1)
			return 1;
		else {
			int end = nums.length - 1;
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i <= end; i++)
				if (i < end) {
					int j = i + 1;
					if (nums[i] == nums[j]) {
						while (j <= end && nums[i] == nums[j]) {
							j++;
						}
					}
					// 是native方法，执行速度快于自己实现，能AC
					System.arraycopy(nums, j, nums, i + 1, end - j + 1);
					end -= j - i - 1;
				}
			return end + 1;
		}
	}

	@Test
	public void testRemove02() {
		int[] nums = { 1, 2, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8 };
		System.out.println(remove02(nums));
		System.out.println(Arrays.toString(nums));
	}

	public void myArrayCopy(int[] array1, int s1, int[] array2, int s2, int len) {
		int[] array = new int[len];
		for (int i = 0; i < len; i++) {
			array[i] = array1[s1 + i];
		}
		for (int i = 0; i < len; i++) {
			array2[s2 + i] = array[i];
		}
	}

	/**
	 * 时间复杂度为n, 空间复杂度为1
	 * 
	 * @param nums
	 * @return
	 */
	public int remove03(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;
		else if (nums.length == 1)
			return 1;
		else {
			int tmp = nums[0], len = 1;
			for (int i = 1; i < nums.length; i++) {
				if (nums[i] != tmp) {
					nums[len] = nums[i];
					tmp = nums[i];
					len++;
				}
			}
			return len;
		}
	}

	@Test
	public void testRemove03() {
		int[] nums = { 1, 2, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8 };
		System.out.println(remove03(nums));
		System.out.println(Arrays.toString(nums));
	}
}
