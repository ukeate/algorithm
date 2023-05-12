package number

/*
	返回第n个斐波那契数

	时间 n
*/
func Fibonacci(n int) int {
	if n < 3 {
		return 1
	}
	a, b := 1, 1
	for i := 3; i <= n; i++ {
		r := a + b
		a, b = b, r
	}
	return b
}

/*
	时间 n
*/
func Fibonacci2(n int) int {
	if n < 3 {
		return 1
	}
	return Fibonacci2(n-2) + Fibonacci2(n-1)
}

/*
	最大子序列和

	时间 n
*/
func SumArr(arr []int) int {
	tmp := arr[0]
	max := tmp

	for i := 1; i < len(arr); i++ {
		if tmp >= 0 {
			tmp += arr[i]
		} else {
			tmp = arr[i]
		}

		if max < tmp {
			max = tmp
		}
	}
	return max
}

/*
	正整数和
	递归
*/
func CommonSum(n int) int {
	if n == 1 {
		return 1
	}
	return CommonSum(n-1) + n
}

/*
	最大公约数

	每执行一次循环, m或n至少缩小了2倍，故时间复杂度上限为log(2)(M),M是循环次数
	对于大量随机测试样例, 每次循环能便m与n值缩小一个10进位，所以平均复杂度为O(lgM)
*/
func GCD(m int, n int) int {
	var max, min int
	if m > n {
		max, min = m, n
	} else {
		max, min = n, m
	}
	for max%min != 0 {
		r := max % min
		max = min
		min = r
	}
	return min
}

/*
	..

	递归

	时间 logM 空间 logM
*/
func GCD2(m int, n int) int {
	var max, min int
	if m > n {
		max, min = m, n
	} else {
		max, min = n, m
	}

	if max%min == 0 {
		return min
	}
	return GCD2(min, max%min)
}

/*
	最小公倍数
*/
func LCM(m int, n int) int {
	return m * n / GCD(m, n)
}

/*
	猴子吃桃：每天吃一半加1个，第n天剩1个，一共几个
*/
func MonkeyEat(n int) int {
	if n == 1 { //指倒数第一天
		return 1
	}
	return MonkeyEat(n-1)*2 + 2
}

/*
	爬楼梯问题：一次走1或2级，爬上n级有多少走法

	要走到n+1层，只能从n层或n-1层走。n+1层方法数为n层与n-1层方法数的和。所以是个斐波那契数列问题
*/
func ClimbStairs(n int) int {
	if n == 1 {
		return 1
	}
	if n == 2 {
		return 2
	}
	return ClimbStairs(n-1) + ClimbStairs(n-2)
}
