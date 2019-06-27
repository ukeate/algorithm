package sort

func MergeSort(arr []int) {
	length := len(arr)
	temp := make([]int, length)

	merge := func(start int, mid int, end int) {
		i := start
		j := mid + 1
		m := mid
		n := end
		k := 0
		for i <= m && j <= n {
			if arr[i] <= arr[j] {
				temp[k] = arr[i]
				k++
				i++
			} else {
				temp[k] = arr[j]
				k++
				j++
			}
		}
		for i <= m {
			temp[k] = arr[i]
			k++
			i++
		}
		for j <= n {
			temp[k] = arr[j]
			k++
			j++
		}
		for r := 0; r < k; r++ {
			arr[start+r] = temp[r]
		}
	}

	recurse := func(start int, end int) {}
	recurse = func(start int, end int) {
		if start < end {
			mid := (start + end) / 2
			recurse(start, mid)
			recurse(mid+1, end)
			merge(start, mid, end)
		}
	}

	recurse(0, length-1)

}
