import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.musiqal.R
import com.example.musiqal.downloadManager.ui.adapters.DownloadItemsInfo

class DownloadManagerRecyclerViewAdapter(
    val context: Context,
) :
    RecyclerView.Adapter<DownloadManagerRecyclerViewAdapter.DownloadManagerHolder>() {

    private var items: List<DownloadItemsInfo> = arrayListOf()
    fun setList(movieList: List<DownloadItemsInfo>) {
        items=(movieList)
        notifyDataSetChanged()

    }
    fun updateItem(movieList: List<DownloadItemsInfo>,position: Int)
    {
        items=movieList
        notifyItemChanged(position)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadManagerHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_dowload_item, null)
        return DownloadManagerHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadManagerHolder, position: Int) {
        val downloadInfo = items.get(position)
        holder.bind(downloadInfo,items,position,context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DownloadManagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var txtTrackName:TextView
        lateinit var txtDownloadDetails:TextView
        lateinit var txtDownloadState:TextView
        lateinit var downloadProgress:ProgressBar
        lateinit var trackImage:ImageView
        init {
            txtTrackName=itemView.findViewById(R.id.recyclerViewDownloadItem_trackName)
            txtDownloadState=itemView.findViewById(R.id.recyclerViewDownloadItem_state)
            txtDownloadDetails=itemView.findViewById(R.id.recyclerViewDownloadItem_trackDownloadingDetails)
            downloadProgress=itemView.findViewById(R.id.recyclerViewDownloadItem_progressBar)
            trackImage=itemView.findViewById(R.id.recyclerViewDownloadItem_imgTrackThumb)
        }
        fun bind(
            downloadInfo: DownloadItemsInfo,
            items: List<DownloadItemsInfo>,
            position: Int,
            context: Context
        ) {
            txtTrackName.setText(downloadInfo.filename)
            txtDownloadState.setText(downloadInfo.state.toString())
            txtDownloadDetails.setText(downloadInfo.progress.toInt().toString())
            downloadProgress.setProgress(downloadInfo.progress.toInt())
//            Glide.with(context).load(downloadInfo.trackImage)
//                .into(trackImage)
        }


    }


}