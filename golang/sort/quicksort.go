package sort

/*
	分割，快排左右
	分割：
	1 取轴
	2 中位为start（轴本身占1位），循环除轴的节点
	2.1 节点值小时，中位后移，交换节点和中位的值
	3 交换轴与中位的值
*/
func QuickSort(arr []int, start int, end int) {
	swap := func(i int, j int) {
		arr[i], arr[j] = arr[j], arr[i]
	}
	partition := func(start int, end int) int {
		pivot := start
		val := arr[start]

		pos := start
		for i := start + 1; i <= end; i++ {
			if arr[i] < val {
				pos++
				swap(pos, i)
			}
		}
		swap(pivot, pos)
		return pos
	}
	if start < end {
		pivot := partition(start, end)
		QuickSort(arr, start, pivot-1)
		QuickSort(arr, pivot+1, end)
	}
}

/*
	..
	双指针法分割
	1 取轴为开头（或交换到开头），开头为左点，缓存值
	2 向中循环
	2.1 找到右点，向左点赋值
	2.2 找到左点，向右点赋值
	3. 找到中点，赋缓存值
*/
func QuickSort2(arr []int, start int, end int) {
	partition := func(start int, end int) int {
		i := start
		j := end
		val := arr[start]

		for i < j {
			for i < j && arr[j] >= val {
				j--
			}
			if i < j {
				arr[i] = arr[j]
				i++
			}
			for i < j && arr[i] < val {
				i++
			}
			if i < j {
				arr[j] = arr[i]
				j--
			}
		}
		arr[i] = val
		return i
	}
	if start < end {
		pivot := partition(start, end)
		QuickSort2(arr, start, pivot-1)
		QuickSort2(arr, pivot+1, end)
	}
}
