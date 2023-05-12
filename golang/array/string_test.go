package array

import (
	"testing"
	"github.com/stretchr/testify/assert"
)

func TestLastWordLen(t *testing.T) {
	assert := assert.New(t)
	s := "in the world"
	length := LastWordLen(s)
	assert.Equal(5, length)
}
func TestReverseWords(t *testing.T) {
	assert := assert.New(t)
	s := []rune("in the world")
	ReverseWords(s)
	assert.Equal([]rune("world the in"), s)
}

func TestAnagram(t *testing.T) {
	assert := assert.New(t)
	flag := Anagram("eel", "lee")
	assert.True(flag)
}

func TestCountAndSay(t *testing.T) {
	assert := assert.New(t)
	s := CountAndSay(6)
	assert.Equal("312211", s)
}
func TestBruteForce(t *testing.T) {
	assert := assert.New(t)
	s := "aabbccagdbbccdec"
	matcher := "bbccd"
	ind := BruteForce(s, matcher)
	assert.Equal(9, ind)
}
