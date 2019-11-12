package space.siy.blogmemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemoListAdapter(
    val data: MutableList<Memo>,
    private val listener: Listener,
    val context: Context
) : RecyclerView.Adapter<MemoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = data[position]
        holder.run {
            titleTextView.text = data[position].title
            contentTextView.text = data[position].content
            itemView.setOnClickListener { listener.onEdit(memo) }
            deleteButton.setOnClickListener { listener.onDelete(memo) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textView_title)
        val contentTextView: TextView = itemView.findViewById(R.id.textView_content)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)
    }

    interface Listener {
        fun onEdit(memo: Memo)
        fun onDelete(memo: Memo)
    }
}
