package com.example.ahorros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegistrarCompra = findViewById<Button>(R.id.btnRegistrarCompra)
        val btnRegistrarTransaccion = findViewById<Button>(R.id.btnRegistrarTransaccion)
        val btnResumen = findViewById<Button>(R.id.btnResumen)

        btnRegistrarCompra.setOnClickListener {
            startActivity(Intent(this, RegistrarCompraActivity::class.java))
        }

        btnRegistrarTransaccion.setOnClickListener {
            startActivity(Intent(this, RegistrarTransaccionActivity::class.java))
        }

        btnResumen.setOnClickListener {
            startActivity(Intent(this, ResumenActivity::class.java))
        }
    }
}
