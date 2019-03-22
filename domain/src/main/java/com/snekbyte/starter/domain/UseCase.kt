package com.snekbyte.starter.domain

abstract class UseCase<out T, in P> where T : Any {
  abstract fun execute(params: P): T
}