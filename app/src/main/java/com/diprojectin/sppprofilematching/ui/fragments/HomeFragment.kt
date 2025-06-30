package com.diprojectin.sppprofilematching.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diprojectin.sppprofilematching.databinding.FragmentHomeBinding
import com.diprojectin.sppprofilematching.ui.admin.kelas.MasterKelasActivity
import com.diprojectin.sppprofilematching.ui.admin.siswa.MasterSiswaActivity
import com.diprojectin.sppprofilematching.utils.SharedPrefManager

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = SharedPrefManager(requireActivity())
        initView()
    }

    private fun initView() {
        with(binding){
            tvName.text = prefManager.getUser()?.nama
            btnMasterSiswa.setOnClickListener {
                val intent = Intent(requireActivity(), MasterSiswaActivity::class.java)
                startActivity(intent)
            }

            btnMasterKelas.setOnClickListener {
                val intent = Intent(requireActivity(), MasterKelasActivity::class.java)
                startActivity(intent)
            }
        }
    }

}