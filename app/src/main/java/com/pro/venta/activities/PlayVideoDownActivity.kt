package com.pro.venta.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.pro.venta.R
import com.pro.venta.model.Course
import kotlinx.android.synthetic.main.activity_play_video_down.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.IOException
import java.util.*

class PlayVideoDownActivity : AppCompatActivity(),
    SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener
{

    var holderAux: SurfaceHolder? = null
    var uriPath = ""
    var landPort = 0
    var isRunning = false
    var finishVideo = false
    var contador = 0
    var clickCourse: Course? = null
    //internal var timerVideo = Timer()
    private var handler: Handler = Handler()
    private lateinit var runnable:Runnable

    var timeD = 0
    var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    var posActual = 0
    var name = ""
    //var adapterCapitulos: CourseAdapter? = null
    private var mediaPlayer: MediaPlayer? = null
    var currentPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video_down)

        //videoViewAux.setSurfaceTextureListener(this)
        //holderAux = videoViewAux.holder
        //videoViewAux.keepScreenOn = true
        //holderAux!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        //holderAux!!.addCallback(this)

        menuClose.setOnClickListener {
            if(mediaPlayer != null) {
                //currentPos = mediaPlayer!!.currentPosition
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null

                handler.removeCallbacks(runnable)
                //handler.removeCallbacksAndMessages(runnable)

                //finalStopTimer()
                //Log.w("destroyholder", "surfaceholder $currentPos")
                finish()
            }else{
                finish()
            }
        }
        videoViewAux.setOnClickListener {
            lauotOptionsVideo.visibility = View.VISIBLE
            lauotOptionsVideo.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })

            doAsync {
                Thread.sleep(3000)

                uiThread {
                    if(lauotOptionsVideo != null) {
                        Log.e("no es null","")
                        lauotOptionsVideo.animate()
                            .alpha(0.0f)
                            .setDuration(200)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    super.onAnimationEnd(animation)
                                    lauotOptionsVideo.visibility = View.GONE
                                }
                            })
                    }else{
                        Log.e("es null","")
                    }
                }
            }
        }

        playButton.setOnClickListener {
            //detener el video
            //cambiar icono
            if(isRunning){
                stopTimer()
            }else{
                startTimer()
            }
        }

        progressBarTimeVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.w("change seek changed","$p1")

                /*if(mediaPlayer != null){
                    mediaPlayer!!.seekTo(3000)
                }*/
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.w("change seek start","${p0!!.progress}")
                /*if(mediaPlayer != null){
                    stopTimer()
                    mediaPlayer!!.pause()
                }*/
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //change mediaplayer seekto
                Log.w("change seek stop","${p0!!.progress}")
                if(mediaPlayer != null){

                    val nrTime = (p0.progress * timeD) / 100
                    Log.w("new time","${nrTime}")

                    mediaPlayer!!.seekTo(nrTime)
                    mediaPlayer!!.setOnSeekCompleteListener(object : MediaPlayer.OnSeekCompleteListener{
                        override fun onSeekComplete(p0: MediaPlayer?) {
                            Log.d("APP", "current pos... "+ mediaPlayer!!.getCurrentPosition())
                            //mediaPlayer!!.start();          // <------------------ start video on seek completed
                            mediaPlayer!!.setOnSeekCompleteListener(null);
                            calendar.timeInMillis = mediaPlayer!!.currentPosition.toLong() //contador * 1000L
                            val minute = String.format(
                                "%02d",
                                Integer.parseInt(calendar.get(Calendar.MINUTE).toString() + "")
                            )
                            val second = String.format(
                                "%02d",
                                Integer.parseInt(calendar.get(Calendar.SECOND).toString() + "")
                            )
                            val dateString = minute + ":" + second;
                            runOnUiThread(Runnable {
                                textViewTimeStart.text = dateString
                            })

                            /*if(isRunning) {
                                startTimer()
                                mediaPlayer!!.start()
                            }*/
                        }
                    })
                }
            }
        })

        //launch landsavep
        name = intent.getStringExtra("name")
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation

        //funcio para ocultar la barra de status cuando se gira la pantalla
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("TAG", "Landscape curso fragm!!!")
            val display = getWindowManager().getDefaultDisplay()
            val params = videoViewAux.getLayoutParams()// as LayoutParams
            params.height = display.height
            //lauotHeaderCurso.visibility = View.GONE
            videoViewAux.setLayoutParams(params)
            lauotOptionsVideo.setLayoutParams(params)

            menuFullScreen.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_minimize))

        } else {
            Log.v("TAG", "Portrait curso fragm!!!")

            val scale = resources.displayMetrics.density
            val pixels = ((150 * scale + 0.5f)).toInt() //as Int
            //lauotHeaderCurso.visibility = View.VISIBLE

            val params = videoViewAux.getLayoutParams()// as LayoutParams
            params.height = pixels
            videoViewAux.setLayoutParams(params)
            lauotOptionsVideo.setLayoutParams(params)

            menuFullScreen.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_fullscreen))
        }
    }

//======SurfaceView override's =====================================================================
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

        if(mediaPlayer != null) {
            currentPos = mediaPlayer!!.currentPosition
            mediaPlayer!!.pause()
            stopTimer()
            Log.w("destroyholder", "surfaceholder $currentPos")
        }

        /*if(mediaPlayer != null) {
            currentPos = mediaPlayer!!.currentPosition
            Log.w("destroy","surfaceholder $currentPos")
            mediaPlayer!!.release()
            mediaPlayer = null
        }*/
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {

        Log.w("created before currten","surfaceholder $currentPos")

        holderAux = holder
        holderAux!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if(mediaPlayer != null) {
            mediaPlayer!!.setDisplay(holder)

            if (currentPos != 0){
                Log.w("created","surfaceholder $currentPos")
                mediaPlayer!!.seekTo(currentPos)

            }
        }

        preparedVideo(name)
    }

//======prepared listener =====================================================================
    override fun onPrepared(p0: MediaPlayer?) {

        Log.e("preapred","readyyyy")

        //get video duration
        timeD = mediaPlayer!!.duration
        Log.w("duration","$timeD")

        //set final time video
        calendar.timeInMillis = timeD.toLong() //contador * 1000L
        val minute = String.format(
            "%02d",
            Integer.parseInt(calendar.get(Calendar.MINUTE).toString() + "")
        )
        val second = String.format(
            "%02d",
            Integer.parseInt(calendar.get(Calendar.SECOND).toString() + "")
        )
        val dateString = minute + ":" + second
        textViewTimeEnd.text = dateString

        mediaPlayer!!.seekTo(currentPos)
    }

    override fun onCompletion(mp: MediaPlayer?) {

        timeD = mediaPlayer!!.duration
        Log.w("duration","$timeD")

        //set final time video
        calendar.timeInMillis = timeD.toLong() //contador * 1000L
        val minute = String.format(
            "%02d",
            Integer.parseInt(calendar.get(Calendar.MINUTE).toString() + "")
        )
        val second = String.format(
            "%02d",
            Integer.parseInt(calendar.get(Calendar.SECOND).toString() + "")
        )
        val dateString = minute + ":" + second
        textViewTimeStart.text = dateString

        showLastDarkScreen()
        finalStopTimer()
    }

//======prepared video to launch =====================================================================
    fun preparedVideo(nameVide: String){

        lauotOptionsVideoProgress.visibility = View.VISIBLE

        lauotOptionsVideo.animate()
            .alpha(0.0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    lauotOptionsVideo.visibility = View.GONE
                }
            })

        currentPos = 0
        contador = 0
        finishVideo = false

        uriPath = nameVide
        launchVideo(1)

    }

    fun launchVideo(local: Int){

        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDisplay(holderAux)

        Log.w("launching video","on lcikc lista table $posActual")

        try {
            //uriPath = clickCourse!!.url
            val fileName = uriPath //clickCourse!!.name
            val mFile3: File? = File(filesDir, "videos/$fileName")

            //start timervideo, every second update progress
            mediaPlayer!!.setDataSource(mFile3!!.absolutePath)

            mediaPlayer!!.isLooping = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(1.0f)
            }
            mediaPlayer!!.setScreenOnWhilePlaying(true)

            Log.e("start video", "$currentPos")
            mediaPlayer!!.setOnPreparedListener(this)
            mediaPlayer!!.setOnCompletionListener(this)

            mediaPlayer!!.setOnSeekCompleteListener(object :
                MediaPlayer.OnSeekCompleteListener {
                override fun onSeekComplete(p0: MediaPlayer?) {
                    Log.d("APP", "current pos... " + mediaPlayer!!.getCurrentPosition())
                    //mediaPlayer!!.start();          // <------------------ start video on seek completed
                    lauotOptionsVideoProgress.visibility = View.INVISIBLE
                    mediaPlayer!!.setOnSeekCompleteListener(null);
                    startTimer()
                }
            })

            mediaPlayer!!.prepareAsync()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopTimer(){
        isRunning = false
        playButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_playbpx))
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }

        //timerVideo.cancel()
        handler.removeCallbacks(runnable)
    }

    fun finalStopTimer(){
        isRunning = false
        playButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_reload))
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            //mediaPlayer!!.release()
            //mediaPlayer = null
        }
        finishVideo = true
        //timerVideo.cancel()
        //timerVideo = Timer()
        handler.removeCallbacks(runnable)
    }

    fun startTimer(){
        if(!finishVideo) {
            isRunning = true
            playButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_pausepx
                )
            )

            if (mediaPlayer != null) {
                mediaPlayer!!.start()
            }

            //timerVideo = Timer()
            runnable = Runnable {
                val milis = mediaPlayer!!.currentPosition //* 1000L
                Log.e("milis", "$milis")
                Log.e("timeD", "$timeD")

                //set progress value
                val progress = (milis * 100.0) / timeD
                progressBarTimeVideo.progress = progress.toInt()

                //pasamos timpo, fin del video
                if (milis >= timeD) {
                    //termina video y timer
                    runOnUiThread(Runnable {
                        showLastDarkScreen()
                        finalStopTimer()
                    })
                } else {
                    //avanza video y timer
                    calendar.timeInMillis = milis.toLong() //contador * 1000L
                    val minute = String.format(
                        "%02d",
                        Integer.parseInt(calendar.get(Calendar.MINUTE).toString() + "")
                    )
                    val second = String.format(
                        "%02d",
                        Integer.parseInt(calendar.get(Calendar.SECOND).toString() + "")
                    )
                    val dateString = minute + ":" + second;
                    runOnUiThread(Runnable {
                        textViewTimeStart.text = dateString
                    })
                }

                contador++

                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)
        }
        else {
            //ya termino el video...
            //reiniciarlo
            lauotOptionsVideo.animate()
                .alpha(0.0f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        lauotOptionsVideo.visibility = View.GONE
                    }
                })

            currentPos = 0
            contador = 0
            finishVideo = false

            launchVideo(0)
        }
    }

    fun showLastDarkScreen(){
        lauotOptionsVideo.visibility = View.VISIBLE
        lauotOptionsVideo.animate()
            .alpha(0.7f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        Log.w("onstart pfff","venga")
        holderAux = videoViewAux.holder
        //videoViewAux.keepScreenOn = true
        //holderAux!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holderAux!!.addCallback(this)

    }

    override fun onResume() {
        super.onResume()
        Log.w("onresume","paso x")

    }

    override fun onBackPressed() {

        if(mediaPlayer != null) {
            currentPos = mediaPlayer!!.currentPosition
            mediaPlayer!!.pause()
            stopTimer()
            Log.w("destroyholder", "surfaceholder $currentPos")
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}
