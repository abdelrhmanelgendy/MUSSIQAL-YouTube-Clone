import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.datamodels.youtubeItemInList.ItemInPlayListPreview
import com.example.musiqal.util.Constants
import com.example.musiqal.util.GetDateFromFormate
import com.example.musiqal.util.OnItemVideoInPlayListClickListner
import pl.droidsonroids.gif.GifImageView


class ItemsInPlayListAdapter(
    val context: Context, val onVideoClick: OnItemVideoInPlayListClickListner
) : RecyclerView.Adapter<ItemsInPlayListAdapter.SpecialUserItemsViewHolder>() {


    private var _listOfYoutubeItemsInPlaylists: MutableList<ItemInPlayListPreview> = arrayListOf()
    fun setListOfItems(
        listOfYoutubeItemsInPlaylists: List<ItemInPlayListPreview>,
        selectedItem: Int
    ) {
        _listOfYoutubeItemsInPlaylists = listOfYoutubeItemsInPlaylists.toMutableList()
        try {
            notifyDataSetChanged()

        } catch (e: Exception) {
        }
    }

    lateinit var specialUserItemsViewHolder: SpecialUserItemsViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialUserItemsViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_view_item_list_of_videos_in_play_list, null)
        specialUserItemsViewHolder = SpecialUserItemsViewHolder(view)
        return specialUserItemsViewHolder
    }

    override fun onBindViewHolder(holder: SpecialUserItemsViewHolder, position: Int) {
        val itemsInPlayList = _listOfYoutubeItemsInPlaylists.get(position).item



        holder.bind(
            itemsInPlayList,
            (position + 1),
            onVideoClick,
            _listOfYoutubeItemsInPlaylists.map { i -> i.item }.toMutableList(),
            position,
            _listOfYoutubeItemsInPlaylists
        )

    }


    override fun getItemCount(): Int {
        return _listOfYoutubeItemsInPlaylists.size
    }

    class SpecialUserItemsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val TAG = "ItemsInPlayListAdapter"
        var txtItemTitle: TextView =
            itemView.findViewById(R.id.recyclerViewItemListOfVideos_txtVideoName)
        var txtItemPosition: TextView =
            itemView.findViewById(R.id.recyclerViewItemListOfVideos_txtPosistion)
        var txtItemLength: TextView =
            itemView.findViewById(R.id.recyclerViewItemListOfVideos_txtVideoLength)
        var txtItemPublish: TextView =
            itemView.findViewById(R.id.recyclerViewItemListOfVideos_TV_VideoPublish)
        var imageViewPlayNow: GifImageView =
            itemView.findViewById(R.id.recyclerViewItemListOfVideos_gifImagePlayNow)
        val date = GetDateFromFormate("yyyy-MM-dd'T'HH:mm:ss")


        fun bind(
            youtubeItem: Item,
            currentInflatPosition: Int,
            onVideoClick: OnItemVideoInPlayListClickListner,
            _listOfYoutubeItemsInPlaylists: MutableList<Item>,
            position: Int,
            _listOfSelectableYoutubeItemsInPLayListPreview: MutableList<ItemInPlayListPreview>
        ) {
            youtubeItem.snippet.also {
                if (it.title.equals(Constants.PRIVATE_VIDEO)) {
                    txtItemTitle.text = Constants.PRIVATE_VIDEO
                    txtItemPosition.text = currentInflatPosition.toString()
                    txtItemPublish.text = ""
                    txtItemLength.text = ""
                } else {
                    txtItemTitle.text = it.title
                    txtItemPosition.text = currentInflatPosition.toString()
                    txtItemPublish.text = "publish: ${date.parseDate(it.publishedAt)}"
                    txtItemLength.text = ""
                }

            }


            imageViewPlayNow.visibility =getVisibilityOfNowPlaying(_listOfSelectableYoutubeItemsInPLayListPreview.get(position).isSelected)

            itemView.setOnClickListener {

                val currentAdapterPosition = adapterPosition
                if (currentAdapterPosition != RecyclerView.NO_POSITION) {
                    val videoItem = _listOfYoutubeItemsInPlaylists.get(currentAdapterPosition)

                    onVideoClick.onVideoClick(
                        videoItem,
                        _listOfYoutubeItemsInPlaylists.map { i -> i.snippet.resourceId.videoId },
                        _listOfYoutubeItemsInPlaylists,
                        position,
                        _listOfSelectableYoutubeItemsInPLayListPreview

                    )

                }
            }
        }

        private fun getVisibilityOfNowPlaying(selected: Boolean): Int {
            if (selected)
            {
                return View.VISIBLE
            }else
            {
                return View.INVISIBLE
            }

        }


    }


}