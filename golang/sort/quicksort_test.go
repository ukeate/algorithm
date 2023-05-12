package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestQuickSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{8, 2, 4, 65, 2, 4, 7, 1, 9, 0, 2, 34, 12}
	QuickSort(arr, 0, len(arr)-1)
	assert.Equal([]int{0, 1, 2, 2, 2, 4, 4, 7, 8, 9, 12, 34, 65}, arr)
}

func TestQuickSort2(t *testing.T) {
	assert := assert.New(t)
	arr := []int{8, 2, 4, 65, 2, 4, 7, 1, 9, 0, 2, 34, 12}
	QuickSort2(arr, 0, len(arr)-1)
	assert.Equal([]int{0, 1, 2, 2, 2, 4, 4, 7, 8, 9, 12, 34, 65}, arr)
}