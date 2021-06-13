package com.example.parcial_iii_autenticacingooglecorreo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.parcial_iii_autenticacingooglecorreo.model.Data
import com.example.parcial_iii_autenticacingooglecorreo.model.Reqres
import kotlinx.android.synthetic.main.activity_usuarios.*

class Usuarios : AppCompatActivity() {
    private val datalist:MutableList<Data> = mutableListOf()
    private lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        myAdapter= MyAdapter(datalist)

        my_recycler_view.layoutManager=LinearLayoutManager(this)
        my_recycler_view.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
       my_recycler_view.adapter=myAdapter

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://reqres.in/api/users?page=2")
            .build()
            .getAsObject(Reqres::class.java, object : ParsedRequestListener<Reqres>{
                override fun onResponse(response: Reqres) {
               datalist.addAll(response.data)
                    myAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    TODO("Not yet implemented")
                }
            })
    }
}