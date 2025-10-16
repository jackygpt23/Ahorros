package com.example.ahorros

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ahorros.data.AppDatabase
import com.example.ahorros.data.Transaccion
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistrarTransaccionActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etMonto: EditText
    private lateinit var rgTipo: RadioGroup
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_transaccion)

        etNombre = findViewById(R.id.etNombre)
        etMonto = findViewById(R.id.etMonto)
        rgTipo = findViewById(R.id.rgTipo)
        btnGuardar = findViewById(R.id.btnGuardarTransaccion)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val monto = etMonto.text.toString().toDoubleOrNull() ?: 0.0

            if(nombre.isEmpty() || monto <= 0) {
                Toast.makeText(this, "Ingrese nombre y monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipo = when (rgTipo.checkedRadioButtonId) {
                R.id.rbIngreso -> "ingreso"
                R.id.rbEgreso -> "egreso"
                else -> ""
            }

            if(tipo.isEmpty()) {
                Toast.makeText(this, "Seleccione tipo de transacción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val transaccion = Transaccion(fecha = fecha, nombre = nombre, tipo = tipo, monto = monto)

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                db.transaccionDao().insertarTransaccion(transaccion)
                Toast.makeText(this@RegistrarTransaccionActivity, "Transacción guardada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
