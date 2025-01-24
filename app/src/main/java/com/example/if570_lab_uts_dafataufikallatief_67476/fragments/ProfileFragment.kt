package com.example.if570_lab_uts_dafataufikallatief_67476.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.if570_lab_uts_dafataufikallatief_67476.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var nameInput: EditText
    private lateinit var nimInput: EditText
    private lateinit var updateButton: Button
    private lateinit var logOutButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        nameInput = view.findViewById(R.id.nameInput)
        nimInput = view.findViewById(R.id.nimInput)
        updateButton = view.findViewById(R.id.updateButton)
        logOutButton = view.findViewById(R.id.logOutButton)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadProfileData()

        updateButton.setOnClickListener {
            saveProfileData()
        }
        logOutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigasi ke loginFragment setelah logout
            view.findNavController().navigate(R.id.loginFragment)
        }

        return view
    }

    private fun loadProfileData(){
        val userId = auth.currentUser?.uid
        if(userId != null){
            firestore.collection("userse")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if(document.exists()){
                        nameInput.setText(document.getString("name"))
                        nimInput.setText(document.getString("nim"))
                    }else{
                        Toast.makeText(context, "Profile anda masih kosong", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e->
                    Toast.makeText(context, "Profile load failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveProfileData(){
        val userId = auth.currentUser?.uid
        val name = nameInput.text.toString()
        val nim = nimInput.text.toString()

        if (name.isEmpty() || nim.isEmpty()){
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if(userId != null){
            val userProfile = hashMapOf(
                "name" to name,
                "nim" to nim
            )
            firestore.collection("userse")
                .document(userId)
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e->
                    Toast.makeText(context, "Profile update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
        }
    }

}