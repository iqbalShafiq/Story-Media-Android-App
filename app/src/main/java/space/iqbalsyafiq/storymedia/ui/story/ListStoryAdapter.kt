package space.iqbalsyafiq.storymedia.ui.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.item_story.view.*
import space.iqbalsyafiq.storymedia.databinding.ItemStoryBinding
import space.iqbalsyafiq.storymedia.model.Story

class ListStoryAdapter(
    private val fragment: ListStoryFragment,
    private val listStory: List<Story>
) : RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {

    private var _binding: ItemStoryBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]

        with(holder.itemView) {
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

    override fun getItemCount(): Int = listStory.size
}