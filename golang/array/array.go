package array

import "math"

/*
	翻转数组

	双指针
	时间 n，空间 1
*/
func Inverse(arr []int) {
	for i, j := 0, len(arr)-1; i < j; i, j = i+1, j-1 {
		arr[i], arr[j] = arr[j], arr[i]
	}
}

/*
	单指针
*/
func Inverse2(arr []int) {
	length := len(arr)
	for i, half := 0, length/2; i < half; i++ {
		j := length - 1 - i
		arr[i], arr[j] = arr[j], arr[i]
	}
}

/*
	轮换，如：[1,2,3,4,5],指定2时，结果[4,5,1,2,3]

	翻转前段，翻转后段，再整体翻转

	时间 n，空间 1
*/
func Rotate(arr []int, k int) {
	if k == 0 {
		return
	}

	length := len(arr)
	if k >= length {
		k = k % length
	}

	inverse := func(start int, end int) {
		for i, j := start, end; i < j; i, j = i+1, j-1 {
			arr[i], arr[j] = arr[j], arr[i]
		}
	}
	inverse(0, length-1-k)
	inverse(length-k, length-1)
	inverse(0, length-1)
}

/*
	回文
*/
func IsPalindrome(s string) bool {
	for i, j := 0, len(s)-1; i < j; i, j = i+1, j-1 {
		if s[i] != s[j] {
			return false
		}
	}
	return true
}

/*
	回文数
	1. 计算位
	2. 循环到half
	2.1 判断高低位余数。
		如 num = 12321, 则 t = 10000
		第一步 1 == (12321 / 10000) % 10 == 12321 % 10
		t /= 10 , 12321 /= 10
		第二步 2 == (12321 ／ 1000) % 10 == 1232 % 10
		判断成功
*/
func IsPalindromeNum(num int) bool {
	if num < 10 {
		return true
	}

	countWei := func() int {
		num := num
		count := 0
		for num > 0 {
			num /= 10
			count++
		}
		return count
	}

	wei := countWei()
	t := math.Pow(10, float64(wei-1))
	half := wei / 2
	n := num
	for i := 0; i < half; i++ {
		if (num/int(t))%10 == n%10 {
			t /= 10
			n /= 10
		} else {
			return false
		}
	}
	return true
}

/*
	最长回文子串

	时间 n^3
*/
func LongestPalindrome(s string) string {
	isPalindrome := func(start int, end int) bool {
		for start < end {
			if s[start] != s[end] {
				return false
			}
			start++
			end--
		}
		return true
	}

	length := len(s)
	from, to, max := 0, 0, 0
	for i := 0; i < length; i++ {
		for j := i; j < length; j++ {
			if isPalindrome(i, j) && (j-i) > max {
				from, to = i, j
				max = to - from
			}
		}
	}

	return s[from : to+1]
}

/*
	中心扩展法

	1 循环所有元素i（i为中心）
	2 循环向两边扩展 start, end。(start=i, end=i+1查找偶数串)
	2.1 记录left, right回文子串
	3 记录此i中心边缘子串是否最大
	*2 ..(start=i-1, end=i+1查找奇数串)
	*3 ..

	时间 n^2，空间 1
 */
func LongestPalindrome2(s string) string {
	maxLeft, maxRight, max := 0, 0, 1
	length := len(s)
	for i := 0; i < length; i++ {
		start, end, length1 := i, i+1, 0
		left, right := start, end
		for start >= 0 && end < length {
			if s[start] == s[end] {
				left, right, length1 = start, end, length1+2
				start, end = start-1, end+1
			} else {
				break
			}
		}
		if length1 > max {
			maxLeft, maxRight, max = left, right, length1
		}

		start, end, length1 = i-1, i+1, 1
		left, right = start, end
		for start >= 0 && end < length {
			if s[start] == s[end] {
				left, right, length1 = start, end, length1+2
				start, end = start-1, end+1
			} else {
				break
			}
		}
		if length1 > max {
			maxLeft, maxRight, max = left, right, length1
		}
	}
	return s[maxLeft : maxRight+1]
}
