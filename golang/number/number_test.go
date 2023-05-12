package number

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestFibonacci(t *testing.T) {
	assert := assert.New(t)
	assert.Equal(13, Fibonacci(7))
}
func TestFibonacci2(t *testing.T) {
	assert := assert.New(t)
	assert.Equal(13, Fibonacci2(7))
}

func TestSumArr(t *testing.T) {
	assert := assert.New(t)
	assert.Equal(35, SumArr([]int{ -2, 10, 12, -20, 33 }))
}