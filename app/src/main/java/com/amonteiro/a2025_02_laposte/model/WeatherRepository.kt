package com.amonteiro.a2025_02_laposte.model

import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request

const val URL_API_WEATHER =
    "https://api.openweathermap.org/data/2.5/find?q=%s&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr"

fun main() = runBlocking {
//    val list = WeatherRepository.loadWeathers("Nice")
//    //val list = WeatherRepository.loadWeathersFake("Nice")
//    for(w in list){
//        println("Il fait ${w.main.temp}° à ${w.name}(id=${w.id}) avec un vent de ${w.wind.speed} m/s\n")
//        println("-Description : ${w.weather.getOrNull(0)?.description}")
//        println("-Icon : ${w.weather.getOrNull(0)?.icon}")
//    }

    val flow = WeatherRepository.getWeathers("Nice", "Paris")

    flow.filter { it.wind.speed > 0 }.collect{
        println(it)
    }

}

object WeatherRepository {

    val client = OkHttpClient()
    val gson = Gson()


    fun loadWeathers(city: String): List<WeatherBean> {
        val json = sendGet(URL_API_WEATHER.format(city))
        //delay(3000)
        return gson.fromJson(json, WeatherAroundBean::class.java).list
    }

    fun getWeathers(vararg cities:String) = flow<WeatherBean> {
        cities.forEach { cityName->
            loadWeathers(cityName).forEach {
                emit(it)
            }
            delay(1000)
        }
    }

    fun loadWeathersFake(city: String): List<WeatherBean> {
        return listOf(
            WeatherBean(
                id = 1,
                name = "$city - Centre",
                main = TempBean(temp = 18.5),
                wind = WindBean(speed = 5.2),
                weather = listOf(DescriptionBean(description = "Ciel dégagé", icon = "01d"))
            ),
            WeatherBean(
                id = 2,
                name = "$city - Nord",
                main = TempBean(temp = 15.3),
                wind = WindBean(speed = 3.8),
                weather = listOf(DescriptionBean(description = "Nuageux", icon = "03d"))
            ),
            WeatherBean(
                id = 3,
                name = "$city - Sud",
                main = TempBean(temp = 20.1),
                wind = WindBean(speed = 6.0),
                weather = listOf(DescriptionBean(description = "Pluie légère", icon = "10d"))
            )
        )
    }


    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        return client.newCall(request).execute().use {
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Réponse du serveur incorrect :${it.code}\n${it.body.string()}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }
}

//Objet de base retourné par l'API
data class WeatherAroundBean(var list:List<WeatherBean>)

//Ici je n'ai mis que ce qui est utile pour l'affichage demandé mais on peut tout mettre
data class WeatherBean(
    var id:Int,
    var name: String,
    var main: TempBean,
    var wind : WindBean,
    var weather : List<DescriptionBean>
)

data class TempBean(var temp: Double)
data class WindBean(var speed: Double)
data class DescriptionBean(var description: String, var icon:String )