package recursion;

import org.junit.Test;

/**
 * 
 * 不用递推，条件语句和乘除法与累加
 * @author outrun
 *
 */
public class SumExceptionMethod {
	
	private int n;
	private int[] array;
	
	
	public SumExceptionMethod() {
	}

	public SumExceptionMethod(int n) {
		super();
		this.n = n;
		array = new int[n + 1];
	}
	/**
	 * 时间复杂度为n，因为进行了n次递归调用
	 * 空间复杂度为n
	 *  堆是n, 栈是n
	 * 
	 * @param i
	 * @return
	 */
	public int sumMethod(int i){
		try {
			array[i] = array[i - 1] + i;
			int k = sumMethod(i + 1);
			return k;
		} catch (ArrayIndexOutOfBoundsException e) {
			return array[n];
		}
	}
	
	public static void main(String[] args) {
		int n = 100;
		SumExceptionMethod sem = new SumExceptionMethod(n);
		int sum = sem.sumMethod(1);
		System.out.println(sum);
	}

}
