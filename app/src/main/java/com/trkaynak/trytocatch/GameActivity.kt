package com.trkaynak.trytocatch

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var nameRecived:String
    private lateinit var scoreSound: MediaPlayer
    private lateinit var addSound: MediaPlayer
    private lateinit var subSound: MediaPlayer
    var score = 0
    var time1 = 2000
    var time2 = 4000
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

        try {
            val intent = intent

            nameRecived = intent.getStringExtra("name")

            userName.text = "Name: "+ nameRecived

            score = 0
            imageArray = arrayListOf(imageView,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9,imageView10,imageView11,imageView12,imageView13,imageView14,imageView15,imageView16,imageView17,imageView18,imageView19,imageView20,imageView21,imageView22,imageView23,imageView24,imageView25,imageView26)
            hideImage()
            btnStart.visibility = View.INVISIBLE

            countdownPeriod = 15000
            createCountDownTimer()
            scoreSound = MediaPlayer.create(this,R.raw.score)
            addSound = MediaPlayer.create(this,R.raw.add)
            subSound = MediaPlayer.create(this, R.raw.out)
        } catch (e: Exception){
            println("Game Activity HATA: "+e)
            Log.e("HATA","onCreate Hatası")
        }

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
        try {
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

                }

                override fun onTick(millisUntilFinished: Long) {
                    timeText.text = "Timer: "+millisUntilFinished/1000
                    countdownPeriod = millisUntilFinished
                }

            }.start()
        } catch (e: Exception){
            println("HATA CDTimer"+e)
        }
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

                    //Show Time image
                     var x = random.nextInt(time2 - time1)
                     handler.postDelayed(runnable, x.toLong())
                }
            }
            handler.post(runnable)
    }

    fun increaseScore(view: View){
        score++
        scoreText.text = "Score : " + score.toString()
        scoreSound.start()
      //  level()
    }

    fun level(){
       if(score <= 3){
           time1 = 3000
           time2 = 3500
           leveltextView.text = "Level: 1"
           hideImage()
       } else if (score <= 7){
          time1 = 2000
           time2 = 2500
           leveltextView.text = "Level: 2"
           hideImage()
       } else if (score <= 15){
           time1 = 1000
           time2 = 1500
           leveltextView.text = "Level: 3"
           hideImage()
       }else if (score <= 20){
           time1 = 800
           time2 = 1000
           leveltextView.text = "Level: 4"
           hideImage()
       } else if (score > 20){
           time1 = 400
           time2 = 800
           leveltextView.text = "Level: 5"
           hideImage()
       } else {
           time1 = 5000
           time2 = 6000
           leveltextView.text = "Level: #Hata"
           hideImage()
       }
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
  //  val requestQueue = Volley.newRequestQueue(this)
    fun list(view: View){
        val listIntent = Intent(applicationContext,ScoreListActivity::class.java)
        listIntent.putExtra("name",nameRecived)
        listIntent.putExtra("score",score.toString())
        save()
        startActivity(listIntent)
    }
    fun save(){
        /*
        val stringRequest = object : StringRequest(Request.Method.POST,insertUrl, Response.Listener<String> {
         response ->
            try {
                val obj = JSONObject(response)
                Toast.makeText(applicationContext,obj.getString("Message"),Toast.LENGTH_LONG).show()
            } catch (e: JSONException){
                e.printStackTrace()
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_LONG).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getParams():Map<String,String>{
                val params = HashMap<String,String>()
                params.put("name",nameRecived)
                params.put("score",score.toString())
                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueu(stringRequest)
        */
    }
}
