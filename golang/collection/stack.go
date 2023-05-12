package collection

import (
	"container/list"
	"strings"
	"fmt"
)

type Stack struct {
	list *list.List
}

func NewStack() *Stack {
	return &Stack{list: list.New()}
}

func (s *Stack) Push(value interface{}) {
	s.list.PushBack(value)
}

func (s *Stack) Pop() interface{} {
	ele := s.list.Back()
	if ele == nil {
		return nil
	}

	s.list.Remove(ele)
	return ele.Value
}

func (s *Stack) Peek() interface{} {
	ele := s.list.Back()
	if ele == nil {
		return nil
	}

	return ele.Value
}
func (s *Stack) Len() int {
	return s.list.Len()
}

func (s *Stack) Empty() bool {
	return s.list.Len() == 0
}

/*
	数组实现stack
*/
type Stack2 struct {
	size int
	arr  []interface{}
}

func NewStack2() *Stack2 {
	return &Stack2{
		size: 0,
		arr:  make([]interface{}, 10),
	}
}

func (s *Stack2) Empty() bool {
	return s.size == 0
}
func (s *Stack2) expandCapacity() {
	arr := make([]interface{}, s.size*2)
	for i, v := range s.arr {
		arr[i] = v
	}
	s.arr = arr
}

func (s *Stack2) push(v interface{}) {
	s.arr[s.size] = v
	s.size++
	if s.size >= len(s.arr) {
		s.expandCapacity()
	}
}

func (s *Stack2) Peek() interface{} {
	if s.Empty() {
		return nil
	}
	return s.arr[s.size-1]
}
func (s *Stack2) Pop() interface{} {
	if s.Empty() {
		return nil
	}
	v := s.Peek()
	s.arr[s.size-1] = nil
	s.size--
	return v
}

/*
	两queue实现stack

	Push：stack为空时放入queue1。正常哪个queue空放入哪个
	Pop: 哪个queue有值，依次弹出放入另一个，最后一个返回
*/
type Stack3 struct {
	size   int
	queue1 *Queue
	queue2 *Queue
}

func NewStack3() *Stack3 {
	return &Stack3{
		size:   0,
		queue1: NewQueue(),
		queue2: NewQueue(),
	}
}
func (s *Stack3) Empty() bool {
	return s.size == 0
}
func (s *Stack3) Push(v interface{}) {
	if s.Empty() || !s.queue1.Empty() {
		s.queue1.Offer(v)
	} else {
		s.queue2.Offer(v)
	}
}
func (s *Stack3) Pop() interface{} {
	if !s.queue1.Empty() {
		for s.queue1.Len() > 1 {
			s.queue2.Offer(s.queue1.Poll())
		}
		s.size--
		return s.queue1.Poll()
	} else {
		for s.queue2.Len() > 1 {
			s.queue1.Offer(s.queue2.Poll())
		}
		s.size--
		return s.queue2.Poll()
	}
}

/*
	栈弹出序列有很多，判断给出弹出序列是否成立

	时间 n^2
*/
func IsPopSeq(pushSeq []int, popSeq []int) bool {
	if pushSeq == nil || popSeq == nil || len(pushSeq) != len(popSeq) {
		return false
	}

	stack := NewStack()
	for i, j := 0, 0; i < len(pushSeq); i++ {
		stack.Push(pushSeq[i])
		for j < len(popSeq) && popSeq[j] == stack.Peek() {
			stack.Pop()
			j++
		}
	}
	return stack.Empty()
}

/*
	括号匹配
*/
func Parentheses(s string) bool {
	stack := NewStack()
	for i := 0; i < len(s); i++ {
		switch c := s[i]; c {
		case '(':
			stack.Push(c)
		case '[':
			stack.Push(c)
		case ')':
			if stack.Empty() || stack.Peek() != uint8('(') {
				return false
			}
			stack.Pop()
		case ']':
			if stack.Empty() || stack.Peek() != uint8('[') {
				return false
			}
			stack.Pop()
		default:
		}
	}
	if stack.Empty() {
		return true
	} else {
		return false
	}
}

/*
	目录简化，如 /a/../c/，简化为/c
*/
func SimplifyPath(path string) string {
	stack := NewStack()
	arr := strings.Split(path, "/")
	for _, str := range arr {
		if str == "" || str == "." {
			continue
		}
		if str == ".." {
			if !stack.Empty() {
				stack.Pop()
			}
		} else {
			stack.Push(str)
		}
	}

	if stack.Empty() {
		return "/"
	}
	retS := ""
	for !stack.Empty() {
		retS = fmt.Sprintf("/%v", stack.Pop()) + retS
	}
	return retS
}
