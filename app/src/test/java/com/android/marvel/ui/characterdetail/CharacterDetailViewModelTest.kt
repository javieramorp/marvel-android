package com.android.marvel.ui.characterdetail

import com.android.marvel.common.extensions.assertIfIsEvent
import com.android.marvel.common.extensions.fixture
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.common.utils.MainDispatcherRule
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetCharacterUseCase
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

class CharacterDetailViewModelTest {

    private lateinit var sut: CharacterDetailViewModel
    private lateinit var getCharacterUseCase: GetCharacterUseCase
    private lateinit var resourcesAccessor: ResourcesAccessor

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        resourcesAccessor = mockk(relaxed = true)
        getCharacterUseCase = mockk(relaxed = true)
        sut = CharacterDetailViewModel(getCharacterUseCase, resourcesAccessor)
    }

    @Test
    fun `should initFlow emits correct sequence of events when view appears` () = runTest {
        val characterId = 1
        val title = "Character detail"
        val expectedCharacter = fixture<Character>()

        every { resourcesAccessor.getString(any()) } returns title
        coEvery { getCharacterUseCase.invoke(characterId) } returns Resource.Success(expectedCharacter)

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow(characterId)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events[0].assertIfIsEvent<CharacterDetailViewModel.Event.SetupUi>()
        assert(setupUiEvent.title == title)

        val loadingEventStart = events[1].assertIfIsEvent<BaseEvent.ShowLoading>()
        assert(loadingEventStart.visibility)

        val showCharactersEvent = events[2].assertIfIsEvent<CharacterDetailViewModel.Event.ShowDetail>()
        assert(showCharactersEvent.character == expectedCharacter)

        val loadingEventEnd = events[3].assertIfIsEvent<BaseEvent.ShowLoading>()
        assert(!loadingEventEnd.visibility)

        coVerify(exactly = 1) { resourcesAccessor.getString(any()) }
        coVerify(exactly = 1) { getCharacterUseCase.invoke(characterId) }
    }
}