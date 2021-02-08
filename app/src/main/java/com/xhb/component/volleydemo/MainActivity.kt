package com.xhb.component.volleydemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    var stringRequest: StringRequest? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = this.findViewById<TextView>(R.id.test_volley)
        textView.setOnClickListener {
            val url = "http://www.baidu.com"
            // Request a string response from the provided URL.
            stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    textView.text = "Response is: ${response.substring(0, 500)}"
                },
                { listener -> textView.text = listener.networkResponse.toString() })

            // Add the request to the RequestQueue.
            Singleton.getInstance(this).addRequest(stringRequest)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Singleton.getInstance(this).queue.cancelAll("1")
    }
}