package com.jgdeveloppement.android_academie.interview

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.jgdeveloppement.android_academie.databinding.FragmentCodeViewBinding
import com.squareup.picasso.Picasso

class CodeViewFragment : Fragment() {

    private var _binding: FragmentCodeViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val args: CodeViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCodeViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        Picasso.get()
            .load(args.imageUrl)
            .into(binding.imageViewCode)
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        (activity as AppCompatActivity).supportActionBar?.setLogo(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


}