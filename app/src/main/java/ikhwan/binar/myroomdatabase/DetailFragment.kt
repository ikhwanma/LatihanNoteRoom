package ikhwan.binar.myroomdatabase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import ikhwan.binar.myroomdatabase.room.AppDatabase
import ikhwan.binar.myroomdatabase.room.Note
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class DetailFragment : Fragment() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())!!
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteData = arguments?.getParcelable<Note>("extra_data") as Note

        tv_judul.text = noteData.judul
        tv_note.text = noteData.catatan
        tv_time.text = noteData.time

        btn_home.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_detailFragment_to_homeFragment)
        }

        btn_edit.setOnClickListener {
            val mBundle = bundleOf("extra_edit" to noteData)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_detailFragment_to_editFragment, mBundle)
        }

        btn_share.setOnClickListener {
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,noteData.catatan)
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }

        btn_delete.setOnClickListener {
            GlobalScope.async {
                val res = appDatabase.appDao().deleteNote(noteData)
                Log.d("resApp", res.toString())
                requireActivity().runOnUiThread {
                    if (res == 1) {
                        Toast.makeText(requireContext(), "Note Dihapus", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_detailFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), "Gagal Menghapus", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


}