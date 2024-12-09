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
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CharactersViewModelTest {

    private lateinit var sut: CharactersViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var resourcesAccessor: ResourcesAccessor

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        resourcesAccessor = mockk(relaxed = true)
        getCharactersUseCase = mockk(relaxed = true)
        sut = CharactersViewModel(getCharactersUseCase, resourcesAccessor)
    }

    @Test
    fun `should initFlow emits correct sequence of events when app starts` () = runTest {
        val title = "Characters"
        val alphabetCache = arrayOf("a", "b", "c")
        val expectedCharacters = listOf(fixture<Character>(), fixture<Character>())

        every { resourcesAccessor.getArray(any()) } returns alphabetCache
        every { resourcesAccessor.getString(any()) } returns title
        coEvery { getCharactersUseCase.invoke(alphabetCache.first()) } returns Resource.Success(expectedCharacters)

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events[0].assertIfIsEvent<CharactersViewModel.Event.SetupUi>()
        assert(setupUiEvent.title == title)

        val loadingEventStart = events[1].assertIfIsEvent<BaseEvent.ShowLoading>()
        assert(loadingEventStart.visibility)

        val showCharactersEvent = events[2].assertIfIsEvent<CharactersViewModel.Event.ShowCharacters>()
        assert(showCharactersEvent.characters == expectedCharacters)

        val loadingEventEnd = events[3].assertIfIsEvent<BaseEvent.ShowLoading>()
        assert(!loadingEventEnd.visibility)

        coVerify(exactly = 1) { resourcesAccessor.getArray(any()) }
        coVerify(exactly = 1) { getCharactersUseCase.invoke(alphabetCache.first()) }
    }

    @Test
    fun `should initFlow emits correct sequence of events when app already starts` () = runTest {
        val title = "Characters"
        val alphabetCache = arrayOf("a", "b", "c")
        val expectedCharacters = listOf(fixture<Character>(), fixture<Character>())

        every { resourcesAccessor.getArray(any()) } returns alphabetCache
        every { resourcesAccessor.getString(any()) } returns title

        val charactersCacheField = CharactersViewModel::class.java.getDeclaredField("charactersCache")
        charactersCacheField.isAccessible = true
        charactersCacheField.set(sut, expectedCharacters)

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events.first().assertIfIsEvent<CharactersViewModel.Event.SetupUi>()
        assert(setupUiEvent.title == title)

        val showCharactersEvent = events.last().assertIfIsEvent<CharactersViewModel.Event.ShowCharacters>()
        assert(showCharactersEvent.characters == expectedCharacters)

        coVerify(exactly = 1) { resourcesAccessor.getArray(any()) }
        coVerify(exactly = 1) { resourcesAccessor.getString(any()) }
        coVerify(exactly = 0) { getCharactersUseCase.invoke(any()) }
    }
}