package ru.juhnowski.calculator.cache


import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


object Cache {
    @JvmField
    val FUNCTIONS: HashMap<Int,CachedFunction> = HashMap<Int,CachedFunction>();
}

class CachedFunction(val response:String, val pod1Title:String, val pod2Title:String);