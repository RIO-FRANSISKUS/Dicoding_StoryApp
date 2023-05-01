package com.example.storyapp.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ItemUsersBinding
import com.example.storyapp.response.Story

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Story>(){
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
        StoryViewHolder(ItemUsersBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val stories = differ.currentList[position]
        val binding = ItemUsersBinding.bind(holder.itemView)

        binding.apply {
            Glide.with(binding.root)
                .load(stories.photoUrl)
                .into(ivItemPhoto)

            tvItemName.text = stories.name
        }

        setOnItemClickListener {
            onItemClickListener?.let {
                it(stories)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Story) -> Unit)? = null

    fun setOnItemClickListener(listener: (Story) -> Unit){
        onItemClickListener = listener
    }
}