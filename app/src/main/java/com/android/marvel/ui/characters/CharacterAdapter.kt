package com.android.marvel.ui.characters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.android.marvel.R
import com.android.marvel.common.extensions.loadUrl
import com.android.marvel.databinding.RowCharacterBinding
import com.android.marvel.domain.models.Character

class CharacterAdapter(private val characters: List<Character>, private val listener: (Character) -> Unit): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character, listener)
    }

    class ViewHolder(private val binding: RowCharacterBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, listener: (Character) -> Unit) = with(binding) {
            tvName.text = character.name
            sivAvatar.loadUrl(character.thumbnail)
            rlLayerRow.setShapeBackground()
            itemView.setOnClickListener { listener(character) }
        }

        private fun View.setShapeBackground() {
            val cornerSizeRightSide = resources.getDimension(R.dimen.ranking_row_avatar) + resources.getDimension(R.dimen.margin_8)
            val cornerSizeLeftSide = resources.getDimension(R.dimen.margin_8)
            background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setBottomRightCornerSize(cornerSizeRightSide / 2)
                    .setTopRightCornerSize(cornerSizeRightSide / 2)
                    .setBottomLeftCornerSize(cornerSizeLeftSide)
                    .setTopLeftCornerSize(cornerSizeLeftSide)
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_character_row))
            }
        }
    }
}