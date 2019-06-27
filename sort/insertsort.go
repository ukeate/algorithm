package sort

// 向前插入，先找位置再移动
func InsertSort(arr []int) {
	length := len(arr)
	for i := 1; i < length; i++ {
		j := i - 1
		for j >= 0 {
			if arr[j] < arr[i] {
				break
			}
			j--
		}
		if j != i-1 {
			temp := arr[i]
			k := i - 1
			for ; k > j; k-- {
				arr[k+1] = arr[k]
			}
			arr[k+1] = temp
		}
	}
}

// 向前插入，边移动边找位置
func InsertSort2(arr []int) {
	length := len(arr)
	for i := 1; i < length; i++ {
		if arr[i-1] > arr[i] {
			temp := arr[i]
			j := i - 1
			for ; j >= 0 && arr[j] > temp; j-- {
				arr[j+1] = arr[j]
			}
			arr[j+1] = temp
		}
	}
}

// 向前插入，值向前冒泡
func InsertSort3(arr []int) {
	length := len(arr)
	for i := 1; i < length; i++ {
		for j := i - 1; j >= 0 && arr[j] > arr[j+1]; j-- {
			arr[j], arr[j+1] = arr[j+1], arr[j]
		}
	}
}
