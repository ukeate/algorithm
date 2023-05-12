package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestSelectSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{4, 3, 1, 2}
	SelectSort(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}
