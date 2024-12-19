package com.android.marvel.ui.characters

import com.android.marvel.common.extensions.assertIfIsEvent
import com.android.marvel.common.extensions.fixture
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.common.utils.MainDispatcherRule
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetCharactersUseCase
import com.android.marvel.ui.base.BaseEvent
import com.android.marvel.ui.base.EventObserver
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class CharactersViewModelTest {

    private lateinit var sut: CharactersViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var resourcesAccessor: ResourcesAccessor

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        resourcesAccessor = mock()
        getCharactersUseCase = mock()
        sut = CharactersViewModel(getCharactersUseCase, resourcesAccessor)
    }

    @Test
    fun `should initFlow emits correct sequence of events when app starts` () = runTest {
        val title = fixture<String>()
        val alphabetCache = arrayOf("a", "b", "c")
        val expectedCharacters = listOf(fixture<Character>(), fixture<Character>())

        whenever(resourcesAccessor.getArray(any())).thenReturn(alphabetCache)
        whenever(resourcesAccessor.getString(any())).thenReturn(title)
        whenever(getCharactersUseCase.invoke(alphabetCache.first())).thenReturn(Resource.Success(expectedCharacters))

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events[0].assertIfIsEvent<CharactersViewModel.Event.SetupUi>()
        assertTrue(setupUiEvent.title == title)

        val loadingEventStart = events[1].assertIfIsEvent<BaseEvent.ShowLoading>()
        assertTrue(loadingEventStart.visibility)

        val showCharactersEvent = events[2].assertIfIsEvent<CharactersViewModel.Event.ShowCharacters>()
        assertTrue(showCharactersEvent.characters == expectedCharacters)

        val loadingEventEnd = events[3].assertIfIsEvent<BaseEvent.ShowLoading>()
        assertFalse(loadingEventEnd.visibility)

        verify(resourcesAccessor, times(1)).getArray(any())
        verify(resourcesAccessor, times(1)).getString(any())
    }

    @Test
    fun `should initFlow emits correct sequence of events when app already starts` () = runTest {
        val title = fixture<String>()
        val alphabetCache = arrayOf("a", "b", "c")
        val expectedCharacters = listOf(fixture<Character>(), fixture<Character>())

        whenever(resourcesAccessor.getArray(any())).thenReturn(alphabetCache)
        whenever(resourcesAccessor.getString(any())).thenReturn(title)

        val charactersCacheField = CharactersViewModel::class.java.getDeclaredField("charactersCache")
        charactersCacheField.isAccessible = true
        charactersCacheField.set(sut, expectedCharacters)

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events.first().assertIfIsEvent<CharactersViewModel.Event.SetupUi>()
        assertTrue(setupUiEvent.title == title)

        val showCharactersEvent = events.last().assertIfIsEvent<CharactersViewModel.Event.ShowCharacters>()
        assertTrue(showCharactersEvent.characters == expectedCharacters)

        verify(resourcesAccessor, times(1)).getArray(any())
        verify(resourcesAccessor, times(1)).getString(any())
        verify(getCharactersUseCase, times(0)).invoke(any())
    }
}