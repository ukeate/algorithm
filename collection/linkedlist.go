package collection

type LinkedListNode struct {
	value interface{}
	pre   *LinkedListNode
	next  *LinkedListNode
}

func NewLinkedListNode(value interface{}) *LinkedListNode {
	return &LinkedListNode{value: value}
}

/*
	index = 0为head.next节点
*/
type LinkedList struct {
	head *LinkedListNode
}

func NewLinkedList(arr []interface{}) *LinkedList {
	head := &LinkedListNode{}
	pre := head
	for _, v := range arr {
		node := NewLinkedListNode(v)
		pre.next = node
		pre = node
	}
	return &LinkedList{
		head: head,
	}
}
func LinkedListNodeLength(head *LinkedListNode) int {
	length := 0
	node := head.next
	for node != nil {
		length ++;
		node = node.next;
	}
	return length
}

/*
	翻转当前节点到最后
*/
func LinkedListNodeReverseList(head *LinkedListNode) *LinkedListNode {
	pre := head
	node := pre.next
	for node != nil {
		next := node.next
		node.next = pre

		pre = node
		node = next
	}
	head.next = nil
	return pre
}

/*
	数组构造链表，尾部连到index
*/
func Array2Circle(arr []interface{}, index int) *LinkedListNode {
	head := NewLinkedListNode(nil)
	node := head
	length := len(arr)

	var startNode *LinkedListNode
	for i := 0; i < length; i++ {
		node.next = NewLinkedListNode(arr[i])
		node = node.next
		if i == index {
			startNode = node
		}
	}
	node.next = startNode
	return head
}

/*
	快慢指针法
	间隔为1, 第length次时相遇

	设length为n, 交点前距离l1, 圈长l2, 交点到相遇点长a，k为正整数，则
	2n = l1 + k*l2 + a
	n = l1 + a
	相减得 n = k*l2 = l1 + a
	则 l1 = (k-1)l2 + (l2 -a)
	其中 l2 - a 为相遇点到交点的剩余距离
	用双指针，一个从头开始，一个从交点开始，同步后移可在交点相遇
*/
func LinkedListCircleNode(head *LinkedListNode) *LinkedListNode {
	slow, fast := head, head
	for fast != nil && fast.next != nil {
		slow = slow.next
		fast = fast.next.next
		if slow == fast {
			break
		}
	}
	if fast == nil || fast.next == nil {
		return nil
	}
	slow = head
	for fast != slow {
		fast = fast.next
		slow = slow.next
	}
	return fast
}

/*
	两数组构造两链表，链表2尾连接到链表1交汇节点
*/
func LinkedListArrayIntersection(arr1 []int, arr2 []int, index int) []*LinkedListNode {
	head1, head2 := NewLinkedListNode(nil), NewLinkedListNode(nil)
	node1, node2 := head1, head2
	length1, length2 := len(arr1), len(arr2)
	var intersection *LinkedListNode

	for i := 0; i < length1; i++ {
		node1.next = NewLinkedListNode(arr1[i])
		node1 = node1.next
		if i == index {
			intersection = node1
		}
	}
	for i := 0; i < length2; i++ {
		node2.next = NewLinkedListNode(arr2[i])
		node2 = node2.next
	}
	node2.next = intersection
	return []*LinkedListNode{head1, head2}
}

/*
	brute force暴力法
	时间 m*n，空间1
*/
func LinkedListGetIntersection(head1 *LinkedListNode, head2 *LinkedListNode) *LinkedListNode {
	for node1 := head1; node1 != nil; node1 = node1.next {
		for node2 := head2; node2 != nil; node2 = node2.next {
			if node1 == node2 {
				return node1
			}
		}
	}
	return nil
}

/*
	hash法

	时间 m+n, 空间 n
*/
func LinkedListGetIntersection2(head1 *LinkedListNode, head2 *LinkedListNode) *LinkedListNode {
	set := NewSet()
	for node := head2; node != nil; node = node.next {
		set.Add(node)
	}
	for node := head1; node != nil; node = node.next {
		if set.Has(node) {
			return node
		}
	}
	return nil
}

/*
	长链表去差值，再同时移动
	时间 max(m,n) 空间 1
*/
func LinkedListGetIntersection3(head1 *LinkedListNode, head2 *LinkedListNode) *LinkedListNode {
	length1, length2 := LinkedListNodeLength(head1), LinkedListNodeLength(head2)
	node1, node2 := head1, head2
	if length1 > length2 {
		k := length1 - length2
		for i := 1; i <= k; i++ {
			node1 = node1.next
		}
	} else if length2 > length1 {
		k := length2 - length1
		for i := 1; i <= k; i++ {
			node2 = node2.next
		}
	}
	for node1 != nil && node2 != nil {
		if node1 == node2 {
			return node1
		} else {
			node1 = node1.next
			node2 = node2.next
		}
	}
	return nil
}

/*
	尾连首
*/
func (l *LinkedList) ToCircle() {
	node := l.head
	for node.next != nil {
		node = node.next
	}
	node.next = l.head.next
}

func (l *LinkedList) Print() {
	node := l.head.next
	for node.next != nil {
		println(node.value)
		node = node.next
	}
	println(node.value)
}

func (l *LinkedList) PrintInverse() {
	node := l.head.next
	stack := NewStack()
	for node.next != nil {
		stack.Push(node.value)
		node = node.next
	}
	stack.Push(node.value)
	for !stack.Empty() {
		println(stack.Pop())
	}
}
func (l *LinkedList) PrintInverse2() {
	printRecursive := func(node *LinkedListNode) {}
	printRecursive = func(node *LinkedListNode) {
		if node.next != nil {
			printRecursive(node.next)
		}
		println(node.value)
	}
	printRecursive(l.head)
}

func (l *LinkedList) Index(ind int) *LinkedListNode {
	node := l.head
	for i := 0; i <= ind; i++ {
		if node.next == nil {
			break
		}
		node = node.next
	}
	return node
}

func (l *LinkedList) Insert(ind int, val interface{}) {
	nodeNew := NewLinkedListNode(val)
	node := l.Index(ind)

	nodeNew.next = node
	node.next = nodeNew
}

func (l *LinkedList) Remove(ind int) {
	pre := l.Index(ind - 1)
	if pre.next != nil {
		after := pre.next.next
		pre.next = after
	}
}
func (l *LinkedList) Reverse() {
	first := l.head.next
	pre := first
	node := pre.next
	for node != nil {
		next := node.next
		node.next = pre

		pre = node
		node = next
	}
	first.next = nil
	l.head.next = pre
}
func (l *LinkedList) Reverse2() {
	reverseRecursive := func(node *LinkedListNode) *LinkedListNode { return nil }
	reverseRecursive = func(node *LinkedListNode) *LinkedListNode {
		if node.next == nil {
			return node
		}
		next := node.next
		tail := reverseRecursive(next)
		next.next = node
		return tail
	}

	first := l.head.next
	tail := reverseRecursive(first)
	first.next = nil
	l.head.next = tail
}
func (l *LinkedList) length() int {
	length := 0
	node := l.head.next
	for node != nil {
		length++
		node = node.next
	}
	return length
}

func (l *LinkedList) IndexTail(n int) *LinkedListNode {
	length := l.length()
	node := l.head
	for i := 0; i < length-n; i++ {
		node = node.next
	}
	return node
}

/*
	双指针
*/
func (l *LinkedList) IndexTail2(n int) *LinkedListNode {
	node1, node2 := l.head, l.head
	for i := 0; i <= n; i++ {
		node2 = node2.next
	}
	for node2 != nil {
		node2 = node2.next
		node1 = node1.next
	}
	return node1
}

func (l *LinkedList) SortedRemoveDuplicated() {
	pre := l.head
	node := pre.next
	for node != nil && node.next != nil {
		next := node.next
		if node.value == next.value {
			for next != nil && node.value == next.value {
				next = next.next
			}
			node = next
			pre.next = next
		} else {
			pre = node
			node = next
		}
	}
}

func (l *LinkedList) ReverseBetween(left int, right int) {
	if left == right {
		return
	}
	first := l.head
	k := 1
	for k < left {
		first = first.next
		k++
	}
	// first -> left - 1
	// k -> left
	pre := first.next
	node := pre.next
	top := pre
	for k < right {
		next := node.next

		node.next = pre
		pre = node
		node = next

		k++
	}
	// k -> n
	// pre -> n
	// node -> n + 1
	top.next = node
	first.next = pre
}

/*
	k之后段截取到开头
*/
func (l *LinkedList) RotateRight(k int) {
	length := l.length()
	if k >= length {
		k = k % length
	}
	if k == 0 {
		return
	}
	first := l.head.next

	pre := first
	index := 1
	for index < length-k {
		pre = pre.next
		index++
	}
	// pre -> n - k
	// index -> n - k + 1
	newFirst := pre.next

	last := newFirst
	for last.next != nil {
		last = last.next
	}

	pre.next = nil
	last.next = first
	l.head.next = newFirst
}

/*
	判断回文
	翻转后段再比较
*/
func (l *LinkedList) IsPalindrome() bool {
	length := l.length()
	half := length / 2

	leftEnd := l.head.next
	for i := 0; i < half-1; i++ {
		leftEnd = leftEnd.next
	}
	rightStart := leftEnd.next
	if length%2 != 0 {
		rightStart = rightStart.next
	}
	rightStart = LinkedListNodeReverseList(rightStart)

	leftStart := l.head.next
	for i := 1; i <= half; i++ {
		if leftStart.value != rightStart.value {
			return false
		}
		leftStart = leftStart.next
		rightStart = rightStart.next
	}
	return true
}

/*
	翻转每一对节点
*/
func (l *LinkedList) SwapPairs() {
	zero := l.head
	pre := zero.next
	node := pre.next
	for pre != nil && node != nil {
		next := node.next

		node.next = pre
		pre.next = next
		zero.next = node

		if next == nil {
			break
		} else {
			zero = pre
			pre = next
			node = pre.next
		}
	}
}

/*
	洗牌

	翻转后半
	两两交错连接左右段
*/
func (l *LinkedList) Reorder() {
	length := l.length()
	half := (length + 1) / 2

	leftEnd := l.head.next
	for i := 1; i < half; i++ {
		leftEnd = leftEnd.next
	}
	rightStart := leftEnd.next
	rightStart = LinkedListNodeReverseList(rightStart)
	leftEnd.next = nil

	left := l.head.next
	right := rightStart
	flag := true
	for right != nil {
		if flag {
			next := left.next
			left.next = right
			left = next
		} else {
			next := right.next
			right.next = left
			right = next
		}
		flag = !flag
	}
}

/*
	约瑟夫环：围成环，不断数到第step个节点出列，直到空
	时间复杂度n * step, 空间复杂度n
*/
func (l *LinkedList) JosephusCircle(start int, step int) {
	length := l.length()
	l.ToCircle()

	startNode := l.head.next
	for i := 0; i < start; i++ {
		startNode = startNode.next
	}

	pre := startNode
	for i := 0; i < length-1; i++ {
		for j := 0; j < step-1; j++ {
			pre = pre.next
		}
		pre.next = pre.next.next
		pre = pre.next
	}
	pre.next = nil
	l.head.next = pre
}
