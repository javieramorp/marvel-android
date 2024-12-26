package com.android.marvel.ui.favoritecharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.marvel.R
import com.android.marvel.common.extensions.observe
import com.android.marvel.databinding.FragmentFavoriteCharactersBinding
import com.android.marvel.ui.base.BaseFragment
import com.android.marvel.ui.characters.CharacterAdapter
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.GoToCharacterDetail
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.SetupUi
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.ShowCharacters
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.ShowCharactersNotAvailable
import com.android.marvel.ui.favoritecharacters.FavoriteCharactersViewModel.Event.GoToCharacters
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCharactersFragment : BaseFragment() {

    private lateinit var binding: FragmentFavoriteCharactersBinding
    private val viewModel: FavoriteCharactersViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteCharactersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initFlow()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.eventsFlow.observe(this) { event ->
            when (event) {
                is SetupUi -> binding.ivCharactersNotAvailable.setOnClickListener { viewModel.didClickOnRetry() }
                is ShowCharacters -> with(binding) {
                    ivCharactersNotAvailable.isVisible = false
                    rvCharacters.adapter = CharacterAdapter(event.characters, viewModel::didClickOnCharacter)
                }
                is ShowCharactersNotAvailable -> binding.ivCharactersNotAvailable.isVisible = true
                is GoToCharacterDetail -> findNavController().navigate(FavoriteCharactersFragmentDirections.actionFavoriteCharactersToCharacterDetail(event.characterId))
                is GoToCharacters -> findNavController().popBackStack(R.id.fragment_characters, false)
                else -> handleEvent(event)
            }
        }
    }
}