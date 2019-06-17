package recursion;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 猴子吃桃问题
 * 
 * @author outrun
 *
 */
public class MonkeyAndPeach {

	public int eat(int sum, int endNum, int curNum) {

		if (curNum >= endNum) {
			return sum;
		} else {
			curNum += 1;
			return eat(2 * sum + 2, endNum, curNum);
		}
	}

	@Test
	public void testHere() {
		System.out.println(eat(1, 10, 1));
	}

	@Test
	public void testHere2() {
		int sum = 1;
		// 时间复杂度是O(n)，空间复杂度是O(1)
		for (int i = 2; i <= 10; i++) {
			sum = sum * 2 + 2;
		}
		System.out.println(sum);
	}

	public int eat2(int n) {
		return n == 1 ? 1 : eat2(n - 1) * 2 + 2;
	}

	public int eat3(int n){
		System.out.println("f(" + n + ")压栈");

		if(n == 1){
			System.out.println("此时函数栈达到最大深度");
			System.out.println("f(" + n + ") 弹栈");
			return 1;
		}else{
			int ret = eat3(n - 1) * 2 + 2;

			System.out.println("f(" + n + ") 弹栈");
			return 	ret;
		}
	}

	@Test
	public void testHere3() {

		// 时间复杂度是O(n), 空间复杂度是O(n)
		// n很大时慎用递归
		System.out.println(eat3(10));
	}
	
	public void check(int n, int num){
		int a = num;
		for(int i = 2; i<= n; i++){
			a = a / 2 - 1;
		}
		System.out.println(a);
		Assert.assertEquals(1, a);
	}
	@Test
	public void testCheck(){
		check(10, 1534);
	}
}
