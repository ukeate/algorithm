package sort

/*
	1 循环：选gap为一半长度
	2 循环：第一段gap
	3 循环gap间隔序列
	4 选择排序
*/
func ShellSort(arr []int) {
	length := len(arr)
	for gap := length / 2; gap > 0; gap /= 2 {
		for i := 0; i < gap; i++ {
			for j := i + gap; j < length; j += gap {
				k := j - gap
				if arr[k] > arr[j] {
					temp := arr[j]
					for k >= 0 && arr[k] > temp {
						arr[k+gap] = arr[k]
						k -= gap
					}
					arr[k+gap] = temp
				}
			}
		}
	}
}

/*
	1 循环：选gap为一半长度
	2 循环：第一个gap之后每个元素为j
	3 j向前的gap间隔序列做选择排序
 */
func ShellSort2(arr []int) {
	length := len(arr)
	for gap := length / 2; gap > 0; gap /= 2 {
		for j := gap; j < length; j++ {
			k := j - gap
			if arr[k] > arr[j] {
				temp := arr[j]
				for k >= 0 && arr[k] > temp {
					arr[k+gap] = arr[k]
					k -= gap
				}
				arr[k+gap] = temp
			}
		}
	}
}

/*
	..
	最后用冒泡排序
*/
func ShellSort3(arr []int) {
	length := len(arr)
	for gap := length / 2; gap > 0; gap /= 2 {
		for i := gap; i < length; i++ {
			for j := i - gap; j >= 0 && arr[j] > arr[j+gap]; j -= gap {
				arr[j], arr[j+gap] = arr[j+gap], arr[j]
			}
		}
	}
}
