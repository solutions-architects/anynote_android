package com.luckhost.domain.models

sealed class Either<out L, out R> {
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()
}

fun <E> E.left() = Either.Left(this)

fun <T> T.right() = Either.Right(this)