package com.example.bangkit_2024_bpaai.Animation.ActivityAnimation.adapter

import android.app.Activity
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkit_2024_bpaai.Animation.ActivityAnimation.HeroActivity
import com.example.bangkit_2024_bpaai.Animation.ActivityAnimation.model.Hero
import com.example.bangkit_2024_bpaai.R
import androidx.core.util.Pair

class ListHeroAdapter(private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHero[position])
    }

    override fun getItemCount(): Int = listHero.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.profileImageView)
        private var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        private var tvDescription: TextView = itemView.findViewById(R.id.descTextView)

        fun bind(hero: Hero) {
            Glide.with(itemView.context)
                .load(hero.photo)
                .circleCrop()
                .into(imgPhoto)
            tvName.text = hero.name
            tvDescription.text = hero.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, HeroActivity::class.java)
                intent.putExtra("Hero", hero)

                // mendefinisikan transitionName untuk ketiga view
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description"),
                    )

                // supaya animasi bisa berjalan (Activity Transition)
//                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())

                // animasi (Shared Element)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}