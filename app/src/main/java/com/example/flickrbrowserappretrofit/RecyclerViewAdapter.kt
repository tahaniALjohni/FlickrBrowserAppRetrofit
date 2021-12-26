package com.example.flickrbrowserappretrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrbrowserappretrofit.databinding.ItemRowBinding


class RecyclerViewAdapter(private val listImage: MainActivity, private val photos: ArrayList<Image>): RecyclerView.Adapter<RecyclerViewAdapter.HOLDER>() {
    class HOLDER(val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HOLDER {
        return HOLDER(ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        val photo = photos[position]

        holder.binding.apply {
            tvText.text = photo.title
            Glide.with(listImage).load(photo.link).into(ivImage)
            llRow.setOnClickListener { listImage.openImg(photo.link) }
        } }
    override fun getItemCount() = photos.size
}