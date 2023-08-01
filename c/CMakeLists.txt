cmake_minimum_required(VERSION 3.25)
project(c)

set(CMAKE_CXX_STANDARD 14)

include_directories(num)

add_executable(c
        num/cal.cpp
        num/cal.h csp/noip2007t1_scholarship_sort.cpp csp/noip2007t1_scholarship_sort.cpp noi/low/str/kmp.cpp)
