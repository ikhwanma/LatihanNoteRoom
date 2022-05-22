package ikhwan.binar.myroomdatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import ikhwan.binar.myroomdatabase.room.AppDatabase
import ikhwan.binar.myroomdatabase.room.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.regex.Pattern


class RegisterFragment : Fragment() {

    private var appDatabase: AppDatabase? = null

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private var cek: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appDatabase = AppDatabase.getInstance(requireContext())
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register.setOnClickListener {
            register()
        }

    }

    private fun register() {
        name = input_nama.text.toString()
        email = input_email.text.toString()
        password = input_password.text.toString()
        cek = isValidEmail(email)

        if (inputCheck(name, email, password, cek)) {
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val user = User(email, name, password)
        GlobalScope.async {
            val cekUser = appDatabase?.appDao()?.getUserRegistered(email)
            if (cekUser != null) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "User dengan email ${user.email} sudah terdaftar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val result = appDatabase?.appDao()?.registerUser(user)
                requireActivity().runOnUiThread {
                    if (result != 0.toLong()) {
                        Toast.makeText(
                            requireContext(),
                            "Sukses mendaftarkan ${user.email}, silakan mencoba untuk login",
                            Toast.LENGTH_LONG
                        ).show()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Gagal mendaftarkan ${user.email}, silakan coba lagi",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun inputCheck(name: String, email: String, password: String, cek: Boolean): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || !cek
            || input_konf_password.text.toString() != password || password.length < 6
        ) {
            if (name.isEmpty()) {
                input_nama.setError("Username Tidak Boleh Kosong")
                input_nama.requestFocus()
            }
            if (email.isEmpty()) {
                input_email.setError("Email Tidak Boleh Kosong")
                input_email.requestFocus()
            }
            if (password.isEmpty()) {
                input_password.setError("Password Tidak Boleh Kosong")
                input_password.requestFocus()
            }
            if (!cek) {
                input_email.setError("Email Tidak Sesuai Format")
                input_email.requestFocus()
            }
            if (input_konf_password.text.toString() != password) {
                input_konf_password.setError("Password Tidak Sama")
                input_konf_password.requestFocus()
            }
            if (password.length < 6) {
                input_password.setError("Password minimal 6 karakter")
                input_password.requestFocus()
            }
            return false
        } else {
            return true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

}