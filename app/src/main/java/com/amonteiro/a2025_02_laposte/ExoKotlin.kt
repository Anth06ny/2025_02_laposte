package com.amonteiro.a2025_02_laposte

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MyLiveData<T>(value:T) {
    var value:T = value
        set(newValue) {
            field = newValue
            action?.invoke(newValue)
        }

    var action : ((T)->Unit)? = null
        set(newValue) {
            field = newValue
            action?.invoke(value)
        }
}



fun main() = runBlocking {

}

data class PersonBean(var name:String, var note:Int)

suspend fun exo3(){


    val list = arrayListOf(PersonBean ("toto", 16),
        PersonBean ("Tata", 20),
        PersonBean ("Toto", 8),
        PersonBean ("Charles", 14))

    println("Afficher la sous liste de personne ayant 10 et +")
    //println(list.filter { it.note >=10 })
    //Pour un affichage de notre choix
    println(list.filter { it.note >=10 }.joinToString("\n") { "-${it.name} : ${it.note}"})

    var isToto : (PersonBean)->Boolean = {it.name.equals("toto",true)}

    //TODO
    println("\n\nAfficher combien il y a de Toto dans la classe ?")
    list.count { isToto(it) }
    list.count(isToto)

    println("\n\nAfficher combien de Toto ayant la moyenne (10 et +)")
    list.count { isToto(it) && it.note >= 10 }

    println("\n\nAfficher combien de Toto ont plus que la moyenne de la classe")
    var average = list.filter { it.name.equals("toto",true) }.map { it.note }.average()
    list.count { it.note> average}

    println("\n\nAfficher la list triée par nom sans doublon")
    println("\n\nAjouter un point a ceux n’ayant pas la moyenne (<10)")
    println("\n\nAjouter un point à tous les Toto")
    println("\n\nRetirer de la liste ceux ayant la note la plus petite. (Il peut y en avoir plusieurs)")


    println("\n\nAfficher les noms de ceux ayant la moyenne(10et+) par ordre alphabétique")

    //TODO Créer une variable isToto contenant une lambda qui teste si un PersonBean s'appelle Toto

    println("\n\nDupliquer la liste ainsi que tous les utilisateurs (nouvelle instance) qu'elle contient")
    var list2 = list.map { it.copy() }


    println("\n\nAfficher par notes croissantes les élèves ayant eu cette note comme sur l'exemple")
}

data class UserBean(var name:String, var old:Int)


fun exo1() {
    //Déclaration
    val lower: (String) -> Unit = { it: String -> println(it.lowercase()) }
    val lower2 = { it: String -> println(it.lowercase()) }
    val lower3: (String) -> Unit = { it -> println(it.lowercase()) }
    val lower4: (String) -> Unit = {         println(it.lowercase()) }

    var max : (Int,Int)-> Int = {a,b -> Math.max(a,b)}

    //Appel
    lower("Coucou")
    println(max(5,6))

    val compareUsersByName :(UserBean, UserBean)->UserBean = {u1, u2 -> if( u1.name.lowercase() <= u2.name.lowercase()) u1 else u2 }

    var minToHour : ((Int?) -> Pair<Int, Int>?)? = {if(it != null) Pair(it/60, it%60) else null}

    println(minToHour?.invoke(125))
    println(minToHour?.invoke(null))
    minToHour = null
}

