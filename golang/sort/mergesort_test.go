package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestMergeSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{4, 3, 1, 2}
	MergeSort(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}
