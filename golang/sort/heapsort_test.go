package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestHeapsort(t *testing.T) {
	assert := assert.New(t)
	elements := []int{3, 1, 5, 7, 2, 4, 9, 6, 10, 8, 33, 2, 21, 2, 15, 22, 77, 11, 0, -1, 23345, 12}
	HeapSort(elements)
	assert.Equal(
		[]int{-1, 0, 1, 2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 21, 22, 33, 77, 23345},
		elements,
	)
}

func TestHeapSort2(t *testing.T) {
	assert := assert.New(t)
	elements := []int{3, 1, 5, 7, 2, 4, 9, 6, 10, 8, 33, 2, 21, 2, 15, 22, 77, 11, 0, -1, 23345, 12}
	HeapSort2(elements)
	assert.Equal(
		[]int{-1, 0, 1, 2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 21, 22, 33, 77, 23345},
		elements,
	)
}
