package com.example.if570_lab_uts_dafataufikallatief_67476.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.example.if570_lab_uts_dafataufikallatief_67476.MainActivity
import com.example.if570_lab_uts_dafataufikallatief_67476.R
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        registerButton = view.findViewById(R.id.registerButton)
        loginLink = view.findViewById(R.id.loginLink)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                            // Navigasi ke homeFragment setelah registrasi berhasil
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginLink.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNavigation()
    }
}