package com.capstone.gometry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.gometry.R
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.databinding.CardGeometryBinding
import com.capstone.gometry.utils.ViewExtensions.setImageFromResource
import com.capstone.gometry.utils.ViewExtensions.setVisible

class GeometryAdapter : ListAdapter<Geometry, GeometryAdapter.ViewHolder>(DiffUtilCallback) {
    private lateinit var onStartActivityCallback: OnStartActivityCallback

    inner class ViewHolder(private var binding: CardGeometryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, geometry: Geometry) {
                val tvPointText = when {
                    geometry.passed -> context.getString(R.string.geometry_passed)
                    geometry.locked -> context.getString(R.string.locked)
                    else -> context.getString(R.string.default_point)
                }

                binding.apply {
                    tvName.text = geometry.name
                    tvPoint.text = tvPointText
                    ivLocked.setVisible(geometry.locked)
                    ivPreview.setImageFromResource(context, geometry.preview)
                    root.setOnClickListener {
                        onStartActivityCallback.onStartActivityCallback(geometry)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardGeometryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val geometry = getItem(position)
        holder.bind(holder.itemView.context, geometry)
    }

    fun setOnStartActivityCallback(onStartActivityCallback: OnStartActivityCallback) {
        this.onStartActivityCallback = onStartActivityCallback
    }

    interface OnStartActivityCallback {
        fun onStartActivityCallback(geometry: Geometry)
    }

    companion object {
        private val DiffUtilCallback = object : DiffUtil.ItemCallback<Geometry>() {
            override fun areItemsTheSame(oldItem: Geometry, newItem: Geometry): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Geometry, newItem: Geometry): Boolean =
                oldItem == newItem
        }
    }
}