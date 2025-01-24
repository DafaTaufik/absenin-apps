package com.example.if570_lab_uts_dafataufikallatief_67476.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.if570_lab_uts_dafataufikallatief_67476.MainActivity
import com.example.if570_lab_uts_dafataufikallatief_67476.R
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        registerLink = view.findViewById(R.id.registerLink)

        val spannableString = SpannableString("Don't have an account? Register")
        val registerText = "Register"
        val startIndex = spannableString.indexOf(registerText)
        val endIndex = startIndex + registerText.length

        // Menambahkan underline dan warna biru pada teks "Register"
        spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, 0)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.biruGelap, null)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerLink.text = spannableString


        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNavigation()
    }

}