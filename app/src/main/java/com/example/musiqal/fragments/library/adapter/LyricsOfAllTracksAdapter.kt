import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.fragments.library.adapter.OnLyricsTrackClickListener
import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import com.example.musiqal.util.GlideLoader


class LyricsOfAllTracksAdapter(
    val context: Context,
    val onTrackClickListener: OnLyricsTrackClickListener
) :
    RecyclerView.Adapter<LyricsOfAllTracksAdapter.HistoryOfPlayedTracksViewHolder>() {

    private val playedTracks: ArrayList<SharedPrefLyricsLocalDataModel> = arrayListOf()
    fun setList(tracks: List<SharedPrefLyricsLocalDataModel>) {
        playedTracks.addAll(tracks)
        notifyItemInserted(tracks.size - 1)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryOfPlayedTracksViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.history_playlist_recyclerview_item, null)
        return HistoryOfPlayedTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryOfPlayedTracksViewHolder, position: Int) {
        val track = playedTracks.get(position)
        holder.bind(track, playedTracks, onTrackClickListener, position)

    }

    override fun getItemCount(): Int {

        return playedTracks.size
    }

    class HistoryOfPlayedTracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val glideLoader = GlideLoader()

        var trackImage: ImageView
        var trackName: TextView
        var channelName: TextView

        init {
            trackImage = itemView.findViewById(R.id.historyPlaylistItem_ImgSong)
            trackName = itemView.findViewById(R.id.historyPlaylistItem_TV_songName)
            channelName = itemView.findViewById(R.id.historyPlaylistItem_TV_ChannelName)

        }


        fun bind(
            track: SharedPrefLyricsLocalDataModel,
            playedTracks: java.util.ArrayList<SharedPrefLyricsLocalDataModel>,
            onTrackClickListener: OnLyricsTrackClickListener,
            position: Int
        ) {
            val clickedPosition = adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    val clickedTrack = playedTracks.get(clickedPosition)
                    onTrackClickListener.onTrackClick(
                        clickedTrack, adapterPosition,playedTracks
                    )
                }
            }


            glideLoader.loadImage(trackImage, track.songThumbnails)
            trackName.setText(track.songName)
            channelName.setText(track.songDuration)


        }


    }


}