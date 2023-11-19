cmake_minimum_required(VERSION 3.22)

project(jniproxy)

link_directories(proxy)

add_library(jniproxy SHARED jniproxy.c)

target_link_libraries(${PROJECT_NAME} proxy)
