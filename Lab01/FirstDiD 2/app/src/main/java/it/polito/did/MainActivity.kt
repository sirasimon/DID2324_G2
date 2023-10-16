package it.polito.did

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private var timer : CountDownTimer ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playButton = findViewById<ImageButton>(R.id.play)
        val timerText = findViewById<TextView>(R.id.Contatore)
        val userText = findViewById<EditText>(R.id.editTextTime)
        val background = findViewById<View>(R.id.conta)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        progressBar.visibility = View.INVISIBLE
        timerText.visibility = View.GONE
        var firstTime = true
        timerText.setOnClickListener {
            timerText.visibility = View.INVISIBLE
            userText.visibility = View.VISIBLE
        }

        userText.setOnFocusChangeListener{
            _,b -> if (!b && ! firstTime) {
                userText.visibility = View.INVISIBLE
                timerText.visibility = View.VISIBLE
                timerText.text = userText.text
            }
            if (firstTime) firstTime = false
        }

        background.setOnClickListener{
            userText.clearFocus()

        }
        playButton.setOnClickListener {
            val userInput = userText.text
            val strs = userInput.split(":").toTypedArray()
            var millis : Long = 0
            if (strs.count() == 1) {
                millis = strs[0].toLong() * 1000
            }
            else if (strs.count() == 2) {
                millis = strs[1].toLong() * 1000 + strs[0].toLong() * 60 * 1000
            }
            else if (strs.count() == 3) {
                millis = strs[2].toLong() * 1000 + strs[1].toLong() * 60 * 1000 + strs[2].toLong() * 3600 * 1000
            }
            object : CountDownTimer(millis, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val totalSeconds = millisUntilFinished / 1000
                    val minutes = totalSeconds / 60
                    val hours = totalSeconds / 3600
                    timerText.setText("$hours:$minutes:$totalSeconds")
                }

                override fun onFinish() {
                    //Suona timer
                }
            }.start()

        }


    }
}