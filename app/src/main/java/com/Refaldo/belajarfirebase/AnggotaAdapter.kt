package com.Refaldo.belajarfirebase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class AnggotaAdapter (
    val anggotaContext: Context,
    val layoutResId: Int,
    val anggotaList: List<Anggota>
): ArrayAdapter<Anggota>(anggotaContext, layoutResId, anggotaList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(anggotaContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val  o_nama: TextView = view.findViewById(R.id.ou_nama)
        val  o_kota: TextView = view.findViewById(R.id.ou_kota)
        val  o_usia: TextView = view.findViewById(R.id.ou_usia)
        val  o_noHp: TextView = view.findViewById(R.id.ou_noHp)
        val  imgEdit: ImageView = view.findViewById(R.id.icn_edit)

        val anggota = anggotaList[position]

        imgEdit.setOnClickListener{
            updateDialog(anggota)
        }

        o_nama.text = "Nama : " + anggota.nama
        o_kota.text = "Kota Lahir : " + anggota.kota
        o_usia.text = "Usia : " + anggota.usia + " tahun"
        o_noHp.text = "No Hp : " + anggota.hp

        return view
    }

    private fun updateDialog(anggota: Anggota){
        val builder = AlertDialog.Builder(anggotaContext)
        builder.setTitle("Update Data")
        val inflater = LayoutInflater.from(anggotaContext)
        val view = inflater.inflate(R.layout.activity_update, null)

        val edtNama = view.findViewById<EditText>(R.id.upNama)
        val edtKota = view.findViewById<EditText>(R.id.upKota)
        val edtUsia = view.findViewById<EditText>(R.id.upUsia)
        val edtNoHp = view.findViewById<EditText>(R.id.upNoHp)

        edtNama.setText(anggota.nama)
        edtKota.setText(anggota.kota)
        edtUsia.setText(anggota.usia)
        edtNoHp.setText(anggota.hp)

        builder.setView(view)

        builder.setPositiveButton("Ubah"){ p0, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("anggota")
            val nama = edtNama.text.toString().trim()
            val alamat = edtKota.text.toString().trim()
            val usia = edtUsia.text.toString().trim()
            val noHp = edtNoHp.text.toString().trim()

            if(nama.isEmpty() or alamat.isEmpty() or usia.isEmpty() or noHp.isEmpty()){
                Toast.makeText(anggotaContext, "Isi data secara lengkap! Tidak boleh kosong",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setPositiveButton
            }
            val anggota = Anggota(anggota.id, nama, alamat, usia, noHp)

            dbAnggota.child(anggota.id).setValue(anggota)
            Toast.makeText(anggotaContext, "Data berhasil di update", Toast.LENGTH_SHORT)
                .show()
        }

        builder.setNeutralButton("Batal"){ p0, p1 -> }

        builder.setNegativeButton("Hapus"){ p0, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("anggota")
                .child(anggota.id)
            val dbDetil = FirebaseDatabase.getInstance().getReference("detil anggota")
                .child(anggota.id)

            dbAnggota.removeValue()
            dbDetil.removeValue()

            Toast.makeText(anggotaContext, "Data berhasil di hapus", Toast.LENGTH_SHORT)
                .show()
        }

        val alert = builder.create()
        alert.show()
    }
}