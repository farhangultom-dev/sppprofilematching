package com.diprojectin.sppprofilematching.ui.siswa.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaProfileBinding
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaSppBinding

class SiswaSppFragment : Fragment() {
    private lateinit var binding: FragmentSiswaSppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSiswaSppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}