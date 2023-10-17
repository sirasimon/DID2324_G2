package it.polito.did

import android.media.RingtoneManager
import android.os.Bundle
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


class CountdownActivity : AppCompatActivity() {

    private val vm by viewModels<CountdownViewModel>()
    private var currentMillis : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notification =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val alarmSound = RingtoneManager.getRingtone(applicationContext, notification)

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
                    vm.createTimer(millis)
                }else {
                    vm.createTimer(currentMillis)
                }
            }
        }

        pauseButton.setOnClickListener{
            vm.cancelTimer()

            pauseButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }

        stopButton.setOnClickListener{
            alarmSound.stop()

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
            vm.cancelTimer();

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
            vm.cancelTimer()

            currentMillis += 60000

            vm.createTimer(currentMillis)

            alarmSound.stop();

            stopButton.visibility = View.INVISIBLE
            resetButton.visibility = View.VISIBLE
            pauseButton.visibility = View.VISIBLE
        }

        // OSSERVA I VALORI E FAI QUALCOSA
        vm.timerValue.observe(this){it ->
            currentMillis = vm.getTimerValue()
            timerText.text = vm.getTimerString()
        }

        vm.isTimeout.observe(this){it ->
            if(it && !vm.isTimerNull()){
                alarmSound.play()
                pauseButton.visibility = View.INVISIBLE
                stopButton.visibility = View.VISIBLE
                resetButton.visibility = View.INVISIBLE
            }
            else if(!it && !vm.isTimerNull()){
                alarmSound.stop();
                stopButton.visibility = View.INVISIBLE
                resetButton.visibility = View.VISIBLE
                pauseButton.visibility = View.VISIBLE
            }
            else if(!it && vm.isTimerNull()){
                alarmSound.stop();
                stopButton.visibility = View.INVISIBLE
                resetButton.visibility = View.INVISIBLE
                pauseButton.visibility = View.INVISIBLE
            }
        }
    }
}