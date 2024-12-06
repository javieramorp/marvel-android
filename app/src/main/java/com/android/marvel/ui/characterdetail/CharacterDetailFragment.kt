package com.android.marvel.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.android.marvel.R
import com.android.marvel.common.extensions.loadUrl
import com.android.marvel.common.extensions.observe
import com.android.marvel.databinding.FragmentCharacterDetailBinding
import com.android.marvel.ui.base.BaseFragment
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCharacterDetailBinding
    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharacterDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initFlow(args.characterId)
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.eventsFlow.observe(this) { event ->
            when (event) {
                is SetupUi -> binding.ivCharacterNotAvailable.setOnClickListener { viewModel.didClickOnRetry() }
                is ShowDetail -> with(binding) {
                    ivCharacterNotAvailable.isVisible = false
                    ivCharacter.loadUrl(event.character.thumbnail, R.drawable.logo_marvel)
                    tvName.text = event.character.name
                    tvDescription.text = event.character.description
                }
                is ShowCharacterNotAvailable -> binding.ivCharacterNotAvailable.isVisible = true
                else -> handleEvent(event)
            }
        }
    }
}