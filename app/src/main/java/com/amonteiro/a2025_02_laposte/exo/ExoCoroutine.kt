package com.amonteiro.a2025_02_laposte.exo

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main() {
    val before = System.currentTimeMillis()
    //versionThread(10_000)
    versionCoroutine(1_000_000)
    val after = System.currentTimeMillis()
    println("Done in ${after - before} ms")
}

fun versionThread(nbCall: Int) {
    val box = BallotBoxBean()
    val listThread = ArrayList<Thread>()

    repeat(nbCall) {
        listThread += thread {
            box.add1VoiceAndWait()
        }
    }

    listThread.forEach { it.join() }

    println("nb =" + box.current)
}

fun versionCoroutine(callNumber: Int) {
    val box = BallotBoxBean()

    runBlocking {
        repeat(callNumber) {
            launch {
                box.add1VoiceAndWaitWithDelay()
            }
        }
    }

    println("Number : " + box.current)
}

class BallotBoxBean {
    private val number = AtomicInteger(0)

    suspend fun add1VoiceAndWaitWithDelay() {
        delay(2000)
        number.incrementAndGet()
    }

    fun add1VoiceAndWait() {
        Thread.sleep(2000)
        number.incrementAndGet()
    }

    val current
        get() = number.get()
}