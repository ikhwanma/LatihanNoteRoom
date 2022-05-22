package ikhwan.binar.myroomdatabase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ikhwan.binar.myroomdatabase.R
import ikhwan.binar.myroomdatabase.room.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(private val listNote: List<Note>, private val onClick : (Note) -> Unit) :  RecyclerView.Adapter<NoteAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listNote[position]

        holder.itemView.apply {
            tv_judul.text = data.judul
            tv_note.text = data.catatan
            tv_time.text = data.time
            rootView.setOnClickListener {
                onClick(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

}