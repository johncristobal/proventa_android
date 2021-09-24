package com.pro.venta.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment

import com.pro.venta.R
import kotlinx.android.synthetic.main.fragment_curso.*
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pro.venta.adapter.CourseAdapter
import com.pro.venta.model.Course
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.view.View
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.ETC1.getHeight
import androidx.core.view.ViewCompat.animate
import android.R.attr.translationY
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.downloader.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.gson.Gson
import com.pro.venta.adapter.DetallesFragmentAdapter
import com.pro.venta.model.User
import com.pro.venta.model.Videos
import com.pro.venta.ws.singleton.settings
import kotlinx.android.synthetic.main.course_item.view.*
import java.io.File
import kotlin.Error
import kotlin.concurrent.timerTask


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/*
* Bajar video
* seekto
* borrar video su no desarga
* */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CursoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CursoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CursoFragment : Fragment(),
    SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener
    //Player.EventListener,
    //TextureView.SurfaceTextureListener
{

    lateinit var fragment1: ListCapitulosFragment
    lateinit var fragment2: DetallesFragment

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var clickCourse: Course? = null
    var surfaceAux: Surface? = null
    var holderAux: SurfaceHolder? = null
    var uriPath = ""
    var landPort = 0
    var isRunning = false
    var finishVideo = false
    var contador = 0
    //internal var timerVideo = Timer()
    private var handler: Handler = Handler()
    private lateinit var runnable:Runnable

    var timeD = 0
    var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    var posActual = 0

    //var adapterCapitulos: CourseAdapter? = null
    private var listener: OnFragmentCursoListener? = null
    private var mediaPlayer: MediaPlayer? = null
    var currentPos = 0

    var settingsShared : settings? = null
    var user : User? = null

    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }
    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    private lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curso, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = DetallesFragmentAdapter(activity!!, childFragmentManager,fragment1,fragment2)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager_ccc)
        viewPager.adapter = sectionsPagerAdapter
        tabsCurso.setupWithViewPager(viewPager)

        muestraData()

        //videoViewAux.setSurfaceTextureListener(this)
        holderAux = videoViewAux.holder
        //videoViewAux.keepScreenOn = true
        //holderAux!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holderAux!!.addCallback(this)

        videoViewAux.setOnClickListener {

            if(mediaPlayer != null) {
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
                        if (lauotOptionsVideo != null) {
                            Log.e("no es null", "")
                            lauotOptionsVideo.animate()
                                .alpha(0.0f)
                                .setDuration(200)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator?) {
                                        super.onAnimationEnd(animation)
                                        lauotOptionsVideo.visibility = View.GONE
                                    }
                                })
                        } else {
                            Log.e("es null", "")
                        }
                    }
                }
            }
        }

        menuButton.setOnClickListener {
            //mostrar fragment de opciones de video
            listener?.onCursoListener("opciones")
        }

        menuFullScreen.setOnClickListener {
            //change to fullscreenn
            if (landPort == 0){
                landPort = 1
                activity!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            else{
                landPort = 0
                activity!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                            activity!!.runOnUiThread(Runnable {
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
    }

    fun muestraData(){
        val prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)

        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")){

            cursoTitle.text = "Invertir en este curso"
            textViewProgress.visibility = View.GONE
            val scale = context!!.resources.displayMetrics.density
            val pixels = ((50 * scale + 0.5f)).toInt() //as Int
            lauotHeaderCurso.visibility = View.VISIBLE

            val params = constraintLayout2.getLayoutParams()// as LayoutParams
            params.height = pixels
            constraintLayout2.setLayoutParams(params)

            ReturnButton.visibility = View.GONE

        }else{
            cursoTitle.text = "Continuar mi curso"
            textViewProgress.visibility = View.VISIBLE

            val scale = context!!.resources.displayMetrics.density
            val pixels = ((80 * scale + 0.5f)).toInt() //as Int
            lauotHeaderCurso.visibility = View.VISIBLE

            val params = constraintLayout2.getLayoutParams()// as LayoutParams
            params.height = pixels
            constraintLayout2.setLayoutParams(params)

            ReturnButton.visibility = View.VISIBLE

            val datos= prefs.getString("user","")

            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            user = gson.fromJson(datos, User::class.java)

            //Se le asignan a las variables lmilisos valores cada uno de los atributos del objeto que estamos deserializando
            val imgPath = user!!.image_path

            if (imgPath != ""){
                /*usar la libreria glide para cargar foto*/
                Glide.with(activity!!)
                    .load(imgPath)
                    .into(ReturnButton)
            }

            progressBarCurso.progress = user!!.progress
            textViewProgress.text = "Tu progreso - ${user!!.progress}%"
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation

        //funcio para ocultar la barra de status cuando se gira la pantalla
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("TAG", "Landscape curso fragm!!!")
            val display = activity!!.getWindowManager().getDefaultDisplay()
            val params = videoViewAux.getLayoutParams()// as LayoutParams
            params.height = display.height
            lauotHeaderCurso.visibility = View.GONE
            videoViewAux.setLayoutParams(params)
            lauotOptionsVideo.setLayoutParams(params)

            menuFullScreen.setImageDrawable(ContextCompat.getDrawable(activity!!,R.drawable.ic_minimize))

        } else {
            Log.v("TAG", "Portrait curso fragm!!!")

            val scale = context!!.resources.displayMetrics.density
            val pixels = ((150 * scale + 0.5f)).toInt() //as Int
            lauotHeaderCurso.visibility = View.VISIBLE

            val params = videoViewAux.getLayoutParams()// as LayoutParams
            params.height = pixels
            videoViewAux.setLayoutParams(params)
            lauotOptionsVideo.setLayoutParams(params)

            menuFullScreen.setImageDrawable(ContextCompat.getDrawable(activity!!,R.drawable.ic_fullscreen))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentCursoListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentCursoListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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

        /*
        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("null")) {
            var videos = ArrayList<Videos>()
            videos.add(Videos("", "https://api.proventas.pro/media1/teaser/0.mp4"))
            videos.add(Videos("", "https://api.proventas.pro/media1/teaser/0.mp4"))

            var capitulos = ArrayList<Course>()
            capitulos.add(
                Course(
                    "0",
                    "name0.mp4",
                    "https://websiteqas2019.miituo.com/img_cupones/muertos150/capitulo1.mp4",
                    0,
                    "",
                    "¡Por fin! Llegó un curso que habla en tu idioma, sin rodeos. Aprende sólo lo que deseas saber para lograr que tus ventas sean exitosas.",
                    "La mejor inversión para ser exitoso en ventas",
                    "",
                    videos
                )
            )

            preparedVideo(capitulos[0],0)
        }*/
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

    fun downfile(url:String, fileName: String){


        //if (player != null) {
        //var prefs= activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        //val mFile2: File? = File(Environment.getExternalStorageDirectory(), "pictures")
        val mFile2: File? = File(activity!!.filesDir, "videos")
        var lastuRL = ""
        if (user!!.account) {
            lastuRL = url+"?token=${user!!.access_token}"
        }else{
            lastuRL = url
        }
        //val fileName = "videoTemp.mp4"

        val downloadId = PRDownloader.download(lastuRL,mFile2!!.absolutePath,fileName)
            .build()
            .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                override fun onStartOrResume() {
                    System.out.println("??????????????????? start")
                }
            })
            .setOnPauseListener(object : OnPauseListener {
                override fun onPause() {
                }
            })
            .setOnCancelListener(object : OnCancelListener {
                override fun onCancel() {
                }
            })
            .setOnProgressListener(object : OnProgressListener {
                override fun onProgress(progress: Progress) {
                    //circlePeView.visibility = View.VISIBLE

                    val per = (progress.currentBytes.toFloat() / progress.totalBytes.toFloat()) * 100.00
                    //var perint = per*100
                    System.out.println("::??????????????????? Per : " + per + " ?? : " + progress.currentBytes + " ?? : " + progress.totalBytes)

                    //circlePeView.setProgress(per.toInt())
                }
            })
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    System.out.println("??????????????????? complete ")
                    Toast.makeText(activity!!,"Video descargado",Toast.LENGTH_SHORT).show()

                    listener?.onCursoListener("listodescarga")

                    //save database local with new video
                    //adapter change icon to trash

                }

                override fun onError(error: com.downloader.Error?) {
                    System.out.println("??????????????????? error " + error)
                }
            })
    }

    fun downloadVideo(course: Course, pos: Int){
        downfile(course.videos[0].url, course.name)
    }

    fun downloadFromOpt(){
        //Toast.makeText(activity!!,"Descargando video...",Toast.LENGTH_SHORT).show()
        downfile(clickCourse!!.videos[0].url, clickCourse!!.name)
    }

    fun preparedVideo(course: Course, pos: Int){

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

        var flagWifi = false
        var flagDatos = false

        clickCourse = course
        posActual = pos

        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                flagWifi = true
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                flagDatos = true
            }
        } else {
            // not connected to the internet
            flagWifi = false
            flagDatos = false
        }

        if (flagDatos || flagWifi){
            //val course = capitulos.get(pos)
            Log.e("nombre",course.videos[0].url)
            launchVideo(0)

        }else{
            //no hay datos...si esta descargado, lanzalo
            if(course.download == 1){
                //de aqui recuperamos nombre
                launchVideo(1)
            }else{
                Toast.makeText(activity!!,"Sin conexión a Internet, intente más tarde...",Toast.LENGTH_SHORT).show()
            }
        }
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

        if (local == 0) {
            try {
                val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
                val sesion = prefs.getString("sesion","null")
                if (sesion!!.equals("null")) {
                    uriPath = clickCourse!!.videos[0].url
                }else{
                    if (user!!.account) {
                        uriPath = clickCourse!!.videos[0].url + "?token=${user!!.access_token}"
                    } else {
                        uriPath = clickCourse!!.videos[0].url
                    }
                }

                val headers = HashMap<String, String>()
                headers.put("rtsp_transport", "tcp")
                headers.put("Accept-Ranges", "bytes")
                headers.put("Status", "206")
                headers.put("Cache-control", "no-cache")

                //start timervideo, every second update progress
                mediaPlayer!!.setDataSource(
                    activity!!
                    ,Uri.parse(uriPath)
                    ,headers
                )

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
        }else{
            //no hay internet, cargamos video descargado
            try {
                //uriPath = clickCourse!!.videos[0].url
                val fileName = clickCourse!!.name
                val mFile3: File? = File(activity!!.filesDir, "videos/$fileName")

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
                        mediaPlayer!!.setOnSeekCompleteListener(null);
                        startTimer()
                    }
                })

                mediaPlayer!!.prepareAsync()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopTimer(){
        isRunning = false
        playButton.setImageDrawable(ContextCompat.getDrawable(activity!!,R.drawable.ic_playbpx))
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }

        //timerVideo.cancel()
        //handler.removeCallbacks(runnable)
        handler.removeCallbacksAndMessages(null)
    }

    fun finalStopTimer(){
        isRunning = false
        playButton.setImageDrawable(ContextCompat.getDrawable(activity!!,R.drawable.ic_reload))
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            //mediaPlayer!!.release()
            //mediaPlayer = null
        }
        finishVideo = true
        //timerVideo.cancel()
        //timerVideo = Timer()
        //handler.removeCallbacks(runnable)
        handler.removeCallbacksAndMessages(null)

        //logica para siguiente video
        //checamos si es ultimo video...
        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("1")) {
            if (settingsShared!!.autoPlayVideo) {
                listener?.onCursoListener("next")
            } else {
                Log.w("notihng", "automatico falso")
            }
        }
    }

    fun startTimer(){
        if(!finishVideo) {
            isRunning = true
            playButton.setImageDrawable(
                ContextCompat.getDrawable(
                    activity!!,
                    R.drawable.ic_pausepx
                )
            )

            if (mediaPlayer != null) {
                mediaPlayer!!.start()
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
                        activity!!.runOnUiThread(Runnable {
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
                        activity!!.runOnUiThread(Runnable {
                            textViewTimeStart.text = dateString
                        })
                    }

                    contador++

                    handler.postDelayed(runnable, 1000)
                }
                handler.postDelayed(runnable, 1000)
            }

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

    override fun onResume() {
        super.onResume()
        Log.e("resume","curso resume")

        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), android.content.Context.MODE_PRIVATE)
        val datos = prefs.getString("settings","")

        if(datos != "") {
            //Creamos una variable gson para volverla a formato objeto.
            val gson = Gson()
            settingsShared = gson.fromJson(datos, settings::class.java) //aqui la cadena se convierte en un objeto.
        }else{
            settingsShared = settings()
        }
        Log.w("settings","${settingsShared!!.autoPlayVideo}")
        Log.w("settings 2","${settingsShared!!.wifiDownload}")
    }

    override fun onStart() {
        super.onStart()

        Log.w("onstart pfff","venga")
        holderAux = videoViewAux.holder
        //videoViewAux.keepScreenOn = true
        holderAux!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holderAux!!.addCallback(this)
    }

    fun changeSpeed(vel: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer!!.playbackParams = mediaPlayer!!.playbackParams.setSpeed(vel)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentCursoListener {
        // TODO: Update argument type and name
        fun onCursoListener(message: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CursoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CursoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
