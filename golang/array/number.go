package array

import (
	"sort"
	"container/list"
)

/*
	描述：给正整数s, 写出连续正整数和为s的所有序列

	双指针法
	由等差数列求和公式知，数列最开始不能过一半
	移end加sum, 移start减sum
	sum找到后，只能同时加减sum，即同时移start、end
*/
func SeriesSum(s int) {
	half := (s + 1) / 2
	start := 1
	end := 2
	sum := 0

	for start < half {
		sum = (start + end) * (end - start + 1) / 2
		if sum == s {
			println(start, end)
			start++
			end++
		} else if sum < s {
			end++
		} else {
			start ++
		}
	}
}

/*
	升序不重复数组中，所有两个元素和为s的组合
	时间 n^2
*/
func TwoSum(arr []int, s int) {
	n := len(arr)

	for i := 0; i < n-1; i++ {
		for j := i + 1; j < n; j++ {
			if arr[i]+arr[j] == s {
				println(i, j)
				break
			}
		}
	}
}

/*
	二分查找

	时间 nlog(n)
*/
func TwoSum2(arr []int, s int) {
	n := len(arr)
	for i, j := 0, 0; i < n-1; i++ {
		another := s - arr[i]
		j = sort.SearchInts(arr, another)
		if j > i {
			println(i, j)
		}
	}
}

/*
	双指针

	移i加sum, 移j减sum
	sum找到后，只能同时加减sum，即同时移i,j

	时间 n
*/
func TwoSum3(arr []int, s int) {
	i := 0
	j := len(arr) - 1
	for i < j {
		if arr[i]+arr[j] == s {
			println(i, j)
			i++
			j--
		} else if arr[i]+arr[j] < s {
			i++
		} else {
			j--
		}
	}
}

/*
	有序数组去重

	时间 n，空间 n
*/
func RemoveDuplicates(arr []int) int {
	if arr == nil || len(arr) == 0 {
		return 0
	}
	if len(arr) == 1 {
		return 1
	}

	end := len(arr) - 1
	list0 := list.New()

	for i := 0; i <= end; {
		if i == end {
			list0.PushBack(arr[i])
			i++
		} else {
			j := i + 1
			if arr[i] == arr[j] {
				for j <= end && arr[i] == arr[j] {
					j++
				}
			}
			list0.PushBack(arr[i])
			i = j
		}
	}

	eleInd := 0
	for ele := list0.Front(); nil != ele; ele = ele.Next() {
		arr[eleInd] = ele.Value.(int)
		eleInd++
	}
	return eleInd
}

/*
	..
	时间 n，空间 1
*/
func RemoveDuplicates2(arr []int) int {
	if arr == nil || len(arr) == 0 {
		return 0
	}
	if len(arr) == 1 {
		return 1
	}

	length := len(arr)
	num := 1
	for i, tmp := 1, arr[0]; i < length; i++ {
		if arr[i] != tmp {
			arr[num] = arr[i]
			tmp = arr[i]
			num++
		}
	}
	return num
}
