package com.Refaldo.belajarfirebase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edNama : EditText
    private lateinit var edHP : EditText
    private lateinit var edUsia : EditText
    private lateinit var edKota : EditText
    private lateinit var btnSimpan : Button
    private lateinit var listData : ListView
    private lateinit var ref : DatabaseReference
    private lateinit var anggotaList : MutableList<Anggota>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("anggota")

        edNama = findViewById(R.id.edtNama)
        edHP = findViewById(R.id.edtNoHP)
        edUsia = findViewById(R.id.edtUsia)
        edKota = findViewById(R.id.edtKota)
        btnSimpan = findViewById(R.id.btnSimpan)
        listData = findViewById(R.id.lv_hasil)

        btnSimpan.setOnClickListener(this)
        anggotaList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    anggotaList.clear()
                    for (a in snapshot.children){
                        val anggota = a.getValue(Anggota::class.java)
                        if (anggota != null){
                            anggotaList.add(anggota)
                        }
                    }
                    val adapter = AnggotaAdapter(this@MainActivity, R.layout.activity_item_anggota, anggotaList)
                    listData.adapter = adapter

                    println("Output : "+anggotaList)
                }
            }

            override fun onCancelled(error: DatabaseError){
                TODO("Not yet implemented")
            }
        })
        listData.setOnItemClickListener{parent, view, position, id ->
            val anggota = anggotaList.get(position)
            val intent = Intent(this@MainActivity, TambahAnggota::class.java)
            intent.putExtra(TambahAnggota.EXTRA_ID, anggota.id)
            intent.putExtra(TambahAnggota.EXTRA_NAMA, anggota.nama)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?){
        simpanData()
    }

    private fun simpanData(){
        val nama = edNama.text.toString().trim()
        val hp = edHP.text.toString()
        val usia = edUsia.text.toString()
        val alamat = edKota.text.toString().trim()

        if(nama.isEmpty() or hp.isEmpty() or usia.isEmpty() or alamat.isEmpty()){
            Toast.makeText(this,"Isi data secara lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        val anggotaId = ref.push().key
        val anggota = Anggota(anggotaId!!, nama, alamat, usia, hp)

        ref.child(anggotaId).setValue(anggota).addOnCompleteListener { Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }
}