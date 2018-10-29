package com.trkaynak.trytocatch

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun game(view: View){
        try {
            if (nameText.text.isEmpty()){
                Toast.makeText(applicationContext,"Name is cannot be empty...",Toast.LENGTH_LONG).show()
            }else {
                val gameIntent = Intent(applicationContext, GameActivity::class.java)
                gameIntent.putExtra("name", nameText.text.toString())
                startActivity(gameIntent)
            }
        } catch (e:Exception){
            println("Game button HATA:"+e)
        }

    }

    fun list(view: View){
            val listIntent = Intent(applicationContext,ScoreListActivity::class.java)
            startActivity(listIntent)

    }
}
