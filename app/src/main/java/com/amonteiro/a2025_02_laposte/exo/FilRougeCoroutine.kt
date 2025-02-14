package com.amonteiro.a2025_02_laposte.exo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeoutException
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

fun main() {
    electionResult()
}

fun electionResult() = runBlocking {
    val start = System.currentTimeMillis()
    //Pour stocker les async
    val task = ArrayList<Pair<ArrayList<Deferred<ResultBean?>>, Job>>()

    repeat(100) { numDept ->

        val listSameDept = ArrayList<Deferred<ResultBean?>>()
        val job = Job()

        repeat(5) {
            listSameDept += async(job) {
                try {
                    getResultFromDepartment(numDept)
                }
                catch (e: Exception) {
                    delay(5000)
                    null
                }
            }
            task += listSameDept to job
        }
    }

    var nbGandalf = 0
    var nbDumbledore = 0
    var nbMerlin = 0
    var answer = 0

    task.mapNotNull { pair: Pair<ArrayList<Deferred<ResultBean?>>, Job> ->
        val resultBean = select<ResultBean?> {
            pair.first.forEach { it.onAwait{it} }
        }
        pair.second.cancel()
        resultBean
    }.forEach {
        nbGandalf += it.nbVoteGandalf
        nbDumbledore += it.nbVoteDumbledore
        nbMerlin += it.nbVoteMerlin
        answer++
    }

    var sum = nbGandalf + nbDumbledore + nbMerlin

    println("Gandalf : ${(nbGandalf * 100.0 / sum).format(2)}%")
    println("Dumbledore : ${(nbDumbledore * 100.0 / sum).format(2)}%")
    println("Merlin : ${(nbMerlin * 100.0 / sum).format(2)}%")
    println("Reponse : $answer%")


    println("Done in ${(System.currentTimeMillis() - start)}ms")
}

//Pour afficher avec 2 chiffres après la virgule 12.556.format(2)
fun Double.format(digits: Int) = "%.${digits}f".format(this)

//Récupère les résultats du département e
suspend fun getResultFromDepartment(deprtNumber: Int): ResultBean {
    delay(nextLong(3000))
    if (nextInt(10) == 1) {
        println("Erreur  : $deprtNumber")
        throw TimeoutException("404 sur le département $deprtNumber")
    }
    println("Le département : $deprtNumber a répondu")
    return ResultBean()
}

class ResultBean {
    val nbVoteGandalf = nextInt(10000)
    val nbVoteDumbledore = nextInt(10000)
    val nbVoteMerlin = nextInt(10000)
}