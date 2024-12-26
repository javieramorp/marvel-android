package com.android.marvel.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.android.marvel.R
import com.android.marvel.common.extensions.createShareIntent
import com.android.marvel.common.extensions.loadUrl
import com.android.marvel.common.extensions.observe
import com.android.marvel.databinding.FragmentCharacterDetailBinding
import com.android.marvel.ui.base.BaseFragment
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.ChangeFavouriteResource
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.SetupUi
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.ShareContent
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.ShowCharacterNotAvailable
import com.android.marvel.ui.characterdetail.CharacterDetailViewModel.Event.ShowDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCharacterDetailBinding
    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: CharacterDetailFragmentArgs by navArgs()
    private lateinit var actionBarMenu: Menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharacterDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //<editor-fold desc="Add menu item Share and Favourite " defaultState="collapsed">
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.character_detail_menu, menu)
                actionBarMenu = menu
                viewModel.didCreateMenu()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    android.R.id.home -> activity?.finish()
                    R.id.action_share -> viewModel.didClickOnShareContent()
                    R.id.action_favourite -> viewModel.didClickOnFavourite()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)
        //</editor-fold>

        viewModel.initFlow(args.characterId)
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.eventsFlow.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SetupUi -> binding.ivCharacterNotAvailable.setOnClickListener { viewModel.didClickOnRetry() }
                is ShowDetail -> with(binding) {
                    ivCharacterNotAvailable.isVisible = false
                    ivCharacter.loadUrl(event.character.thumbnail, R.drawable.logo_marvel)
                    tvName.text = event.character.name
                    tvDescription.text = event.character.description
                }
                is ShowCharacterNotAvailable -> binding.ivCharacterNotAvailable.isVisible = true
                is ShareContent -> activity?.createShareIntent(event.title, event.infoContent)
                is ChangeFavouriteResource -> actionBarMenu.findItem(R.id.action_favourite).icon = event.resource
                else -> handleEvent(event)
            }
        }
    }
}