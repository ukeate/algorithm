package collection

type Set struct {
	m map[interface{}]interface{}
}

func NewSet() *Set {
	return &Set{
		m: map[interface{}]interface{}{},
	}
}

func (s *Set) Add(o interface{}) {
	s.m[o] = true
}
func (s *Set) Remove(o interface{}) {
	delete(s.m, o)
}
func (s *Set) Has(o interface{}) bool {
	_, ok := s.m[o]
	return ok
}
func (s *Set) Len() int {
	return len(s.m)
}
func (s *Set) Clear() {
	s.m = map[interface{}]interface{}{}
}
func (s *Set) IsEmpty() bool {
	return s.Len() == 0
}
func (s *Set) Slice() []interface{} {
	slice := make([]interface{}, s.Len())
	i := 0
	for key, _ := range s.m {
		slice[i] = key
		i++
	}
	return slice
}
