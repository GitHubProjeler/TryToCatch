package com.trkaynak.trytocatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_score_list.*

class ScoreListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        try {
            val intent = intent
            val name: String = intent.getStringExtra("name")
            val score: String = intent.getStringExtra("score")
            nametextViewScore.text = "Name: "+name+" Score: "+score
        }catch (e: Exception){
            println("HATA:"+e)
        }

    }
}
