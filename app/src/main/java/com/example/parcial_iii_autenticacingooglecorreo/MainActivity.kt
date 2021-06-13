package com.example.parcial_iii_autenticacingooglecorreo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_home2.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setup()

        sesion()
    }

    override fun onStart() {
        super.onStart()

        Cerrar.visibility = View.VISIBLE
    }

    private fun sesion() {

        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email: String? = prefs.getString("email", null)

        if (email != null){
            Cerrar.visibility = View.INVISIBLE
            showHome(email)
        }

    }

    //Función que agregamos en el onCreate, esta contiene el codigo de ambos botones
    // y dependiendo de qué boton presione el usuario tendrá diferente acción

    private fun setup(){

        title = "Autenticación"

        registrar.setOnClickListener {
            if (correo.text.isNotEmpty() && contraseña.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.text.toString(),
                        contraseña.text.toString()).addOnCompleteListener {
                            if(it.isSuccessful){
                                showHome(it.result?.user?.email ?: "")
                            }else{
                                showAlert()
                            }

                }

            }
        }

        iniciar.setOnClickListener {
            if (correo.text.isNotEmpty() && contraseña.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo.text.toString(),
                        contraseña.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?: "")
                    }else{
                        showAlert()
                    }

                }

            }
        }

        Google.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showHome(email:String){

        val homeIntent: Intent = Intent(this, HomeActivity2::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)



            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(account.email ?: "")
                        }else{
                            showAlert()
                        }

                    }

                }
            } catch (e: ApiException){
                showAlert()
            }

        }

    }

}