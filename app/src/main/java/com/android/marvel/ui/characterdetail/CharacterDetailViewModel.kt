package com.android.marvel.ui.characterdetail

import androidx.lifecycle.viewModelScope
import com.android.marvel.R
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.*
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.GetCharacterUseCase
import com.android.marvel.ui.base.EventObserver
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(private val getCharacterUseCase: GetCharacterUseCase,
                                                   resourcesAccessor: ResourcesAccessor): BaseViewModel(resourcesAccessor) {

    sealed class Event: EventObserver {
        data class SetupUi(val title: String): Event()
        data class ShowDetail(val character: Character): Event()
        data object ShowCharacterNotAvailable: Event()
    }

    private var characterIdCache = 0

    //region private methods
    private fun retrieveCharacter() {
        viewModelScope.launch {
            showLoading(true)
            when(val result = getCharacterUseCase.invoke(characterIdCache)) {
                is Resource.Success -> doEvent(ShowDetail(result.value))
                is Resource.Failure -> {
                    doEvent(ShowCharacterNotAvailable)
                    handleError(result)
                }
            }
            showLoading(false)
        }
    }
    //endregion

    //region inputs
    fun initFlow(characterId: Int) {
        this.characterIdCache = characterId
        doEvent(SetupUi(getString(R.string.character_detail_title)))
        retrieveCharacter()
    }

    fun didClickOnRetry() {
        retrieveCharacter()
    }
    //endregion
}