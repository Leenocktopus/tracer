package com.leandoer.tracer.service

fun String.toBinary(): String {
    return (if (this.matches("[0-1]+".toRegex())) this else java.lang.Long.toBinaryString(this.toLong()))
}