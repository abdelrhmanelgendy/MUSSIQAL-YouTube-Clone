import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.R
import com.example.musiqal.search.OnHistoryDataClickListener
import com.example.musiqal.search.database.SearchHistoryData

class YoutubeSearchAdapter(
    val context: Context,
    val onHistoryDataClickListener: OnHistoryDataClickListener
) :
    RecyclerView.Adapter<YoutubeSearchAdapter.YoutubeSearchHolder>() {

     var listOfHistoryData: ArrayList<SearchHistoryData> = arrayListOf()
    fun setList(movieList: List<SearchHistoryData>) {
        Log.d("TAG", "setList: "+movieList.toString())
        listOfHistoryData.clear()
        listOfHistoryData.addAll(movieList)
        notifyDataSetChanged()

    }

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeSearchHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_search, null)
        return YoutubeSearchHolder(view)
    }

    override fun onBindViewHolder(holder: YoutubeSearchHolder, position: Int) {
        val historyDataItem = listOfHistoryData.get(position)
        holder.bind(
            historyDataItem,
            listOfHistoryData,
            context,
            position,
            onHistoryDataClickListener
        )
//        if (selectedPosition == position) {
//        changeColor(holder)
//        } else {
//            holder.itemView.setBackgroundColor(Color.BLACK)
//        }
//        holder.txtViewSearchTitle.setOnLongClickListener(longItemClick)
//        holder.itemView.setOnLongClickListener {
//            selectedPosition = position
//            onHistoryDataClickListener.onDataSearchLongClick(historyDataItem, position)
//            notifyDataSetChanged()
//            return@setOnLongClickListener true
//        }
//        holder.imgHistory.setOnLongClickListener {
//            selectedPosition = position
//            onHistoryDataClickListener.onDataSearchLongClick(historyDataItem, position)
//            notifyDataSetChanged()
//            return@setOnLongClickListener true
//        }

    }

    private fun changeColor(holder: YoutubeSearchHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.itemView.setBackgroundColor(
                context.resources.getColor(
                    R.color.light_gray,
                    context.theme
                )
            )
        } else {
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.light_gray))
        }
    }

    override fun getItemCount(): Int {
        return listOfHistoryData.size
    }

    class YoutubeSearchHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var txtViewSearchTitle: TextView
        lateinit var imgTopArrow: ImageView
        lateinit var imgHistory: ImageView

        init {
            txtViewSearchTitle = itemView.findViewById(R.id.searchItem_txtHIstoryTitle)
            imgTopArrow = itemView.findViewById(R.id.searchItem_imgAssignHistoryToET)
            imgHistory = itemView.findViewById(R.id.searchItem_imgHistory)
        }

        fun bind(
            historyDataItem: SearchHistoryData,
            listOfSearchData: List<SearchHistoryData>,
            context: Context,
            position: Int,
            onHistoryDataClickListener: OnHistoryDataClickListener
        ) {
            txtViewSearchTitle.text = historyDataItem.searchTitle

            imgHistory.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onHistoryDataClickListener.onDataSearchClick(
                        listOfSearchData.get(
                            adapterPosition
                        ), adapterPosition
                    )
                }
            }
            txtViewSearchTitle.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {


                    onHistoryDataClickListener.onDataSearchClick(
                        listOfSearchData.get(
                            adapterPosition
                        ), adapterPosition
                    )

                }
            }


            val longItemClick = object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        Log.d("TAG", "onLongClick: ")
                        if (!listOfSearchData.get(adapterPosition).searchHistory.equals("-1")) {
                            onHistoryDataClickListener.onDataSearchLongClick(
                                listOfSearchData,
                                listOfSearchData.get(
                                    adapterPosition
                                ), adapterPosition
                            )
                        }
                        return true
                    } else {
                        return false
                    }
                }
            }
            txtViewSearchTitle.setOnLongClickListener(longItemClick)
            imgTopArrow.setOnLongClickListener(longItemClick)
            imgHistory.setOnLongClickListener(longItemClick)
            itemView.setOnLongClickListener(longItemClick)
            imgTopArrow.setOnClickListener {
                onHistoryDataClickListener.onTopArrowClick(
                    listOfSearchData.get(adapterPosition),
                    adapterPosition
                )
            }

        }


    }


}