import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musiqal.R
import com.example.musiqal.datamodels.youtubeApiSearchForVideo.Item
import com.example.musiqal.util.MakingToast
import com.example.musiqal.util.OnSearchedItemClickListner
import com.example.musiqal.youtubeAudioVideoExtractor.YouTubeDurationConverter
//import com.example.musiqal.util.YouTubeVideoDurationFilter
import kotlin.collections.ArrayList


class SearchResultAdapter(
    private val context: Context,
    private val onSearchItemClickListner: OnSearchedItemClickListner
) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private val listOfSearchResults: ArrayList<Item> = arrayListOf()

    fun setList(videoslist: List<Item>) {
        listOfSearchResults.clear()
        listOfSearchResults.addAll(videoslist)
        Log.d("TAG", "setList1: " + videoslist)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search_result, null)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val videoItem = listOfSearchResults.get(position)
        holder.bind(videoItem, context, onSearchItemClickListner, listOfSearchResults)

    }

    override fun getItemCount(): Int {
        return listOfSearchResults.size
    }

    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgVideoImage: ImageView
        var imgVideoOptions: ImageView
        var txtVideotitle: TextView
        var txtChannelName: TextView
        var txtVideoLength: TextView
        var cardViewLive: CardView

        init {
            imgVideoImage = itemView.findViewById(R.id.searchResultItem_img_VideoImage)
            imgVideoOptions = itemView.findViewById(R.id.searchResultItem_img_options)
            txtVideotitle = itemView.findViewById(R.id.searchResultItem_TV_txtTitle)
            txtChannelName = itemView.findViewById(R.id.searchResultItem_TV_txtChannelName)
            txtVideoLength = itemView.findViewById(R.id.searchResultItem_TV_txtVideoDuration)
            cardViewLive = itemView.findViewById(R.id.searchResultItem_item_live)
        }

        fun bind(
            videoItem: Item,
            context: Context,
            onSearchItemClickListner: OnSearchedItemClickListner,
            listOfSearchResults: ArrayList<Item>
        ) {
            txtVideotitle.text = videoItem.snippet.title
            txtChannelName.text = videoItem.snippet.channelTitle

            Glide.with(context)
                .load(videoItem.snippet.thumbnails.high.url)
                .into(imgVideoImage)

            Log.d("TAG", "bind: " + videoItem.videoDuration)
            //P0D

            if (videoItem.videoDuration.equals("P0D")) {
                //Show live tag and hide txt
                txtVideoLength.isVisible = false
                cardViewLive.isVisible = true
            } else {
                //hide live tage and show txt
                txtVideoLength.isVisible = true
                cardViewLive.isVisible = false
                txtVideoLength.text =
                    YouTubeDurationConverter.getTimeInStringFormated(videoItem.videoDuration)


            }


            itemView.setOnClickListener {

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val currentVideoDuration =
                        listOfSearchResults.get(adapterPosition).videoDuration
                    if (currentVideoDuration.equals("P0D")) {
                        MakingToast(context).toast(
                            "sorry playing live videos is not avaiblable now",
                            MakingToast.LENGTH_SHORT
                        )
                    } else {
                        val searchedVideo = listOfSearchResults.get(adapterPosition)
                        onSearchItemClickListner.onSearchResultClick(
                            searchedVideo,
                            listOfSearchResults,
                            adapterPosition
                        )
                    }
                }
            }

        }


    }


}