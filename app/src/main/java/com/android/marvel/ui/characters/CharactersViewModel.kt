package com.android.marvel.ui.characters

import androidx.lifecycle.viewModelScope
import com.android.marvel.R
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetCharactersUseCase
import com.android.marvel.ui.base.EventObserver
import com.android.marvel.ui.base.BaseViewModel
import com.android.marvel.ui.characters.CharactersViewModel.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val getCharactersUseCase: GetCharactersUseCase): BaseViewModel() {

    sealed class Event: EventObserver {
        data class SetupUi(val title: String): Event()
        data class ShowCharacters(val characters: List<Character>): Event()
        object ShowCharactersNotAvailable: Event()
        data class GoToCharacterDetail(val characterId: Int): Event()
    }

    private lateinit var alphabetCache: Array<String>
    private var lastSelectedAlphabetPosition = 0

    //region private methods
    private fun retrieveCharacters(nameStartsWith: String?) {
        viewModelScope.launch {
            showLoading(true)
            when(val result = getCharactersUseCase.invoke(nameStartsWith)) {
                is Resource.Success -> doEvent(ShowCharacters(result.value))
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
    fun initFlow() { /** we use initFlow because resourcesAccessor injection in BaseViewModel is not initiated at init{} call */
        this.alphabetCache = resourcesAccessor.getArray(R.array.alphabet)
        doEvent(SetupUi(getString(R.string.characters_title)))
        retrieveCharacters(alphabetCache.first())
    }

    fun didClickOnCharacter(character: Character) {
        doEvent(GoToCharacterDetail(character.id))
    }

    fun didClickOnLetterAt(position: Int) {
        this.lastSelectedAlphabetPosition = position
        val startsLetterCharacter = alphabetCache[position]
        retrieveCharacters(startsLetterCharacter)
    }

    fun didClickOnRetry() {
        val startsLetterCharacter = alphabetCache[lastSelectedAlphabetPosition]
        retrieveCharacters(startsLetterCharacter)
    }
    //endregion
}