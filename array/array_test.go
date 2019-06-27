package array

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestInverse(t *testing.T) {
	assert := assert.New(t)
	arr := []int{1, 2, 3, 4, 5}
	Inverse(arr)
	assert.Equal([]int{5, 4, 3, 2, 1}, arr)
}

func TestInverse2(t *testing.T) {
	assert := assert.New(t)
	arr := []int{1, 2, 3, 4, 5}
	Inverse2(arr)
	assert.Equal([]int{5, 4, 3, 2, 1}, arr)
}

func TestRotate(t *testing.T) {
	assert := assert.New(t)
	arr := []int{1, 2, 3, 4, 5, 6, 7}
	Rotate(arr, 3)
	assert.Equal([]int{5, 6, 7, 1, 2, 3, 4}, arr)
}

func TestIsPalindrome(t *testing.T) {
	assert := assert.New(t)
	assert.False(IsPalindrome(".s.ebs"))
	assert.True(IsPalindrome(".sws."))
}

func TestIsPalindromeNum(t *testing.T) {
	assert := assert.New(t)
	assert.False(IsPalindromeNum(1234))
	assert.True(IsPalindromeNum(1234321))
}

func TestLongestPalindrome(t *testing.T) {
	assert := assert.New(t)
	assert.Equal("woabcbaow", LongestPalindrome("sdbsdaswoabcbaowe"))
}

func TestLongestPalindrome2(t *testing.T) {
	assert := assert.New(t)
	assert.Equal("woabcbaow", LongestPalindrome2("sdbsdaswoabcbaowe"))
}