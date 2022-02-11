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
import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import com.example.musiqal.util.GlideLoader
import kotlin.collections.ArrayList


class RecentlyFoundLyricsAdapter(
    val context: Context,
    val onLyricsAdapterItemClickListener: OnLyricsAdapterItemClickListener,

    ) :
    RecyclerView.Adapter<RecentlyFoundLyricsAdapter.HistoryOfPlayedTracksViewHolder>() {

    private val playedTracks: ArrayList<SharedPrefLyricsLocalDataModel> = arrayListOf()
    fun setList(tracks: List<SharedPrefLyricsLocalDataModel>) {
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
        holder.bind(track, playedTracks, onLyricsAdapterItemClickListener, position)

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
            calenderHelper = CalenderHelper()

        }


        fun bind(
            track: SharedPrefLyricsLocalDataModel,
            playedTracks: java.util.ArrayList<SharedPrefLyricsLocalDataModel>,
            onTrackClickListener: OnLyricsAdapterItemClickListener,
            position: Int
        ) {
            val clickedPosition = adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    val clickedTrack = playedTracks.get(clickedPosition)
                    onTrackClickListener.onLyricsClick(clickedPosition,clickedTrack)
                }
            }


            glideLoader.loadImage(trackImage, track.songThumbnails)
            trackName.setText(track.songName)


            playedDate.setText(track.songDuration)


        }


    }

    interface OnLyricsAdapterItemClickListener {
        fun onLyricsClick(position: Int, localLyricsDataModel: SharedPrefLyricsLocalDataModel)
    }


}