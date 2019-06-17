package recursion;

import junit.framework.Assert;

import org.junit.Test;
/**
 * 爬楼梯问题
 * 
 * @author outrun
 *
 */
public class _040Climb {
	int count = 0;
	/**
	 * 
	 * 空间复杂度为n
	 * 调两次情况用递归树分析，树的高度是栈的最大深度
	 * 
	 * 时间复杂度: 上限是2pow(n), 下限是2pow(n/2)
	 * 放缩法
	 * 放大法: T(n) = T(n - 1) + T(n - 2) < T(n - 1) + T(n - 1) = 2T(n - 1)
	 *   = 2 * (T(n - 2) + T(n - 3)) < 2 * (T(n - 2) + T(n - 2)) = 2 * 2T(n-2)
	 *   < ... < 2pow(n-1)T(1)
	 * 缩小法: T(n) = T(n - 1) + T(n - 2) > 2T(n - 2) > 2pow(2)T(n - 4) > 2pow(3)T(n - 6)
	 *   > 2pow((n-2)/2)T(2)
	 * 
	 * 缺点: 从递归树分析可以看出, 同数值的递归可能被多次重复计算
	 * @param n
	 * @return
	 */
	public int fib01(int n){
		count++;
		if(n == 1 || n == 2){
			return n;
		}else{
			int k = fib01(n - 1) + fib01(n - 2);
			return k;
		}
	}

	@Test
	public void test(){
		int n = 7;
		int result = fib01(n);
		System.out.println(result);
		System.out.println(count);
		Assert.assertTrue(count <= Math.pow(2, n) && count >= Math.pow(2, n / 2));
	}

	/**
	 * 备忘录法
	 * 空间复杂度为n
	 * 
	 * 时间复杂度为n
	 * 
	 * @param n
	 * @param array
	 * @return
	 */
	public int dfs(int n, int[] array){
		if(array[n] != 0){
			return array[n];
		}else{
			array[n] = dfs(n - 1, array) + dfs(n - 2, array);
			return array[n];
		}
	}
	public int fib03(int n){
		if(n == 1 || n == 2){
			return n;
		}else{
			int [] array = new int[n + 1];
			array[1] = 1;
			array[2] = 2;
			return dfs(n, array);
		}
	}
	/**
	 * 
	 * 动态规划法
	 * 
	 * 时间复杂度为n, 空间复杂度为n
	 * @param n
	 * @return
	 */
	public int fib04(int n){
		if(n == 1 || n == 2){
			return n;
		}else{
			int[] array = new int[n + 1];
			array[1] = 1;
			array[2] = 2;
			for(int i = 3;i <=n; i++){
				array[i] = array[i - 1] + array[i - 2];
			}
			return array[n];
		}
	}
	/**
	 * 状态压缩法，优化动态规划法空间复杂度
	 * 
	 * 时间复杂度为n, 空间复杂度为1
	 * 
	 * @param n
	 * @return
	 */
	public int fib05(int n){
		if(n == 1|| n == 2){
			return n;
		}else{
			int a = 1;
			int b = 2;
			int t;
			for(int i=3; i <= n; i++){
				t = a + b;
				a=b;
				b=t;
			}
			return b;
		}
	}
	/**
	 * 
	 * 通项公式计算
	 * 
	 * 空间复杂度为1
	 * 时间复杂度为log(2)(n)，是数学函数的时间复杂度
	 * @param n
	 * @return
	 */
	public int fib06(int n){
		if(n == 1|| n == 2){
			return n;
		}else{
			double sqrtFive = Math.sqrt(5);
			n++;
			double a = Math.pow((1 + sqrtFive) / 2, n);		
			double b = Math.pow((1 - sqrtFive) / 2, n);		
			double result = 1 / sqrtFive * (a - b);
			return (int) Math.floor(result);
	}
	}
}
