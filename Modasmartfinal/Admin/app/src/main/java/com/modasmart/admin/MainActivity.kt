package com.modasmart.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var btnFetchUser: Button
    private lateinit var btnVerDonaciones: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)
        btnFetchUser = findViewById(R.id.btnFetchUser)
        btnVerDonaciones = findViewById(R.id.btnVerDonaciones)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        btnFetchUser.setOnClickListener {
            val intent = Intent(this, PuntosActivity::class.java)
            startActivity(intent)
        }

        btnVerDonaciones.setOnClickListener {
            val intent = Intent(this, ReciclajeActivity::class.java)
            startActivity(intent)
        }

    }
}