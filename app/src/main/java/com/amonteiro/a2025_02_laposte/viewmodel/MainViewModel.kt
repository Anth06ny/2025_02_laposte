package com.amonteiro.a2025_02_laposte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a2025_02_laposte.model.WeatherRepository
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

class MainViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

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