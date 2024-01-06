package com.teamdontbe.feature.home

import android.content.Intent
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeBinding
import com.teamdontbe.feature.example.ExampleActivity

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun initView() {
        initExampleBtnClickLister()
    }

    private fun initExampleBtnClickLister() {
        binding.btnHomeExample.setOnClickListener {
            startActivity(Intent(requireContext(), ExampleActivity::class.java))
        }
    }
}
