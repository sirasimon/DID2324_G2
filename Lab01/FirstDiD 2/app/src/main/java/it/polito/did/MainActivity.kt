package it.polito.did

import android.app.Activity
import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()
    private var timer : CountDownTimer ?= null
    private var currentMillis : Long = 0

    //private var notification : Uri ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notification =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r = RingtoneManager.getRingtone(applicationContext, notification)

        //BUTTONS
        val playButton = findViewById<ImageButton>(R.id.play)
        val pauseButton = findViewById<ImageButton>(R.id.pause)
        val addMinButton = findViewById<ImageButton>(R.id.add_minute)
        val stopButton = findViewById<ImageButton>(R.id.stop)
        val resetButton = findViewById<ImageButton>(R.id.reset)

        var lastKeyCode : Int      //ultimo tasto premuto

        val timerText = findViewById<TextView>(R.id.Contatore)      //testo che scende (non editabile)
        val userText = findViewById<EditText>(R.id.editTextTime)    //timer impostato dall'utente
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

                if(!userText.text.isBlank())
                    userText.visibility = View.INVISIBLE

                timerText.visibility = View.VISIBLE
                timerText.text = userText.text
            }
            if (firstTime) firstTime = false
        }


        userText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            lastKeyCode = keyCode

            Log.d("LAST KC", "KeyCode detected $lastKeyCode")   //Due punti sarebbe 59+74


            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val view = this.currentFocus
                if (view != null) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }

                userText.clearFocus()
                background.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        background.setOnClickListener{
            Log.d("BG", "Cliccato BG")

            //NON CANCELLARE
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            userText.clearFocus()

            background.requestFocus()

            //TODO far sparire la tastiera
        }

        playButton.setOnClickListener {
            if(!userText.text.isBlank()){
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

                playButton.visibility = View.INVISIBLE
                pauseButton.visibility = View.VISIBLE
                resetButton.visibility = View.VISIBLE
                addMinButton.visibility = View.VISIBLE
                //progressBar.visibility = View.VISIBLE

                progressBar.max = millis.toInt()

                if(currentMillis==0L){
                    timer = object : CountDownTimer(millis, 1000) {                 //creazione timer

                        override fun onTick(millisUntilFinished: Long) {
                            currentMillis = millisUntilFinished
                            //progressBar.progress = currentMillis.toInt()

                            val totalSeconds = millisUntilFinished / 1000
                            val minutes = totalSeconds / 60
                            val hours = totalSeconds / 3600

                            //timerText.setText("$hours:${minutes%60}:${totalSeconds%60}")    //TODO sistemare formattazione
                            timerText.setText(String.format("%02d:%02d:%02d", hours, minutes%60, totalSeconds%60))
                        }

                        override fun onFinish() {
                            //val notification =
                            //    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            //val r = RingtoneManager.getRingtone(applicationContext, notification)

                            pauseButton.visibility = View.INVISIBLE
                            stopButton.visibility = View.VISIBLE
                            resetButton.visibility = View.INVISIBLE
                            //addMinButton.visibility = View.INVISIBLE

                            r.play()
                        }
                    }.start()
                }else {
                    timer = object :
                        CountDownTimer(currentMillis, 1000) {                 //creazione timer

                        override fun onTick(millisUntilFinished: Long) {
                            currentMillis = millisUntilFinished
                            //progressBar.progress = currentMillis.toInt()

                            val totalSeconds = millisUntilFinished / 1000
                            val minutes = totalSeconds / 60
                            val hours = totalSeconds / 3600
                            //timerText.setText("$hours:${minutes%60}:${totalSeconds%60}")    //TODO sistemare formattazione
                            timerText.setText(
                                String.format(
                                    "%02d:%02d:%02d",
                                    hours,
                                    minutes % 60,
                                    totalSeconds % 60
                                )
                            )
                        }

                        override fun onFinish() {
                            //val notification =
                            //    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            //val r = RingtoneManager.getRingtone(applicationContext, notification)
                            pauseButton.visibility = View.INVISIBLE
                            stopButton.visibility = View.VISIBLE
                            resetButton.visibility = View.INVISIBLE
                            //addMinButton.visibility = View.INVISIBLE

                            r.play()
                        }
                    }.start()
                }
            }
        }

        pauseButton.setOnClickListener{
            timer?.cancel()

            pauseButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }

        stopButton.setOnClickListener{
            r.stop()

            stopButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
            addMinButton.visibility = View.INVISIBLE

            currentMillis = 0;

            userText.text = null
            userText.hint = "00:00:00"

            timerText.visibility = View.INVISIBLE
            userText.visibility = View.VISIBLE
        }

        resetButton.setOnClickListener {
            timer?.cancel()

            stopButton.visibility = View.INVISIBLE
            pauseButton.visibility = View.INVISIBLE
            resetButton.visibility = View.INVISIBLE
            addMinButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE

            currentMillis = 0;

            userText.text = null
            userText.hint = "00:00:00"

            timerText.visibility = View.INVISIBLE
            userText.visibility = View.VISIBLE
        }

        addMinButton.setOnClickListener {
            timer?.cancel()


            currentMillis += 60000

            timer = object : CountDownTimer(currentMillis, 1000) {                 //creazione timer

                override fun onTick(millisUntilFinished: Long) {
                    currentMillis = millisUntilFinished
                    //progressBar.progress = currentMillis.toInt()

                    val totalSeconds = millisUntilFinished / 1000
                    val minutes = totalSeconds / 60
                    val hours = totalSeconds / 3600
                    //timerText.setText("$hours:${minutes%60}:${totalSeconds%60}")    //TODO sistemare formattazione
                    timerText.setText(String.format("%02d:%02d:%02d", hours, minutes%60, totalSeconds%60))
                }

                override fun onFinish() {
                    //val notification =
                    //    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    //val r = RingtoneManager.getRingtone(applicationContext, notification)
                    pauseButton.visibility = View.INVISIBLE
                    stopButton.visibility = View.VISIBLE
                    resetButton.visibility = View.INVISIBLE
                    //addMinButton.visibility = View.INVISIBLE

                    r.play()
                }
            }.start()

            r.stop();
            stopButton.visibility = View.INVISIBLE
            resetButton.visibility = View.VISIBLE
            pauseButton.visibility = View.VISIBLE
        }
    }
}

/*
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

 */