package sort

/*
	数组表示大顶堆，pos为父节点时, pos*2+1为第一个子节点
	调整堆：父值非最大时下降
	1 给出父节点pos,找到儿子child。
	2 循环直到child越界
	2.1 找大儿子下降，更新pos，计算child

	构建堆: 默认给出数组为堆，从最后一个父节点pos=halfLen开始，向前一个个父节点调整堆

	堆排序:
	1 构建堆，重复2
	2 循环堆节点
	2.1 交换堆顶与末节点叶子, 下降堆顶
*/
func HeapSort(elements []int) {
	swap := func(i int, j int) {
		elements[i], elements[j] = elements[j], elements[i]
	}
	headAdjuct := func(pos int, len int) {
		val := elements[pos]
		child := pos*2 + 1

		for child < len {
			if child+1 < len && elements[child] < elements[child+1] {
				child++
			}
			if elements[pos] >= elements[child] {
				break
			}

			elements[pos] = elements[child]
			pos = child
			child = pos*2 + 1
			elements[pos] = val
		}
	}

	buildHeap := func() {
		halfLen := len(elements) / 2
		for i := halfLen; i >= 0; i-- {
			headAdjuct(i, len(elements))
		}

	}

	buildHeap()
	for i := len(elements) - 1; i > 0; i-- {
		swap(0, i)
		headAdjuct(0, i)
	}
}

func HeapSort2(arr []int) {
	length := len(arr)
	swap := func(i int, j int) {
		arr[i], arr[j] = arr[j], arr[i]
	}
	parentPos := func(pos int) int {
		return (pos - 1) / 2
	}
	child1Pos := func(pos int) int {
		return pos*2 + 1
	}
	fixDown := func(pos int, l int) {
		val := arr[pos]
		child := child1Pos(pos)
		for child < l {
			if child+1 < l && arr[child+1] > arr[child] {
				child++
			}
			if arr[child] <= val {
				break
			}
			arr[pos] = arr[child]
			pos = child
			child = child1Pos(pos)
		}
		arr[pos] = val
	}
	buildHeap := func() {
		lastParentPos := parentPos(length - 1)
		for i := lastParentPos; i >= 0; i-- {
			fixDown(i, length)
		}
	}

	buildHeap()
	for i := length - 1; i > 0; i-- {
		swap(0, i)
		fixDown(0, i)
	}

	//fixUp := func(child int) {
	//	val := arr[child]
	//	pos := parentPos(child)
	//	for pos >= 0 && child > 0 {
	//		if arr[pos] >= val {
	//			break
	//		}
	//		arr[child] = arr[pos]
	//		child = pos
	//		pos = parentPos(child)
	//	}
	//	arr[child] = val
	//}
}
