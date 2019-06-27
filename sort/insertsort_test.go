package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestInsertSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{4, 3, 1, 2}
	InsertSort(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}

func TestInsertSort2(t *testing.T) {
	assert := assert.New(t)
	arr := []int{4, 3, 1, 2}
	InsertSort2(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}
func TestInsertSort3(t *testing.T) {
	assert := assert.New(t)
	arr := []int{4, 3, 1, 2}
	InsertSort3(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}
