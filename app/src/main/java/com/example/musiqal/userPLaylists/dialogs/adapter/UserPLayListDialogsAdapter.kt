import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.userPLaylists.dialogs.adapter.OnPlayListClickListener
import com.example.musiqal.userPLaylists.model.UserPlayList


class UserPLayListDialogsAdapter(
    val context: Context,
    val onPlayListClickListner: OnPlayListClickListener
) :
    RecyclerView.Adapter<UserPLayListDialogsAdapter.UserPLayListDialogsViewHolder>() {

    private val _listOfPLaylists: ArrayList<UserPlayList> = arrayListOf()
    fun setList(listOfPLaylists: List<UserPlayList>) {
        _listOfPLaylists.clear()
        _listOfPLaylists.addAll(listOfPLaylists)
        notifyItemInserted(listOfPLaylists.size - 1)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPLayListDialogsViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.recycler_view_item_user_playlists, null
        )
        return UserPLayListDialogsViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserPLayListDialogsViewHolder, position: Int) {
        val userplaylist = _listOfPLaylists.get(position)
        holder.bind(userplaylist, onPlayListClickListner, _listOfPLaylists)

    }

    override fun getItemCount(): Int {
        return _listOfPLaylists.size
    }

    class UserPLayListDialogsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        var playListNameTV: TextView

        init {
            playListNameTV = itemView.findViewById(R.id.recyclerViewItemUserPlatList_TV_name)
        }

        fun bind(
            userplaylist: UserPlayList,
            onPlayListClickListner: OnPlayListClickListener,
            _listOfPLaylists: ArrayList<UserPlayList>
        ) {
            playListNameTV.setText(userplaylist.playListName)
            itemView.setOnClickListener {
                val adapterPosition = adapterPosition
                if (adapterPosition != -1) {
                    onPlayListClickListner.onPLaylistClick(_listOfPLaylists.get(adapterPosition))
                }
            }

        }


    }


}