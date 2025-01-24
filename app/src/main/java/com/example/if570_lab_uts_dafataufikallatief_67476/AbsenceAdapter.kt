package com.example.if570_lab_uts_dafataufikallatief_67476

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.if570_lab_uts_dafataufikallatief_67476.model.Absence

class AbsenceAdapter(private val absenceList: List<Absence>) : RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder>() {

    inner class AbsenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val absenceImageView: ImageView = itemView.findViewById(R.id.absenceImageView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView) // Tambahkan TextView untuk status

        fun bind(absence: Absence) {
            // Load image using Glide or Picasso
            Glide.with(itemView.context)
                .load(absence.imageUrl)
                .into(absenceImageView)

            dateTextView.text = "Absen pada: ${absence.date}"
            timeTextView.text = "Pukul: ${absence.time}"
            statusTextView.text = "Status: ${absence.status}" // Tampilkan status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absence, parent, false)
        return AbsenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsenceViewHolder, position: Int) {
        holder.bind(absenceList[position])
    }

    override fun getItemCount(): Int {
        return absenceList.size
    }
}
