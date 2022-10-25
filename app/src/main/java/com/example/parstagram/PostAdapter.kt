package com.example.parstagram

import android.content.Context
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val posts: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position : Int){
        val post = posts.get(position)
        val user = User()
        post.getUser()?.let { user.setUser(it) }
        holder.bind(post, user)
    }
    override fun getItemCount(): Int {
        return posts.size
    }
    fun clear() {

        posts.clear()

        notifyDataSetChanged()

    }
    fun addAll(newPosts: List<Post>) {

        posts.addAll(newPosts)

        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val tvUsername : TextView
         val ivImage : ImageView
        val ivPPic : ImageView
         val tvDescription: TextView

         init{
             tvUsername = itemView.findViewById(R.id.tvUserName)
             tvDescription = itemView.findViewById(R.id.tvDescription)
             ivImage = itemView.findViewById(R.id.ivImage)
             ivPPic = itemView.findViewById(R.id.ivProfilePic)
         }

        fun bind(post: Post, user: User){
            tvDescription.text = post.getDescription()
            tvUsername.text = post.getUser()?.username

            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
            Glide.with(itemView.context).load(user.getProfilePic()?.url).into(ivPPic)
        }
    }
}