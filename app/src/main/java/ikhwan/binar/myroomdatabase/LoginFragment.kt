package ikhwan.binar.myroomdatabase


import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.myroomdatabase.datastore.DataStoreManager
import ikhwan.binar.myroomdatabase.room.AppDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btn_register
import kotlinx.android.synthetic.main.fragment_login.input_email
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var appDatabase : AppDatabase? = null

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var dataStoreManager: DataStoreManager
    private var cek: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())
        dataStoreManager = DataStoreManager(requireContext())
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager.email.asLiveData().observe(viewLifecycleOwner){
            if (it != ""){
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        btn_register.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        email = input_email.text.toString()
        password = input_password.text.toString()

        loginUser(email, password)
    }

    private fun loginUser(email: String, password: String) {
        GlobalScope.async {
            val user = appDatabase?.appDao()?.getUserRegistered(email)
            requireActivity().runOnUiThread{
                if (user != null) {
                    if (email == user.email && password == user.password){
                        GlobalScope.launch {
                            dataStoreManager.saveData(email)
                        }
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        Toast.makeText(requireContext(), "Password yang anda masukkan salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Akun dengan email ${email} belum terdaftar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}