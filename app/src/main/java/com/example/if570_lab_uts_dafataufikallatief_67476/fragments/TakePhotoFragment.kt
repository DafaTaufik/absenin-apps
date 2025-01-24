package com.example.if570_lab_uts_dafataufikallatief_67476.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.if570_lab_uts_dafataufikallatief_67476.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TakePhotoFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var cancelButton: Button
    private lateinit var retryButton: Button
    private lateinit var yesAbsenButton: Button
    private var photoBitmap: Bitmap? = null

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_take_photo, container, false)

        imageView = view.findViewById(R.id.imageView)
        cancelButton = view.findViewById(R.id.cancelButton)
        retryButton = view.findViewById(R.id.retryButton)
        yesAbsenButton = view.findViewById(R.id.yesAbsenButton)

        // Cancel button navigates back to home
        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_to_homeFragment)
        }

        // Retry button to retake photo
        retryButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        // Absen button to upload the photo
        yesAbsenButton.setOnClickListener {
            photoBitmap?.let { uploadPhotoToFirebase(it) }
        }
        if (photoBitmap == null) {
            dispatchTakePictureIntent()
        } else {
            // If photo already exists, show it in ImageView
            imageView.setImageBitmap(photoBitmap)
        }
        return view
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                photoBitmap = it
                imageView.setImageBitmap(it)
            }
        }
    }

    private fun uploadPhotoToFirebase(bitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance().reference
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoRef = storageRef.child("images/absen_$timestamp.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = photoRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            // Get the download URL of the uploaded image
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                // Save additional information to Firestore
                saveAbsenceToFirestore(imageUrl)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAbsenceToFirestore(imageUrl: String) {
        // Get the current user ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Get the current date
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            // Check if there's an existing absence record for the current date
            val db = FirebaseFirestore.getInstance()
            db.collection("userse").document(userId)
                .collection("absences")
                .whereEqualTo("date", currentDate)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val status: String
                    if (querySnapshot.isEmpty) {
                        status = "datang"
                    } else {
                        status = "pulang"
                    }

                    // Get the current time
                    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                    val absenceData = hashMapOf(
                        "imageUrl" to imageUrl,
                        "date" to currentDate,
                        "time" to currentTime,
                        "status" to status
                    )

                    // Save the data in Firestore
                    db.collection("userse").document(userId)
                        .collection("absences")
                        .add(absenceData)  // This will generate an Auto-ID for the document
                        .addOnSuccessListener {
                            Toast.makeText(context, "Absence data saved successfully!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_to_homeFragment)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to save data: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error checking absence data: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
