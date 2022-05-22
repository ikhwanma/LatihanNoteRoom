package ikhwan.binar.myroomdatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import ikhwan.binar.myroomdatabase.datastore.DataStoreManager
import ikhwan.binar.myroomdatabase.room.AppDatabase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var appDatabase : AppDatabase
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())!!
        dataStoreManager = DataStoreManager(requireContext())
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreManager.email.asLiveData().observe(viewLifecycleOwner){
            GlobalScope.async {
                val user = appDatabase.appDao().getUserRegistered(it)
                requireActivity().runOnUiThread {
                    val txt = "Hello, ${user.nama.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }}"
                    tv_username.text = txt
                    tv_email.text = user.email
                }
            }
        }

        btn_logout.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment)
            GlobalScope.launch {
                dataStoreManager.saveData("")
            }
        }
    }
}