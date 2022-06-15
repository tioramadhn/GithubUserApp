package com.dicoding.githubuserappv2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserappv2.R
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem

class UserAdapter(private val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, avatar) = listUser[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.imgPhoto)
        holder.tvName.text = username
        holder.tvUserName.text = "@${username}"
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount() = listUser.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvUserName: TextView = itemView.findViewById(R.id.tv_item_user_name)
    }

}