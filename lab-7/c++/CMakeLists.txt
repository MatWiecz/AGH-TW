cmake_minimum_required(VERSION 3.14)
project(c__)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp)
add_executable(c__ ${SOURCE_FILES})
target_link_libraries(c__ pthread)