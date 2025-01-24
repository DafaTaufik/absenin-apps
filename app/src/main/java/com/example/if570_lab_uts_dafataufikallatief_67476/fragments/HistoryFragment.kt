package com.example.if570_lab_uts_dafataufikallatief_67476.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.if570_lab_uts_dafataufikallatief_67476.AbsenceAdapter
import com.example.if570_lab_uts_dafataufikallatief_67476.R
import com.example.if570_lab_uts_dafataufikallatief_67476.model.Absence
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var absenceAdapter: AbsenceAdapter
    private lateinit var emptyStateImage: ImageView
    private lateinit var emptyText: TextView
    private var absenceList: MutableList<Absence> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        emptyStateImage = view.findViewById(R.id.emptyStateImage)
        emptyText = view.findViewById(R.id.emptyStateText)

        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchAbsenceData()

        return view
    }

    private fun fetchAbsenceData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("userse").document(userId)
                .collection("absences")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    absenceList.clear()
                    for (document in querySnapshot) {
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val date = document.getString("date") ?: ""
                        val time = document.getString("time") ?: ""
                        val status = document.getString("status") ?: "" // Ambil status dari Firestore
                        val absence = Absence(imageUrl, date, time, status) // status
                        absenceList.add(absence)
                    }
                    if (absenceList.isEmpty()) {
                        // Jika tidak ada tampilkan gambar,teks empty state
                        recyclerView.visibility = View.GONE
                        emptyStateImage.visibility = View.VISIBLE
                        emptyText.visibility = View.VISIBLE
                    } else {
                        // Jika ada tampilkan RecyclerView
                        absenceAdapter = AbsenceAdapter(absenceList)
                        recyclerView.adapter = absenceAdapter
                        recyclerView.visibility = View.VISIBLE
                        emptyStateImage.visibility = View.GONE
                        emptyText.visibility = View.GONE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error fetching absence data: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
