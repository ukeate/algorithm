package array

import "strconv"

const ALPHABET_LENGHT = 26

func LastWordLen(s string) int {
	isLetter := func(c uint8) bool {
		if (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') {
			return true
		} else {
			return false
		}
	}
	length := len(s)
	if length < 1 {
		return 1
	}
	pos := length - 1
	for pos >= 0 {
		if isLetter(s[pos]) {
			break
		} else {
			pos--
		}
	}
	retLen := 0
	for pos >= 0 {
		if !isLetter(s[pos]){
			break
		} else {
			pos--
			retLen++
		}
	}
	return retLen
}

/*
	翻转词序
*/
func ReverseWords(s []rune) {
	reverse := func(start int, end int) {
		for start < end {
			s[start], s[end] = s[end], s[start]
			start++
			end--
		}
	}

	if s == nil || len(s) <= 1 {
		return
	}
	n := len(s)
	i := 0
	for i < n {
		j := i
		for j < n {
			if s[j] == ' ' {
				break
			} else {
				j++
			}
		}
		reverse(i, j-1)
		for j < n && s[j] == ' ' {
			j++
		}
		i = j
	}
	reverse(0, n-1)
}

/*
	同字异序
*/
func Anagram(source string, target string) bool {
	if len(source) != len(target) {
		return false
	}
	length := len(source)
	table1 := make([]int, ALPHABET_LENGHT)
	table2 := make([]int, ALPHABET_LENGHT)

	for i := 0; i < length; i++ {
		table1[source[i]-'a']++
		table2[target[i]-'a']++
	}
	for i := 0; i < ALPHABET_LENGHT; i++ {
		if table1[i] != table2[i] {
			return false
		}
	}
	return true
}

/*
	题目解释：原题的意思就是用一个新的字符串描述上一个字符串，用数字表示上一个：
	当n=1时：输出1；
	当n=2时，解释1，1读作1个 ，表示为11；
	当n=3时，解释上一个11，读作2个1，表示为21；（注意相同数字的描述）
	当n=4时，解释上一个21，读作1个2，一个1，表示为1211；
	当n=5时，解释上一个1211，读作1个1，1个2，2个1，表示为111221；
	当n=6时，解释上一个111221，读作3个1，2个2，1个1，表示为312211；

	时间 n^3, 空间 n^2, n是输入的值
*/
func CountAndSay(n int) string {
	if n <= 0 {
		return ""
	}
	if n == 1 {
		return "1"
	}
	if n == 2 {
		return "11"
	}
	s := "11"
	result := ""
	for i := 3; i <= n; i++ {
		temp := s[0]
		count := 1
		for j := 1; j < len(s); j++ { // len的时间复杂度在for中越来越接近n^2
			if s[j] == temp {
				count++
			} else {
				// result的长度越来越接近n^2
				result += strconv.Itoa(count) + strconv.Itoa(int(temp-48))
				count = 1
				temp = s[j]
			}
		}
		result += strconv.Itoa(count) + strconv.Itoa(int(temp-48))
		s = result
		result = ""
	}
	return s
}

/*
	模式匹配，brute force法
	时间 m*n，空间1
*/
func BruteForce(s string, matcher string) int {
	m := len(s)
	n := len(matcher)
	for i := 0; i < m; i++ {
		count := 0
		for j := 0; j < n && i+j < m; j++ {
			if s[i+j] != matcher[j] {
				break
			} else {
				count++
			}
		}
		if count == n {
			return i
		}
	}
	return -1
}

/*
	时间m+n
*/
func KMP() {

}
