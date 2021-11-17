import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.util.Constants
import com.example.musiqal.util.GetDateFromFormate
import com.example.musiqal.util.OnItemVideoInPlayListClickListner
import pl.droidsonroids.gif.GifImageView


class UserPlayListPreviewAdapter(
    val context: Context, val onVideoClick: OnItemVideoInPlayListClickListner
) : RecyclerView.Adapter<UserPlayListPreviewAdapter.SpecialUserItemsViewHolder>() {


    private var _listOfYoutubeItemsInPlaylists: MutableList<Item> = arrayListOf()
    fun setListOfItems(
        listOfYoutubeItemsInPlaylists: List<Item>,
    ) {
        _listOfYoutubeItemsInPlaylists = listOfYoutubeItemsInPlaylists.toMutableList()
        notifyItemInserted(listOfYoutubeItemsInPlaylists.size - 1)
    }

    lateinit var specialUserItemsViewHolder: SpecialUserItemsViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialUserItemsViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_view_item_list_of_videos_in_play_list, null)
        specialUserItemsViewHolder = SpecialUserItemsViewHolder(view)
        return specialUserItemsViewHolder
    }

    override fun onBindViewHolder(holder: SpecialUserItemsViewHolder, position: Int) {
        val currentSongItem = _listOfYoutubeItemsInPlaylists.get(position)
        holder.bind(currentSongItem, (position + 1), onVideoClick, _listOfYoutubeItemsInPlaylists)

    }


    override fun getItemCount(): Int {
        return _listOfYoutubeItemsInPlaylists.size
    }

    class SpecialUserItemsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

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
        ) {


            itemView.setOnClickListener {


                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Log.d("TAG", "bind: ")

                    val videoItem = _listOfYoutubeItemsInPlaylists.get(adapterPosition)
                    onVideoClick.onVideoClick(
                        videoItem,
                        _listOfYoutubeItemsInPlaylists.map { i -> i.snippet.resourceId.videoId },
                        _listOfYoutubeItemsInPlaylists,
                        adapterPosition)

                }
            }

            youtubeItem.snippet.also {
                if (it.title.equals(Constants.PRIVATE_VIDEO)) {
                    txtItemTitle.text = Constants.PRIVATE_VIDEO
                    txtItemPosition.text = currentInflatPosition.toString()
                    txtItemPublish.text = ""
                    txtItemLength.text = ""
                } else {
                    txtItemTitle.text = it.title
                    txtItemPosition.text = currentInflatPosition.toString()
                    txtItemPublish.text = "Added: ${date.parseDate(it.publishedAt)}"

                }

            }


        }


    }


}