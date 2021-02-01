package com.Refaldo.belajarfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DetilAnggotaAdapter(
    val detilContext: Context,
    val layoutResId: Int,
    val detilList: List<DetilAnggota>
) : ArrayAdapter<DetilAnggota>(detilContext, layoutResId, detilList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(detilContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val tvAlamat = view.findViewById<TextView>(R.id.ou_alamat)
        val tvKodePos = view.findViewById<TextView>(R.id.ou_kodePos)

        val detil = detilList[position]
        tvAlamat.text = detil.alamat
        tvKodePos.text = detil.kodePos

        return view
    }
}

