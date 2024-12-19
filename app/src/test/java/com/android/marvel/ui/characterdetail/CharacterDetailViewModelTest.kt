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

class CharacterDetailViewModelTest {

    private lateinit var sut: CharacterDetailViewModel
    private lateinit var getCharacterUseCase: GetCharacterUseCase
    private lateinit var resourcesAccessor: ResourcesAccessor

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        resourcesAccessor = mock()
        getCharacterUseCase = mock()
        sut = CharacterDetailViewModel(getCharacterUseCase, resourcesAccessor)
    }

    @Test
    fun `should initFlow emits correct sequence of events when view appears` () = runTest {
        val characterId = 1
        val title = fixture<String>()
        val expectedCharacter = fixture<Character>()

        whenever(resourcesAccessor.getString(any())).thenReturn(title)
        whenever(getCharacterUseCase.invoke(characterId)).thenReturn(Resource.Success(expectedCharacter))

        val events = mutableListOf<EventObserver>()
        val job = launch { sut.eventsFlow.toList(events) }

        sut.initFlow(characterId)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val setupUiEvent = events[0].assertIfIsEvent<CharacterDetailViewModel.Event.SetupUi>()
        assertTrue(setupUiEvent.title == title)

        val loadingEventStart = events[1].assertIfIsEvent<BaseEvent.ShowLoading>()
        assertTrue(loadingEventStart.visibility)

        val showCharactersEvent = events[2].assertIfIsEvent<CharacterDetailViewModel.Event.ShowDetail>()
        assertTrue(showCharactersEvent.character == expectedCharacter)

        val loadingEventEnd = events[3].assertIfIsEvent<BaseEvent.ShowLoading>()
        assertFalse(loadingEventEnd.visibility)

        verify(resourcesAccessor, times(1)).getString(any())
        verify(getCharacterUseCase, times(1)).invoke(characterId)
    }
}