package com.trkaynak.trytocatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_score_list.*

class ScoreListActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var score:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        try {
            val intent = intent
            name = intent.getStringExtra("name")
            score = intent.getStringExtra("score")
            nametextViewScore.text = "Name: "+name+" Score: "+score
        }catch (e: Exception){
            println("HATA:"+e)
        }

    }
}
