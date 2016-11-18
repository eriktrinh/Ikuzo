package com.eriktrinh.ikuzo.ui.page

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Character
import com.eriktrinh.ikuzo.utils.ext.loadAndCenterInto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_character_view.view.*

class CharacterAdapter(val context: Context, var characters: List<Character>) : RecyclerView.Adapter<CharacterHolder>() {
    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_character_view, parent, false)

        return CharacterHolder(context, view)
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        holder.bindItem(characters[position])
    }

    fun addItems(items: List<Character>) {
        val oldSize = characters.size
        characters = characters.plus(items)
        for (i in 0..items.size - 1) {
            Picasso.with(context)
                    .load(items[i].imageUrl)
                    .fetch()
        }
        notifyItemRangeInserted(oldSize, items.size)
    }

    fun clearItems() {
        val oldSize = characters.size
        characters = emptyList()
        notifyItemRangeRemoved(0, oldSize)
    }
}

class CharacterHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.character_image
    private val nameText = itemView.character_name
    private val roleText = itemView.character_role

    fun bindItem(character: Character) {
        Picasso.with(context)
                .loadAndCenterInto(character.imageUrl, imageView)
        val name = character.firstName + if (character.lastName != null) " ${character.lastName}" else ""
        nameText.text = if (name.length <= 20) name else "${name.substring(0..19)}…"
        roleText.text = character.role
    }
}