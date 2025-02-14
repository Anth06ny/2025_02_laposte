package com.amonteiro.a2025_02_laposte

import com.amonteiro.a2025_02_laposte.model.DescriptionBean
import com.amonteiro.a2025_02_laposte.model.TempBean
import com.amonteiro.a2025_02_laposte.model.WeatherBean
import com.amonteiro.a2025_02_laposte.model.WeatherRepository
import com.amonteiro.a2025_02_laposte.model.WindBean
import com.amonteiro.a2025_02_laposte.viewmodel.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainViewModelTest {

    //Dispatcher pour les Coroutines, pilotables à  l'aide de advanceUntilIdle()
    private val testDispatcher = StandardTestDispatcher()
    private val viewModel = MainViewModel(testDispatcher)

    init {
        mockkObject(WeatherRepository)
        every { WeatherRepository.loadWeathers("Paris") }.returns(getParisFakeResult())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadWeather() = runTest(testDispatcher) {

        // Vérifier l'état avant le lancement de la coroutine
        assertFalse(viewModel.runInProgress.value)


        // Appeler la méthode à tester
        viewModel.loadWeathers("Paris")

        // Vérifier l'état avant le lancement de la coroutine
        assertTrue(viewModel.runInProgress.value)

        // Avancer l'exécution des coroutines jusqu'à l'état courant
        advanceUntilIdle()

        // Vérifier l'état avant le lancement de la coroutine
        assertFalse(viewModel.runInProgress.value)
        assertTrue(viewModel.dataList.value.isNotEmpty())

        //Mockk
        //On vérifie que loadWeathers("Paris") à bien été appelé
        verify { WeatherRepository.loadWeathers("Paris") }

        //On vérifie qu'aucun autre appel à WeatherRepository à été effectué
        confirmVerified(WeatherRepository)

        //TODO Tester que le titre et l'id du 1er élément sont bien ceux retournés par getParisFakeResult()
        assertEquals(viewModel.dataList.value.first().title, getParisFakeResult().first.name)
        assertEquals(viewModel.dataList.value.first().id, getParisFakeResult().first.id)

    }

    fun getParisFakeResult() = arrayListOf(
        WeatherBean(
            id = 1,
            name = "Paris",
            main = TempBean(temp = 20.0),
            wind = WindBean(speed = 5.0),
            weather = listOf(DescriptionBean(description = "Ensoleillé", icon = "01d"))
        )
    )
}