package com.github.forJrking.expand

/**
 * @Des: 扩展出三元运算符的作用
 * @Author: forjrking
 * @Time: 2021/5/7 9:40 AM
 * @Version: 1.0.0
 **/
infix fun <T> Boolean.then(value: T?) = if (this) value else null

fun <T> Boolean.then(value: T?, default: T) = if (this) value else default

inline fun <T> Boolean.then(function: () -> T, default: T) = if (this) function() else default

inline fun <T> Boolean.then(function: () -> T, default: () -> T) = if (this) function() else default()

infix inline fun <reified T> Boolean.then(function: () -> T) = if (this) function() else null