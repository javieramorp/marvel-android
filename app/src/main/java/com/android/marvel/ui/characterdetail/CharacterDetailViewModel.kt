package com.android.marvel.ui.characterdetail

import android.graphics.drawable.Drawable
import androidx.lifecycle.viewModelScope
import com.android.marvel.R
import com.android.marvel.common.infrastructure.ResourcesAccessor
import com.android.marvel.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.*
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.usecases.DeleteFavoriteCharacterUseCase
import com.android.marvel.domain.usecases.GetCharacterUseCase
import com.android.marvel.domain.usecases.IsFavoriteCharacterUseCase
import com.android.marvel.domain.usecases.SetFavoriteCharacterUseCase
import com.android.marvel.ui.base.EventObserver
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(private val getCharacterUseCase: GetCharacterUseCase,
                                                   private val isFavoriteCharacterUseCase: IsFavoriteCharacterUseCase,
                                                   private val setFavoriteCharacterUseCase: SetFavoriteCharacterUseCase,
                                                   private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
                                                   private val resourcesAccessor: ResourcesAccessor): BaseViewModel(resourcesAccessor) {

    sealed class Event: EventObserver {
        data class SetupUi(val title: String): Event()
        data class ShowDetail(val character: Character): Event()
        data class ShareContent(val title: String, val infoContent: String): Event()
        data class ChangeFavouriteResource(val resource: Drawable?): Event()
        data object ShowCharacterNotAvailable: Event()
    }

    private var characterIdCache = 0
    private var characterCache: Character? = null
    private var isFavoriteCharacterCache = false

    //region private methods
    private fun retrieveCharacter() {
        viewModelScope.launch {
            showLoading(true)
            when(val result = getCharacterUseCase.invoke(characterIdCache)) {
                is Resource.Success -> {
                    characterCache = result.value
                    doEvent(ShowDetail(result.value))
                }
                is Resource.Failure -> {
                    doEvent(ShowCharacterNotAvailable)
                    handleError(result)
                }
            }
            showLoading(false)
        }
    }

    private fun setFavoriteCharacter() {
        characterCache?.let { character ->
            viewModelScope.launch {
                showLoading(true)
                when (val result = setFavoriteCharacterUseCase.invoke(character)) {
                    is Resource.Success -> isFavoriteCharacter()
                    is Resource.Failure -> handleError(result)
                }
                showLoading(false)
            }
        } ?: showMessage(R.string.common_error_operation_fail)
    }

    private fun deleteFavoriteCharacter() {
        characterCache?.let { character ->
            viewModelScope.launch {
                showLoading(true)
                when (val result = deleteFavoriteCharacterUseCase.invoke(character)) {
                    is Resource.Success -> isFavoriteCharacter()
                    is Resource.Failure -> handleError(result)
                }
                showLoading(false)
            }
        } ?: showMessage(R.string.common_error_operation_fail)
    }

    private fun isFavoriteCharacter() {
        viewModelScope.launch {
            showLoading(true)
            when(val result = isFavoriteCharacterUseCase.invoke(characterIdCache)) {
                is Resource.Success -> {
                    isFavoriteCharacterCache = result.value
                    changeFavoriteUi()
                }
                is Resource.Failure -> handleError(result)
            }
            showLoading(false)
        }
    }

    private fun changeFavoriteUi() {
        val drawableResourceId = if (isFavoriteCharacterCache) R.drawable.ic_star_selected else R.drawable.ic_star_unselected
        doEvent(ChangeFavouriteResource(resourcesAccessor.getDrawable(drawableResourceId)))
    }
    //endregion

    //region inputs
    fun initFlow(characterId: Int) {
        this.characterIdCache = characterId
        doEvent(SetupUi(getString(R.string.character_detail_title)))
        retrieveCharacter()
    }

    fun didCreateMenu() {
        isFavoriteCharacter()
    }

    fun didClickOnShareContent() {
        characterCache?.let {
            doEvent(ShareContent(it.name, it.description))
        }
    }

    fun didClickOnFavourite() {
        if (isFavoriteCharacterCache) {
            deleteFavoriteCharacter()
        } else {
            setFavoriteCharacter()
        }
    }

    fun didClickOnRetry() {
        retrieveCharacter()
    }
    //endregion
}