package ikhwan.binar.myroomdatabase

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.myroomdatabase.adapter.NoteAdapter
import ikhwan.binar.myroomdatabase.room.AppDatabase
import ikhwan.binar.myroomdatabase.room.Note
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.input_catatan
import kotlinx.android.synthetic.main.fragment_add.input_judul
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.btn_edit
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class EditFragment : Fragment() {

    private lateinit var appDatabase : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())!!
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteData = arguments?.getParcelable<Note>("extra_edit") as Note

        input_judul.setText(noteData.judul)
        input_catatan.setText(noteData.catatan)

        btn_edit.setOnClickListener {
            val judul = input_judul.text.toString()
            val catatan = input_catatan.text.toString()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)
            val note = Note(noteData.id, judul, catatan, noteData.email, formatted.toString())


            GlobalScope.async {
                val res = appDatabase.appDao().updateNote(note)

                requireActivity().runOnUiThread {
                    if (res != 0) {
                        Navigation.findNavController(view).navigate(R.id.action_editFragment_to_homeFragment)
                    }
                }
            }
        }
    }

}