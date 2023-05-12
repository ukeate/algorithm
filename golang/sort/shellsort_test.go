package sort

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestShellSort(t *testing.T) {
	assert := assert.New(t)
	arr := []int{5, 3, 7, 9, 1, 6, 4, 8, 2}
	ShellSort(arr)
	assert.Equal([]int{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr)
}

func TestShellSort2(t *testing.T) {
	assert := assert.New(t)
	arr := []int{5, 3, 7, 9, 1, 6, 4, 8, 2}
	ShellSort2(arr)
	assert.Equal([]int{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr)
}

func TestShellSort3(t *testing.T) {
	assert := assert.New(t)
	arr := []int{5, 3, 7, 9, 1, 6, 4, 8, 2}
	ShellSort3(arr)
	assert.Equal([]int{1, 2, 3, 4, 5, 6, 7, 8, 9}, arr)
}
