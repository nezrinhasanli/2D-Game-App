package com.nezrin.a2d_gameapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.nezrin.a2d_gameapp.databinding.FragmentGameScreenBinding
import com.nezrin.a2d_gameapp.databinding.FragmentResultScreenBinding

class ResultScreenFragment : Fragment() {
    private lateinit var binding: FragmentResultScreenBinding
    private val args:ResultScreenFragmentArgs by navArgs()
    var score=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultScreenBinding.inflate(inflater, container, false)

        //getting total score
        score=args.totalScore
        binding.textViewTotalScore.text=score.toString()

        //getting highest score
        val preferences= requireActivity().applicationContext.getSharedPreferences("Result",Context.MODE_PRIVATE)
        val highestScore=preferences.getInt("highestScore",0)

        if (score>highestScore){
            val editor=preferences.edit()
            editor.putInt("highestScore",score)
            editor.commit() //saving score

            binding.textViewHighestScore.text=score.toString()

        }else{
            binding.textViewHighestScore.text=highestScore.toString()
        }

        binding.buttonTryAgain.setOnClickListener{
            val navigation=ResultScreenFragmentDirections.fromResultToSplash()
            Navigation.findNavController(it).navigate(navigation)
        }
        return binding.root
    }

}