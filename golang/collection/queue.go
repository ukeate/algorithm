package collection

import "container/list"

type Queue struct {
	list *list.List
}

func NewQueue() *Queue {
	return &Queue{list: list.New()}
}

func (s *Queue) Offer(value interface{}) {
	s.list.PushBack(value)
}

func (s *Queue) Poll() interface{} {
	ele := s.list.Front()
	if ele == nil {
		return nil
	}

	s.list.Remove(ele)
	return ele.Value
}

func (s *Queue) Peek() interface{} {
	ele := s.list.Front()
	if ele == nil {
		return nil
	}

	return ele.Value
}
func (s *Queue) Len() int {
	return s.list.Len()
}

func (s *Queue) Empty() bool {
	return s.list.Len() == 0
}

type Queue2Node struct {
	value interface{}
	next  *Queue2Node
}

func NewQueue2Node(value interface{}) *Queue2Node {
	return &Queue2Node{value: value}
}

/*
	链表实现queue
*/
type Queue2 struct {
	size int
	head *Queue2Node
	last *Queue2Node
}

func NewQueue2() *Queue2 {
	q := &Queue2{
		size: 0,
		head: NewQueue2Node(nil),
	}
	q.last = q.head
	return q
}

func (q *Queue2) Empty() bool {
	return q.size == 0
}

func (q *Queue2) Offer(v interface{}) {
	node := NewQueue2Node(v)
	q.last.next = node
	q.last = node
	q.size++
}
func (q *Queue2) Peek() interface{} {
	if q.Empty() {
		return nil
	}
	return q.head.next.value
}
func (q *Queue2) Poll() interface{} {
	if q.Empty() {
		return nil
	}
	node := q.head.next
	q.head.next = node.next
	q.size--
	if q.size == 0 {
		q.last = q.head
	}
	return node.value
}

/*
	两stack实现queue
*/
type Queue3 struct {
	size   int
	stack1 *Stack
	stack2 *Stack
}

func NewQueue3() *Queue3 {
	return &Queue3{
		size:   0,
		stack1: NewStack(),
		stack2: NewStack(),
	}
}
func (q *Queue3) swap() {
	for !q.stack1.Empty() {
		q.stack2.Push(q.stack1.Pop())
	}
}
func (q *Queue3) Offer(v interface{}) {
	q.stack1.Push(v)
	q.size++
}
func (q *Queue3) Poll() interface{} {
	if q.stack2.Empty() {
		q.swap()
	}
	v := q.stack2.Pop()
	q.size--
	return v
}
func (q *Queue3) Peek() interface{} {
	if q.stack2.Empty() {
		q.swap()
	}
	return q.stack2.Peek()
}
