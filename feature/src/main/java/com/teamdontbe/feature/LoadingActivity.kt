package com.teamdontbe.feature

import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivityLoadingBinding
import kotlin.random.Random

class LoadingActivity : BindingActivity<ActivityLoadingBinding>(R.layout.activity_loading) {
    override fun initView() {
        initLoadingContent()
    }

    private fun initLoadingContent() {
        val loadingContents = resources.getStringArray(R.array.loading_contents)
        val randomIndex = Random.nextInt(loadingContents.size)
        val randomContent = loadingContents[randomIndex]

        binding.tvLoadingContent.text = randomContent
    }
}