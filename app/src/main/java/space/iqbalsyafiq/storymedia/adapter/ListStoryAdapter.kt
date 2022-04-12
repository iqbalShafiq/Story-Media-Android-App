package space.iqbalsyafiq.storymedia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import space.iqbalsyafiq.storymedia.databinding.ItemStoryBinding
import space.iqbalsyafiq.storymedia.model.Story
import space.iqbalsyafiq.storymedia.ui.story.ListStoryFragment

class ListStoryAdapter(
    private val fragment: ListStoryFragment
) : PagingDataAdapter<Story, ListStoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var _binding: ItemStoryBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story, fragment: ListStoryFragment) {
            with(binding) {
                tvName.text = story.name
                Glide.with(fragment)
                    .asBitmap()
                    .fitCenter()
                    .transform(RoundedCorners(16))
                    .load(story.photoUrl)
                    .into(ivStoryImage)
                clItem.setOnClickListener {
                    fragment.goToDetail(story)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        val story = getItem(position)
        if (story != null) {
            holder.bind(story, fragment)
        } else {
            Log.d(TAG, "onBindViewHolder: null")
        }
    }

    companion object {
        private val TAG = ListStoryAdapter::class.java.simpleName
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}