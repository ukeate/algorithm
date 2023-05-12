package collection

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestLinkedList_IndexTail2(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5, 6})
	assert.Equal(2, list1.IndexTail(4).value)
}
func TestLinkedList_SortedRemoveDuplicated(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 2, 6})
	list1.SortedRemoveDuplicated()
	assert.Equal(6, list1.Index(3).value)
}
func TestLinkedList_ReverseBetween(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5})
	list1.ReverseBetween(2, 4)
	assert.Equal(4, list1.Index(1).value)
}
func TestLinkedList_RotateRight(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5})
	list1.RotateRight(2)
	assert.Equal(5, list1.Index(1).value)

}
func TestLinkedList_IsPalindrome(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 3, 2, 1})
	assert.True(list1.IsPalindrome())
}
func TestLinkedList_SwapPairs(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5})
	list1.SwapPairs()
	assert.Equal(2, list1.Index(0).value)
}
func TestLinkedList_Reorder(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5})
	list1.Reorder()
	assert.Equal(5, list1.Index(1).value)
}
func TestLinkedList_JosephusCircle(t *testing.T) {
	assert := assert.New(t)
	list1 := NewLinkedList([]interface{}{1, 2, 3, 4, 5})
	list1.JosephusCircle(3, 4)
	assert.Equal(5, list1.head.next.value)
}
func TestLinkedListArrayIntersection(t *testing.T) {
	assert := assert.New(t)
	heads := LinkedListArrayIntersection([]int{1, 2, 3, 4, 5, 6}, []int{7, 8}, 3)
	assert.Equal(4, LinkedListGetIntersection(heads[0], heads[1]).value)
	assert.Equal(4, LinkedListGetIntersection2(heads[0], heads[1]).value)
	assert.Equal(4, LinkedListGetIntersection3(heads[0], heads[1]).value)
}
func TestLinkedListCircleNode(t *testing.T) {
	assert := assert.New(t)
	head := Array2Circle([]interface{}{1, 2, 3, 4, 5, 6}, 3)
	assert.Equal(4, LinkedListCircleNode(head).value)
}

