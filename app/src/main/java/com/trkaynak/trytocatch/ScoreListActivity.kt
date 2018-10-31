package com.trkaynak.trytocatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_score_list.*
import org.json.JSONException
import org.json.JSONObject

class ScoreListActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var score:String

    val insertUrl = "http://ios-android.trkaynak.com/addActivity.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_list)

        try {
            val intent = intent
            name = intent.getStringExtra("name")
            score = intent.getStringExtra("score")
            nametextViewScore.text = "Name: " + name
            scoretextView.text = "Score: " + score

        }catch (e: Exception){
            println("HATA:"+e)
        }

    }

    fun save(view: View){
        val nameRecived = nametextViewScore.text.toString()
        val scoreRecived = scoretextView.text.toString()
        try {
            val stringRequest = object : StringRequest(Request.Method.POST,insertUrl,Response.Listener<String> {
                response ->
                try {
                    val obj = JSONObject(response)

                }catch (e: JSONException){
                    e.printStackTrace()
                }
            }, object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(applicationContext,"Hata",Toast.LENGTH_LONG).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String,String>()
                    params.put("name",nametextViewScore.text.toString())
                    params.put("score",scoretextView.text.toString())
                    return params
                }
            }
            VolleySingleton.instance?.addToRequestQueu(stringRequest)
            Toast.makeText(applicationContext,"Eklendi",Toast.LENGTH_LONG).show()
        } catch (e: Exception){
            println("HATA OLUŞTU "+e)
        }

    }
}
