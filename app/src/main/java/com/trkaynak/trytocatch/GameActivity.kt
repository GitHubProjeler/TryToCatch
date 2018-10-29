package com.trkaynak.trytocatch

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

class GameActivity : AppCompatActivity() {

    var nameRecived:String=""
    private lateinit var scoreSound: MediaPlayer
    private lateinit var addSound: MediaPlayer
    private lateinit var subSound: MediaPlayer
    var score = 0
    var imageArray = ArrayList<ImageView>()
    var timeAdd = ArrayList<ImageView>()
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}
    var mCountDownTimer: CountDownTimer? = null
    var countdownPeriod: Long = 0
    var insertUrl="https://ios-android.trkaynak.com/tryinsert.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val intent = intent

        nameRecived = intent.getStringExtra("name")
        userName.text = "Name: "+ nameRecived

        score = 0
        imageArray = arrayListOf(imageView,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9,imageView10,imageView11,imageView12,imageView13,imageView14,imageView15,imageView16,imageView17,imageView18,imageView19,imageView20,imageView21,imageView22,imageView23,imageView24,imageView25,imageView26)
        hideImage()
       btnStart.visibility = View.INVISIBLE

        countdownPeriod = 5000
        createCountDownTimer()
        scoreSound = MediaPlayer.create(this,R.raw.score)
        addSound = MediaPlayer.create(this,R.raw.add)
        subSound = MediaPlayer.create(this, R.raw.out)
    }

        fun onTouch():Boolean{
            if(mCountDownTimer != null)
                mCountDownTimer!!.cancel()
            createCountDownTimer()
            return true
        }
            fun onTouch2(): Boolean {
                if (mCountDownTimer != null)
                    mCountDownTimer!!.cancel()
                createCountDownTimer2()
                return true
            }

    private fun createCountDownTimer(){
        mCountDownTimer = object : CountDownTimer(countdownPeriod + 5000,1000){

            override fun onFinish() {
                Toast.makeText(applicationContext,"Score: $score", Toast.LENGTH_LONG).show()
                timeText.text = "Timer: 0"
                handler.removeCallbacks(runnable)
                //puan alınacak resimler gizleniyor
                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                //ek zaman alınacak resimler gizleniyor
                for (add in timeAdd){
                    add.visibility = View.INVISIBLE
                }

                val intent = intent
                intent.putExtra("score",score)
                btnStart.visibility = View.VISIBLE

                //add mysql

                save()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeText.text = "Timer: "+millisUntilFinished/1000
                countdownPeriod = millisUntilFinished
            }

        }.start()
    }

    private fun createCountDownTimer2() {
        mCountDownTimer = object : CountDownTimer(countdownPeriod - 5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeText.text = "Timer : " + millisUntilFinished / 1000
                countdownPeriod = millisUntilFinished
            }

            override fun onFinish() {
                Toast.makeText(applicationContext,"Score : $score",Toast.LENGTH_LONG).show()
                timeText.text = "Timer : 0"
                handler.removeCallbacks(runnable)
                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                for (add in timeAdd){
                    add.visibility = View.INVISIBLE
                }

                btnStart.visibility = View.VISIBLE
            }
        }.start()
    }

    fun hideImage() {
        runnable = object : Runnable{
            override fun run() {
                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                for (add in timeAdd){
                    add.visibility = View.INVISIBLE
                }

                val random = Random()
                val index = random.nextInt(27 - 0)

                imageArray[index].visibility = View.VISIBLE

                //Show Time
                val x = random.nextInt(1500 - 200)
                handler.postDelayed(runnable, x.toLong())
            }
        }
        handler.post(runnable)
    }

    fun increaseScore(view: View){
        score++
        scoreText.text = "Score : " + score.toString()
        scoreSound.start()
    }

    fun addTime(view: View){
        onTouch()
        countdownPeriod = countdownPeriod + 5000
        timeText.text = "Timer : " + countdownPeriod.toString()
        addSound.start()
    }
    fun outTime(view: View){
        onTouch2()
        countdownPeriod = countdownPeriod - 5000
        if (countdownPeriod <= 0){
            timeText.text = "Timer : 0"
        } else
            timeText.text = "Timer : " + countdownPeriod.toString()
        subSound.start()
    }
    fun start(view: View){
        score = 0
        scoreText.text = "Score : 0"
        hideImage()
        countdownPeriod = 10000;
        createCountDownTimer()
        btnStart.visibility = View.INVISIBLE
    }
    fun list(view: View){
        val listIntent = Intent(applicationContext,ScoreListActivity::class.java)
        listIntent.putExtra("name",nameRecived)
        listIntent.putExtra("score",score.toString())
        startActivity(listIntent)
    }

    val queue = Volley.newRequestQueue(this)
    fun save(){
        val request = StringRequest(Request.Method.POST,insertUrl, Response.Listener<String> {response ->

            @Throws(AuthFailureError::class)
            fun getParams(): Map<String, String> {
                val parameters = HashMap<String, String>()
                parameters["name"] = nameRecived.toString()
                parameters["score"] = score.toString()
                return parameters
            }

        }, Response.ErrorListener {
            error( println("HATA"))
        })

        queue.add(request)
    }
}
