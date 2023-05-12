package array

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestSeriesSum(t *testing.T) {
	SeriesSum(21)
}

func TestTwoSum(t *testing.T) {
	TwoSum([]int{ 1, 2, 3, 4, 5, 6, 7, 8, 9 },10)
}
func TestTwoSum2(t *testing.T) {
	TwoSum2([]int{ 1, 2, 3, 4, 5, 6, 7, 8, 9 },10)
}
func TestTwoSum3(t *testing.T) {
	TwoSum3([]int{ 1, 2, 3, 4, 5, 6, 7, 8, 9 },10)
}

func TestRemoveDuplicates(t *testing.T) {
	assert := assert.New(t)
	arr := []int{ 1, 2, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8 }
	length := RemoveDuplicates(arr)
	assert.Equal(8,length)
}
func TestRemoveDuplicates2(t *testing.T) {
	assert := assert.New(t)
	arr := []int{ 1, 2, 2, 2, 3, 4, 4, 5, 6, 6, 7, 7, 8 }
	length := RemoveDuplicates2(arr)
	assert.Equal(8,length)
}