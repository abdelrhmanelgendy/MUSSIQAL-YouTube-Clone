import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Item
import com.example.musiqal.util.Constants
import com.example.musiqal.util.OnPlayListItemClickListner
import com.squareup.picasso.Picasso

class SpecialUserItemsAdapter(
    val context: Context,
    val viewID: Int,
    val _listOfYoutubePlaylists: List<Item>,
    val onPlayListItemClickListner: OnPlayListItemClickListner

) :
    RecyclerView.Adapter<SpecialUserItemsAdapter.SpecialUserItemsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialUserItemsViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(viewID, null)
        return SpecialUserItemsViewHolder(view, viewID)
    }

    override fun onBindViewHolder(holder: SpecialUserItemsViewHolder, position: Int) {
        val youtubePlayList = _listOfYoutubePlaylists.get(position)
        holder.bind(
            youtubePlayList,
            onPlayListItemClickListner,
            _listOfYoutubePlaylists,
            getSavedImageQualityFromPresistence()
        )

    }

    private fun getSavedImageQualityFromPresistence(): Int {
        val imageQuality =
            context.getSharedPreferences(Constants.SHARED_PREF_USER_SETTINGS, Context.MODE_PRIVATE)
                .getInt(Constants.IMAGE_QUALITY, Constants.IMAQE_HIGH_QUALITY)
        return imageQuality
    }

    override fun getItemCount(): Int {
        return _listOfYoutubePlaylists.size
    }

    class SpecialUserItemsViewHolder(itemView: View, viewID: Int) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var imag: ImageView
        lateinit var txtViewName: TextView
        lateinit var txtViewDescription: TextView

        init {
            when (viewID) {
                R.layout.special_recycler_view_playlist_items_no_border -> {
                    imag =
                        itemView.findViewById(R.id.specialRecyclerViewItem_NoBorder_imagePlayList)
                    txtViewName =
                        itemView.findViewById(R.id.specialRecyclerViewItem_NoBorder__TV_PlayListName)
                    txtViewDescription =
                        itemView.findViewById(R.id.specialRecyclerViewItem_NoBorder__TV_PlayListDescription)
                }
                R.layout.special_recycler_view_playlist_items -> {
                    imag = itemView.findViewById(R.id.specialRecyclerViewItem_imagePlayList)
                    txtViewName =
                        itemView.findViewById(R.id.specialRecyclerViewItem_TV_PlayListName)
                    txtViewDescription =
                        itemView.findViewById(R.id.specialRecyclerViewItem_TV_PlayListDescription)
                }
            }
        }


        fun bind(
            youtubePlayList: Item,
            onPlayListItemClickListner: OnPlayListItemClickListner,
            _listOfYoutubePlaylists: List<Item>,
            imageQuality: Int
        ) {

            var imageUrl = ""
            when (imageQuality) {
                Constants.IMAQE_Default_QUALITY -> imageUrl =
                    youtubePlayList.snippet.thumbnails.default.url
                Constants.IMAQE_MEDIAM_QUALITY -> imageUrl =
                    youtubePlayList.snippet.thumbnails.medium.url
                Constants.IMAQE_HIGH_QUALITY -> imageUrl =
                    youtubePlayList.snippet.thumbnails.high.url

            }
            txtViewName.setText(youtubePlayList.snippet.title)
            txtViewDescription.setText(youtubePlayList.snippet.description)
            Picasso.get().load(imageUrl).resize(300, 500)
                .centerCrop()
                .error(R.color.black)
                .into(imag)
            val currentAdapterPosition = adapterPosition
            if (currentAdapterPosition != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    onPlayListItemClickListner.onItemClick(
                        _listOfYoutubePlaylists.get(
                            currentAdapterPosition
                        )
                    )

                }

            }


        }


    }


}