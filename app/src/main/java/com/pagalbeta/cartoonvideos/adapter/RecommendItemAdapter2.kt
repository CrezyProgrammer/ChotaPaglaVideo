package com.pagalbeta.cartoonvideos.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pagalbeta.cartoonvideos.PlayActivity
import com.pagalbeta.cartoonvideos.R
import com.pagalbeta.cartoonvideos.databinding.LayoutBinding
import com.pagalbeta.cartoonvideos.entity.Video

class FacebookAdsRecyclerviewAdapter2(
    private val activity: Activity,
    private val list: ArrayList<Video>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val inflate =
                LayoutInflater.from(parent.context).inflate(R.layout.layout, parent, false)
return            ItemViewHolder(inflate)


    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

               val item = list[position]

               holder as ItemViewHolder
               item.let { holder.bind(item) }
           }

    }

    class ItemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {



        fun bind(item: Video) {


            val binding= LayoutBinding.bind(itemView)
            Glide.with(itemView.context).load("https://img.youtube.com/vi/${item.id}/hqdefault.jpg").into(
                binding.imageView
            )
            binding.textView.text=item.title
            binding.views.text="${item.views} views"
            binding.date.text="${item.date} ago"
            binding.duration.text=item.duration
            binding.linearLayout.setOnClickListener {
                (it.context as Activity).finish()
                it.context.startActivity(
                    Intent(it.context, PlayActivity::class.java).putExtra(
                        "title",
                        item.title
                    ).putExtra("id", item.id)
                )

            }
        }
    }


