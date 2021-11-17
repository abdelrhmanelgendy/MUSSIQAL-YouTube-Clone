import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.helpers.CalenderHelper
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util.OnTrackClickListener
import com.example.musiqal.util.GlideLoader
import com.example.musiqal.util.ImageUrlUtil
import kotlin.collections.ArrayList


class RecentlyPlayedTracksAdapter(
    val context: Context,
    val onTrackClickListener: OnTrackClickListener,

) :
    RecyclerView.Adapter<RecentlyPlayedTracksAdapter.HistoryOfPlayedTracksViewHolder>() {

    private val playedTracks: ArrayList<Item> = arrayListOf()
    fun setList(tracks: List<Item>) {
        playedTracks.addAll(tracks.reversed())
        notifyItemInserted(tracks.size - 1)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryOfPlayedTracksViewHolder {
        val view =
            LayoutInflater.from(context).inflate(
                R.layout.recycler_view_item_recently_played_tracks, null
            )
        return HistoryOfPlayedTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryOfPlayedTracksViewHolder, position: Int) {
        val track = playedTracks.get(position)
        holder.bind(track, playedTracks, onTrackClickListener, position)
        Log.d("TAG", "onBindViewHolder: "+position)

    }

    override fun getItemCount(): Int {

        return playedTracks.size
    }

    class HistoryOfPlayedTracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val glideLoader = GlideLoader()

        var trackImage: ImageView
        var trackName: TextView
        var playedDate: TextView


         var calenderHelper: CalenderHelper
        init {
            trackImage = itemView.findViewById(R.id.recyclerViewItemRecentlyPlayed_trackImage)
            trackName = itemView.findViewById(R.id.recyclerViewItemRecentlyPlayed_trackName)
            playedDate = itemView.findViewById(R.id.recyclerViewItemRecentlyPlayed_trackDate)
            calenderHelper= CalenderHelper()

        }


        fun bind(
            track: Item,
            playedTracks: java.util.ArrayList<Item>,
            onTrackClickListener: OnTrackClickListener,
            position: Int
        ) {
            val clickedPosition = adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    val track = playedTracks.get(clickedPosition)
                    onTrackClickListener.onVideoClick(
                        track, listOf(), playedTracks, adapterPosition
                    )
                }
            }


            glideLoader.loadImage(trackImage, ImageUrlUtil.getMeduimResolutionImageUrl(track))
            trackName.setText(track.snippet.title)


            playedDate.setText(track.snippet.channelTitle)



        }


    }


}