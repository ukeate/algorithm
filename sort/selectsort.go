package sort

func SelectSort(arr []int) {
	length := len(arr)
	for i := 0; i < length; i++ {
		ind := i
		for j := i + 1; j < length; j++ {
			if arr[ind] > arr[j] {
				ind = j
			}
		}
		arr[i], arr[ind] = arr[ind], arr[i]
	}
}
