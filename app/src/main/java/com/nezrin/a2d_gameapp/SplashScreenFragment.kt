package com.nezrin.a2d_gameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nezrin.a2d_gameapp.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment() {
private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSplashScreenBinding.inflate(inflater,container,false)

        binding.buttonStart.setOnClickListener{
            val navigation=SplashScreenFragmentDirections.fromSplashToGame()
            Navigation.findNavController(it).navigate(navigation)
        }
        return binding.root
    }


}