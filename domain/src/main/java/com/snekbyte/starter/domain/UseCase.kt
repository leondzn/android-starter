package com.snekbyte.starter.domain

interface UseCase<out T, in P> where T : Any {
  fun execute(params: P): T
}