package com.mazibahrami.example.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.R
import com.mazibahrami.example.databinding.LayoutBlogListItemBinding
import com.mazibahrami.example.util.GlideManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlogPostListAdapter(
    private val requestManager: GlideManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CLASS_NAME = "BlogPostListAdapter"

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {

        override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemBinding = LayoutBlogListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BlogPostViewHolder(
            itemBinding,
            interaction,
            requestManager
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BlogPostViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BlogPost>) {
        val commitCallback = Runnable {

            /*
                if process died or nav back need to restore layoutmanager AFTER
                data is set... very annoying.
                Not sure why I need the delay... Can't figure this out. I've tested with lists
                100x the size of this one and the 100ms delay works fine.
             */
            CoroutineScope(Main).launch {
                delay(100)
                interaction?.restoreListPosition()
            }
        }

        differ.submitList(list, commitCallback)
    }

    class BlogPostViewHolder
    constructor(
        private val itemBinding: LayoutBlogListItemBinding,
        private val interaction: Interaction?,
        private val requestManager: GlideManager
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: BlogPost) = with(itemBinding) {
            root.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            requestManager
                .setImage(item.image, blogImage)
            blogCategory.text = item.category
            blogTitle.text = item.title
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)

        fun restoreListPosition()
    }
}