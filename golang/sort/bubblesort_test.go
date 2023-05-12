package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestBubbleSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{3, 4, 1, 2}
	BubbleSort(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)
}

func TestBubbleSortSwapFlag(t *testing.T) {
	assert := assert.New(t)
	arr := []int{3, 4, 1, 2}
	BubbleSortSwapFlag(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)

}
func TestBubbleSortTailFlag(t *testing.T) {
	assert := assert.New(t)
	arr := []int{3, 4, 1, 2}
	BubbleSortTailFlag(arr)
	assert.Equal([]int{1, 2, 3, 4}, arr)

}
