package sort

func BubbleSort(arr []int) {
	length := len(arr)
	for i := 0; i < length; i++ {
		for j := 1; j < length-i; j++ {
			if arr[j-1] > arr[j] {
				arr[j-1], arr[j] = arr[j], arr[j-1]
			}
		}
	}
}

func BubbleSortSwapFlag(arr []int) {
	flag := true
	for i := len(arr); flag == true; i-- {
		flag = false
		for j := 1; j < i; j++ {
			if arr[j-1] > arr[j] {
				arr[j-1], arr[j] = arr[j], arr[j-1]
				flag = true
			}
		}
	}
}

func BubbleSortTailFlag(arr []int) {
	flag := len(arr)
	for flag > 1 {
		i := flag
		flag = 0
		for j := 1; j < i; j++ {
			if arr[j-1] > arr[j] {
				arr[j-1], arr[j] = arr[j], arr[j-1]
				flag = j
			}
		}
	}
}
