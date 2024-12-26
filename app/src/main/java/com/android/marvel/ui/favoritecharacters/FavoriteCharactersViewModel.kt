package com.android.marvel.ui.favoritecharacters

import androidx.lifecycle.viewModelScope
import com.android.marvel.R
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetFavoriteCharactersUseCase
import com.android.marvel.ui.base.BaseViewModel
import com.android.marvel.ui.base.EventObserver
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.GoToCharacterDetail
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.SetupUi
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.ShowCharacters
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.ShowCharactersNotAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCharactersViewModel @Inject constructor(private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
                                                      resourcesAccessor: ResourcesAccessor): BaseViewModel(resourcesAccessor) {

    sealed class Event: EventObserver {
        data class SetupUi(val title: String): Event()
        data class ShowCharacters(val characters: List<Character>): Event()
        data object ShowCharactersNotAvailable: Event()
        data class GoToCharacterDetail(val characterId: Int): Event()
        data object GoToCharacters: Event()
    }

    private var charactersCache: List<Character>? = null

    //region private methods
    private fun retrieveFavoriteCharacters() {
        viewModelScope.launch {
            showLoading(true)
            when(val result = getFavoriteCharactersUseCase.invoke()) {
                is Resource.Success -> {
                    val characters = result.value
                    if (characters.isEmpty()) {
                        doEvent(Event.GoToCharacters)
                    } else {
                        charactersCache = characters
                        doEvent(ShowCharacters(characters))
                    }
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
        doEvent(SetupUi(getString(R.string.favorite_characters_title)))
        retrieveFavoriteCharacters()
    }

    fun didClickOnCharacter(character: Character) {
        doEvent(GoToCharacterDetail(character.id))
    }

    fun didClickOnRetry() {
        retrieveFavoriteCharacters()
    }
    //endregion
}