package com.android.marvel.ui.characters

import androidx.lifecycle.viewModelScope
import com.android.marvel.R
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetCharactersUseCase
import com.android.marvel.ui.base.BaseViewModel
import com.android.marvel.ui.base.EventObserver
import com.android.marvel.ui.characters.CharactersViewModel.Event.GoToCharacterDetail
import com.android.marvel.ui.characters.CharactersViewModel.Event.SetupUi
import com.android.marvel.ui.characters.CharactersViewModel.Event.ShowCharacters
import com.android.marvel.ui.characters.CharactersViewModel.Event.ShowCharactersNotAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val getCharactersUseCase: GetCharactersUseCase,
                                              private val resourcesAccessor: ResourcesAccessor): BaseViewModel(resourcesAccessor) {

    sealed class Event: EventObserver {
        data class SetupUi(val title: String): Event()
        data class ShowCharacters(val characters: List<Character>): Event()
        data object ShowCharactersNotAvailable: Event()
        data class GoToCharacterDetail(val characterId: Int): Event()
    }

    private lateinit var alphabetCache: Array<String>
    private var lastSelectedAlphabetIndex = 0
    private var charactersCache: List<Character>? = null

    //region private methods
    private fun retrieveCharacters(nameStartsWith: String?) {
        viewModelScope.launch {
            showLoading(true)
            when(val result = getCharactersUseCase.invoke(nameStartsWith)) {
                is Resource.Success -> {
                    val characters = result.value
                    charactersCache = characters
                    doEvent(ShowCharacters(characters))
                }
                is Resource.Failure -> {
                    doEvent(ShowCharactersNotAvailable)
                    handleError(result)
                }
            }
            showLoading(false)
        }
    }
    //endregion

    //region inputs
    fun initFlow() {
        this.alphabetCache = resourcesAccessor.getArray(R.array.alphabet)
        doEvent(SetupUi(getString(R.string.characters_title)))

        charactersCache?.let {
            doEvent(ShowCharacters(it))
        } ?: retrieveCharacters(alphabetCache.first())
    }

    fun didClickOnCharacter(character: Character) {
        doEvent(GoToCharacterDetail(character.id))
    }

    fun didClickOnLetterAt(position: Int) {
        this.lastSelectedAlphabetIndex = position
        val startsLetterCharacter = alphabetCache[position]
        retrieveCharacters(startsLetterCharacter)
    }

    fun didClickOnRetry() {
        val startsLetterCharacter = alphabetCache[lastSelectedAlphabetIndex]
        retrieveCharacters(startsLetterCharacter)
    }
    //endregion
}