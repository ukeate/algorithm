package recursion;

import org.junit.Test;

/**
 * gcd 最大公约数
 * lcm 最小公倍数
 * 
 * @author outrun
 *
 */
public class GcdAndLcm {

	/**
	 * 每执行一次循环, m或n至少缩小了2倍，故时间复杂度上限为log(2)(M),M是循环次数
	 * 对于大量随机测试样例, 每次循环能便m与n值缩小一个10进位，所以平均复杂度为O(lgM)
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public int gcd01(int m, int n){
		int a = Math.max(m, n);
		int b = Math.min(m, n);
		m = a;
		n = b;
		int r;
		while(m % n != 0){
			r = m % n;
			m = n;
			n = r;
		}
		return n;
	}
	@Test
	public void testGcd01(){
		System.out.println(gcd01(100, 44));
	}
	/**
	 * 时间复杂度同上
	 * 空间复杂度为O(lgM)
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public int gcd02(int m, int n){
		/*int a = Math.max(m, n);
		int b = Math.min(m, n);
		if(a % b == 0){
			return b;
		}else{
			return gcd02(b, a % b);
		}*/
		return m >= n ? m % n == 0 ? n : gcd02(n, m % n) : n % m == 0 ? m : gcd02(m, n % m);
	}
	@Test
	public void testGcd02(){
		System.out.println(gcd02(105, 252));
	}
	
	public int lcm(int m, int n){
		return m * n / gcd02(m, n);
	}
	
	@Test
	public void testLcm(){
		System.out.println(lcm(60, 24));
	}
}
