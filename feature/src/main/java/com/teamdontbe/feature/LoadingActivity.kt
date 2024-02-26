package com.teamdontbe.feature

import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivityLoadingBinding
import kotlin.random.Random

class LoadingActivity : BindingActivity<ActivityLoadingBinding>(R.layout.activity_loading) {

    lateinit var fadeOutAnim: Animation

    override fun initView() {
        initLoadingContent()

        initAnimation()
    }

    private fun initAnimation() {
        Handler().postDelayed({
            fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.anim_loading_fade_out)
            fadeOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    // 애니메이션이 끝난 후에 액티비티를 종료
                    binding.layoutLoading.visibility = View.GONE
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.layoutLoading.startAnimation(fadeOutAnim)
        }, 1800)
    }

    private fun initLoadingContent() {
        val loadingContents = resources.getStringArray(R.array.loading_contents)
        val randomIndex = Random.nextInt(loadingContents.size)
        val randomContent = loadingContents[randomIndex]

        binding.tvLoadingContent.text = randomContent
    }
}
