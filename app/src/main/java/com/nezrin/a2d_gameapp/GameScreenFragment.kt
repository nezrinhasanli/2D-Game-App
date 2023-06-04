package com.nezrin.a2d_gameapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.nezrin.a2d_gameapp.databinding.FragmentGameScreenBinding
import kotlin.concurrent.schedule
import java.util.*
import kotlin.math.floor

class GameScreenFragment : Fragment() {
    private lateinit var binding: FragmentGameScreenBinding
    //Positions
    private var anakarakterX = 0.0f
    private var anakarakterY = 0.0f
    private var siyahkareX = 0.0f
    private var siyahkareY = 0.0f
    private var saridaireX = 0.0f
    private var saridaireY = 0.0f
    private var kirmiziucgenX = 0.0f
    private var kirmiziucgenY = 0.0f

    //Sizes
    private var ekranGenisligi = 0
    private var ekranYukseligi = 0
    private var anakarakterGenisligi = 0
    private var anakarakterYuksekligi = 0

    //Controller
    private var dokunmaKontrol = false
    private var baslangicKontrol = false

    private val timer= Timer()

    private var score=0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentGameScreenBinding.inflate(inflater,container,false)

        //cisimler ekranda gorunmeyecek ekranin xaricinde olacaq
        binding.siyahkare.x=-800.0f
        binding.siyahkare.y=-800.0f
        binding.saridaire.x=-800.0f
        binding.saridaire.y=-800.0f
        binding.kirmiziucgen.x=-800.0f
        binding.kirmiziucgen.y=-800.0f


        binding.cl.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if(baslangicKontrol) {
                    if (event?.action == MotionEvent.ACTION_DOWN) {
                        Log.e("MotionEvent", "ACTION_DOWN: Touched the screen")
                        dokunmaKontrol = true
                    }
                    if (event?.action == MotionEvent.ACTION_UP) {
                        Log.e("MotionEvent", "ACTION_UP: Touched the screen")
                        dokunmaKontrol = false
                    }
                    } else {
                    baslangicKontrol = true

                    binding.textViewStartGame.visibility=View.INVISIBLE

                    anakarakterX = binding.anakarakter.x
                    anakarakterY = binding.anakarakter.y
                    anakarakterGenisligi = binding.anakarakter.width
                    anakarakterYuksekligi = binding.anakarakter.height
                    ekranGenisligi = binding.cl.width
                    ekranYukseligi = binding.cl.height

                    timer.schedule(0, 20) {
                        Handler(Looper.getMainLooper()).post {
                            anakarakterMoving()
                            cisimlerMoving()
                            carpismaControl()
                        }
                    }
                }
                return true
            }
        })
        return binding.root
    }

    fun anakarakterMoving(){

        val anakarakterSpeed=ekranYukseligi/60.0f

        if (dokunmaKontrol) {
            anakarakterY -= anakarakterSpeed
        } else {

            anakarakterY += anakarakterSpeed
        }
        if (anakarakterY <= 0.0f) {

            anakarakterY = 0.0f
        }
        if (anakarakterY>=ekranYukseligi-anakarakterYuksekligi){
            anakarakterY=(ekranYukseligi-anakarakterYuksekligi).toFloat()

        }
        binding.anakarakter.y = anakarakterY

    }

    fun cisimlerMoving(){
        //cisimlerin hizi
        siyahkareX-=ekranGenisligi/44.0f
        saridaireX-=ekranGenisligi/54.0f
        kirmiziucgenX-=ekranGenisligi/36.0f

        if (siyahkareX<0.0f){
            siyahkareX= ekranGenisligi+20.0f
            siyahkareY= floor(Math.random()* ekranYukseligi).toFloat()
        }
        binding.siyahkare.x=siyahkareX
        binding.siyahkare.y= siyahkareY

        if (saridaireX<0.0f){
            saridaireX= ekranGenisligi+20.0f
            saridaireY= floor(Math.random()* ekranYukseligi).toFloat()
        }
        binding.saridaire.x=saridaireX
        binding.saridaire.y= saridaireY

        if (kirmiziucgenX<0.0f){
            kirmiziucgenX= ekranGenisligi+20.0f
            kirmiziucgenY= floor(Math.random()* ekranYukseligi).toFloat()
        }
        binding.kirmiziucgen.x=kirmiziucgenX
        binding.kirmiziucgen.y= kirmiziucgenY
    }

    fun carpismaControl(){

        val saridaireMerkezX = saridaireX + binding.saridaire.width/2.0f
        val saridaireMerkezY = saridaireY + binding.saridaire.height/2.0f

        if (0.0f <= saridaireMerkezX && saridaireMerkezX <= anakarakterGenisligi
            && anakarakterY <= saridaireMerkezY && saridaireMerkezY <= anakarakterY+anakarakterYuksekligi){
            score+=20
            saridaireX = -10.0f
        }

        val kirmziucgenMerkezX = kirmiziucgenX + binding.kirmiziucgen.width/2.0f
        val kirmziucgenMerkezY = kirmiziucgenY + binding.kirmiziucgen.height/2.0f

        if (0.0f <= kirmziucgenMerkezX && kirmziucgenMerkezX <= anakarakterGenisligi
            && anakarakterY <= kirmziucgenMerkezY && kirmziucgenMerkezY <= anakarakterY + anakarakterYuksekligi) {
            score += 50
            kirmiziucgenX = -10.0f
        }

        val siyahkareMerkezX = siyahkareX + binding.siyahkare.width/2.0f
        val siyahkareMerkezY = siyahkareY + binding.siyahkare.height/2.0f

        if (0.0f <= siyahkareMerkezX && siyahkareMerkezX <= anakarakterGenisligi
            && anakarakterY <= siyahkareMerkezY && siyahkareMerkezY <= anakarakterY + anakarakterYuksekligi) {
            siyahkareX = -10.0f

            timer.cancel()//Timer durdur.

            val direction=GameScreenFragmentDirections.fromGameToResult(binding.textViewScore.text.toString().toInt())
            findNavController().navigate(direction)
//            findNavController().navigate(R.id.fromGameToResult)
        }
        binding.textViewScore.text = score.toString()
    }
}