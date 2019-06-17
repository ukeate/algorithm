package recursion;

public class SumExceptionConstructor {
	
	public static int n;
	public static int[] array;
	/**
	 * 
	 * 时间复杂度为n
	 * 空间复杂度
	 * heap为2n, stack为n, 所以空间复杂度为n
	 * @param i
	 */
	public SumExceptionConstructor(int i){
		try {
			array[i] = array[i - 1] + i;
			new SumExceptionConstructor(i + 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(array[n]);
		}
	}

	public static void main(String[] args) {
		int n = 100;
		SumExceptionConstructor.n = n;
		SumExceptionConstructor.array = new int[n + 1];
		new SumExceptionConstructor(1);
	}
}
