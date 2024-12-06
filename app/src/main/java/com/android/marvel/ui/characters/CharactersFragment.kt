package com.android.marvel.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.marvel.R
import com.android.marvel.common.extensions.observe
import com.android.marvel.databinding.FragmentCharactersBinding
import com.android.marvel.ui.base.BaseFragment
import com.android.marvel.ui.characters.CharactersViewModel.Event.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : BaseFragment() {

    private lateinit var binding: FragmentCharactersBinding
    private val viewModel: CharactersViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharactersBinding.inflate(inflater)
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
                is SetupUi -> {
                    binding.lvAlphabet.adapter = activity?.let { activity -> ArrayAdapter(activity, R.layout.row_alphabet, resources.getStringArray(R.array.alphabet)) }
                    binding.lvAlphabet.setOnItemClickListener { _, _, position, _ -> viewModel.didClickOnLetterAt(position) }
                    binding.ivCharactersNotAvailable.setOnClickListener { viewModel.didClickOnRetry() }
                }
                is ShowCharacters -> {
                    binding.ivCharactersNotAvailable.isVisible = false
                    binding.rvCharacters.adapter = CharacterAdapter(event.characters, viewModel::didClickOnCharacter)
                }
                is ShowCharactersNotAvailable -> binding.ivCharactersNotAvailable.isVisible = true
                is GoToCharacterDetail -> findNavController().navigate(CharactersFragmentDirections.actionCharactersToCharacterDetail(event.characterId))
                else -> handleEvent(event)
            }
        }
    }
}