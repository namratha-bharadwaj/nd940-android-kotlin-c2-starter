package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.models.Asteroid

class MainAsteroidAdapter(private val clickListener: AsteroidListener):
    ListAdapter<Asteroid, MainAsteroidAdapter.AsteroidViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val dataBinding = AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context))
        return AsteroidViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)

        holder.apply {
            this.itemView.setOnClickListener {
                clickListener.onClick(asteroid)
            }
            this.bind(asteroid)
        }
    }

    class AsteroidViewHolder(var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) {
            clickListener(asteroid)

        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }
}