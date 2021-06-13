package com.example.parcial_iii_autenticacingooglecorreo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import com.example.parcial_iii_autenticacingooglecorreo.R.id.btnIr
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home2.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)



        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        setup(email ?: "")

        //Guardamos los datos de inicio

        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

        prefs.putString("email", email)
        prefs.apply()

    }

    private fun setup(email: String){

        title = "Inicio"
        mostrarEmail.text = email

        cerrarSesion.setOnClickListener {

            val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
        onBackPressed()
        }


    }

    fun Ir(view: View) {
        intent = Intent(this@HomeActivity2,Usuarios::class.java)
        startActivity(intent)
    }


}