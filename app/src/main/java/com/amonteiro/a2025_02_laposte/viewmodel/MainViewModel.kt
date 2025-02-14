package com.amonteiro.a2025_02_laposte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a2025_02_laposte.model.WeatherRepository
import com.amonteiro.a2025_02_laposte.model.WeatherRepository.loadWeathers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Paris")


    while (viewModel.runInProgress.value) {
        delay(500)
        println("On attend...")
    }

    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}" )
}

data class PictureBean(val id: Int, val url: String, val title: String, val longText: String)

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

class MainViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {//Création d'un jeu de donnée au démarrage
        loadFakeData()
    }

    fun loadFakeData(runInProgress :Boolean = false, errorMessage:String = "" ) {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT),
            PictureBean(2, "https://picsum.photos/201", "BCDE", LONG_TEXT),
            PictureBean(3, "https://picsum.photos/202", "CDEF", LONG_TEXT),
            PictureBean(4, "https://picsum.photos/203", "EFGH", LONG_TEXT)
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }

    fun loadWeathers(cityName: String) {

        runInProgress.value = true


        viewModelScope.launch(dispatcher) {
            try {
                dataList.value = WeatherRepository.loadWeathers(cityName).map {
                    PictureBean(
                        id = it.id,
                        url = "https://openweathermap.org/img/wn/01d@4x.png",
                        title = it.name,
                        longText = "Il fait ${it.main.temp}° à ${it.name}(id=${it.id}) avec un vent de ${it.wind.speed} m/s"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur"
            }
            finally {
                runInProgress.value = false
            }
        }


    }
}

