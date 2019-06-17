package recursion;

import org.junit.Test;

public class CommonSum {
	
	/**
	 * 
	 * 时间复杂度O(n), 空间复杂度O(1)
	 * @param n
	 * @return
	 */
	public int commonMethod01(int n){
		int sum = 0;
		for(int i = 1; i <=n; i++){
			sum += i;
		}
		return sum;
	}
	/**
	 * 时间复杂度O(n), 空间复杂度O(n)
	 * 
	 * @param n
	 * @return
	 */
	public int commonMethod02(int n){
		if(n == 1){
			return 1;
		}else{
			return commonMethod02(n - 1) + n;
		}
	}

	@Test
	public void test(){
		int n = 100;
		System.out.println(commonMethod02(100));
	}
	
	/**
	 * 时间复杂度和空间复杂度都是O(1)
	 * 
	 * @param n
	 * @return
	 */
	public int commonMethod03(int n){
		return n * (1 + n) / 2;
	}
	
}
