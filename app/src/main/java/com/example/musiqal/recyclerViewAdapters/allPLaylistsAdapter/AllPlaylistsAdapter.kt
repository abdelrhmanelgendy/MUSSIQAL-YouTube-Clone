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
import com.example.musiqal.userPLaylists.dialogs.adapter.OnPlayListClickListener
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.util.GlideLoader


class AllPlaylistsAdapter(
    val context: Context,
    val onPlayListClickListener: OnPlayListClickListener
) :
    RecyclerView.Adapter<AllPlaylistsAdapter.HistoryOfPlayedTracksViewHolder>() {

    private val userPlaylists: ArrayList<UserPlayList> = arrayListOf()
    fun setList(playlists: List<UserPlayList>) {
        userPlaylists.addAll(playlists)
        notifyItemInserted(playlists.size - 1)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryOfPlayedTracksViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_view_item_all_playlists, null)
        return HistoryOfPlayedTracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryOfPlayedTracksViewHolder, position: Int) {
        val playlist = userPlaylists.get(position)
        holder.bind(playlist, position, userPlaylists,onPlayListClickListener)


    }

    override fun getItemCount(): Int {

        return userPlaylists.size
    }

    class HistoryOfPlayedTracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val glideLoader = GlideLoader()
        val calenderHelper = CalenderHelper()
        var imgOPtion: ImageView
        var playListImage: ImageView
        var playlistName: TextView
        var playlistDescription: TextView

        init {
            imgOPtion = itemView.findViewById(R.id.allPLaylistsItem_imgOption)
            playListImage = itemView.findViewById(R.id.allPLaylistsItem_playlistImage)
            playlistName = itemView.findViewById(R.id.allPLaylistsItem_playlistName)
            playlistDescription = itemView.findViewById(R.id.allPLaylistsItem_playlistDescription)

        }

        fun bind(
            currentPlaylist: UserPlayList,
            position: Int,
            userPlaylists: ArrayList<UserPlayList>,
            onPlayListClickListener: OnPlayListClickListener
        ) {

            glideLoader.loadImage(playListImage, currentPlaylist.playListCoverUrl)
            playlistName.setText(currentPlaylist.playListName)
            calenderHelper.setCalender(currentPlaylist.playLisLastUpdate.toLong())
            playlistDescription.setText("Last update: ${calenderHelper.toString()} .${currentPlaylist.playListItems.size} tracks")

            if (adapterPosition!=RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    onPlayListClickListener.onPLaylistClick(userPlaylists.get(adapterPosition))
                }
            }
            if (adapterPosition!=RecyclerView.NO_POSITION)
            {
                imgOPtion.setOnClickListener {
                    Log.d("TAG", "bind: ")
                }
            }

        }


    }


}