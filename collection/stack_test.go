package collection

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestStack(t *testing.T) {
	assert := assert.New(t)
	stack := NewStack()
	stack.Push("(")
	stack.Push("[")
	assert.Equal("[",stack.Peek())
	assert.Equal("[",stack.Pop())
	assert.Equal("(",stack.Pop())
}
func TestIsPopSeq(t *testing.T) {
	assert := assert.New(t)
	push := []int{1,2,3,4,5}
	pop1 := []int{4,5,3,2,1}
	pop2 := []int{4,3,5,1,2}
	pop3 := []int{3,5,4,2,1}
	assert.True(IsPopSeq(push,pop1))
	assert.False(IsPopSeq(push,pop2))
	assert.True(IsPopSeq(push,pop3))
}

func TestParentheses(t *testing.T) {
	assert := assert.New(t)
	assert.True(Parentheses("(([])())"))
	assert.False(Parentheses("(([(])))"))

}
func TestSimplifyPath(t *testing.T) {
	assert := assert.New(t)
	assert.Equal("/",SimplifyPath("/../../"))
	assert.Equal("/home/foo",SimplifyPath("/home//foo//"))
	assert.Equal("/c/d",SimplifyPath("a/./b/../../../c/d/"))
}