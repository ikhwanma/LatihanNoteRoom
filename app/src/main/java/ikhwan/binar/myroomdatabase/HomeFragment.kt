package ikhwan.binar.myroomdatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.myroomdatabase.adapter.NoteAdapter
import ikhwan.binar.myroomdatabase.datastore.DataStoreManager
import ikhwan.binar.myroomdatabase.room.AppDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var appDatabase : AppDatabase
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())!!
        dataStoreManager = DataStoreManager(requireContext())
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_add.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_addFragment)
        }

        fab_profile.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        getList()
    }

    private fun getList() {
        dataStoreManager.email.asLiveData().observe(viewLifecycleOwner){
            GlobalScope.launch {
                val res = appDatabase.appDao().getNote(it)

                requireActivity().runOnUiThread {
                    if (res.isNotEmpty()) {
                        rv_note.layoutManager = LinearLayoutManager(requireContext())
                        rv_note.adapter = NoteAdapter(res){
                            val mBundle = bundleOf("extra_data" to it)
                            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, mBundle)
                        }
                    }
                }

            }
        }
    }

}