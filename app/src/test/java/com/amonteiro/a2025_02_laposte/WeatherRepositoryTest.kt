package com.amonteiro.a2025_02_laposte

import com.amonteiro.a2025_02_laposte.model.WeatherRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class WeatherRepositoryTest {

    @Test
    fun loadWeatherNiceTest(){
       val list =  WeatherRepository.loadWeathers("Nice")
       assertTrue(list.isNotEmpty())

        for (city in list) {
            assertTrue("Le nom ne contient pas Nice", city.name.contains("Nice", true))
            assertTrue("La température n'est pas entre -40 et 60°", city.main.temp in -40.0..60.0)
            assertTrue("La description est vide", city.weather.isNotEmpty())
            assertTrue("Il n'y a pas d'icône", city.weather[0].icon.isNotBlank())
        }
    }

    @Test(expected = Exception::class)
    fun loadWeathersEmptyString(){
        WeatherRepository.loadWeathers("")
    }
}